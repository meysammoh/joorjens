package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Product;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductRepositoryImpl
        extends RepositoryImpAbstract<Long, Product>
        implements ProductRepository {

    @Override
    public int update(final List<Pair<String, Integer>> barcodePriceList) throws JoorJensException {
        if (barcodePriceList.size() > 0) {
            final StringBuilder query = new StringBuilder("UPDATE Product t set t.priceConsumer=(CASE t.barcode");
            final StringBuilder where = new StringBuilder("WHERE t.barcode IN(");
            int counter = 0;
            for (Pair<String, Integer> barcodePrice : barcodePriceList) {
                query.append(String.format(" WHEN %s THEN %d", barcodePrice.getFirst(), barcodePrice.getSecond()));
                if (++counter > 1) {
                    where.append(',');
                }
                where.append(barcodePrice.getFirst());
            }
            where.append(")");
            query.append(" END) ").append(where);
            return update(query.toString(), null);
        }
        return 0;
    }

    @Override
    public int update(long id, int price) throws JoorJensException {
        if (price > 0) {
            final String query = "UPDATE Product t set t.updatedTime=:updatedTime, t.priceConsumer=:price WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("updatedTime", Utility.getCurrentTime());
            map.put("price", price);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    public Product find(String barcode) throws JoorJensException {
        final String query = "SELECT t FROM Product t WHERE t.barcode=:barcode";
        final Map<String, Object> map = new HashMap<>();
        map.put("barcode", barcode);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Product> search(long id, String barcode, String name
            , int priceFrom, int priceTo, float rateFrom, float rateTo
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs
            , boolean firstPage, boolean like, Boolean blockedByDistributor, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, barcode, name
                , priceFrom, priceTo, rateFrom, rateTo, productBrandTypeIds, productCategoryTypeIds
                , typesAreParent, orderTypeIds, ascDescs, firstPage, like, blockedByDistributor, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Third<Long, String, String>> getAllPairs(long id, String barcode, String name
            , int priceFrom, int priceTo, float rateFrom, float rateTo
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs
            , boolean firstPage, boolean like, Boolean blockedByDistributor, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, barcode, name
                , priceFrom, priceTo, rateFrom, rateTo, productBrandTypeIds, productCategoryTypeIds
                , typesAreParent, orderTypeIds, ascDescs, firstPage, like, blockedByDistributor, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Third<>((long) obj[0], (String) obj[1], (String) obj[2]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, String barcode, String name
            , int priceFrom, int priceTo, float rateFrom, float rateTo
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs
            , boolean firstPage, boolean like, Boolean blockedByDistributor, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.name,t.barcode");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM Product t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(barcode)) {
            query.append(" and t.barcode=:barcode");
            map.put("barcode", barcode);
        }
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }

        if (priceFrom > 0) {
            query.append(" and t.priceConsumer>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            query.append(" and t.priceConsumer<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (rateFrom > 0) {
            query.append(" and t.rateSum/t.rateCount>=:rateFrom");
            map.put("rateFrom", rateFrom);
        }
        if (rateTo > 0 && rateTo >= rateFrom) {
            query.append(" and t.rateSum/t.rateCount<=:rateTo");
            map.put("rateTo", rateTo);
        }

        if (productBrandTypeIds != null && productBrandTypeIds.size() > 0) {
            query.append(" and t.productBrandType.");
            if (typesAreParent) {
                query.append("parent.");
            }
            query.append("id in (:productBrandTypeIds)");
            map.put("productBrandTypeIds", productBrandTypeIds);
        }
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            query.append(" and t.productCategoryType.");
            if (typesAreParent) {
                query.append("parent.");
            }
            query.append("id in (:productCategoryTypeIds)");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
        }

        if(firstPage) {
            query.append(" and exists (select distinct dp.product.id from DistributorProduct dp where dp.product.id=t.id");
            if(blockedByDistributor != null) {
                query.append(" and dp.block=:blockedByDistributor");
                map.put("blockedByDistributor", blockedByDistributor);
            }
            query.append(")");
        }

        if (block != null) {
            query.append(" and t.productBrandType.block=:brandBlock");
            query.append(" and t.productCategoryType.block=:catBlock");
            map.put("brandBlock", block);
            map.put("catBlock", block);
        }

        final String order = TypeEnumeration.getOrder("t", orderTypeIds, ascDescs);
        query.append(" ").append(order);

        return new Pair<>(query.toString(), map);
    }
}