package ir.joorjens.joorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.ChangePasswordParams;

public class ChangePasswordActivity extends ControlPanelBaseActivity implements ActivityServiceListener{

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

    //ProgressDialog progressDialog;
    SweetAlertDialog mDialog;
    EditTextPlus mEtPassword, mEtNewPassword, getmEtNewPasswordRep;
    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.changePassword;
    }

    @Override
    public String getActivityTitle() {
        String tt = "پنل کاربری " + Authenticator.loadAccount().getName() + " / تغییر رمز عبور";
        return tt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_change_password);

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.chpa_ll_main), ChangePasswordActivity.this);

        ButtonPlus btnChangePass = (ButtonPlus) findViewById(R.id.chpa_btn_change_password);
        mEtPassword = (EditTextPlus) findViewById(R.id.chpa_et_current_pass);
        mEtNewPassword = (EditTextPlus) findViewById(R.id.chpa_et_new_pass);
        getmEtNewPasswordRep = (EditTextPlus) findViewById(R.id.chpa_et_new_pass_rep);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = StaticHelperFunctions.showLoadingDialog(ChangePasswordActivity.this
                        , "در حال انجام عملیات ...");
//                progressDialog = new ProgressDialog(ChangePasswordActivity.this,
//                        ProgressDialog.STYLE_SPINNER);
//                progressDialog.setMessage("در حال انجام عملیات ...");
//                progressDialog.show();

                ChangePasswordParams params = new ChangePasswordParams(mEtPassword.getText().toString(),
                        mEtNewPassword.getText().toString(), getmEtNewPasswordRep.getText().toString());
                UserAPIs.changePassword(ChangePasswordActivity.this, Authenticator.loadAuthenticationToken(), params, 0);
            }
        });
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        mDialog.dismissWithAnimation();
        //progressDialog.dismiss();
        if(apiCode == APICode.changePassword) {
            StaticHelperFunctions.showConfirmDialog (this, "رمز با موفقیت تغییر یافت", null);
            UserAPIs.logout(this, Authenticator.loadAuthenticationToken(), 0);
        }
        else if(apiCode == APICode.logout){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            Authenticator.cleanAccount();
            loginIntent.putExtra("logout", true);
            startActivity(loginIntent);
            finish();
        }

    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mDialog.dismissWithAnimation();
//        progressDialog.dismiss();
        if(apiCode == APICode.changePassword) {
            StaticHelperFunctions.showMessage(this, ((ServiceResponse) data).getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
        else if(apiCode == APICode.logout){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            Authenticator.cleanAccount();
            loginIntent.putExtra("logout", true);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mDialog.dismissWithAnimation();
//        progressDialog.dismiss();
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
