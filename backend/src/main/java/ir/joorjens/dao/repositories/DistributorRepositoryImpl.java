package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Distributor;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DistributorRepositoryImpl
        extends RepositoryImpAbstract<Long, Distributor>
        implements DistributorRepository {

    @Override
    public int update(long id, int childCount) throws JoorJensException {
        if (childCount != 0) {
            String query = "UPDATE Distributor t set t.updatedTime=:updatedTime, t.childCount=t.childCount+:childCount WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("updatedTime", Utility.getCurrentTime());
            map.put("childCount", childCount);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    public int update(final Set<Long> ids, boolean block) throws JoorJensException {
        if (ids.size() > 0) {
            String query = "UPDATE Distributor t set t.updatedTime=:updatedTime, t.block=:block WHERE t.id in (:ids)";
            final Map<String, Object> map = new HashMap<>();
            map.put("updatedTime", Utility.getCurrentTime());
            map.put("block", block);
            map.put("ids", ids);
            return update(query, map);
        }
        return 0;
    }

    @Override
    public int rate(final long id, final float rate, final int count) throws JoorJensException {
        if (rate != 0) {
            final Map<String, Object> map = new HashMap<>();
            final StringBuilder query = new StringBuilder("UPDATE Distributor t set t.rateSum=t.rateSum+:rate");
            map.put("rate", rate);
            if (count != 0) {
                query.append(",t.rateCount=:count");
                map.put("count", count);
            }
            query.append(" WHERE t.id=:id");
            map.put("id", id);
            return update(query.toString(), map);
        }
        return 0;
    }

    @Override
    public Distributor findBySerial(String serial) throws JoorJensException {
        final String query = "SELECT t FROM Distributor t WHERE t.serial=:serial";
        final Map<String, Object> map = new HashMap<>();
        map.put("serial", serial);
        return findBy(query, map);
    }

    @Override
    public Distributor findByManager(long managerId, String managerMobile, String managerNI) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM Distributor t WHERE");
        final Map<String, Object> map = new HashMap<>();
        if (managerId > 0) {
            query.append(" t.manager.id=:managerId");
            map.put("managerId", managerId);
        } else if (!Utility.isEmpty(managerMobile)) {
            query.append(" t.manager.mobileNumber=:managerMobile");
            map.put("managerMobile", managerMobile);
        } else if (!Utility.isEmpty(managerNI)) {
            query.append(" t.manager.nationalIdentifier=:managerNI");
            map.put("managerNI", managerNI);
        } else {
            return null;
        }

        return findBy(query.toString(), map);
    }

    @Override
    public FetchResult<Distributor> search(final long id, final int type, final String name, final String serial
            , final String registrationNumber, final String telephone, final String fax, final String site
            , final int createdTimeFrom, final int createdTimeTo, final int dailySaleFrom, final int dailySaleTo
            , final long areaCityId, final long areaProId, final long managerId
            , final long parentId, final String parentSerial, final String managerNationalIdentifier
            , final Set<Long> activityTypeIds, final Set<Long> supportAreaIds
            , final Set<Long> productBrandTypeIds, final Set<Long> productCategoryTypeIds
            , final boolean typesAreParent, final String orderTypeIds, final String ascDescs
            , final boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, type, name, serial
                , registrationNumber, telephone, fax, site
                , createdTimeFrom, createdTimeTo, dailySaleFrom, dailySaleTo
                , areaCityId, areaProId, managerId, parentId, parentSerial, managerNationalIdentifier
                , activityTypeIds, supportAreaIds
                , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                , orderTypeIds, ascDescs, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(final long id, final int type, final String name, final String serial
            , final String registrationNumber, final String telephone, final String fax, final String site
            , final int createdTimeFrom, final int createdTimeTo, final int dailySaleFrom, final int dailySaleTo
            , final long areaCityId, final long areaProId, final long managerId
            , final long parentId, final String parentSerial, final String managerNationalIdentifier
            , final Set<Long> activityTypeIds, final Set<Long> supportAreaIds
            , final Set<Long> productBrandTypeIds, final Set<Long> productCategoryTypeIds
            , final boolean typesAreParent, final String orderTypeIds, final String ascDescs
            , final boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, type, name, serial
                , registrationNumber, telephone, fax, site
                , createdTimeFrom, createdTimeTo, dailySaleFrom, dailySaleTo
                , areaCityId, areaProId, managerId, parentId, parentSerial, managerNationalIdentifier
                , activityTypeIds, supportAreaIds
                , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                , orderTypeIds, ascDescs, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(final long id, final int type, final String name, final String serial
            , final String registrationNumber, final String telephone, final String fax, final String site
            , final int createdTimeFrom, final int createdTimeTo, final int dailySaleFrom, final int dailySaleTo
            , final long areaCityId, final long areaProId, final long managerId
            , final long parentId, final String parentSerial, final String managerNationalIdentifier
            , final Set<Long> activityTypeIds, final Set<Long> supportAreaIds
            , final Set<Long> productBrandTypeIds, final Set<Long> productCategoryTypeIds
            , final boolean typesAreParent, final String orderTypeIds, final String ascDescs
            , final boolean like, Boolean block, final boolean pair) {
        final StringBuilder table = new StringBuilder("Distributor t");
        final StringBuilder where = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();

        addCommonQuery(where, map, id, block);
        if (type > 0) {
            where.append(" and t.type=:type");
            map.put("type", type);
        }
        if (!Utility.isEmpty(name)) {
            where.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (!Utility.isEmpty(serial)) {
            where.append(" and t.serial=:serial");
            map.put("serial", serial);
        }
        if (!Utility.isEmpty(registrationNumber)) {
            where.append(" and t.registrationNumber=:registrationNumber");
            map.put("registrationNumber", registrationNumber);
        }
        if (!Utility.isEmpty(telephone)) {
            where.append(" and t.telephone=:telephone");
            map.put("telephone", telephone);
        }
        if (!Utility.isEmpty(fax)) {
            where.append(" and t.fax=:fax");
            map.put("fax", fax);
        }
        if (!Utility.isEmpty(site)) {
            where.append(" and t.site=:site");
            map.put("site", site);
        }

        if (createdTimeFrom > 0) {
            where.append(" and t.createdTime>=:createdTimeFrom");
            map.put("createdTimeFrom", createdTimeFrom);
        }
        if (createdTimeTo > 0 && createdTimeTo >= createdTimeFrom) {
            where.append(" and t.createdTime<=:createdTimeTo");
            map.put("createdTimeTo", createdTimeTo);
        }
        if (dailySaleFrom > 0) {
            where.append(" and t.dailySale>=:dailySaleFrom");
            map.put("dailySaleFrom", dailySaleFrom);
        }
        if (dailySaleTo > 0 && dailySaleTo >= dailySaleFrom) {
            where.append(" and t.dailySaleTo<=:dailySaleTo");
            map.put("dailySaleTo", dailySaleTo);
        }

        if (areaCityId > 0) {
            where.append(" and t.areaCity.id=:areaCityId");
            map.put("areaCityId", areaCityId);
        }
        if (areaProId > 0) {
            where.append(" and t.areaCity.parent.id=:areaProId");
            map.put("areaProId", areaProId);
        }
        if (managerId > 0) {
            where.append(" and t.manager.id=:managerId");
            map.put("managerId", managerId);
        }
        if (!Utility.isEmpty(managerNationalIdentifier)) {
            where.append(" and t.manager.nationalIdentifier=:managerNationalIdentifier");
            map.put("managerNationalIdentifier", managerNationalIdentifier);
        }

        if (parentId > 0) {
            where.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
        }
        if (!Utility.isEmpty(parentSerial)) {
            where.append(" and t.parent.serial=:parentSerial");
            map.put("parentSerial", parentSerial);
        }

        if (productBrandTypeIds != null && productBrandTypeIds.size() > 0) {
            where.append(" and t.id in (select distinct dp.distributor.id from DistributorProduct dp");
            where.append(" where dp.product.productBrandType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productBrandTypeIds))");
            map.put("productBrandTypeIds", productBrandTypeIds);
        }
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            where.append(" and t.id in (select distinct dp.distributor.id from DistributorProduct dp");
            where.append(" where dp.product.productCategoryType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productCategoryTypeIds))");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
        }

        boolean joined = false;
        if (activityTypeIds != null && activityTypeIds.size() > 0) {
            table.append(" join t.activityTypes at");
            where.append(" and at.id in (:activityTypeIds)");
            map.put("activityTypeIds", activityTypeIds);
            joined = true;
        }
        if (supportAreaIds != null && supportAreaIds.size() > 0) {
            table.append(" join t.supportAreas sa");
            where.append(" and sa.id in (:supportAreaIds)");
            map.put("supportAreaIds", supportAreaIds);
            joined = true;
        }

        final String order = TypeEnumeration.getOrder("t", orderTypeIds, ascDescs);
        final StringBuilder query = new StringBuilder("SELECT");
        if (joined) {
            query.append(" distinct");
        }
        if (pair) {
            query.append(" t.id,t.name");
        } else {
            query.append(" %s");
        }
        query.append(" FROM ").append(table).append(" WHERE 1=1 ").append(where).append(" ").append(order);

        return new Pair<>(query.toString(), map);
    }
}