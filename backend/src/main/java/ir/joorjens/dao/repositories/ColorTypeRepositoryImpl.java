package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ColorTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ColorType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColorTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, ColorType>
        implements ColorTypeRepository {

    @Override
    public FetchResult<ColorType> search(long id, String name, String code, Boolean block, boolean like, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, code, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, String code, Boolean block, boolean like) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, code, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, String code, boolean like, Boolean block
            , boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM ColorType t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (!Utility.isEmpty(code)) {
            query.append(" and t.code=:code");
            map.put("code", code);
        }
        return new Pair<>(query.toString(), map);
    }

}