package ir.joorjens.joorapp.activities;

import android.os.Bundle;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.APICode;

public class WaitForAdminConfirmationActivity extends ControlPanelBaseActivity implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        _isActive = true;
    }
    @Override
    public void onStop() {
        _isActive = false;
        super.onStop();
    }
    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.WaitForAdminConfirmation;
    }

    @Override
    public String getActivityTitle() {
        return "پنل کاربری";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_wait_for_admin_confirmation);

        Profile profile = Authenticator.loadAccount();
        String userFullName = "خطا";
        if( profile != null ){
            userFullName = profile.getName();
        }

        TextViewPlus mTvWFACMsg = (TextViewPlus)findViewById(R.id.tv_wfac_msg);
        mTvWFACMsg.setText(getResources().getString(R.string.label_dear_user) + " " +
                userFullName + " " + "اطلاعات شما در سیستم ذخیره شده و منتظر تایید مدیر سیستم میباشد.");
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onServiceSuccess(apiCode, data, requestCode);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onServiceFail(apiCode, data, requestCode);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onNetworkFail(apiCode, data, requestCode);
        }
    }
}
