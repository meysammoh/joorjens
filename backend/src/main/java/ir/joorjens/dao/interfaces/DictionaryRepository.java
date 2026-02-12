package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Dictionary;

public interface DictionaryRepository extends Repository<Long, Dictionary> {

    Dictionary findByName(String name) throws JoorJensException;

    FetchResult<Dictionary> search(long id, String name, boolean like, Boolean block, int max, int offset) throws JoorJensException;

}