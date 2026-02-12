package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Advertising;

public interface AdvertisingRepository extends Repository<Long, Advertising> {

    int updateClick(long id, long clickCount) throws JoorJensException;

    FetchResult<Advertising> search(long id, String title, String link, Boolean app, int type, int fromTime, int toTime, String orderTypeIds, String ascDescs, Boolean block,
                                    int max, int offset) throws JoorJensException;

}