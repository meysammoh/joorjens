package ir.joorjens.joorapp.models;

/**
 * Created by Meysam on 14/04/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Distributor {

    @SerializedName("createdTime")
    @Expose
    private Integer createdTime;
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("registrationNumber")
    @Expose
    private String registrationNumber;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("imageStatute")
    @Expose
    private String imageStatute;
    @SerializedName("imageBanner")
    @Expose
    private String imageBanner;
    @SerializedName("childCount")
    @Expose
    private Integer childCount;
    @SerializedName("dailySale")
    @Expose
    private Integer dailySale;

    @SerializedName("contractType")
    @Expose
    private Integer contractType;
    @SerializedName("settlementDays")
    @Expose
    private Integer settlementDays;
    @SerializedName("settlementDaysJoorJens")
    @Expose
    private Integer settlementDaysJoorJens;
    @SerializedName("settlementTypeTimeDays")
    @Expose
    private Integer settlementTypeTimeDays;
    @SerializedName("addressRemnant")
    @Expose
    private String addressRemnant;
    @SerializedName("activityTypes")
    @Expose
    private List<ActivityType> activityTypes;
    @SerializedName("supportAreas")
    @Expose
    private List<SupportArea> supportAreas;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("areaCityId")
    @Expose
    private Integer areaCityId;
    @SerializedName("areaCityName")
    @Expose
    private String areaCityName;
    @SerializedName("managerId")
    @Expose
    private Integer managerId;
    @SerializedName("managerName")
    @Expose
    private String managerName;

    @SerializedName("managerMobile")
    @Expose
    private String managerMobile;
    @SerializedName("managerNI")
    @Expose
    private String managerNI;
    @SerializedName("managerImageP")
    @Expose
    private String managerImageP;
    @SerializedName("contractTypeName")
    @Expose
    private String contractTypeName;

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Integer getDailySale() {
        return dailySale;
    }

    public void setDailySale(Integer dailySale) {
        this.dailySale = dailySale;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Integer getSettlementDays() {
        return settlementDays;
    }

    public void setSettlementDays(Integer settlementDays) {
        this.settlementDays = settlementDays;
    }

    public Integer getSettlementDaysJoorJens() {
        return settlementDaysJoorJens;
    }

    public void setSettlementDaysJoorJens(Integer settlementDaysJoorJens) {
        this.settlementDaysJoorJens = settlementDaysJoorJens;
    }

    public Integer getSettlementTypeTimeDays() {
        return settlementTypeTimeDays;
    }

    public void setSettlementTypeTimeDays(Integer settlementTypeTimeDays) {
        this.settlementTypeTimeDays = settlementTypeTimeDays;
    }

    public String getAddressRemnant() {
        return addressRemnant;
    }

    public void setAddressRemnant(String addressRemnant) {
        this.addressRemnant = addressRemnant;
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<SupportArea> getSupportAreas() {
        return supportAreas;
    }

    public void setSupportAreas(List<SupportArea> supportAreas) {
        this.supportAreas = supportAreas;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getAreaCityId() {
        return areaCityId;
    }

    public void setAreaCityId(Integer areaCityId) {
        this.areaCityId = areaCityId;
    }

    public String getAreaCityName() {
        return areaCityName;
    }

    public void setAreaCityName(String areaCityName) {
        this.areaCityName = areaCityName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }

    public String getManagerNI() {
        return managerNI;
    }

    public void setManagerNI(String managerNI) {
        this.managerNI = managerNI;
    }

    public String getManagerImageP() {
        return managerImageP;
    }

    public void setManagerImageP(String managerImageP) {
        this.managerImageP = managerImageP;
    }

    public String getContractTypeName() {
        return contractTypeName;
    }

    public void setContractTypeName(String contractTypeName) {
        this.contractTypeName = contractTypeName;
    }
}
