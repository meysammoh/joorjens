package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.TransactionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionRepositoryImpl
        extends RepositoryImpAbstract<Long, Transaction>
        implements TransactionRepository {

    @Override
    public FetchResult<Transaction> search(final long id, final Boolean credit, final Boolean sheba, final Boolean success
            , final int timeFrom, final int timeTo, final int invoiceFrom, final int invoiceTo
            , final long customerId, final String customerMobile, final long cartId, final String cartSerial, final int max, final int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Transaction t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (credit != null) {
            query.append(" and t.credit=:credit");
            map.put("credit", credit);
        }
        if (sheba != null) {
            query.append(" and t.sheba=:sheba");
            map.put("sheba", sheba);
        }
        if (success != null) {
            query.append(" and t.success=:success");
            map.put("success", success);
        }

        if (timeFrom > 0) {
            query.append(" and t.createdTime>=:timeFrom");
            map.put("timeFrom", timeFrom);
        }
        if (timeTo > 0 && timeTo >= timeFrom) {
            query.append(" and t.createdTime<=:timeTo");
            map.put("timeTo", timeTo);
        }
        if (invoiceFrom > 0) {
            query.append(" and t.invoiceTime>=:invoiceFrom");
            map.put("invoiceFrom", invoiceFrom);
        }
        if (invoiceTo > 0 && invoiceTo >= invoiceFrom) {
            query.append(" and t.invoiceTime<=:invoiceTo");
            map.put("invoiceTo", invoiceTo);
        }

        if (customerId > 0) {
            query.append(" and t.customer.id=:customerId");
            map.put("customerId", customerId);
        } else if(!Utility.isEmpty(customerMobile)) {
            query.append(" and t.customer.mobileNumber=:customerMobile");
            map.put("customerMobile", customerMobile);
        }
        if (cartId > 0) {
            query.append(" and t.cart.id=:cartId");
            map.put("cartId", cartId);
        } else if(!Utility.isEmpty(cartSerial)) {
            query.append(" and t.cart.serial=:cartSerial");
            map.put("cartSerial", cartSerial);
        }

        return search(query.toString(), map, max, offset);
    }
}