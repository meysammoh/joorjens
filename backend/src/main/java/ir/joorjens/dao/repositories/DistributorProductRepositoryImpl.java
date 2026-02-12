package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorProductRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.businessEntity.DashboardDistributorProductInfo;
import ir.joorjens.model.entity.DistributorProduct;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.*;
import java.util.stream.Collectors;

public class DistributorProductRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorProduct>
        implements DistributorProductRepository {

    @Override
    public int update(long id, int price, int priceMin) throws JoorJensException {
        if (price > 0) {
            final String query = "UPDATE DistributorProduct t set t.updatedTime=:updatedTime" +
                    ",t.price=:price,t.priceMin=:priceMin WHERE t.id=:id";
            final Map<String, Object> map = new HashMap<>();
            map.put("updatedTime", Utility.getCurrentTime());
            map.put("price", price);
            map.put("priceMin", priceMin);
            map.put("id", id);
            return update(query, map);
        }
        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<DistributorProduct> search(long id, Set<Long> distributorIds, long productId
            , String productBarcode, String productName, int priceFrom, int priceTo, Boolean onlyBundling
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , Boolean supportCheck, Set<Long> supportAreaIds
            , final boolean typesAreParent, final boolean onlyStocks
            , String orderTypeIds, String ascDescs
            , boolean like, Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, distributorIds, productId
                , productBarcode, productName, priceFrom, priceTo, onlyBundling
                , productBrandTypeIds, productCategoryTypeIds
                , supportCheck, supportAreaIds, typesAreParent, onlyStocks
                , orderTypeIds, ascDescs, like, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Third<Long, String, String>> getAllPairs(long id, Set<Long> distributorIds, long productId
            , String productBarcode, String productName, int priceFrom, int priceTo, Boolean onlyBundling
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , Boolean supportCheck, Set<Long> supportAreaIds
            , final boolean typesAreParent, final boolean onlyStocks
            , String orderTypeIds, String ascDescs, boolean like, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id, distributorIds, productId
                , productBarcode, productName, priceFrom, priceTo, onlyBundling
                , productBrandTypeIds, productCategoryTypeIds
                , supportCheck, supportAreaIds, typesAreParent, onlyStocks
                , orderTypeIds, ascDescs, like, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Third<>((long) obj[0], (String) obj[1], (String) obj[2]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id, Set<Long> distributorIds, long productId
            , String productBarcode, String productName, int priceFrom, int priceTo, Boolean onlyBundling
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , Boolean supportCheck, Set<Long> supportAreaIds
            , final boolean typesAreParent, final boolean onlyStocks
            , String orderTypeIds, String ascDescs, boolean like, Boolean block, boolean pair) throws JoorJensException {
        final StringBuilder table = new StringBuilder("DistributorProduct t");
        final StringBuilder where = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();

        addCommonQuery(where, map, id, block);
        if (priceFrom > 0) {
            where.append(" and t.price>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            where.append(" and t.price<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (onlyBundling != null) {
            where.append(" and t.onlyBundling=:onlyBundling");
            map.put("onlyBundling", onlyBundling);
        }

        if (distributorIds != null && distributorIds.size() > 0) {
            where.append(" and t.distributor.id in (:distributorIds)");
            map.put("distributorIds", distributorIds);
        }
        if (productId > 0) {
            where.append(" and t.product.id=:productId");
            map.put("productId", productId);
        }
        if (!Utility.isEmpty(productBarcode)) {
            where.append(" and t.product.barcode=:productBarcode");
            map.put("productBarcode", productBarcode);
        }
        if (!Utility.isEmpty(productName)) {
            where.append(" and t.product.name").append(like ? " like " : "=").append(":productName");
            map.put("productName", like ? '%' + productName + '%' : productName);
        }
        if (productBrandTypeIds != null && productBrandTypeIds.size() > 0) {
            where.append(" and t.product.productBrandType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productBrandTypeIds)");
            map.put("productBrandTypeIds", productBrandTypeIds);
        }
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            where.append(" and t.product.productCategoryType.");
            if (typesAreParent) {
                where.append("parent.");
            }
            where.append("id in (:productCategoryTypeIds)");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
        }
        if (onlyStocks) {
            where.append(" and t.stock>=t.minOrder");
        }

        if (supportCheck != null) {
            where.append(" and t.supportCheck=:supportCheck");
            map.put("supportCheck", supportCheck);
        }

        if (block != null) {
            where.append(" and t.distributor.block=:disBlock and t.product.block=:productBlock");
            where.append(" and t.product.productCategoryType.block=:catBlock and t.product.productBrandType.block=:brandBlock");
            where.append(" and t.product.productCategoryType.parent.block=:parentCatBlock and t.product.productBrandType.parent.block=:parentBrandBlock");
            map.put("disBlock", block);
            map.put("productBlock", block);
            map.put("catBlock", block);
            map.put("brandBlock", block);
            map.put("parentCatBlock", block);
            map.put("parentBrandBlock", block);
        }

        boolean joined = false;
        if (supportAreaIds != null && supportAreaIds.size() > 0) {
            table.append(" join t.distributor.supportAreas dsa");
            where.append(" and dsa.id in (:supportAreaIds)");
            map.put("supportAreaIds", supportAreaIds);
            joined = true;
        }

        final String order = TypeEnumeration.getOrder("t", orderTypeIds, ascDescs);
        final StringBuilder query = new StringBuilder("SELECT");
        if (joined) {
            query.append(" distinct");
        }
        if (pair) {
            query.append(" t.id,t.product.name,t.distributor.name");
        } else {
            query.append(" %s");
        }
        query.append(" FROM ").append(table).append(" WHERE 1=1 ").append(where).append(" ").append(order);

        return new Pair<>(query.toString(), map);
    }

    @Override
    public FetchResult<DashboardDistributorProductInfo> dashboard(final long distributorId
            , final Boolean onlyBundling, final Boolean supportCheck
            , final long productId, final String productBarcode
            , final Set<Long> productBrandTypeIds, final Set<Long> productCategoryTypeIds, final boolean typesAreParent
            , final int max, final int offset) throws JoorJensException {

        final String fields = "count(t.id) as allCnt"
                + ", sum(saleCount) as saleCount"
                + ", sum(stock) as stock"
                + ", sum(case when stock>0 then 1 else 0 end) as stockProduct"
                + ", sum(case when stock>stockWarn then 1 else 0 end) as stockProductWithoutWarn"
                + ", sum(stock * price) as price"
                + ", sum(stock * priceMin) as priceMin"
                + ", avg(settlementPercent) as settlementPercentAvg";
        final StringBuilder query = new StringBuilder("Select %s from DistributorProduct t where 1=1");
        final Map<String, Object> map = new HashMap<>();

        if (onlyBundling != null) {
            query.append(" and t.onlyBundling=:onlyBundling");
            map.put("onlyBundling", onlyBundling);
        }
        if (supportCheck != null) {
            query.append(" and t.supportCheck=:supportCheck");
            map.put("supportCheck", supportCheck);
        }

        if (distributorId > 0) {
            query.append(" and t.distributor.id=:distributorId)");
            map.put("distributorId", distributorId);
        }
        if (productId > 0) {
            query.append(" and t.product.id=:productId");
            map.put("productId", productId);
        }
        if (!Utility.isEmpty(productBarcode)) {
            query.append(" and t.product.barcode=:productBarcode");
            map.put("productBarcode", productBarcode);
        }
        if (productBrandTypeIds != null && productBrandTypeIds.size() > 0) {
            query.append(" and t.product.productBrandType.");
            if (typesAreParent) {
                query.append("parent.");
            }
            query.append("id in (:productBrandTypeIds)");
            map.put("productBrandTypeIds", productBrandTypeIds);
        }
        if (productCategoryTypeIds != null && productCategoryTypeIds.size() > 0) {
            query.append(" and t.product.productCategoryType.");
            if (typesAreParent) {
                query.append("parent.");
            }
            query.append("id in (:productCategoryTypeIds)");
            map.put("productCategoryTypeIds", productCategoryTypeIds);
        }

        final FetchResult<Object> tmp = searchAdvanced(query.toString(), fields, map, null, max, offset);
        final FetchResult<DashboardDistributorProductInfo> result = new FetchResult<>(tmp.getMax(), tmp.getOffset(), tmp.getTotal());
        for (Object row : tmp.getResult()) {
            result.addResult(new DashboardDistributorProductInfo(row));
        }
        return result;
    }
}