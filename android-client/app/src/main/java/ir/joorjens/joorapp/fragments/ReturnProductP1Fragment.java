package ir.joorjens.joorapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.zxing.client.android.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.activities.ReturnProductActivity;
import ir.joorjens.joorapp.activities.ShowAllTheMostActivity;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CategoryAPIs;


public class ReturnProductP1Fragment extends Fragment implements ActivityServiceListener {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
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

    public static ReturnProductP1Fragment newInstance(){
        ReturnProductP1Fragment fragment = new ReturnProductP1Fragment();
        return fragment;
    }

    private ButtonPlus _btnScanBarcode;
    private ButtonPlus _btnSearch;
    private EditTextPlus _etProductName;
    private EditTextPlus _etProductBarcode;
    private SpinnerPlus _spProductCategory;

    private final int Barcode_Request_Code = 1;
    private List<KeyValueChildItem> expandableListTitle;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 994;
    private static final String TAG = "Home Base Activity";
    protected static final String PRODUCT_BARCODE_SN_TAG = "PRODUCT_BARCODE_SN";

    private void initializeComponents(View view){
        _btnScanBarcode = (ButtonPlus) view.findViewById(R.id.rpa_btn_scan_barcode);
        _btnScanBarcode.setOnClickListener(scanButtonClicked);
        _btnSearch = (ButtonPlus) view.findViewById(R.id.rpa_btn_search);
        _btnSearch.setOnClickListener(searchButtonClicked);
        _etProductName = (EditTextPlus) view.findViewById(R.id.rpa_et_product_name);
        _etProductBarcode = (EditTextPlus) view.findViewById(R.id.rpa_et_product_barcode);
        _spProductCategory = (SpinnerPlus) view.findViewById(R.id.rpa_sp_product_category);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_product_p1, container, false);
        initializeComponents(view);

        return view;
    }

    private Bundle makeNormalSearchParams(){

        Bundle searchBundle = new Bundle();
        if (!_etProductBarcode.getText().toString().equals(""))
            searchBundle.putString("barcode", _etProductBarcode.getText().toString());
        if (!_etProductName.getText().toString().equals(""))
            searchBundle.putString("name", _etProductName.getText().toString());
        if (_spProductCategory.getSelectedItemPosition() > 0)
            searchBundle.putString("productCategoryTypeId", ((KeyValueChildItem) _spProductCategory.getSelectedItem()).getId() + "");
        return searchBundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Barcode_Request_Code) {
            if (resultCode == Activity.RESULT_OK) {
                String productId = data.getStringExtra("barcode_result");
                _etProductBarcode.setText(productId);
            }
        }
    }


    private void processAllCategories(List<KeyValueChildItem> cats){
        expandableListTitle = new ArrayList<>();

        for (KeyValueChildItem cat : cats) {
            expandableListTitle.add( cat );
        }
        KeyValueChildItem item = new KeyValueChildItem();
        item.setId(new Long(0));
        item.setName(getResources().getString(R.string.label_choose));
        expandableListTitle.add(0,item);
        SpinnerPlusAdapter<KeyValueChildItem> adapter = new SpinnerPlusAdapter<>(getActivity(), R.layout.spinner_item_default_fc, expandableListTitle);
        // attaching data adapter to spinner
        _spProductCategory.setAdapter(adapter);
    }

    private View.OnClickListener searchButtonClicked = new View.OnClickListener() {
        public void onClick(View v) {
            Bundle searchBundle = makeNormalSearchParams();
            android.support.v4.app.FragmentManager mFragmentManager = getFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            ReturnProductP2Fragment nextFragment = ReturnProductP2Fragment.newInstance();
            nextFragment.setArguments(searchBundle);
            mFragmentTransaction.replace(R.id.rpa_fragments_container, nextFragment).commit();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "Camera Permission granted!");
//                    Intent intent = new Intent(ReturnProductActivity.this, CaptureActivity.class);
//                    StaticHelperFunctions.openActivity(ReturnProductActivity.this, intent);
//
//                } else {
//
//                    Log.w(TAG, "Camera Permission was not granted!");
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
    }

    private View.OnClickListener scanButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission is ok ...");
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, Barcode_Request_Code);
            } else {
                Log.d(TAG, "Requesting Camera access runtime ...");
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {
                    Log.w(TAG, "User checked never show permission request dialog!!!!");
                    //Toast.makeText(HomeBaseActivity.this, "Set camera permission", Toast.LENGTH_LONG).show();
                } else {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        }
    };
    public ActivityKeys getActivityId() {
        return ActivityKeys.ReturnProduct;
    }

    @Override
    public void onResume() {
        super.onResume();

        _isActive = true;

        //CategoryAPIs.getHierarchicalCategories(this, Authenticator.loadAuthenticationToken(), 1);
        processAllCategories(CacheContainer.get().getCategories());
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
//        if(apiCode==APICode.getHierarchicalCategories){
//            List<KeyValueChildItem> cats = (List<KeyValueChildItem>) data;
//
//            processAllCategories(cats);
//        }
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
