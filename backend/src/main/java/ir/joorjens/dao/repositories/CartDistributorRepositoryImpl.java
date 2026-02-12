package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CartDistributorRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.CartDistributor;

import java.util.HashMap;
import java.util.Map;

public class CartDistributorRepositoryImpl
        extends RepositoryImpAbstract<Long, CartDistributor>
        implements CartDistributorRepository {

    @Override
    public int updateDeliverer(final long cartDistId, final long distDelivererId) throws JoorJensException {
        final String query = "UPDATE CartDistributor t set t.updatedTime=:updatedTime" +
                ",t.deliverer.id=:distDelivererId WHERE t.id=:id";
        final Map<String, Object> map = new HashMap<>();
        map.put("updatedTime", Utility.getCurrentTime());
        map.put("distDelivererId", distDelivererId);
        map.put("id", cartDistId);
        return update(query, map);
    }

    @Override
    public CartDistributor find(final String serial) throws JoorJensException {
        final Map<String, Object> map = new HashMap<>();
        final String serialQuery = getSerialQuery(serial, map);
        if (serialQuery == null) {
            return null;
        }
        final String query = "SELECT t FROM CartDistributor t WHERE " + serialQuery;
        return findBy(query, map);
    }

    @Override
    public FetchResult<CartDistributor> search(final long id, final String serial
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , final Boolean finished
            , final long storeId, final String storeName
            , final long storeManagerId, final String storeManagerMobile, final long distributorId
            , final long delivererId, final String delivererMobile
            , final boolean like, final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM CartDistributor t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(serial)) {
            final String serialQuery = getSerialQuery(serial, map);
            if (serialQuery != null) {
                query.append(" and ").append(serialQuery);
            }
        }

        CartRepositoryImpl.setFields(query, map, timeFrom, timeTo, timeFinishedFrom, timeFinishedTo, countFrom, countTo
                , packCountFrom, packCountTo, packPriceConsumerFrom, packPriceConsumerTo
                , packPriceFrom, packPriceTo, packPriceDiscountFrom, packPriceDiscountTo, finished);

        if (storeId > 0) {
            query.append(" and t.cart.store.id=:storeId");
            map.put("storeId", storeId);
        }
        if (!Utility.isEmpty(storeName)) {
            query.append(" and t.cart.store.name").append(like ? " like " : "=").append(":storeName");
            map.put("storeName", like ? '%' + storeName + '%' : storeName);
        }
        if (storeManagerId > 0) {
            query.append(" and t.cart.store.manager.id=:storeManagerId");
            map.put("storeManagerId", storeManagerId);
        }
        if (!Utility.isEmpty(storeManagerMobile)) {
            query.append(" and t.cart.store.manager.mobileNumber=:storeManagerMobile");
            map.put("storeManagerMobile", storeManagerMobile);
        }
        if (distributorId > 0) {
            query.append(" and t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }
        if (delivererId > 0) {
            query.append(" and t.deliverer.id=:delivererId");
            map.put("delivererId", delivererId);
        }
        if (!Utility.isEmpty(delivererMobile)) {
            query.append(" and t.deliverer.customer.mobileNumber=:delivererMobile");
            map.put("delivererMobile", delivererMobile);
        }

        return search(query.toString(), map, max, offset);
    }

    private static String getSerialQuery(final String serial, final Map<String, Object> map) {
        final String[] serialDist = serial != null ? serial.split(Config.SERIAL_SPLITTER) : new String[0];
        if (serialDist.length != 2) {
            return null;
        }
        final String query = "t.cart.serial=:cartSerial and t.distributor.serial=:distributorSerial";
        map.put("cartSerial", serialDist[0]);
        map.put("distributorSerial", serialDist[1]);
        return query;
    }
}