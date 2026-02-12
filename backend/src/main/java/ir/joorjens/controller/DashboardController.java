package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.jmx.Config;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class DashboardController {

    public DashboardController() {

        get(Config.API_PREFIX + UrlRolesType.DASHBOARD_SALE.getUrl(), (req, res) -> CartController.dashboard(req, true), json());
        get(Config.API_PREFIX + UrlRolesType.DASHBOARD_ORDER.getUrl(), (req, res) -> CartController.dashboard(req, false), json());
        get(Config.API_PREFIX + UrlRolesType.DASHBOARD_PRODUCT.getUrl(), (req, res) -> DistributorProductController.dashboard(req), json());

    }

    //-------------------------------------------------------------------------------------------------
}