package ir.joorjens.joorapp.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.zxing.client.android.Intents;

import java.util.ArrayList;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.DeliveryOrderListAdapter;
import ir.joorjens.joorapp.adapters.DrawerListAdapter;
import ir.joorjens.joorapp.adapters.NavItem;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.UserAPIs;

/**
 * Created by ZM on 6/14/2018.
 */

public abstract class DelivererBaseActivity extends AppCompatActivity implements ActivityServiceListener {

    private FrameLayout mContentFrame;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private ListView mDrawerList;
    private ImageViewPlus mImgToolbarMessage;
    private TextViewPlus mTvMainMenuProfile;
    private TextViewPlus mTvMainMenuCredit;
    private ImageViewPlus mImgMainMenuAvatar;

    private static final int CAPTURE_PICTURE = 10;
    private static final int SELECT_PICTURE = 11;
    private static final int PIC_CROP = 12;
    Bitmap newProfileImage;

    public abstract ActivityKeys getActivityId();
    public abstract String getActivityTitle();

    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, mContentFrame, false);
        mContentFrame.addView(contentView, 0);
        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.del_content_frame), DelivererBaseActivity.this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverer_base);
        setDimens();
        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.del_main_drawer), DelivererBaseActivity.this);

        // toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_private_info), ActivityKeys.PrivateInfo));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_del_menu_order_history), ActivityKeys.DeliveryOrders));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_del_menu_accounting), ActivityKeys.DeliveryAccounting));
        mNavItems.add(new NavItem(getResources().getString(R.string.lbl_menu_exit),  ActivityKeys.Logout));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.del_main_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mContentFrame = (FrameLayout) findViewById(R.id.del_content_frame);

        // Populate the Navigation Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.del_drawer_pane);
        mDrawerList = (ListView) findViewById(R.id.del_navList);
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

        mImgToolbarMessage = (ImageViewPlus) findViewById(R.id.del_img_notif);
        mImgToolbarMessage.setOnClickListener(listener_notif_btn);


        ImageViewPlus imgb = (ImageViewPlus)findViewById(R.id.del_toolbar_3dot_menu);
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

        mTvMainMenuProfile = (TextViewPlus) findViewById(R.id.del_tv_main_menu_profile_name);
        mTvMainMenuCredit = (TextViewPlus) findViewById(R.id.del_tv_main_menu_credit);
        mImgMainMenuAvatar = (ImageViewPlus) findViewById(R.id.del_img_main_menu_avatar);
        mImgMainMenuAvatar.setOnClickListener(onProfileImageClicked);
        RelativeLayout profileBox = (RelativeLayout) findViewById(R.id.del_rl_profile_box);
        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent controlPanelIntent = new Intent(DelivererBaseActivity.this, ControlPanelActivity.class);
                StaticHelperFunctions.openActivity(DelivererBaseActivity.this, controlPanelIntent);
            }
        });
        Profile p = Authenticator.loadAccount();
        mTvMainMenuProfile.setText(p.getName());
        mTvMainMenuCredit.setText(String.format("%,d", p.getCredit()) + " تومان");
        mImgMainMenuAvatar.setImage(p.getImageProfile());
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

    private View.OnClickListener onProfileImageClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // change image
            StaticHelperFunctions.selectDialog(DelivererBaseActivity.this, selectImageListener,1);
        }
    };

    private View.OnClickListener listener_notif_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // open message activity
            Intent msgIntent = new Intent(DelivererBaseActivity.this, MessagesListActivity.class);
            StaticHelperFunctions.openActivity(DelivererBaseActivity.this, msgIntent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE || requestCode == CAPTURE_PICTURE) {
                Uri selectedImageUri = data.getData();
                performCrop(selectedImageUri);
                //String imagePath = StaticHelperFunctions.getPathFromURI(this, selectedImageUri);
                //creatingStore.setManagerImageP(imagePath);
            } else if (requestCode == PIC_CROP) {
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

    private void selectItemFromDrawer(NavItem item, int position) {
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getTitle());
        //String tt =  ((NavItem)mDrawerList.getItemAtPosition(position)).getTitle();

        if(item.getId() == getActivityId()){}
        else if(item.getId() == ActivityKeys.Logout){
            UserAPIs.logout(this, Authenticator.loadAuthenticationToken(), 0);
        }
        else if(item.getId() == ActivityKeys.PrivateInfo){
            Intent privateInfoIntent = new Intent(this, DeliveryPrivateInfoActivity.class);
            StaticHelperFunctions.openActivity(DelivererBaseActivity.this, privateInfoIntent);
        }
        else if(item.getId() == ActivityKeys.DeliveryOrders){
            Intent ordersHistoryIntent = new Intent(this, DeliveryOrdersActivity.class);
            StaticHelperFunctions.openActivity(DelivererBaseActivity.this, ordersHistoryIntent);
        }

        else if(item.getId() == ActivityKeys.DeliveryAccounting){
            Intent AccountingIntent = new Intent(this, DeliveryAccountingActivity.class);
            StaticHelperFunctions.openActivity(DelivererBaseActivity.this, AccountingIntent);
        }

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void setDimens()
    {
        int w = getResources().getDisplayMetrics().widthPixels;
        float toolbarIconP = (float) .1133;
        Toolbar llToolbar = (Toolbar)findViewById(R.id.del_toolbar);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)llToolbar.getLayoutParams();
        params.height = (int)(toolbarIconP*w);
        params.bottomMargin = (int)(.002*w);
        llToolbar.requestLayout();
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
