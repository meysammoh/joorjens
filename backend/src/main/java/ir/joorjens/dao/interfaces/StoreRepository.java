package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Store;

import java.util.List;

public interface StoreRepository extends Repository<Long, Store> {

    Store find(long managerId) throws JoorJensException;

    FetchResult<Store> search(long id, String name
            , String telephone, String businessLicense, String postalCode
            , long areaZoneId, long areaCityId, long areaProId, long managerId
            , String managerNationalIdentifier, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name
            , String telephone, String businessLicense, String postalCode
            , long areaZoneId, long areaCityId, long areaProId, long managerId
            , String managerNationalIdentifier, boolean like, Boolean block) throws JoorJensException;
}
