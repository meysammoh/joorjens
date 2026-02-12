package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.StoreRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoreRepositoryImpl
        extends RepositoryImpAbstract<Long, Store>
        implements StoreRepository {

    @Override
    public Store find(final long managerId) throws JoorJensException {
        final String query = "SELECT t FROM Store t WHERE t.manager.id=:managerId";
        final Map<String, Object> map = new HashMap<>();
        map.put("managerId", managerId);
        return findBy(query, map);
    }

    @Override
    public FetchResult<Store> search(long id, String name
            , String telephone, String businessLicense, String postalCode
            , long zoneId, long cityId, long proId, long managerId
            , String managerNationalIdentifier, boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, telephone, businessLicense, postalCode
                , zoneId, cityId, proId, managerId, managerNationalIdentifier, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name
            , String telephone, String businessLicense, String postalCode
            , long zoneId, long cityId, long proId, long managerId
            , String managerNationalIdentifier, boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, telephone, businessLicense, postalCode
                , zoneId, cityId, proId, managerId, managerNationalIdentifier, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name
            , String telephone, String businessLicense, String postalCode
            , long zoneId, long cityId, long proId, long managerId
            , String managerNationalIdentifier, boolean like, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM Store t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (!Utility.isEmpty(telephone)) {
            query.append(" and t.telephone=:telephone");
            map.put("telephone", telephone);
        }
        if (!Utility.isEmpty(businessLicense)) {
            query.append(" and t.businessLicense=:businessLicense");
            map.put("businessLicense", businessLicense);
        }
        if (!Utility.isEmpty(postalCode)) {
            query.append(" and t.postalCode=:postalCode");
            map.put("postalCode", postalCode);
        }

        if (zoneId > 0) {
            query.append(" and t.areaZone.id=:zoneId");
            map.put("zoneId", zoneId);
        }
        if (cityId > 0) {
            query.append(" and t.areaZone.parent.id=:cityId");
            map.put("cityId", cityId);
        }
        if (proId > 0) {
            query.append(" and t.areaZone.parent.parent.id=:proId");
            map.put("proId", proId);
        }
        if (managerId > 0) {
            query.append(" and t.manager.id=:managerId");
            map.put("managerId", managerId);
        }
        if (!Utility.isEmpty(managerNationalIdentifier)) {
            query.append(" and t.manager.nationalIdentifier=:managerNationalIdentifier");
            map.put("managerNationalIdentifier", managerNationalIdentifier);
        }

        return new Pair<>(query.toString(), map);
    }
}