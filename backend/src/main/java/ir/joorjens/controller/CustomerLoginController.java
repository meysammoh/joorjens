package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.CustomerLoginRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.CustomerLogin;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class CustomerLoginController {

    private static final CustomerLoginRepository REPO_LOGIN = (CustomerLoginRepository) RepositoryManager.getByEntity(CustomerLogin.class);
    private static final FormFactory<CustomerLogin> FORM = new FormFactory<>(CustomerLogin.class);

    public CustomerLoginController() {

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, CustomerLogin.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN_VIEW.getUrl(), (req, res) -> search(req, true), json());

    }

    private static CustomerLogin upsert(final Request req, final boolean update) throws JoorJensException {
        final CustomerLogin customerLogin = FORM.get(req.body());
        if (update && customerLogin.getId() > 0) { //update
            CustomerLogin customerLoginPre = getCustomerLogin(customerLogin.getId());
            customerLogin.setEdit(customerLoginPre);
        }
        AbstractController.upsert(req, CustomerLogin.class, customerLogin, update);
        return customerLogin;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getCustomerLogin(id);
        }
        final long customerId = (queryMap.containsKey("customerid")) ? Long.parseLong(queryMap.get("customerid")) : 0;
        final int loginFrom = (queryMap.containsKey("loginfrom")) ? Integer.parseInt(queryMap.get("loginfrom")) : 0;
        final int loginTo = (queryMap.containsKey("loginto")) ? Integer.parseInt(queryMap.get("loginto")) : 0;
        final int logoutFrom = (queryMap.containsKey("logoutfrom")) ? Integer.parseInt(queryMap.get("logoutfrom")) : 0;
        final int logoutTo = (queryMap.containsKey("logouto")) ? Integer.parseInt(queryMap.get("logouto")) : 0;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        final FetchResult<CustomerLogin> result = REPO_LOGIN.search(id, customerId, loginFrom, loginTo, logoutFrom, logoutTo, max, offset);
        //calculate loginPeriod -------------------------
        int loginPeriod = 0;
        if(result.getResultSize() > 0) {
            for (CustomerLogin cl : result.getResult()) {
                loginPeriod += cl.getLoginPeriod();
            }
        }
        result.setInfo(Utility.getTime(loginPeriod));
        //calculate loginPeriod -------------------------
        return result;
    }

    //-------------------------------------------------------------------------------------------------
    static CustomerLogin getCustomerLogin(long id) throws JoorJensException {
        final Optional<CustomerLogin> customerLoginOptional = REPO_LOGIN.getByKey(id);
        if (!customerLoginOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, CustomerLogin.getEN());
        }
        return customerLoginOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}