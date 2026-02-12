package ir.joorjens.joorapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by ZM on 5/13/2018.
 */

public class JJApp extends Application {

    private static Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    public void onCreate() {
        super.onCreate();

        JJApp.context = getApplicationContext();

        //Sherlock.init(this);

//        Firebase.setAndroidContext(this);
//


//        Bundle bundle = new Bundle();
//        String id = "1year";
//        String name = "Annual membership subscription";
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "EUR");
//        bundle.putString(FirebaseAnalytics.Param.PRICE, "299.00");
//        getAnalyticsObject().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        try {
//            ApplicationInfo appinfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            bundle = appinfo.metaData;
//            String analytics_enabled = bundle.getString("firebase_analytics_collection_enabled");
//            Log.d("TAG", "______________________________Name: " + analytics_enabled);
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.d("TAG", "___________________________Failed to load meta-data, NameNotFound: " + e.getMessage());
//        } catch (NullPointerException e) {
//            Log.d("TAG", "___________________________Failed to load meta-data, NullPointer: " + e.getMessage());
//        }
    }

    private FirebaseAnalytics getAnalyticsObject(){
        if(mFirebaseAnalytics == null){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            mFirebaseAnalytics.setMinimumSessionDuration(20000);
        }

        return mFirebaseAnalytics;
    }

    public static Context getAppContext() {
        return JJApp.context;
    }

}
