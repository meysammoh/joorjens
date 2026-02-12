package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Log;


public interface LogRepository extends Repository<Long, Log> {
    FetchResult<Log> search(long id, long customerId, int actionType, int resultType
            , String className, int fromTime, int toTime, int max, int offset) throws JoorJensException;
}