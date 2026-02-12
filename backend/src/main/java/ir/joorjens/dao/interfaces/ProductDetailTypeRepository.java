package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ProductDetailType;

import java.util.List;

public interface ProductDetailTypeRepository extends Repository<Long, ProductDetailType> {
    int update(long id, int childCount) throws JoorJensException;

    ProductDetailType getByName(String name, long parentId) throws JoorJensException;

    FetchResult<ProductDetailType> search(long id, String name, long parentId, boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<ProductDetailType> getAllTypes(long id, String name, long parentId, boolean like, Boolean block) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, long parentId, boolean like, Boolean block) throws JoorJensException;

    List<ProductDetailType> getAll(long parentId) throws JoorJensException;
}