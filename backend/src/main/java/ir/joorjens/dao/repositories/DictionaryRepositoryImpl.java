package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DictionaryRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Dictionary;

import java.util.HashMap;
import java.util.Map;

public class DictionaryRepositoryImpl
        extends RepositoryImpAbstract<Long, Dictionary>
        implements DictionaryRepository {

    @Override
    public Dictionary findByName(final String name) throws JoorJensException {
        final String query = "SELECT t FROM Dictionary t WHERE t.name=:name";
        final Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findBy(query, map);
    }

    @Override
    public FetchResult<Dictionary> search(long id, String name, boolean like, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Dictionary t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (block != null) {
            query.append(" and t.block=:block");
            map.put("block", block);
        }
        return search(query.toString(), map, max, offset);
    }
}