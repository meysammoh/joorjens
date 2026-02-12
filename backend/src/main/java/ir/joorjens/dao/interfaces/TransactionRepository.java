package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Transaction;

public interface TransactionRepository extends Repository<Long, Transaction> {

    FetchResult<Transaction> search(long id, Boolean credit, Boolean sheba, Boolean success
            , int timeFrom, int timeTo, int invoiceFrom, int invoiceTo
            , long customerId, String customerMobile, long cartId, String cartSerial, int max, int offset) throws JoorJensException;

}