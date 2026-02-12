package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.OrderStatusTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.OrderStatusType;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class OrderStatusTypeController {

    private static final OrderStatusTypeRepository REPO_ORDER_ST = (OrderStatusTypeRepository) RepositoryManager.getByEntity(OrderStatusType.class);
    private static final FormFactory<OrderStatusType> FORM = new FormFactory<>(OrderStatusType.class);

    public OrderStatusTypeController() {

        post(Config.API_PREFIX + UrlRolesType.ORDER_STATUS_TYPE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.ORDER_STATUS_TYPE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.ORDER_STATUS_TYPE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.ORDER_STATUS_TYPE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static OrderStatusType upsert(final Request req, final boolean update) throws JoorJensException {
        OrderStatusType orderST = FORM.get(req.body());
        if (update && orderST.getId() > 0) { //update
            OrderStatusType orderSTPre = getOrderStatusType(orderST.getId());
            orderST.setEdit(orderSTPre);
        }
        AbstractController.upsert(req, OrderStatusType.class, orderST, update);
        return orderST;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getOrderStatusType(id);
        }
        String name = queryMap.get("name");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        if (pair) {
            return REPO_ORDER_ST.getAllPairs(id, name, like);
        }
        int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_ORDER_ST.search(id, name, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static OrderStatusType getOrderStatusType(long id) throws JoorJensException {
        Optional<OrderStatusType> orderSTOptional = REPO_ORDER_ST.getByKey(id);
        if (!orderSTOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, OrderStatusType.getEN());
        }
        return orderSTOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}