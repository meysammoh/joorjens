package ir.joorjens.dao.repositories;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.PromotionRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionRepositoryImpl
        extends RepositoryImpAbstract<Long, Promotion>
        implements PromotionRepository {

    @Override
    public FetchResult<Promotion> search(long id, int fromPrice, int toPrice, Boolean block, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Promotion t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        setQueryField(query, map, id, fromPrice, toPrice, block);
        return search(query.toString(), map, max, offset);
    }

    @Override
    public Promotion findByAmount(int buyingAmount) throws JoorJensException {
        final String query = "SELECT t FROM Promotion t WHERE " + getWhereByAmount();
        final Map<String, Object> map = new HashMap<>();
        map.put("buyingAmount", buyingAmount);
        return findBy(query, map);
    }

    @Override
    public Promotion findToAmount(int buyingAmount) throws JoorJensException {
        final String query = "SELECT t FROM Promotion t WHERE " + getWhereToAmount();
        final Map<String, Object> map = new HashMap<>();
        map.put("buyingAmount", buyingAmount);
        return findBy(query, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Promotion> getAll() throws JoorJensException {
        final String query = "SELECT t FROM Promotion t";
        return findAllBy(query, null);
    }

    public static void setQueryField(final StringBuilder query, final Map<String, Object> map
            , long id, int fromPrice, int toPrice, Boolean block) throws JoorJensException {
        if (id > 0) {
            query.append(" AND t.id=:id");
            map.put("id", id);
        }
        if (block != null) {
            query.append(" AND t.block=:block");
            map.put("block", block);
        }
        if (fromPrice > 0) {
            query.append(" AND t.fromPrice>=:fromPrice");
            map.put("fromPrice", fromPrice);
        }
        if (toPrice > 0 && toPrice >= fromPrice) {
            query.append(" AND t.toPrice<=:toPrice");
            map.put("toPrice", toPrice);
        }
    }

    public static String getWhereByAmount() throws JoorJensException {
        //+ " and ((100-t.toPercent)*t.toPrice)<=:buyingAmount"; //this is exist in Promotion.class
        return "t.fromPrice<=:buyingAmount and t.toPrice>=:buyingAmount";
    }

    public static String getWhereToAmount() throws JoorJensException {
        return ":buyingAmount>=(t.toPrice-(((100-t.toPercent)/100)*t.toPrice)) ORDER BY t.toPrice DESC";
    }
}