package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CustomerRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerRepositoryImpl
        extends RepositoryImpAbstract<Long, Customer>
        implements CustomerRepository {

    @Override
    public int update(long id, int credit) throws JoorJensException {
        if (id <= 0) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder("UPDATE Customer u SET u.updatedTime=:updatedTime");
        map.put("updatedTime", Utility.getCurrentTime());
        if (credit != 0) {
            sb.append(",u.credit=u.credit+:credit");
            map.put("credit", credit);
        }
        int rowAffected = 0;
        if (map.size() > 1) {
            sb.append(" WHERE u.id=:id");
            map.put("id", id);
            rowAffected = update(sb.toString(), map);
        }
        return rowAffected;
    }

    @Override
    public int updateInCartable(long id, boolean inCartable) throws JoorJensException {
        if (id <= 0) {
            return 0;
        }
        final Map<String, Object> map = new HashMap<>();
        final String query = "UPDATE Customer u SET u.updatedTime=:updatedTime, u.inCartable=:inCartable WHERE u.id=:id";
        map.put("updatedTime", Utility.getCurrentTime());
        map.put("inCartable", inCartable);
        map.put("id", id);
        return update(query, map);
    }

    @Override
    public Customer getByMobile(String mobile) throws JoorJensException {
        final String query = "Select t from Customer t Where t.mobileNumber=:mobileNumber";
        final Map<String, Object> map = new HashMap<>();
        map.put("mobileNumber", mobile);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Customer> search(long id, long areaCityId, long areaProvinceId
            , Set<Long> roleIds, Set<Integer> roleTypes
            , String mobileNumber, String nationalIdentifier
            , String firstName, String lastName, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, areaCityId, areaProvinceId, roleIds, roleTypes,
                mobileNumber, nationalIdentifier, firstName, lastName, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, long areaCityId, long areaProvinceId
            , Set<Long> roleIds, Set<Integer> roleTypes
            , String mobileNumber, String nationalIdentifier
            , String firstName, String lastName, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, areaCityId, areaProvinceId, roleIds, roleTypes,
                mobileNumber, nationalIdentifier, firstName, lastName, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }


    private Pair<String, Map<String, Object>> getQuery(long id, long areaCityId, long areaProvinceId
            , Set<Long> roleIds, Set<Integer> roleTypes
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block, boolean pair) throws JoorJensException {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            final String nameOrMobile = "case when (t.firstName is null and t.lastName is null) then t.mobileNumber else (t.firstName||' '||t.lastName) end";
            query.append("SELECT t.id,").append(nameOrMobile);
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM Customer t WHERE 1=1");
        //map.put("activationCode", Customer.ACTIVATED);
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(mobileNumber)) {
            query.append(" and t.mobileNumber=:mobileNumber");
            map.put("mobileNumber", mobileNumber);
        }
        if (!Utility.isEmpty(nationalIdentifier)) {
            query.append(" and t.nationalIdentifier=:nationalIdentifier");
            map.put("nationalIdentifier", nationalIdentifier);
        }
        if (!Utility.isEmpty(firstName)) {
            query.append(" and t.firstName=:firstName");
            map.put("firstName", firstName);
        }
        if (!Utility.isEmpty(lastName)) {
            query.append(" and t.lastName=:lastName");
            map.put("lastName", lastName);
        }

        if (areaProvinceId > 0) {
            query.append(" and t.areaCity.parent.id=:areaProvinceId");
            map.put("areaProvinceId", areaProvinceId);
        }
        if (areaCityId > 0) {
            query.append(" and t.areaCity.id=:areaCityId");
            map.put("areaCityId", areaCityId);
        }
        if (roleIds != null && roleIds.size() > 0) {
            query.append(" and t.role.id in (:roleIds)");
            map.put("roleIds", roleIds);
        }
        if (roleTypes != null && roleTypes.size() > 0) {
            query.append(" and t.role.roleType in (:roleTypes)");
            map.put("roleTypes", roleTypes);
        }
        return new Pair<>(query.toString(), map);
    }
}