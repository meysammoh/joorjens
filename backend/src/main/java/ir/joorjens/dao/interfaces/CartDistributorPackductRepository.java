package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.businessEntity.CartFavoriteInfo;
import ir.joorjens.model.entity.CartDistributorPackduct;

import java.util.List;
import java.util.Set;

public interface CartDistributorPackductRepository extends Repository<Long, CartDistributorPackduct> {

    List<CartFavoriteInfo> favorite(long storeManagerId
            , long productId, String productBarcode, String productName
            , int priceFrom, int priceTo, Boolean onlyBundling, Boolean supportCheck
            , boolean onlyStocks, boolean typesAreParent
            , Set<Long> distributorIds, Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , int orderTypeId, boolean asc
            , boolean like, int max, int offset) throws JoorJensException;

    FetchResult<CartDistributorPackduct> search(long id, String serial
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , Boolean finished
            , long storeId, String storeName
            , long storeManagerId, String storeManagerMobile, long distributorId
            , int type, long distributorPackageId, long distributorProductPackageId
            , final long productId, final String productBarcode
            , boolean like, int max, int offset) throws JoorJensException;
}