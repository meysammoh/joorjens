package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.OrderStatusTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.OrderStatusType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderStatusTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, OrderStatusType>
        implements OrderStatusTypeRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<OrderStatusType> search(long id, String name, boolean like, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, like, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, boolean like) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, like, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderStatusType> findAll() throws JoorJensException {
        return findAllBy("SELECT t FROM OrderStatusType t", null);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, boolean like, boolean pair) {
        final StringBuilder sb = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            sb.append("SELECT t.id,t.name");
        } else {
            sb.append("SELECT %s");
        }
        sb.append(" FROM OrderStatusType t WHERE 1=1");
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(name)) {
            sb.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        return new Pair<>(sb.toString(), map);
    }

}