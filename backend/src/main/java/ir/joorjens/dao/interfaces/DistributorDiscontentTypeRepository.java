package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorDiscontentType;

import java.util.List;

public interface DistributorDiscontentTypeRepository extends Repository<Long, DistributorDiscontentType> {

    FetchResult<DistributorDiscontentType> search(long id, String name, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, boolean like, Boolean block) throws JoorJensException;

    List<DistributorDiscontentType> findAll() throws JoorJensException;
}