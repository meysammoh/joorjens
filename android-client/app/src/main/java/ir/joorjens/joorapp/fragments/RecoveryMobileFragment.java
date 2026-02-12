package ir.joorjens.joorapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.RecoverPasswordActivity;
import ir.joorjens.joorapp.activities.SignupActivity;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;

public class RecoveryMobileFragment extends Fragment implements ActivityServiceListener{

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

    @BindView(R.id.btn_recover_mobile)
    Button _ButtonRecoverMobile;

    @BindView(R.id.input_recovery_mobile)
    EditText _Input_recovery_mobile;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recovery_mobile, container, false);
        ButterKnife.bind(this, v);
        _ButtonRecoverMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode();
            }
        });
        return v;
    }

    private void requestCode(){
        mProgressDialog = new ProgressDialog(getActivity(),
                ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getResources().getString(R.string.running_query));
        mProgressDialog.show();
        //Request for code
        UserAPIs.forgetPassword(this, _Input_recovery_mobile.getText().toString(), 0);
    }
    private void replaceFragment(String resetPasswordCode){
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        RecoveryChangeFragment fragment2 = RecoveryChangeFragment.newInstance("");
        Bundle args = new Bundle();
        args.putString("mobile", _Input_recovery_mobile.getText().toString());
        args.putString("resetPasswordCode", resetPasswordCode);
        fragment2.setArguments(args);
        mFragmentTransaction.addToBackStack("xyz");
        mFragmentTransaction.hide(RecoveryMobileFragment.this);
        mFragmentTransaction.add(R.id.recovery_fragment_container, fragment2);
        mFragmentTransaction.commit();
    }
    public static RecoveryMobileFragment newInstance(String text) {

        RecoveryMobileFragment f = new RecoveryMobileFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismiss();
        String resetPasswordCode = String.valueOf(
                StaticHelperFunctions.getOnlyNumeric(((ServiceResponse)data).getMessage()));
        replaceFragment(resetPasswordCode);
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismiss();
        ServiceResponse errorResp = (ServiceResponse)data;
        StaticHelperFunctions.showMessage(getActivity(), errorResp.getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismiss();
        Throwable errorResp = (Throwable)data;
        StaticHelperFunctions.showMessage(getActivity(), errorResp.getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
