package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CartDistributorPackductRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.businessEntity.CartFavoriteInfo;
import ir.joorjens.model.entity.CartDistributorPackduct;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.*;

public class CartDistributorPackductRepositoryImpl
        extends RepositoryImpAbstract<Long, CartDistributorPackduct>
        implements CartDistributorPackductRepository {

    @Override
    public List<CartFavoriteInfo> favorite(final long storeManagerId
            , final long productId, final String productBarcode, final String productName
            , final int priceFrom, final int priceTo, final Boolean onlyBundling, final Boolean supportCheck
            , final boolean onlyStocks, final boolean typesAreParent
            , final Set<Long> distributorIds, final Set<Long> productBrandTypeIds, final Set<Long> productCategoryTypeIds
            , final int orderTypeId, final boolean asc
            , final boolean like, final int max, final int offset) throws JoorJensException {
        final StringBuilder where = new StringBuilder("t.cartDistributor.cart.store.manager.id=:storeManagerId");
        final Map<String, Object> map = new HashMap<>();
        map.put("storeManagerId", storeManagerId);
        if (productId > 0) {
            where.append(" and t.distributorProduct.product.id=:productId");
            map.put("productId", productId);
        }
        if (!Utility.isEmpty(productBarcode)) {
            where.append(" and t.distributorProduct.product.barcode=:productBarcode");
            map.put("productBarcode", productBarcode);
        }
        if (!Utility.isEmpty(productName)) {
            where.append(" and t.distributorProduct.product.name").append(like ? " like " : "=").append(":productName");
            map.put("productName", like ? '%' + productName + '%' : productName);
        }
        if (priceFrom > 0) {
            where.append(" and t.distributorProduct.price>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            where.append(" and t.distributorProduct.price<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (onlyBundling != null) {
            where.append(" and t.distributorProduct.onlyBundling=:onlyBundling");
            map.put("onlyBundling", onlyBundling);
        }
        if (supportCheck != null) {
            where.append(" and t.distributorProduct.supportCheck=:supportCheck");
            map.put("supportCheck", supportCheck);
        }
        if (onlyStocks) {
            where.append(" and t.distributorProduct.stock>=t.maxOrder");
        }

        if (distributorIds != null && distributorIds.size() > 0) {
            where.append(" and t.distributorProduct.distributor.id in (:distributorIds)");
            map.put("distributorIds", distributorIds);
        }
        if (productBrandTypeIds != null && productBrandTypeIds.size() > 0) {
            where.append(" and t.distributorProduct.product.productBrandType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productBrandTypeIds)");
            map.put("productBrandTypeIds", productBrandTypeIds);
        }
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            where.append(" and t.distributorProduct.product.productCategoryType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productCategoryTypeIds)");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
        }

        final String orderField;
        if (TypeEnumeration.DB_OT_PRICE.getId() == orderTypeId) {
            orderField = "sum(t.cartPrice.packPriceDiscount)";
        } else { //default if(TypeEnumeration.DB_OT_COUNT.getId() == orderTypeId) {
            orderField = "sum(t.cartPrice.count)";
        }

        final String query = String.format("SELECT" +
                " t.distributorProduct.id,t.distributorProduct.distributor.id,t.distributorProduct.product.id" +
                ",t.distributorProduct.distributor.name,t.distributorProduct.product.name" +
                ",t.distributorProduct.product.barcode,t.distributorProduct.product.image" +
                ",%s FROM CartDistributorPackduct t" +
                " WHERE %s" +
                " group by t.distributorProduct.id" +
                " order by %s %s", orderField, where.toString(), orderField, (asc ? " asc" : " desc"));
        final List all = findAllBy(query, map, max, offset);

        final List<CartFavoriteInfo> result = new ArrayList<>();
        for (Object obj : all) {
            result.add(new CartFavoriteInfo(obj));
        }
        return result;
    }

    @Override
    public FetchResult<CartDistributorPackduct> search(final long id, final String serial
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , final Boolean finished
            , final long storeId, String storeName
            , final long storeManagerId, final String storeManagerMobile, final long distributorId
            , final int type, final long distributorPackageId, final long distributorProductPackageId
            , final long productId, final String productBarcode
            , final boolean like, final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM CartDistributorPackduct t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(serial)) {
            query.append(" and t.cartDistributor.cart.serial=:serial");
            map.put("serial", serial);
        }

        CartRepositoryImpl.setFields(query, map, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, countFrom, countTo
                , packCountFrom, packCountTo, packPriceConsumerFrom, packPriceConsumerTo
                , packPriceFrom, packPriceTo, packPriceDiscountFrom, packPriceDiscountTo, finished);

        if (storeId > 0) {
            query.append(" and t.cartDistributor.cart.store.id=:storeId");
            map.put("storeId", storeId);
        }
        if (!Utility.isEmpty(storeName)) {
            query.append(" and t.cartDistributor.cart.store.name").append(like ? " like " : "=").append(":storeName");
            map.put("storeName", like ? '%' + storeName + '%' : storeName);
        }
        if (storeManagerId > 0) {
            query.append(" and t.cartDistributor.cart.store.manager.id=:storeManagerId");
            map.put("storeManagerId", storeManagerId);
        }
        if (!Utility.isEmpty(storeManagerMobile)) {
            query.append(" AND t.cartDistributor.cart.store.manager.mobileNumber=:storeManagerMobile");
            map.put("storeManagerMobile", storeManagerMobile);
        }
        if (distributorId > 0) {
            query.append(" and t.cartDistributor.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }

        if (type > 0) {
            query.append(" and t.type=:type");
            map.put("type", type);
            if (productId > 0) {
                if (TypeEnumeration.CPT_PRODUCT.getId() == type) {
                    if (productId > 0) {
                        query.append(" and t.distributorProductPackage.distributorProduct.product.id=:productId");
                        map.put("productId", productId);
                    }
                    if (!Utility.isEmpty(productBarcode)) {
                        query.append(" and t.distributorProductPackage.distributorProduct.product.barcode=:productBarcode");
                        map.put("productBarcode", productBarcode);
                    }
                } else if (TypeEnumeration.CPT_PACKAGE.getId() == type) {
                    if (productId > 0) {
                        query.append(" and t.distributorPackage.id in (select distinct d.distributorPackage.id from DistributorPackageProduct d" +
                                " where d.distributorProduct.product.id=:productId)");
                        map.put("productId", productId);
                    }
                    if (!Utility.isEmpty(productBarcode)) {
                        query.append(" and t.distributorPackage.id in (select distinct d.distributorPackage.id from DistributorPackageProduct d" +
                                " where d.distributorProduct.product.barcode=:productBarcode)");
                        map.put("productBarcode", productBarcode);
                    }
                }
            }
        }
        if (distributorPackageId > 0) {
            query.append(" and t.distributorPackage.id=:distributorPackageId");
            map.put("distributorPackageId", distributorPackageId);
        }
        if (distributorProductPackageId > 0) {
            query.append(" and t.distributorProductPackage.id=:distributorProductPackageId");
            map.put("distributorProductPackageId", distributorProductPackageId);
        }

        return search(query.toString(), map, max, offset);
    }

}