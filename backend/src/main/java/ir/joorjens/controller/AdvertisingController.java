package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.AdvertisingRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Advertising;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class AdvertisingController {

    private static final AdvertisingRepository REPO_AD = (AdvertisingRepository) RepositoryManager.getByEntity(Advertising.class);
    private static final FormFactory<Advertising> FORM = new FormFactory<>(Advertising.class);

    public AdvertisingController() {

        post(Config.API_PREFIX + UrlRolesType.ADVERTISING_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.ADVERTISING_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.ADVERTISING_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Advertising.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.ADVERTISING_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.ADVERTISING_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.ADVERTISING_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Advertising.class, id, block);
        }, json());

        post(Config.API_PREFIX + UrlRolesType.ADVERTISING_CLICK.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return click(id);
        }, json());
    }

    private static Advertising upsert(final Request req, final boolean update) throws JoorJensException {
        final Advertising advertising = FORM.get(req.body());
        final String preImage;
        if (update && advertising.getId() > 0) { //update
            final Advertising advertisingPre = getAdvertising(advertising.getId());
            advertising.setEdit(advertisingPre);
            preImage = advertisingPre.getImage();
        } else {
            preImage = null;
        }

        final String imageAddress = Config.baseFolderAdvertising + advertising.getUuid();
        advertising.setImage(ImageRW.saveImage(Config.baseFolderAdvertising, advertising.getImage(), imageAddress, preImage, false, true, true));

        AbstractController.upsert(req, Advertising.class, advertising, update);
        return advertising;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getAdvertising(id);
        }
        final String title = queryMap.get("title");
        final String link = queryMap.get("link");
        final Boolean app = queryMap.containsKey("app") ? Boolean.parseBoolean(queryMap.get("app")) : null;
        final int type = (queryMap.containsKey("type")) ? Integer.parseInt(queryMap.get("type")) : 0;
        final int fromTime = (queryMap.containsKey("fromtime")) ? Integer.parseInt(queryMap.get("fromtime")) : 0;
        final int toTime = (queryMap.containsKey("totime")) ? Integer.parseInt(queryMap.get("totime")) : 0;
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_AD.search(id, title, link, app, type, fromTime, toTime, orderTypeIds, ascDescs, block, max, offset);
    }

    private static ResponseMessage click(final long id) throws JoorJensException {
        final int rowAffected = REPO_AD.updateClick(id, 1);
        if(rowAffected == 0) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND);
        }
        return new ResponseMessage();
    }

    //-------------------------------------------------------------------------------------------------
    static Advertising getAdvertising(final long id) throws JoorJensException {
        final Optional<Advertising> advertisingOptional = REPO_AD.getByKey(id);
        if (!advertisingOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Advertising.getEN());
        }
        return advertisingOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}