package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.APICode;

public class ControlPanelActivity extends ControlPanelBaseActivity implements ActivityServiceListener{

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
        return ActivityKeys.ControlPanel;
    }

    @Override
    public String getActivityTitle() {
        String tt = "پنل کاربری " + Authenticator.loadAccount().getName();
        return tt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_control_panel);

        LinearLayout llChangePass = (LinearLayout)findViewById(R.id.cpa_ll_change_pass);
        llChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePassIntent = new Intent(ControlPanelActivity.this, ChangePasswordActivity.class);
                startActivity(changePassIntent);
                finish();
            }
        });

//        Profile p = Authenticator.loadAccount();
//        if(p.getRoleId() == 1011 && p.getInCartable() == false){
//            // show registration activity
//            Intent registrationIntent = new Intent(this, CommonRegistrationActivity.class);
//            startActivity(registrationIntent);
//        }
//        else if(p.getRoleId() == 1011 && p.getInCartable() == true){
//            // show waiting for admin activity
//            Intent waitForAdminConfirmationIntent = new Intent(this, WaitForAdminConfirmationActivity.class);
//            startActivity(waitForAdminConfirmationIntent);
//        }
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
