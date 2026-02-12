package ir.joorjens.dao.repositories;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.PositionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Position;

import java.util.HashMap;
import java.util.Map;

public class PositionRepositoryImpl
        extends RepositoryImpAbstract<Long, Position>
        implements PositionRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Position> search(long customerId, int timeFrom, int timeTo, int max, int offset) throws JoorJensException {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder("SELECT %s FROM Position t WHERE t.customer.id=:customerId");
        map.put("customerId", customerId);
        if (timeFrom > 0) {
            sb.append(" and t.time>=:timeFrom");
            map.put("timeFrom", timeFrom);
        }
        if (timeTo > 0 && timeTo > timeFrom) {
            sb.append(" and t.time<=:timeTo");
            map.put("timeTo", timeTo);
        }
        return search(sb.toString(), map, max, offset);
    }
}
