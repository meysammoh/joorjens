package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.activities.ControlPanelBaseActivity;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.models.AreaResult;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.RegisterAPIs;

public class StoreRegistrationP1Fragment extends Fragment implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        CommonRegistrationActivity.setOnRefreshListener(onRefresh);
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

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @BindView(R.id.btn_srp1_continue)  ButtonPlus mBtnSRP1Continue;

    @BindView(R.id.et_srp1_store_name) EditTextPlus mEtSRP1StoreName;
    @BindView(R.id.et_srp1_telephone) EditTextPlus mEtSRP1Telephone;
    @BindView(R.id.et_srp1_businessLicense) EditTextPlus mEtSRP1BusinessLicense;
    @BindView(R.id.sp_srp1_province) SpinnerPlus<AreaResult> mSpSRP1Province;
    @BindView(R.id.sp_srp1_city) SpinnerPlus<AreaResult> mSpSRP1City;
    @BindView(R.id.sp_srp1_zone) SpinnerPlus<AreaResult> mSpSRP1Zone;
    @BindView(R.id.et_srp1_avenueMain) EditTextPlus mEtSRP1AvenueMain;
    @BindView(R.id.et_srp1_avenueAuxiliary) EditTextPlus mEtSRP1AvenueAuxiliary;
    @BindView(R.id.et_srp1_plaque) EditTextPlus mEtSRP1Plaque;
    @BindView(R.id.et_srp1_postalCode) EditTextPlus mEtSRP1PostalCode;

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(getContext(), "sp1", Toast.LENGTH_SHORT).show();
            CommonRegistrationActivity.getRefreshLayout().setRefreshing(true);
            getAllProvince();
            CommonRegistrationActivity.getRefreshLayout().setRefreshing(false);
        }
    };

    public StoreRegistrationP1Fragment() {
    }

    private boolean checkMandatoryFields(){
        if(TextUtils.isEmpty(mEtSRP1StoreName.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1Telephone.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1BusinessLicense.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1AvenueMain.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1AvenueAuxiliary.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1Plaque.getText().toString()) ||
                TextUtils.isEmpty(mEtSRP1PostalCode.getText().toString()) ){
            return false;
        }

        try {
            int areaZoneId = ((AreaResult)mSpSRP1Zone.getSelectedItem()).getId();
            int h= 0;
        }catch (Exception ex){
            return false;
        }

        return true;
    }

    private void replaceFragment(){

        if(checkMandatoryFields() == true) {
            // save page content
            Store st = ((CommonRegistrationActivity) getActivity()).creatingStore;
            st.setName(mEtSRP1StoreName.getText().toString());
            st.setTelephone(mEtSRP1Telephone.getText().toString());
            st.setBusinessLicense(mEtSRP1BusinessLicense.getText().toString());
            int areaZoneId = ((AreaResult)(mSpSRP1Zone.getSelectedItem())).getId();
            st.setAreaZoneId(areaZoneId);
            //st.seta (mEtSRP1AvenueMain.getText().toString());
            //st.setAvenueAuxiliary(mEtSRP1AvenueAuxiliary.getText().toString());
            //st.setPlaque(mEtSRP1Plaque.getText().toString());
            st.setPostalCode(mEtSRP1PostalCode.getText().toString());

            mFragmentManager = getFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            StoreRegistrationP2Fragment nextFragment = StoreRegistrationP2Fragment.newInstance();

            mFragmentTransaction.hide(StoreRegistrationP1Fragment.this);
            mFragmentTransaction.addToBackStack("StoreRegistrationP2Fragment");
            mFragmentTransaction.replace(R.id.common_registration_container, nextFragment).commit();

            //((CommonRegistrationActivity) getActivity()).scrollToUp();
        }
        else{
            StaticHelperFunctions.showMessage(getActivity(), "یکی از فیلدهای اجباری پر نشده", StaticHelperFunctions.MessageType.Alert);
        }
    }

    public static StoreRegistrationP1Fragment newInstance(){
        StoreRegistrationP1Fragment fragment = new StoreRegistrationP1Fragment();
        return fragment;
    }

    private void getAllProvince(){
        String cookie = Authenticator.loadAuthenticationToken();
        RegisterAPIs.searchArea(this, EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Province),
                EnumHelper.getEnumCode(EnumHelper.AllEnumNames.AdministrativeDivision), cookie,
                EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Province));
    }
    private void getAllCitiesOfProvince(int parentId){
        String cookie = Authenticator.loadAuthenticationToken();
        RegisterAPIs.searchArea(this, EnumHelper.getEnumCode(EnumHelper.AllEnumNames.City),
                parentId, cookie, EnumHelper.getEnumCode(EnumHelper.AllEnumNames.City));
    }
    private void getAllZonesOfCity(int parentId){
        String cookie = Authenticator.loadAuthenticationToken();
        RegisterAPIs.searchArea(this, EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Zone),
                parentId, cookie, EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Zone));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store_registration_p1, container, false);
        ButterKnife.bind(this, v);

        mBtnSRP1Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment();
            }
        });

        getAllProvince();
        mSpSRP1Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    AreaResult ar = (AreaResult)parent.getAdapter().getItem(position);
                    getAllCitiesOfProvince(ar.getId());
                }catch (Exception ex){
                    //Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpSRP1City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    AreaResult ar = (AreaResult)parent.getAdapter().getItem(position);
                    getAllZonesOfCity(ar.getId());
                }catch (Exception ex){
                    //Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        ResultList<AreaResult> areas = (ResultList<AreaResult>)data;
        SpinnerPlusAdapter<AreaResult> adapter = new SpinnerPlusAdapter<AreaResult>(getContext(),
                R.layout.spinner_item_default_fc, areas.getResult());
        if(requestCode == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Province)){
            // fill Province spinner
            mSpSRP1Province.setAdapter(adapter);
        }
        else if(requestCode == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.City)){
            // fill City spinner
            mSpSRP1City.setAdapter(adapter);
        }
        else if(requestCode == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.Zone)){
            // fill Zone spinner
            mSpSRP1Zone.setAdapter(adapter);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        ServiceResponse sresp = (ServiceResponse)data;
        //Toast.makeText(getContext(), sresp.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        Throwable t = (Throwable)data;
        //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
