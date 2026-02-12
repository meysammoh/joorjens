package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.joorjens.common.Utility;
import ir.joorjens.common.parser.UserAgent;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customer_session", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ip", "userAgent", "id_customer", "id_role"}, name = "UK_CUSTOMER_SESSION__ip_userAgent_customer_role")})
public class CustomerSession extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "جلسه";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //------------------------------------------------
    @Column(nullable = false, length = 25)
    private final String ip;
    @Column(nullable = false, length = 500)
    @JsonIgnore
    public final String userAgent;
    @Column(nullable = false, length = 500)
    @JsonIgnore
    public String uclaim;
    @Column(nullable = false)
    @JsonIgnore
    private UUID token;
    private int expiredTime = 0;
    private boolean reopen = false;

    @JsonIgnore
    public String browserManufacturer, browserGroup;
    @JsonIgnore
    public String browserVersion;
    @JsonIgnore
    public String osManufacturer, osGroup;
    @JsonIgnore
    public String device;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "FK_CUSTOMER_SESSION__CUSTOMER__customer"))
    @JsonIgnore
    private final Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", foreignKey = @ForeignKey(name = "FK_CUSTOMER_SESSION__ROLE__role"))
    @JsonIgnore
    private final Role role;
    //------------------------------------------------

    public CustomerSession() {
        this(0);
    }

    public CustomerSession(long id) {
        this.id = id;
        this.ip = this.userAgent = null;
        this.customer = null;
        this.role = null;
    }

    public CustomerSession(String ip, String userAgent, Customer customer, Role role) {
        this.ip = Utility.toLowerCase(ip);
        this.userAgent = Utility.toLowerCase(userAgent);
        this.customer = customer;
        this.role = role;
    }

    public void setFields() {
        final UserAgent ua = UserAgent.getUserAgent(this.userAgent);
        this.browserVersion = ua.browserVersion;
        this.browserManufacturer = ua.browserManufacturer;
        this.browserGroup = ua.browserGroup;
        this.osManufacturer = ua.osManufacturer;
        this.osGroup = ua.osGroup;
        this.device = ua.device;

        this.token = Utility.getUUID();
        this.uclaim = Jwts.builder()
                .setSubject(getCustomerMobileNumber())
                .setExpiration(new Date(1000L * getCacheExpireTime()))
                .claim("ip", getIp())
                .claim("userAgent", getUserAgent())
                .claim("customerId", getCustomerId())
                .claim("roleId", getRoleId())
                .signWith(SignatureAlgorithm.HS512, this.token.toString())
                .compact();
    }

    public String getKey() {
        return this.getCustomerId() + "#" + this.getRoleId() + "#" + this.ip + "#" + this.userAgent;
    }
    //-----------------------------------------------------------------------------------------------------------

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIp() {
        return ip;
    }

    public String getUclaim() {
        return uclaim;
    }

    public void setUclaim(String uclaim) {
        this.uclaim = uclaim;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isReopen() {
        return reopen;
    }

    public void setReopen(boolean reopen) {
        this.reopen = reopen;
    }

    public String getBrowserManufacturer() {
        return browserManufacturer;
    }

    public void setBrowserManufacturer(String browserManufacturer) {
        this.browserManufacturer = browserManufacturer;
    }

    public String getBrowserGroup() {
        return browserGroup;
    }

    public void setBrowserGroup(String browserGroup) {
        this.browserGroup = browserGroup;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getOsManufacturer() {
        return osManufacturer;
    }

    public void setOsManufacturer(String osManufacturer) {
        this.osManufacturer = osManufacturer;
    }

    public String getOsGroup() {
        return osGroup;
    }

    public void setOsGroup(String osGroup) {
        this.osGroup = osGroup;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    //-----------------------------------------------------------------------------------------------------------

    public Customer getCustomer() {
        return customer;
    }

    public Role getRole() {
        return role;
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid() throws JoorJensException {
        final boolean OK = customer != null && role != null
                && !Utility.isEmpty(ip) && !Utility.isEmpty(userAgent);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getCustomerId() {
        return (this.customer != null) ? this.customer.getId() : 0;
    }

    @JsonGetter
    public String getCustomerMobileNumber() {
        return (this.customer != null) ? this.customer.getMobileNumber() : null;
    }

    @JsonGetter
    public String getCustomerName() {
        return (this.customer != null) ? this.customer.getName() : null;
    }

    @JsonGetter
    public long getRoleId() {
        return (this.role != null) ? this.role.getId() : 0;
    }

    @JsonGetter
    public String getRoleName() {
        return (this.role != null) ? this.role.getName() : null;
    }

    @JsonGetter
    public boolean isActive() {
        return expiredTime == 0;
    }

    @JsonGetter
    public String getBrowser() {
        final StringBuilder sb = new StringBuilder();
        if (browserManufacturer != null) {
            sb.append(browserManufacturer).append(" ");
        }
        if (browserGroup != null) {
            sb.append(browserGroup).append(" ");
        }
        return sb.append(browserVersion).toString();
    }

    @JsonGetter
    public String getOS() {
        final StringBuilder sb = new StringBuilder();
        if (osManufacturer != null) {
            sb.append(osManufacturer).append(" ");
        }
        if (osGroup != null) {
            sb.append(osGroup).append(" ");
        }
        return sb.toString();
    }

    @JsonGetter
    public String getDeviceType() {
        return device != null ? device : "";
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomerSession) {
            CustomerSession cs = (CustomerSession) obj;
            return cs.getKey().equals(this.getKey());
        }
        return super.equals(obj);
    }


    //-----------------------------------------------------------------------------------------------------------

    public static int getCacheExpireTime() {
        return Utility.getCurrentTime() + (12 * Config.TIME_MONTH_SEC);
    }

    public static CustomerSession getCustomerSession(String uclaim, UUID token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(token.toString())
                    .parseClaimsJws(uclaim);
            Claims body = claims.getBody();
            long customerId = Long.parseLong(body.get("customerId").toString());
            long roleId = Long.parseLong(body.get("roleId").toString());
            String ip = body.get("ip").toString();
            String userAgent = body.get("userAgent").toString();
            return new CustomerSession(ip, userAgent, new Customer(customerId), new Role(roleId));
        } catch (Exception e) {
            //logger.debug(String.format("jwtException. Message: %s", e.getMessage()));
        }
        return null;
    }

    //-----------------------------------------------------------------------------------------------------------

}