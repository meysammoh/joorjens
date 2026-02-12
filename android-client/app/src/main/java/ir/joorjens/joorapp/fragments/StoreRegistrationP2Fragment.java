package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.models.ActivityType;
import ir.joorjens.joorapp.models.EnumValues;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CategoryAPIs;
import ir.joorjens.joorapp.webService.CommonAPIs;

public class StoreRegistrationP2Fragment extends Fragment implements ActivityServiceListener {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        _isActive = true;
        CommonRegistrationActivity.setOnRefreshListener(onRefresh);
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

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @BindView(R.id.btn_srp2_continue)  ButtonPlus mBtnSRP2Continue;
    @BindView(R.id.et_srp2_managerNI)  EditTextPlus mEtSRP2ManagerNI;
    @BindView(R.id.sp_srp2_managerGT)  SpinnerPlus<EnumValues> mSpSRP2Gender;
    @BindView(R.id.sp_srp2_activityType)  SpinnerPlus<EnumValues> mSpSRP2ActivityType;

    public StoreRegistrationP2Fragment() {
    }

    private boolean checkMandatoryFields(){
        if(TextUtils.isEmpty(mEtSRP2ManagerNI.getText().toString())){
            return false;
        }
        try {
            int gender = ((EnumValues) ( mSpSRP2Gender.getSelectedItem())).getItemId();
            //int activityType = ((PairResultItem) ( mSpSRP2ActivityType.getSelectedItem())).getItemId();
            return true;
        }catch (Exception ex){
            return false;
        }
    }


    private void loadEnums(){
        String cookie = Authenticator.loadAuthenticationToken();
        String typeIds = String.valueOf(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Gender));
        //typeIds += ",";
        //typeIds += String.valueOf(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.ActivityType));

        //CategoryAPIs.getFirstLevelCategoryPairs(this, cookie, 1);

        CommonAPIs.getEnumValue(this, typeIds, cookie, 0);
    }

    private void replaceFragment(){

        if(checkMandatoryFields() == true) {

            Store st = ((CommonRegistrationActivity) getActivity()).creatingStore;
            st.setManagerNI(mEtSRP2ManagerNI.getText().toString());
            int managerGT = ((EnumValues) (mSpSRP2Gender.getSelectedItem())).getItemId();
            st.setManagerGT(managerGT);
            //int activityType = ((PairResultItem) ( mSpSRP2ActivityType.getSelectedItem())).getItemId();
            List<ActivityType> activityTypes = new ArrayList<>();
            //activityTypes.add(activityType);
            //st.setActivityTypes(activityTypes);

            mFragmentManager = getFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            StoreRegistrationP3Fragment nextFragment = StoreRegistrationP3Fragment.newInstance();

            mFragmentTransaction.hide(StoreRegistrationP2Fragment.this);
            mFragmentTransaction.addToBackStack("StoreRegistrationP3Fragment");
            mFragmentTransaction.replace(R.id.common_registration_container, nextFragment).commit();

            //((CommonRegistrationActivity) getActivity()).scrollToUp();
        }
        else{
            StaticHelperFunctions.showMessage(getActivity(), "یکی از فیلدهای اجباری پر نشده", StaticHelperFunctions.MessageType.Alert);
        }
    }

    public static StoreRegistrationP2Fragment newInstance(){
        StoreRegistrationP2Fragment fragment = new StoreRegistrationP2Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store_registration_p2, container, false);
        ButterKnife.bind(this, v);
        mSpSRP2ActivityType.setEnabled(false);

        mBtnSRP2Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });


        loadEnums();
        return v;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.getEnumValue) {
            Map<Integer, List<EnumValues>> ec = (Map<Integer, List<EnumValues>>) data;
            List<EnumValues> evGender = ec.get(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Gender));
            mSpSRP2Gender.setAdapter(new SpinnerPlusAdapter<EnumValues>(getContext(), R.layout.spinner_item_default_fc, evGender));
        }
        else if(apiCode == APICode.getFirstLevelCategoryPairs){
            List<PairResultItem> cats = (List<PairResultItem>)data;
            SpinnerPlusAdapter<PairResultItem> catsAdapter = new SpinnerPlusAdapter<>(
                    getContext(), R.layout.spinner_item_default_fc, cats);
            mSpSRP2ActivityType.setAdapter(catsAdapter);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        ServiceResponse sresp = (ServiceResponse)data;
        StaticHelperFunctions.showMessage(getActivity(), sresp.getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        Throwable t = (Throwable)data;
        StaticHelperFunctions.showMessage(getActivity(), t.getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(getContext(), "sp2", Toast.LENGTH_SHORT).show();
            CommonRegistrationActivity.getRefreshLayout().setRefreshing(true);
            loadEnums();
            CommonRegistrationActivity.getRefreshLayout().setRefreshing(false);
        }
    };
}
