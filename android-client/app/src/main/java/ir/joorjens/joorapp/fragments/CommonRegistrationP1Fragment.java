package ir.joorjens.joorapp.fragments;


import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;

public class CommonRegistrationP1Fragment extends Fragment {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @BindView(R.id.btn_crp1_continue) ButtonPlus mBtnCRP1Continue;

    public CommonRegistrationP1Fragment() {

    }

    public static CommonRegistrationP1Fragment newInstance(){
        CommonRegistrationP1Fragment fragment = new CommonRegistrationP1Fragment();
        return fragment;
    }

    private void replaceFragment(){
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        CommonRegistrationP2Fragment nextFragment = CommonRegistrationP2Fragment.newInstance();

        ((CommonRegistrationActivity)getActivity()).creatingStore.setName("dsfsdf");
        //mFragmentTransaction.hide(CommonRegistrationP1Fragment.this);
        mFragmentTransaction.addToBackStack("CommonRegistrationP2Fragment");
        mFragmentTransaction.replace(R.id.common_registration_container, nextFragment).commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_common_registration_p1, container, false);
        ButterKnife.bind(this, v);

        mBtnCRP1Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });


        // Inflate the layout for this fragment
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        CommonRegistrationActivity.setSwipeEnabled(false);
        CommonRegistrationActivity.setOnRefreshListener(null);
    }
}
