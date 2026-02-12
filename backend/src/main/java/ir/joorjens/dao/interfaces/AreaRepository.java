package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Area;

import java.util.List;

public interface AreaRepository extends Repository<Long, Area> {
    int update(long id, int childCount) throws JoorJensException;

    FetchResult<Area> search(long id, String name, int adType, long parentId, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Area> getAllAreas(long id, String name, int adType, long parentId, boolean like, Boolean block) throws JoorJensException;

    List<Third<Long, String, Long>> getAllPairs(long id, String name, int adType, long parentId, boolean like, Boolean block) throws JoorJensException;
}
