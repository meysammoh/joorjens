package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.CustomerInterface;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "distributor_employee")
public class DistributorEmployee extends AbstractModel implements CustomerInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "همکاران شرکت پخش";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    private long id;
    private boolean banded;
    private String accountNumber;
    private int credit;
    private String note;
    @JsonIgnore
    private UUID uuid = Utility.getUUID();
    //------------------------------------------------
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_EMPLOYEE__CUSTOMER__customer"))
    @JsonIgnore
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_EMPLOYEE__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_last_login", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_EMPLOYEE__CUSTOMER_LOGIN__lastLogin"))
    @JsonIgnore
    private CustomerLogin lastLogin;
    //------------------------------------------------

    public DistributorEmployee() {
    }

    public DistributorEmployee(final Customer customer) {
        this.setCustomer(customer);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        setCustomerId(id);
    }

    @Override
    @JsonGetter
    public String getMobileNumber() {
        return (this.customer != null) ? this.customer.getMobileNumber() : null;
    }

    @Override
    @JsonSetter
    public void setMobileNumber(String mobileNumber) {
        if(!Utility.isEmpty(mobileNumber)) {
            setCustomer();
            this.customer.setMobileNumber(mobileNumber);
        }
    }

    @Override
    @JsonGetter
    public String getNationalIdentifier() {
        return (this.customer != null) ? this.customer.getNationalIdentifier() : null;
    }

    @Override
    @JsonSetter
    public void setNationalIdentifier(String nationalIdentifier) {
        if(!Utility.isEmpty(nationalIdentifier)) {
            setCustomer();
            this.customer.setNationalIdentifier(nationalIdentifier);
        }
    }

    @Override
    @JsonGetter
    public String getFirstName() {
        return (this.customer != null) ? this.customer.getFirstName() : null;
    }

    @Override
    @JsonSetter
    public void setFirstName(String firstName) {
        if(!Utility.isEmpty(firstName)) {
            setCustomer();
            this.customer.setFirstName(firstName);
        }
    }

    @Override
    @JsonGetter
    public String getLastName() {
        return (this.customer != null) ? this.customer.getLastName() : null;
    }

    @Override
    @JsonSetter
    public void setLastName(String lastName) {
        if(!Utility.isEmpty(lastName)) {
            setCustomer();
            this.customer.setLastName(lastName);
        }
    }

    @Override
    @JsonGetter
    public String getFatherName() {
        return (this.customer != null) ? this.customer.getFatherName() : null;
    }

    @Override
    @JsonSetter
    public void setFatherName(String fatherName) {
        if(!Utility.isEmpty(fatherName)) {
            setCustomer();
            this.customer.setFatherName(fatherName);
        }
    }

    @Override
    @JsonGetter
    public int getGenderType() {
        return (this.customer != null) ? this.customer.getGenderType() : 0;
    }

    @Override
    @JsonSetter
    public void setGenderType(int genderType) {
        if(genderType > 0) {
            setCustomer();
            this.customer.setGenderType(genderType);
        }
    }

    @Override
    @JsonGetter
    public int getBirthTime() {
        return (this.customer != null) ? this.customer.getBirthTime() : 0;
    }

    @Override
    @JsonSetter
    public void setBirthTime(int birthTime) {
        if(birthTime > 0) {
            setCustomer();
            this.customer.setBirthTime(birthTime);
        }
    }

    @Override
    @JsonGetter
    public String getImageProfile() {
        return (this.customer != null) ? this.customer.getImageProfile() : null;
    }

    @Override
    @JsonSetter
    public void setImageProfile(String imageProfile) {
        if(!Utility.isEmpty(imageProfile)) {
            setCustomer();
            this.customer.setImageProfile(imageProfile);
        }
    }

    @Override
    @JsonGetter
    public String getEmail() {
        return (this.customer != null) ? this.customer.getEmail() : null;
    }

    @Override
    @JsonSetter
    public void setEmail(String email) {
        if(!Utility.isEmpty(email)) {
            setCustomer();
            this.customer.setEmail(email);
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isBanded() {
        return banded;
    }

    @Override
    public void setBanded(boolean banded) {
        this.banded = banded;
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int getCredit() {
        return this.credit;
    }

    @Override
    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null && customer.getId() > 0) {
            setCustomerId(customer.getId());
        }
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public CustomerLogin getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(CustomerLogin lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.customer != null && this.customer.isValid() && this.distributor != null
                && TypeEnumeration.isDistributorEmployee(this.customer.getRoleType());
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    @Override
    public boolean isBlock() {
        return super.isBlock() || (customer != null && customer.isBlock());
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    @JsonGetter
    public String getName() {
        return (this.customer != null) ? this.customer.getName() : null;
    }

    @JsonGetter
    public String getGenderTypeName() {
        return (this.customer != null) ? this.customer.getGenderTypeName() : null;
    }

    @JsonGetter
    public long getRoleId() {
        return (this.customer != null) ? this.customer.getRoleId() : 0;
    }

    @JsonGetter
    public String getRoleName() {
        return (this.customer != null) ? this.customer.getRoleName() : null;
    }

    @Override
    @JsonGetter
    public String getLastLoginIp() {
        return (this.lastLogin != null) ? this.lastLogin.getIp() : null;
    }

    @Override
    @JsonGetter
    public int getLastLoginTime() {
        return (this.lastLogin != null) ? this.lastLogin.getCreatedTime() : 0;
    }

    @JsonGetter
    public long getLastLoginId() {
        return (this.lastLogin != null) ? this.lastLogin.getId() : 0;
    }

    @JsonGetter
    public long getDistributorId() {
        return (this.distributor != null) ? this.distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributor != null) ? this.distributor.getName() : null;
    }

    @JsonGetter
    public String getDistributorAreaCityName() {
        return (this.distributor != null) ? this.distributor.getAreaCityName() : null;
    }

    @JsonGetter
    public String getDistributorSerial() {
        return (this.distributor != null) ? this.distributor.getSerial() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setCustomerId(long id) {
        if (id > 0) {
            setCustomer();
            this.id = id;
            this.customer.setId(id);
        }
    }

    @JsonSetter
    public void setRoleId(long roleId) {
        if (roleId > 0) {
            setCustomer();
            this.customer.setRoleId(roleId);
        }
    }

    @JsonSetter
    public void setPassword(String password) {
        if (!Utility.isEmpty(password)) {
            setCustomer();
            this.customer.setPassword(password);
        }
    }

    @JsonSetter
    public void setPasswordRepeat(String passwordRepeat) {
        if (!Utility.isEmpty(passwordRepeat)) {
            setCustomer();
            this.customer.setPasswordRepeat(passwordRepeat);
        }
    }

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonIgnore
    private void setCustomer() {
        if (this.customer == null) {
            this.customer = new Customer();
        }
    }
    //-----------------------------------------------------------------------------------------------------------
}