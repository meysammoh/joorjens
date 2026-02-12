package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.VehicleBrandTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.VehicleBrandType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class VehicleBrandTypeController {

    private static final VehicleBrandTypeRepository REPO_TYPE = (VehicleBrandTypeRepository) RepositoryManager.getByEntity(VehicleBrandType.class);
    private static final FormFactory<VehicleBrandType> FORM = new FormFactory<>(VehicleBrandType.class);

    public VehicleBrandTypeController() {

        post(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, VehicleBrandType.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static VehicleBrandType upsert(final Request req, final boolean update) throws JoorJensException {
        final VehicleBrandType vehicleBrandType = FORM.get(req.body());
        if (update && vehicleBrandType.getId() > 0) { //update
            VehicleBrandType typePre = getVehicleBrandType(vehicleBrandType.getId());
            vehicleBrandType.setEdit(typePre);
        }
        AbstractController.upsert(req, VehicleBrandType.class, vehicleBrandType, update);
        return vehicleBrandType;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getVehicleBrandType(id);
        }
        final String name = queryMap.get("name");
        final int capacityFrom = (queryMap.containsKey("capacityfrom")) ? Integer.parseInt(queryMap.get("capacityfrom")) : 0;
        final int capacityTo = (queryMap.containsKey("capacityto")) ? Integer.parseInt(queryMap.get("capacityto")) : 0;
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_TYPE.getAllPairs(id, name, capacityFrom, capacityTo, like, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_TYPE.search(id, name, capacityFrom, capacityTo, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static VehicleBrandType getVehicleBrandType(long id) throws JoorJensException {
        final Optional<VehicleBrandType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, VehicleBrandType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}