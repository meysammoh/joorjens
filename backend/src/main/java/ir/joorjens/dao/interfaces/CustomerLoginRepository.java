package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.CustomerLogin;

public interface CustomerLoginRepository extends Repository<Long, CustomerLogin> {

    int update(long id, int logoutTime) throws JoorJensException;

    CustomerLogin getLast(long customerSessionId, int day) throws JoorJensException;

    FetchResult<CustomerLogin> search(long id, long customerId
            , int loginFrom, int loginTo
            , int logoutFrom, int logoutTo
            , int max, int offset) throws JoorJensException;

}