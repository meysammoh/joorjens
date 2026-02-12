package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.BannerRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Banner;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class BannerController {

    private static final BannerRepository REPO_BANNER = (BannerRepository) RepositoryManager.getByEntity(Banner.class);
    private static final FormFactory<Banner> FORM = new FormFactory<>(Banner.class);

    public BannerController() {

        post(Config.API_PREFIX + UrlRolesType.BANNER_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.BANNER_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.BANNER_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Banner.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.BANNER_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.BANNER_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.BANNER_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Banner.class, id, block);
        }, json());

    }

    private static Banner upsert(final Request req, final boolean update) throws JoorJensException {
        final Banner banner = FORM.get(req.body());
        final String preImage;
        if (update && banner.getId() > 0) { //update
            final Banner bannerPre = getBanner(banner.getId());
            banner.setEdit(bannerPre);
            preImage = bannerPre.getImage();
        } else {
            preImage = null;
        }

        final String imageAddress = Config.baseFolderBanner + banner.getUuid();
        banner.setImage(ImageRW.saveImage(Config.baseFolderBanner, banner.getImage(), imageAddress, preImage, false, true, true));

        AbstractController.upsert(req, Banner.class, banner, update);
        return banner;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getBanner(id);
        }
        final String title = queryMap.get("title");
        final String link = queryMap.get("link");
        final String orderTypeIds = queryMap.get("ordertypeid");
        final String ascDescs = queryMap.get("asc");
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_BANNER.search(id, title, link, orderTypeIds, ascDescs, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Banner getBanner(final long id) throws JoorJensException {
        final Optional<Banner> bannerOptional = REPO_BANNER.getByKey(id);
        if (!bannerOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Banner.getEN());
        }
        return bannerOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}