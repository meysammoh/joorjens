package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorDiscontentTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorDiscontentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DistributorDiscontentTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorDiscontentType>
        implements DistributorDiscontentTypeRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<DistributorDiscontentType> search(long id, String name
            , boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DistributorDiscontentType> findAll() throws JoorJensException {
        return findAllBy("SELECT t FROM DistributorDiscontentType t", null);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, boolean like, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM DistributorDiscontentType t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        return new Pair<>(query.toString(), map);
    }
}