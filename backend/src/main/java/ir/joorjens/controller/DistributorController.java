package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.DistributorRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class DistributorController {

    //-------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorController.class);
    private static final DistributorRepository REPO_DIS = (DistributorRepository) RepositoryManager.getByEntity(Distributor.class);
    private static final FormFactory<Distributor> FORM = new FormFactory<>(Distributor.class);
    private static final FormFactory<Long[]> FORM_BRANCH = new FormFactory<>(Long[].class);
    //-------------------------------------------------------------------------------------------------

    public DistributorController() {
        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_INSERT.getUrl(), (req, res) -> upsert(req, null, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_UPDATE.getUrl(), (req, res) -> upsert(req, null, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELETE.getUrl() + ":id/", (req, res) -> remove(req), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_INSERT_BRANCH.getUrl() + ":id/", (req, res) -> branch(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DELETE_BRANCH.getUrl() + ":id/", (req, res) -> branch(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PAIR.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EXIST.getUrl(), (req, res) -> search(req, false, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROFILE.getUrl(), (req, res) -> profile(req), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Distributor.class, id, block);
        }, json());

    }

    static Object upsert(final Request req, final String preBody, final boolean update) throws JoorJensException {
        final Distributor distributor = FORM.get(req.body());
        final boolean fromCartable = (preBody != null);
        final Distributor distributorPre;
        if (fromCartable) {
            distributorPre = FORM.get(preBody);//body from cartable
        } else if (update && distributor.getId() > 0) { //update
            distributorPre = getDistributor(distributor.getId());
            distributor.setEdit(distributorPre);
        } else {
            distributorPre = null;
        }

        if (distributor.getActivityTypes() != null) {
            for (ProductCategoryType at : distributor.getActivityTypes()) {
                ProductCategoryTypeController.getProductCategoryType(at.getId());
            }
        }

        boolean canBeDistributorAdmin = false;
        final Customer fromCustomer;
        //setting manager of distributor
        if (distributor.getManagerId() == 0) {
            fromCustomer = AAA.getCustomer(req);
            if (fromCustomer.isInCartable()) {
                throw new JoorJensException(ExceptionCode.CUSTOMER_IN_CARTABLE);
            }
            canBeDistributorAdmin = canBeDistributorAdmin(fromCustomer);
            if (!canBeDistributorAdmin) {
                throw new JoorJensException(ExceptionCode.INVALID_MANAGER, Distributor.getEN());
            }
        } else {
            fromCustomer = CustomerController.getCustomer(distributor.getManagerId());
        }
        distributor.setManagerId(fromCustomer.getId());

        final Area areaCity;
        if (distributor.getAreaCityId() > 0) {
            areaCity = AreaController.getArea(distributor.getAreaCityId());
        } else {
            areaCity = null;
        }

        final Distributor parent;
        if (distributor.getParentId() > 0) {
            parent = getDistributor(distributor.getParentId());
        } else if (!Utility.isEmpty(distributor.getParentSerial())) {
            parent = getDistributor(distributor.getParentSerial());
        } else {
            parent = null;
        }
        distributor.setParent(parent);

        distributor.isValid();
        saveImages(distributor, distributorPre, fromCustomer.getUuid(), fromCartable);

        if (canBeDistributorAdmin) {
            fromCustomer.setInCartable(true);
            CustomerController.updateCustomerInCartable(fromCustomer.getId(), true);

            final Cartable cartable = new Cartable(Distributor.getTaskType(distributor.getType(), update).getId()
                    , TypeEnumeration.PRT_MEDIUM.getId(), AbstractController.getQueryString(req, null)
                    , Utility.toJson(distributor), Distributor.getEN(), fromCustomer);
            AbstractController.upsert(req, Cartable.class, cartable, update);
            return new ResponseMessage(String.format(ResponseCode.CARTABLE_ADDED_DISTRIBUTOR.getMessage(), fromCustomer.getName()));
        } else {
            fromCustomer.setInCartable(false);
            fromCustomer.setNationalIdentifier(distributor.getManagerNI());
            fromCustomer.setImageProfile(distributor.getManagerImageP());
            fromCustomer.setImageNationalIdentifier(distributor.getManagerImageNI());
            fromCustomer.setRoleId(TypeEnumeration.UR_DISTRIBUTION_ADMIN.getId());
            if (areaCity != null) {
                fromCustomer.setAreaCity(areaCity);
            }
            AbstractController.upsert(req, Customer.class, fromCustomer, true);

            if (update) {
                AbstractController.upsert(req, Distributor.class, distributor, true);
            } else {
                insert(req, distributor);
                if (distributor.getParentId() > 0) {
                    REPO_DIS.update(distributor.getParentId(), 1);
                }
            }

            final DistributorEmployee employee = new DistributorEmployee(fromCustomer);
            employee.setDistributor(distributor);
            AbstractController.upsert(req, DistributorEmployee.class, employee, true);

            return distributor;
        }
    }

    private static ResponseMessage remove(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        final Distributor distributor = getDistributor(id);
        final ResponseMessage responseMessage = AbstractController.delete(req, Distributor.class, id);
        if (responseMessage.isOk() && distributor.getParentId() > 0) {
            REPO_DIS.update(distributor.getParentId(), -1);
        }
        return responseMessage;
    }

    private static ResponseMessage branch(final Request req, final boolean insertOrDelete) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        final Distributor distributor = getDistributor(id);
        final Long[] branches = FORM_BRANCH.get(req.body());

        final Map<Long, Area> supportAreas = new HashMap<>();
        for (Area area : distributor.getSupportAreas()) {
            supportAreas.put(area.getId(), area);
        }
        int changeCount = 0;
        if (branches != null && branches.length > 0) {
            for (Long areaId : branches) {
                if (insertOrDelete) {
                    if (!supportAreas.containsKey(areaId)) {
                        supportAreas.put(areaId, AreaController.getArea(areaId));
                        ++changeCount;
                    }
                } else if (supportAreas.containsKey(areaId)) {
                    supportAreas.remove(areaId);
                    ++changeCount;
                }
            }
        } else if (!insertOrDelete && supportAreas.size() > 0) { //remove all if body is empty
            changeCount = supportAreas.size();
            supportAreas.clear();
        }

        if (changeCount > 0) {
            distributor.setSupportAreas(new HashSet<>(supportAreas.values()));
            AbstractController.upsert(req, Distributor.class, distributor, true);
        }
        final TypeEnumeration action;
        if (insertOrDelete) {
            action = TypeEnumeration.ACTION_INSERT;
        } else {
            action = TypeEnumeration.ACTION_DELETE;
        }
        return new ResponseMessage(ResponseCode.getMessage(changeCount, "منطقه تحت پوشش", action));
    }

    private static Object search(final Request req, final boolean view, final boolean pair, final boolean exist) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            final Distributor distributor = getDistributor(id);
            switch (customer.getRoleType()) {
                case UR_DISTRIBUTION_ADMIN:
                    if (customer.getId() != distributor.getManagerId()) {
                        throw new JoorJensException(ExceptionCode.OP_FORBIDDEN_FILTER);
                    }
                    break;
            }
            return distributor;
        }
        final String serial = queryMap.get("serial");
        if (exist) {
            return getDistributor(serial);
        }
        switch (customer.getRoleType()) {
            case UR_DISTRIBUTION_ADMIN:
                queryMap.put("managerid", String.valueOf(customer.getId()));
                break;
        }

        final int type = (queryMap.containsKey("type")) ? Integer.parseInt(queryMap.get("type")) : TypeEnumeration.DT_MAIN.getId();
        final String name = queryMap.get("name");
        final String registrationNumber = queryMap.get("registrationnumber");
        final String telephone = queryMap.get("telephone");
        final String fax = queryMap.get("fax");
        final String site = queryMap.get("site");
        final int createdTimeFrom = (queryMap.containsKey("createdtimefrom")) ? Integer.parseInt(queryMap.get("createdtimefrom")) : 0;
        final int createdTimeTo = (queryMap.containsKey("createdtimeto")) ? Integer.parseInt(queryMap.get("createdtimeto")) : 0;
        final int dailySaleFrom = (queryMap.containsKey("dailysalefrom")) ? Integer.parseInt(queryMap.get("dailysalefrom")) : 0;
        final int dailySaleTo = (queryMap.containsKey("dailysaleto")) ? Integer.parseInt(queryMap.get("dailysaleto")) : 0;
        final long areaCityId = (queryMap.containsKey("areacityid")) ? Long.parseLong(queryMap.get("areacityid")) : 0;
        final long areaProId = (queryMap.containsKey("areaproid")) ? Long.parseLong(queryMap.get("areaproid")) : 0;
        final long managerId = (queryMap.containsKey("managerid")) ? Long.parseLong(queryMap.get("managerid")) : 0;
        final String managerNationalIdentifier = queryMap.get("managerni");
        final long parentId = (queryMap.containsKey("parentid")) ? Long.parseLong(queryMap.get("parentid")) : 0;
        final String parentSerial = queryMap.get("parentserial");
        final Set<Long> activityTypeIds = Utility.getSetLong(queryMap.get("activitytypeids"), ",") //
                , supportAreaIds = Utility.getSetLong(queryMap.get("supportareaids"), ",") //
                , productBrandTypeIds = Utility.getSetLong(queryMap.get("productbrandtypeid"), ",")//
                , productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeid"), ",");
        final boolean typesAreParent = (queryMap.containsKey("typesareparent")) && Boolean.parseBoolean(queryMap.get("typesareparent"));
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        if (pair) {
            return REPO_DIS.getAllPairs(id, type, name, serial, registrationNumber, telephone, fax, site
                    , createdTimeFrom, createdTimeTo, dailySaleFrom, dailySaleTo
                    , areaCityId, areaProId, managerId, parentId, parentSerial, managerNationalIdentifier
                    , activityTypeIds, supportAreaIds
                    , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                    , orderTypeIds, ascDescs, like, block);
        }
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DIS.search(id, type, name, serial, registrationNumber, telephone, fax, site
                , createdTimeFrom, createdTimeTo, dailySaleFrom, dailySaleTo
                , areaCityId, areaProId, managerId, parentId, parentSerial, managerNationalIdentifier
                , activityTypeIds, supportAreaIds
                , productBrandTypeIds, productCategoryTypeIds, typesAreParent
                , orderTypeIds, ascDescs, like, block, max, offset);
    }

    private static Distributor profile(Request req) throws JoorJensException {
        return getDistributor(AAA.getCustomerId(req), null, null);
    }
    //-------------------------------------------------------------------------------------------------

    private static Distributor insert(final Request req, final Distributor distributor) throws JoorJensException {
        int counter = 0;
        do {
            try {
                distributor.setKey();
                AbstractController.upsert(req, Distributor.class, distributor, false);
            } catch (JoorJensException e) {
                if (e.getResponseMessage().getCode() == ExceptionCode.UK_DISTRIBUTOR_SERIAL.getErrorCode()) {
                    ++counter;
                } else {
                    LOGGER.error(String.format("Exception@insert. Message: %s", e.getMessage()));
                    throw new JoorJensException(ExceptionCode.EXCEPTION);
                }
            }
        } while (distributor.getId() == 0);
        if (counter > 0) {
            LOGGER.warn(String.format("@insert: inserting unique serial exception: %d", counter));
        }
        return distributor;
    }

    //-------------------------------------------------------------------------------------------------

    public static Distributor getDistributor(final long id) throws JoorJensException {
        final Optional<Distributor> distributorOptional = REPO_DIS.getByKey(id);
        if (!distributorOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Distributor.getEN());
        }
        return distributorOptional.get();
    }

    static Distributor getDistributor(final String serial) throws JoorJensException {
        if (Utility.isEmpty(serial)) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Distributor.getEN());
        }
        final Distributor distributor = REPO_DIS.findBySerial(serial);
        if (distributor == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Distributor.getEN());
        }
        return distributor;
    }

    static Distributor getDistributor(long managerId, String managerMobile, String managerNI) throws JoorJensException {
        return REPO_DIS.findByManager(managerId, managerMobile, managerNI);
    }

    static int rate(final long id, final float rate, final int count) throws JoorJensException {
        return REPO_DIS.rate(id, rate, count);
    }

    static boolean canBeDistributorAdmin(final Customer customer) throws JoorJensException {
        return customer.getRoleId() == TypeEnumeration.UR_CUSTOMER.getId()
                || customer.getRoleId() == TypeEnumeration.UR_DISTRIBUTION_ADMIN.getId();
    }

    static void saveImages(final Distributor distributor, final Distributor distributorPre, final UUID uuid, final boolean confirm) throws JoorJensException {
        if (confirm) {
            distributor.setImageStatute(ImageRW.confirmImage(Config.baseFolderDistributor, distributor.getImageStatute(), distributorPre.getImageStatute(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderDistributor + Config.TEMPORARY_PREFIX + uuid + "_statute";
            distributor.setImageStatute(ImageRW.saveImage(Config.baseFolderDistributor, distributor.getImageStatute(), imageAddress, (distributorPre != null ? distributorPre.getImageStatute() : ""), false, true, false));
        }

        if (confirm) {
            distributor.setImageBanner(ImageRW.confirmImage(Config.baseFolderDistributor, distributor.getImageBanner(), distributorPre.getImageBanner(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderDistributor + Config.TEMPORARY_PREFIX + uuid + "_banner";
            distributor.setImageBanner(ImageRW.saveImage(Config.baseFolderDistributor, distributor.getImageBanner(), imageAddress, (distributorPre != null ? distributorPre.getImageBanner() : ""), false, true, false));
        }

        if (confirm) {
            distributor.setManagerImageP(ImageRW.confirmImage(Config.baseFolderDistributor, distributor.getManagerImageP(), distributorPre.getManagerImageP(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderDistributor + Config.TEMPORARY_PREFIX + uuid + "_profile";
            distributor.setManagerImageP(ImageRW.saveImage(Config.baseFolderDistributor, distributor.getManagerImageP(), imageAddress, (distributorPre != null ? distributorPre.getManagerImageP() : ""), false, true, true));
            if (Utility.isEmpty(distributor.getManagerImageP())) {
                distributor.setManagerImageP(Config.DEFAULT_PROFILE_IMAGE_URL);
            }
        }

        if (confirm) {
            distributor.setManagerImageNI(ImageRW.confirmImage(Config.baseFolderDistributor, distributor.getManagerImageNI(), distributorPre.getManagerImageNI(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderDistributor + Config.TEMPORARY_PREFIX + uuid + "_nationalIdentifier";
            distributor.setManagerImageNI(ImageRW.saveImage(Config.baseFolderDistributor, distributor.getManagerImageNI(), imageAddress, (distributorPre != null ? distributorPre.getManagerImageNI() : ""), false, true, false));
        }
    }

    static void deleteTempImages(String body) throws JoorJensException {
        final Distributor distributor = FORM.get(body);
        ImageRW.deleteTempImage(distributor.getImageStatute());
        ImageRW.deleteTempImage(distributor.getImageBanner());
        ImageRW.deleteTempImage(distributor.getManagerImageP());
        ImageRW.deleteTempImage(distributor.getManagerImageNI());
    }
    //-------------------------------------------------------------------------------------------------
}