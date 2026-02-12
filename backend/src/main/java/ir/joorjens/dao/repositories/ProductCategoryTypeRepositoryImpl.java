package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductCategoryTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ProductCategoryType;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductCategoryTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, ProductCategoryType>
        implements ProductCategoryTypeRepository {

    @Override
    public int update(long id, int childCount) throws JoorJensException {
        if (childCount != 0) {
            String query = "UPDATE ProductCategoryType t set t.childCount=t.childCount+:childCount WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("childCount", childCount);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<ProductCategoryType> search(long id, String name, long parentId
            , Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductCategoryType> getAllTypes(long id, String name, long parentId, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, block, false);
        return findAllBy(String.format(query.getFirst(), "t"), query.getSecond());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, long parentId, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, parentId, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductCategoryType> getAll(long parentId) throws JoorJensException {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        query.append(String.format("SELECT t  FROM ProductCategoryType t WHERE t.id!=%d", ProductCategoryType.getFakeId()));
        if (parentId > 0) {
            query.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
        }
        return findAllBy(query.toString(), map);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, long parentId, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(String.format(" FROM ProductCategoryType t WHERE t.id!=%d", ProductCategoryType.getFakeId()));
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name=:name");
            map.put("name", name);
        }
        if (parentId > 0) {
            query.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
            if (block != null) {
                query.append(" and t.parent.block=:parentBlock");
                map.put("parentBlock", block);
            }
        }

        final String order = TypeEnumeration.getOrderByWeightName("t");
        query.append(" ").append(order);

        return new Pair<>(query.toString(), map);
    }
}