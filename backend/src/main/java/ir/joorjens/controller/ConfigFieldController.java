package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ConfigFieldRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.ConfigField;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class ConfigFieldController {

    private static final ConfigFieldRepository REPO_CONFIG = (ConfigFieldRepository) RepositoryManager.getByEntity(ConfigField.class);
    private static final FormFactory<ConfigField> FORM = new FormFactory<>(ConfigField.class);

    public ConfigFieldController() {

        post(Config.API_PREFIX + UrlRolesType.CONFIG_UPDATE.getUrl(), (req, res) -> upsert(req), json());

        get(Config.API_PREFIX + UrlRolesType.CONFIG_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.CONFIG_VIEW.getUrl(), (req, res) -> search(req, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.CONFIG_PAIR.getUrl(), (req, res) -> search(req, true, false), json());
    }

    private static ConfigField upsert(Request req) throws JoorJensException {
        ConfigField configField = FORM.get(req.body());
        if (configField.getId() > 0) { //update
            ConfigField configFieldPre = getConfigField(configField.getId());
            configField.setEdit(configFieldPre);
        }
        AbstractController.upsert(req, ConfigField.class, configField, true);
        CacheGuava.invalidateConfig(configField.getType());
        return configField;
    }

    private static Object search(Request req, boolean pair, boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getConfigField(id);
        }
        String name = queryMap.get("name");
        float valueFrom = (queryMap.containsKey("valuefrom")) ? Float.parseFloat(queryMap.get("valuefrom")) : 0;
        float valueTo = (queryMap.containsKey("valueto")) ? Float.parseFloat(queryMap.get("valueto")) : 0;
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        if (pair) {
            return REPO_CONFIG.getAllPairs(id, name, valueFrom, valueTo, like);
        }
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_CONFIG.search(id, name, valueFrom, valueTo, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    public static ConfigField getConfigField(long id) throws JoorJensException {
        Optional<ConfigField> fieldOptional = REPO_CONFIG.getByKey(id);
        if (!fieldOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, ConfigField.getEN());
        }
        return fieldOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}