package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Banner;

public interface BannerRepository extends Repository<Long, Banner> {

    FetchResult<Banner> search(long id, String title, String link, String orderTypeIds, String ascDescs, Boolean block
            , int max, int offset) throws JoorJensException;

}