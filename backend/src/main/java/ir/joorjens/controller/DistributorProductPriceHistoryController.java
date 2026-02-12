package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorProductPriceHistoryRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.DistributorProductPriceHistory;
import spark.Request;

import java.util.Map;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;

public class DistributorProductPriceHistoryController {

    private static final DistributorProductPriceHistoryRepository REPO_PRICE = (DistributorProductPriceHistoryRepository) RepositoryManager.getByEntity(DistributorProductPriceHistory.class);

    public DistributorProductPriceHistoryController() {
        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_PRODUCT_PRICE_HISTORY_SEARCH.getUrl(), (req, res) -> search(req), json());
    }

    private static FetchResult<DistributorProductPriceHistory> search(Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        final long distributorProductId = (queryMap.containsKey("distributorproductid")) ? Long.parseLong(queryMap.get("distributorproductid")) : 0;
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        distributorId = CartController.getDistributorId(req, distributorId, false);
        final long productId = (queryMap.containsKey("productid")) ? Long.parseLong(queryMap.get("productid")) : 0;
        final String barcode = queryMap.get("productbarcode");
        final int priceFrom = (queryMap.containsKey("pricefrom")) ? Integer.parseInt(queryMap.get("pricefrom")) : 0;
        final int priceTo = (queryMap.containsKey("priceto")) ? Integer.parseInt(queryMap.get("priceto")) : 0;
        final int timeFrom = (queryMap.containsKey("timefrom")) ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        final int timeTo = (queryMap.containsKey("timeto")) ? Integer.parseInt(queryMap.get("timeto")) : 0;
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PRICE.search(id, distributorProductId, distributorId, productId, barcode
                , priceFrom, priceTo, timeFrom, timeTo, max, offset);
    }
}