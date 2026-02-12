package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.BonusCardDetailRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.BonusCardDetail;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public class BonusCardDetailRepositoryImpl
        extends RepositoryImpAbstract<Long, BonusCardDetail>
        implements BonusCardDetailRepository {

    @Override
    public BonusCardDetail findByNumber(String number, int status) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT t FROM BonusCardDetail t WHERE t.number=:number");
        final Map<String, Object> map = new HashMap<>();
        map.put("number", number);
        final TypeEnumeration statusType = TypeEnumeration.get(status);
        if (statusType != null) {
            switch (statusType) {
                case CS_USED:
                    query.append(" and t.customer is not null");
                    break;
                case CS_EXPIRED:
                    query.append(" and t.bonusCard.to<:currentTime");
                    map.put("currentTime", Utility.getCurrentTime());
                    break;
                case CS_NEW:
                    query.append(" and t.customer is null and t.bonusCard.to>=:currentTime");
                    map.put("currentTime", Utility.getCurrentTime());
                    break;
            }
        }
        return findBy(query.toString(), map);
    }

    @Override
    public FetchResult<BonusCardDetail> search(long id, String number, int status
            , int countFrom, int countTo, int priceFrom, int priceTo, int digitFrom, int digitTo
            , int timeFF, int timeFT, int timeTF, int timeTT
            , long bonusCardId, long customerId, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM BonusCardDetail t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(number)) {
            query.append(" and t.number=:number");
            map.put("number", number);
        }
        final TypeEnumeration statusType = TypeEnumeration.get(status);
        if (statusType != null) {
            switch (statusType) {
                case CS_USED:
                    query.append(" and t.customer is not null");
                    break;
                case CS_EXPIRED:
                    query.append(" and t.bonusCard.to<:currentTime");
                    map.put("currentTime", Utility.getCurrentTime());
                    break;
                case CS_NEW:
                    query.append(" and t.customer is null and t.bonusCard.to>=:currentTime");
                    map.put("currentTime", Utility.getCurrentTime());
                    break;
            }
        }

        if (countFrom > 0) {
            query.append(" and t.bonusCard.count>=:countFrom");
            map.put("countFrom", countFrom);
        }
        if (countTo > 0 && countTo >= countFrom) {
            query.append(" and t.bonusCard.count<=:countTo");
            map.put("countTo", countTo);
        }
        if (priceFrom > 0) {
            query.append(" and t.bonusCard.price>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            query.append(" and t.bonusCard.price<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (digitFrom > 0) {
            query.append(" and t.bonusCard.digit>=:digitFrom");
            map.put("digitFrom", digitFrom);
        }
        if (digitTo > 0 && digitTo >= digitFrom) {
            query.append(" and t.bonusCard.digit<=:digitTo");
            map.put("digitTo", digitTo);
        }

        if (timeFF > 0) {
            query.append(" and t.bonusCard.from>=:timeFF");
            map.put("timeFF", timeFF);
        }
        if (timeFT > 0 && timeFT >= timeFF) {
            query.append(" and t.bonusCard.from<=:timeFT");
            map.put("timeFT", timeFT);
        }
        if (timeTF > 0) {
            query.append(" and t.bonusCard.to>=:timeTF");
            map.put("timeTF", timeTF);
        }
        if (timeTT > 0 && timeTT >= timeTF) {
            query.append(" and t.bonusCard.to<=:timeTT");
            map.put("timeTT", timeTT);
        }

        if (bonusCardId > 0) {
            query.append(" and t.bonusCard.id=:bonusCardId");
            map.put("bonusCardId", bonusCardId);
        }
        if (customerId > 0) {
            query.append(" and t.customer.id=:customerId");
            map.put("customerId", customerId);
        }

        return search(query.toString(), map, max, offset);
    }
}