package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.CartDistributorRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.CartDistributor;
import ir.joorjens.model.entity.DistributorDeliverer;
import ir.joorjens.model.entity.OrderStatusType;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class CartDistributorController {

    //-------------------------------------------------------------------------------------------------
    private static final CartDistributorRepository REPO_CART_DIST = (CartDistributorRepository) RepositoryManager.getByEntity(CartDistributor.class);
    //-------------------------------------------------------------------------------------------------

    public CartDistributorController() {
        post(Config.API_PREFIX + UrlRolesType.CART_DIST_UPDATE_DELIVERER.getUrl() + ":cartDistId/:distDelivererId/", (req, res) -> {
            final long cartDistId = Long.parseLong(req.params(":cartDistId"));
            final long distDelivererId = Long.parseLong(req.params(":distDelivererId"));
            return updateDeliverer(cartDistId, distDelivererId);
        }, json());
        get(Config.API_PREFIX + UrlRolesType.CART_DIST_SEARCH.getUrl(), (req, res) -> search(req, false), json());
    }

    //-------------------------------------------------------------------------------------------------

    private static Object updateDeliverer(final long cartDistId, final long distDelivererId) throws JoorJensException {
        final CartDistributor cartDistributor = getCartDistributor(cartDistId);
        if (cartDistributor.isFinished()) {
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_COMPLETED, CartDistributor.getEN());
        }
        final OrderStatusType orderStatus = OrderStatusTypeController.getOrderStatusType(TypeEnumeration.OS_READY.getId());
        final Map<Long, Integer> orderStatusMap = cartDistributor.getOrderStatusMap();
        if(!orderStatusMap.containsKey(orderStatus.getId())) { // آماده ارسال
            throw JoorJensException.getInstanceByFormat(ExceptionCode.CART_DIST_DELIVERER, orderStatus.getName());
        }
        final DistributorDeliverer distributorDeliverer = DistributorDelivererController.getDistributorDeliverer(distDelivererId);
        if(distributorDeliverer.isBanded() || distributorDeliverer.isBlock()) {
            throw new JoorJensException(ExceptionCode.OP_USER_BANDED);
        }
        if (1 != REPO_CART_DIST.updateDeliverer(cartDistributor.getId(), distributorDeliverer.getId())) {
            throw new JoorJensException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
        return new ResponseMessage();
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        final String serial = queryMap.get("serial");
        if (view) {
            if (id > 0) {
                return getCartDistributor(id);
            } else {
                return getCartDistributor(serial);
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
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        final long storeId = (queryMap.containsKey("storeid")) ? Long.parseLong(queryMap.get("storeid")) : 0;
        final String storeName = queryMap.get("storename");
        final long storeManagerId = queryMap.containsKey("customerid") ? Long.parseLong(queryMap.get("customerid")) : 0;
        final String storeManagerMobile = queryMap.get("customermobile");
        final long delivererId = (queryMap.containsKey("delivererid")) ? Long.parseLong(queryMap.get("delivererid")) : 0;
        final String delivererMobile = queryMap.get("deliverermobile");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;

        final Pair<Long, String> idMobile = CartController.getCustomerIdMobile(req, storeManagerId, storeManagerMobile);
        final boolean hasCustomerInfo = idMobile.getFirst() != null && idMobile.getSecond() != null;
        distributorId = CartController.getDistributorId(req, distributorId, hasCustomerInfo);
        return REPO_CART_DIST.search(id, serial, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, countFrom, countTo, packCountFrom, packCountTo
                , packPriceConsumerFrom, packPriceConsumerTo, packPriceFrom, packPriceTo
                , packPriceDiscountFrom, packPriceDiscountTo, finished
                , storeId, storeName, idMobile.getFirst(), idMobile.getSecond(), distributorId
                , delivererId, delivererMobile, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------

    static CartDistributor getCartDistributor(final long id) throws JoorJensException {
        final Optional<CartDistributor> cartOptional = REPO_CART_DIST.getByKey(id);
        if (!cartOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, CartDistributor.getEN());
        }
        return cartOptional.get();
    }

    static CartDistributor getCartDistributor(final String serial) throws JoorJensException {
        if (Utility.isEmpty(serial)) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, CartDistributor.getEN());
        }
        final CartDistributor cartDistributor = REPO_CART_DIST.find(serial);
        if (cartDistributor == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, CartDistributor.getEN());
        }
        return cartDistributor;
    }

    //-------------------------------------------------------------------------------------------------

}