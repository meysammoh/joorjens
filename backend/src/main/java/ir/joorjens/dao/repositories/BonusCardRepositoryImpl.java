package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.BannerRepository;
import ir.joorjens.dao.interfaces.BonusCardRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Banner;
import ir.joorjens.model.entity.BonusCard;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public class BonusCardRepositoryImpl
        extends RepositoryImpAbstract<Long, BonusCard>
        implements BonusCardRepository {

    @Override
    public int updateCount(long id, int count) throws JoorJensException {
        if (id <= 0 && count == 0) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder("UPDATE BonusCard u SET u.updatedTime=:updatedTime");
        map.put("updatedTime", Utility.getCurrentTime());
        if (count != 0) {
            sb.append(",u.count=u.count+:count");
            map.put("count", count);
        }
        int rowAffected = 0;
        if (map.size() > 1) {
            sb.append(" WHERE u.id=:id");
            map.put("id", id);
            rowAffected = update(sb.toString(), map);
        }
        return rowAffected;
    }

    @Override
    public FetchResult<BonusCard> search(long id, String name
            , int countFrom, int countTo, int priceFrom, int priceTo, int digitFrom, int digitTo
            , int timeFF, int timeFT, int timeTF, int timeTT
            , boolean like, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM BonusCard t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(name)) {
            query.append(" and t.name").append(like ? " like " : "=").append(":name");
            map.put("name", like ? '%' + name + '%' : name);
        }

        if (countFrom > 0) {
            query.append(" and t.count>=:countFrom");
            map.put("countFrom", countFrom);
        }
        if (countTo > 0 && countTo >= countFrom) {
            query.append(" and t.count<=:countTo");
            map.put("countTo", countTo);
        }
        if (priceFrom > 0) {
            query.append(" and t.price>=:priceFrom");
            map.put("priceFrom", priceFrom);
        }
        if (priceTo > 0 && priceTo >= priceFrom) {
            query.append(" and t.price<=:priceTo");
            map.put("priceTo", priceTo);
        }
        if (digitFrom > 0) {
            query.append(" and t.digit>=:digitFrom");
            map.put("digitFrom", digitFrom);
        }
        if (digitTo > 0 && digitTo >= digitFrom) {
            query.append(" and t.digit<=:digitTo");
            map.put("digitTo", digitTo);
        }

        if (timeFF > 0) {
            query.append(" and t.from>=:timeFF");
            map.put("timeFF", timeFF);
        }
        if (timeFT > 0 && timeFT >= timeFF) {
            query.append(" and t.from<=:timeFT");
            map.put("timeFT", timeFT);
        }
        if (timeTF > 0) {
            query.append(" and t.to>=:timeTF");
            map.put("timeTF", timeTF);
        }
        if (timeTT > 0 && timeTT >= timeTF) {
            query.append(" and t.to<=:timeTT");
            map.put("timeTT", timeTT);
        }

        return search(query.toString(), map, max, offset);
    }
}