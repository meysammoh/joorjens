package ir.joorjens.joorapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.fragments.DistributorFilterCategoriesFragment;
import ir.joorjens.joorapp.fragments.DistributorFilterNameFragment;


/**
 * Created by meysammoh on 16.03.18.
 */

public class DistributorsFilterListActivity extends HomeBaseActivity {


    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        _isActive = true;
        loadArgs();
        showData();
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

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.Distributor;
    }

    @Override
    public String getActivityTitle() {
        return getResources().getString(R.string.bottomBarCompany);
    }

    public static String mFilterIdParamName = "FilterId";
    public static int mFilterByCategory = 0;
    public static int mFilterByName = 1;
    private int mFilterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_distributors_filter_list, ActivityKeys.Distributor);
        super.showTitle(true);
        if (savedInstanceState == null) {
            DistributorFilterCategoriesFragment currentFragment = DistributorFilterCategoriesFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dist_fragment_container, currentFragment).commit();
        }
    }

    private void loadArgs(){
        if(getIntent().getExtras() != null){
            Integer filterId = getIntent().getExtras().getInt(mFilterIdParamName,0);
            mFilterId = filterId;
        }
    }

    private void showData(){
        if (mFilterId == mFilterByName) {
            DistributorFilterNameFragment newFragment = DistributorFilterNameFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dist_fragment_container, newFragment).commit();
        } else if (mFilterId == mFilterByCategory) {
            DistributorFilterCategoriesFragment newFragment = DistributorFilterCategoriesFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dist_fragment_container, newFragment).commit();
        }
    }
}
