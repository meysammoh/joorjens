package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CustomerSessionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.CustomerSession;

import java.util.HashMap;
import java.util.Map;

public class CustomerSessionRepositoryImpl
        extends RepositoryImpAbstract<Long, CustomerSession>
        implements CustomerSessionRepository {

    @Override
    public int updateAccess(long id) throws JoorJensException {
        if (id <= 0) {
            return 0;
        }
        final String query = "UPDATE CustomerSession u SET u.updatedTime=:updatedTime WHERE u.id=:id";
        final Map<String, Object> map = new HashMap<>();
        map.put("updatedTime", Utility.getCurrentTime());
        map.put("id", id);
        return update(query, map);
    }

    @Override
    public CustomerSession getByUclaim(final String uclaim) throws JoorJensException {
        final String query = "Select t from CustomerSession t Where t.uclaim=:uclaim";
        final Map<String, Object> map = new HashMap<>();
        map.put("uclaim", uclaim);
        return findBy(query, map);
    }

    @Override
    public CustomerSession getByKey(final CustomerSession customerSession) throws JoorJensException {
        //public Customer getByMobile(String mobile) throws JoorJensException {
        final String query = "Select t from CustomerSession t Where t.ip=:ip and t.userAgent=:userAgent" +
                " and t.customer.id=:customerId and t.role.id=:roleId";
        final Map<String, Object> map = new HashMap<>();
        map.put("ip", customerSession.getIp());
        map.put("userAgent", customerSession.getUserAgent());
        map.put("customerId", customerSession.getCustomerId());
        map.put("roleId", customerSession.getRoleId());
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<CustomerSession> search(long id, long customerId, long roleId, String ip
            , String browserManufacturer, String browserGroup, String browserVersion
            , String osManufacturer, String osGroup, String device
            , int max, int offset) throws JoorJensException {
        final StringBuilder sb = new StringBuilder("select %s from CustomerSession t Where 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (customerId > 0) {
            sb.append(" and t.customer.id=:customerId");
            map.put("customerId", customerId);
        }
        if (roleId > 0) {
            sb.append(" and t.role.id=:roleId");
            map.put("roleId", roleId);
        }
        if (!Utility.isEmpty(ip)) {
            sb.append(" and t.ip=:ip");
            map.put("ip", Utility.toLowerCase(ip));
        }

        if (!Utility.isEmpty(browserManufacturer)) {
            sb.append(" and t.browserManufacturer=:browserManufacturer");
            map.put("browserManufacturer", Utility.toFirstUpper(browserManufacturer));
        }
        if (!Utility.isEmpty(browserGroup)) {
            sb.append(" and t.browserGroup=:browserGroup");
            map.put("browserGroup", Utility.toFirstUpper(browserGroup));
        }
        if (!Utility.isEmpty(browserVersion)) {
            sb.append(" and t.browserVersion=:browserVersion");
            map.put("browserVersion", browserVersion);
        }

        if (!Utility.isEmpty(osManufacturer)) {
            sb.append(" and t.osManufacturer=:osManufacturer");
            map.put("osManufacturer", Utility.toFirstUpper(osManufacturer));
        }
        if (!Utility.isEmpty(osGroup)) {
            sb.append(" and t.osGroup=:osGroup");
            map.put("osGroup", Utility.toFirstUpper(osGroup));
        }
        if (!Utility.isEmpty(device)) {
            sb.append(" and t.device=:device");
            map.put("device", Utility.toFirstUpper(device));
        }

        return search(sb.toString(), map, max, offset);
    }
}