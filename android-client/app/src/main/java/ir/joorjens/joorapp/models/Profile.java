package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 8/6/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Profile {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("nationalIdentifier")
    @Expose
    private String nationalIdentifier;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("genderType")
    @Expose
    private Integer genderType;
    @SerializedName("birthTime")
    @Expose
    private Integer birthTime;
    @SerializedName("imageProfile")
    @Expose
    private String imageProfile;
    @SerializedName("imageNationalIdentifier")
    @Expose
    private String imageNationalIdentifier;
    @SerializedName("imageBirthCertificate")
    @Expose
    private String imageBirthCertificate;
    @SerializedName("banded")
    @Expose
    private Boolean banded;
    @SerializedName("credit")
    @Expose
    private Integer credit;
    @SerializedName("inCartable")
    @Expose
    private Boolean inCartable;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("areaCityId")
    @Expose
    private Integer areaCityId;
    @SerializedName("areaCityName")
    @Expose
    private String areaCityName;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("lastLoginIp")
    @Expose
    private String lastLoginIp;
    @SerializedName("lastLoginTime")
    @Expose
    private Integer lastLoginTime;
    @SerializedName("lastLoginId")
    @Expose
    private Integer lastLoginId;
    @SerializedName("genderTypeName")
    @Expose
    private String genderTypeName;
    @SerializedName("areaProId")
    @Expose
    private Integer areaProId;
    @SerializedName("areaProName")
    @Expose
    private String areaProName;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNationalIdentifier() {
        return nationalIdentifier;
    }

    public void setNationalIdentifier(String nationalIdentifier) {
        this.nationalIdentifier = nationalIdentifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }

    public Integer getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Integer birthTime) {
        this.birthTime = birthTime;
    }

    public String getImageProfile() {
        return imageProfile;
    }

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

    public Boolean getBanded() {
        return banded;
    }

    public void setBanded(Boolean banded) {
        this.banded = banded;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Boolean getInCartable() {
        return inCartable;
    }

    public void setInCartable(Boolean inCartable) {
        this.inCartable = inCartable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLastLoginId() {
        return lastLoginId;
    }

    public void setLastLoginId(Integer lastLoginId) {
        this.lastLoginId = lastLoginId;
    }

    public String getGenderTypeName() {
        return genderTypeName;
    }

    public void setGenderTypeName(String genderTypeName) {
        this.genderTypeName = genderTypeName;
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
}
