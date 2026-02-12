package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.businessEntity.DashboardDistributorProductInfo;
import ir.joorjens.model.entity.DistributorProduct;

import java.util.List;
import java.util.Set;

public interface DistributorProductRepository extends Repository<Long, DistributorProduct> {

    int update(long id, int price, int priceMin) throws JoorJensException;

    FetchResult<DistributorProduct> search(long id, Set<Long> distributorIds, long productId
            , String productBarcode, String productName, int priceFrom, int priceTo, Boolean onlyBundling
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , Boolean supportCheck, Set<Long> supportAreaIds
            , boolean typesAreParent, boolean onlyStocks
            , String orderTypeIds, String ascDescs, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Third<Long, String, String>> getAllPairs(long id, Set<Long> distributorIds, long productId
            , String productBarcode, String productName, int priceFrom, int priceTo, Boolean onlyBundling
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , Boolean supportCheck, Set<Long> supportAreaIds
            , boolean typesAreParent, boolean onlyStocks
            , String orderTypeIds, String ascDescs, boolean like, Boolean block) throws JoorJensException;

    FetchResult<DashboardDistributorProductInfo> dashboard(long distributorId
            , Boolean onlyBundling, Boolean supportCheck
            , long productId, String productBarcode
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds, boolean typesAreParent
            , int max, int offset) throws JoorJensException;
}