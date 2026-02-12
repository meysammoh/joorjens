package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ColorTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.ColorType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class ColorTypeController {

    private static final ColorTypeRepository REPO_TYPE = (ColorTypeRepository) RepositoryManager.getByEntity(ColorType.class);
    private static final FormFactory<ColorType> FORM = new FormFactory<>(ColorType.class);

    public ColorTypeController() {

        post(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, ColorType.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());

        post(Config.API_PREFIX + UrlRolesType.COLOR_TYPE_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, ColorType.class, id, block);
        }, json());
    }

    private static ColorType upsert(final Request req, final boolean update) throws JoorJensException {
        final ColorType colorType = FORM.get(req.body());
        if (update && colorType.getId() > 0) { //update
            final ColorType typePre = getColorType(colorType.getId());
            colorType.setEdit(typePre);
        }
        AbstractController.upsert(req, ColorType.class, colorType, update);
        return colorType;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getColorType(id);
        }
        final String name = queryMap.get("name");
        final String code = queryMap.get("code");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_TYPE.getAllPairs(id, name, code, block, like);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_TYPE.search(id, name, code, block, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static ColorType getColorType(final long id) throws JoorJensException {
        final Optional<ColorType> typeOptional = REPO_TYPE.getByKey(id);
        if (!typeOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ColorType.getEN());
        }
        return typeOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}