package ir.joorjens.joorapp.activities;

import android.os.Bundle;
import android.view.View;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.FreakyInputBox;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.StoreAPIs;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.ChangePasswordParams;

public class DeliveryPrivateInfoActivity extends DelivererBaseActivity {

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

    private ImageViewPlus _imgPiAvatar;
    private TextViewPlus _tvPiName;

    private FreakyInputBox _fibPersonalId;
    private FreakyInputBox _fibMobileNumber;
    private FreakyInputBox _fibEmail;

    private FreakyInputBox _fibVehicleType;
    private FreakyInputBox _fibVehiclePlaque;

    private FreakyInputBox _fibCurrentPassword;
    private FreakyInputBox _fibNewPassword;
    private FreakyInputBox _fibNewPasswordRep;

    private ButtonPlus _btnApplyChanges;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_delivery_private_info);

        _imgPiAvatar = (ImageViewPlus) findViewById(R.id.del_img_pi_avatar);
        _tvPiName = (TextViewPlus) findViewById(R.id.del_tv_pi_name);

        _fibPersonalId = (FreakyInputBox) findViewById(R.id.del_pi_fib_personal_id);
        _fibMobileNumber = (FreakyInputBox) findViewById(R.id.del_pi_fib_mobile_number);
        _fibEmail = (FreakyInputBox) findViewById(R.id.del_pi_fib_email);

        _fibVehicleType = (FreakyInputBox) findViewById(R.id.del_pi_fib_vehicle_type);
        _fibVehiclePlaque = (FreakyInputBox) findViewById(R.id.del_pi_fib_vehicle_plaque);

        _fibCurrentPassword = (FreakyInputBox) findViewById(R.id.pi_fib_current_password);
        _fibNewPassword = (FreakyInputBox) findViewById(R.id.pi_fib_new_password);
        _fibNewPasswordRep = (FreakyInputBox) findViewById(R.id.pi_fib_new_password_rep);

        _btnApplyChanges = (ButtonPlus) findViewById(R.id.del_pi_btn_apply_changes);
        _btnApplyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordParams params = new ChangePasswordParams(_fibCurrentPassword.getValue(),
                        _fibNewPassword.getValue(), _fibNewPasswordRep.getValue());
                UserAPIs.changePassword(DeliveryPrivateInfoActivity.this, Authenticator.loadAuthenticationToken(), params, 0);
            }
        });

        Profile p = Authenticator.loadAccount();
        //StoreAPIs.viewStore(this, Authenticator.loadAuthenticationToken(), p.getId(), 0);

        _tvPiName.setText(p.getName()+"");
        _imgPiAvatar.setImage(p.getImageProfile());

        _fibPersonalId.setValue(p.getNationalIdentifier()+"");
        _fibMobileNumber.setValue(p.getMobileNumber()+"");
        _fibEmail.setValue("????");
    }

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.PrivateInfo;
    }

    @Override
    public String getActivityTitle() {
        return "اطلاعات شخصی" ;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.viewStore){
//            Store store = (Store)data;
//            // set values_fibPersonalId.setValue();
//            _fibStoreName.setValue(store.getName()+"");
//            _fibStoreAddress.setValue(store.getAreaCityName()+"");// TODO total address
//            _fibStorePhone.setValue(store.getTelephone()+"");
//            _fibStoreLicence.setValue(store.getBusinessLicense()+"");
        }
        else if(apiCode == APICode.changePassword){
            StaticHelperFunctions.showMessage(this, "رمز با موفقیت تغییر کرد", StaticHelperFunctions.MessageType.Confirm);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
