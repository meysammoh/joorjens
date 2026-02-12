package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.BonusCard;

public interface BonusCardRepository extends Repository<Long, BonusCard> {

    int updateCount(long id, int count) throws JoorJensException;

    FetchResult<BonusCard> search(long id, String name
            , int countFrom, int countTo, int priceFrom, int priceTo, int digitFrom, int digitTo
            , int timeFF, int timeFT, int timeTF, int timeTT
            , boolean like, Boolean block, int max, int offset) throws JoorJensException;

}