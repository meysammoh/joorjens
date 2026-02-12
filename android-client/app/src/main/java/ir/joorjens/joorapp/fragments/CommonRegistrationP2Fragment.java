package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.RadioButtonPlus;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

public class CommonRegistrationP2Fragment extends Fragment {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @BindView(R.id.btn_crp2_continue) ButtonPlus mBtnCRP2Continue;
    @BindView(R.id.rg_pdr_filter) RadioGroup mRgCRP2Role;
    @BindView(R.id.rd_crp2_role_store) RadioButtonPlus mRdCRP2Store;
    @BindView(R.id.rd_crp2_role_delivery) RadioButtonPlus mRdCRP2Delivery;
    //@BindView(R.id.rd_crp2_role_producer) RadioButtonPlus mRdCRP2Producer;
    //@BindView(R.id.rd_crp2_role_distributor) RadioButtonPlus mRdCRP2Distributor;
    //@BindView(R.id.rd_crp2_role_store_backup) RadioButtonPlus mRdCRP2StoreBackup;



    public CommonRegistrationP2Fragment() {
    }

    private void replaceFragment(){
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        StoreRegistrationP1Fragment nextFragment = StoreRegistrationP1Fragment.newInstance();

        mFragmentTransaction.hide(CommonRegistrationP2Fragment.this);
        mFragmentTransaction.addToBackStack("StoreRegistrationP1Fragment");
        mFragmentTransaction.replace(R.id.common_registration_container, nextFragment).commit();

        //((CommonRegistrationActivity)getActivity()).scrollToUp();
    }

    public static CommonRegistrationP2Fragment newInstance(){
        CommonRegistrationP2Fragment fragment = new CommonRegistrationP2Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_common_registration_p2, container, false);
        ButterKnife.bind(this, v);

        mRdCRP2Store.setTypeId(1);
        mRdCRP2Delivery.setTypeId(0);
        //mRdCRP2Distributor.setTypeId(0);
        //mRdCRP2Producer.setTypeId(0);
        //mRdCRP2StoreBackup.setTypeId(0);

        mBtnCRP2Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = mRgCRP2Role.getCheckedRadioButtonId();
                if(id > 0) {
                    RadioButtonPlus selectedRd = (RadioButtonPlus) getActivity().findViewById(id);
                    if (selectedRd.getTypeId() == 1) {
                        replaceFragment();
                    } else {
                        StaticHelperFunctions.showMessage(getActivity(), "تنها نوع فروشگاه در موبایل قابل استفاده است", StaticHelperFunctions.MessageType.Alert);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonRegistrationActivity.setSwipeEnabled(false);
        CommonRegistrationActivity.setOnRefreshListener(null);
    }
}
