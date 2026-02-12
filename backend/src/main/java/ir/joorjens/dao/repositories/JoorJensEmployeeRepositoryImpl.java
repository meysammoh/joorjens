package ir.joorjens.dao.repositories;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.JoorJensEmployeeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.JoorJensEmployee;

import java.util.HashMap;
import java.util.Map;

public class JoorJensEmployeeRepositoryImpl
        extends RepositoryImpAbstract<Long, JoorJensEmployee>
        implements JoorJensEmployeeRepository {

    @Override
    public FetchResult<JoorJensEmployee> search(long id, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM JoorJensEmployee t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        return search(query.toString(), map, max, offset);
    }
}