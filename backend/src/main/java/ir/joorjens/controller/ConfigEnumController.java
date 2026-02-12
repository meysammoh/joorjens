package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.List;
import java.util.Map;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;

public class ConfigEnumController {

    public ConfigEnumController() {

        get(Config.API_PREFIX + UrlRolesType.CONFIG_ALL.getUrl(), (req, res) -> TypeEnumeration.getMapIdName(), json());

        get(Config.API_PREFIX + UrlRolesType.CONFIG_MAIN.getUrl(), (req, res) -> {
            Map<String, String> queryMap = AbstractController.getQueryMap(req, true);
            if (queryMap.containsKey("typeid")) {
                return getObject(TypeEnumeration.getList(Integer.parseInt(queryMap.get("typeid"))));
            }
            return getObject(TypeEnumeration.getList());
        });

        get(Config.API_PREFIX + UrlRolesType.CONFIG_HIERARCHICAL.getUrl(), (req, res) -> {
            Map<String, String> queryMap = AbstractController.getQueryMap(req, true);
            if (queryMap.containsKey("typeids")) {
                return getObject(queryMap.get("typeids"));
            }
            return getObject(TypeEnumeration.getMapList());
        });

        get(Config.API_PREFIX + UrlRolesType.CONFIG_ROLES.getUrl() + ":id/", (req, res) -> {
            final int id = Integer.parseInt(req.params(":id"));
            return getObject(TypeEnumeration.getListRoles(id));
        });

    }

    private static Object getObject(List<TypeEnumeration> list) throws JoorJensException {
        if (list == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND);
        }
        final StringBuilder sb = new StringBuilder("[");
        int counter = 0;
        for (TypeEnumeration type : list) {
            if (++counter > 1) {
                sb.append(',');
            }
            sb.append(type.toString());
        }
        return sb.append(']');
    }

    private static Object getObject(Map<Integer, List<TypeEnumeration>> mapList) throws JoorJensException {
        if (mapList == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND);
        }
        final StringBuilder sb = new StringBuilder("{");
        int counter = 0;
        for (Map.Entry<Integer, List<TypeEnumeration>> idList : mapList.entrySet()) {
            if (++counter > 1) {
                sb.append(',');
            }
            sb.append('"').append(idList.getKey()).append("\":").append(getObject(idList.getValue()));
        }
        return sb.append('}');
    }

    private static Object getObject(String typeIds) throws JoorJensException {
        if (typeIds == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND);
        }
        final String[] typeIdsArr = typeIds.split("\\s*,\\s*");
        final StringBuilder sb = new StringBuilder("{");
        int counter = 0;
        for (String typeId : typeIdsArr) {
            if (++counter > 1) {
                sb.append(',');
            }
            sb.append('"').append(typeId).append("\":").append(getObject(TypeEnumeration.getList(Integer.parseInt(typeId))));
        }
        return sb.append('}');
    }

}