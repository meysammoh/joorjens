
/**
 * Created by Mohsen on 10/7/2017.
 */

package ir.joorjens.joorapp.models;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.List;

public class Store {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("businessLicense")
    @Expose
    private String businessLicense;
    @SerializedName("imageBusinessLicense")
    @Expose
    private String imageBusinessLicense;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("activityTypes")
    @Expose
    private List<ActivityType> activityTypes = null;
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
    @SerializedName("managerImageNI")
    @Expose
    private String managerImageNI;
    @SerializedName("areaZoneId")
    @Expose
    private Integer areaZoneId;
    @SerializedName("areaZoneName")
    @Expose
    private String areaZoneName;
    @SerializedName("areaProId")
    @Expose
    private Integer areaProId;
    @SerializedName("areaProName")
    @Expose
    private String areaProName;
    @SerializedName("managerGT")
    @Expose
    private Integer managerGT;
    @SerializedName("managerGTName")
    @Expose
    private String managerGTName;
    @SerializedName("managerImageBC")
    @Expose
    private String managerImageBC;

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

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
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

    public String getManagerImageNI() {
        return managerImageNI;
    }

    public void setManagerImageNI(String managerImageNI) {
        this.managerImageNI = managerImageNI;
    }

    public Integer getAreaZoneId() {
        return areaZoneId;
    }

    public void setAreaZoneId(Integer areaZoneId) {
        this.areaZoneId = areaZoneId;
    }

    public String getAreaZoneName() {
        return areaZoneName;
    }

    public void setAreaZoneName(String areaZoneName) {
        this.areaZoneName = areaZoneName;
    }

    public Integer getAreaProId() {
        return areaProId;
    }

    public void setAreaProId(Integer areaProId) {
        this.areaProId = areaProId;
    }

    public String getAreaProName() {
        return areaProName;
    }

    public void setAreaProName(String areaProName) {
        this.areaProName = areaProName;
    }

    public Integer getManagerGT() {
        return managerGT;
    }

    public void setManagerGT(Integer managerGT) {
        this.managerGT = managerGT;
    }

    public String getManagerGTName() {
        return managerGTName;
    }

    public void setManagerGTName(String managerGTName) {
        this.managerGTName = managerGTName;
    }

    public String getManagerImageBC() {
        return managerImageBC;
    }

    public void setManagerImageBC(String managerImageBC) {
        this.managerImageBC = managerImageBC;
    }
}
