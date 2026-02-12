package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.VehicleBrandType;

import java.util.List;

public interface VehicleBrandTypeRepository extends Repository<Long, VehicleBrandType> {

    FetchResult<VehicleBrandType> search(long id, String name, int capacityFrom, int capacityTo
            , boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, int capacityFrom, int capacityTo
            , boolean like, Boolean block) throws JoorJensException;

    List<VehicleBrandType> findAll() throws JoorJensException;
}