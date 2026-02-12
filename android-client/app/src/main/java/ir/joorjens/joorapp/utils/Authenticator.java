package ir.joorjens.joorapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.models.*;

/**
 * Created by Mohsen on 8/4/2017.
 */

public class Authenticator {

    private enum userSettingKeys{
        account,
        authenticationToken
    }

    private static SharedPreferences settings;
    private static SharedPreferences.Editor settingsEditor;
    private static final String USERPREFNAME = "userSettings";

    @Nullable
    public static Profile loadAccount(){
        try {
            if(settings == null){
                Initialize(JJApp.getAppContext());
            }
            String customerJsonString = settings.getString(userSettingKeys.account.toString(), "nf");
            if (customerJsonString != "nf") {
                // found so convert to json
                Gson gson = new Gson();
                Profile profile = null;
                profile = gson.fromJson(customerJsonString, Profile.class);
                int h = 0;
                return profile;
            } else {
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }
    public static String loadAuthenticationToken(){
        String authKey = "";
        authKey = settings.getString(userSettingKeys.authenticationToken.toString(), "nf");
        if(authKey != "nf"){
            // exists
            return authKey;
        }
        else {
            return "";
        }
    }

    public static void Initialize(Context context){
        settings = context.getSharedPreferences(USERPREFNAME, Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
    }

    public static boolean hasRegisterdUser(){
//        Customer customer = loadAccount();
//        if(customer != null) {
//            return true;
//        }
//        return false;
        return loadAuthenticationToken() == "" ? false : true;
    }

    public static boolean saveAccount(Profile profile, String authTocken) {
        if(settingsEditor == null){
            Initialize(JJApp.getAppContext());
        }
        if(profile != null) {
            Gson gson = new Gson();
            String userJson = gson.toJson(profile);
            settingsEditor.remove(userSettingKeys.account.toString());
            settingsEditor.putString(userSettingKeys.account.toString(), userJson);
        }
        settingsEditor.remove(userSettingKeys.authenticationToken.toString());
        settingsEditor.putString(userSettingKeys.authenticationToken.toString() , authTocken);
        settingsEditor.commit();
        return true;
    }

    public static boolean cleanAccount(){
        settingsEditor.clear();
        settingsEditor.commit();
        return true;
    }

}
