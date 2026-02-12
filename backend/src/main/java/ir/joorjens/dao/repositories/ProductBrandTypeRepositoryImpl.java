package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductBrandTypeRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.ProductBrandType;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductBrandTypeRepositoryImpl
        extends RepositoryImpAbstract<Long, ProductBrandType>
        implements ProductBrandTypeRepository {

    @Override
    public int update(long id, int childCount) throws JoorJensException {
        if (childCount != 0) {
            String query = "UPDATE ProductBrandType t set t.childCount=t.childCount+:childCount WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("childCount", childCount);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductBrandType getByName(String name, int pbType, long parentId) throws JoorJensException {
        final String query = "SELECT t from ProductBrandType t where t.pbType=:pbType and t.name=:name and t.parent.id=:parentId";
        final Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("pbType", pbType);
        map.put("parentId", parentId);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<ProductBrandType> search(long id, String name, int pbType, long parentId
            , Set<Long> productCategoryTypeIds, boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, pbType, parentId
                , productCategoryTypeIds, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductBrandType> getAllProductBrandTypes(long id, String name, int pbType, long parentId
            , Set<Long> productCategoryTypeIds, boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, pbType, parentId
                , productCategoryTypeIds, like, block, false);
        return findAllBy(String.format(query.getFirst(), "t"), query.getSecond());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Pair<Long, String>> getAllPairs(long id, String name, int pbType, long parentId
            , Set<Long> productCategoryTypeIds, boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, name, pbType, parentId
                , productCategoryTypeIds, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Pair<>((long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductBrandType> getAll(long parentId) throws JoorJensException {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        query.append(String.format("SELECT t  FROM ProductBrandType t WHERE t.id!=%d", ProductBrandType.getFakeId()));
        if (parentId > 0) {
            query.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
        }
        return findAllBy(query.toString(), map);
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String name, int pbType, long parentId
            , Set<Long> productCategoryTypeIds, boolean like, Boolean block, boolean pair) {
        final StringBuilder table = new StringBuilder("ProductBrandType t");
        final Map<String, Object> map = new HashMap<>();
        final StringBuilder where = new StringBuilder(String.format(" WHERE t.id!=%d", ProductBrandType.getFakeId()));
        addCommonQuery(where, map, id, block);
        if (!Utility.isEmpty(name)) {
            where.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }
        if (TypeEnumeration.get(pbType) != null) {
            where.append(" and t.pbType=:pbType");
            map.put("pbType", pbType);
        }
        if (parentId > 0) {
            where.append(" and t.parent.id=:parentId");
            map.put("parentId", parentId);
            if (block != null) {
                where.append(" and t.parent.block=:parentBlock");
                map.put("parentBlock", block);
            }
        }

        boolean joined = false;
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            table.append(" join t.productCategoryTypes pct");
            where.append(" and pct.id in (:productCategoryTypeIds)");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
            joined = true;
            if (block != null) {
                where.append(" and pct.block=:pctBlock");
                map.put("pctBlock", block);
            }
        }

        final String order = TypeEnumeration.getOrderByWeightName("t");
        final StringBuilder query = new StringBuilder("SELECT");
        if (joined) {
            query.append(" distinct");
        }
        if (pair) {
            query.append(" t.id,t.name");
        } else {
            query.append(" %s");
        }
        query.append(" FROM ").append(table).append(where).append(" ").append(order);

        return new Pair<>(query.toString(), map);
    }
}