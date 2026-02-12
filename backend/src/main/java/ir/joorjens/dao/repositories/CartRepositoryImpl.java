package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CartRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.businessEntity.CartOrderInfo;
import ir.joorjens.model.entity.Cart;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.*;

public class CartRepositoryImpl
        extends RepositoryImpAbstract<Long, Cart>
        implements CartRepository {

    @Override
    public Cart find(final String serial) throws JoorJensException {
        final String query = "SELECT t FROM Cart t WHERE t.serial=:serial";
        final Map<String, Object> map = new HashMap<>();
        map.put("serial", serial);
        return findBy(query, map);
    }

    @Override
    public FetchResult<Cart> search(final long id, final String serial
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , Boolean finished
            , long storeId, String storeName
            , long storeManagerId, String storeManagerMobile
            , final boolean like, final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Cart t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(serial)) {
            query.append(" and t.serial=:serial");
            map.put("serial", serial);
        }

        setFields(query, map, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, countFrom, countTo
                , packCountFrom, packCountTo, packPriceConsumerFrom, packPriceConsumerTo
                , packPriceFrom, packPriceTo, packPriceDiscountFrom, packPriceDiscountTo, finished);

        if (storeId > 0) {
            query.append(" and t.store.id=:storeId");
            map.put("storeId", storeId);
        }
        if (!Utility.isEmpty(storeName)) {
            query.append(" and t.store.name").append(like ? " like " : "=").append(":storeName");
            map.put("storeName", like ? '%' + storeName + '%' : storeName);
        }
        if (storeManagerId > 0) {
            query.append(" and t.store.manager.id=:customerId");
            map.put("customerId", storeManagerId);
        }
        if (!Utility.isEmpty(storeManagerMobile)) {
            query.append(" AND t.store.manager.mobileNumber=:storeManagerMobile");
            map.put("storeManagerMobile", storeManagerMobile);
        }

        return search(query.toString(), map, max, offset);
    }

    @Override
    public FetchResult<CartPrice> dashboardSales(final int timeStampId
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo, Boolean finished
            , final boolean groupByAreas, final boolean groupByDistributor, final Set<Long> areaIds
            , final long productId, final String productBarcode
            , final long disProductId, final long disPackageId
            , final long distributorId
            , final long storeId, final long managerId, final String managerMobile
            , final int max, final int offset) throws JoorJensException {

        final TypeEnumeration timeStamp = TypeEnumeration.get(timeStampId);
        final String fields = "count(DISTINCT t.id) as total"
                + ",sum(t.cartPrice.count) as count"
                + ",sum(t.cartPrice.packCount) as packCount"
                + ",sum(t.cartPrice.packPriceConsumer) as packPriceConsumer"
                + ",sum(t.cartPrice.packPrice) as packPrice"
                + ",sum(t.cartPrice.packPriceDiscount) as packPriceDiscount"
                + ",sum(t.cartPrice.amountCheck) as amountCheck"
                + ",sum(t.cartPrice.amountCache) as amountCache"
                + ",sum(t.cartPrice.amountCredit) as amountCredit"
                + ",avg(t.cartPrice.settlementPercent) as settlementPercent"
                + ",sum(t.cartPrice.joorJensShare) as joorJensShare";
        final String table, timeColumn, areaColumn, distributorColumn;
        final StringBuilder where = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (productId > 0 || !Utility.isEmpty(productBarcode)
                || disProductId > 0 || disPackageId > 0) {
            table = "CartDistributorPackduct";
            timeColumn = getColumnTime("t.cartDistributor.cart", timeStamp);
            areaColumn = "t.cartDistributor.cart.store.areaZone";
            distributorColumn = "t.cartDistributor.distributor.id";
            if (productId > 0) {
                where.append(" AND t.distributorProduct.product.id=:productId");
                map.put("productId", productId);
            } else if (!Utility.isEmpty(productBarcode)) {
                where.append(" AND t.distributorProduct.product.barcode=:productBarcode");
                map.put("productBarcode", productBarcode);
            } else if (disProductId > 0) {
                where.append(" AND t.distributorProduct.id=:disProductId");
                map.put("disProductId", disProductId);
            } else if (disPackageId > 0) {
                where.append(" AND t.distributorPackage.id=:disPackageId");
                map.put("disPackageId", disPackageId);
            }
            if (distributorId > 0) {
                where.append(" AND t.cartDistributor.distributor.id=:distributorId");
                map.put("distributorId", distributorId);
            }
            if (storeId > 0) {
                where.append(" AND t.cartDistributor.cart.store.id=:storeId");
                map.put("storeId", storeId);
            } else if (managerId > 0) {
                where.append(" AND t.cartDistributor.cart.store.manager.id=:managerId");
                map.put("managerId", managerId);
            } else if (!Utility.isEmpty(managerMobile)) {
                where.append(" AND t.cartDistributor.cart.store.manager.mobileNumber=:managerMobile");
                map.put("managerMobile", managerMobile);
            }
        } else if (groupByDistributor || distributorId > 0) {
            table = "CartDistributor";
            timeColumn = getColumnTime("t.cart", timeStamp);
            areaColumn = "t.cart.store.areaZone";
            distributorColumn = "t.distributor.id";
            if (distributorId > 0) {
                where.append(" AND t.distributor.id=:distributorId");
                map.put("distributorId", distributorId);
            }
            if (storeId > 0) {
                where.append(" AND t.cart.store.id=:storeId");
                map.put("storeId", storeId);
            } else if (managerId > 0) {
                where.append(" AND t.cart.store.manager.id=:managerId");
                map.put("managerId", managerId);
            } else if (!Utility.isEmpty(managerMobile)) {
                where.append(" AND t.cart.store.manager.mobileNumber=:managerMobile");
                map.put("managerMobile", managerMobile);
            }
        } else {
            table = "Cart";
            areaColumn = "t.store.areaZone";
            timeColumn = getColumnTime("t", timeStamp);
            distributorColumn = null;
            if (storeId > 0) {
                where.append(" AND t.store.id=:storeId");
                map.put("storeId", storeId);
            } else if (managerId > 0) {
                where.append(" AND t.store.manager.id=:managerId");
                map.put("managerId", managerId);
            } else if (!Utility.isEmpty(managerMobile)) {
                where.append(" AND t.store.manager.mobileNumber=:managerMobile");
                map.put("managerMobile", managerMobile);
            }
        }
        if (areaIds != null && areaIds.size() > 0) {
            where.append(" AND ").append(areaColumn).append(".id in :areaIds");
            map.put("areaIds", areaIds);
        }

        setFields(where, map, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, finished);

        boolean group = false, order = false;
        final boolean groupByTime = !Utility.isEmpty(timeColumn);
        final boolean groupByDis = groupByDistributor && !Utility.isEmpty(distributorColumn);
        final StringBuilder groupBy = new StringBuilder();
        final StringBuilder orderBy = new StringBuilder();
        if (groupByTime) {
            groupBy.append(timeColumn);
            orderBy.append(timeColumn).append(" DESC");
            group = order = true;
        }
        if (groupByAreas) {
            if (group) {
                groupBy.append(',');
                orderBy.append(',');
            }
            groupBy.append(areaColumn);
            orderBy.append(areaColumn).append(" DESC");
            group = order = true;
        }
        if (groupByDis) {
            if (group) {
                groupBy.append(',');
                orderBy.append(',');
            }
            groupBy.append(distributorColumn);
            orderBy.append(distributorColumn).append(" DESC");
            group = order = true;
        }

        final String query = "SELECT %s FROM " + table + " t WHERE 1=1" + where
                + (group ? " GROUP BY " + groupBy : "")
                + (order ? " ORDER BY " + orderBy : "");
        final FetchResult<Object> tmp = searchAdvanced(query, groupBy.toString() + (group ? ',' : ' ') + fields
                , map, null, max, offset);
        final FetchResult<CartPrice> result = new FetchResult<>(tmp.getMax(), tmp.getOffset(), tmp.getTotal());
        for (Object row : tmp.getResult()) {
            result.addResult(new CartPrice(row, groupByTime, groupByAreas, groupByDistributor, timeStamp));
        }
        return result;
    }

    @Override
    public FetchResult<CartOrderInfo> dashboardOrders(final int timeStampId
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo, Boolean finished
            , final boolean groupByAreas, final boolean groupByDistributor, final Set<Long> areaIds
            , final long productId, final String productBarcode
            , final long disProductId, final long disPackageId
            , final long distributorId
            , final long storeId, final long managerId, final String managerMobile
            , final int max, final int offset) throws JoorJensException {

        final TypeEnumeration timeStamp = TypeEnumeration.get(timeStampId);
        final String fields = "count(t.orderStatusType.id)" //
                + ", count(DISTINCT t.cartDistributor.cart.id)" //
                , areaColumn = "t.cartDistributor.cart.store.areaZone" //
                , distributorColumn = "t.cartDistributor.distributor.id" //
                , timeColumn = getColumnTime("t.cartDistributor.cart", timeStamp);
        final StringBuilder where = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (productId > 0) {
            where.append(" AND t.distributorProduct.product.id=:productId");
            map.put("productId", productId);
        } else if (!Utility.isEmpty(productBarcode)) {
            where.append(" AND t.distributorProduct.product.barcode=:productBarcode");
            map.put("productBarcode", productBarcode);
        } else if (disProductId > 0) {
            where.append(" AND t.distributorProduct.id=:disProductId");
            map.put("disProductId", disProductId);
        } else if (disPackageId > 0) {
            where.append(" AND t.distributorPackage.id=:disPackageId");
            map.put("disPackageId", disPackageId);
        }
        if (distributorId > 0) {
            where.append(" AND t.cartDistributor.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        if (storeId > 0) {
            where.append(" AND t.cartDistributor.cart.store.id=:storeId");
            map.put("storeId", storeId);
        } else if (managerId > 0) {
            where.append(" AND t.cartDistributor.cart.store.manager.id=:managerId");
            map.put("managerId", managerId);
        } else if (!Utility.isEmpty(managerMobile)) {
            where.append(" AND t.cartDistributor.cart.store.manager.mobileNumber=:managerMobile");
            map.put("managerMobile", managerMobile);
        }
        if (areaIds != null && areaIds.size() > 0) {
            where.append(" AND ").append(areaColumn).append(".id in :areaIds");
            map.put("areaIds", areaIds);
        }

        setFields(where, map, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, 0, 0
                , 0, 0, 0, 0, 0, 0, 0, 0, finished);

        final boolean groupByTime = !Utility.isEmpty(timeColumn);
        final StringBuilder groupBy = new StringBuilder("t.orderStatusType");
        final StringBuilder orderBy = new StringBuilder("t.orderStatusType DESC");
        if (groupByTime) {
            groupBy.append(',').append(timeColumn);
            orderBy.append(',').append(timeColumn).append(" DESC");
        }
        if (groupByAreas) {
            groupBy.append(',').append(areaColumn);
            orderBy.append(',').append(areaColumn).append(" DESC");
        }
        if (groupByDistributor) {
            groupBy.append(',').append(distributorColumn);
            orderBy.append(',').append(distributorColumn).append(" DESC");
        }

        final String query = "SELECT %s FROM CartDistributorPackduct t WHERE 1=1" + where
                + " GROUP BY " + groupBy
                + " ORDER BY " + orderBy;
        final FetchResult<Object> tmp = searchAdvanced(query, groupBy.toString() + ',' + fields
                , map, null, max, offset);
        final FetchResult<CartOrderInfo> result = new FetchResult<>(tmp.getMax(), tmp.getOffset(), tmp.getTotal());
        for (Object row : tmp.getResult()) {
            result.addResult(new CartOrderInfo(row, groupByTime, groupByAreas, groupByDistributor, timeStamp));
        }
        final String queryCount = "SELECT count(DISTINCT t.cartDistributor.cart.id) FROM CartDistributorPackduct t WHERE 1=1" + where;
        result.setInfo(count(queryCount, map));
        return result;
    }

    //----------------------------------------------------------------------------------------------------------

    private static String getColumnTime(final String tablePrefix, final TypeEnumeration timeStamp) {
        String timeColumn = null;
        if (timeStamp != null) {
            switch (timeStamp) {
                case TS_DAY:
                    timeColumn = "timeDay";
                    break;
                case TS_WEEK:
                    timeColumn = "timeWeek";
                    break;
                case TS_MONTH:
                    timeColumn = "timeMonth";
                    break;
            }
        }
        return timeColumn != null ? String.format("%s.%s", tablePrefix, timeColumn) : null;
    }

    static void setFields(final StringBuilder query, final Map<String, Object> map
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , final Boolean finished) {

        if (timeFrom > 0) {
            query.append(" and t.createdTime>=:timeFrom");
            map.put("timeFrom", timeFrom);
        }
        if (timeTo > 0 && timeTo >= timeFrom) {
            query.append(" and t.createdTime<=:timeTo");
            map.put("timeTo", timeTo);
        }
        if (countFrom > 0) {
            query.append(" and t.cartPrice.count>=:countFrom");
            map.put("countFrom", countFrom);
        }
        if (countTo > 0 && countTo >= countFrom) {
            query.append(" and t.cartPrice.count<=:countTo");
            map.put("countTo", countTo);
        }
        if (packCountFrom > 0) {
            query.append(" and t.packCount>=:packCountFrom");
            map.put("packCountFrom", packCountFrom);
        }
        if (packCountTo > 0 && packCountTo >= packCountFrom) {
            query.append(" and t.packCount<=:packCountTo");
            map.put("packCountTo", packCountTo);
        }
        if (packPriceConsumerFrom > 0) {
            query.append(" and t.packPriceConsumer>=:packPriceConsumerFrom");
            map.put("packPriceConsumerFrom", packPriceConsumerFrom);
        }
        if (packPriceConsumerTo > 0 && packPriceConsumerTo >= packPriceConsumerFrom) {
            query.append(" and t.packPriceConsumer<=:packPriceConsumerTo");
            map.put("packPriceConsumerTo", packPriceConsumerTo);
        }
        if (packPriceFrom > 0) {
            query.append(" and t.packPrice>=:packPriceFrom");
            map.put("packPriceFrom", packPriceFrom);
        }
        if (packPriceTo > 0 && packPriceTo >= packPriceFrom) {
            query.append(" and t.packPrice<=:packPriceTo");
            map.put("packPriceTo", packPriceTo);
        }
        if (packPriceDiscountFrom > 0) {
            query.append(" and t.packPriceDiscount>=:packPriceDiscountFrom");
            map.put("packPriceDiscountFrom", packPriceDiscountFrom);
        }
        if (packPriceDiscountTo > 0 && packPriceDiscountTo >= packPriceDiscountFrom) {
            query.append(" and t.packPriceDiscount<=:packPriceDiscountTo");
            map.put("packPriceDiscountTo", packPriceDiscountTo);
        }
        if (timeFinishedFrom > 0) {
            query.append(" and t.timeFinished>=:timeFinishedFrom");
            map.put("timeFinishedFrom", timeFinishedFrom);
        }
        if (timeFinishedTo > 0 && timeFinishedTo >= timeFinishedFrom) {
            query.append(" and t.timeFinished<=:timeFinishedTo");
            map.put("timeFinishedTo", timeFinishedTo);
        }
        if (finished != null) {
            if (finished) {
                query.append(" and t.timeFinished>0");
            } else {
                query.append(" and t.timeFinished=0");
            }
        }
    }
}