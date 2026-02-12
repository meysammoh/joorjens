package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.BonusCardDetailRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.BonusCard;
import ir.joorjens.model.entity.BonusCardDetail;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class BonusCardDetailController {

    //-------------------------------------------------------------------------------------------------
    private static final BonusCardDetailRepository REPO_BONUS_DETAIL = (BonusCardDetailRepository) RepositoryManager.getByEntity(BonusCardDetail.class);
    private static final FormFactory<BonusCardDetail> FORM = new FormFactory<>(BonusCardDetail.class);
    //-------------------------------------------------------------------------------------------------

    public BonusCardDetailController() {
        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return remove(req, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, BonusCardDetail.class, id, block);
        }, json());

        post(Config.API_PREFIX + UrlRolesType.BONUS_CARD_DETAIL_USE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return use(req, id);
        }, json());
    }

    private static BonusCardDetail upsert(final Request req, boolean update) throws JoorJensException {
        final BonusCardDetail bonusCardDetail = FORM.get(req.body());
        final BonusCard bonusCard = BonusCardController.getBonusCard(bonusCardDetail.getBonusCardId());
        bonusCardDetail.setBonusCard(bonusCard);

        if (update && bonusCardDetail.getId() > 0) {
            final BonusCardDetail bonusCardDetailPre = getBonusCardDetail(bonusCardDetail.getId());
            bonusCardDetail.setEdit(bonusCardDetailPre);
        }
        AbstractController.upsert(req, BonusCardDetail.class, bonusCardDetail, update);

        if (!update) {
            BonusCardController.updateCount(bonusCard.getId(), 1);
        }

        return bonusCardDetail;
    }

    private static ResponseMessage remove(final Request req, final long id) throws JoorJensException {
        final BonusCardDetail bcd = getBonusCardDetail(id);
        if (bcd.getStatus() == TypeEnumeration.CS_USED) {
            throw new JoorJensException(ExceptionCode.FK_BONUS_CARD);
        }
        final BonusCard bonusCard = BonusCardController.getBonusCard(bcd.getBonusCardId());
        bonusCard.removeBonusCardDetails(id);
        AbstractController.upsert(req, BonusCard.class, bonusCard, true);
        BonusCardController.updateCount(bonusCard.getId(), -1);
        return new ResponseMessage(String.format(ResponseCode.DONE_DELETE.getMessage(), BonusCardDetail.getEN()));
    }

    private static ResponseMessage use(final Request req, final long id) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final BonusCardDetail bcd = getBonusCardDetail(id);
        if (bcd.isBlock()) {
            throw new JoorJensException(ExceptionCode.OP_ENTITY_BLOCKED);
        }
        if (bcd.getStatus() == TypeEnumeration.CS_USED) {
            throw new JoorJensException(ExceptionCode.FK_BONUS_CARD);
        }
        bcd.setCustomer(customer);
        AbstractController.upsert(req, BonusCardDetail.class, bcd, true);
        CustomerController.updateCredit(customer, bcd.getBonusCardPrice());
        final ResponseMessage responseMessage = new ResponseMessage(ResponseCode.CARD_USED);
        responseMessage.setData(customer.getCredit());
        return responseMessage;
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        final String number = queryMap.get("number");
        if (view) {
            if (!Utility.isEmpty(number)) {
                return getBonusCardDetail(number);
            } else {
                return getBonusCardDetail(id);
            }
        }

        final int status = (queryMap.containsKey("status")) ? Integer.parseInt(queryMap.get("status")) : 0;
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
        final long bonusCardId = (queryMap.containsKey("bonuscardid")) ? Long.parseLong(queryMap.get("bonuscardid")) : 0;
        final long customerId = (queryMap.containsKey("customerid")) ? Long.parseLong(queryMap.get("customerid")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_BONUS_DETAIL.search(id, number, status, countFrom, countTo, priceFrom, priceTo
                , digitFrom, digitTo, timeFF, timeFT, timeTF, timeTT, bonusCardId, customerId, block, max, offset);
    }

    //-------------------------------------------------------------------------------------------------

    static BonusCardDetail getBonusCardDetail(final long id) throws JoorJensException {
        final Optional<BonusCardDetail> optional = REPO_BONUS_DETAIL.getByKey(id);
        if (!optional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, BonusCardDetail.getEN());
        }
        return optional.get();
    }

    static BonusCardDetail getBonusCardDetail(final String number) throws JoorJensException {
        final BonusCardDetail bonusCardDetail = REPO_BONUS_DETAIL.findByNumber(number, 0);
        if (bonusCardDetail == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, BonusCardDetail.getEN());
        }
        return bonusCardDetail;
    }

    //-------------------------------------------------------------------------------------------------
}