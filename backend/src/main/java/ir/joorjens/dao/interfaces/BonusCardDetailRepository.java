package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.BonusCard;
import ir.joorjens.model.entity.BonusCardDetail;

public interface BonusCardDetailRepository extends Repository<Long, BonusCardDetail> {

    BonusCardDetail findByNumber(String number, int status) throws JoorJensException;

    FetchResult<BonusCardDetail> search(long id, String number, int status
            , int countFrom, int countTo, int priceFrom, int priceTo, int digitFrom, int digitTo
            , int timeFF, int timeFT, int timeTF, int timeTT
            , long bonusCardId, long customerId, Boolean block, int max, int offset) throws JoorJensException;

}