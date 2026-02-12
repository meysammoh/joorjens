package ir.joorjens.joorapp.activities;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.fragments.CommonRegistrationP1Fragment;
import ir.joorjens.joorapp.models.Store;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;

public class CommonRegistrationActivity extends ControlPanelBaseActivity implements ActivityServiceListener{

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

    private static SwipeRefreshLayout.OnRefreshListener mListener;
    private NestedScrollView mMainScroll;
    private static SwipeRefreshLayout mRefreshLayout;
    public static void setSwipeEnabled(boolean enabled){
        mRefreshLayout.setEnabled(enabled);
    }
    public static void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        mListener = listener;
        mRefreshLayout.setOnRefreshListener(listener);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);
    }
    public static SwipeRefreshLayout getRefreshLayout(){
        return mRefreshLayout;
    }



    @BindView(R.id.img_profile_pic) ImageView mImgProfilePic;
    //@BindView(R.id.cr_scroll) ScrollView mScrollView;
    @BindView(R.id.tv_registration_profile_name) TextViewPlus mTvCRProfileName;

    public Store creatingStore;

    private static final int CAPTURE_PICTURE = 0;
    private static final int SELECT_PICTURE = 1;
    private static final int PIC_CROP = 2;

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.CommonRegistration;
    }

    @Override
    public String getActivityTitle() {
        return "پنل کاربری";
    }

    //public void scrollToUp(){
    //    mScrollView.fullScroll(ScrollView.FOCUS_UP);
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_common_regitration);
        ButterKnife.bind(this);
        creatingStore = new Store();

        mMainScroll = (NestedScrollView) findViewById(R.id.reg_main_scroll);
        mMainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                CommonRegistrationActivity.setSwipeEnabled(false);
                if(mListener != null) {
                    if (scrollY <= 4) {
                        CommonRegistrationActivity.setSwipeEnabled(true);
                    } else {
                        CommonRegistrationActivity.setSwipeEnabled(false);
                    }
                }
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.reg_refresh_data);

        if (savedInstanceState == null) {
            CommonRegistrationP1Fragment crp1Fragment = CommonRegistrationP1Fragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("CommonRegistrationP1Fragment");
            ft.replace(R.id.common_registration_container, crp1Fragment).commit();
        }

        mTvCRProfileName.setText(Authenticator.loadAccount().getName());
        mImgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            StaticHelperFunctions.selectDialog(CommonRegistrationActivity.this, selectImageListener,1);
            }
        });
    }

    private StaticHelperFunctions.selectDialogListener selectImageListener = new StaticHelperFunctions.selectDialogListener() {
        @Override
        public void onCameraSelected(DialogInterface dialog, int requestCode) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, CAPTURE_PICTURE);
            dialog.dismiss();
        }

        @Override
        public void onGallerySelected(DialogInterface dialog, int requestCode) {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, CAPTURE_PICTURE);
            dialog.dismiss();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == RESULT_OK && requestCode < 10) {
            if (requestCode == SELECT_PICTURE || requestCode == CAPTURE_PICTURE) {
                Uri selectedImageUri = data.getData();
                performCrop(selectedImageUri);
                //String imagePath = StaticHelperFunctions.getPathFromURI(this, selectedImageUri);
                //creatingStore.setManagerImageP(imagePath);
            }
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                creatingStore.setManagerImageP(StaticHelperFunctions.convertToBase64(thePic));
                mImgProfilePic.setImageBitmap(thePic);
            }
        }
    }

        private void performCrop(Uri picUri){

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onServiceSuccess(apiCode, data, requestCode);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onServiceFail(apiCode, data, requestCode);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onNetworkFail(apiCode, data, requestCode);
        }
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            Fragment fragment = getFragmentManager().
                    findFragmentByTag("StoreRegistrationP4Fragment");
            if(fragment != null){
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }else {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

    }
}
