package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends Repository<Long, Product> {

    int update(List<Pair<String, Integer>> barcodePriceList) throws JoorJensException;

    int update(long id, int price) throws JoorJensException;

    Product find(String barcode) throws JoorJensException;

    FetchResult<Product> search(long id, String barcode, String name
            , int priceFrom, int priceTo, float rateFrom, float rateTo
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs
            , boolean firstPage, boolean like, Boolean blockedByDistributor, Boolean block, int max, int offset) throws JoorJensException;

    List<Third<Long, String, String>> getAllPairs(long id, String barcode, String name
            , int priceFrom, int priceTo, float rateFrom, float rateTo
            , Set<Long> productBrandTypeIds, Set<Long> productCategoryTypeIds
            , boolean typesAreParent, String orderTypeIds, String ascDescs
            , boolean firstPage, boolean like, Boolean blockedByDistributor, Boolean block) throws JoorJensException;

}