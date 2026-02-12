package ir.joorjens.joorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//import cn.pedant.SweetAlert.SweetAlertDialog;

import ir.joorjens.joorapp.R;

import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.TimerButton;
import ir.joorjens.joorapp.models.*;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.ActivationParams;

public class AccountActivationActivity extends AppCompatActivity implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;
    }
    @Override
    protected void onStop() {
        _isActive = false;
        super.onStop();
    }
    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    @BindView(R.id.input_activation_code) EditText mActivationText;
    @BindView(R.id.label_activation_msg) TextViewPlus mMsgLabel;
    @BindView(R.id.btn_activate_account) Button mActivateButton;
    @BindView(R.id.btn_resend_code) TimerButton mActivateResendButton;
    private static final String TAG = "ActivateActivity";
    private static final int activateId = 0;
    private static final int resendActivationCodeId = 1;
    private String mobileNumber = "";
    //private ProgressDialog progressDialog  = null;
    private SweetAlertDialog mProgressDialog = null;


    // must delete just for activation code
    private int activationCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activation);
        ButterKnife.bind(this);

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.aaa_main), AccountActivationActivity.this);

        mobileNumber = getIntent().getExtras().getString("mobileNumber");

        // must delete just for activation code
        activationCode = getIntent().getExtras().getInt("activationCode");
        mActivationText.setText(String.valueOf(activationCode));

        mActivateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activate();
            }
        });
        mActivateResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendActivationCode();
            }
        });
        mActivateResendButton.setmTimerDuration(60);
        mActivateResendButton.startDownCounter();
        Profile profile = Authenticator.loadAccount();
        String userFullName = "خطا";
        if( profile != null ){
            userFullName = profile.getName();
        }
        mMsgLabel.setText(getResources().getString(R.string.label_dear_user)+
                " "+ userFullName +" "+
                getResources().getString(R.string.label_activation_msg));
    }

    public void activate() {
        Log.d(TAG, "Activate");
        mProgressDialog = StaticHelperFunctions.showLoadingDialog(this, getResources().getString(R.string.running_query));

        try {
            ActivationParams activationParams = new ActivationParams(mobileNumber,
                    Integer.valueOf(mActivationText.getText().toString()));
            UserAPIs.activate(this, activationParams, activateId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            StaticHelperFunctions.showMessage(this, "کد فعال سازی فقط باید عددی باشد", StaticHelperFunctions.MessageType.Alert);
        }
    }

    public void resendActivationCode() {
        Log.d(TAG, "Resend");
        mActivateResendButton.startDownCounter();
        UserAPIs.resendActivationCode(this, mobileNumber, resendActivationCodeId);
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.activate) {
            Profile profile = (Profile)data;
            Authenticator.saveAccount(profile, "");

            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            finish();
        }
        else if(apiCode == APICode.resendActivationCode){
            ServiceResponse response = (ServiceResponse)data;
            StaticHelperFunctions.showMessage(this,response.getMessage(), StaticHelperFunctions.MessageType.Confirm);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        if(apiCode == APICode.activate) {
            ServiceResponse responseActivate = (ServiceResponse) data;
            StaticHelperFunctions.showMessage(this,responseActivate.getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
         else if (apiCode == APICode.resendActivationCode){
            ServiceResponse responseResendAvtivate = (ServiceResponse)data;
            StaticHelperFunctions.showMessage(this,responseResendAvtivate.getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        if(apiCode == APICode.activate) {
            Throwable tActivate = (Throwable) data;
            StaticHelperFunctions.showMessage(this,tActivate.getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
        else if (apiCode == APICode.resendActivationCode){
            Throwable tResendActivate = (Throwable)data;
            StaticHelperFunctions.showMessage(this,tResendActivate.getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }
}
