package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.background.LogTask;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.file.FileRW;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.ProductRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private static final ProductRepository REPO_PRODUCT = (ProductRepository) RepositoryManager.getByEntity(Product.class);
    private static final FormFactory<Product> FORM = new FormFactory<>(Product.class);

    public ProductController() {

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_INSERT.getUrl(), (req, res) -> upsert(req, null, false), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_UPDATE.getUrl(), (req, res) -> upsert(req, null, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PRODUCT_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            // return AbstractController.delete(req, Product.class, id);
            return remove(req, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_PAIR.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_EXIST.getUrl(), (req, res) -> search(req, false, false, true), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Product.class, id, block);
        }, json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_PRICE.getUrl() + ":id/:price/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final int price = Integer.parseInt(req.params(":price"));
            return updatePrice(req, id, -1, price, true);
        }, json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_PRICE_BATCH.getUrl(), (req, res) -> updatePriceBatch(req), json());
    }

    static Object upsert(final Request req, final String preBody, final boolean update) throws JoorJensException {
        final Customer fromCustomer = AAA.getCustomer(req);
        final Product product = FORM.get(req.body());
        final boolean fromCartable = (preBody != null);
        final Product productPre;
        if (fromCartable) {
            productPre = FORM.get(preBody);//body from cartable
        } else if (update && product.getId() > 0) { //update
            productPre = getProduct(product.getId());
            product.setEdit(productPre);
        } else {
            productPre = null;
        }

        final ProductBrandType productBrandType;
        if(product.getProductBrandTypeId() > 0 || Utility.isEmpty(product.getProductBrandTypeNameTyped())) {
            productBrandType = ProductBrandTypeController.getProductBrandType(product.getProductBrandTypeId());
        } else {
            productBrandType = null;
        }
        final ProductCategoryType productCategoryType = ProductCategoryTypeController.getProductCategoryType(product.getProductCategoryTypeId());
        checkDetails(product, productCategoryType);
        product.isValid();

        final boolean canSaveDirectly = canSaveDirectly(fromCustomer);
        saveImages(product, productPre, fromCartable, canSaveDirectly);

        if (canSaveDirectly) {
            product.setProductBrandType(productBrandType);
            product.setProductCategoryType(productCategoryType);
            AbstractController.upsert(req, Product.class, product, update);
            final int prePrice = (productPre != null) ? productPre.getPriceConsumer() : 0;
            updatePrice(req, product.getId(), prePrice, product.getPriceConsumer(), update);
            return product;
        } else {
            final int taskTypeId = (update) ? TypeEnumeration.TT_UPDATE_PRODUCT.getId() : TypeEnumeration.TT_INSERT_PRODUCT.getId();
            final Cartable cartable = new Cartable(product.getBarcode(), taskTypeId, TypeEnumeration.PRT_HIGH.getId()
                    , AbstractController.getQueryString(req, null), Utility.toJson(product), Product.getEN(), fromCustomer);
            if (CartableController.isExist(cartable)) {
                throw new JoorJensException(ExceptionCode.CUSTOMER_IN_CARTABLE);
            }
            AbstractController.upsert(req, Cartable.class, cartable, update);
            return new ResponseMessage(String.format(ResponseCode.CARTABLE_ADDED_PRODUCT.getMessage()
                    , fromCustomer.getName(), product.getName()));
        }
    }

    private static ResponseMessage remove(final Request req, final long id) throws JoorJensException {
        //return AbstractController.delete(req, Product.class, id);
        final Product product = getProduct(id);
        final String className = Product.class.getSimpleName();
        final long customerId = AAA.getCustomerIdForLog(req);
        REPO_PRODUCT.executeQueries(
                "delete from ProductPriceHistory t where t.product.id=" + id
                , "delete from ProductDetail t where t.product.id=" + id
                , "delete from Product t where t.id=" + id
        );
        LogTask.addLog(new Log(customerId, TypeEnumeration.ACTION_DELETE.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                , className, String.format("id=%d, url=%s", id, Utility.getRequestInfo(req, true))
                , Utility.toJson(product)));
        LOGGER.info(String.format("DELETE(%d) request was done: %s", id, Utility.getRequestInfo(req, false)));
        return new ResponseMessage(String.format(ResponseCode.DONE_DELETE.getMessage(), Product.getEN()));
    }

    private static Object search(final Request req, final boolean view, final boolean pair, final boolean exist) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getProduct(id);
        }
        final String barcode = queryMap.get("barcode");
        if (exist) {
            return getProduct(barcode);
        }

        final String name = queryMap.get("name");
        final int priceFrom = (queryMap.containsKey("pricefrom")) ? Integer.parseInt(queryMap.get("pricefrom")) : 0;
        final int priceTo = (queryMap.containsKey("priceto")) ? Integer.parseInt(queryMap.get("priceto")) : 0;
        final float rateFrom = (queryMap.containsKey("ratefrom")) ? Float.parseFloat(queryMap.get("ratefrom")) : 0;
        final float rateTo = (queryMap.containsKey("rateto")) ? Float.parseFloat(queryMap.get("rateto")) : 0;
        final Set<Long> productBrandTypeIds = Utility.getSetLong(queryMap.get("productbrandtypeid"), ",")//
                , productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeid"), ",");
        final boolean typesAreParent = (queryMap.containsKey("typesareparent")) && Boolean.parseBoolean(queryMap.get("typesareparent"));
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean blockedByDistributor = queryMap.containsKey("ifproductblocked") ? Boolean.parseBoolean(queryMap.get("ifproductblocked")) : null;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final Boolean firstPage = queryMap.containsKey("firstpage") && Boolean.parseBoolean(queryMap.get("firstpage"));
        if (pair) {
            return REPO_PRODUCT.getAllPairs(id, barcode, name, priceFrom, priceTo, rateFrom, rateTo
                    , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                    , orderTypeIds, ascDescs, firstPage, like, blockedByDistributor, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PRODUCT.search(id, barcode, name, priceFrom, priceTo, rateFrom, rateTo
                , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                , orderTypeIds, ascDescs, firstPage, like, blockedByDistributor, block, max, offset);
    }

    private static ResponseMessage updatePrice(final Request req, long productId, int prePrice, int newPrice, boolean update) throws JoorJensException {
        if(update && prePrice < 0) {
            final Product product = getProduct(productId);
            prePrice = product.getPriceConsumer();
        }
        if(update && prePrice == newPrice) {
            return new ResponseMessage(ResponseCode.DONE_NO_CHANGE);
        }
        final ProductPriceHistory pph = new ProductPriceHistory(productId, newPrice);
        AbstractController.upsert(req, ProductPriceHistory.class, pph, false);
        if(update) {
            final int rowAffected = REPO_PRODUCT.update(productId, newPrice);
            if (1 != rowAffected) {
                throw new JoorJensException(ExceptionCode.EXCEPTION);
            }
        }
        return new ResponseMessage(String.format(ResponseCode.DONE_UPDATE.getMessage(), Product.getEN()));
    }

    private static ResponseMessage updatePriceBatch(final Request req) throws JoorJensException {
        final List<Pair<String, Integer>> barcodePriceList = new ArrayList<>();
        //----- analyse csv input
        final String[] lines = req.body().split("\\r?\\n");
        int counter = 0;
        for (String line: lines) {
            ++counter;
            final String[] barcodePrice = line.split("\\s*,\\s*");
            if (barcodePrice.length == 2) {
                try {
                    barcodePriceList.add(new Pair<>(barcodePrice[0].trim(), Integer.parseInt(barcodePrice[1].trim())));
                } catch (NumberFormatException e) {
                    throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM
                            , String.format("قیمت موجود در ورودی(سطر %d)", counter));
                }
            } else {
                throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM
                        , String.format("فایل(سطر %d)", counter));
            }
        }
        //----
        final int rowSize = barcodePriceList.size();
        final int rowAffected = REPO_PRODUCT.update(barcodePriceList);
        final ResponseMessage responseMessage = new ResponseMessage(String.format(ResponseCode.DONE_UPDATE.getMessage(), Product.getEN()));
        responseMessage.setData(new Pair<>(rowSize, rowAffected));
        return responseMessage;
    }

    //-------------------------------------------------------------------------------------------------
    static Product getProduct(final long id) throws JoorJensException {
        final Optional<Product> productOptional = REPO_PRODUCT.getByKey(id);
        if (!productOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Product.getEN());
        }
        return productOptional.get();
    }

    static Product getProduct(final String barcode) throws JoorJensException {
        if (Utility.isEmpty(barcode)) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Product.getEN());
        }
        final Product product = REPO_PRODUCT.find(barcode);
        if (product == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Product.getEN());
        }
        return product;
    }

    private static void checkDetails(final Product product, final ProductCategoryType productCategoryType) throws JoorJensException {
        final Set<Long> details = new HashSet<>();
        final Set<Long> detailsMandatory = new HashSet<>();

        //itself and parent details ----------------------------
        final Set<ProductCategoryDetailType> productDetailTypes = new HashSet<>(productCategoryType.getProductDetailTypes());
        ProductCategoryType pctParent = productCategoryType.getParent();
        while (pctParent != null && !pctParent.isFake()) {
            productDetailTypes.addAll(pctParent.getProductDetailTypes());
            pctParent = pctParent.getParent();
        }
        //-------------------------------------------------------
        //find mandatories --------------------------------------
        for (ProductCategoryDetailType pcdt : productDetailTypes) {
            details.add(pcdt.getProductDetailTypeId());
            if (pcdt.isMandatory()) {
                detailsMandatory.add(pcdt.getProductDetailTypeId());
            }
        }// details of category
        //-------------------------------------------------------
        //check details -----------------------------------------
        for (ProductDetail pd : product.getProductDetails()) {
            if (!details.contains(pd.getProductDetailTypeId())) {
                final ProductDetailType pdt = ProductDetailTypeController.getProductDetailType(pd.getProductDetailTypeId());
                throw new JoorJensException(ExceptionCode.PRODUCT_DETAIL_UNDEFINED, pdt.getName());
            }
            pd.setFields(product);
            pd.isValid();
            details.remove(pd.getProductDetailTypeId());
            detailsMandatory.remove(pd.getProductDetailTypeId());
        }
        if (detailsMandatory.size() > 0) {
            throw new JoorJensException(ExceptionCode.PRODUCT_DETAIL_MANDATORY);
        }
        //-------------------------------------------------------
    }

    static boolean canSaveDirectly(final Customer customer) throws JoorJensException {
        return customer.getRoleId() == TypeEnumeration.UR_CENTRAL_ADMIN.getId()
                || customer.getRoleId() == TypeEnumeration.UR_CENTRAL_OPERATOR.getId()
                || customer.getRoleId() == TypeEnumeration.UR_CENTRAL_CONTROLLER.getId();
    }

    static void saveImages(final Product product, final Product productPre, final boolean confirmCartable, final boolean insertDirectly) throws JoorJensException {
        //create cat path
        final ProductCategoryType cat = product.getProductCategoryType();
        final long catId;
        if(cat != null) {
            catId = cat.isFistLevel() ? cat.getId() : cat.getParentId();
        } else {
            catId = ProductCategoryType.getFakeId();
        }
        final String path = Config.baseFolderProduct + catId + "/";
        FileRW.mkdir(path, true, false);
        //--------------------------------

        if (confirmCartable) {
            product.setImage(ImageRW.confirmImage(path, product.getImage(), productPre.getImage(), false, true, true));
        } else {
            final String imageAddress;
            if(insertDirectly) {
                imageAddress = path + product.getBarcode();
            } else {
                imageAddress = path + Config.TEMPORARY_PREFIX + product.getBarcode();
            }
            product.setImage(ImageRW.saveImage(path, product.getImage(), imageAddress, (productPre != null ? productPre.getImage() : null), false, true, true));
        }
        //TODO add another images for product
    }

    static void deleteTempImages(String body) throws JoorJensException {
        final Product product = FORM.get(body);
        ImageRW.deleteTempImage(product.getImage());
    }

    //-------------------------------------------------------------------------------------------------
}