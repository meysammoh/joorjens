package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.fragments.OrderPastFragment;
import ir.joorjens.joorapp.fragments.ViewPastCartsByDateFragment;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;


/**
 * Created by meysammoh on 16.03.18.
 */

public class OrdersFilterListActivity extends HomeBaseActivity {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
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

    public static String KeyShowPastOrdersTab = "ShowPastOrdersTab";
    private Boolean _showPastOrdersTab;
    private int _currentTabBtnId;
    private ButtonPlus mBtnCurrentTab, mBtnPastTab;
    private LinearLayout mLLCurrentTab, mLLPastTab;

    @Override
    protected void onResume() {
        super.onResume();

        _isActive = true;

        try {
            _showPastOrdersTab = getIntent().getExtras().getBoolean(KeyShowPastOrdersTab, false);
        }catch (Exception ex){_showPastOrdersTab = false;}
        if(_showPastOrdersTab == true){
            mLLPastTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
            mLLCurrentTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
            mBtnPastTab.setTextColor(getResources().getColor(R.color.color_first));
            mBtnCurrentTab.setTextColor(getResources().getColor(R.color.color_black90));

            OrderPastFragment newFragment = OrderPastFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dist_fragment_container, newFragment).commit();
            _currentTabBtnId = R.id.orders_btn_tab_past;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_orders_filter_list, ActivityKeys.ListOrders);
        _currentTabBtnId = R.id.dist_btn_tab_name;
        if (savedInstanceState == null) {
            ViewPastCartsByDateFragment currentFragment = ViewPastCartsByDateFragment.newInstance();
            currentFragment.setItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrder( v.getTag().toString() );
                }
            });
            Bundle bundle = new Bundle();
                bundle.putBoolean( ViewPastCartsActivity.IsFinishedParamName, false );
            currentFragment.setArguments( bundle );
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dist_fragment_container, currentFragment).commit();
        }

        mBtnCurrentTab = (ButtonPlus) findViewById(R.id.dist_btn_tab_name);
        mBtnPastTab = (ButtonPlus) findViewById(R.id.orders_btn_tab_past);
        mBtnCurrentTab.setOnClickListener(changeTab);
        mBtnPastTab.setOnClickListener(changeTab);
        mBtnCurrentTab.setTextColor(getResources().getColor(R.color.color_first));
        mBtnPastTab.setTextColor(getResources().getColor(R.color.color_black90));

        mLLCurrentTab = (LinearLayout) findViewById(R.id.dist_ll_tab_categories);
        mLLPastTab = (LinearLayout) findViewById(R.id.dist_ll_tab_name);
        mLLCurrentTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
        mLLPastTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));


    }

    private View.OnClickListener changeTab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( v.getId() != _currentTabBtnId) {
                if (v.getId() == R.id.dist_btn_tab_name) {
                    mLLCurrentTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mLLPastTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mBtnCurrentTab.setTextColor(getResources().getColor(R.color.color_first));
                    mBtnPastTab.setTextColor(getResources().getColor(R.color.color_black90));

                    ViewPastCartsByDateFragment newFragment = ViewPastCartsByDateFragment.newInstance();
                    newFragment.setItemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOrder( v.getTag().toString() );
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putBoolean( ViewPastCartsActivity.IsFinishedParamName, false );
                    newFragment.setArguments( bundle );
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.dist_fragment_container, newFragment).commit();
                } else if (v.getId() == R.id.orders_btn_tab_past) {
                    mLLPastTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mLLCurrentTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mBtnPastTab.setTextColor(getResources().getColor(R.color.color_first));
                    mBtnCurrentTab.setTextColor(getResources().getColor(R.color.color_black90));

                    OrderPastFragment newFragment = OrderPastFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.dist_fragment_container, newFragment).commit();
                }
                _currentTabBtnId = v.getId();
            }
        }
    };

    void showOrder(String serial){
            Intent showOrder = new Intent(this, ViewPastCartsActivity.class);
            showOrder.putExtra(ViewPastCartsActivity.CartSerialParamName, serial);
            StaticHelperFunctions.openActivity(this, showOrder);

    }
}
