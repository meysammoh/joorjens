package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.background.LogTask;
import ir.joorjens.common.Utility;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.DistributorPackageRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DistributorPackageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorPackageController.class);
    private static final DistributorPackageRepository REPO_PACKAGE = (DistributorPackageRepository) RepositoryManager.getByEntity(DistributorPackage.class);
    private static final FormFactory<DistributorPackage> FORM = new FormFactory<>(DistributorPackage.class);

    public DistributorPackageController() {

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
//            return AbstractController.delete(req, DistributorPackage.class, id);
            return remove(req, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, DistributorPackage.class, id, block);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PACKAGE_PRODUCT_COUNT_IN.getUrl(), (req, res) -> getProductCountIn(req), json());

    }

    private static DistributorPackage upsert(final Request req, final boolean update) throws JoorJensException {
        final DistributorPackage disPack = FORM.get(req.body());
        final String preImage;
        if (update && disPack.getId() > 0) { //update
            final DistributorPackage disPackPre = getDistributorPackage(disPack.getId());
            disPack.setEdit(disPackPre);
            preImage = disPackPre.getImage();
        } else {
            preImage = null;
        }

        final Distributor distributor = DistributorController.getDistributor(disPack.getDistributorId());
        disPack.setDistributor(distributor);
        for (DistributorPackageProduct dpp : disPack.getPackageProducts()) {
            final DistributorProduct distributorProduct = DistributorProductController.getDistributorProduct(dpp.getDistributorProductId());
            dpp.setDistributorProduct(distributorProduct);
            dpp.setDistributorPackage(disPack);
            dpp.isValid();
        }

        final String imageAddress = Config.baseFolderDistributorPackage + disPack.getUuid();
        disPack.setImage(ImageRW.saveImage(Config.baseFolderDistributorPackage, disPack.getImage(), imageAddress, preImage, false, true, true));

        AbstractController.upsert(req, DistributorPackage.class, disPack, update);
        return disPack;
    }

    private static ResponseMessage remove(final Request req, final long id) throws JoorJensException {
        //return AbstractController.delete(req, Product.class, id);
        final DistributorPackage distributorPackage = getDistributorPackage(id);
        final String className = DistributorPackage.class.getSimpleName();
        final long customerId = AAA.getCustomerIdForLog(req);
        REPO_PACKAGE.executeQueries(
                "delete from DistributorPackageProduct t where t.distributorPackage.id=" + id
                , "delete from DistributorPackage t where t.id=" + id
        );
        LogTask.addLog(new Log
                (customerId, TypeEnumeration.ACTION_DELETE.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                , className, String.format("id=%d, url=%s", id, Utility.getRequestInfo(req, true))
                , Utility.toJson(distributorPackage)));
        LOGGER.info(String.format("DELETE(%d) request was done: %s", id, Utility.getRequestInfo(req, false)));
        return new ResponseMessage(String.format(ResponseCode.DONE_DELETE.getMessage(), DistributorPackage.getEN()));
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getDistributorPackage(id);
        }
        final Boolean bundlingOrDiscount = (queryMap.containsKey("bundlingordiscount")) ? Boolean.parseBoolean(queryMap.get("bundlingordiscount")) : null;
        final int fromTime = (queryMap.containsKey("fromtime")) ? Integer.parseInt(queryMap.get("fromtime")) : 0;
        final int toTime = (queryMap.containsKey("totime")) ? Integer.parseInt(queryMap.get("totime")) : 0;
        final Set<Long> distributorIds = Utility.getSetLong(queryMap.get("distributorid"), ",");
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final String productBarcode = queryMap.get("productbarcode");
        final String productName = queryMap.get("productname");
        final String packageName = queryMap.get("packagename");
        final Boolean supportCheck = (queryMap.containsKey("supportcheck")) ? Boolean.parseBoolean(queryMap.get("supportcheck")) : null;
        final boolean onlyStocks = (queryMap.containsKey("onlystocks")) && Boolean.parseBoolean(queryMap.get("onlystocks"));
        final Boolean expired = (queryMap.containsKey("expired")) ? Boolean.parseBoolean(queryMap.get("expired")) : null;
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PACKAGE.search(id, distributorIds, productId, productBarcode, productName, packageName
                , bundlingOrDiscount, fromTime, toTime, supportCheck, onlyStocks, expired
                , orderTypeIds, ascDescs, like, block, max, offset);
    }

    private static ResponseMessage getProductCountIn(final Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final Boolean bundlingOrDiscount = (queryMap.containsKey("bundlingordiscount")) ? Boolean.parseBoolean(queryMap.get("bundlingordiscount")) : null;
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final String productBarcode = queryMap.get("productbarcode");
        //final String productName = queryMap.get("productname");
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final long count = REPO_PACKAGE.getProductCountIn(productId, productBarcode, bundlingOrDiscount, block);
        if(count == 0) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND);
        }
        return new ResponseMessage("" + count);
    }

    //-------------------------------------------------------------------------------------------------
    static DistributorPackage getDistributorPackage(final long id) throws JoorJensException {
        final Optional<DistributorPackage> packOptional = REPO_PACKAGE.getByKey(id);
        if (!packOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorPackage.getEN());
        }
        return packOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}