package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ProductPriceHistory;

public interface ProductPriceHistoryRepository extends Repository<Long, ProductPriceHistory> {

    FetchResult<ProductPriceHistory> search(long id, long productId, String barcode
            , int priceFrom, int priceTo, int timeFrom, int timeTo
            , int max, int offset) throws JoorJensException;

}