package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.CustomerSessionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Area;
import ir.joorjens.model.entity.CustomerSession;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.delete;
import static spark.Spark.get;

public class CustomerSessionController {

    private static final CustomerSessionRepository REPO_SESSION = (CustomerSessionRepository) RepositoryManager.getByEntity(CustomerSession.class);

    public CustomerSessionController() {

        delete(Config.API_PREFIX + UrlRolesType.CUSTOMER_SESSION_DELETE.getUrl() + ":id/", (req, res) -> closeSession(req), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_SESSION_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_SESSION_VIEW.getUrl(), (req, res) -> search(req, true), json());

    }

    private static ResponseMessage closeSession(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        final CustomerSession customerSession = getCustomerSession(id);
        AAA.closeCustomerSession(customerSession);
        customerSession.setExpiredTime(Utility.getCurrentTime());
        AbstractController.upsert(req, CustomerSession.class, customerSession, true);
        return new ResponseMessage(String.format(ResponseCode.DONE_DELETE.getMessage(), CustomerSession.getEN()));

    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getCustomerSession(id);
        }
        long customerId = (queryMap.containsKey("customerid")) ? Long.parseLong(queryMap.get("customerid")) : 0;
        long roleId = (queryMap.containsKey("roleid")) ? Long.parseLong(queryMap.get("roleid")) : 0;
        final String ip = queryMap.get("ip");
        final String browserManufacturer = queryMap.get("browserManufacturer");
        final String browserGroup = queryMap.get("browserGroup");
        final String browserVersion = queryMap.get("browserVersion");
        final String osManufacturer = queryMap.get("osManufacturer");
        final String osGroup = queryMap.get("osGroup");
        final String device = queryMap.get("device");
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;

        return REPO_SESSION.search(id, customerId, roleId, ip
                , browserManufacturer, browserGroup, browserVersion
                , osManufacturer, osGroup, device, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static CustomerSession getCustomerSession(final long id) throws JoorJensException {
        final Optional<CustomerSession> customerSessionOptional = REPO_SESSION.getByKey(id);
        if (!customerSessionOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Area.getEN());
        }
        return customerSessionOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}