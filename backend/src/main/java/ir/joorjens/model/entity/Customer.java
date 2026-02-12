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
@Table(name = "customer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mobileNumber"}, name = "UK_CUSTOMER__mobileNumber"),
        @UniqueConstraint(columnNames = {"nationalIdentifier"}, name = "UK_CUSTOMER__nationalIdentifier")})
public class Customer extends AbstractModel implements CustomerInterface {
    private static final long serialVersionUID = 1395L;
    public static final int ACTIVATED = 1;
    public static final int CODE_LENGTH = 6;

    //------------------------------------------------
    public static String getEN() {
        return "مشتری";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //------------------------------------------------
    //in sso side
    @Column(nullable = false, length = 11)
    private String mobileNumber;
    @Column(length = 10)
    private String nationalIdentifier;
    private String firstName;
    private String lastName;
    private String fatherName;
    private int genderType;
    private int birthTime;
    private String imageProfile;
    private String imageNationalIdentifier;
    private String imageBirthCertificate;
    private String email;
    @Column(columnDefinition = "boolean default true", nullable = false)
    private boolean banded = true;
    private String accountNumber;
    private int credit;
    //------------------------------------------------
    @JsonIgnore
    private UUID uuid = Utility.getUUID();
    //------------------------------------------------
    private String note;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean inCartable = false;
    //------------------------------------------------
    @Column(nullable = false, length = 100)
    @JsonIgnore
    private String password;
    @JsonIgnore
    private int activationCode, resetPasswordCode;
    @JsonIgnore
    @Transient
    private int loginApp;
    @JsonIgnore
    @Transient
    private String passwordRepeat, passwordNew;
    @Transient
    private String captchaCode, captchaText;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_area_city", foreignKey = @ForeignKey(name = "FK_CUSTOMER__AREA__areaCity"))
    @JsonIgnore
    private Area areaCity;
    private String addressRemnant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", foreignKey = @ForeignKey(name = "FK_CUSTOMER__ROLE__role"))
    @JsonIgnore
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_last_login", foreignKey = @ForeignKey(name = "FK_CUSTOMER__CUSTOMER_LOGIN__lastLogin"))
    @JsonIgnore
    private CustomerLogin lastLogin;
    //------------------------------------------------

    public Customer() {
    }

    public Customer(long id) {
        this.id = id;
    }

    public void setEdit(final Customer customer) {
        super.setEdit(customer);
        this.mobileNumber = customer.mobileNumber;
        this.uuid = customer.uuid;
        this.banded = customer.banded;
        this.inCartable = customer.inCartable;
        this.credit = customer.credit;
        this.password = customer.password;
        this.activationCode = customer.activationCode;
        this.resetPasswordCode = customer.resetPasswordCode;
        this.lastLogin = customer.lastLogin;
    }

    //-----------------------------------------------------------------------------------------------------------

    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    @JsonSetter
    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    @JsonSetter
    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public int getActivationCode() {
        return activationCode;
    }

    @JsonSetter
    public void setActivationCode(int activationCode) {
        this.activationCode = activationCode;
    }

    public int getResetPasswordCode() {
        return resetPasswordCode;
    }

    @JsonSetter
    public void setResetPasswordCode(int resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public int getLoginApp() {
        return loginApp;
    }

    @JsonSetter
    public void setLoginApp(int loginApp) {
        this.loginApp = loginApp;
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

    @Override
    public String getMobileNumber() {
        return mobileNumber;
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String getNationalIdentifier() {
        return nationalIdentifier;
    }

    @Override
    public void setNationalIdentifier(String nationalIdentifier) {
        this.nationalIdentifier = nationalIdentifier;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = !Utility.isEmpty(firstName) ? firstName.trim() : null;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = !Utility.isEmpty(lastName) ? lastName.trim() : null;
    }

    @Override
    public String getFatherName() {
        return fatherName;
    }

    @Override
    public void setFatherName(String fatherName) {
        this.fatherName = !Utility.isEmpty(fatherName) ? fatherName.trim() : null;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getGenderType() {
        return genderType;
    }

    @Override
    public void setGenderType(int genderType) {
        final TypeEnumeration gt = TypeEnumeration.get(genderType);
        if (gt != null && TypeEnumeration.GENDER == gt.getParent()) {
            this.genderType = genderType;
        }
    }

    @Override
    public int getBirthTime() {
        return birthTime;
    }

    @Override
    public void setBirthTime(int birthTime) {
        this.birthTime = birthTime;
    }

    @Override
    public String getImageProfile() {
        return imageProfile;
    }

    @Override
    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageNationalIdentifier() {
        return imageNationalIdentifier;
    }

    public void setImageNationalIdentifier(String imageNationalIdentifier) {
        this.imageNationalIdentifier = imageNationalIdentifier;
    }

    public String getImageBirthCertificate() {
        return imageBirthCertificate;
    }

    public void setImageBirthCertificate(String imageBirthCertificate) {
        this.imageBirthCertificate = imageBirthCertificate;
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
        return credit;
    }

    @Override
    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public boolean isBanded() {
        return banded;
    }

    @Override
    public void setBanded(boolean banded) {
        this.banded = banded;
    }

    @Override
    public UUID getUuid() {
        return uuid;
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

    public boolean isInCartable() {
        return inCartable;
    }

    public void setInCartable(boolean inCartable) {
        this.inCartable = inCartable;
    }

    //-----------------------------------------------------------------------------------------------------------

    public String getAddressRemnant() {
        return addressRemnant;
    }

    public void setAddressRemnant(String addressRemnant) {
        this.addressRemnant = addressRemnant;
    }

    public Area getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(Area areaCity) {
        this.areaCity = areaCity;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public CustomerLogin getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(CustomerLogin lastLogin) {
        this.lastLogin = lastLogin;
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid() throws JoorJensException {
        final StringBuilder parameter = new StringBuilder();
        if (!Utility.validateMobileNumber(this.mobileNumber)) {
            parameter.append("موبایل");
        } else if (!Utility.isEmpty(this.nationalIdentifier) && !Utility.validateNationalIdentifier(this.nationalIdentifier)) {
            parameter.append(" کدملی");
        } else if (!Utility.isEmpty(this.email) && !Utility.validateEmail(this.email)) {
            parameter.append(" ایمیل");
        }
        final String param = parameter.toString().trim();
        final boolean OK = Utility.isEmpty(param);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, param);
        }
        return true;
    }

    public void addCredit(final int credit) {
        this.credit += credit;
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getName() {
        return (!Utility.isEmpty(firstName) || !Utility.isEmpty(lastName)) ? (firstName + " " + lastName).trim() : "بدون نام";
    }

    @JsonGetter
    public long getAreaCityId() {
        return (this.areaCity != null) ? this.areaCity.getId() : 0;
    }

    @JsonGetter
    public String getAreaCityName() {
        return (this.areaCity != null) ? this.areaCity.getName() : null;
    }

    @JsonGetter
    public long getAreaProId() {
        return (this.areaCity != null) ? this.areaCity.getParentId() : 0;
    }

    @JsonGetter
    public String getAreaProName() {
        return (this.areaCity != null) ? this.areaCity.getParentName() : null;
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
    public String getGenderTypeName() {
        TypeEnumeration gt = TypeEnumeration.get(this.genderType);
        return (gt != null) ? gt.getFa() : null;
    }

    @JsonGetter
    public long getLastLoginId() {
        return (this.lastLogin != null) ? this.lastLogin.getId() : 0;
    }

    @JsonGetter
    public String getLastLoginIp() {
        return (this.lastLogin != null) ? this.lastLogin.getIp() : null;
    }

    @JsonGetter
    public int getLastLoginTime() {
        return (this.lastLogin != null) ? this.lastLogin.getCreatedTime() : 0;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setAreaCityId(long id) {
        if(id > 0) {
            this.areaCity = new Area(id);
        }
    }

    @JsonSetter
    public void setRoleId(long id) {
        if(id > 0) {
            this.role = new Role(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonIgnore
    public TypeEnumeration getRoleType() {
        return (this.role != null) ? this.role.getRoleType() : TypeEnumeration.UR_CUSTOMER;
    }
    //-----------------------------------------------------------------------------------------------------------

}