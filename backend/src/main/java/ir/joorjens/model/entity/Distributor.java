package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "distributor", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial"}, name = "UK_DISTRIBUTOR__serial")})
public class Distributor extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "شرکت پخش";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "serial", nullable = false, length = 30)
    private String serial;

    @Column(nullable = false)
    private String name;
    private String note;
    private int type;
    private String registrationNumber;
    private String telephone, fax;
    private String site, email;
    private String imageStatute;
    private String imageBanner;
    @Column(columnDefinition = "int(11) default 0")
    private int childCount;
    //------------------------------------------------
    @Column(columnDefinition = "float default '100.00'")
    @JsonIgnore
    private float rateSum = 100;
    @Column(columnDefinition = "int(11) default 20")
    @JsonIgnore
    private int rateCount = 20;
    //------------------------------------------------
    @Column(columnDefinition = "float default '0.00'")
    private float dailySale = 0;
    //------------------------------------------------
    @Column(columnDefinition = "int(11) default 50")
    private int openCartsWarn = 50; //اگر تعداد فاکتورهای باز به این حد رسید هشدار داده شود
    @Column(columnDefinition = "int(11) default 100")
    private int openCartsBlock = 100;
    private int contractType;
    private int settlementDays;
    private int settlementDaysJoorJens;
    //------------------------------------------------
    private int settlementTypeTimeDays; //property for OrderSettlementType in Time mode;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_area_city", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__AREA__areaCity"))
    @JsonIgnore
    private Area areaCity;
    private String addressRemnant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parent", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__DISTRIBUTOR__parent"))
    @JsonIgnore
    private Distributor parent;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_manager", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__CUSTOMER__manager"))
    @JsonIgnore
    private Customer manager;
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "distributor__activity_Type",
            joinColumns = @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__PCATEGORY_TYPE__distributor")),
            inverseJoinColumns = @JoinColumn(name = "id_productCategoryType", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__PCATEGORY_TYPE__productCategoryType")))
    private Set<ProductCategoryType> activityTypes = new HashSet<>();
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "distributor__area",
            joinColumns = @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__AREA__distributor")),
            inverseJoinColumns = @JoinColumn(name = "id_area", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR__AREA__area")))
    private Set<Area> supportAreas = new HashSet<>();
    //------------------------------------------------

    public Distributor() {
    }

    public Distributor(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        final TypeEnumeration t = TypeEnumeration.get(type);
        if (t != null && TypeEnumeration.DISTRIBUTOR_TYPE == t.getParent()) {
            this.type = type;
        }
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageStatute() {
        return imageStatute;
    }

    public void setImageStatute(String imageStatute) {
        this.imageStatute = imageStatute;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public float getRateSum() {
        return rateSum;
    }

    public void setRateSum(float rateSum) {
        this.rateSum = rateSum;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public float getDailySale() {
        return dailySale;
    }

    public void setDailySale(float dailySale) {
        this.dailySale = dailySale;
    }

    public Area getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(Area areaCity) {
        this.areaCity = areaCity;
    }

    public String getAddressRemnant() {
        return addressRemnant;
    }

    public void setAddressRemnant(String addressRemnant) {
        this.addressRemnant = addressRemnant;
    }

    public Distributor getParent() {
        return parent;
    }

    public void setParent(Distributor parent) {
        this.parent = parent;
    }

    public Customer getManager() {
        return manager;
    }

    public void setManager(Customer manager) {
        this.manager = manager;
    }

    public int getOpenCartsWarn() {
        return openCartsWarn;
    }

    public void setOpenCartsWarn(int openCartsWarn) {
        this.openCartsWarn = openCartsWarn;
    }

    public int getOpenCartsBlock() {
        return openCartsBlock;
    }

    public void setOpenCartsBlock(int openCartsBlock) {
        this.openCartsBlock = openCartsBlock;
    }

    public int getContractType() {
        return contractType;
    }

    public void setContractType(int contractType) {
        final TypeEnumeration ct = TypeEnumeration.get(contractType);
        if (ct != null && TypeEnumeration.CONTRACT_TYPE == ct.getParent()) {
            this.contractType = contractType;
        }
    }

    public int getSettlementDays() {
        return settlementDays;
    }

    public void setSettlementDays(int settlementDays) {
        this.settlementDays = settlementDays;
    }

    public int getSettlementDaysJoorJens() {
        return settlementDaysJoorJens;
    }

    public void setSettlementDaysJoorJens(int settlementDaysJoorJens) {
        this.settlementDaysJoorJens = settlementDaysJoorJens;
    }

    public int getSettlementTypeTimeDays() {
        return settlementTypeTimeDays;
    }

    public void setSettlementTypeTimeDays(int settlementTypeTimeDays) {
        this.settlementTypeTimeDays = settlementTypeTimeDays;
    }

    public Set<ProductCategoryType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ProductCategoryType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public Set<Area> getSupportAreas() {
        return supportAreas;
    }

    public void setSupportAreas(Set<Area> supportAreas) {
        this.supportAreas = supportAreas;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final TypeEnumeration type = TypeEnumeration.get(this.type);
        if (type == null) {
            throw new JoorJensException(ExceptionCode.TYPE_UNDEFINED, getEN());
        }
        final boolean isTypeOK;
        switch (type) {
            case DT_MAIN:
                isTypeOK = this.parent == null;
                break;
            case DT_MAIN_BRANCH:
                isTypeOK = this.parent != null;
                break;
            default:
                throw new JoorJensException(ExceptionCode.TYPE_UNDEFINED, getEN());
        }
        if (!isTypeOK) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "کد شناسایی");
        }
        final boolean OK = !Utility.isEmpty(this.name) && this.areaCity != null && this.manager != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(Distributor distributor) {
        super.setEdit(distributor);
        setSerial(distributor.serial);
        setChildCount(distributor.childCount);
        setRateSum(distributor.rateSum);
        setRateCount(distributor.rateCount);
        setDailySale(distributor.dailySale);
        setSupportAreas(distributor.supportAreas);
        setParent(distributor.parent); //you can comment this
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getCreatedTime() {
        return super.getCreatedTime();
    }

    @JsonGetter
    public String getTypeName() {
        TypeEnumeration t = TypeEnumeration.get(this.type);
        return (t != null) ? t.getFa() : null;
    }

    @JsonGetter
    public float getRate() {
        return (float) Utility.round((rateSum / rateCount), 2);
    }

    @JsonGetter
    public long getAreaCityId() {
        return (this.areaCity != null) ? areaCity.getId() : 0;
    }

    @JsonGetter
    public String getAreaCityName() {
        return (this.areaCity != null) ? areaCity.getName() : null;
    }

    @JsonGetter
    public long getParentId() {
        return (this.parent != null) ? parent.getId() : 0;
    }

    @JsonGetter
    public String getParentName() {
        return (this.parent != null) ? parent.getName() : null;
    }

    @JsonGetter
    public String getParentSerial() {
        return (this.parent != null) ? parent.getSerial() : null;
    }

    @JsonGetter
    public long getManagerId() {
        return (this.manager != null) ? manager.getId() : 0;
    }

    @JsonGetter
    public String getManagerName() {
        return (this.manager != null) ? manager.getName() : null;
    }

    @JsonGetter
    public String getManagerMobile() {
        return (this.manager != null) ? manager.getMobileNumber() : null;
    }

    @JsonGetter
    public String getManagerNI() {
        return (this.manager != null) ? manager.getNationalIdentifier() : null;
    }

    @JsonGetter
    public String getManagerImageP() {
        return (this.manager != null) ? manager.getImageProfile() : null;
    }

    @JsonGetter
    public String getManagerImageNI() {
        return (this.manager != null) ? manager.getImageNationalIdentifier() : null;
    }

    @JsonGetter
    public String getContractTypeName() {
        TypeEnumeration ct = TypeEnumeration.get(this.contractType);
        return (ct != null) ? ct.getFa() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setAreaCityId(long id) {
        if (id > 0) {
            this.areaCity = new Area(id);
        }
    }

    @JsonSetter
    public void setParentId(long id) {
        if (id > 0) {
            setParent();
            this.parent.setId(id);
        }
    }

    @JsonSetter
    public void setParentSerial(String parentSerial) {
        if(!Utility.isEmpty(parentSerial)) {
            setParent();
            this.parent.setSerial(parentSerial);
        }
    }

    @JsonSetter
    public void setManagerId(long id) {
        if (id > 0) {
            setManager();
            this.manager.setId(id);
        }
    }

    @JsonSetter
    public void setManagerNI(String nationalIdentifier) {
        if (!Utility.isEmpty(nationalIdentifier)) {
            setManager();
            this.manager.setNationalIdentifier(nationalIdentifier);
        }
    }

    @JsonSetter
    public void setManagerImageP(String imageProfile) {
        setManager();
        this.manager.setImageProfile(imageProfile);
    }

    @JsonSetter
    public void setManagerImageNI(String imageNationalIdentifier) {
        setManager();
        this.manager.setImageNationalIdentifier(imageNationalIdentifier);
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonIgnore
    private void setManager() {
        if (this.manager == null) {
            this.manager = new Customer();
        }
    }

    @JsonIgnore
    private void setParent() {
        if (this.parent == null) {
            this.parent = new Distributor();
        }
    }

    @JsonIgnore
    public void setKey() {
        this.serial = Utility.randomNumber(Config.DISTRIBUTOR_SERIAL_LEN) + "";
                //+ "-" + Utility.randomNumber(2 * Config.DISTRIBUTOR_SERIAL_LEN);
    }
    //-----------------------------------------------------------------------------------------------------------
    public static TypeEnumeration getTaskType(int type, boolean update) {
        TypeEnumeration taskType = TypeEnumeration.TASK_TYPE;
        switch (TypeEnumeration.get(type)){
            case DT_MAIN:
                if(update) {
                    taskType = TypeEnumeration.TT_UPDATE_DISTRIBUTOR_MAIN;
                } else {
                    taskType = TypeEnumeration.TT_INSERT_DISTRIBUTOR_MAIN;
                }
                break;
            case DT_MAIN_BRANCH:
                if(update) {
                    taskType = TypeEnumeration.TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH;
                } else {
                    taskType = TypeEnumeration.TT_INSERT_DISTRIBUTOR_MAIN_BRANCH;
                }
                break;
        }
        return taskType;
    }
    //-----------------------------------------------------------------------------------------------------------
}