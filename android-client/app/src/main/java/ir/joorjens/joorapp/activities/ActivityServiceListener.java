package ir.joorjens.joorapp.activities;

import ir.joorjens.joorapp.webService.APICode;

/**
 * Created by mohsen on 8/4/17.
 */

public interface ActivityServiceListener {
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode);
    public void onServiceFail(APICode apiCode, Object data, int requestCode);
    public void onNetworkFail(APICode apiCode, Object data, int requestCode);
    public boolean isActive();
}
