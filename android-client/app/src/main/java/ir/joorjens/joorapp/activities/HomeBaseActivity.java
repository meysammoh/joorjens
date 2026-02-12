package ir.joorjens.joorapp.activities;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

//import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
//import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.utils.BottomNavigationViewBehavior;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;


public abstract class HomeBaseActivity extends BaseActivity implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;
        mFab.setVisibility(View.GONE);
        mFab.setOnClickListener(null);
        //CartAPIs.getCart(this, Authenticator.loadAuthenticationToken(), 1);
    }
    @Override
    protected void onStop() {
        _isActive = false;
        super.onStop();
    }
    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    LinearLayout mllTopBorder;
    TextViewPlus mTvActivityTitle;
    private static FloatingActionButton mFab;

    FrameLayout mHbaContentFrame;
    //NestedScrollView mMainScroll;
    private int mBarcode;
    private ActivityKeys mCurrentKey = null;
    @BindView(R.id.hp_home)
    ImageViewPlus mImgHpHome;
    @BindView(R.id.hp_company)
    ImageViewPlus mImgHpCompany;
    @BindView(R.id.hp_category)
    ImageViewPlus mImgHpCategory;
    @BindView(R.id.hp_search)
    ImageViewPlus mImgHpSearch;
    BottomNavigationView mBottomBar;
    LinearLayout mLLSearch;
    LinearLayout mLLCategory;
    LinearLayout mLLCompany;
    LinearLayout mLLHome;

    protected static final String PRODUCT_BARCODE_SN_TAG = "PRODUCT_BARCODE_SN";

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.HomeBase;
    }

    @Override
    public String getActivityTitle() {
        return "";
    }

    public void showTitle(boolean show){
        if(show){
            mllTopBorder.setVisibility(View.VISIBLE);
        }else{
            mllTopBorder.setVisibility(View.GONE);
        }
    }

    public static void setFabVisibility(boolean visibility){
        if(visibility)
            mFab.show();
        else
            mFab.hide();
    }

    public static void setFabOnClickListener(View.OnClickListener listener){
        mFab.setOnClickListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_home_base);

        ButterKnife.bind(this);

        mllTopBorder = (LinearLayout) findViewById(R.id.hba_top_border);
        mTvActivityTitle = (TextViewPlus) findViewById(R.id.hba_activity_title);
        mTvActivityTitle.setText(getActivityTitle().toString());

        mLLHome = (LinearLayout) findViewById(R.id.hp_ll_home);
        mLLCompany = (LinearLayout) findViewById(R.id.hp_ll_company);
        mLLCategory = (LinearLayout) findViewById(R.id.hp_ll_category);
        mLLSearch = (LinearLayout) findViewById(R.id.hp_ll_search);
        mLLHome.setOnClickListener(okl);
        mLLCompany.setOnClickListener(okl);
        mLLCategory.setOnClickListener(okl);
        mLLSearch.setOnClickListener(okl);


        mHbaContentFrame = (FrameLayout) findViewById(R.id.hba_content_frame);
        mBottomBar = (BottomNavigationView)findViewById(R.id.main_bottomNavigation);
        mBottomBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBottomBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = mBottomBar.getHeight();
            }
        });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomBar.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.hba_main), HomeBaseActivity.this);

        mImgHpHome.setOnClickListener(okl);
        mImgHpCompany.setOnClickListener(okl);
        mImgHpCategory.setOnClickListener(okl);
        mImgHpSearch.setOnClickListener(okl);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
    }


    public void setBottomBarVisibility(boolean show){
        if(show){
            //mBottomBar.setVisibility(View.VISIBLE);
            slideUp(mBottomBar);
        }else{
            //mBottomBar.setVisibility(View.GONE);
            slideDown(mBottomBar);
        }
    }


    boolean upStart = false, downStart = false;
    int height = 0;
    private void slideUp(BottomNavigationView child) {
        if(!upStart)
            child.animate().translationY(.0f).setDuration(90).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    upStart = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    upStart = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    upStart = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    private void slideDown(BottomNavigationView child) {
        if(!downStart)
            child.animate().translationY(height).setDuration(90).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    downStart = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    downStart = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    downStart = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    private View.OnClickListener okl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hp_home:
                case R.id.hp_ll_home:
                    //if (mCurrentKey != ActivityKeys.Home) {
                        Intent homeIntent = new Intent(HomeBaseActivity.this, HomeActivity.class);
                        StaticHelperFunctions.openActivity(HomeBaseActivity.this, homeIntent);
                    //}
                    break;

                case R.id.hp_search:
                case R.id.hp_ll_search:
                    //if (mCurrentKey != ActivityKeys.SearchProduct) {
                        //Intent searchProductIntent = new Intent(HomeBaseActivity.this, SearchProductActivity.class);
                        //openSearchActivity(mBarcode);
                    openPreSearchActivity();
                    //}
                    break;
                case R.id.hp_category:
                case R.id.hp_ll_category:
                    //if (mCurrentKey != ActivityKeys.Category) {
                        Intent categoryIntent = new Intent(HomeBaseActivity.this, CategoryActivity.class);
                        StaticHelperFunctions.openActivity(HomeBaseActivity.this, categoryIntent);
                    //}
                    break;
                case R.id.hp_company:
                case R.id.hp_ll_company:
                    //if (mCurrentKey != ActivityKeys.Distributor) {
                        //Intent distIntent = new Intent(HomeBaseActivity.this, DistributorsFilterListActivity.class);
                        Intent distIntent = new Intent(HomeBaseActivity.this, DistributorActivity.class);
                        Bundle searchParams = new Bundle();
                        distIntent.putExtras(searchParams);
                        StaticHelperFunctions.openActivity(HomeBaseActivity.this, distIntent);
                    //}
                    break;
            }
        }
    };

    private void openSearchActivity(int barcode) {
        Intent searchIntent = new Intent(this, SearchProductActivity.class);
        searchIntent.putExtra("barcode", barcode);
        searchIntent.putExtra("logo_id", 5);
        StaticHelperFunctions.openActivity(HomeBaseActivity.this, searchIntent);
    }

    private void openPreSearchActivity() {
        Intent preSearchIntent = new Intent(this, PreSearchActivity.class);
        StaticHelperFunctions.openActivity(HomeBaseActivity.this, preSearchIntent);
    }

    public void addContentView(int layoutId, ActivityKeys activityId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, mHbaContentFrame, false);
        StaticHelperFunctions.setKeyboardAutoHide(contentView, HomeBaseActivity.this);

        // clear logo selections
        mImgHpHome.getImageView().setImageResource(R.drawable.hp_home_logo_unsel);
        mImgHpCategory.getImageView().setImageResource(R.drawable.hp_category_logo_unsel);
        mImgHpCompany.getImageView().setImageResource(R.drawable.hp_company_logo_unsel);
        mImgHpSearch.getImageView().setImageResource(R.drawable.hp_search_logo_unsel);

        if (activityId == ActivityKeys.Home) {
            mCurrentKey = ActivityKeys.Home;
            mImgHpHome.getImageView().setImageResource(R.drawable.hp_home_logo_sel);
        } else if (activityId == ActivityKeys.SearchProduct) {
            mCurrentKey = ActivityKeys.SearchProduct;
            mImgHpSearch.getImageView().setImageResource(R.drawable.hp_search_logo_sel);
        } else if (activityId == ActivityKeys.Category) {
            mCurrentKey = ActivityKeys.Category;
            mImgHpCategory.getImageView().setImageResource(R.drawable.hp_category_logo_sel);
        } else if (activityId == ActivityKeys.Distributor) {
            mCurrentKey = ActivityKeys.Distributor;
            mImgHpCompany.getImageView().setImageResource(R.drawable.hp_company_logo_sel);
        }

        mHbaContentFrame.addView(contentView, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //now getIntent() should always return the last received intent
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        super.onServiceSuccess(apiCode, data, requestCode);
//        if(apiCode == APICode.getCart){
//            Cart cart = (Cart) data;
//            super.setCartCount( cart.getCartPrice().getPackCount() );
//        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        super.onServiceFail(apiCode, data, requestCode);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        super.onNetworkFail(apiCode, data, requestCode);
    }
}
