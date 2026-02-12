package ir.joorjens.dao.interfaces;

import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.OrderStatusType;

import java.util.List;

public interface OrderStatusTypeRepository extends Repository<Long, OrderStatusType> {

    FetchResult<OrderStatusType> search(long id, String name, boolean like, int max, int offset) throws JoorJensException;

    List<Pair<Long, String>> getAllPairs(long id, String name, boolean like) throws JoorJensException;

    List<OrderStatusType> findAll() throws JoorJensException;
}