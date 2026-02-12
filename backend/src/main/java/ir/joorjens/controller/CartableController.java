package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.CartableRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Cartable;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class CartableController {

    private static final CartableRepository REPO_CARTABLE = (CartableRepository) RepositoryManager.getByEntity(Cartable.class);

    public CartableController() {
        post(Config.API_PREFIX + UrlRolesType.CARTABLE_APPROVE.getUrl() + ":cartableId/", (req, res) -> approve(req), json());

        post(Config.API_PREFIX + UrlRolesType.CARTABLE_REJECT.getUrl() + ":cartableId/", (req, res) -> reject(req), json());

        post(Config.API_PREFIX + UrlRolesType.CARTABLE_RENEW.getUrl() + ":cartableId/", (req, res) -> renew(req), json());

        get(Config.API_PREFIX + UrlRolesType.CARTABLE_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.CARTABLE_VIEW.getUrl(), (req, res) -> search(req, true), json());
    }

    private static ResponseMessage approve(final Request req) throws JoorJensException {
        final long cartableId = Long.parseLong(req.params(":cartableId"));
        final Cartable cartable = getCartable(cartableId);
        isEditable(cartable);
        boolean update = true;
        switch (cartable.getTaskType()) {
            case TT_INSERT_DISTRIBUTOR_MAIN:
            case TT_INSERT_DISTRIBUTOR_MAIN_BRANCH:
                update = false;
            case TT_UPDATE_DISTRIBUTOR_MAIN:
            case TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH:
                DistributorController.upsert(req, cartable.getBody(), update);
                break;

            case TT_INSERT_STORE:
                update = false;
            case TT_UPDATE_STORE:
                StoreController.upsert(req, cartable.getBody(), update);
                break;

            case TT_INSERT_DISTRIBUTOR_EMPLOYEE:
                update = false;
            case TT_UPDATE_DISTRIBUTOR_EMPLOYEE:
                break;

            case TT_INSERT_PRODUCT:
                update = false;
            case TT_UPDATE_PRODUCT:
                ProductController.upsert(req, cartable.getBody(), update);
                break;
        }

        cartable.setDonnerCustomer(AAA.getCustomer(req));
        cartable.setDoneTime(Utility.getCurrentTime());
        cartable.setStatus(TypeEnumeration.TS_APPROVED.getId());
        AbstractController.upsert(req, Cartable.class, cartable, true);
        return new ResponseMessage(ResponseCode.CARTABLE_APPROVED);
    }

    private static ResponseMessage reject(final Request req) throws JoorJensException {
        final long cartableId = Long.parseLong(req.params(":cartableId"));
        final Cartable cartable = getCartable(cartableId);
        isEditable(cartable);

        final boolean aboutCustomer;
        switch (cartable.getTaskType()) {
            case TT_INSERT_DISTRIBUTOR_MAIN:
            case TT_INSERT_DISTRIBUTOR_MAIN_BRANCH:
            case TT_UPDATE_DISTRIBUTOR_MAIN:
            case TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH:
                DistributorController.deleteTempImages(cartable.getBody());
                aboutCustomer = true;
                break;

            case TT_INSERT_STORE:
            case TT_UPDATE_STORE:
                StoreController.deleteTempImages(cartable.getBody());
                aboutCustomer = true;
                break;

            case TT_INSERT_DISTRIBUTOR_EMPLOYEE:
            case TT_UPDATE_DISTRIBUTOR_EMPLOYEE:
                aboutCustomer = true;
                break;

            case TT_INSERT_PRODUCT:
            case TT_UPDATE_PRODUCT:
                ProductController.deleteTempImages(cartable.getBody());
                aboutCustomer = false;
                break;

            default:
                aboutCustomer = false;
                break;
        }

        cartable.setNote(req.body());
        cartable.setDonnerCustomer(AAA.getCustomer(req));
        cartable.setDoneTime(Utility.getCurrentTime());
        cartable.setStatus(TypeEnumeration.TS_REJECTED.getId());
        AbstractController.upsert(req, Cartable.class, cartable, true);

        if (aboutCustomer) {
            final Customer fromCustomer = cartable.getFromCustomer();
            fromCustomer.setInCartable(false);
            CustomerController.updateCustomerInCartable(fromCustomer.getId(), false);
        }

        return new ResponseMessage(ResponseCode.CARTABLE_REJECTED);
    }

    private static ResponseMessage renew(final Request req) throws JoorJensException {
        final long cartableId = Long.parseLong(req.params(":cartableId"));
        final Cartable cartable = getCartable(cartableId);
        isRenewable(cartable);

        final boolean aboutCustomer;
        switch (cartable.getTaskType()) {
            case TT_INSERT_DISTRIBUTOR_MAIN:
            case TT_INSERT_DISTRIBUTOR_MAIN_BRANCH:
            case TT_UPDATE_DISTRIBUTOR_MAIN:
            case TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH:
            case TT_INSERT_STORE:
            case TT_UPDATE_STORE:
            case TT_INSERT_DISTRIBUTOR_EMPLOYEE:
            case TT_UPDATE_DISTRIBUTOR_EMPLOYEE:
                aboutCustomer = true;
                break;

            case TT_INSERT_PRODUCT:
            case TT_UPDATE_PRODUCT:
            default:
                aboutCustomer = false;
                break;
        }

        //cartable.setNote();
        cartable.setDonnerCustomer(null);
        cartable.setDoneTime(0);
        cartable.setStatus(TypeEnumeration.TS_NEW.getId());
        AbstractController.upsert(req, Cartable.class, cartable, true);

        if (aboutCustomer) {
            final Customer fromCustomer = cartable.getFromCustomer();
            fromCustomer.setInCartable(true);
            CustomerController.updateCustomerInCartable(fromCustomer.getId(), true);
        }

        return new ResponseMessage(ResponseCode.CARTABLE_RENEWED);
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getCartable(id);
        }

        final int status = (queryMap.containsKey("status")) ? Integer.parseInt(queryMap.get("status")) : 0;
        final int type = (queryMap.containsKey("type")) ? Integer.parseInt(queryMap.get("type")) : 0;
        final String key = queryMap.get("key");
        final int priority = (queryMap.containsKey("priority")) ? Integer.parseInt(queryMap.get("priority")) : 0;
        final Boolean toJoorJens = (queryMap.containsKey("tojoorjens")) ? Boolean.parseBoolean(queryMap.get("tojoorjens")) : null;
        final int createdTimeFrom = (queryMap.containsKey("createdtimefrom")) ? Integer.parseInt(queryMap.get("createdtimefrom")) : 0;
        final int createdTimeTo = (queryMap.containsKey("createdtimeto")) ? Integer.parseInt(queryMap.get("createdtimeto")) : 0;
        final int doneTimeFrom = (queryMap.containsKey("donetimefrom")) ? Integer.parseInt(queryMap.get("donetimefrom")) : 0;
        final int doneTimeTo = (queryMap.containsKey("donetimeto")) ? Integer.parseInt(queryMap.get("donetimeto")) : 0;
        final long fromCustomerId = (queryMap.containsKey("fromcustomerid")) ? Long.parseLong(queryMap.get("fromcustomerid")) : 0;
        final long toCustomerId = (queryMap.containsKey("tocustomerid")) ? Long.parseLong(queryMap.get("tocustomerid")) : 0;
        final long donnerCustomerId = (queryMap.containsKey("donnercustomerid")) ? Long.parseLong(queryMap.get("donnercustomerid")) : 0;
        final long toDistributorId = (queryMap.containsKey("todistributorid")) ? Long.parseLong(queryMap.get("todistributorid")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_CARTABLE.search(id, status, type, key, priority, toJoorJens
                , createdTimeFrom, createdTimeTo, doneTimeFrom, doneTimeTo
                , fromCustomerId, toCustomerId, donnerCustomerId, toDistributorId, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Cartable getCartable(final long id) throws JoorJensException {
        final Optional<Cartable> cartableOptional = REPO_CARTABLE.getByKey(id);
        if (!cartableOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Cartable.getEN());
        }
        return cartableOptional.get();
    }

    static boolean isEditable(final Cartable cartable) throws JoorJensException {
        switch (cartable.getStatusType()) {
            case TS_APPROVED:
            case TS_REJECTED:
                throw new JoorJensException(ExceptionCode.TASK_WAS_DONE);
        }
        return true;
    }

    static boolean isRenewable(final Cartable cartable) throws JoorJensException {
        switch (cartable.getStatusType()) {
            case TS_APPROVED:
                throw new JoorJensException(ExceptionCode.TASK_WAS_DONE);
            case TS_NEW:
                throw new JoorJensException(ExceptionCode.TASK_IS_NEW);
        }
        return true;
    }

    static boolean isExist(final Cartable cartable) throws JoorJensException {
        return REPO_CARTABLE.isExist(cartable.getStatus(), cartable.getType(), cartable.getKey());
    }
    //-------------------------------------------------------------------------------------------------
}