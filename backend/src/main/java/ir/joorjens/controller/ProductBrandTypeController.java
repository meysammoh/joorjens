package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.ProductBrandTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.ProductBrandType;
import ir.joorjens.model.entity.ProductCategoryType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ProductBrandTypeController {

    private static final ProductBrandTypeRepository REPO_PBT = (ProductBrandTypeRepository) RepositoryManager.getByEntity(ProductBrandType.class);
    private static final FormFactory<ProductBrandType> FORM = new FormFactory<>(ProductBrandType.class);

    public ProductBrandTypeController() {

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_DELETE.getUrl() + ":id/", (req, res) -> remove(req), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_CHILD.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, false, true), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_BRAND_TYPE_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, ProductBrandType.class, id, block);
        }, json());
    }

    private static ProductBrandType upsert(final Request req, final boolean update) throws JoorJensException {
        final ProductBrandType productBrandType = FORM.get(req.body());
        if (update && productBrandType.getId() > 0) { //update
            ProductBrandType pbtPre = getProductBrandType(productBrandType.getId());
            productBrandType.setEdit(pbtPre);
        }

        if (productBrandType.isMain()) {
            productBrandType.setParentFake();
        } else if (productBrandType.getParentId() > 0) {
            ProductBrandType pbtParent = getProductBrandType(productBrandType.getParentId());
            productBrandType.setParent(pbtParent);
        }

        if (productBrandType.getProductCategoryTypes() != null) {
            for (ProductCategoryType pct : productBrandType.getProductCategoryTypes()) {
                ProductCategoryTypeController.getProductCategoryType(pct.getId());
            }
        }

        AbstractController.upsert(req, ProductBrandType.class, productBrandType, update);
        if (!update && productBrandType.getParentId() > 0) {
            REPO_PBT.update(productBrandType.getParentId(), 1);
        }
        return productBrandType;
    }

    private static ResponseMessage remove(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        if (id == ProductBrandType.getFakeId()) {
            throw new JoorJensException(ExceptionCode.FK_);
        }
        final ProductBrandType productBrandType = getProductBrandType(id);
        final ResponseMessage responseMessage = AbstractController.delete(req, ProductBrandType.class, id);
        if (responseMessage.isOk() && productBrandType.getParentId() > 0) {
            REPO_PBT.update(productBrandType.getParentId(), -1);
        }
        return responseMessage;
    }

    private static Object search(final Request req, final boolean view, final boolean child, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getProductBrandType(id);
        }
        long parentId = (queryMap.containsKey("parentid")) ? Long.parseLong(queryMap.get("parentid")) : 0;
        final boolean firstLevel = (queryMap.containsKey("firstlevel")) && Boolean.parseBoolean(queryMap.get("firstlevel"));
        if (firstLevel) {
            parentId = ProductBrandType.getFakeId();
        }
        final String name = queryMap.get("name");
        final int pbType = (queryMap.containsKey("pbtype")) ? Integer.parseInt(queryMap.get("pbtype")) : 0;
        final Set<Long> productCategoryTypeIds = Utility.getSetLong(queryMap.get("productcategorytypeids"), ",");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_PBT.getAllPairs(id, name, pbType, parentId, productCategoryTypeIds, like, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        if (child) {
            if (parentId <= 0) {
                throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductBrandType.getEN());
            }
            return REPO_PBT.search(id, null, 0, parentId, productCategoryTypeIds, like, block, max, offset);
        }
        return REPO_PBT.search(id, name, pbType, parentId, productCategoryTypeIds, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static ProductBrandType getProductBrandType(final long id) throws JoorJensException {
        final Optional<ProductBrandType> pbtOptional = REPO_PBT.getByKey(id);
        if (!pbtOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductBrandType.getEN());
        }
        return pbtOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}