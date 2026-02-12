package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.Utility;
import ir.joorjens.common.email.EmailSend;
import ir.joorjens.common.file.ImageRW;
import ir.joorjens.common.response.*;
import ir.joorjens.dao.interfaces.CustomerRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.entity.Role;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.get;
import static spark.Spark.post;

public class CustomerController {

    private static final CustomerRepository REPO_CUSTOMER = (CustomerRepository) RepositoryManager.getByEntity(Customer.class);
    private static final FormFactory<Customer> FORM = new FormFactory<>(Customer.class);

    public CustomerController() {

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_SEARCH.getUrl(), (req, res) -> search(req, false, false), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_VIEW.getUrl(), (req, res) -> search(req, true, false), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_PAIR.getUrl(), (req, res) -> search(req, false, true), json());

        get(Config.API_PREFIX + UrlRolesType.CUSTOMER_PROFILE.getUrl(), (req, res) -> profile(req), json());

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_BLOCK.getUrl() + ":id/:block/", (req, res) -> {
            long id = Long.parseLong(req.params(":id"));
            final boolean block = Boolean.parseBoolean(req.params(":block"));
            return AbstractController.block(req, Customer.class, id, block);
        }, json());

        //---------------------------------------------------------------------------------------------

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_SIGNUP.getUrl(), (req, res) -> signup(req), json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_ACTIVATE.getUrl(), (req, res) -> activate(req), json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_RESEND_ACTIVATE.getUrl(), (req, res) -> resendActivationCode(req), json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_FORGET_PASS.getUrl(), (req, res) -> forgetPassword(req), json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_FORGET_PASS_VERIFY.getUrl(), (req, res) -> forgetPasswordVerify(req), json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_RESET_PASS.getUrl(), (req, res) -> resetPassword(req), json());

        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGIN.getUrl(), CustomerController::login, json());
        post(Config.API_PREFIX + UrlRolesType.CUSTOMER_LOGOUT.getUrl(), CustomerController::logout, json());

    }

    private static Customer upsert(final Request req, final boolean update) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        return upsert(req, customer, update, !update);
    }

    private static Object search(Request req, boolean view, boolean pair) throws JoorJensException {
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getCustomer(id);
        }
        final String mobileNumber = queryMap.get("mobilenumber");
        final String nationalIdentifier = queryMap.get("nationalidentifier");
        final String firstName = queryMap.get("firstname");
        final String lastName = queryMap.get("lastname");
        final long areaCityId = (queryMap.containsKey("areacityid")) ? Long.parseLong(queryMap.get("areacityid")) : 0;
        final long areaProvinceId = (queryMap.containsKey("areaprovinceid")) ? Long.parseLong(queryMap.get("areaprovinceid")) : 0;
        final Set<Long> roleIds = Utility.getSetLong(queryMap.get("roleid"), ",");
        final Set<Integer> roleTypes = Utility.getSetInteger(queryMap.get("roletype"), ",");
        final Boolean block = queryMap.containsKey("block") ? Boolean.parseBoolean(queryMap.get("block")) : null;
        if (pair) {
            return REPO_CUSTOMER.getAllPairs(id, areaCityId, areaProvinceId, roleIds, roleTypes,
                    mobileNumber, nationalIdentifier, firstName, lastName, block);
        }
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_CUSTOMER.search(id, areaCityId, areaProvinceId, roleIds, roleTypes,
                mobileNumber, nationalIdentifier, firstName, lastName, block, max, offset);
    }

    private static Customer profile(Request req) throws JoorJensException {
        return getCustomer(AAA.getCustomerId(req));
    }

    //-------------------------------------------------------------------------------------------------
    private static ResponseMessage signup(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final String password = Utility.generatePassword(customer.getPassword(), customer.getPasswordRepeat());
        customer.setPassword(password);
        customer.setActivationCode((int) Utility.randomNumber(Customer.CODE_LENGTH));
        customer.setRole(new Role(TypeEnumeration.UR_CUSTOMER.getId()));
        AbstractController.upsert(req, Customer.class, customer, false);
        //todo send activation code
        ResponseCode rs = ResponseCode.ACTIVATION_CODE_MOBILE;
        if (customer.getEmail() != null) {
            rs = ResponseCode.ACTIVATION_CODE_MOBILE_EMAIL;
            EmailSend.sendEmail(customer.getEmail(), customer.getActivationCode(), true);
        }
        final String message = rs.getMessage() + "-> " + customer.getActivationCode();
        return new ResponseMessage(message, Customer.CODE_LENGTH);
    }

    private static ResponseMessage resendActivationCode(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final Customer preCustomer = getCustomer(customer.getMobileNumber());
        preCustomer.setActivationCode((int) Utility.randomNumber(Customer.CODE_LENGTH));
        AbstractController.upsert(req, Customer.class, preCustomer, true);
        //todo send activation code
        ResponseCode rs = ResponseCode.ACTIVATION_CODE_MOBILE;
        if (preCustomer.getEmail() != null) {
            rs = ResponseCode.ACTIVATION_CODE_MOBILE_EMAIL;
            EmailSend.sendEmail(preCustomer.getEmail(), preCustomer.getActivationCode(), true);
        }
        final String message = rs.getMessage() + "-> " + preCustomer.getActivationCode();
        return new ResponseMessage(message, Customer.CODE_LENGTH);
    }

    private static Customer activate(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final Customer preCustomer = getCustomer(customer.getMobileNumber());
        if (preCustomer.getActivationCode() != customer.getActivationCode()) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "کد فعال‌سازی");
        }
        preCustomer.setActivationCode(Customer.ACTIVATED);
        preCustomer.setBanded(false);
        preCustomer.setBlock(false);
        AbstractController.upsert(req, Customer.class, preCustomer, true);
        //todo login
        return preCustomer;
    }

    private static ResponseMessage forgetPassword(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final Customer preCustomer = getCustomer(customer.getMobileNumber());
        preCustomer.setResetPasswordCode((int) Utility.randomNumber(Customer.CODE_LENGTH));
        AbstractController.upsert(req, Customer.class, preCustomer, true);
        //todo send activation code
        ResponseCode rs = ResponseCode.RESET_CODE_MOBILE;
        if (preCustomer.getEmail() != null) {
            rs = ResponseCode.RESET_CODE_MOBILE_EMAIL;
            EmailSend.sendEmail(preCustomer.getEmail(), preCustomer.getResetPasswordCode(), false);
        }
        final String message = rs.getMessage() + "-> " + preCustomer.getResetPasswordCode();
        return new ResponseMessage(message, Customer.CODE_LENGTH);
    }

    private static ResponseMessage forgetPasswordVerify(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final Customer preCustomer = getCustomer(customer.getMobileNumber());
        if (preCustomer.getResetPasswordCode() != customer.getResetPasswordCode()) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "کد فعال‌سازی");
        }
        final String password = Utility.generatePassword(customer.getPassword(), customer.getPasswordRepeat());
        preCustomer.setResetPasswordCode(Customer.ACTIVATED);
        preCustomer.setPassword(password);
        AbstractController.upsert(req, Customer.class, preCustomer, true);
        return new ResponseMessage(ResponseCode.PASSWORD_CHANGED);
    }

    private static ResponseMessage resetPassword(Request req) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        final Customer loginCustomer = AAA.getCustomer(req);
        final Customer preCustomer = getCustomer(loginCustomer.getId());
        String password = Utility.generatePassword(customer.getPassword());
        if (!password.equals(preCustomer.getPassword())) {
            throw new JoorJensException(ExceptionCode.OP_NOT_PASSWORD_MATCH);
        }
        password = Utility.generatePassword(customer.getPasswordNew(), customer.getPasswordRepeat());
        preCustomer.setPassword(password);
        AbstractController.upsert(req, Customer.class, preCustomer, true);
        //todo logout
        return new ResponseMessage(ResponseCode.PASSWORD_CHANGED);
    }

    private static Customer login(Request req, Response res) throws JoorJensException {
        final Customer customer = FORM.get(req.body());
        return AAA.login(req, res, customer, false);
    }

    private static ResponseMessage logout(Request req, Response res) throws JoorJensException {
        return AAA.logout(req, res);
    }

    //-------------------------------------------------------------------------------------------------
    static Customer getCustomer(long id) throws JoorJensException {
        Optional<Customer> customerOptional = REPO_CUSTOMER.getByKey(id);
        if (!customerOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Customer.getEN());
        }
        return customerOptional.get();
    }

    static Customer getCustomer(String mobile) throws JoorJensException {
        Customer customer = REPO_CUSTOMER.getByMobile(mobile);
        if (customer == null) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Customer.getEN());
        }
        return customer;
    }

    static boolean updateCustomerInCartable(long id, boolean inCartable) throws JoorJensException {
        final int rowAffected = REPO_CUSTOMER.updateInCartable(id, inCartable);
        return rowAffected == 1;
    }

    static void updateCredit(final Customer customer, final int credit) throws JoorJensException {
        if(customer != null) {
            customer.addCredit(credit);
            REPO_CUSTOMER.update(customer.getId(), credit);
            //todo create transaction
        }
    }

    static Customer upsert(final Request req, final Customer customer, boolean update, final boolean checkPassword) throws JoorJensException {
        if(checkPassword) { //insert
            final String password = Utility.generatePassword(customer.getPassword(), customer.getPasswordRepeat());
            customer.setPassword(password);
            customer.setActivationCode(Customer.ACTIVATED);
            customer.setBanded(false);
            customer.setBlock(false);
        }
        final String preProfile;
        if (update && customer.getId() > 0) { //update
            Customer preCustomer = getCustomer(customer.getId());
            customer.setEdit(preCustomer);
            preProfile = customer.getImageProfile();
        } else {
            preProfile = null;
        }

        if (customer.getAreaCityId() > 0) {
            AreaController.getArea(customer.getAreaCityId());
        }
        if (customer.getRoleId() > 0) {
            RoleController.getRole(customer.getRoleId());
        }

        final String imageAddress = Config.baseFolderCustomer + customer.getUuid();
        customer.setImageProfile(ImageRW.saveImage(Config.baseFolderCustomer, customer.getImageProfile(), imageAddress, preProfile, false, true, true));
        if (Utility.isEmpty(customer.getImageProfile())) {
            customer.setImageProfile(Config.DEFAULT_PROFILE_IMAGE_URL);
        }

        AbstractController.upsert(req, Customer.class, customer, update);
        return customer;
    }
    //-------------------------------------------------------------------------------------------------
}