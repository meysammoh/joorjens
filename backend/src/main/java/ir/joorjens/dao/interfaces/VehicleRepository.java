package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends Repository<Long, Vehicle> {

    FetchResult<Vehicle> search(long id
            , int licensePlateFirst, String licensePlateLetter, int licensePlateSecond, int licensePlateCode
            , int color, int manufactureYear, long vehicleBrandTypeId, long distributorId
            , Boolean block, int max, int offset) throws JoorJensException;

    List<Third<Long, String, String>> getAllPairs(long id
            , int licensePlateFirst, String licensePlateLetter, int licensePlateSecond, int licensePlateCode
            , int color, int manufactureYear, long vehicleBrandTypeId, long distributorId, Boolean block) throws JoorJensException;
}