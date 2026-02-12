package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.LogRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Log;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public class LogRepositoryImpl
        extends RepositoryImpAbstract<Long, Log>
        implements LogRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Log> search(long id, long customerId, int actionType, int resultType
            , String className, int fromTime, int toTime, int max, int offset) throws JoorJensException {
        StringBuilder sb = new StringBuilder("SELECT %s FROM Log t WHERE 1=1 ");
        Map<String, Object> map = new HashMap<>();

        if (id > 0) {
            sb.append(" and t.id=:id");
            map.put("id", id);
        }
        if (customerId > 0) {
            sb.append(" and t.customer.id=:customerId");
            map.put("customerId", customerId);
        }
        if (TypeEnumeration.contains(actionType)) {
            sb.append(" and t.actionType=:actionType");
            map.put("actionType", actionType);
        }
        if (TypeEnumeration.contains(resultType)) {
            sb.append(" and t.resultType=:resultType");
            map.put("resultType", resultType);
        }
        if (!Utility.isEmpty(className)) {
            sb.append(" and t.className=:className");
            map.put("className", className);
        }
        if (fromTime > 0) {
            sb.append(" and t.createdTime>=:fromTime");
            map.put("fromTime", fromTime);
        }
        if (toTime > 0 && toTime >= fromTime) {
            sb.append(" and t.createdTime<=:toTime");
            map.put("toTime", toTime);
        }
        return search(sb.toString(), map, max, offset);
    }
}