package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.DistributorPromotionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.DistributorPromotion;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DistributorPromotionController {

    private static final DistributorPromotionRepository REPO_PROMOTION = (DistributorPromotionRepository) RepositoryManager.getByEntity(DistributorPromotion.class);
    private static final FormFactory<DistributorPromotion> FORM = new FormFactory<>(DistributorPromotion.class);

    public DistributorPromotionController() {

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, DistributorPromotion.class, id);
        }, json());

        delete(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, DistributorPromotion.class, id, block);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_VIEW.getUrl(), (req, res) -> search(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PROMOTION_MESSAGE.getUrl() + ":buyingAmount/:distributorId", (req, res) -> {
            final int buyingAmount = Integer.parseInt(req.params(":buyingAmount"));
            final long distributorId = Long.parseLong(req.params(":distributorId"));
            return getMessage(distributorId, buyingAmount);
        }, json());

    }

    private static DistributorPromotion upsert(final Request req, final boolean update) throws JoorJensException {
        final DistributorPromotion promotion = FORM.get(req.body());
        if (update && promotion.getId() > 0) { //update
            final DistributorPromotion promotionPre = getDistributorPromotion(promotion.getId());
            promotion.setEdit(promotionPre);
        }

        final List<DistributorPromotion> allPromotion = REPO_PROMOTION.getAll(promotion.getDistributorId());
        allPromotion.add(promotion);
        if (!Utility.isFromToOk(allPromotion)) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "بازه‌ها", DistributorPromotion.getEN());
        }

        AbstractController.upsert(req, DistributorPromotion.class, promotion, update);
        return promotion;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getDistributorPromotion(id);
        }
        final long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        final int fromPrice = (queryMap.containsKey("fromprice")) ? Integer.parseInt(queryMap.get("fromprice")) : 0;
        final int toPrice = (queryMap.containsKey("toprice")) ? Integer.parseInt(queryMap.get("toprice")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PROMOTION.search(id, fromPrice, toPrice, distributorId, block, max, offset);
    }

    private static ResponseMessage getMessage(final long distributorId, final int buyingAmount) throws JoorJensException {
        final String message = getDistributorPromotionMessage(distributorId, buyingAmount);
        if (message == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorPromotion.getEN());
        }
        return new ResponseMessage(message);
    }

    //-------------------------------------------------------------------------------------------------
    static DistributorPromotion getDistributorPromotion(final long id) throws JoorJensException {
        final Optional<DistributorPromotion> promotionOptional = REPO_PROMOTION.getByKey(id);
        if (!promotionOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorPromotion.getEN());
        }
        return promotionOptional.get();
    }

    static DistributorPromotion getDistributorPromotionByAmount(final long distributorId, final int buyingAmount) throws JoorJensException {
        return REPO_PROMOTION.findByAmount(distributorId, buyingAmount);
    }

    static DistributorPromotion getDistributorPromotionToAmount(final long distributorId, final int buyingAmount) throws JoorJensException {
        return REPO_PROMOTION.findToAmount(distributorId, buyingAmount);
    }

    static String getDistributorPromotionMessage(final long distributorId, final int buyingAmount) throws JoorJensException {
        final DistributorPromotion promotion = getDistributorPromotionByAmount(distributorId, buyingAmount);
        if (promotion != null && promotion.isShow(buyingAmount)) {
            final int buy = promotion.getToPrice() - buyingAmount;
            final String message = CacheGuava.getConfigValueStr(TypeEnumeration.CONFIG_PROMOTION_MESSAGE);
            return String.format(message, promotion.getToPrice(), promotion.getCredit(), buy);
        }
        return null;
    }

    //-------------------------------------------------------------------------------------------------
}