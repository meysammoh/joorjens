package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorPackageRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.DistributorPackage;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DistributorPackageRepositoryImpl
        extends RepositoryImpAbstract<Long, DistributorPackage>
        implements DistributorPackageRepository {

    @Override
    public FetchResult<DistributorPackage> search(final long id, final Set<Long> distributorIds
            , final long productId, final String productBarcode, String productName, String packageName
            , final Boolean bundlingOrDiscount, final int from, final int to
            , final Boolean supportCheck, final boolean onlyStocks, final Boolean expired
            , final String orderTypeIds, final String ascDescs
            , final boolean like, final Boolean block, final int max, final int offset) throws JoorJensException {
        final StringBuilder table = new StringBuilder("DistributorPackage t");
        final StringBuilder where = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();

        addCommonQuery(where, map, id, block);
        if (!Utility.isEmpty(packageName)) {
            where.append(" and t.name=:packageName");
            map.put("packageName", packageName);
        }
        if (bundlingOrDiscount != null) {
            where.append(" and t.bundlingOrDiscount=:bundlingOrDiscount");
            map.put("bundlingOrDiscount", bundlingOrDiscount);
        }
        if (from > 0) {
            where.append(" and t.from>=:from");
            map.put("from", from);
        }
        if (to > 0 && to >= from) {
            where.append(" and t.to<=:to");
            map.put("to", to);
        }
        if (distributorIds != null && distributorIds.size() > 0) {
            where.append(" and t.distributor.id in (:distributorIds)");
            map.put("distributorIds", distributorIds);
        }
        if (onlyStocks) {
            where.append(" and t.stock>=t.maxOrder");
        }
        if (expired != null) {
            if(expired) {
                where.append(" and t.to<=:expiredTime");
            } else {
                where.append(" and t.from<=:expiredTime and t.to>=:expiredTime");
            }
            map.put("expiredTime", Utility.getCurrentTime());
        }
        if (supportCheck != null) {
            where.append(" and t.supportCheck=:supportCheck");
            map.put("supportCheck", supportCheck);
        }
        if (block != null) {
            where.append(" and t.distributor.block=:distributorBlock");
            map.put("distributorBlock", block);
        }

        final boolean joined = addProductInfo(table, where, map, productId, productBarcode, productName
                , like, block);

        final String order = TypeEnumeration.getOrder("t", orderTypeIds, ascDescs);
        final StringBuilder query = new StringBuilder("SELECT");
        if (joined) {
            query.append(" distinct");
        }
        query.append(" %s FROM ").append(table).append(" WHERE 1=1 ").append(where).append(" ").append(order);

        return search(query.toString(), map, max, offset);
    }

    @Override
    public long getProductCountIn(long productId, String productBarcode, Boolean bundlingOrDiscount, Boolean block) throws JoorJensException {
        if (productId <= 0 && Utility.isEmpty(productBarcode)) {
            return 0;
        }

        final StringBuilder table = new StringBuilder("DistributorPackage t");
        final StringBuilder where = new StringBuilder(" WHERE t.from<=:currentTime and t.to>=:currentTime");
        final Map<String, Object> map = new HashMap<>();
        map.put("currentTime", Utility.getCurrentTime());

        if (bundlingOrDiscount != null) {
            where.append(" and t.bundlingOrDiscount=:bundlingOrDiscount");
            map.put("bundlingOrDiscount", bundlingOrDiscount);
        }

        addProductInfo(table, where, map, productId, productBarcode, null, false, block);

        final String query = "SELECT distinct count(distinct t.id) FROM " + table + where;
        final List rows = findAllBy(query, map);
        if (rows != null && rows.size() == 1) {
            return Long.parseLong(rows.get(0).toString());
        }
        return 0;
    }

    private boolean addProductInfo(final StringBuilder table, final StringBuilder where, final Map<String, Object> map
            , long productId, String productBarcode, String productName, boolean like, Boolean block) {
        boolean joined = false;
        if (productId > 0) {
            table.append(" join t.packageProducts pp");
            where.append(" and pp.distributorProduct.product.id=:productId");
            map.put("productId", productId);
            joined = true;
        } else if (!Utility.isEmpty(productBarcode)) {
            table.append(" join t.packageProducts pp");
            where.append(" and pp.distributorProduct.product.barcode=:productBarcode");
            map.put("productBarcode", productBarcode);
            joined = true;
        } else if (!Utility.isEmpty(productName)) {
            table.append(" join t.packageProducts pp");
            where.append(" and pp.distributorProduct.product.name").append(like ? " like " : "=").append(":productName");
            map.put("productName", like ? '%' + productName + '%' : productName);
            joined = true;
        }

        if (joined && block != null) {
            where.append(" and pp.distributorProduct.product.block=:ppdpBlock");
            map.put("ppdpBlock", block);
        }
        return joined;
    }
}