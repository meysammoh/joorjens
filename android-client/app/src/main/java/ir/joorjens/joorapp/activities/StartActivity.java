package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CacheContainer;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        decide();
    }

    private void decide(){
        Profile p = Authenticator.loadAccount();

        if(p.getRoleId() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Role_Customer)){
            // this user has not role and registration required
            if(p.getInCartable()){
                // user do the registration but not accepted by admin yet.
                // so show wait for admin activity
                Intent waitForAdminConfirmationIntent = new Intent(this, WaitForAdminConfirmationActivity.class);
                startActivity(waitForAdminConfirmationIntent);
                finish();
            }
            else{
                // user not registered yet. so got to registration activity
                Intent registrationIntent = new Intent(this, CommonRegistrationActivity.class);
                startActivity(registrationIntent);
                finish();
            }
        }

        else {

            if(p.getRoleId() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Role_Store_Admin) || p.getRoleId() == 1071 ){
                // open store admin app
                CacheContainer.get().addInitListener(initializeListener);
                CacheContainer.get().startUpdating();
            }

            else if ( p.getRoleId() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Role_Distribution_Deliverer) || p.getRoleId() == 1051) {
                // open store distributor deliverer order list activity
                Intent myIntent = new Intent(StartActivity.this, DeliveryOrdersActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish();
            }
            else {

                // show message and exit application
                StaticHelperFunctions.showConfirmDialog(this, getResources().getString(R.string.lbl_bad_user_try_to_login),
                        StaticHelperFunctions.MessageType.Alert, exitListener, "خروج");
                return;
            }
        }
    }

    SweetAlertDialog.OnSweetClickListener exitListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismissWithAnimation();
            finish();
        }
    };


    CacheContainer.InitializeListener initializeListener = new CacheContainer.InitializeListener() {
        @Override
        public void onInitCompleted() {
            // open home activity
            Intent myIntent = new Intent(StartActivity.this, HomeActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
            finish();
        }
    };
}
