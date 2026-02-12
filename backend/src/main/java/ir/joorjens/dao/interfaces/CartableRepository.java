package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Cartable;

public interface CartableRepository extends Repository<Long, Cartable> {

    boolean isExist(int status, int type, String key) throws JoorJensException;

    FetchResult<Cartable> search(long id, int status, int type, String key, int priority, Boolean toJoorJens
            , int createdTimeFrom, int createdTimeTo, int doneTimeFrom, int doneTimeTo
            , long fromCustomerId, long toCustomerId, long donnerCustomerId, long toDistributorId
            , Boolean block, int max, int offset) throws JoorJensException;
}
