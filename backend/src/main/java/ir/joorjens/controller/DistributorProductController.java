package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.*;
import ir.joorjens.dao.interfaces.DistributorProductPackageRepository;
import ir.joorjens.dao.interfaces.DistributorProductRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.businessEntity.DashboardDistributorProductInfo;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DistributorProductController {

    //-------------------------------------------------------------------------------------------------
    private static final DistributorProductRepository REPO_DIS_PRO = (DistributorProductRepository) RepositoryManager.getByEntity(DistributorProduct.class);
    private static final DistributorProductPackageRepository REPO_DIS_PRO_PACK = (DistributorProductPackageRepository) RepositoryManager.getByEntity(DistributorProductPackage.class);
    private static final FormFactory<DistributorProduct> FORM = new FormFactory<>(DistributorProduct.class);
    //-------------------------------------------------------------------------------------------------

    public DistributorProductController() {
        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, DistributorProduct.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_PAIR.getUrl(), (req, res) -> search(req, false, true), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, DistributorProduct.class, id, block);
        }, json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_PRICE.getUrl() + ":dpId/:price/", (req, res) -> {
            final long distributorProductId = Long.parseLong(req.params(":dpId"));
            final int price = Integer.parseInt(req.params(":price"));
            return updatePrice(req, distributorProductId, -1, price, price, true);
        }, json());
    }

    static Object upsert(final Request req, final boolean update) throws JoorJensException {
        final DistributorProduct distributorProduct = FORM.get(req.body());
        final int prePrice;
        if (update && distributorProduct.getId() > 0) { //update
            final DistributorProduct distributorProductPre = getDistributorProduct(distributorProduct.getId());
            distributorProduct.setEdit(distributorProductPre);
            prePrice = distributorProductPre.getPrice();
        } else {
            prePrice = 0;
        }
        final Product product = ProductController.getProduct(distributorProduct.getProductId());
        distributorProduct.setProduct(product);
        final Distributor distributor = DistributorController.getDistributor(distributorProduct.getDistributorId());
        distributorProduct.setDistributor(distributor);

        for (DistributorProductPackage dpp : distributorProduct.getPackages()) {
            dpp.setFields(dpp, distributorProduct);
            dpp.isValid();
        }
        for (DistributorProductDiscount dpd : distributorProduct.getDiscounts()) {
            dpd.setDistributorProduct(distributorProduct);
            dpd.isValid();
        }

        AbstractController.upsert(req, DistributorProduct.class, distributorProduct, update);
        updatePrice(req, distributorProduct.getId(), prePrice, distributorProduct.getPrice(), distributorProduct.getPriceMin(), update);
        return distributorProduct;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getDistributorProduct(id);
        }

        final int priceFrom = (queryMap.containsKey("pricefrom")) ? Integer.parseInt(queryMap.get("pricefrom")) : 0;
        final int priceTo = (queryMap.containsKey("priceto")) ? Integer.parseInt(queryMap.get("priceto")) : 0;
        final Boolean onlyBundling = (queryMap.containsKey("onlybundling")) ? Boolean.parseBoolean(queryMap.get("onlybundling")) : null;
        final String productBarcode = queryMap.get("productbarcode");
        final String productName = queryMap.get("productname");
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final Set<Long> settlementTypeIds = Utility.getSetLong(queryMap.get("settlementtypeids"), ",") //
                , supportAreaIds = Utility.getSetLong(queryMap.get("supportareaids"), ",")//
                , productBrandTypeIds = Utility.getSetLong(queryMap.get("productbrandtypeid"), ",")//
                , productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeid"), ",")//
                , distributorIds = Utility.getSetLong(queryMap.get("distributorid"), ",");
        final boolean typesAreParent = (queryMap.containsKey("typesareparent")) && Boolean.parseBoolean(queryMap.get("typesareparent"));
        final Boolean supportCheck = (queryMap.containsKey("supportcheck")) ? Boolean.parseBoolean(queryMap.get("supportcheck")) : null;
        final boolean onlyStocks = (queryMap.containsKey("onlystocks")) && Boolean.parseBoolean(queryMap.get("onlystocks"));
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_DIS_PRO.getAllPairs(id, distributorIds, productId, productBarcode, productName
                    , priceFrom, priceTo, onlyBundling, productBrandTypeIds, productCategoryTypeIds
                    , supportCheck, supportAreaIds, typesAreParent, onlyStocks
                    , orderTypeIds, ascDescs, like, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        final FetchResult<DistributorProduct> result = REPO_DIS_PRO.search(id, distributorIds, productId, productBarcode, productName
                , priceFrom, priceTo, onlyBundling, productBrandTypeIds, productCategoryTypeIds
                , supportCheck, supportAreaIds, typesAreParent, onlyStocks
                , orderTypeIds, ascDescs, like, block, max, offset);
        Pair<Integer, Integer> minMaxOrder = new Pair<>(Integer.MAX_VALUE, Integer.MIN_VALUE);
        for (DistributorProduct dp : result.getResult()) {
            if (dp.getMinOrder() < minMaxOrder.getFirst()) {
                minMaxOrder.setFirst(dp.getMinOrder());
            }
            if (dp.getMaxOrder() > minMaxOrder.getSecond()) {
                minMaxOrder.setSecond(dp.getMaxOrder());
            }
        }
        result.setInfo(minMaxOrder);
        return result;
    }

    private static ResponseMessage updatePrice(final Request req, long distributorProductId, int prePrice
            , int newPrice, int newPriceMin, boolean update) throws JoorJensException {
        if(update && prePrice < 0) {
            final DistributorProduct distributorProduct = getDistributorProduct(distributorProductId);
            prePrice = distributorProduct.getPrice();
            newPriceMin = distributorProduct.calculatePriceMin(newPrice);
        }
        if(update && prePrice == newPrice) {
            return new ResponseMessage(ResponseCode.DONE_NO_CHANGE);
        }
        final DistributorProductPriceHistory dpph = new DistributorProductPriceHistory(distributorProductId, newPrice, newPriceMin);
        AbstractController.upsert(req, DistributorProductPriceHistory.class, dpph, false);
        if(update) {
            final int rowAffected = REPO_DIS_PRO.update(distributorProductId, newPrice, newPriceMin);
            if (1 != rowAffected) {
                throw new JoorJensException(ExceptionCode.EXCEPTION);
            }
        }
        return new ResponseMessage(String.format(ResponseCode.DONE_UPDATE.getMessage(), DistributorProduct.getEN()));
    }
    //-------------------------------------------------------------------------------------------------

    static DistributorProduct getDistributorProduct(final long id) throws JoorJensException {
        final Optional<DistributorProduct> dpOptional = REPO_DIS_PRO.getByKey(id);
        if (!dpOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorProduct.getEN());
        }
        return dpOptional.get();
    }

    static DistributorProductPackage getDistributorProductPackage(final long id) throws JoorJensException {
        final Optional<DistributorProductPackage> dppOptional = REPO_DIS_PRO_PACK.getByKey(id);
        if (!dppOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorProductPackage.getEN());
        }
        return dppOptional.get();
    }

    static FetchResult<DashboardDistributorProductInfo> dashboard(final Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final Boolean onlyBundling = (queryMap.containsKey("onlybundling")) ? Boolean.parseBoolean(queryMap.get("onlybundling")) : null;
        final Boolean supportCheck = (queryMap.containsKey("supportcheck")) ? Boolean.parseBoolean(queryMap.get("supportcheck")) : null;
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final String productBarcode = queryMap.get("productbarcode");
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        distributorId = CartController.getDistributorId(req, distributorId, false);
        final Set<Long> productBrandTypeIds = Utility.getSetLong(queryMap.get("productbrandtypeid"), ",")//
                , productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeid"), ",");
        final boolean typesAreParent = (queryMap.containsKey("typesareparent")) && Boolean.parseBoolean(queryMap.get("typesareparent"));
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DIS_PRO.dashboard(distributorId, onlyBundling, supportCheck
                , productId, productBarcode, productBrandTypeIds, productCategoryTypeIds, typesAreParent, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
}