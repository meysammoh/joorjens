package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.BonusCardDetail;
import ir.joorjens.model.entity.Dictionary;
import ir.joorjens.model.entity.ValidIP;

public interface ValidIPRepository extends Repository<Long, ValidIP> {

    boolean hasAccess(String ip, long roleId) throws JoorJensException;

    FetchResult<ValidIP> search(long id, String ip, long roleId, int max, int offset) throws JoorJensException;

}