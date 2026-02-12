package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.VehicleRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class VehicleController {

    private static final VehicleRepository REPO_VEHICLE = (VehicleRepository) RepositoryManager.getByEntity(Vehicle.class);
    private static final FormFactory<Vehicle> FORM = new FormFactory<>(Vehicle.class);

    public VehicleController() {
        post(Config.API_PREFIX + UrlRolesType.VEHICLE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.VEHICLE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.VEHICLE_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Vehicle.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.VEHICLE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static Vehicle upsert(final Request req, final boolean update) throws JoorJensException {
        final Vehicle vehicle = FORM.get(req.body());
        if (update && vehicle.getId() > 0) { //update
            final Vehicle vehiclePre = getVehicle(vehicle.getId());
            vehicle.setEdit(vehiclePre);
        }

        final VehicleBrandType vehicleBrandType = VehicleBrandTypeController.getVehicleBrandType(vehicle.getVehicleBrandTypeId());
        vehicle.setVehicleBrandType(vehicleBrandType);
        final Distributor distributor = DistributorController.getDistributor(vehicle.getDistributorId());
        vehicle.setDistributor(distributor);
        final ColorType colorType = ColorTypeController.getColorType(vehicle.getColorTypeId());
        vehicle.setColorType(colorType);

        AbstractController.upsert(req, Vehicle.class, vehicle, update);
        return vehicle;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getVehicle(id);
        }
        final int licensePlateFirst = (queryMap.containsKey("licenseplatefirst")) ? Integer.parseInt(queryMap.get("licenseplatefirst")) : 0;
        final String licensePlateLetter = queryMap.get("licenseplateletter");
        final int licensePlateSecond = (queryMap.containsKey("licenseplatesecond")) ? Integer.parseInt(queryMap.get("licenseplatesecond")) : 0;
        final int licensePlateCode = (queryMap.containsKey("licenseplatecode")) ? Integer.parseInt(queryMap.get("licenseplatecode")) : 0;
        final int color = (queryMap.containsKey("color")) ? Integer.parseInt(queryMap.get("color")) : 0;
        final int manufactureYear = (queryMap.containsKey("manufactureyear")) ? Integer.parseInt(queryMap.get("manufactureyear")) : 0;
        final long vehicleBrandTypeId = (queryMap.containsKey("vehiclebrandtypeid")) ? Long.parseLong(queryMap.get("vehiclebrandtypeid")) : 0;
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        distributorId = CartController.getDistributorId(req, distributorId, false);
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_VEHICLE.getAllPairs(id, licensePlateFirst, licensePlateLetter, licensePlateSecond, licensePlateCode
                    , color, manufactureYear, vehicleBrandTypeId, distributorId, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_VEHICLE.search(id, licensePlateFirst, licensePlateLetter, licensePlateSecond, licensePlateCode
                , color, manufactureYear, vehicleBrandTypeId, distributorId, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Vehicle getVehicle(final long id) throws JoorJensException {
        final Optional<Vehicle> vehicleOptional = REPO_VEHICLE.getByKey(id);
        if (!vehicleOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Vehicle.getEN());
        }
        return vehicleOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}