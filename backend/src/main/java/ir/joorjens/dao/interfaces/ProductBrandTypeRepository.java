package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.ProductBrandType;

import java.util.List;
import java.util.Set;

public interface ProductBrandTypeRepository extends Repository<Long, ProductBrandType> {
    int update(long id, int childCount) throws JoorJensException;

    ProductBrandType getByName(String name, int pbType, long parentId) throws JoorJensException;

    FetchResult<ProductBrandType> search(long id, String name, int pbType, long parentId, Set<Long> productCategoryTypeIds
            , boolean like, Boolean block, int max, int offset) throws JoorJensException;

    List<ProductBrandType> getAllProductBrandTypes(long id, String name, int pbType, long parentId, Set<Long> productCategoryTypeIds
            , boolean like, Boolean block) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, int pbType, long parentId, Set<Long> productCategoryTypeIds
            , boolean like, Boolean block) throws JoorJensException;

    List<ProductBrandType> getAll(long parentId) throws JoorJensException;
}