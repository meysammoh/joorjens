package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Position;

public interface PositionRepository extends Repository<Long, Position> {
    FetchResult<Position> search(long customerId, int timeFrom, int timeTo, int max, int offset) throws JoorJensException;
}