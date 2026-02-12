package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.BonusCardRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.BonusCard;
import ir.joorjens.model.entity.BonusCardDetail;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class BonusCardController {

    //-------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusCardController.class);
    //-------------------------------------------------------------------------------------------------
    private static final BonusCardRepository REPO_BONUS = (BonusCardRepository) RepositoryManager.getByEntity(BonusCard.class);
    private static final FormFactory<BonusCard> FORM = new FormFactory<>(BonusCard.class);
    //-------------------------------------------------------------------------------------------------

    public BonusCardController() {
        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_INSERT.getUrl(), (req, res) -> insert(req), json());

        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_UPDATE.getUrl(), (req, res) -> update(req), json());

        delete(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return remove(req, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.BONUS_CARD_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.BONUS_CARD_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, BonusCard.class, id, block);
        }, json());
    }

    private static BonusCard insert(final Request req) throws JoorJensException {
        final BonusCard bonusCard = FORM.get(req.body());
        bonusCard.clearBonusCardDetails();
        AbstractController.upsert(req, BonusCard.class, bonusCard, false);
        for (int i = 0; i < bonusCard.getCount(); i++) {
            final BonusCardDetail bcd = new BonusCardDetail(bonusCard);
            upsertBonusCardDetail(req, bonusCard, bcd, false);
        }
        return bonusCard;
    }

    private static BonusCard update(final Request req) throws JoorJensException {
        final BonusCard bonusCard = FORM.get(req.body());
        final BonusCard bonusCardPre = getBonusCard(bonusCard.getId());
        bonusCard.setEdit(bonusCardPre);
        final boolean isUsed = isBonusCardUsed(bonusCard.getId());
        final boolean isChangedDigit = bonusCard.getDigit() != bonusCardPre.getDigit();
        final boolean isChangedCount = bonusCard.getCount() != bonusCardPre.getCount();
        if (isUsed && (bonusCard.getPrice() != bonusCardPre.getPrice()
                || isChangedCount || isChangedDigit)) {
            throw new JoorJensException(ExceptionCode.FK_BONUS_CARD);
        }
        if (isChangedDigit) {
            for (BonusCardDetail bcd : bonusCard.getBonusCardDetails()) {
                upsertBonusCardDetail(req, bonusCard, bcd, true);
            }
        }
        if (isChangedCount) {
            if (bonusCard.getCount() < bonusCardPre.getCount()) {
                int counter = 0;
                final Set<BonusCardDetail> bonusCardDetails = new HashSet<>(bonusCard.getBonusCardDetails());
                bonusCard.clearBonusCardDetails();
                for (BonusCardDetail bcd : bonusCardDetails) {
                    if (++counter > bonusCard.getCount()) {
                        break;
                    }
                    bonusCard.addBonusCardDetails(bcd);
                }
            } else {
                for (int i = bonusCardPre.getCount(); i < bonusCard.getCount(); i++) {
                    final BonusCardDetail bcd = new BonusCardDetail(bonusCard);
                    upsertBonusCardDetail(req, bonusCard, bcd, false);
                    bonusCard.addBonusCardDetails(bcd);
                }
            }
        }
        AbstractController.upsert(req, BonusCard.class, bonusCard, true);
        return bonusCard;
    }

    private static ResponseMessage remove(final Request req, final long id) throws JoorJensException {
        if (isBonusCardUsed(id)) {
            throw new JoorJensException(ExceptionCode.FK_BONUS_CARD);
        }
        return AbstractController.delete(req, BonusCard.class, id);
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getBonusCard(id);
        }

        final String name = queryMap.get("name");
        final int countFrom = (queryMap.containsKey("countfrom")) ? Integer.parseInt(queryMap.get("countfrom")) : 0;
        final int countTo = (queryMap.containsKey("countto")) ? Integer.parseInt(queryMap.get("countto")) : 0;
        final int priceFrom = (queryMap.containsKey("pricefrom")) ? Integer.parseInt(queryMap.get("pricefrom")) : 0;
        final int priceTo = (queryMap.containsKey("priceto")) ? Integer.parseInt(queryMap.get("priceto")) : 0;
        final int digitFrom = (queryMap.containsKey("digitfrom")) ? Integer.parseInt(queryMap.get("digitfrom")) : 0;
        final int digitTo = (queryMap.containsKey("digitto")) ? Integer.parseInt(queryMap.get("digitto")) : 0;
        final int timeFF = (queryMap.containsKey("timeff")) ? Integer.parseInt(queryMap.get("timeff")) : 0;
        final int timeFT = (queryMap.containsKey("timeft")) ? Integer.parseInt(queryMap.get("timeft")) : 0;
        final int timeTF = (queryMap.containsKey("timetf")) ? Integer.parseInt(queryMap.get("timetf")) : 0;
        final int timeTT = (queryMap.containsKey("timett")) ? Integer.parseInt(queryMap.get("timett")) : 0;
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_BONUS.search(id, name, countFrom, countTo, priceFrom, priceTo
                , digitFrom, digitTo, timeFF, timeFT, timeTF, timeTT, like, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------

    static BonusCard getBonusCard(final long id) throws JoorJensException {
        final Optional<BonusCard> optional = REPO_BONUS.getByKey(id);
        if (!optional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, BonusCard.getEN());
        }
        return optional.get();
    }

    private static boolean isBonusCardUsed(final long id) throws JoorJensException {
        final BonusCard bonusCard = getBonusCard(id);
        for (BonusCardDetail bcd : bonusCard.getBonusCardDetails()) {
            if (TypeEnumeration.CS_USED == bcd.getStatus()) {
                return true;
            }
        }
        return false;
    }

    static int updateCount(final long id, final int count) throws JoorJensException {
        return REPO_BONUS.updateCount(id, count);
    }

    private static BonusCardDetail upsertBonusCardDetail(final Request req, final BonusCard bonusCard
            , final BonusCardDetail bcd, final boolean update) throws JoorJensException {
        int counter = 0;
        boolean exception;
        do {
            final long number = Utility.randomNumber(bonusCard.getDigit());
            bcd.setNumber(number + "");
            exception = false;
            try {
                AbstractController.upsert(req, BonusCardDetail.class, bcd, update);
            } catch (JoorJensException e) {
                if (e.getErrorCode() == ExceptionCode.UK_BONUS_CARD_DETAIL_NUMBER.getErrorCode()) {
                    ++counter;
                    exception = true;
                } else {
                    throw e;
                }
            }
        } while (exception);
        if (counter > 0) {
            LOGGER.warn(String.format("inserting BonusCardDetail unique key exception: %d", counter));
        }
        return bcd;
    }
    //-------------------------------------------------------------------------------------------------
}