package ir.joorjens.joorapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.DistributorActivity;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.CheckBoxPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.BrandAPIs;
import ir.joorjens.joorapp.webService.CacheContainer;

public class DistributorFilterNameFragment extends Fragment implements ActivityServiceListener{

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

    List<PairResultItem> expandableListTitle;
    SpinnerPlus<PairResultItem> mSpinnerBrands;
    ButtonPlus searchButton;
    EditTextPlus nameField;
    //SpinnerPlus<PairResultItem> brandSpinner;
    public DistributorFilterNameFragment() {

    }

    public static DistributorFilterNameFragment newInstance(){
        DistributorFilterNameFragment fragment = new DistributorFilterNameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distributor_filter_name, container, false);
        mSpinnerBrands = (SpinnerPlus<PairResultItem>) v.findViewById(R.id.sp_dist_filter_products_brand);
        searchButton =  (ButtonPlus) v.findViewById(R.id.btn_dist_filter_search);
        searchButton.setOnClickListener( searchDistributorListener );
        nameField = (EditTextPlus) v.findViewById(R.id.et_dist_filter_name);
        //brandSpinner = (SpinnerPlus<PairResultItem>) v.findViewById(R.id.sp_dist_filter_products_brand);

        //BrandAPIs.getPairBrands( this, Authenticator.loadAuthenticationToken(), 0);
        processAllBrands(CacheContainer.get().getBrands());

        return v;
    }

    private View.OnClickListener searchDistributorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle params = new Bundle();
            String name = nameField.getText().toString();
            if( !name.isEmpty()){
                params.putString(DistributorActivity.DistributorNameParam, name);
            }
            PairResultItem br = (PairResultItem) mSpinnerBrands.getSelectedItem();
            if( br.getFirst() > -1){
                params.putString(DistributorActivity.DistributorBrandListParam, br.getFirst()+"");
            }

            Intent searchDistIntent = new Intent(getContext(), DistributorActivity.class);
            searchDistIntent.putExtras( params );
            StaticHelperFunctions.openActivity(getContext(), searchDistIntent);
        }
    };
    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
//        if(apiCode==APICode.pairBrands){
//            List<PairResultItem> brands = (List<PairResultItem>) data;
//            processAllBrands(brands);
//        }

    }

    private void processAllBrands(List<PairResultItem> brands) {
        expandableListTitle = new ArrayList<>();
        PairResultItem item = new PairResultItem();
        item.setFirst(-1l);
        item.setSecond(getResources().getString(R.string.label_choose));
        expandableListTitle.add(item);
        for (PairResultItem pair: brands) {
            expandableListTitle.add( pair );
        }

        SpinnerPlusAdapter<PairResultItem> adapter = new SpinnerPlusAdapter<>(this.getActivity(),
                R.layout.spinner_item_default_fc, expandableListTitle);
        // attaching data adapter to spinner
        mSpinnerBrands.setAdapter(adapter);
    }
}
