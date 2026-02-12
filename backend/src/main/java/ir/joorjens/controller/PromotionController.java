package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.PromotionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Promotion;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class PromotionController {

    private static final PromotionRepository REPO_PROMOTION = (PromotionRepository) RepositoryManager.getByEntity(Promotion.class);
    private static final FormFactory<Promotion> FORM = new FormFactory<>(Promotion.class);

    public PromotionController() {

        post(Config.API_PREFIX + UrlRolesType.PROMOTION_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PROMOTION_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PROMOTION_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Promotion.class, id);
        }, json());

        delete(Config.API_PREFIX + UrlRolesType.PROMOTION_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Promotion.class, id, block);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.PROMOTION_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.PROMOTION_VIEW.getUrl(), (req, res) -> search(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.PROMOTION_MESSAGE.getUrl() + ":buyingAmount/", (req, res) -> {
            final int buyingAmount = Integer.parseInt(req.params(":buyingAmount"));
            return getMessage(buyingAmount);
        }, json());

    }

    private static Promotion upsert(final Request req, final boolean update) throws JoorJensException {
        final Promotion promotion = FORM.get(req.body());
        if (update && promotion.getId() > 0) { //update
            final Promotion promotionPre = getPromotion(promotion.getId());
            promotion.setEdit(promotionPre);
        }

        final List<Promotion> allPromotion = REPO_PROMOTION.getAll();
        allPromotion.add(promotion);
        if (!Utility.isFromToOk(allPromotion)) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "بازه‌ها", Promotion.getEN());
        }

        AbstractController.upsert(req, Promotion.class, promotion, update);
        return promotion;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getPromotion(id);
        }
        final int fromPrice = (queryMap.containsKey("fromprice")) ? Integer.parseInt(queryMap.get("fromprice")) : 0;
        final int toPrice = (queryMap.containsKey("toprice")) ? Integer.parseInt(queryMap.get("toprice")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PROMOTION.search(id, fromPrice, toPrice, block, max, offset);
    }

    private static ResponseMessage getMessage(final int buyingAmount) throws JoorJensException {
        final String message = getPromotionMessage(buyingAmount);
        if (message == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Promotion.getEN());
        }
        return new ResponseMessage(message);
    }

    //-------------------------------------------------------------------------------------------------
    static Promotion getPromotion(final long id) throws JoorJensException {
        final Optional<Promotion> promotionOptional = REPO_PROMOTION.getByKey(id);
        if (!promotionOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Promotion.getEN());
        }
        return promotionOptional.get();
    }

    static Promotion getPromotionByAmount(final int buyingAmount) throws JoorJensException {
        return REPO_PROMOTION.findByAmount(buyingAmount);
    }

    static Promotion getPromotionToAmount(final int buyingAmount) throws JoorJensException {
        return REPO_PROMOTION.findToAmount(buyingAmount);
    }

    static String getPromotionMessage(final int buyingAmount) throws JoorJensException {
        final Promotion promotion = getPromotionByAmount(buyingAmount);
        if (promotion != null && promotion.isShow(buyingAmount)) {
            final int buy = promotion.getToPrice() - buyingAmount;
            final String message = CacheGuava.getConfigValueStr(TypeEnumeration.CONFIG_PROMOTION_MESSAGE);
            return String.format(message, promotion.getToPrice(), promotion.getCredit(), buy);
        }
        return null;
    }
    //-------------------------------------------------------------------------------------------------
}