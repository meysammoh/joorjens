package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductPriceHistoryRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ProductPriceHistory;

import java.util.HashMap;
import java.util.Map;

public class ProductPriceHistoryRepositoryImpl
        extends RepositoryImpAbstract<Long, ProductPriceHistory>
        implements ProductPriceHistoryRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<ProductPriceHistory> search(long id, long productId, String barcode
            , int priceFrom, int priceTo, int timeFrom, int timeTo
            , int max, int offset) throws JoorJensException {

        final StringBuilder sb = new StringBuilder("SELECT %s FROM ProductPriceHistory t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }

        if (priceFrom > 0) {
            sb.append(" and t.price>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            sb.append(" and t.price<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (timeFrom > 0) {
            sb.append(" and t.createdTime>=:timeFrom");
            map.put("timeFrom", timeFrom);
        }
        if (timeTo > 0 && timeTo >= timeFrom) {
            sb.append(" and t.createdTime<=:timeTo");
            map.put("timeTo", timeTo);
        }

        if (productId > 0) {
            sb.append(" and t.product.id=:productId");
            map.put("productId", productId);
        }
        if(!Utility.isEmpty(barcode)) {
            sb.append(" and t.product.barcode=:barcode");
            map.put("barcode", barcode);
        }

        return search(sb.toString(), map, max, offset);
    }

}