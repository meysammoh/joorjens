package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.background.PositionTask;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.PositionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Position;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class PositionController {
    private static final PositionRepository REPO_POSITION = (PositionRepository) RepositoryManager.getByEntity(Position.class);
    private static final FormFactory<Position> FORM = new FormFactory<>(Position.class);

    public PositionController() {

        post(Config.API_PREFIX + UrlRolesType.POSITION_ADD.getUrl(), (req, res) -> add(req), json());

        get(Config.API_PREFIX + UrlRolesType.POSITION_SEARCH.getUrl(), (req, res) -> search(req), json());
    }

    private static ResponseMessage add(Request req) throws JoorJensException {
        Position position = FORM.get(req.body());
        position.setCustomer(AAA.getCustomer(req));
        PositionTask.addPosition(position);
        return new ResponseMessage();
    }

    private static FetchResult<Position> search(Request req) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long customerId = (queryMap.containsKey("customerid")) ? Long.parseLong(queryMap.get("customerid")) : 0;
        if (customerId == 0) {
            customerId = AAA.getCustomerId(req);
        }
        int timeFrom = (queryMap.containsKey("timefrom")) ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        int timeTo = (queryMap.containsKey("timeto")) ? Integer.parseInt(queryMap.get("timeto")) : 0;
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_POSITION.search(customerId, timeFrom, timeTo, max, offset);
    }
}