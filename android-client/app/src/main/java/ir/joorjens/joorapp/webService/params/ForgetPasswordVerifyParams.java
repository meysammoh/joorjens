package ir.joorjens.joorapp.webService.params;

/**
 * Created by Mohsen on 8/18/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordVerifyParams {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("resetPasswordCode")
    @Expose
    private Integer resetPasswordCode;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("passwordRepeat")
    @Expose
    private String passwordRepeat;

    public ForgetPasswordVerifyParams(String mobileNumber, Integer resetPasswordCode, String password, String passwordRepeat) {
        this.mobileNumber = mobileNumber;
        this.resetPasswordCode = resetPasswordCode;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(Integer resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

}
