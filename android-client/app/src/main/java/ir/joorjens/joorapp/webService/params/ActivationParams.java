package ir.joorjens.joorapp.webService.params;

/**
 * Created by Mohsen on 8/8/2017.
 */

public class ActivationParams {
    private String mobileNumber;

    public ActivationParams(String mobileNumber, int activationCode) {
        this.mobileNumber = mobileNumber;
        this.activationCode = activationCode;
    }

    private int activationCode;

    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getActivationCode() {
        return activationCode;
    }
    public void setActivationCode(int activationCode) {
        this.activationCode = activationCode;
    }


}
