package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.BannerRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Banner;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public class BannerRepositoryImpl
        extends RepositoryImpAbstract<Long, Banner>
        implements BannerRepository {

    @Override
    public FetchResult<Banner> search(long id, String title, String link, String orderTypeIds
            , String ascDescs, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Banner t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        addCommonQuery(query, map, id, block);
        if (!Utility.isEmpty(title)) {
            query.append(" and t.title=:title");
            map.put("title", title);
        }
        if (!Utility.isEmpty(link)) {
            query.append(" and t.link=:link");
            map.put("link", link);
        }
        return search(query.toString(), map, TypeEnumeration.getOrder("t", orderTypeIds, ascDescs), max, offset);
    }
}