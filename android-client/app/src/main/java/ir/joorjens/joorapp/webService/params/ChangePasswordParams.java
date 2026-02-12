package ir.joorjens.joorapp.webService.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 12/25/2017.
 */

public class ChangePasswordParams {
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("passwordNew")
    @Expose
    private String passwordNew;
    @SerializedName("passwordRepeat")
    @Expose
    private String passwordRepeat;

    public ChangePasswordParams(String password, String passwordNew, String passwordRepeat) {
        this.password = password;
        this.passwordNew = passwordNew;
        this.passwordRepeat = passwordRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
}
