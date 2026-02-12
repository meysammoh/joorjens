package ir.joorjens.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.background.StocksUpdater;
import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.CartDistributorPackductRepository;
import ir.joorjens.dao.interfaces.CartRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.interfaces.DistributorPackductInterface;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class CartController {

    //-------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    private static final TypeReference<List<Long>> TR_LIST_LONG = new TypeReference<List<Long>>() {
    };
    private static final CartRepository REPO_CART = (CartRepository) RepositoryManager.getByEntity(Cart.class);
    private static final CartDistributorPackductRepository REPO_CART_PACKDUCT = (CartDistributorPackductRepository) RepositoryManager.getByEntity(CartDistributorPackduct.class);
    private static final FormFactory<CartDistributorPackduct> FORM = new FormFactory<>(CartDistributorPackduct.class);
    //-------------------------------------------------------------------------------------------------

    public CartController() {

        get(Config.API_PREFIX + UrlRolesType.CART_GET.getUrl(), (req, res) -> myCart(req), json());
        delete(Config.API_PREFIX + UrlRolesType.CART_CLEAR.getUrl(), (req, res) -> clear(req), json());
        post(Config.API_PREFIX + UrlRolesType.CART_ADD.getUrl(), (req, res) -> upsert(req, true), json());
        delete(Config.API_PREFIX + UrlRolesType.CART_REMOVE.getUrl(), (req, res) -> upsert(req, false), json());
        post(Config.API_PREFIX + UrlRolesType.CART_FINALIZE.getUrl(), (req, res) -> insert(req), json());

        get(Config.API_PREFIX + UrlRolesType.CART_SEARCH.getUrl(), (req, res) -> search(req, false), json());
        get(Config.API_PREFIX + UrlRolesType.CART_VIEW.getUrl(), (req, res) -> search(req, true), json());
        get(Config.API_PREFIX + UrlRolesType.CART_FAVORITE.getUrl(), (req, res) -> favorite(req), json());

        post(Config.API_PREFIX + UrlRolesType.CART_UPDATE.getUrl() + ":orderStatusTypeId/:cartDistPackductId/", (req, res) -> {
            final int orderStatusTypeId = Integer.parseInt(req.params(":orderStatusTypeId"));
            final long cartDistPackductId = Long.parseLong(req.params(":cartDistPackductId"));
            return updateCartDisPackduct(req, orderStatusTypeId, cartDistPackductId);
        }, json());
        post(Config.API_PREFIX + UrlRolesType.CART_UPDATE_BATCH_PACKDUCT.getUrl() + ":orderStatusTypeId/", (req, res) -> {
            final int orderStatusTypeId = Integer.parseInt(req.params(":orderStatusTypeId"));
            return updateCartDisPackductBatch(req, orderStatusTypeId);
        }, json());
        post(Config.API_PREFIX + UrlRolesType.CART_UPDATE_BATCH_DIST.getUrl() + ":orderStatusTypeId/:cartDistId/", (req, res) -> {
            final int orderStatusTypeId = Integer.parseInt(req.params(":orderStatusTypeId"));
            final long cartDistId = Long.parseLong(req.params(":cartDistId"));
            return updateCartDistBatch(req, orderStatusTypeId, cartDistId);
        }, json());
    }

    //-------------------------------------------------------------------------------------------------

    private static Cart myCart(final Request req) throws JoorJensException {
        final Cart cart = CacheGuava.getCart(req);
        if(cart == null) {
            throw new JoorJensException(ExceptionCode.CART_EMPTY);
        }
        return cart;
    }

    private static ResponseMessage clear(final Request req) throws JoorJensException {
        CacheGuava.invalidateCart(req);
        return new ResponseMessage();
    }

    private static Cart upsert(final Request req, final boolean add) throws JoorJensException {
        Cart cart = CacheGuava.getCart(req);
        if (cart == null) {
            if (add) {
                final Customer customer = AAA.getCustomer(req);
                final Store store = StoreController.getStoreByManager(customer.getId());
                cart = new Cart(store);
                CacheGuava.putCart(req, cart);
            } else {
                throw new JoorJensException(ExceptionCode.CART_EMPTY);
            }
        }

        final CartDistributorPackduct cdp = FORM.get(req.body());
        cdp.isValid();
        if (cdp.isOrdinal()) {
            final DistributorProduct dp = DistributorProductController.getDistributorProduct(cdp.getDistributorProductId());
            cdp.setDistributorProduct(dp);
        } else if (cdp.isPackage()) {
            final DistributorPackage dp = DistributorPackageController.getDistributorPackage(cdp.getDistributorPackageId());
            cdp.setDistributorPackage(dp);
        }
        if (cdp.getOrderStatusTypeId() > 0) {
            final OrderStatusType os = OrderStatusTypeController.getOrderStatusType(cdp.getOrderStatusTypeId());
            cdp.setOrderStatusType(os);
        }
        isValid(cdp);

        final int size = cart.update(cdp, add);
        if (size == 0) {
            throw new JoorJensException(ExceptionCode.CART_EMPTY);
        }
        cart.setPromotionMessage(PromotionController.getPromotionMessage((int) cart.getCartPrice().getAllPriceDiscount()));
        return cart;
    }

    private static Cart insert(final Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final boolean useCredit = (queryMap.containsKey("usecredit")) && Boolean.parseBoolean(queryMap.get("usecredit"));
        final Customer customer = AAA.getCustomer(req);
        final Cart cart = myCart(req);

        //-------------------------------------------------
        final CartPrice cartPrice = cart.getCartPrice();
        Customer customerCredit = null;
        int credit = 0;
        //load and use credit
        Transaction transactionCredit = null;
        if (useCredit) {
            customerCredit = CustomerController.getCustomer(customer.getId());
            if (customerCredit.getCredit() > 0 && cartPrice.getAmountCache() > 0) {
                final long creditDecrease = Math.min(cartPrice.getAmountCache(), customerCredit.getCredit());
                credit -= ((int) creditDecrease);
                cartPrice.setAmountCredit(creditDecrease);
                transactionCredit = new Transaction(creditDecrease, cart, false);
                updateDistributorCredit(cart);
            }
        }
        //load and calculate promotion credit
        Transaction transactionPromotion = null;
        final int buyingAmount = (int) cartPrice.getAmountCache();//AllPriceDiscount();
        final Promotion promotion = PromotionController.getPromotionToAmount(buyingAmount);
        if (promotion != null) {
            if (customerCredit == null) {
                customerCredit = CustomerController.getCustomer(customer.getId());
            }
            credit += promotion.getCredit();
            cart.setPromotionCredit(promotion.getCredit());
            transactionPromotion = new Transaction(promotion.getCredit(), cart, true);
        }
        //-------------------------------------------------

        //-------------------------------------------------
        insert(req, cart);
        //todo insert transactions
        StocksUpdater.addToQueue(cart);
        CacheGuava.invalidateCart(req);
        //-------------------------------------------------

        //-------------------------------------------------
        //updateCartDisPackduct user credit
        final boolean creditChanged = transactionCredit != null || transactionPromotion != null;
        if (creditChanged) {
            if (transactionCredit != null) {
                transactionCredit.updateCartNote();
                AbstractController.upsert(req, Transaction.class, transactionCredit, false);
            }
            if (transactionPromotion != null) {
                transactionPromotion.updateCartNote();
                AbstractController.upsert(req, Transaction.class, transactionPromotion, false);
            }
            if(credit != 0) {
                CustomerController.updateCredit(customerCredit, credit);
                customer.setCredit(customerCredit.getCredit());
            }
        }
        //-------------------------------------------------

        return cart;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        final String serial = queryMap.get("serial");
        if (view) {
            if (id > 0) {
                return getCart(id);
            } else {
                return getCart(serial);
            }
        }
        final int timeFrom = (queryMap.containsKey("timefrom")) ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        final int timeTo = (queryMap.containsKey("timeto")) ? Integer.parseInt(queryMap.get("timeto")) : 0;
        final int timeFinishedFrom = (queryMap.containsKey("timefinishedfrom")) ? Integer.parseInt(queryMap.get("timefinishedfrom")) : 0;
        final int timeFinishedTo = (queryMap.containsKey("timefinishedto")) ? Integer.parseInt(queryMap.get("timefinishedto")) : 0;
        final int countFrom = (queryMap.containsKey("countfrom")) ? Integer.parseInt(queryMap.get("countfrom")) : 0;
        final int countTo = (queryMap.containsKey("countto")) ? Integer.parseInt(queryMap.get("countto")) : 0;
        final int packCountFrom = (queryMap.containsKey("packcountfrom")) ? Integer.parseInt(queryMap.get("packcountfrom")) : 0;
        final int packCountTo = (queryMap.containsKey("packcountto")) ? Integer.parseInt(queryMap.get("packcountto")) : 0;
        final int packPriceConsumerFrom = (queryMap.containsKey("packpriceconsumerfrom")) ? Integer.parseInt(queryMap.get("packpriceconsumerfrom")) : 0;
        final int packPriceConsumerTo = (queryMap.containsKey("packpriceconsumerto")) ? Integer.parseInt(queryMap.get("packpriceconsumerto")) : 0;
        final int packPriceFrom = (queryMap.containsKey("packpricefrom")) ? Integer.parseInt(queryMap.get("packpricefrom")) : 0;
        final int packPriceTo = (queryMap.containsKey("packpriceto")) ? Integer.parseInt(queryMap.get("packpriceto")) : 0;
        final int packPriceDiscountFrom = (queryMap.containsKey("packpricediscountfrom")) ? Integer.parseInt(queryMap.get("packpricediscountfrom")) : 0;
        final int packPriceDiscountTo = (queryMap.containsKey("packpricediscountto")) ? Integer.parseInt(queryMap.get("packpricediscountto")) : 0;
        final Boolean finished = (queryMap.containsKey("finished")) ? Boolean.parseBoolean(queryMap.get("finished")) : null;
        final long storeId = (queryMap.containsKey("storeid")) ? Long.parseLong(queryMap.get("storeid")) : 0;
        final String storeName = queryMap.get("storename");
        final long storeManagerId = queryMap.containsKey("customerid") ? Long.parseLong(queryMap.get("customerid")) : 0;
        final String storeManagerMobile = queryMap.get("customermobile");
        final Pair<Long, String> idMobile = CartController.getCustomerIdMobile(req, storeManagerId, storeManagerMobile);
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_CART.search(id, serial, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, countFrom, countTo, packCountFrom, packCountTo
                , packPriceConsumerFrom, packPriceConsumerTo, packPriceFrom, packPriceTo
                , packPriceDiscountFrom, packPriceDiscountTo, finished
                , storeId, storeName, idMobile.getFirst(), idMobile.getSecond(), like, max, offset);
    }

    private static Object favorite(final Request req) throws JoorJensException {
        final long customerId = AAA.getCustomerId(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final int priceFrom = (queryMap.containsKey("pricefrom")) ? Integer.parseInt(queryMap.get("pricefrom")) : 0;
        final int priceTo = (queryMap.containsKey("priceto")) ? Integer.parseInt(queryMap.get("priceto")) : 0;
        final Boolean onlyBundling = (queryMap.containsKey("onlybundling")) ? Boolean.parseBoolean(queryMap.get("onlybundling")) : null;
        final String productBarcode = queryMap.get("productbarcode");
        final String productName = queryMap.get("productname");
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final Set<Long> settlementTypeIds = Utility.getSetLong(queryMap.get("settlementtypeids"), ",") //
                , productBrandTypeIds = Utility.getSetLong(queryMap.get("productbrandtypeid"), ",")//
                , productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeid"), ",")//
                , distributorIds = Utility.getSetLong(queryMap.get("distributorid"), ",");
        final boolean typesAreParent = (queryMap.containsKey("typesareparent")) && Boolean.parseBoolean(queryMap.get("typesareparent"));
        final Boolean supportCheck = (queryMap.containsKey("supportcheck")) ? Boolean.parseBoolean(queryMap.get("supportcheck")) : null;
        final boolean onlyStocks = (queryMap.containsKey("onlystocks")) && Boolean.parseBoolean(queryMap.get("onlystocks"));
        final int orderTypeId = (queryMap.containsKey("ordertypeid")) ? Integer.parseInt(queryMap.get("ordertypeid")) : 0;
        final boolean asc = (queryMap.containsKey("asc")) && Boolean.parseBoolean(queryMap.get("asc"));
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_CART_PACKDUCT.favorite(customerId, productId, productBarcode, productName
                , priceFrom, priceTo, onlyBundling, supportCheck, onlyStocks, typesAreParent
                , distributorIds, productBrandTypeIds, productCategoryTypeIds
                , orderTypeId, asc, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------

    private static void updateDistributorCredit(final Cart cart) {
        CartPrice cartPrice = cart.getCartPrice();
        final long allPrice = cartPrice.getAllPriceDiscount();
        final long allCredit = cartPrice.getAmountCredit();
        long credit, remainedCredit = allCredit;
        float percent;
        int count = cart.getDistributorSize();
        for (CartDistributor cartDistributor: cart.getDistributorSet()) {
            cartPrice = cartDistributor.getCartPrice();
            if (--count == 0) {
                cartPrice.setAmountCredit(remainedCredit);  // خطای رند شدن درصدها میمونه برای آخری!
            } else {
                percent = Utility.getPercent(allPrice, cartPrice.getAllPriceDiscount());
                credit = (long) Utility.getPrice(allCredit, percent);
                remainedCredit -= credit;
                cartPrice.setAmountCredit(credit);
            }
        }
    }

    private static Object updateCartDisPackduct(final Request req, final int orderStatusTypeId, final long cartDistPackductId) throws JoorJensException {
        final CartDistributorPackduct cartDistributorPackduct = getCartDistributorPackduct(cartDistPackductId);
        updateCartDistributorPackductStatus(cartDistributorPackduct, orderStatusTypeId);
        AbstractController.upsert(req, CartDistributorPackduct.class, cartDistributorPackduct, true);

        final Map<String, Object> result = updateCartStatus(req, cartDistributorPackduct.getCartDistributor());
        result.put("cartDistributorPackduct", cartDistributorPackduct.isFinished());
        result.put("doneCount", 1);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Object updateCartDisPackductBatch(final Request req, final int orderStatusTypeId) throws JoorJensException {
        CartDistributor cartDistributor = null;
        final List<Long> cartDistPackductIdSet = (List<Long>) Utility.fromJson(req.body(), TR_LIST_LONG);
        final List<CartDistributorPackduct> list = new ArrayList<>();
        for (Long cartDistPackductId : cartDistPackductIdSet) {
            final CartDistributorPackduct cartDistributorPackduct = getCartDistributorPackduct(cartDistPackductId);
            if(cartDistributor == null) {
                cartDistributor = cartDistributorPackduct.getCartDistributor();
                if (cartDistributor.isFinished()) {
                    throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_COMPLETED, CartDistributor.getEN());
                }
            }
            try {
                updateCartDistributorPackductStatus(cartDistributorPackduct, orderStatusTypeId);
                list.add(cartDistributorPackduct);
                //AbstractController.upsert(req, CartDistributorPackduct.class, cartDistributorPackduct, true);
            } catch (JoorJensException e) {
                LOGGER.debug(String.format("@updateBatchCartDistributor. cartDistributorPackduct(%d), Message: %s", cartDistributorPackduct.getId(), e.getMessage()));
            }
        }

        final Map<String, Object> result = new HashMap<>();
        if (list.size() > 0 && cartDistributor != null) {
            REPO_CART_PACKDUCT.updateBatch(list);
            result.putAll(updateCartStatus(req, cartDistributor));
        }
        result.put("doneCount", list.size());

        return result;
    }

    private static Object updateCartDistBatch(final Request req, final int orderStatusTypeId, final long cartDistId) throws JoorJensException {
        final CartDistributor cartDistributor = CartDistributorController.getCartDistributor(cartDistId);
        if (cartDistributor.isFinished()) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_COMPLETED, CartDistributor.getEN());
        }

        final List<CartDistributorPackduct> list = new ArrayList<>();
        for (CartDistributorPackduct cartDistributorPackduct : cartDistributor.getPackageSet()) {
            try {
                updateCartDistributorPackductStatus(cartDistributorPackduct, orderStatusTypeId);
                list.add(cartDistributorPackduct);
                //AbstractController.upsert(req, CartDistributorPackduct.class, cartDistributorPackduct, true);
            } catch (JoorJensException e) {
                LOGGER.debug(String.format("@updateBatchCartDistributor. cartDistributorPackduct(%d), Message: %s", cartDistributorPackduct.getId(), e.getMessage()));
            }
        }

        final Map<String, Object> result = new HashMap<>();
        if (list.size() > 0) {
            REPO_CART_PACKDUCT.updateBatch(list);
            result.putAll(updateCartStatus(req, cartDistributor));
        }
        result.put("doneCount", list.size());

        return result;
    }

    private static void updateCartDistributorPackductStatus(final CartDistributorPackduct cartDistributorPackduct
            , final int orderStatusTypeId) throws JoorJensException {
        if(cartDistributorPackduct.isFinished()) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_COMPLETED, CartDistributorPackduct.getEN());
        }
        //todo check permissrion
        boolean OK = isStateOK((int) cartDistributorPackduct.getOrderStatusTypeId(), orderStatusTypeId);
        if(!OK) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_NOT_OK, CartDistributorPackduct.getEN());
        }
        cartDistributorPackduct.setOrderStatusTypeId(orderStatusTypeId);
        if(TypeEnumeration.OS_DELIVERED.getId() == orderStatusTypeId) {
            cartDistributorPackduct.setTimeFinished(Utility.getCurrentTime());
        }
    }

    private static Map<String, Object> updateCartStatus(final Request req, final CartDistributor cartDistributor) throws JoorJensException {
        final Cart cart = cartDistributor.getCart();
        boolean pre = cartDistributor.setFinished();
        if(pre != cartDistributor.isFinished()) {
            AbstractController.upsert(req, CartDistributor.class, cartDistributor, true);
            pre = cart.setFinished();
            if(pre != cart.isFinished()) {
                AbstractController.upsert(req, Cart.class, cart, true);
            }
        }
        final Map<String, Object> result = new HashMap<>();
        result.put("cart", cart.isFinished());
        result.put("cartDistributor", cartDistributor.isFinished());
        return result;
    }

    //-------------------------------------------------------------------------------------------------

    private static Cart insert(final Request req, final Cart cart) throws JoorJensException {
        int counter = 0;
        do {
            try {
                cart.setKey();
                AbstractController.upsert(req, Cart.class, cart, false);
            } catch (JoorJensException e) {
                if (e.getResponseMessage().getCode() == ExceptionCode.UK_DISTRIBUTOR_SERIAL.getErrorCode()) {
                    ++counter;
                } else {
                    LOGGER.error(String.format("Exception@insert. Message: %s", e.getMessage()));
                    throw new JoorJensException(ExceptionCode.EXCEPTION);
                }
            }
        } while (cart.getId() == 0);
        if (counter > 0) {
            LOGGER.warn(String.format("@insert: inserting unique serial exception: %d", counter));
        }
        return cart;
    }

    private static void isValid(final CartDistributorPackduct cdp) throws JoorJensException {
        final long count = cdp.getCartPrice().getCount();

        final DistributorPackductInterface dpi;
        if(cdp.isOrdinal()) {
            dpi = cdp.getDistributorProduct();
        } else if (cdp.isPackage()) {
            final DistributorPackage dp = cdp.getDistributorPackage();
            dpi = dp;
            if(!dp.isValidTime()) {
                throw new JoorJensException(ExceptionCode.CART_PACK_TIME);
            }
        } else {
            LOGGER.warn(String.format("@isValid: cart type is not defined!: %d", cdp.getType()));
            return;
        }

        if (dpi.getMinOrder() > 0 && count < dpi.getMinOrder()) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_MIN_ORDER, dpi.getMinOrder());
        }
        if (dpi.getMaxOrder() > 0 && count > dpi.getMaxOrder()) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_MAX_ORDER, dpi.getMaxOrder());
        }
        if (count > dpi.getStock()) {
            throw new JoorJensException(ExceptionCode.CART_DISTRIBUTOR_STOCK);
        }
    }

    private static boolean isStateOK(final int orderStatusTypeIdFrom, final int orderStatusTypeIdTo) {
        final TypeEnumeration orderStatusTypeFrom = TypeEnumeration.get(orderStatusTypeIdFrom);
        final TypeEnumeration orderStatusTypeTo = TypeEnumeration.get(orderStatusTypeIdTo);
        boolean OK = false;
        switch (orderStatusTypeFrom) {
            case OS_RECEIVED:
                switch (orderStatusTypeTo) {
                    case OS_APPROVE:
                        OK = true;
                        break;
                }
                break;
            case OS_APPROVE:
                switch (orderStatusTypeTo) {
                    case OS_PAYED:
                        OK = true;
                        break;
                }
                break;
            case OS_PAYED:
                switch (orderStatusTypeTo) {
                    case OS_PROCESSED:
                        OK = true;
                        break;
                }
                break;
            case OS_PROCESSED:
                switch (orderStatusTypeTo) {
                    case OS_READY:
                        OK = true;
                        break;
                }
                break;
            case OS_READY:
                switch (orderStatusTypeTo) {
                    case OS_POST:
                        OK = true;
                        break;
                }
                break;
            case OS_POST:
                switch (orderStatusTypeTo) {
                    case OS_DELIVERED:
                        OK = true;
                        break;
                }
                break;
        }
        return OK;
    }

    //-------------------------------------------------------------------------------------------------

    static Cart getCart(final long id) throws JoorJensException {
        final Optional<Cart> cartOptional = REPO_CART.getByKey(id);
        if (!cartOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Cart.getEN());
        }
        return cartOptional.get();
    }

    static Cart getCart(final String serial) throws JoorJensException {
        if (Utility.isEmpty(serial)) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Cart.getEN());
        }
        final Cart cart = REPO_CART.find(serial);
        if (cart == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Cart.getEN());
        }
        return cart;
    }

    static CartDistributorPackduct getCartDistributorPackduct(final long id) throws JoorJensException {
        final Optional<CartDistributorPackduct> cartOptional = REPO_CART_PACKDUCT.getByKey(id);
        if (!cartOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, CartDistributorPackduct.getEN());
        }
        return cartOptional.get();
    }

    //-------------------------------------------------------------------------------------------------

    static Pair<Long, String> getCustomerIdMobile(final Request request, final long customerId, final String customerMobile) throws JoorJensException {
        final Pair<Long, String> idMobile = new Pair<>();
        final Customer customer = AAA.getCustomer(request);
        switch (customer.getRoleType()) {
            case UR_CENTRAL_ADMIN:
            case UR_CENTRAL_CONTROLLER:
            case UR_CENTRAL_OPERATOR:
            case UR_CENTRAL_ACCOUNTANT:
            case UR_CENTRAL_SUPPORTER:
                idMobile.setFirst(customerId);
                idMobile.setSecond(customerMobile);
                break;
            case UR_DISTRIBUTION_ADMIN:
            case UR_DISTRIBUTION_CONTROLLER:
            case UR_DISTRIBUTION_OPERATOR:
            case UR_DISTRIBUTION_DELIVERER:
                //todo throw new JoorJensException(ExceptionCode.OP_FORBIDDEN); ?!
                idMobile.setFirst(customerId);
                idMobile.setSecond(customerMobile);
                break;
            case UR_CUSTOMER:
            case UR_STORE_ADMIN:
            default:
                idMobile.setFirst(customer.getId());
                idMobile.setSecond(customer.getMobileNumber());
                break;
        }
        return idMobile;
    }

    static long getDistributorId(final Request request, long distributorId, boolean hasCustomerInfo) throws JoorJensException {
        final Customer customer = AAA.getCustomer(request);
        switch (customer.getRoleType()) {
            case UR_CENTRAL_ADMIN:
            case UR_CENTRAL_CONTROLLER:
            case UR_CENTRAL_OPERATOR:
            case UR_CENTRAL_ACCOUNTANT:
            case UR_CENTRAL_SUPPORTER:
                break;
            case UR_DISTRIBUTION_ADMIN:
                /*final Distributor distributor = DistributorController.getDistributor(customer.getId(), null, null);
                distributorId = distributor != null ? distributor.getId() : -1;
                break;
                */
            case UR_DISTRIBUTION_CONTROLLER:
            case UR_DISTRIBUTION_OPERATOR:
            case UR_DISTRIBUTION_DELIVERER:
                final DistributorEmployee distributorEmployee = DistributorEmployeeController.getDistributorEmployee(customer.getId(), null, null);
                distributorId = distributorEmployee != null ? distributorEmployee.getDistributorId() : -1;
                break;
            case UR_CUSTOMER:
            case UR_STORE_ADMIN:
            default:
                distributorId = hasCustomerInfo ? 0 : -1;
                break;
        }

        if (distributorId < 0) {
            throw new JoorJensException(ExceptionCode.OP_FORBIDDEN_FILTER);
        }

        return distributorId;
    }

    static Object dashboard(final Request req, final boolean saleOrOrder) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final int timeFrom = (queryMap.containsKey("timefrom")) ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        final int timeTo = (queryMap.containsKey("timeto")) ? Integer.parseInt(queryMap.get("timeto")) : 0;
        final int timeFinishedFrom = (queryMap.containsKey("timefinishedfrom")) ? Integer.parseInt(queryMap.get("timefinishedfrom")) : 0;
        final int timeFinishedTo = (queryMap.containsKey("timefinishedto")) ? Integer.parseInt(queryMap.get("timefinishedto")) : 0;
        final int timeStampId = (queryMap.containsKey("timestampid")) ? Integer.parseInt(queryMap.get("timestampid")) : 0;
        final Boolean finished = (queryMap.containsKey("finished")) ? Boolean.parseBoolean(queryMap.get("finished")) : null;
        final boolean byArea = (queryMap.containsKey("byarea")) && Boolean.parseBoolean(queryMap.get("byarea"));
        final boolean byDistributor = (queryMap.containsKey("bydistributor")) && Boolean.parseBoolean(queryMap.get("bydistributor"));
        final Set<Long> areaIds = Utility.getSetLong(queryMap.get("areaids"), ",");
        final long productId = (queryMap.containsKey("productid")) ? Integer.parseInt(queryMap.get("productid")) : 0;
        final String productBarcode = queryMap.get("productbarcode");
        final long disProductId = (queryMap.containsKey("disProductid")) ? Integer.parseInt(queryMap.get("disProductid")) : 0;
        final long disPackageId = (queryMap.containsKey("disPackageid")) ? Integer.parseInt(queryMap.get("disPackageid")) : 0;
        long distributorId = (queryMap.containsKey("distributorid")) ? Integer.parseInt(queryMap.get("distributorid")) : 0;
        final long storeId = (queryMap.containsKey("storeid")) ? Integer.parseInt(queryMap.get("storeid")) : 0;
        final long storeManagerId = queryMap.containsKey("customerid") ? Long.parseLong(queryMap.get("customerid")) : 0;
        final String storeManagerMobile = queryMap.get("customermobile");
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;

        final Pair<Long, String> idMobile = CartController.getCustomerIdMobile(req, storeManagerId, storeManagerMobile);
        final boolean hasCustomerInfo = idMobile.getFirst() != null && idMobile.getSecond() != null;
        distributorId = CartController.getDistributorId(req, distributorId, hasCustomerInfo);

        if(saleOrOrder) {
            return REPO_CART.dashboardSales(timeStampId, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, finished, byArea, byDistributor, areaIds, productId, productBarcode
                    , disProductId, disPackageId, distributorId, storeId, idMobile.getFirst(), idMobile.getSecond(), max, offset);
        } else {
            return REPO_CART.dashboardOrders(timeStampId, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, finished, byArea, byDistributor, areaIds, productId, productBarcode
                    , disProductId, disPackageId, distributorId, storeId, idMobile.getFirst(), idMobile.getSecond(), max, offset);
        }
    }

    //-------------------------------------------------------------------------------------------------
}