package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Customer;

import java.util.List;
import java.util.Set;

public interface CustomerRepository extends Repository<Long, Customer> {

    int update(long id, int credit) throws JoorJensException;

    int updateInCartable(long id, boolean inCartable) throws JoorJensException;

    Customer getByMobile(String mobile) throws JoorJensException;

    FetchResult<Customer> search(long id, long areaCityId, long areaProvinceId
            , Set<Long> roleIds, Set<Integer> roleTypes
            , String mobileNumber, String nationalIdentifier
            , String firstName, String lastName, Boolean block, int max, int offset) throws JoorJensException;

    List getAllPairs(long id, long areaCityId, long areaProvinceId
            , Set<Long> roleIds, Set<Integer> roleTypes
            , String mobileNumber, String nationalIdentifier
            , String firstName, String lastName, Boolean block) throws JoorJensException;
}