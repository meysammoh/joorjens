package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorDiscontentTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.DistributorDiscontentType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DistributorDiscontentTypeController {

    private static final DistributorDiscontentTypeRepository REPO_TYPE = (DistributorDiscontentTypeRepository) RepositoryManager.getByEntity(DistributorDiscontentType.class);
    private static final FormFactory<DistributorDiscontentType> FORM = new FormFactory<>(DistributorDiscontentType.class);

    public DistributorDiscontentTypeController() {

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, DistributorDiscontentType.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_DISCONTENT_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static DistributorDiscontentType upsert(final Request req, final boolean update) throws JoorJensException {
        DistributorDiscontentType distributorDiscontentType = FORM.get(req.body());
        if (update && distributorDiscontentType.getId() > 0) { //update
            DistributorDiscontentType typePre = getDistributorDiscontentType(distributorDiscontentType.getId());
            distributorDiscontentType.setEdit(typePre);
        }
        AbstractController.upsert(req, DistributorDiscontentType.class, distributorDiscontentType, update);
        return distributorDiscontentType;
    }

    private static Object search(Request req, boolean view, boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getDistributorDiscontentType(id);
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
    static DistributorDiscontentType getDistributorDiscontentType(long id) throws JoorJensException {
        Optional<DistributorDiscontentType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorDiscontentType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}