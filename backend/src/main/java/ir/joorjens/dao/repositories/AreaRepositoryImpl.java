package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.AreaRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Area;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AreaRepositoryImpl
        extends RepositoryImpAbstract<Long, Area>
        implements AreaRepository {

    @Override
    public int update(long id, int childCount) throws JoorJensException {
        if (childCount != 0) {
            String query = "UPDATE Area t set t.childCount=t.childCount+:childCount WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("childCount", childCount);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Area> search(long id, String name, int adType, long parentId
            , boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, adType, parentId, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Area> getAllAreas(long id, String name, int adType, long parentId
            , boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, adType, parentId, like, block, false);
        return findAllBy(String.format(query.getFirst(), "t"), query.getSecond());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Third<Long, String, Long>> getAllPairs(long id, String name, int adType, long parentId
            , boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, adType, parentId, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Third<>((long) obj[0], (String) obj[1], (long) obj[2]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, int adType, long parentId
            , boolean like, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name,t.parent.id");
        } else {
            query.append("SELECT %s");
        }
        query.append(String.format(" FROM Area t WHERE t.id!=%d", Area.getFakeId()));
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (TypeEnumeration.get(adType) != null) {
            query.append(" and t.adType=:adType");
            map.put("adType", adType);
        }
        if (parentId > 0) {
            query.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
            if (block != null) {
                query.append(" and t.parent.block=:parentBlock");
                map.put("parentBlock", block);
            }
        }
        return new Pair<>(query.toString(), map);
    }

}