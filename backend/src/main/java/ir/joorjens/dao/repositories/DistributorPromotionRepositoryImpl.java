package ir.joorjens.dao.repositories;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorPromotionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorPromotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributorPromotionRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorPromotion>
        implements DistributorPromotionRepository {

    @Override
    public FetchResult<DistributorPromotion> search(long id, int fromPrice, int toPrice
            , long distributorId, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM DistributorPromotion t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        PromotionRepositoryImpl.setQueryField(query, map, id, fromPrice, toPrice, block);
        if(distributorId > 0) {
            query.append(" AND t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        return search(query.toString(), map, max, offset);
    }

    @Override
    public DistributorPromotion findByAmount(long distributorId, int buyingAmount) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM DistributorPromotion t WHERE " + PromotionRepositoryImpl.getWhereByAmount());
        final Map<String, Object> map = new HashMap<>();
        map.put("buyingAmount", buyingAmount);
        if(distributorId > 0) {
            query.append(" AND t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        return findBy(query.toString(), map);
    }

    @Override
    public DistributorPromotion findToAmount(long distributorId, int buyingAmount) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM DistributorPromotion t WHERE " + PromotionRepositoryImpl.getWhereToAmount());
        final Map<String, Object> map = new HashMap<>();
        map.put("buyingAmount", buyingAmount);
        if(distributorId > 0) {
            query.append(" AND t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        return findBy(query.toString(), map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DistributorPromotion> getAll(long distributorId) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM DistributorPromotion t where 1=1");
        final Map<String, Object> map = new HashMap<>();
        if(distributorId > 0) {
            query.append(" AND t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        return findAllBy(query.toString(), map);
    }

}