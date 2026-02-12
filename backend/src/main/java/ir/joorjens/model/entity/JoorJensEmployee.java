package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.CustomerInterface;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "joorjens_employee")
public class JoorJensEmployee extends AbstractModel implements CustomerInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "همکاران جورجنس";
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
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "FK_JOORJENS_EMPLOYEE__CUSTOMER__customer"))
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_last_login", foreignKey = @ForeignKey(name = "FK_JOORJENS_EMPLOYEE__CUSTOMER_LOGIN__lastLogin"))
    @JsonIgnore
    private CustomerLogin lastLogin;
    //------------------------------------------------

    public JoorJensEmployee() {
    }

    public JoorJensEmployee(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getMobileNumber() {
        return (this.customer != null) ? this.customer.getMobileNumber() : null;
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        if (this.customer != null) {
            this.customer.setMobileNumber(mobileNumber);
        }
    }

    @Override
    public String getNationalIdentifier() {
        return (this.customer != null) ? this.customer.getNationalIdentifier() : null;
    }

    @Override
    public void setNationalIdentifier(String nationalIdentifier) {
        if (this.customer != null) {
            this.customer.setNationalIdentifier(nationalIdentifier);
        }
    }

    @Override
    public String getFirstName() {
        return (this.customer != null) ? this.customer.getFirstName() : null;
    }

    @Override
    public void setFirstName(String firstName) {
        if (this.customer != null) {
            this.customer.setFirstName(firstName);
        }
    }

    @Override
    public String getLastName() {
        return (this.customer != null) ? this.customer.getLastName() : null;
    }

    @Override
    public void setLastName(String lastName) {
        if (this.customer != null) {
            this.customer.setLastName(lastName);
        }
    }

    @Override
    public String getFatherName() {
        return (this.customer != null) ? this.customer.getFatherName() : null;
    }

    @Override
    public void setFatherName(String fatherName) {
        if (this.customer != null) {
            this.customer.setFatherName(fatherName);
        }
    }

    @Override
    public int getGenderType() {
        return (this.customer != null) ? this.customer.getGenderType() : 0;
    }

    @Override
    public void setGenderType(int genderType) {
        if (this.customer != null) {
            this.customer.setGenderType(genderType);
        }
    }

    @Override
    public int getBirthTime() {
        return (this.customer != null) ? this.customer.getBirthTime() : 0;
    }

    @Override
    public void setBirthTime(int birthTime) {

    }

    @Override
    public String getImageProfile() {
        return (this.customer != null) ? this.customer.getImageProfile() : null;
    }

    @Override
    public void setImageProfile(String imageProfile) {
        if (this.customer != null) {
            this.customer.setImageProfile(imageProfile);
        }
    }

    @Override
    public String getEmail() {
        return (this.customer != null) ? this.customer.getEmail() : null;
    }

    @Override
    public void setEmail(String email) {
        if (this.customer != null) {
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
    }

    public CustomerLogin getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(CustomerLogin lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.customer != null && this.customer.isValid();
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

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setCustomerId(long id) {
        if (id > 0) {
            this.customer = new Customer(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}