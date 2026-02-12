package ir.joorjens.joorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.SignUpParams;

public class SignupActivity extends AppCompatActivity implements ActivityServiceListener {

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

    private static final String TAG = "SignupActivity";
    private SweetAlertDialog mProgressDialog = null;

    @BindView(R.id.input_signup_mobile)  EditText _mobileText;
    @BindView(R.id.input_signup_password) EditText _passwordText;
    @BindView(R.id.input_signup_password_repeat) EditText _passwordRepeatText;
    @BindView(R.id.input_signup_first_name) EditText _firstNameText;
    @BindView(R.id.input_signup_last_name) EditText _lastNameText;
    @BindView(R.id.input_signup_email) EditText _emailNameText;
    @BindView(R.id.btn_signup_create) Button _signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.signup_a_main), SignupActivity.this);

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        SignUpParams signUpParams = new SignUpParams(
                _mobileText.getText().toString(),
                _passwordText.getText().toString(),
                _passwordRepeatText.getText().toString(),
                _firstNameText.getText().toString(),
                _lastNameText.getText().toString(),
                _emailNameText.getText().toString());
        UserAPIs.signUp(this,signUpParams, 0);

        mProgressDialog = StaticHelperFunctions.showLoadingDialog(this, getResources().getString(R.string.running_query));
        Profile profile = new Profile();
        profile.setMobileNumber( _mobileText.getText().toString() );
        profile.setName( _firstNameText.getText().toString() + " " + _lastNameText.getText().toString() );
        Authenticator.saveAccount(profile, "");


    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        _signupButton.setEnabled(true);

        int activationCode = StaticHelperFunctions.getOnlyNumeric(((ServiceResponse)data).getMessage());
        Intent myIntent = new Intent(this, AccountActivationActivity.class);
        myIntent.putExtra("mobileNumber", _mobileText.getText().toString());
        myIntent.putExtra("activationCode", activationCode);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        _signupButton.setEnabled(true);
        StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        _signupButton.setEnabled(true);
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
