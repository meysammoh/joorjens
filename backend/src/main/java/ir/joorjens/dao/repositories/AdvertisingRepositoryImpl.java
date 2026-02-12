package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.AdvertisingRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Advertising;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public class AdvertisingRepositoryImpl
        extends RepositoryImpAbstract<Long, Advertising>
        implements AdvertisingRepository {

    @Override
    public int updateClick(long id, long clickCount) throws JoorJensException {
        if (id <= 0 || clickCount == 0) {
            return 0;
        }
        final String query = "UPDATE Advertising t SET t.clickCount=t.clickCount+:clickCount WHERE t.id=:id";
        Map<String, Object> map = new HashMap<>();
        map.put("clickCount", clickCount);
        map.put("id", id);
        return update(query, map);
    }

    @Override
    public FetchResult<Advertising> search(long id, String title, String link, Boolean app
            , int type, int fromTime, int toTime, String orderTypeIds, String ascDescs
            , Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Advertising t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(title)) {
            query.append(" and t.title=:title");
            map.put("title", title);
        }
        if (!Utility.isEmpty(link)) {
            query.append(" and t.link=:link");
            map.put("link", link);
        }
        if (app != null) {
            query.append(" and t.app=:app");
            map.put("app", app);
        }
        if (type > 0) {
            query.append(" and t.type=:type");
            map.put("type", type);
        }
        if (fromTime > 0) {
            query.append(" and t.fromTime>=:fromTime");
            map.put("fromTime", fromTime);
        }
        if (toTime > 0 && toTime >= fromTime) {
            query.append(" and t.toTime<=:toTime");
            map.put("toTime", toTime);
        }

        return search(query.toString(), map, TypeEnumeration.getOrder("t", orderTypeIds, ascDescs), max, offset);
    }
}