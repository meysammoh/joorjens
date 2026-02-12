package ir.joorjens.dao.repositories;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorRateDiscontentRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorRateDiscontent;

import java.util.HashMap;
import java.util.Map;

public class DistributorRateDiscontentRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorRateDiscontent>
        implements DistributorRateDiscontentRepository {

    @Override
    public DistributorRateDiscontent find(final long cartId, final long distributorId) throws JoorJensException {
        final String query = "SELECT t FROM DistributorRateDiscontent t" +
                " WHERE t.cart.id=:cartId and t.distributor.id=:distributorId";
        final Map<String, Object> map = new HashMap<>();
        map.put("cartId", cartId);
        map.put("distributorId", distributorId);
        return findBy(query, map);
    }

    @Override
    public FetchResult<DistributorRateDiscontent> search(long id
            , final long cartId, final long distributorId, final long customerId
            , final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM DistributorRateDiscontent t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (cartId > 0) {
            query.append(" and t.cart.id=:cartId");
            map.put("cartId", cartId);
        }
        if (distributorId > 0) {
            query.append(" and t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        if (customerId > 0) {
            query.append(" and t.cart.store.manager.id=:customerId");
            map.put("customerId", customerId);
        }
        return search(query.toString(), map, max, offset);
    }
}