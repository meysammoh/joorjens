package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorDeliverer;

import java.util.List;

public interface DistributorDelivererRepository extends Repository<Long, DistributorDeliverer> {

    FetchResult<DistributorDeliverer> search(long id, long distributorId, long vehicleId
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, long distributorId, long vehicleId
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block) throws JoorJensException;
}
