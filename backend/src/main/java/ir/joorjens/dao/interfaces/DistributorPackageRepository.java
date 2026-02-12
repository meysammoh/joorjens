package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorPackage;

import java.util.Set;

public interface DistributorPackageRepository extends Repository<Long, DistributorPackage> {

    FetchResult<DistributorPackage> search(long id, Set<Long> distributorIds
            , long productId, String productBarcode, String productName, String packageName
            , Boolean bundlingOrDiscount, int from , int to
            , Boolean supportCheck, boolean onlyStocks, Boolean expired
            , String orderTypeIds, String ascDescs
            , boolean like, Boolean block, int max, int offset) throws JoorJensException;

    long getProductCountIn(long productId, String productBarcode, Boolean bundlingOrDiscount, Boolean block) throws JoorJensException;
}