package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.CustomerSession;

public interface CustomerSessionRepository extends Repository<Long, CustomerSession> {

    int updateAccess(long id) throws JoorJensException;

    CustomerSession getByUclaim(String uclaim) throws JoorJensException;

    /**
     * find by customerId, roleId, ip, userAgent
     *
     * @param customerSession it`s field are normal
     * @return customerSession if find
     * @throws JoorJensException
     */
    CustomerSession getByKey(final CustomerSession customerSession) throws JoorJensException;

    FetchResult<CustomerSession> search(long id, long customerId, long roleId, String ip
            , String browserManufacturer, String browserGroup, String browserVersion
            , String osManufacturer, String osGroup, String device
            , int max, int offset) throws JoorJensException;

}