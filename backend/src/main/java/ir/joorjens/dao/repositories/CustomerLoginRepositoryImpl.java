package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CustomerLoginRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.CustomerLogin;

import java.util.HashMap;
import java.util.Map;

public class CustomerLoginRepositoryImpl
        extends RepositoryImpAbstract<Long, CustomerLogin>
        implements CustomerLoginRepository {

    @Override
    public int update(long id, int logoutTime) throws JoorJensException {
        if (id <= 0) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder("UPDATE CustomerLogin t SET t.updatedTime=:updatedTime");
        map.put("updatedTime", Utility.getCurrentTime());
        if (logoutTime > (Utility.getCurrentTime() - Config.TIME_DAY_SEC)) {
            sb.append(",t.logoutTime=:expiredTime");
            map.put("logoutTime", logoutTime);
        }
        int rowAffected = 0;
        if (map.size() > 1) {
            sb.append(" WHERE t.id=:id");
            map.put("id", id);
            rowAffected = update(sb.toString(), map);
        }
        return rowAffected;
    }

    @Override
    public CustomerLogin getLast(final long customerSessionId, final int day) throws JoorJensException {
        final String query = "Select t from CustomerLogin t Where t.customerSession.id=:customerSessionId and t.day=:day";
        final Map<String, Object> map = new HashMap<>();
        map.put("customerSessionId", customerSessionId);
        map.put("day", day);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<CustomerLogin> search(long id, long customerId
            , int loginFrom, int loginTo
            , int logoutFrom, int logoutTo
            , int max, int offset) throws JoorJensException {
        final StringBuilder sb = new StringBuilder("select %s from CustomerLogin t Where 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (customerId > 0) {
            sb.append(" and t.customerSession.customer.id=:customerId");
            map.put("customerId", customerId);
        }

        if (loginFrom > 0) {
            sb.append(" and t.loginTime>=:loginFrom");
            map.put("loginFrom", loginFrom);
        }
        if (loginTo > 0 && loginTo >= loginFrom) {
            sb.append(" and t.loginTime<=:loginTo");
            map.put("loginTo", loginTo);
        }
        if (logoutFrom > 0) {
            sb.append(" and t.logoutTime>=:logoutFrom");
            map.put("logoutFrom", logoutFrom);
        }
        if (logoutTo > 0 && logoutTo >= logoutFrom) {
            sb.append(" and t.logoutTime<=:logoutTo");
            map.put("logoutTo", logoutTo);
        }

        return search(sb.toString(), map, max, offset);
    }

}