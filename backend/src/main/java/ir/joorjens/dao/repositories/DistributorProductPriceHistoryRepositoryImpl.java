package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorProductPriceHistoryRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorProductPriceHistory;

import java.util.HashMap;
import java.util.Map;

public class DistributorProductPriceHistoryRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorProductPriceHistory>
        implements DistributorProductPriceHistoryRepository {

    @Override
    public FetchResult<DistributorProductPriceHistory> search(long id
            , long distributorProductId, long distributorId, long productId, String barcode
            , int priceFrom, int priceTo, int timeFrom, int timeTo, int max, int offset) throws JoorJensException {
        final StringBuilder sb = new StringBuilder("SELECT %s FROM DistributorProductPriceHistory t WHERE 1=1");
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

        if (distributorProductId > 0) {
            sb.append(" and t.distributorProduct.id=:distributorProductId");
            map.put("distributorProductId", distributorProductId);
        }
        if (distributorId > 0) {
            sb.append(" and t.distributorProduct.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        if (productId > 0) {
            sb.append(" and t.distributorProduct.product.id=:productId");
            map.put("productId", productId);
        }
        if(!Utility.isEmpty(barcode)) {
            sb.append(" and t.distributorProduct.product.barcode=:barcode");
            map.put("barcode", barcode);
        }

        return search(sb.toString(), map, max, offset);
    }

}