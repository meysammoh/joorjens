package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;

public class ReturnProductP2Fragment extends Fragment implements ActivityServiceListener{

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

    private ImageViewPlus _imgProductPic;
    private TextViewPlus _tvProductName;
    private TextViewPlus _tvProductCategory;
    private TextViewPlus _tvProductPackingType;
    private TextViewPlus _tvProductPrice;

    public ReturnProductP2Fragment() {
        // Required empty public constructor
    }

    private void initializeComponents(View v){
        // UI elements
        _imgProductPic = (ImageViewPlus) v.findViewById(R.id.frpp2_img_product);
        _tvProductName = (TextViewPlus) v.findViewById(R.id.frpp2_product_name);
        _tvProductCategory = (TextViewPlus) v.findViewById(R.id.frpp2_product_category);
        _tvProductPackingType = (TextViewPlus) v.findViewById(R.id.frpp2_product_packing_type);
        _tvProductPrice = (TextViewPlus) v.findViewById(R.id.frpp2_product_price);

    }

    private void setUIValues(DistributorProduct dp){
        _imgProductPic.setImage(dp.getProductImage());
        _tvProductName.setText(dp.getProductName());
        for(ProductDetails detail : dp.getProductDetails()) {
            if (detail.getDescription().contains("نوع ظرف")) {
                _tvProductPackingType.setText(detail.getDescription());
            }
        }
        _tvProductCategory.setText(dp.getProductCategoryTypeName());
        _tvProductPrice.setText(String.format("%,d", dp.getProductPriceConsumer()) + " تومان" );
    }

    public static ReturnProductP2Fragment newInstance(){
        ReturnProductP2Fragment fragment = new ReturnProductP2Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_return_product_p2, container, false);

        Bundle searchOptions = getArguments();
        Map<String, String> options = new HashMap<>();
        for (String key :
                searchOptions.keySet()) {
            options.put(key, searchOptions.getString(key));
        }
        //DistributorProductAPIs.searchDProducts(this, Authenticator.loadAuthenticationToken(), options, 1);

        initializeComponents(v);

        return v;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchDistributorProduct){
            ResultList<DistributorProduct> dp = (ResultList<DistributorProduct>)data;
            setUIValues(dp.getResult().get(0));
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
