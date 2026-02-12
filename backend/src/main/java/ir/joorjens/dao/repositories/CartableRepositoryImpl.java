package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CartableRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Cartable;

import java.util.HashMap;
import java.util.Map;

public class CartableRepositoryImpl
        extends RepositoryImpAbstract<Long, Cartable>
        implements CartableRepository {

    @Override
    public boolean isExist(final int status, final int type, final String key) throws JoorJensException {
        final String query = "SELECT %s FROM Cartable t WHERE t.status=:status and t.type=:type and t.key=:key";
        final Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("type", type);
        map.put("key", key);
        final FetchResult<Cartable> result = search(query, map, 1, 0);
        return result != null && result.getResultSize() > 0;
    }

    @Override
    public FetchResult<Cartable> search(long id, int status, int type, String key, int priority, Boolean toJoorJens
            , int createdTimeFrom, int createdTimeTo, int doneTimeFrom, int doneTimeTo
            , long fromCustomerId, long toCustomerId, long donnerCustomerId, long toDistributorId
            , Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Cartable t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        if (status > 0) {
            query.append(" and t.status=:status");
            map.put("status", status);
        }
        if (type > 0) {
            query.append(" and t.type=:type");
            map.put("type", type);
        }
        if (!Utility.isEmpty(key)) {
            query.append(" and t.key=:key");
            map.put("key", key);
        }
        if (priority > 0) {
            query.append(" and t.priority=:priority");
            map.put("priority", priority);
        }
        if (toJoorJens != null) {
            query.append(" and t.toJoorJens=:toJoorJens");
            map.put("toJoorJens", toJoorJens);
        }

        if (createdTimeFrom > 0) {
            query.append(" and t.createdTime>=:createdTimeFrom");
            map.put("createdTimeFrom", createdTimeFrom);
        }
        if (createdTimeTo > 0 && createdTimeTo >= createdTimeFrom) {
            query.append(" and t.createdTime<=:createdTimeTo");
            map.put("createdTimeTo", createdTimeTo);
        }
        if (doneTimeFrom > 0) {
            query.append(" and t.doneTime>=:doneTimeFrom");
            map.put("doneTimeFrom", doneTimeFrom);
        }
        if (doneTimeTo > 0 && doneTimeTo >= doneTimeFrom) {
            query.append(" and t.doneTime<=:doneTimeTo");
            map.put("doneTimeTo", doneTimeTo);
        }

        if (fromCustomerId > 0) {
            query.append(" and t.fromCustomer.id=:fromCustomerId");
            map.put("fromCustomerId", fromCustomerId);
        }
        if (toCustomerId > 0) {
            query.append(" and t.toCustomer.id=:toCustomerId");
            map.put("toCustomerId", toCustomerId);
        }
        if (donnerCustomerId > 0) {
            query.append(" and t.donnerCustomer.id=:donnerCustomerId");
            map.put("donnerCustomerId", donnerCustomerId);
        }
        if (toDistributorId > 0) {
            query.append(" and t.toDistributor.id=:toDistributorId");
            map.put("toDistributorId", toDistributorId);
        }

        return search(query.toString(), map, max, offset);
    }
}