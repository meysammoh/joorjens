package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ProductCategoryType;

import java.util.List;

public interface ProductCategoryTypeRepository extends Repository<Long, ProductCategoryType> {
    int update(long id, int childCount) throws JoorJensException;

    FetchResult<ProductCategoryType> search(long id, String name, long parentId, Boolean block, int max, int offset) throws JoorJensException;

    List<ProductCategoryType> getAllTypes(long id, String name, long parentId, Boolean block) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, long parentId, Boolean block) throws JoorJensException;

    List<ProductCategoryType> getAll(long parentId) throws JoorJensException;
}