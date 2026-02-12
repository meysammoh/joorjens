package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.AreaRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Area;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class AreaController {

    private static final AreaRepository REPO_AREA = (AreaRepository) RepositoryManager.getByEntity(Area.class);
    private static final FormFactory<Area> FORM = new FormFactory<>(Area.class);

    public AreaController() {

        post(Config.API_PREFIX + UrlRolesType.AREA_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.AREA_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.AREA_DELETE.getUrl() + ":id/", (req, res) -> remove(req), json());

        get(Config.API_PREFIX + UrlRolesType.AREA_SEARCH.getUrl(), (req, res) -> search(req, false, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.AREA_VIEW.getUrl(), (req, res) -> search(req, true, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.AREA_CHILD.getUrl(), (req, res) -> search(req, false, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.AREA_PAIR.getUrl(), (req, res) -> search(req, false, false, true), json());

        post(Config.API_PREFIX + UrlRolesType.AREA_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Area.class, id, block);
        }, json());
    }

    private static Area upsert(final Request req, final boolean update) throws JoorJensException {
        final Area area = FORM.get(req.body());
        if (update && area.getId() > 0) { //update
            Area areaPre = getArea(area.getId());
            area.setEdit(areaPre);
        }

        if (area.isProvince()) {
            area.setParentFake();
        } else if (area.getParentId() > 0) {
            Area areaParent = getArea(area.getParentId());
            area.setParent(areaParent);
        }
        AbstractController.upsert(req, Area.class, area, update);
        if (!update && area.getParentId() > 0) {
            REPO_AREA.update(area.getParentId(), 1);
        }
        return area;
    }

    private static ResponseMessage remove(final Request req) throws JoorJensException {
        final long id = Long.parseLong(req.params(":id"));
        if (id == Area.getFakeId()) {
            throw new JoorJensException(ExceptionCode.FK_);
        }
        final Area area = getArea(id);
        final ResponseMessage responseMessage = AbstractController.delete(req, Area.class, id);
        if (responseMessage.isOk() && area.getParentId() > 0) {
            REPO_AREA.update(area.getParentId(), -1);
        }
        return responseMessage;
    }

    private static Object search(final Request req, final boolean view, final boolean child, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getArea(id);
        }
        long parentId = (queryMap.containsKey("parentid")) ? Long.parseLong(queryMap.get("parentid")) : 0;
        final boolean firstLevel = (queryMap.containsKey("firstlevel")) && Boolean.parseBoolean(queryMap.get("firstlevel"));
        if (firstLevel) {
            parentId = Area.getFakeId();
        }
        final String name = queryMap.get("name");
        final int adType = (queryMap.containsKey("adtype")) ? Integer.parseInt(queryMap.get("adtype")) : 0;
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_AREA.getAllPairs(id, name, adType, parentId, like, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        if (child) {
            if (parentId <= 0) {
                throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Area.getEN());
            }
            return REPO_AREA.search(id, null, 0, parentId, like, block, max, offset);
        }
        return REPO_AREA.search(id, name, adType, parentId, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Area getArea(final long id) throws JoorJensException {
        final Optional<Area> areaOptional = REPO_AREA.getByKey(id);
        if (!areaOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Area.getEN());
        }
        return areaOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}