package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_manager"}, name = "UK_STORE__manager")})
public class Store extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "سوپرمارکت";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    private String telephone;
    private String businessLicense;
    private String imageBusinessLicense;
    @Column(length = 10)
    private String postalCode;
    private String note;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_area_zone", foreignKey = @ForeignKey(name = "FK_STORE__AREA_areaZone"))
    @JsonIgnore
    private Area areaZone;
    private String avenueMain;
    private String avenueAuxiliary;
    private String addressRemnant;
    private String plaque;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_manager", foreignKey = @ForeignKey(name = "FK_STORE__CUSTOMER__manager"))
    @JsonIgnore
    private Customer manager;
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "store__activity_Type",
            joinColumns = @JoinColumn(name = "id_store", foreignKey = @ForeignKey(name = "FK_STORE__PCATEGORY_TYPE__store")),
            inverseJoinColumns = @JoinColumn(name = "id_productCategoryType", foreignKey = @ForeignKey(name = "FK_STORE__PCATEGORY_TYPE__productCategoryType")))
    private Set<ProductCategoryType> activityTypes = new HashSet<>();
    //------------------------------------------------

    public Store() {
    }

    public Store(long id) {
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getImageBusinessLicense() {
        return imageBusinessLicense;
    }

    public void setImageBusinessLicense(String imageBusinessLicense) {
        this.imageBusinessLicense = imageBusinessLicense;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Set<ProductCategoryType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ProductCategoryType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Area getAreaZone() {
        return areaZone;
    }

    public void setAreaZone(Area areaZone) {
        this.areaZone = areaZone;
    }

    public String getAvenueMain() {
        return avenueMain;
    }

    public void setAvenueMain(String avenueMain) {
        this.avenueMain = avenueMain;
    }

    public String getAvenueAuxiliary() {
        return avenueAuxiliary;
    }

    public void setAvenueAuxiliary(String avenueAuxiliary) {
        this.avenueAuxiliary = avenueAuxiliary;
    }

    public String getAddressRemnant() {
        return addressRemnant;
    }

    public void setAddressRemnant(String addressRemnant) {
        this.addressRemnant = addressRemnant;
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public Customer getManager() {
        return manager;
    }

    public void setManager(Customer manager) {
        this.manager = manager;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(this.name) && this.areaZone != null && this.manager != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getAddressFull() {
        final StringBuilder address = new StringBuilder();
        boolean added = false;
        if(areaZone != null) {
            address.append(areaZone.getParentName()).append(Config.ADDRESS_SPLITTER).append(areaZone.getName());
            added = true;
        }
        if(avenueMain != null) {
            if (added) {
                address.append(Config.ADDRESS_SPLITTER);
            }
            address.append(avenueMain);
            added = true;
        }
        if(avenueAuxiliary != null) {
            if (added) {
                address.append(Config.ADDRESS_SPLITTER);
            }
            address.append(avenueAuxiliary);
            added = true;
        }
        if(addressRemnant != null) {
            if (added) {
                address.append(Config.ADDRESS_SPLITTER);
            }
            address.append(addressRemnant);
            added = true;
        }
        if(plaque != null) {
            if (added) {
                address.append(Config.ADDRESS_SPLITTER);
            }
            address.append("پلاک ").append(plaque);
            added = true;
        }
        return added ? address.toString() : null;
    }

    @JsonGetter
    public long getAreaZoneId() {
        return (this.areaZone != null) ? areaZone.getId() : 0;
    }

    @JsonGetter
    public String getAreaZoneName() {
        return (this.areaZone != null) ? areaZone.getName() : null;
    }

    @JsonGetter
    public long getAreaCityId() {
        return (this.areaZone != null) ? areaZone.getParentId() : 0;
    }

    @JsonGetter
    public String getAreaCityName() {
        return (this.areaZone != null) ? areaZone.getParentName() : null;
    }

    @JsonGetter
    public long getAreaProId() {
        return (this.areaZone != null && this.areaZone.getParent() != null) ? areaZone.getParent().getParentId() : 0;
    }

    @JsonGetter
    public String getAreaProName() {
        return (this.areaZone != null && this.areaZone.getParent() != null) ? areaZone.getParent().getParentName() : null;
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
    public int getManagerGT() {
        return (this.manager != null) ? manager.getGenderType() : 0;
    }

    @JsonGetter
    public String getManagerGTName() {
        return (this.manager != null) ? manager.getGenderTypeName() : null;
    }

    @JsonGetter
    public String getManagerImageP() {
        return (this.manager != null) ? manager.getImageProfile() : null;
    }

    @JsonGetter
    public String getManagerImageBC() {
        return (this.manager != null) ? manager.getImageBirthCertificate() : null;
    }

    @JsonGetter
    public String getManagerImageNI() {
        return (this.manager != null) ? manager.getImageNationalIdentifier() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setAreaZoneId(long id) {
        if (id > 0) {
            this.areaZone = new Area(id);
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
        if(!Utility.isEmpty(nationalIdentifier)) {
            setManager();
            this.manager.setNationalIdentifier(nationalIdentifier);
        }
    }

    @JsonSetter
    public void setManagerGT(int genderType) {
        setManager();
        this.manager.setGenderType(genderType);
    }

    @JsonSetter
    public void setManagerImageP(String imageProfile) {
        setManager();
        this.manager.setImageProfile(imageProfile);
    }

    @JsonSetter
    public void setManagerImageBC(String imageBirthCertificate) {
        setManager();
        this.manager.setImageBirthCertificate(imageBirthCertificate);
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
    //-----------------------------------------------------------------------------------------------------------
}