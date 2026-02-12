package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.DistributorPromotion;

import java.util.List;

public interface DistributorPromotionRepository extends Repository<Long, DistributorPromotion> {

    FetchResult<DistributorPromotion> search(long id, int fromPrice, int toPrice
            , long distributorId, Boolean block, int max, int offset) throws JoorJensException;

    DistributorPromotion findByAmount(long distributorId, int buyingAmount) throws JoorJensException;

    DistributorPromotion findToAmount(long distributorId, int buyingAmount) throws JoorJensException;

    List<DistributorPromotion> getAll(long distributorId) throws JoorJensException;
}