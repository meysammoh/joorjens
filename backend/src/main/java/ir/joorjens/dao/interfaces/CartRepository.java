package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.businessEntity.CartOrderInfo;
import ir.joorjens.model.entity.Cart;
import ir.joorjens.model.util.CartPrice;

import java.util.Set;

public interface CartRepository extends Repository<Long, Cart> {

        Cart find(String serial) throws JoorJensException;

        FetchResult<Cart> search(long id, String serial
                , int timeFrom, int timeTo
                , int timeFinishedFrom, int timeFinishedTo
                , int countFrom, int countTo
                , int packCountFrom, int packCountTo
                , int packPriceConsumerFrom, int packPriceConsumerTo
                , int packPriceFrom, int packPriceTo
                , int packPriceDiscountFrom, int packPriceDiscountTo
                , Boolean finished
                , long storeId, String storeName
                , long storeManagerId, String storeManagerMobile
                , boolean like, int max, int offset) throws JoorJensException;

        FetchResult<CartPrice> dashboardSales(final int timeStampId
                , int timeFrom, int timeTo
                , int timeFinishedFrom, int timeFinishedTo, Boolean finished
                , final boolean groupByAreas, final boolean groupByDistributor, final Set<Long> areaIds
                , long productId, String productBarcode
                , long disProductId, long disPackageId
                , long distributorId
                , long storeId, long managerId, String managerMobile
                , int max, int offset) throws JoorJensException;

        FetchResult<CartOrderInfo> dashboardOrders(int timeStampId
                , int timeFrom, int timeTo
                , int timeFinishedFrom, int timeFinishedTo, Boolean finished
                , boolean groupByAreas, boolean groupByDistributor, Set<Long> areaIds
                , long productId, String productBarcode
                , long disProductId, long disPackageId
                , long distributorId
                , long storeId, long managerId, String managerMobile
                , int max, int offset) throws JoorJensException;
}