package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ColorType;

import java.util.List;

public interface ColorTypeRepository extends Repository<Long, ColorType> {

    FetchResult<ColorType> search(long id, String name, String code, Boolean block, boolean like, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, String code, Boolean block, boolean like) throws JoorJensException;

}