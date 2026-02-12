package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductReturnTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.ProductReturnType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ProductReturnTypeController {

    private static final ProductReturnTypeRepository REPO_TYPE = (ProductReturnTypeRepository) RepositoryManager.getByEntity(ProductReturnType.class);
    private static final FormFactory<ProductReturnType> FORM = new FormFactory<>(ProductReturnType.class);

    public ProductReturnTypeController() {

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, ProductReturnType.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PRODUCT_RETURN_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static ProductReturnType upsert(final Request req, final boolean update) throws JoorJensException {
        ProductReturnType productReturnType = FORM.get(req.body());
        if (update && productReturnType.getId() > 0) { //update
            ProductReturnType typePre = getProductReturnType(productReturnType.getId());
            productReturnType.setEdit(typePre);
        }
        AbstractController.upsert(req, ProductReturnType.class, productReturnType, update);
        return productReturnType;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getProductReturnType(id);
        }
        String name = queryMap.get("name");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_TYPE.getAllPairs(id, name, like, block);
        }
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_TYPE.search(id, name, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static ProductReturnType getProductReturnType(long id) throws JoorJensException {
        Optional<ProductReturnType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ProductReturnType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}