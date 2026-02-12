package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorProductPriceHistory;

public interface DistributorProductPriceHistoryRepository extends Repository<Long, DistributorProductPriceHistory> {

    FetchResult<DistributorProductPriceHistory> search(long id
            , long distributorProductId, long distributorId, long productId, String barcode
            , int priceFrom, int priceTo, int timeFrom, int timeTo, int max, int offset) throws JoorJensException;

}