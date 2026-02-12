package ir.joorjens.joorapp.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.fragments.DistributorListByNameFragment;
import ir.joorjens.joorapp.fragments.DistributorListByScoreFragment;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.utils.SlideAnimation;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

//import ir.joorjens.joorapp.adapters.CategoriesExpandableListAdapter;

public class DistributorActivity extends HomeBaseActivity {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    // onResume is used
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



    private LinearLayout mllDATopBorder;
    private int mTopBorderHeight = 0;

    public void showTabsAndToolbar(boolean show){
        if(show){
            mllDATopBorder.setVisibility(View.VISIBLE);
            super.showTitle(true);
        }else{
            mllDATopBorder.setVisibility(View.GONE);
            super.showTitle(false);
        }
    }

    public void setTopTabsVisibility(boolean visibility){
        if(visibility){
            if(mllDATopBorder.getMeasuredHeight() == 0 && !sliding) {
                Log.d("TAG", "sliding from " + 0 + " to : " + mTopBorderHeight);
                sliding = true;
                SlideAnimation animation = new SlideAnimation(mllDATopBorder, 0, mTopBorderHeight);
                animation.setAnimationListener(mAnListener);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(100);
                mllDATopBorder.setAnimation(animation);
                mllDATopBorder.startAnimation(animation);

                ValueAnimator valAnim = ValueAnimator.ofInt(0, mTopBorderHeight);
                valAnim.setDuration(100);
                valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mFrameLayout.setPadding(0, (Integer) animation.getAnimatedValue(), 0,0);
                    }
                });
                valAnim.start();
            }
        }
        else{
            if(mllDATopBorder.getMeasuredHeight() == mTopBorderHeight && !sliding) {
                Log.d("TAG", "setTopTabsVisibility+++++++++++++: " + mllDATopBorder.getMeasuredHeight());

                sliding = true;
                SlideAnimation animation = new SlideAnimation(mllDATopBorder, mTopBorderHeight, 0);
                animation.setAnimationListener(mAnListener);
                animation.setInterpolator(new AccelerateInterpolator());
                animation.setDuration(100);
                mllDATopBorder.setAnimation(animation);
                mllDATopBorder.startAnimation(animation);

                if(mFrameLayout.getPaddingTop() > 0) {
                    ValueAnimator valAnim = ValueAnimator.ofInt(mTopBorderHeight, 0);
                    valAnim.setDuration(100);
                    valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mFrameLayout.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0);
                        }
                    });
                    valAnim.start();
                }
            }
        }
    }

    private Animation.AnimationListener mAnListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            sliding = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private boolean sliding = false;

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.Distributor;
    }

    @Override
    public String getActivityTitle() {
        return getResources().getString(R.string.bottomBarCompany);
    }

    public static String DistributorNameParam = "name";
    public static String DistributorBrandListParam = "productbrandtypeid";
    public static String DistributorCategoryListParam = "productcategorytypeid";
    //public static String DistributorSerialParam = "serial";
    public static String DistributorIdParam = "id";

    private int _currentTabBtnId = -1;
    private ButtonPlus mBtnScoreTab, mBtnAlphabetTab;
    private LinearLayout mLLScoreTab, mLLAlphabetTab;
    HashMap<Integer, Distributor> _distributors;
    private Bundle searchParams;
    private ButtonPlus mBtnSearchByName;
    private ButtonPlus mBtnSearchByCategory;
    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_distributor, ActivityKeys.Distributor);
        super.showTitle(true);

        mllDATopBorder = (LinearLayout) findViewById(R.id.ll_da_top_border);
        mllDATopBorder.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mllDATopBorder.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mTopBorderHeight = mllDATopBorder.getHeight();
            }
        });

        mBtnScoreTab = (ButtonPlus) findViewById(R.id.distlist_btn_tab_scores);
        mBtnAlphabetTab = (ButtonPlus) findViewById(R.id.distlist_btn_tab_alphabet);
        mBtnScoreTab.setOnClickListener(changeTab);
        mBtnAlphabetTab.setOnClickListener(changeTab);

        mBtnSearchByCategory = (ButtonPlus) findViewById(R.id.dist_filter_by_category);
        mBtnSearchByCategory.setOnClickListener(onSearchByCategory);
        mBtnSearchByName = (ButtonPlus) findViewById(R.id.dist_filter_by_name);
        mBtnSearchByName.setOnClickListener(onSearchByName);

        mLLScoreTab = (LinearLayout) findViewById(R.id.distlist_ll_tab_score);
        mLLAlphabetTab = (LinearLayout) findViewById(R.id.distlist_ll_tab_alphabet);

        mFrameLayout = (FrameLayout) findViewById(R.id.distlist_fragment_container);
    }

    private View.OnClickListener onSearchByName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent distIntent = new Intent(DistributorActivity.this, DistributorsFilterListActivity.class);
            Bundle searchParams = new Bundle();
            searchParams.putInt(DistributorsFilterListActivity.mFilterIdParamName, DistributorsFilterListActivity.mFilterByName);
            distIntent.putExtras(searchParams);
            StaticHelperFunctions.openActivity(DistributorActivity.this, distIntent);
        }
    };

    private View.OnClickListener onSearchByCategory = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent distIntent = new Intent(DistributorActivity.this, DistributorsFilterListActivity.class);
            Bundle searchParams = new Bundle();
            searchParams.putInt(DistributorsFilterListActivity.mFilterIdParamName, DistributorsFilterListActivity.mFilterByCategory);
            distIntent.putExtras(searchParams);
            StaticHelperFunctions.openActivity(DistributorActivity.this, distIntent);
        }
    };

    private View.OnClickListener changeTab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( v.getId() != _currentTabBtnId) {
                if (v.getId() == R.id.distlist_btn_tab_alphabet) {
                    mLLScoreTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mLLAlphabetTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mBtnScoreTab.setTextColor(getResources().getColor(R.color.color_black90));
                    mBtnAlphabetTab.setTextColor(getResources().getColor(R.color.color_first));

                    DistributorListByNameFragment newFragment = DistributorListByNameFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    newFragment.setArguments( searchParams );
                    ft.replace(R.id.distlist_fragment_container, newFragment).commit();
                } else if (v.getId() == R.id.distlist_btn_tab_scores) {
                    mLLAlphabetTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mLLScoreTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mBtnAlphabetTab.setTextColor(getResources().getColor(R.color.color_black90));
                    mBtnScoreTab.setTextColor(getResources().getColor(R.color.color_first));

                    DistributorListByScoreFragment newFragment = DistributorListByScoreFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    newFragment.setArguments( searchParams );
                    ft.replace(R.id.distlist_fragment_container, newFragment).commit();
                }
                _currentTabBtnId = v.getId();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        _isActive = true;

        searchParams = new Bundle();
        _currentTabBtnId = -1;

        if(getIntent().getExtras() != null){
            String name = getIntent().getExtras().getString(DistributorNameParam,"");
            if(!name.isEmpty())
                searchParams.putString(DistributorNameParam, name);
            String brands = getIntent().getExtras().getString(DistributorBrandListParam,"");
            if(!brands.isEmpty())
                searchParams.putString(DistributorBrandListParam, brands);
            String cats = getIntent().getExtras().getString(DistributorCategoryListParam,"");
            if(!cats.isEmpty())
                searchParams.putString(DistributorCategoryListParam, cats);
        }

        if (_currentTabBtnId == -1) {
            _currentTabBtnId = R.id.distlist_btn_tab_alphabet;
            mBtnScoreTab.setTextColor(getResources().getColor(R.color.color_black90));
            mBtnAlphabetTab.setTextColor(getResources().getColor(R.color.color_first));
            mLLScoreTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
            mLLAlphabetTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
            DistributorListByNameFragment currentFragment = DistributorListByNameFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            currentFragment.setArguments( searchParams );
            ft.replace(R.id.distlist_fragment_container, currentFragment).commit();
        }
    }

    public void setDistributors(HashMap<Integer, Distributor> distributors){
        _distributors = distributors;
    }

    public Distributor getDistributor( Integer Id){
        Distributor res = _distributors.containsKey(Id) ? _distributors.get(Id) : null;
        return res;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

}
