package ir.joorjens.joorapp.webService;

import ir.joorjens.joorapp.activities.ActivityServiceListener;

/**
 * Created by Mohsen on 5/18/2018.
 */

public class AdvertisingAPIs {

    public static void searchAdvertising(final ActivityServiceListener listener,
                                         String cookie,
                                         final int requestCode){
        listener.onServiceSuccess(APICode.searchAdvertising, null, requestCode);
    }
}
