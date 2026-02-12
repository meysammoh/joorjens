package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.CartDistributor;

public interface CartDistributorRepository extends Repository<Long, CartDistributor> {

    int updateDeliverer(long cartDistId, long distDelivererId ) throws JoorJensException;

    CartDistributor find(String serial) throws JoorJensException;

    FetchResult<CartDistributor> search(long id, String serial
            , final int timeFrom, final int timeTo
            , final int timeFinishedFrom, final int timeFinishedTo
            , final int countFrom, final int countTo
            , final int packCountFrom, final int packCountTo
            , final int packPriceConsumerFrom, final int packPriceConsumerTo
            , final int packPriceFrom, final int packPriceTo
            , final int packPriceDiscountFrom, final int packPriceDiscountTo
            , Boolean finished
            , long storeId, String storeName
            , long storeManagerId, String storeManagerMobile, long distributorId
            , long delivererId, String delivererMobile
            , boolean like, int max, int offset) throws JoorJensException;
}