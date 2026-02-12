package ir.joorjens.controller;

import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.PermissionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Permission;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class PermissionController {
    private static final PermissionRepository REPO_PERMISSION = (PermissionRepository) RepositoryManager.getByEntity(Permission.class);
    private static final FormFactory<Permission> FORM = new FormFactory<>(Permission.class);

    public PermissionController() {
        post(Config.API_PREFIX + UrlRolesType.PERMISSION_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.PERMISSION_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.PERMISSION_DELETE.getUrl() + ":id/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Permission.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.PERMISSION_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.PERMISSION_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.PERMISSION_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static Permission upsert(final Request req, final boolean update) throws JoorJensException {
        Permission permission = FORM.get(req.body());
        if (update && permission.getId() > 0) { //update
            Permission prePermission = getPermission(permission.getId());
            permission.setEdit(prePermission);
        }

        AbstractController.upsert(req, Permission.class, permission, update);
        return permission;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = queryMap.containsKey("id") ? Long.parseLong(queryMap.get("id")) : 0;
        final String url = queryMap.get("url");
        if (view) {
            if (!Utility.isEmpty(url)) {
                return getPermission(url);
            }
            return getPermission(id);
        }
        if (pair) {
            return REPO_PERMISSION.getAllPairs(id, url);
        }
        final int max = queryMap.containsKey("max") ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = queryMap.containsKey("offset") ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_PERMISSION.search(id, url, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Permission getPermission(long id) throws JoorJensException {
        final Optional<Permission> permissionOptional = REPO_PERMISSION.getByKey(id);
        if (!permissionOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Permission.getEN());
        }
        return permissionOptional.get();
    }

    public static Permission getPermission(String url) throws JoorJensException {
        final Permission permission = REPO_PERMISSION.getByUrl(url);
        if (permission == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Permission.getEN());
        }
        return permission;
    }
    //-------------------------------------------------------------------------------------------------
}