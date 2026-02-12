package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.JoorJensEmployee;

public interface JoorJensEmployeeRepository extends Repository<Long, JoorJensEmployee> {

    FetchResult<JoorJensEmployee> search(long id, Boolean block, int max, int offset) throws JoorJensException;

}
