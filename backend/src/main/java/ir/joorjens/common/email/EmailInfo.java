package ir.joorjens.common.email;

import ir.joorjens.common.Utility;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Khalil Alijani Mamaqani
 * @version 1.0.0
 * @since 30/6/2012
 */
public class EmailInfo {

    private String userName, password, email;
    private Map<String, String> properties = new HashMap<>();

    public EmailInfo() {
    }

    public EmailInfo(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public boolean isValid() {
        return Utility.validateEmail(this.email) && this.properties.size() > 0
                && !Utility.isEmpty(this.userName) && !Utility.isEmpty(this.password);
    }

    //------------------------------------------------------------------------------------------------------------------
    private Session session = null;

    public Session getSession() {
        return session;
    }

    public synchronized void setSession() {
        Properties props = new Properties();
        props.putAll(properties);
        this.session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------------
}