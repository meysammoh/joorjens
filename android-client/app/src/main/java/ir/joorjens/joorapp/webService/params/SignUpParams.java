package ir.joorjens.joorapp.webService.params;

/**
 * Created by Mohsen on 8/4/2017.
 */

public class SignUpParams {
    private String mobileNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordRepeat;
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public SignUpParams setEmail(String email) {
        this.email = email;
        return this;
    }



    public SignUpParams(String mobileNumber, String password,String password_repeat,
                        String firstName, String lastName, String email) {
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordRepeat = password_repeat;
    }
}
