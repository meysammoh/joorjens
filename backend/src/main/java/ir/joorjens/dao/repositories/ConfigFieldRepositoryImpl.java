package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ConfigFieldRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ConfigField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigFieldRepositoryImpl
        extends RepositoryImpAbstract<Long, ConfigField>
        implements ConfigFieldRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<ConfigField> search(long id, String name, float valueFrom, float valueTo, boolean like,  int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, valueFrom, valueTo, like, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, float valueFrom, float valueTo, boolean like) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, valueFrom, valueTo, like, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConfigField> findAll() throws JoorJensException {
        return findAllBy("SELECT t FROM ConfigField t", null);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, float valueFrom, float valueTo, boolean like, boolean pair) {
        final StringBuilder sb = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            sb.append("SELECT t.id,t.name");
        } else {
            sb.append("SELECT %s");
        }
        sb.append(" FROM ConfigField t WHERE 1=1");
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(name)) {
            sb.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (valueFrom > 0) {
            sb.append(" and t.value>=:valueFrom");
            map.put("valueFrom", valueFrom);
        }
        if (valueTo > 0 && valueTo >= valueFrom) {
            sb.append(" and t.value<=:valueTo");
            map.put("valueTo", valueTo);
        }
        return new Pair<>(sb.toString(), map);
    }

}