package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Promotion;

import java.util.List;

public interface PromotionRepository extends Repository<Long, Promotion> {

    FetchResult<Promotion> search(long id, int fromPrice, int toPrice, Boolean block, int max, int offset) throws JoorJensException;

    Promotion findByAmount(int buyingAmount) throws JoorJensException;

    Promotion findToAmount(int buyingAmount) throws JoorJensException;

    List<Promotion> getAll() throws JoorJensException;
}