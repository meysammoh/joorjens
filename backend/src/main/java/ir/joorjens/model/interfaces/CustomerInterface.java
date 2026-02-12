package ir.joorjens.model.interfaces;

import java.util.UUID;

public interface CustomerInterface {

    long getId();

    void setId(long id);

    String getMobileNumber();

    void setMobileNumber(String mobileNumber);

    String getNationalIdentifier();

    void setNationalIdentifier(String nationalIdentifier);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getFatherName();

    void setFatherName(String fatherName);

    int getGenderType();

    void setGenderType(int genderType);

    int getBirthTime();

    void setBirthTime(int birthTime);

    String getImageProfile();

    void setImageProfile(String imageProfile);

    String getEmail();

    void setEmail(String email);

    //---------------------------------------------

    boolean isBanded();

    void setBanded(boolean banded);

    String getAccountNumber();

    void setAccountNumber(String accountNumber);

    int getCredit();

    void setCredit(int credit);

    String getLastLoginIp();

    int getLastLoginTime();

    UUID getUuid();

    void setUuid(UUID uuid);

    //---------------------------------------------

    String getName();
}