package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.ProductCategoryTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.businessEntity.CategoryHierarchical;
import ir.joorjens.model.entity.ProductCategoryDetailType;
import ir.joorjens.model.entity.ProductCategoryType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ProductCategoryTypeController {

    private static final ProductCategoryTypeRepository REPO_TYPE = (ProductCategoryTypeRepository) RepositoryManager.getByEntity(ProductCategoryType.class);
    private static final FormFactory<ProductCategoryType> FORM = new FormFactory<>(ProductCategoryType.class);

    public ProductCategoryTypeController() {

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_DELETE.getUrl() + ":id/", (req, res) -> remove(req), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_CHILD.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_CAT_TYPE_HIERARCHICAL.getUrl(), (req, res) -> hierarchical(ProductCategoryType.getFakeId()), json());
    }

    private static ProductCategoryType upsert(final Request req, final boolean update) throws JoorJensException {
        final ProductCategoryType productCategoryType = FORM.get(req.body());
        if (update && productCategoryType.getId() > 0) { //update
            ProductCategoryType typePre = getProductCategoryType(productCategoryType.getId());
            productCategoryType.setEdit(typePre);
        }

        if (productCategoryType.isFistLevel()) {
            productCategoryType.setParentFake();
        } else if (productCategoryType.getParentId() > 0) {
            ProductCategoryType parent = getProductCategoryType(productCategoryType.getParentId());
            productCategoryType.setParent(parent);
        }
        for (ProductCategoryDetailType pcd : productCategoryType.getProductDetailTypes()) {
            pcd.setFields(productCategoryType);
            pcd.isValid();
        }
        AbstractController.upsert(req, ProductCategoryType.class, productCategoryType, update);
        if (!update && productCategoryType.getParentId() > 0) {
            REPO_TYPE.update(productCategoryType.getParentId(), 1);
        }
        return productCategoryType;
    }

    private static ResponseMessage remove(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        if (id == ProductCategoryType.getFakeId()) {
            throw new JoorJensException(ExceptionCode.FK_);
        }
        final ProductCategoryType productCategoryType = getProductCategoryType(id);
        final ResponseMessage responseMessage = AbstractController.delete(req, ProductCategoryType.class, id);
        if (responseMessage.isOk() && productCategoryType.getParentId() > 0) {
            REPO_TYPE.update(productCategoryType.getParentId(), -1);
        }
        return responseMessage;
    }

    private static Object search(final Request req, final boolean view, final boolean child, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getProductCategoryType(id);
        }
        long parentId = (queryMap.containsKey("parentid")) ? Long.parseLong(queryMap.get("parentid")) : 0;
        final boolean firstLevel = (queryMap.containsKey("firstlevel")) && Boolean.parseBoolean(queryMap.get("firstlevel"));
        if (firstLevel) {
            parentId = ProductCategoryType.getFakeId();
        }
        final String name = queryMap.get("name");
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_TYPE.getAllPairs(id, name, parentId, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        if (child) {
            if (parentId <= 0) {
                throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductCategoryType.getEN());
            }
            return REPO_TYPE.search(id, null, parentId, block, max, offset);
        }
        return REPO_TYPE.search(id, name, parentId, block, max, offset);
    }

    private static List<CategoryHierarchical> hierarchical(final long parentId) throws JoorJensException {
        final List<CategoryHierarchical> list = new ArrayList<>();

        final List<Pair<Long, String>> parents = REPO_TYPE.getAllPairs(0, null, parentId, false);
        if(parents.size() == 0) {
            return list;
        }

        for(Pair<Long, String> parent: parents) {
            CategoryHierarchical hir = new CategoryHierarchical(parent.getFirst(), parent.getSecond());
            list.add(hir);
            hir.child.addAll(hierarchical(hir.id));
        }

        return list;
    }

    //-------------------------------------------------------------------------------------------------
    static ProductCategoryType getProductCategoryType(final long id) throws JoorJensException {
        final Optional<ProductCategoryType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductCategoryType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}