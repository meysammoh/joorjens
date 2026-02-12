package ir.joorjens.joorapp.fragments;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.CommonRegistrationActivity;
import ir.joorjens.joorapp.customViews.BrowseButton;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.StoreAPIs;

public class StoreRegistrationP3Fragment extends Fragment  implements ActivityServiceListener{
    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        CommonRegistrationActivity.setOnRefreshListener(null);
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
    private SweetAlertDialog mProgressDialog;
    @BindView(R.id.btn_srp3_continue) ButtonPlus mBtnSRP3Continue;
    @BindView(R.id.btn_srp3_imageBusinessLicense) BrowseButton mBtnSRP3ImageBusinessLicense;
    @BindView(R.id.btn_srp3_managerImageNI) BrowseButton mBtnSRP3ManagerImageNI;
    @BindView(R.id.btn_srp3_managerImageBC) BrowseButton mBtnSRP3ManagerImageBC;

    public StoreRegistrationP3Fragment() {
    }

    private boolean checkMandatoryFields(){
        if(TextUtils.isEmpty(mBtnSRP3ManagerImageNI.getFullPath()) ||
                TextUtils.isEmpty(mBtnSRP3ImageBusinessLicense.getFullPath()) ){
            return false;
        }
        return true;
    }

    private StaticHelperFunctions.selectDialogListener imageListener = new StaticHelperFunctions.selectDialogListener() {
        @Override
        public void onCameraSelected(DialogInterface dialog, int requestCode) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, requestCode);
            dialog.dismiss();
        }

        @Override
        public void onGallerySelected(DialogInterface dialog, int requestCode) {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, requestCode);
            dialog.dismiss();
        }
    };

    private void insertStore(){
        if(checkMandatoryFields()) {

            mProgressDialog = StaticHelperFunctions.showLoadingDialog(getActivity(),
                    getResources().getString(R.string.running_query));


            Store store = ((CommonRegistrationActivity) getActivity()).creatingStore;
//            store.setImageBusinessLicense(mBtnSRP3ImageBusinessLicense.getFullPath());
//            store.setManagerImageNI(mBtnSRP3ManagerImageNI.getFullPath());
//            store.setManagerImageBC(mBtnSRP3ManagerImageBC.getFullPath());
//
//            // convert images to base64
//            String imageBusinessLicensePath = store.getImageBusinessLicense();
//            String managerImageNIPath = store.getManagerImageNI();
//            String managerImageBCPath = store.getManagerImageBC();
//            store.setImageBusinessLicense(StaticHelperFunctions.convertToBase64(imageBusinessLicensePath));
//            store.setManagerImageNI(StaticHelperFunctions.convertToBase64(managerImageNIPath));
//            store.setManagerImageBC(StaticHelperFunctions.convertToBase64(managerImageBCPath));

            StoreAPIs.insertStore(this, Authenticator.loadAuthenticationToken(), store, 0);
        }
        else{
            StaticHelperFunctions.showMessage(getActivity(), "یکی از فیلدهای اجباری پر نشده", StaticHelperFunctions.MessageType.Alert);
        }
    }

    private void replaceFragment() {
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        StoreRegistrationP4Fragment nextFragment = StoreRegistrationP4Fragment.newInstance();
        mFragmentTransaction.hide(StoreRegistrationP3Fragment.this);
        mFragmentTransaction.addToBackStack("StoreRegistrationP4Fragment");
        mFragmentTransaction.replace(R.id.common_registration_container, nextFragment).commit();

        //((CommonRegistrationActivity) getActivity()).scrollToUp();
    }

    public static StoreRegistrationP3Fragment newInstance(){
        StoreRegistrationP3Fragment fragment = new StoreRegistrationP3Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store_registration_p3, container, false);
        ButterKnife.bind(this, v);

        mBtnSRP3Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStore();
            }
        });

        mBtnSRP3ImageBusinessLicense.setParent(getActivity());
        mBtnSRP3ImageBusinessLicense.setSelectRequestCode(11);
        mBtnSRP3ImageBusinessLicense.setCropRequestCode(21);
        mBtnSRP3ImageBusinessLicense.setImageListener(imageListener);

        mBtnSRP3ManagerImageNI.setParent(getActivity());
        mBtnSRP3ManagerImageNI.setSelectRequestCode(12);
        mBtnSRP3ManagerImageNI.setCropRequestCode(22);
        mBtnSRP3ManagerImageNI.setImageListener(imageListener);

        mBtnSRP3ManagerImageBC.setParent(getActivity());
        mBtnSRP3ManagerImageBC.setSelectRequestCode(13);
        mBtnSRP3ManagerImageBC.setCropRequestCode(23);
        mBtnSRP3ManagerImageBC.setImageListener(imageListener);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK) {

            if (requestCode > 10 && requestCode < 20) { // select image{
                Uri selectedImageUri = data.getData();
                String imageFullPath = StaticHelperFunctions.getPathFromURI(getActivity(), selectedImageUri);
                if (requestCode == mBtnSRP3ImageBusinessLicense.getSelectRequestCode()) {
                    mBtnSRP3ImageBusinessLicense.setText(imageFullPath);
                    performCrop(selectedImageUri, mBtnSRP3ImageBusinessLicense.getCropRequestCode());
                } else if (requestCode == mBtnSRP3ManagerImageNI.getSelectRequestCode()) {
                    mBtnSRP3ManagerImageNI.setText(imageFullPath);
                    performCrop(selectedImageUri, mBtnSRP3ManagerImageNI.getCropRequestCode());
                } else if (requestCode == mBtnSRP3ManagerImageBC.getSelectRequestCode()) {
                    mBtnSRP3ManagerImageBC.setText(imageFullPath);
                    performCrop(selectedImageUri, mBtnSRP3ManagerImageBC.getCropRequestCode());
                }
            }else if (requestCode > 20 && requestCode < 30) { // crop image{
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                Store creatingStore = ((CommonRegistrationActivity)getActivity()).creatingStore;

                if (requestCode == mBtnSRP3ImageBusinessLicense.getCropRequestCode()) {
                    creatingStore.setImageBusinessLicense(StaticHelperFunctions.convertToBase64(thePic));
                    mBtnSRP3ImageBusinessLicense.showText();
                } else if (requestCode == mBtnSRP3ManagerImageNI.getCropRequestCode()) {
                    creatingStore.setManagerImageNI(StaticHelperFunctions.convertToBase64(thePic));
                    mBtnSRP3ManagerImageNI.showText();
                } else if (requestCode == mBtnSRP3ManagerImageBC.getCropRequestCode()) {
                    creatingStore.setManagerImageBC(StaticHelperFunctions.convertToBase64(thePic));
                    mBtnSRP3ManagerImageBC.showText();
                }
            }
        }
    }

    private void performCrop(Uri picUri, int requestCode) {

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
//            //indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            //indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, requestCode);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
        }
    }


    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        replaceFragment();
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        StaticHelperFunctions.showMessage(getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        mProgressDialog.dismissWithAnimation();
        StaticHelperFunctions.showMessage(getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
