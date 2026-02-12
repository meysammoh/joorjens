package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorEmployeeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import spark.Request;

import java.util.Map;
import java.util.Optional;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class DistributorEmployeeController {

    private static final DistributorEmployeeRepository REPO_DIS_EMP = (DistributorEmployeeRepository) RepositoryManager.getByEntity(DistributorEmployee.class);
    private static final FormFactory<DistributorEmployee> FORM = new FormFactory<>(DistributorEmployee.class);

    public DistributorEmployeeController() {
        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EMPLOYEE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EMPLOYEE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EMPLOYEE_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EMPLOYEE_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.DISTRIBUTOR_EMPLOYEE_PAIR.getUrl(), (req, res) -> search(req, false, true), json());
    }

    private static DistributorEmployee upsert(final Request req, final boolean update) throws JoorJensException {
        final DistributorEmployee employee = FORM.get(req.body());
        return upsert(req, employee, update);
    }

    private static Object search(Request req, boolean view, boolean pair) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            final DistributorEmployee employee = getDistributorEmployee(id);
            switch (customer.getRoleType()) {
                case UR_DISTRIBUTION_OPERATOR:
                case UR_DISTRIBUTION_CONTROLLER:
                    if (customer.getId() != employee.getId()) {
                        throw new JoorJensException(ExceptionCode.OP_FORBIDDEN_FILTER);
                    }
                    break;
            }
            return employee;
        }
        final String mobileNumber = queryMap.get("mobilenumber");
        final String nationalIdentifier = queryMap.get("nationalidentifier");
        final String firstName = queryMap.get("firstname");
        final String lastName = queryMap.get("lastname");
        long distributorId = (queryMap.containsKey("distributorid")) ? Long.parseLong(queryMap.get("distributorid")) : 0;
        distributorId = CartController.getDistributorId(req, distributorId, false);
        final long roleId = (queryMap.containsKey("roleid")) ? Long.parseLong(queryMap.get("roleid")) : 0;
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_DIS_EMP.getAllPairs(id, distributorId, roleId, mobileNumber, nationalIdentifier
                    , firstName, lastName, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_DIS_EMP.search(id, distributorId, roleId, mobileNumber, nationalIdentifier
                , firstName, lastName, block, max, offset);
    }
    //-------------------------------------------------------------------------------------------------
    static DistributorEmployee getDistributorEmployee(final long id) throws JoorJensException {
        final DistributorEmployee distributorE = getDistributorEmployee(id, null, null);
        if (distributorE == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, DistributorEmployee.getEN());
        }
        return distributorE;
    }

    static DistributorEmployee getDistributorEmployee(long id, String mobile, String nationalIdentifier) throws JoorJensException {
        if (id > 0) {
            final Optional<DistributorEmployee> optional = REPO_DIS_EMP.getByKey(id);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                return null;
            }
        }
        return REPO_DIS_EMP.find(id, mobile, nationalIdentifier);
    }

    static DistributorEmployee upsert(final Request req, final DistributorEmployee employee, final boolean update) throws JoorJensException {
        if (update && employee.getId() > 0) { //update
            final DistributorEmployee employeePre = getDistributorEmployee(employee.getId());
            employee.setEdit(employeePre);
        }

        final Distributor distributor = DistributorController.getDistributor(employee.getDistributorId());
        employee.setDistributor(distributor);

        final boolean checkPassword = !update; //if insert then check password
        final Customer customer = employee.getCustomer();
        customer.setAreaCity(distributor.getAreaCity());
        CustomerController.upsert(req, customer, update, checkPassword);
        employee.setCustomer(customer);

        AbstractController.upsert(req, DistributorEmployee.class, employee, true);
        return employee;
    }

    //-------------------------------------------------------------------------------------------------
}