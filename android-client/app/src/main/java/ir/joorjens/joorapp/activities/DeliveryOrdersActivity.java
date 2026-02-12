package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.fragments.DeliveryOrdersListFragment;
import ir.joorjens.joorapp.fragments.DeliveryOrdersMapFragment;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

public class DeliveryOrdersActivity extends DelivererBaseActivity {

    private int mCurrentTabBtnId;
    private ButtonPlus mBtnOrdersMapTab, mBtnOrdersListTab;
    private LinearLayout mLlOrdersMapTab, mLlOrdersListTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_delivery_orders);

        mCurrentTabBtnId = R.id.del_order_list_tab;
        if (savedInstanceState == null) {
            DeliveryOrdersListFragment currentFragment = new DeliveryOrdersListFragment();
//            currentFragment.setItemClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showOrder( v.getTag().toString() );
//                }
//            });
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.del_orders_fragment_container, currentFragment).commit();
        }

        mBtnOrdersListTab = (ButtonPlus) findViewById(R.id.del_order_list_tab);
        mBtnOrdersMapTab = (ButtonPlus) findViewById(R.id.del_order_map_tab);
        mBtnOrdersListTab.setOnClickListener(changeTab);
        mBtnOrdersMapTab.setOnClickListener(changeTab);
        mBtnOrdersListTab.setTextColor(getResources().getColor(R.color.color_first));
        mBtnOrdersMapTab.setTextColor(getResources().getColor(R.color.color_black90));

        mLlOrdersListTab = (LinearLayout) findViewById(R.id.del_ll_list_tab);
        mLlOrdersMapTab = (LinearLayout) findViewById(R.id.del_ll_map_tab);
        mLlOrdersListTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
        mLlOrdersMapTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
    }

    private View.OnClickListener changeTab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( v.getId() != mCurrentTabBtnId) {
                if (v.getId() == R.id.del_order_list_tab) {
                    mLlOrdersListTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mLlOrdersMapTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mBtnOrdersListTab.setTextColor(getResources().getColor(R.color.color_first));
                    mBtnOrdersMapTab.setTextColor(getResources().getColor(R.color.color_black90));

                    DeliveryOrdersListFragment newFragment = new DeliveryOrdersListFragment();
//                    newFragment.setItemClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            showOrder( v.getTag().toString() );
//                        }
//                    });
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.del_orders_fragment_container, newFragment).commit();
                }

                else if (v.getId() == R.id.del_order_map_tab) {
                    mLlOrdersMapTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mLlOrdersListTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mBtnOrdersMapTab.setTextColor(getResources().getColor(R.color.color_first));
                    mBtnOrdersListTab.setTextColor(getResources().getColor(R.color.color_black90));

                    DeliveryOrdersMapFragment newFragment = new DeliveryOrdersMapFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.del_orders_fragment_container, newFragment).commit();
                }
                mCurrentTabBtnId = v.getId();
            }
        }
    };

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.DeliveryOrders;
    }

    @Override
    public String getActivityTitle() {
        return "لیست سفارشات";
    }

//    void showOrder(String serial){
//        Intent showOrder = new Intent(this, DeliveryViewCartDistributorActivity.class);
//        showOrder.putExtra(DeliveryViewCartDistributorActivity.CartSerialParamName, serial);
//        StaticHelperFunctions.openActivity(this, showOrder);
//    }
}
