package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorRateDiscontent;

public interface DistributorRateDiscontentRepository extends Repository<Long, DistributorRateDiscontent> {

    DistributorRateDiscontent find(long cartId, long distributorId) throws JoorJensException;

    FetchResult<DistributorRateDiscontent> search(long id
            , long cartId, long distributorId, long customerId
            , int max, int offset) throws JoorJensException;
}
