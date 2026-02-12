package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.TransactionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.entity.Transaction;
import spark.Request;

import java.util.Map;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;

public class TransactionController {

    //--------------------------------------------------------------------------------------------
    private static final TransactionRepository REPO_TRANS = (TransactionRepository) RepositoryManager.getByEntity(Transaction.class);
    //--------------------------------------------------------------------------------------------

    public TransactionController() {
        get(Config.API_PREFIX + UrlRolesType.TRANSACTION_SEARCH.getUrl(), (req, res) -> search(req), json());
    }

    //--------------------------------------------------------------------------------------------

    private static FetchResult<Transaction> search(final Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = queryMap.containsKey("id") ? Long.parseLong(queryMap.get("id")) : 0;
        final Boolean credit = queryMap.containsKey("credit") ? java.lang.Boolean.parseBoolean(queryMap.get("credit")) : null;
        final Boolean sheba = queryMap.containsKey("sheba") ? java.lang.Boolean.parseBoolean(queryMap.get("sheba")) : null;
        final Boolean success = queryMap.containsKey("success") ? java.lang.Boolean.parseBoolean(queryMap.get("success")) : null;
        final int timeFrom = queryMap.containsKey("timefrom") ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        final int timeTo = queryMap.containsKey("timeto") ? Integer.parseInt(queryMap.get("timeto")) : 0;
        final int invoiceFrom = queryMap.containsKey("invoicefrom") ? Integer.parseInt(queryMap.get("invoicefrom")) : 0;
        final int invoiceTo = queryMap.containsKey("invoiceto") ? Integer.parseInt(queryMap.get("invoiceto")) : 0;
        long customerId = queryMap.containsKey("customerid") ? Long.parseLong(queryMap.get("customerid")) : 0;
        final String customerMobile = queryMap.get("customermobile");
        customerId = getCustomerId(req, customerId);
        final Pair<Long, String> idMobile = CartController.getCustomerIdMobile(req, customerId, customerMobile);
        final long cartId = queryMap.containsKey("cartid") ? Long.parseLong(queryMap.get("cartid")) : 0;
        final String cartSerial = queryMap.get("cartserial");
        final int max = queryMap.containsKey("max") ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = queryMap.containsKey("offset") ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_TRANS.search(id, credit, sheba, success, timeFrom, timeTo
                , invoiceFrom, invoiceTo, idMobile.getFirst(), idMobile.getSecond(), cartId, cartSerial, max, offset);
    }

    //--------------------------------------------------------------------------------------------

    private static long getCustomerId(final Request request, final long customerId) throws JoorJensException {
        final Customer customer = AAA.getCustomer(request);
        switch (customer.getRoleType()) {
            case UR_CENTRAL_ADMIN:
            case UR_CENTRAL_CONTROLLER:
            case UR_CENTRAL_OPERATOR:
            case UR_CENTRAL_ACCOUNTANT:
            case UR_CENTRAL_SUPPORTER:
                return customerId;
            default:
                return customer.getId();
        }
    }

    //--------------------------------------------------------------------------------------------
}