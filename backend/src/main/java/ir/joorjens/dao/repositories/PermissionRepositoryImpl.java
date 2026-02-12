package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.PermissionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionRepositoryImpl
        extends RepositoryImpAbstract<Long, Permission>
        implements PermissionRepository {

    @Override
    public Permission getByUrl(String url) throws JoorJensException {
        final String query = "Select t from Permission t Where t.url=:url";
        final Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findBy(query, map);
    }

    @Override
    public FetchResult<Permission> search(long id, String url, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, url, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String url) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, url, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Permission> findAll() throws JoorJensException {
        return findAllBy("SELECT t FROM Permission t", null);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String url, boolean pair) {
        final StringBuilder sb = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            sb.append("SELECT t.id,t.name");
        } else {
            sb.append("SELECT %s");
        }
        sb.append(" FROM Permission t WHERE 1=1");
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(url)) {
            sb.append(" and t.url=:url");
            map.put("url", url);
        }
        return new Pair<>(sb.toString(), map);
    }
}