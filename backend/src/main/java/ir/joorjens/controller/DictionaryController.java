package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DictionaryRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Dictionary;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DictionaryController {

    private static final DictionaryRepository REPO_DICTIONARY = (DictionaryRepository) RepositoryManager.getByEntity(Dictionary.class);
    private static final FormFactory<Dictionary> FORM = new FormFactory<>(Dictionary.class);

    public DictionaryController() {

        post(Config.API_PREFIX + UrlRolesType.DICTIONARY_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DICTIONARY_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DICTIONARY_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Dictionary.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DICTIONARY_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.DICTIONARY_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.DICTIONARY_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Dictionary.class, id, block);
        }, json());

    }

    private static Dictionary upsert(final Request req, final boolean update) throws JoorJensException {
        final Dictionary dictionary = FORM.get(req.body());
        if (update && dictionary.getId() > 0) { //update
            final Dictionary dictionaryPre = getDictionary(dictionary.getId());
            dictionary.setEdit(dictionaryPre);
        }
        AbstractController.upsert(req, Dictionary.class, dictionary, update);
        return dictionary;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        final String name = queryMap.get("name");
        if (view) {
            if (id > 0) {
                return getDictionary(id);
            }
            if (name != null) {
                return getDictionary(name);
            }
        }
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DICTIONARY.search(id, name, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Dictionary getDictionary(final long id) throws JoorJensException {
        final Optional<Dictionary> dictionaryOptional = REPO_DICTIONARY.getByKey(id);
        if (!dictionaryOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Dictionary.getEN());
        }
        return dictionaryOptional.get();
    }

    static Dictionary getDictionary(final String name) throws JoorJensException {
        return REPO_DICTIONARY.findByName(name);
    }
    //-------------------------------------------------------------------------------------------------
}