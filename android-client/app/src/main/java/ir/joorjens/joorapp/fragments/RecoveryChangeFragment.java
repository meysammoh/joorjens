package ir.joorjens.joorapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.LoginActivity;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.webService.params.ForgetPasswordVerifyParams;

public class RecoveryChangeFragment extends Fragment  implements ActivityServiceListener{

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

    @BindView(R.id.btn_change_password)Button mButtonChangePassword;
    @BindView(R.id.input_recovery_password)TextView mInputPassword;
    @BindView(R.id.input_recovery_pass_repeat)TextView mInputPasswordRepeat;
    @BindView(R.id.input_recovery_code)TextView mInputCode;
    private String mobileNumber;
    ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recovery_change, container, false);
        ButterKnife.bind(this, v);
        mButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();

            }
        });
        mobileNumber = getArguments().getString("mobile");
        mInputCode.setText(getArguments().getString("resetPasswordCode"));
        return v;
    }

    private void changePassword(){
        mProgressDialog = new ProgressDialog(getActivity(),
                ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getResources().getString(R.string.running_query));
        mProgressDialog.show();

        ForgetPasswordVerifyParams params = new ForgetPasswordVerifyParams(
                 mobileNumber,
                Integer.valueOf(mInputCode.getText().toString()),
                mInputPassword.getText().toString(),
                mInputPasswordRepeat.getText().toString());
        UserAPIs.forgetPasswordVerify(this, params, 0);
    }

    public static RecoveryChangeFragment newInstance(String text) {

        RecoveryChangeFragment f = new RecoveryChangeFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        // goto login activity
        mProgressDialog.dismiss();
        StaticHelperFunctions.showMessage(getActivity(),  "رمز با موفقیت تغییر یافت", StaticHelperFunctions.MessageType.Confirm);
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();
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
