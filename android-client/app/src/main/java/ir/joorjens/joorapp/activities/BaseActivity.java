package ir.joorjens.joorapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Cache;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.DrawerListAdapter;
import ir.joorjens.joorapp.adapters.NavItem;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.SwipeRefreshLayoutPlus;
import ir.joorjens.joorapp.customViews.SwipeRefreshPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.fragments.ViewCartFragment;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.ProductAPIs;
import ir.joorjens.joorapp.webService.UserAPIs;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by Mohsen on 9/30/2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityServiceListener{

    private static final int CAPTURE_PICTURE = 10;
    private static final int SELECT_PICTURE = 11;
    private static final int PIC_CROP = 12;
    Bitmap newProfileImage;

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    FrameLayout mContentFrame;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    TextViewPlus mTvMainMenuProfile;
    TextViewPlus mTvMainMenuCredit;
    ImageViewPlus mImgMainMenuAvatar;
    ImageViewPlus mImgToolbarScan;
    ImageViewPlus mImgToolbarCart;
    ImageViewPlus mImgToolbarMessage;
    LinearLayout mllLogo;
    private static boolean startUp = true;

    private final int Barcode_Request_Code = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 994;
    private static final String TAG = "Base Activity";
    protected static final String PRODUCT_BARCODE_SN_TAG = "PRODUCT_BARCODE_SN";
    public  static String ProductBarcodeParam = "productbarcode";

    private void setDimens()
    {
        int w = getResources().getDisplayMetrics().widthPixels;
        float toolbarIconP = (float) .1133;
        Toolbar llToolbar = (Toolbar)findViewById(R.id.toolbar);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)llToolbar.getLayoutParams();
        params.height = (int)(toolbarIconP*w);
        params.bottomMargin = (int)(.002*w);
        llToolbar.requestLayout();
    }

    public abstract ActivityKeys getActivityId();
    public abstract String getActivityTitle();

    public void setCartCount(Integer count){
        mImgToolbarCart.setCount(count);
    }

    public void setMessagesCount(Integer count){
        mImgToolbarMessage.setCount(count);
    }

    private void selectItemFromDrawer(NavItem item, int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getTitle());
        //String tt =  ((NavItem)mDrawerList.getItemAtPosition(position)).getTitle();

        if(item.getId() == getActivityId()){}
        else if(item.getId() == ActivityKeys.Logout){
            UserAPIs.logout(this, Authenticator.loadAuthenticationToken(), 0);
        }
        else if(item.getId() == ActivityKeys.Credit){
            //Intent creditIntent = new Intent(this, CreditActivity.class);
            //StaticHelperFunctions.openActivity(BaseActivity.this, creditIntent);
        }
        else if(item.getId() == ActivityKeys.PrivateInfo){
            Intent privateInfoIntent = new Intent(this, PrivateInfoActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, privateInfoIntent);
        }
        else if(item.getId() == ActivityKeys.Turnover){
            Intent turnoverIntent = new Intent(this, TurnoverActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, turnoverIntent);
        }
        else if(item.getId() == ActivityKeys.PreSearch){
            Intent preSearchIntent = new Intent(this, PreSearchActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, preSearchIntent);
        }
        else if(item.getId() == ActivityKeys.ReturnProduct){
            //Intent returnProductIntent = new Intent(this, ReturnProductActivity.class);
            //StaticHelperFunctions.openActivity(BaseActivity.this, returnProductIntent);
        }
        else if(item.getId() == ActivityKeys.ListOrders){
            Intent cartIntent = new Intent(BaseActivity.this, OrdersFilterListActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, cartIntent);
        }
        else if(item.getId() == ActivityKeys.CallSupport){
            // call support
            StaticHelperFunctions.call(this, "02188607815");
        }

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, mContentFrame, false);
        mContentFrame.addView(contentView, 0);
        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.content_frame), BaseActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setDimens();

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.main_drawer), BaseActivity.this);

        // toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_sand_menue);

        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_private_info), ActivityKeys.PrivateInfo));
        //mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_credit), ActivityKeys.Credit));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_turnover), ActivityKeys.Turnover));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_search), ActivityKeys.PreSearch));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_product_return), ActivityKeys.ReturnProduct));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_order_list), ActivityKeys.ListOrders));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_call_support), ActivityKeys.CallSupport));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_exit),  ActivityKeys.Logout));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);

        // Populate the Navigation Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavItem item = ((NavItem)parent.getAdapter().getItem(position));
                selectItemFromDrawer(item, position);
            }
        });

        mImgToolbarScan = (ImageViewPlus)findViewById(R.id.img_scan);
        mImgToolbarScan.setOnClickListener(listener_scan_btn);
        mImgToolbarCart = (ImageViewPlus) findViewById(R.id.img_cart);
        mImgToolbarCart.setOnClickListener(listener_cart_btn);

        mImgToolbarMessage = (ImageViewPlus) findViewById(R.id.img_notif);
        mImgToolbarMessage.setOnClickListener(listener_notif_btn);


        ImageViewPlus imgb = (ImageViewPlus)findViewById(R.id.toolbar_3dot_menu);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mDrawerLayout.isDrawerOpen(mDrawerPane)){
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }
                else{
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        mTvMainMenuProfile = (TextViewPlus) findViewById(R.id.tv_main_menu_profile_name);
        mTvMainMenuCredit = (TextViewPlus) findViewById(R.id.tv_main_menu_credit);
        mImgMainMenuAvatar = (ImageViewPlus) findViewById(R.id.img_main_menu_avatar);
        mImgMainMenuAvatar.setOnClickListener(onProfileImageClicked);
        RelativeLayout profileBox = (RelativeLayout) findViewById(R.id.rl_profile_box);
        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent controlPanelIntent = new Intent(BaseActivity.this, ControlPanelActivity.class);
                StaticHelperFunctions.openActivity(BaseActivity.this, controlPanelIntent);
            }
        });
        Profile p = Authenticator.loadAccount();
        mTvMainMenuProfile.setText(p.getName());
        mTvMainMenuCredit.setText(String.format("%,d", p.getCredit()) + " تومان");
        mImgMainMenuAvatar.setImage(p.getImageProfile());

        mllLogo = (LinearLayout) findViewById(R.id.ll_logo);
        mllLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(BaseActivity.this, HomeActivity.class);
                StaticHelperFunctions.openActivity(BaseActivity.this, homeIntent);
            }
        });
    }

    private View.OnClickListener onProfileImageClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // change image
            StaticHelperFunctions.selectDialog(BaseActivity.this, selectImageListener,1);
        }
    };

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
            startActivityForResult(i, SELECT_PICTURE);
            dialog.dismiss();
        }
    };


    private View.OnClickListener listener_notif_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // open message activity
            Intent msgIntent = new Intent(BaseActivity.this, MessagesListActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, msgIntent);
        }
    };


    private View.OnClickListener listener_cart_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent viewCartIntent = new Intent(BaseActivity.this ,ViewPastCartsActivity.class);
            StaticHelperFunctions.openActivity(BaseActivity.this, viewCartIntent);
        }
    };

    private View.OnClickListener listener_scan_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int permissionCheck = ContextCompat.checkSelfPermission(BaseActivity.this,
                    Manifest.permission.CAMERA);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission is ok ...");
                Intent intent = new Intent(BaseActivity.this, CaptureActivity.class);
                intent.setAction(Intents.Scan.ACTION);
                intent.putExtra(Intents.Scan.MODE, Intents.Scan.ONE_D_MODE);
                startActivityForResult(intent, Barcode_Request_Code);
            } else {
                Log.d(TAG, "Requesting Camera access runtime ...");
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this,
                        Manifest.permission.CAMERA)) {
                    Log.w(TAG, "User checked never show permission request dialog!!!!");
                    //Toast.makeText(HomeBaseActivity.this, "Set camera permission", Toast.LENGTH_LONG).show();
                } else {

                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Barcode_Request_Code) {
            if (resultCode == Activity.RESULT_OK) {
                String productId = data.getStringExtra(Intents.Scan.RESULT);
//                if (this.getClass() != SearchProductActivity.class) {
//                    Intent intent = new Intent(BaseActivity.this, SearchProductActivity.class);
//                    intent.putExtra(PRODUCT_BARCODE_SN_TAG, productId);
//                    StaticHelperFunctions.openActivity(BaseActivity.this, intent);
//                } else {
                    //ProcessBarcodeResult(productId);
//                }
                gotoProductViewByBarcode( productId );
            }
        }

        if (resultCode == RESULT_OK) {
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
                Profile profile = Authenticator.loadAccount();
                profile.setImageProfile(StaticHelperFunctions.convertToBase64(thePic));
                UserAPIs.updateProfile(this, Authenticator.loadAuthenticationToken(), profile, 1);
                newProfileImage = thePic;
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

    void gotoProductViewByBarcode(String barcodeSN){
        Intent showProduct = new Intent(getApplicationContext(), ProductActivity.class);
        showProduct.putExtra("icon_id", R.drawable.search_result + "");
        showProduct.putExtra("title", "نتیجه جستجو");
        showProduct.putExtra("action", ProductActivity.ActionType.PA_SEARCH_BARCODE);
        showProduct.putExtra(ProductBarcodeParam, barcodeSN);
        StaticHelperFunctions.openActivity(BaseActivity.this, showProduct);
    }
    protected void ProcessBarcodeResult(String barcodeSN) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        CacheContainer.get().addCartListener(cartListener);
        int cartCount = 0, messageCount = 0;
        if(CacheContainer.get().getCart() != null) {
            cartCount = CacheContainer.get().getCart().getPackageSize() == null ? 0 :
                    CacheContainer.get().getCart().getPackageSize();
        }
        messageCount = CacheContainer.get().getUnreadMessagesCount();
        setCartCount(cartCount);
        setMessagesCount(messageCount);
    }

    private CacheContainer.CartEventsListener cartListener = new CacheContainer.CartEventsListener() {
        @Override
        public void onCartDataChanged(Cart updatedCart) {
            int count = 0;
            if(CacheContainer.get().getCart() != null) {
                count = CacheContainer.get().getCart().getPackageSize();
            }
            setCartCount(count);
        }
    };



    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Camera Permission granted!");
                    Intent intent = new Intent(BaseActivity.this, CaptureActivity.class);
                    StaticHelperFunctions.openActivity(BaseActivity.this, intent);

                } else {

                    Log.w(TAG, "Camera Permission was not granted!");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean closeDrawerMenu(){
        if(mDrawerLayout.isDrawerOpen(mDrawerPane)){
            mDrawerLayout.closeDrawer(mDrawerPane);
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheContainer.get().removeCartListener(cartListener);
//        CacheContainer.get().stopUpdating();
    }

    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(mDrawerPane)){
            mDrawerLayout.closeDrawer(mDrawerPane);
        }
        else{
            //this.finishAffinity();
            super.onBackPressed();
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.logout) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            Authenticator.cleanAccount();
            loginIntent.putExtra("logout", true);
            startActivity(loginIntent);
            finish();
        }
        if(apiCode == APICode.updateProfile){
            if(newProfileImage != null) {
                mImgMainMenuAvatar.getImageView().setImageBitmap(newProfileImage);
                newProfileImage = null;
            }
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.logout) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            Authenticator.cleanAccount();
            loginIntent.putExtra("logout", true);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.logout) {
            StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }
}
