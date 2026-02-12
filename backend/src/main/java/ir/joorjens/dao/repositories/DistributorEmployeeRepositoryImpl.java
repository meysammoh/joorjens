package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorEmployeeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorEmployee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DistributorEmployeeRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorEmployee>
        implements DistributorEmployeeRepository {

    @Override
    public DistributorEmployee find(long id, String mobileNumber, String nationalIdentifier) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM DistributorEmployee t WHERE");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" t.id=:id");
            map.put("id", id);
        } else if (!Utility.isEmpty(mobileNumber)) {
            query.append(" t.customer.mobileNumber=:mobileNumber");
            map.put("mobileNumber", mobileNumber);
        } else if (!Utility.isEmpty(nationalIdentifier)) {
            query.append(" t.customer.nationalIdentifier=:nationalIdentifier");
            map.put("nationalIdentifier", nationalIdentifier);
        } else {
            return null;
        }

        return findBy(query.toString(), map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<DistributorEmployee> search(long id, long distributorId, long roleId
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, distributorId, roleId
                , mobileNumber, nationalIdentifier, firstName, lastName, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, long distributorId, long roleId
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, distributorId, roleId
                , mobileNumber, nationalIdentifier, firstName, lastName, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, long distributorId, long roleId
            , String mobileNumber, String nationalIdentifier, String firstName, String lastName
            , Boolean block, boolean pair) throws JoorJensException {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            final String nameOrMobile = "case when (t.customer.firstName is null and t.customer.lastName is null) then t.customer.mobileNumber else (t.customer.firstName||' '||t.customer.lastName) end";
            query.append("SELECT t.id,").append(nameOrMobile);
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM DistributorEmployee t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(mobileNumber)) {
            query.append(" and t.customer.mobileNumber=:mobileNumber");
            map.put("mobileNumber", mobileNumber);
        }
        if (!Utility.isEmpty(nationalIdentifier)) {
            query.append(" and t.customer.nationalIdentifier=:nationalIdentifier");
            map.put("nationalIdentifier", nationalIdentifier);
        }
        if (!Utility.isEmpty(firstName)) {
            query.append(" and t.customer.firstName=:firstName");
            map.put("firstName", firstName);
        }
        if (!Utility.isEmpty(lastName)) {
            query.append(" and t.customer.lastName=:lastName");
            map.put("lastName", lastName);
        }

        if (distributorId > 0) {
            query.append(" and t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        if (roleId > 0) {
            query.append(" and t.customer.role.id=:roleId");
            map.put("roleId", roleId);
        }

        if (block != null) {
            query.append(" and t.distributor.block=:distributorBlock");
            query.append(" and t.customer.role.block=:roleBlock");
            map.put("distributorBlock", block);
            map.put("roleBlock", block);
        }
        return new Pair<>(query.toString(), map);
    }
}