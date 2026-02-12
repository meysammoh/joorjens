package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.ProductDetailTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.ProductDetailType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ProductDetailTypeController {

    private static final ProductDetailTypeRepository REPO_TYPE = (ProductDetailTypeRepository) RepositoryManager.getByEntity(ProductDetailType.class);
    private static final FormFactory<ProductDetailType> FORM = new FormFactory<>(ProductDetailType.class);

    public ProductDetailTypeController() {

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_DELETE.getUrl() + ":id/", (req, res) -> remove(req), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_CHILD.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_DETAIL_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, false, true), json());
    }

    private static ProductDetailType upsert(final Request req, final boolean update) throws JoorJensException {
        final ProductDetailType productDetailType = FORM.get(req.body());
        if (update && productDetailType.getId() > 0) { //update
            ProductDetailType typePre = getProductDetailType(productDetailType.getId());
            productDetailType.setEdit(typePre);
        }

        if (productDetailType.isFistLevel()) {
            productDetailType.setParentFake();
        } else if (productDetailType.getParentId() > 0) {
            ProductDetailType parent = getProductDetailType(productDetailType.getParentId());
            productDetailType.setParent(parent);
        }
        AbstractController.upsert(req, ProductDetailType.class, productDetailType, update);
        if (!update && productDetailType.getParentId() > 0) {
            REPO_TYPE.update(productDetailType.getParentId(), 1);
        }
        return productDetailType;
    }

    private static ResponseMessage remove(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        if (id == ProductDetailType.getFakeId()) {
            throw new JoorJensException(ExceptionCode.FK_);
        }
        final ProductDetailType productDetailType = getProductDetailType(id);
        final ResponseMessage responseMessage = AbstractController.delete(req, ProductDetailType.class, id);
        if (responseMessage.isOk() && productDetailType.getParentId() > 0) {
            REPO_TYPE.update(productDetailType.getParentId(), -1);
        }
        return responseMessage;
    }

    private static Object search(final Request req, final boolean view, final boolean child, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getProductDetailType(id);
        }
        long parentId = (queryMap.containsKey("parentid")) ? Long.parseLong(queryMap.get("parentid")) : 0;
        final boolean firstLevel = (queryMap.containsKey("firstlevel")) && Boolean.parseBoolean(queryMap.get("firstlevel"));
        if (firstLevel) {
            parentId = ProductDetailType.getFakeId();
        }
        final String name = queryMap.get("name");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_TYPE.getAllPairs(id, name, parentId, like, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        if (child) {
            if (parentId <= 0) {
                throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductDetailType.getEN());
            }
            return REPO_TYPE.search(id, null, parentId, like, block, max, offset);
        }
        return REPO_TYPE.search(id, name, parentId, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static ProductDetailType getProductDetailType(final long id) throws JoorJensException {
        final Optional<ProductDetailType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductDetailType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}