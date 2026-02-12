package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorDelivererRepository;
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

public class DistributorDelivererController {

    private static final DistributorDelivererRepository REPO_DIS_DEL = (DistributorDelivererRepository) RepositoryManager.getByEntity(DistributorDeliverer.class);
    private static final FormFactory<DistributorDeliverer> FORM = new FormFactory<>(DistributorDeliverer.class);

    public DistributorDelivererController() {
        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELIVERER_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELIVERER_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELIVERER_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELIVERER_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELIVERER_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static DistributorDeliverer upsert(final Request req, final boolean update) throws JoorJensException {
        final DistributorDeliverer deliverer = FORM.get(req.body());
        if (update && deliverer.getId() > 0) { //update
            final DistributorDeliverer delivererPre = getDistributorDeliverer(deliverer.getId());
            deliverer.setEdit(delivererPre);
        }

        final Distributor distributor = DistributorController.getDistributor(deliverer.getDistributorId());
        deliverer.setDistributor(distributor);
        final Vehicle vehicle = VehicleController.getVehicle(deliverer.getVehicleId());
        deliverer.setVehicle(vehicle);

        final Customer customer = deliverer.getCustomer();
        final DistributorEmployee distributorEmployee = new DistributorEmployee(customer);
        distributorEmployee.setDistributor(distributor);
        distributorEmployee.setRoleId(TypeEnumeration.UR_DISTRIBUTION_DELIVERER.getId());
        DistributorEmployeeController.upsert(req, distributorEmployee, update);
        deliverer.setCustomer(customer);

        AbstractController.upsert(req, DistributorDeliverer.class, deliverer, true);

        return deliverer;
    }

    private static Object search(Request req, boolean view, boolean pair) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            final DistributorDeliverer deliverer = getDistributorDeliverer(id);
            switch (customer.getRoleType()) {
                case UR_DISTRIBUTION_DELIVERER:
                    if (customer.getId() != deliverer.getId()) {
                        throw new JoorJensException(ExceptionCode.OP_FORBIDDEN_FILTER);
                    }
                    break;
            }
            return deliverer;
        }
        final String mobileNumber = queryMap.get("mobilenumber");
        final String nationalIdentifier = queryMap.get("nationalidentifier");
        final String firstName = queryMap.get("firstname");
        final String lastName = queryMap.get("lastname");
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        distributorId = CartController.getDistributorId(req, distributorId, false);
        final long vehicleId = (queryMap.containsKey("vehicleid")) ? Long.parseLong(queryMap.get("vehicleid")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_DIS_DEL.getAllPairs(id, distributorId, vehicleId
                    , mobileNumber, nationalIdentifier, firstName, lastName, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DIS_DEL.search(id, distributorId, vehicleId,
                mobileNumber, nationalIdentifier, firstName, lastName, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static DistributorDeliverer getDistributorDeliverer(final long id) throws JoorJensException {
        final Optional<DistributorDeliverer> distributorDOptional = REPO_DIS_DEL.getByKey(id);
        if (!distributorDOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorDeliverer.getEN());
        }
        return distributorDOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}