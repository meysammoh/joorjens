package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.LogRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Log;
import spark.Request;

import java.util.Map;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;

public class LogController {
    private static final LogRepository REPO_LOG = (LogRepository) RepositoryManager.getByEntity(Log.class);

    public LogController() {
        get(Config.API_PREFIX + UrlRolesType.LOG_SEARCH.getUrl(), (req, res) -> search(req), json());
    }

    private static FetchResult<Log> search(Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = queryMap.containsKey("id") ? Long.parseLong(queryMap.get("id")) : 0;
        long customerId = queryMap.containsKey("customerid") ? Long.parseLong(queryMap.get("customerid")) : 0;
        if (customerId == 0) {
            customerId = AAA.getCustomerId(req);
        }
        int actionType = queryMap.containsKey("actiontype") ? Integer.parseInt(queryMap.get("actiontype")) : 0;
        int resultType = queryMap.containsKey("resulttype") ? Integer.parseInt(queryMap.get("resulttype")) : 0;
        String className = queryMap.get("classname");
        int fromTime = queryMap.containsKey("fromtime") ? Integer.parseInt(queryMap.get("fromtime")) : 0;
        int toTime = queryMap.containsKey("totime") ? Integer.parseInt(queryMap.get("totime")) : 0;
        int max = queryMap.containsKey("max") ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = queryMap.containsKey("offset") ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_LOG.search(id, customerId, actionType, resultType, className, fromTime, toTime, max, offset);
    }
}