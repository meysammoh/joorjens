package ir.joorjens.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.RoleRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.entity.Role;
import ir.joorjens.model.entity.ValidIP;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class RoleController {

    private static final TypeReference<List<ValidIP>> TR_SET_IPS = new TypeReference<List<ValidIP>>() {
    };
    private static final RoleRepository REPO_ROLE = (RoleRepository) RepositoryManager.getByEntity(Role.class);
    private static final FormFactory<Role> FORM = new FormFactory<>(Role.class);
    private static final FormFactory<ValidIP> FORM_IP = new FormFactory<>(ValidIP.class);

    public RoleController() {

        post(Config.API_PREFIX + UrlRolesType.ROLE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.ROLE_IP_UPDATE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return updateIps(req, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.ROLE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.ROLE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.ROLE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.ROLE_PROFILE.getUrl(), (req, res) -> profile(req), json());

    }

    private static Role upsert(final Request req, final boolean update) throws JoorJensException {
        final Role role = FORM.get(req.body());
        if (update && role.getId() > 0) {
            final Role rolePre = getRole(role.getId());
            role.setEdit(rolePre);
        }
        AbstractController.upsert(req, Role.class, role, update);
        AAA.invalidateRole(role.getId());
        return role;
    }

    private static Role updateIps(final Request req, final long id) throws JoorJensException {
        final Role role = getRole(id);
        role.setEdit(role);
        final List<ValidIP> validIPList = FORM_IP.getList(req.body(), TR_SET_IPS);
        final Set<ValidIP> validIPSet = new HashSet<>();
        for (ValidIP validIP: validIPList) {
            validIP.setRole(role);
            validIP.isValid();
            validIPSet.add(validIP);
        }
        role.setValidIPs(validIPSet);
        AbstractController.upsert(req, Role.class, role, true);
        AAA.invalidateRole(role.getId());
        return role;
    }

    private static Object search(final Request req, final boolean view, final boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        long id = queryMap.containsKey("id") ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getRole(id);
        }
        String name = queryMap.get("name");
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_ROLE.getAllPairs(id, name, like, block);
        }
        int max = queryMap.containsKey("max") ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        int offset = queryMap.containsKey("offset") ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_ROLE.search(id, name, like, block, max, offset);

    }

    private static Role profile(Request req) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        return getRole(customer.getRoleId());
    }

    //-------------------------------------------------------------------------------------------------
    static Role getRole(long id) throws JoorJensException {
        Optional<Role> roleOptional = REPO_ROLE.getByKey(id);
        if (!roleOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Role.getEN());
        }
        return roleOptional.get();
    }
    //-------------------------------------------------------------------------------------------------
}