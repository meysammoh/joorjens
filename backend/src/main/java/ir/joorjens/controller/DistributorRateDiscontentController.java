package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorRateDiscontentRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class DistributorRateDiscontentController {

    private static final DistributorRateDiscontentRepository REPO_DIS_RD = (DistributorRateDiscontentRepository) RepositoryManager.getByEntity(DistributorRateDiscontent.class);
    private static final FormFactory<DistributorRateDiscontent> FORM = new FormFactory<>(DistributorRateDiscontent.class);

    public DistributorRateDiscontentController() {
        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_RATE_DISCONTENT_UPSERT.getUrl(), (req, res) -> upsert(req), json());
        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_RATE_DISCONTENT_PRE.getUrl() + ":cartId/:distributorId/", (req, res) -> find(req), json());
        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_RATE_DISCONTENT_SEARCH.getUrl(), (req, res) -> search(req, false), json());
        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_RATE_DISCONTENT_VIEW.getUrl(), (req, res) -> search(req, true), json());
    }

    private static DistributorRateDiscontent upsert(final Request req) throws JoorJensException {
        final long fromCustomerId = AAA.getCustomerId(req);
        final DistributorRateDiscontent rateDiscontent = FORM.get(req.body());

        final Distributor distributor = DistributorController.getDistributor(rateDiscontent.getDistributorId());
        final Cart cart = CartController.getCart(rateDiscontent.getCartId());
        if (fromCustomerId != cart.getStoreManagerId() || !cart.containsDistributor(distributor.getId())) {
            throw new JoorJensException(ExceptionCode.CART_RATE_NOT_YOURS);
        }
        final DistributorRateDiscontent rateDiscontentPre = getDistributorRateDiscontent(cart.getId(), distributor.getId());
        final boolean update = rateDiscontentPre != null;
        float rate = rateDiscontent.getRate();
        if (update) {
            rateDiscontent.setEdit(rateDiscontentPre);
            rate -= rateDiscontentPre.getRate();
        }
        AbstractController.upsert(req, DistributorRateDiscontent.class, rateDiscontent, update);
        if (rate <= 2) {
            //todo insert into cartable
        }
        DistributorController.rate(distributor.getId(), rate, (update) ? 0 : 1);
        return rateDiscontent;
    }

    private static DistributorRateDiscontent find(final Request req) throws JoorJensException {
        final long cartId = Long.parseLong(req.params(":cartId"));
        final long distributorId = Long.parseLong(req.params(":distributorId"));
        final DistributorRateDiscontent rateDiscontentPre = getDistributorRateDiscontent(cartId, distributorId);
        if (rateDiscontentPre == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorRateDiscontent.getEN());
        }
        return rateDiscontentPre;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getDistributorRateDiscontent(id);
        }
        final long cartId = (queryMap.containsKey("cartid")) ? Long.parseLong(queryMap.get("cartid")) : 0;
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        long customerId = (queryMap.containsKey("customerid")) ? Long.parseLong(queryMap.get("customerid")) : 0;
        if (TypeEnumeration.UR_STORE_ADMIN == customer.getRoleType()) {
            customerId = customer.getId();
        }
        distributorId = CartController.getDistributorId(req, distributorId, customerId > 0);
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DIS_RD.search(id, cartId, distributorId, customerId, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static DistributorRateDiscontent getDistributorRateDiscontent(final long id) throws JoorJensException {
        final Optional<DistributorRateDiscontent> distributorRDOptional = REPO_DIS_RD.getByKey(id);
        if (!distributorRDOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorRateDiscontent.getEN());
        }
        return distributorRDOptional.get();
    }

    static DistributorRateDiscontent getDistributorRateDiscontent(final long cartId, final long distributorId) throws JoorJensException {
        return REPO_DIS_RD.find(cartId, distributorId);
    }
    //-------------------------------------------------------------------------------------------------
}