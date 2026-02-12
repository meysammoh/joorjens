package ir.joorjens.aaa;

import com.google.common.cache.*;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.controller.AbstractController;
import ir.joorjens.controller.PermissionController;
import ir.joorjens.dao.interfaces.CustomerLoginRepository;
import ir.joorjens.dao.interfaces.CustomerRepository;
import ir.joorjens.dao.interfaces.CustomerSessionRepository;
import ir.joorjens.dao.interfaces.RoleRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import javax.ws.rs.core.Cookie;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class AAA {
    private static final Logger logger = LoggerFactory.getLogger(AAA.class);
    private static final CustomerRepository REPO_CUSTOMER = (CustomerRepository) RepositoryManager.getByEntity(Customer.class);
    private static final CustomerSessionRepository REPO_SESSION = (CustomerSessionRepository) RepositoryManager.getByEntity(CustomerSession.class);
    private static final CustomerLoginRepository REPO_LOGIN = (CustomerLoginRepository) RepositoryManager.getByEntity(CustomerLogin.class);
    private static final RoleRepository REPO_ROLE = (RoleRepository) RepositoryManager.getByEntity(Role.class);

    // ------------------------------------ NoLoginUrls -----------------------------------
    private static final Set<String> PUBLIC_URL_SET = new HashSet<>();

    public static void addPublicUrl(String url) {
        PUBLIC_URL_SET.add(url);
        //PUBLIC_URL_SET.add("customer/signup/");
    }

    // --------------------------------------- Params --------------------------------------
    private static final String UCLAIM = "uclaim";
    private static final int EXPIRE_SEC = Config.TIME_HOUR;
    // ----------------------------------- Calling Cache -----------------------------------
    private static final Object LOCK = new Object();
    private static final Map<String, String> CUSTOMER_UCLAIM = new HashMap<>();
    private static final LoadingCache<String, Customer> UCLAIM_CUSTOMER = CacheBuilder.newBuilder()
            .maximumSize(50_000)
            .expireAfterAccess(EXPIRE_SEC, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<String, Customer>() {
                @Override
                public void onRemoval(RemovalNotification<String, Customer> rn) {
                    final String uclaimReq = rn.getKey();
                    final CustomerLogin lastLogin = rn.getValue().getLastLogin();
                    updateLastLogin(uclaimReq, lastLogin, EXPIRE_SEC);
                }
            })
            .build(new CacheLoader<String, Customer>() {
                @Override
                public Customer load(String uclaim) throws JoorJensException {
                    return loadUclaimInfo(uclaim);
                }
            });

    private static final LoadingCache<Long, Set<String>> ROLE_PERMISSION = CacheBuilder.newBuilder()
            .maximumSize(1_000)
            .expireAfterWrite(EXPIRE_SEC, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, Set<String>>() {
                @Override
                public Set<String> load(Long id) throws JoorJensException {
                    Optional<Role> roleOptional = REPO_ROLE.getByKey(id);
                    if (roleOptional.isPresent()) {
                        return roleOptional.get().getPermissionUrls();
                    }
                    return null;
                }
            });

    private static final LoadingCache<String, Permission> PERMISSION_URL = CacheBuilder.newBuilder()
            .maximumSize(2_000)
            .expireAfterWrite(EXPIRE_SEC, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Permission>() {
                @Override
                public Permission load(String url) throws JoorJensException {
                    return PermissionController.getPermission(url);
                }
            });

    //------------------------------------------------------------------------------------------------------------------

    public static Customer login(Request req, Response res, Customer you, boolean afterActivation) throws JoorJensException {
        you.isValid();
        boolean OK = false;
        final Customer customer = REPO_CUSTOMER.getByMobile(you.getMobileNumber());
        if (customer != null) {
            if (afterActivation) {
                OK = true;
            } else {
                String password = Utility.generatePassword(you.getPassword());
                if (password.equals(customer.getPassword())) {
                    OK = true;
                }
            }
        }
        if (!OK) {
            throw new JoorJensException(ExceptionCode.OP_NOT_USER_PASSWORD);
        }
        if (customer.isBanded() || customer.isBlock()) {
            throw new JoorJensException(ExceptionCode.OP_USER_BANDED);
        }

        final Role role = customer.getRole();
        final String ip = AbstractController.getIp(req);
        if(!role.hasIp(ip)) {
            throw new JoorJensException(ExceptionCode.OP_USER_IP);
        }

        //todo check from app and role
        int app = customer.getLoginApp();

        CustomerSession customerSession = new CustomerSession(ip, req.userAgent(), customer, role);
        String uclaim = CUSTOMER_UCLAIM.get(customerSession.getKey());
        if (uclaim == null) {
            final CustomerSession customerSessionFind = REPO_SESSION.getByKey(customerSession);
            if (customerSessionFind != null) {
                customerSession = customerSessionFind;
                if (customerSession.isActive()) {
                    REPO_SESSION.updateAccess(customerSession.getId());
                } else {
                    customerSession.setExpiredTime(0);
                    customerSession.setReopen(true);
                    REPO_SESSION.update(customerSession);
                }
            } else {
                customerSession.setFields();
                REPO_SESSION.persist(customerSession);
            }
            final CustomerLogin customerLogin = getLastLogin(customerSession);
            if (customerLogin != null) {
                customer.setLastLogin(customerLogin);
                REPO_CUSTOMER.update(customer);
            }

            uclaim = customerSession.getUclaim();
            synchronized (LOCK) {
                UCLAIM_CUSTOMER.put(uclaim, customer);
                CUSTOMER_UCLAIM.put(customerSession.getKey(), uclaim);
            }
            logger.info(String.format("Customer(%d) with role(%d) login!", customer.getId(), role.getId()));
        } else {
            logger.debug(String.format("Customer(%d) with role(%d) login again!", customer.getId(), role.getId()));
        }
        res.cookie(Config.API_PREFIX, UCLAIM, uclaim, CustomerSession.getCacheExpireTime(), false, true);
        return customer;
    }

    public static ResponseMessage logout(Request req, Response res) throws JoorJensException {
        boolean OK = false;
        final Customer customer = AAA.getCustomer(req);
        final String uclaimOrReq = getUclaim(req);
        final CustomerSession customerSession = new CustomerSession(AbstractController.getIp(req)
                , req.userAgent(), customer, customer.getRole());
        final CustomerSession customerSessionFind = REPO_SESSION.getByKey(customerSession);
        //role may change, so customerSessionFind = null;
        if (customerSessionFind != null) {
            final CustomerLogin customerLogin = new CustomerLogin(customerSessionFind);
            final String uclaimExist = updateLastLogin(uclaimOrReq, customerLogin, 0);
            if (uclaimExist != null) {
                OK = true;
                UCLAIM_CUSTOMER.invalidate(uclaimExist);
            }
        } //if session Found

        if(uclaimOrReq != null && res != null) { //is from web not from onRemoval
            final Cookie cookie = new Cookie(UCLAIM, uclaimOrReq);
            res.cookie(Config.API_PREFIX, cookie.getName(), cookie.getValue(), 0, false, true);
        }
        if (OK) {
            logger.info(String.format("Customer(%d) with Role(%d) logout", customer.getId(), customer.getRoleId()));
        } else {
            logger.debug(String.format("CustomerInfo is null for uclaim: %s", uclaimOrReq));
            throw new JoorJensException(ExceptionCode.OP_NO_UCLAIM);
        }
        return new ResponseMessage(ResponseCode.LOGOUT);
    }

    public static void closeCustomerSession(CustomerSession customerSession) {
        synchronized (LOCK) {
            UCLAIM_CUSTOMER.invalidate(customerSession.getUclaim());
            CUSTOMER_UCLAIM.remove(customerSession.getKey());
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private static Customer loadUclaimInfo(final String uclaim) {
        Customer customer = null;
        try {
            final CustomerSession customerSession = REPO_SESSION.getByUclaim(uclaim);
            if (customerSession != null && customerSession.isActive()) {
                final CustomerSession customerSessionReq = CustomerSession.getCustomerSession(uclaim, customerSession.getToken());
                if (customerSessionReq != null && customerSession.equals(customerSessionReq)) {
                    REPO_SESSION.updateAccess(customerSession.getId());
                    logger.debug(String.format("session(uclaim) of Customer(%d) with role(%d) loaded!"
                            , customerSession.getCustomerId(), customerSession.getRoleId()));
                    getLastLogin(customerSession);
                    CUSTOMER_UCLAIM.put(customerSession.getKey(), uclaim);
                    customer = customerSession.getCustomer();
                } else {
                    logger.warn(String.format("session(uclaim) of Customer(%d) with role(%d) not OK! uclaim: %s"
                            , customerSession.getCustomerId(), customerSession.getRoleId(), uclaim));
                }
            } else {
                logger.warn(String.format("session(uclaim) not found! uclaim: %s", uclaim));
            }
        } catch (JoorJensException ignored) {
        }
        return customer;
    }

    private static CustomerLogin getLastLogin(final CustomerSession customerSession) {
        CustomerLogin customerLogin = null;
        try {
            int day = Utility.getTimeStamp(0, TypeEnumeration.TS_DAY, true);
            final CustomerLogin customerLoginLsat = REPO_LOGIN.getLast(customerSession.getId(), day);
            if (customerLoginLsat == null || customerLoginLsat.getLogoutTime() > 0) {
                customerLogin = new CustomerLogin(customerSession);
                REPO_LOGIN.persist(customerLogin);
                logger.debug(String.format("new login was inserted for Customer(%d) with Role(%d)!"
                        , customerSession.getCustomerId(), customerSession.getRoleId()));
            } else {
                customerLogin = customerLoginLsat;
            }
        } catch (JoorJensException ignored) {
        }
        if (customerLogin == null) {
            logger.warn(String.format("getLastLogin is null for Customer(%d) with Role(%d)!"
                    , customerSession.getCustomerId(), customerSession.getRoleId()));
        }
        return customerLogin;
    }

    private static String updateLastLogin(final String uclaimReq, final CustomerLogin lastLogin, final int idlePeriod) {
        String uclaimExist = null;
        boolean updated = false;
        long customerId = 0, roleId = 0;
        try {
            if (lastLogin != null) {
                customerId = lastLogin.getCustomerId();
                roleId = lastLogin.getRoleId();
                synchronized (LOCK) {
                    uclaimExist = CUSTOMER_UCLAIM.remove(lastLogin.getCustomerSessionKey());
                }
                if (uclaimExist != null && uclaimReq != null && uclaimReq.equals(uclaimExist)) {
                    int day = Utility.getTimeStamp(0, TypeEnumeration.TS_DAY, true);
                    final CustomerLogin customerLoginLsat = REPO_LOGIN.getLast(lastLogin.getCustomerSessionId(), day);
                    if (customerLoginLsat != null && customerLoginLsat.getLogoutTime() == 0) {
                        customerLoginLsat.setLogoutTime(Utility.getCurrentTime());
                        customerLoginLsat.setIdlePeriod(idlePeriod);
                        if (customerLoginLsat.getLoginPeriod() > Config.TIME_MIN) {
                            REPO_LOGIN.update(customerLoginLsat);
                        } else {
                            REPO_LOGIN.delete(customerLoginLsat);
                        }
                        updated = true;
                    }
                } // if uclaim are same
            } //if login is  not null
        } catch (JoorJensException ignored) {
        }
        logger.info(String.format("Customer(%d) with Role(%d) was updated(%s) with IdlePeriod(%d)."
                , customerId, roleId, updated, idlePeriod));
        return uclaimExist;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static Customer getCustomer(Request req) throws JoorJensException {
        final String uclaim = getUclaim(req);
        Customer customer = null;
        try {
            customer = UCLAIM_CUSTOMER.get(uclaim); //if remember customer
            //todo getIfPresent(uclaim);
        } catch (Exception ignored) {
        }
        if (customer == null) {
            throw new JoorJensException(ExceptionCode.OP_NOT_LOGIN);
        }
        return customer;
    }

    public static long getCustomerId(Request req) throws JoorJensException {
        return getCustomer(req).getId();
    }

    public static long getCustomerIdForLog(Request req) {
        long customerId = 0;
        try {
            customerId = getCustomerId(req);
        } catch (JoorJensException ignored) {
        }
        return customerId;
    }

    public static String getUclaim(Request req) throws JoorJensException {
        final String uClaim = req.cookie(UCLAIM);
        if(uClaim == null) {
            throw new JoorJensException(ExceptionCode.OP_NOT_LOGIN);
        }
        return uClaim;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static void invalidateRole(final long roleId) {
        if (null != ROLE_PERMISSION.getIfPresent(roleId)) {
            ROLE_PERMISSION.invalidate(roleId);
        }
    }

    public static boolean hasPermission(Request req) throws JoorJensException {
        final boolean OK;
        Customer customer = null;
        try {
            customer = getCustomer(req);
        } catch (JoorJensException ignored) {
        }
        if (customer == null) {
            OK = isPublic(req);
            if (!OK) {
                throw new JoorJensException(ExceptionCode.OP_NOT_LOGIN);
            }
        } else {
            final String apiUri = getApiUrl(req.uri());
            Permission permission = null;
            try {
                permission = PERMISSION_URL.get(apiUri);
            } catch (Exception ignored) {
            }
            if (permission != null) {
                Set<String> permissions = null;
                try {
                    permissions = ROLE_PERMISSION.get(customer.getRoleId());
                } catch (Exception e) {
                    logger.error(String.format("@hasPermission: role(%d) permission can not be loaded. Message: %s"
                            , customer.getRoleId(), e.getMessage()));
                }
                OK = permissions != null && permissions.contains(permission.getUrl());
            } else {
                //permission did not defined? ex pair will not be defined and just login required!
                OK = true;
            }
        }
        if (!OK) {
            throw new JoorJensException(ExceptionCode.OP_FORBIDDEN);
        }
        return true;
    }

    private static boolean isPublic(Request req) {
        String apiUri = getApiUrl(req.uri());
        return PUBLIC_URL_SET.contains(apiUri);
    }

    private static String getApiUrl(String uri) {
        String name = uri.substring(uri.indexOf(Config.API_PREFIX) + Config.API_PREFIX.length());
        String[] n = name.split("/");
        if (n.length > 1) {
            return String.format("%s/%s/", n[0], n[1]);
        }
        return "error";
    }

    //------------------------------------------------------------------------------------------------------------------

}