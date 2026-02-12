package ir.joorjens.joorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.ProductAPIs;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.LoginParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ActivityServiceListener{

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

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private SweetAlertDialog mLoadingDialog = null;
    //private ProgressDialog progressDialog  = null;
    private String authToken;

    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup)  TextView _signupLink;
    @BindView(R.id.link_sp_recover_password)  TextView _recoverPassLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if( getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("logout")) {
                boolean isLogout = getIntent().getExtras().getBoolean("logout");
                if (isLogout) {
                    StaticHelperFunctions.showMessage(this, "شما با موفقیت از سیستم خارج شدید", StaticHelperFunctions.MessageType.Confirm);
                }
            }
        }

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.la_main), LoginActivity.this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignUp activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _recoverPassLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recoverPassword();

            }
        });

    }

    private void recoverPassword(){
        Intent myIntent = new Intent(this, RecoverPasswordActivity.class);
        startActivity(myIntent);
    }
    public void login() {
        Log.d(TAG, "Login");
        _loginButton.setEnabled(false);

        mLoadingDialog = StaticHelperFunctions.showLoadingDialog(this, "در حال اعتبار سنجی ...");
//        progressDialog = new ProgressDialog(LoginActivity.this,
//                ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage("در حال اعتبار سنجی ...");
//        progressDialog.show();

        LoginParams lp = new LoginParams(_mobileText.getText().toString(),_passwordText.getText().toString());
        UserAPIs.login(this, lp, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.login){
            Response<ServiceResponse> response = (Response<ServiceResponse>)data;
            ServiceResponse sresp = response.body();
            authToken = response.headers().get("Set-Cookie");
            Authenticator.saveAccount(null, authToken);

            //get profile
            UserAPIs.getProfile(this, authToken, 0);
        }
        else if(apiCode == APICode.getProfile){
            //progressDialog.dismiss();
            mLoadingDialog.dismissWithAnimation();
            Profile p = (Profile) data;
            Authenticator.saveAccount(p, authToken);

            // open start activity
            Intent myIntent = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(myIntent);
            finish();
        }
        else if(apiCode == APICode.resendActivationCode){
            int activationCode = StaticHelperFunctions.getOnlyNumeric(((ServiceResponse)data).getMessage());
            Intent myIntent = new Intent(this, AccountActivationActivity.class);
            myIntent.putExtra("mobileNumber", _mobileText.getText().toString());
            myIntent.putExtra("activationCode", activationCode);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mLoadingDialog.dismissWithAnimation();
//        progressDialog.dismiss();
        ServiceResponse resp = (ServiceResponse)(data);
        _loginButton.setEnabled(true);
        if(resp.getCode() == 3005){

            StaticHelperFunctions.showMessage(this, resp.getMessage(), StaticHelperFunctions.MessageType.Alert);
            UserAPIs.resendActivationCode(this, _mobileText.getText().toString(), 0);
        }
        else {
            StaticHelperFunctions.showMessage(this, resp.getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mLoadingDialog.dismissWithAnimation();
//        progressDialog.dismiss();
        Throwable t = (Throwable)data;
        StaticHelperFunctions.showMessage(this, t.getMessage(), StaticHelperFunctions.MessageType.Alert);
        _loginButton.setEnabled(true);
    }
}