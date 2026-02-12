package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "customer_login")
public class CustomerLogin extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "لاگ ورود و خروج";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //------------------------------------------------
    private int day; //final
    private int loginTime;
    private int logoutTime;
    private int idlePeriod;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer_session", foreignKey = @ForeignKey(name = "FK_CUSTOMER_LOGIN__CUSTOMER_SESSION__customerSession"))
    @JsonIgnore
    private CustomerSession customerSession; //final
    //------------------------------------------------

    public CustomerLogin() {
        this.loginTime = this.logoutTime = this.day = 0;
        this.customerSession = null;
    }

    public CustomerLogin(CustomerSession customerSession) {
        this.customerSession = customerSession;
        this.setLoginTime(Utility.getCurrentTime());
        this.idlePeriod = 0;
    }

    public void setEdit(final CustomerLogin customerLogin) {
        super.setEdit(customerLogin);
        this.customerSession = customerLogin.customerSession;
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

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
        this.setDay(Utility.getTimeStamp(loginTime, TypeEnumeration.TS_DAY, true));
    }

    public int getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(int logoutTime) {
        this.logoutTime = logoutTime;
    }

    public int getIdlePeriod() {
        return idlePeriod;
    }

    public void setIdlePeriod(int idlePeriod) {
        this.idlePeriod = idlePeriod;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    //-----------------------------------------------------------------------------------------------------------

    public CustomerSession getCustomerSession() {
        return customerSession;
    }

    public void setCustomerSession(CustomerSession customerSession) {
        this.customerSession = customerSession;
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid() throws JoorJensException {
        final boolean OK = customerSession != null && day > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonIgnore
    public String getCustomerSessionKey() {
        return (this.customerSession != null) ? this.customerSession.getKey() : null;
    }

    @JsonGetter
    public long getCustomerSessionId() {
        return (this.customerSession != null) ? this.customerSession.getId() : 0;
    }

    @JsonGetter
    public long getCustomerId() {
        return (this.customerSession != null) ? this.customerSession.getCustomerId() : 0;
    }

    @JsonGetter
    public String getCustomerName() {
        return (this.customerSession != null) ? this.customerSession.getCustomerName() : null;
    }

    @JsonGetter
    public long getRoleId() {
        return (this.customerSession != null) ? this.customerSession.getRoleId() : 0;
    }

    @JsonGetter
    public String getRoleName() {
        return (this.customerSession != null) ? this.customerSession.getRoleName() : null;
    }

    @JsonGetter
    public String getIp() {
        return (this.customerSession != null) ? this.customerSession.getIp() : null;
    }

    @JsonGetter
    public boolean isActive() {
        return logoutTime == 0;
    }

    @JsonGetter
    public int getLoginPeriod() {
        int lgTime = getLogoutTime();
        if (lgTime == 0) {
            lgTime = Utility.getCurrentTime();
        }
        return lgTime - getLoginTime();
    }

    @JsonGetter
    public String getLoginPeriodStr() {
        return Utility.getTime(getLoginPeriod());
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setCustomerSessionId(long id) {
        if (id > 0) {
            this.customerSession = new CustomerSession(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------

}