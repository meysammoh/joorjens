package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.VehicleBrandTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.VehicleBrandType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleBrandTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, VehicleBrandType>
        implements VehicleBrandTypeRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<VehicleBrandType> search(long id, String name
            , int capacityFrom, int capacityTo, boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, capacityFrom, capacityTo, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, int capacityFrom, int capacityTo
            , boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, capacityFrom, capacityTo, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<VehicleBrandType> findAll() throws JoorJensException {
        return findAllBy("SELECT t FROM VehicleBrandType t", null);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name
            , int capacityFrom, int capacityTo, boolean like, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM VehicleBrandType t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (capacityFrom > 0) {
            query.append(" and t.capacity>=:capacityFrom");
            map.put("capacityFrom", capacityFrom);
        }
        if (capacityTo > 0 && capacityTo >= capacityFrom) {
            query.append(" and t.capacity<=:capacityTo");
            map.put("capacityTo", capacityTo);
        }
        return new Pair<>(query.toString(), map);
    }
}