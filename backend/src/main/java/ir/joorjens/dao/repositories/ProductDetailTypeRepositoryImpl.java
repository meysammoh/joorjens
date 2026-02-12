package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductDetailTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ProductDetailType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDetailTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, ProductDetailType>
        implements ProductDetailTypeRepository {

    @Override
    public int update(long id, int childCount) throws JoorJensException {
        if (childCount != 0) {
            String query = "UPDATE ProductDetailType t set t.childCount=t.childCount+:childCount WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("childCount", childCount);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductDetailType getByName(String name, long parentId) throws JoorJensException {
        final String query = "SELECT t from ProductDetailType t where t.name=:name and t.parent.id=:parentId";
        final Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("parentId", parentId);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<ProductDetailType> search(long id, String name, long parentId
            , boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductDetailType> getAllTypes(long id, String name, long parentId
            , boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, like, block, false);
        return findAllBy(String.format(query.getFirst(), "t"), query.getSecond());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, long parentId
            , boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductDetailType> getAll(long parentId) throws JoorJensException {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        query.append(String.format("SELECT t  FROM ProductDetailType t WHERE t.id!=%d", ProductDetailType.getFakeId()));
        if (parentId > 0) {
            query.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
        }
        return findAllBy(query.toString(), map);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, long parentId
            , boolean like, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(String.format(" FROM ProductDetailType t WHERE t.id!=%d", ProductDetailType.getFakeId()));
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
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