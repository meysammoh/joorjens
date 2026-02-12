package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ValidIPRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ValidIP;

import java.util.HashMap;
import java.util.Map;

public class ValidIPRepositoryImpl
        extends RepositoryImpAbstract<Long, ValidIP>
        implements ValidIPRepository {

    @Override
    public boolean hasAccess(final String ip, final long roleId) throws JoorJensException {
        final String query = "SELECT t FROM ValidIP t WHERE t.ip=:ip and t.role.id=:roleId";
        final Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("roleId", roleId);
        final ValidIP validIP = findBy(query, map);
        return validIP != null;
    }

    @Override
    public FetchResult<ValidIP> search(final long id, final String ip, final long roleId
            , final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM ValidIP t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(ip)) {
            query.append(" and t.ip=:ip");
            map.put("ip", ip);
        }
        if (roleId > 0) {
            query.append(" and t.role.id=:roleId");
            map.put("roleId", roleId);
        }

        return search(query.toString(), map, max, offset);
    }
}