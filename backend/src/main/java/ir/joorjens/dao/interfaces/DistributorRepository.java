package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Distributor;

import java.util.List;
import java.util.Set;

public interface DistributorRepository extends Repository<Long, Distributor> {

    int update(long id, int childCount) throws JoorJensException;

    int update(Set<Long> ids, boolean block) throws JoorJensException;

    int rate(long id, float rate, int count) throws JoorJensException;

    Distributor findBySerial(String serial) throws JoorJensException;

    Distributor findByManager(long managerId, String managerMobile, String managerNI) throws JoorJensException;

    FetchResult<Distributor> search(long id, int type, String name, String serial
            , String registrationNumber, String telephone, String fax, String site
            , int createdTimeFrom, int createdTimeTo, int dailySaleFrom, int dailySaleTo
            , long areaCityId, long areaProId, long managerId
            , long parentId, String parentSerial, String managerNationalIdentifier
            , Set<Long> activityTypeIds, Set<Long> supportAreaIds
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, int type, String name, String serial
            , String registrationNumber, String telephone, String fax, String site
            , int createdTimeFrom, int createdTimeTo, int dailySaleFrom, int dailySaleTo
            , long areaCityId, long areaProId, long managerId
            , long parentId, String parentSerial, String managerNationalIdentifier
            , Set<Long> activityTypeIds, Set<Long> supportAreaIds
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs, boolean like, Boolean block) throws JoorJensException;

}