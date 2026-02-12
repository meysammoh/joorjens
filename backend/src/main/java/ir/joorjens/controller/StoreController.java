package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.StoreRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

public class StoreController {

    private static final StoreRepository REPO_STORE = (StoreRepository) RepositoryManager.getByEntity(Store.class);
    private static final FormFactory<Store> FORM = new FormFactory<>(Store.class);

    public StoreController() {
        post(Config.API_PREFIX + UrlRolesType.STORE_INSERT.getUrl(), (req, res) -> upsert(req, null, false), json());

        post(Config.API_PREFIX + UrlRolesType.STORE_UPDATE.getUrl(), (req, res) -> upsert(req, null, true), json());

        delete(Config.API_PREFIX + UrlRolesType.STORE_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Store.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.STORE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.STORE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.STORE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.STORE_PROFILE.getUrl(), (req, res) -> profile(req), json());
    }

    static Object upsert(final Request req, final String preBody, final boolean update) throws JoorJensException {
        final Store store = FORM.get(req.body());
        final boolean fromCartable = (preBody != null);
        final Store storePre;
        if(fromCartable) {
            storePre = FORM.get(preBody);//body from cartable
        } else if (update && store.getId() > 0) { //update
            storePre = getStore(store.getId());
            store.setEdit(storePre);
        } else {
            storePre = null;
        }

        boolean canBeStoreAdmin = false;
        final Customer fromCustomer;
        //setting manager of store
        if (store.getManagerId() == 0) {
            fromCustomer = AAA.getCustomer(req);
            if (fromCustomer.isInCartable()) {
                throw new JoorJensException(ExceptionCode.CUSTOMER_IN_CARTABLE);
            }
            canBeStoreAdmin = canBeStoreAdmin(fromCustomer);
            if (!canBeStoreAdmin) {
                throw new JoorJensException(ExceptionCode.INVALID_MANAGER, Store.getEN());
            }
        } else {
            fromCustomer = CustomerController.getCustomer(store.getManagerId());
        }
        store.setManagerId(fromCustomer.getId());

        if (store.getActivityTypes() != null) {
            for (ProductCategoryType at : store.getActivityTypes()) {
                ProductCategoryTypeController.getProductCategoryType(at.getId());
            }
        }
        final Area areaCity;
        if (store.getAreaZoneId() > 0) {
            final Area areaZone = AreaController.getArea(store.getAreaZoneId());
            areaCity = areaZone.getParent();
        } else {
            areaCity = null;
        }
        store.isValid();
        saveImages(store, storePre, fromCustomer.getUuid(), fromCartable);

        if (canBeStoreAdmin) {
            fromCustomer.setInCartable(true);
            CustomerController.updateCustomerInCartable(fromCustomer.getId(), true);

            final int taskTypeId = (update) ? TypeEnumeration.TT_UPDATE_STORE.getId() : TypeEnumeration.TT_INSERT_STORE.getId();
            final Cartable cartable = new Cartable(taskTypeId, TypeEnumeration.PRT_MEDIUM.getId()
                    , AbstractController.getQueryString(req, null), Utility.toJson(store), Store.getEN(), fromCustomer);
            AbstractController.upsert(req, Cartable.class, cartable, update);
            return new ResponseMessage(String.format(ResponseCode.CARTABLE_ADDED_STORE.getMessage(), fromCustomer.getName()));
        } else {
            fromCustomer.setInCartable(false);
            fromCustomer.setGenderType(store.getManagerGT());
            fromCustomer.setNationalIdentifier(store.getManagerNI());
            fromCustomer.setImageProfile(store.getManagerImageP());
            fromCustomer.setImageNationalIdentifier(store.getManagerImageNI());
            fromCustomer.setImageBirthCertificate(store.getManagerImageBC());
            fromCustomer.setRoleId(TypeEnumeration.UR_STORE_ADMIN.getId());
            if(areaCity != null) {
                fromCustomer.setAreaCity(areaCity);
            }
            AbstractController.upsert(req, Customer.class, fromCustomer, true);

            AbstractController.upsert(req, Store.class, store, update);
            return store;
        }
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            final Store store = getStore(id);
            switch (customer.getRoleType()) {
                case UR_STORE_ADMIN:
                    if (customer.getId() != store.getManagerId()) {
                        throw new JoorJensException(ExceptionCode.OP_FORBIDDEN_FILTER);
                    }
                    break;
            }
            return store;
        }
        switch (customer.getRoleType()) {
            case UR_STORE_ADMIN:
                queryMap.put("managerid", String.valueOf(customer.getId()));
                break;
        }
        final String name = queryMap.get("name");
        final String telephone = queryMap.get("telephone");
        final String businessLicense = queryMap.get("businesslicense");
        final String postalCode = queryMap.get("postalcode");
        final long areaZoneId = (queryMap.containsKey("areazoneid")) ? Long.parseLong(queryMap.get("areazoneid")) : 0;
        final long areaCityId = (queryMap.containsKey("areacityid")) ? Long.parseLong(queryMap.get("areacityid")) : 0;
        final long areaProId = (queryMap.containsKey("areaproid")) ? Long.parseLong(queryMap.get("areaproid")) : 0;
        final long managerId = (queryMap.containsKey("managerid")) ? Long.parseLong(queryMap.get("managerid")) : 0;
        final String managerNationalIdentifier = queryMap.get("managerni");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_STORE.getAllPairs(id, name, telephone, businessLicense, postalCode
                    , areaZoneId, areaCityId, areaProId, managerId, managerNationalIdentifier, like, block);
        }
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_STORE.search(id, name, telephone, businessLicense, postalCode
                , areaZoneId, areaCityId, areaProId, managerId, managerNationalIdentifier
                , like, block, max, offset);
    }

    private static Store profile(Request req) throws JoorJensException {
        return getStoreByManager(AAA.getCustomerId(req));
    }
    //-------------------------------------------------------------------------------------------------
    static Store getStore(final long id) throws JoorJensException {
        final Optional<Store> storeOptional = REPO_STORE.getByKey(id);
        if (!storeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Store.getEN());
        }
        return storeOptional.get();
    }

    static Store getStoreByManager(final long managerId) throws JoorJensException {
        final Store store = REPO_STORE.find(managerId);
        if (store == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Store.getEN());
        }
        return store;
    }

    static boolean canBeStoreAdmin(final Customer customer) throws JoorJensException {
        return customer.getRoleId() == TypeEnumeration.UR_CUSTOMER.getId()
                || customer.getRoleId() == TypeEnumeration.UR_STORE_ADMIN.getId();
    }

    static void saveImages(final Store store, final Store storePre, final UUID uuid, final boolean confirm) throws JoorJensException {
        if (confirm) {
            store.setImageBusinessLicense(ImageRW.confirmImage(Config.baseFolderStore, store.getImageBusinessLicense(), storePre.getImageBusinessLicense(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderStore + Config.TEMPORARY_PREFIX + uuid + "_businessLicense";
            store.setImageBusinessLicense(ImageRW.saveImage(Config.baseFolderStore, store.getImageBusinessLicense(), imageAddress, (storePre != null ? storePre.getImageBusinessLicense() : ""), false, true, false));
        }

        if (confirm) {
            store.setManagerImageP(ImageRW.confirmImage(Config.baseFolderStore, store.getManagerImageP(), storePre.getManagerImageP(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderStore + Config.TEMPORARY_PREFIX + uuid + "_profile";
            store.setManagerImageP(ImageRW.saveImage(Config.baseFolderStore, store.getManagerImageP(), imageAddress, (storePre != null ? storePre.getManagerImageP() : ""), false, true, true));
            if (Utility.isEmpty(store.getManagerImageP())) {
                store.setManagerImageP(Config.DEFAULT_PROFILE_IMAGE_URL);
            }
        }

        if (confirm) {
            store.setManagerImageNI(ImageRW.confirmImage(Config.baseFolderStore, store.getManagerImageNI(), storePre.getManagerImageNI(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderStore + Config.TEMPORARY_PREFIX + uuid + "_nationalIdentifier";
            store.setManagerImageNI(ImageRW.saveImage(Config.baseFolderStore, store.getManagerImageNI(), imageAddress, (storePre != null ? storePre.getManagerImageNI() : ""), false, true, false));
        }

        if (confirm) {
            store.setManagerImageBC(ImageRW.confirmImage(Config.baseFolderStore, store.getManagerImageBC(), storePre.getManagerImageBC(), false, true, true));
        } else {
            final String imageAddress = Config.baseFolderStore + Config.TEMPORARY_PREFIX + uuid + "_birthCertificate";
            store.setManagerImageBC(ImageRW.saveImage(Config.baseFolderStore, store.getManagerImageBC(), imageAddress, (storePre != null ? storePre.getManagerImageBC() : ""), false, true, false));
        }
    }

    static void deleteTempImages(String body) throws JoorJensException {
        final Store store = FORM.get(body);
        ImageRW.deleteTempImage(store.getImageBusinessLicense());
        ImageRW.deleteTempImage(store.getManagerImageP());
        ImageRW.deleteTempImage(store.getManagerImageNI());
        ImageRW.deleteTempImage(store.getManagerImageBC());
    }
    //-------------------------------------------------------------------------------------------------
}