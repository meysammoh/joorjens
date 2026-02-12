package ir.joorjens.joorapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;

public class StoreRegistrationP4Fragment extends Fragment {

    @BindView(R.id.tv_srp4_msg) TextViewPlus mTvSRP4Msg;
    ButtonPlus mBtnExit;
    Activity mContext;

    public StoreRegistrationP4Fragment() {
    }

    public static StoreRegistrationP4Fragment newInstance(){
        StoreRegistrationP4Fragment fragment = new StoreRegistrationP4Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store_registration_p4, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, v);
        mBtnExit = (ButtonPlus) v.findViewById(R.id.btn_srp4_exit);
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        Profile profile = Authenticator.loadAccount();
        String userFullName = "خطا";
        if( profile != null ){
            userFullName = profile.getName();
        }
        mTvSRP4Msg.setText(getResources().getString(R.string.label_dear_user) + " " +
        userFullName + " " + getResources().getString(R.string.srp4_complete_msg));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonRegistrationActivity.setOnRefreshListener(null);
    }
}
