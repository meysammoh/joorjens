package ir.joorjens.joorapp.activities;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.DeliveryOrderProductRow;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.CartDistributorProduct;

public class DeliveryViewCartDistributorActivity extends DelivererBaseActivity {

    private CartDistributor mCartDist;

    private NestedScrollView mMainScroll;
    private RelativeLayout mRlDelTopContent;
    private TextViewPlus mTvDelOrderListTitleBarTitle, mTvDelOrderListTitleBarSerial;
    private TextViewPlus mTvDelStoreAddress, mTvDelStoreAdminName, mTvDelStorePhone;
    private TextViewPlus mTvDelSumOrderPrice, mTvDelSumOrderCash,mTvDelSumOrderCheck;
    private ButtonPlus mBtnDelConfirmDelivery;
    private LinearLayout mLLDelOrderProductsContainer;
    public static String cartDistParamName = "cartDistObj";

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
    public void onPause() {
        _isActive = false;
        super.onPause();
    }

    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.DeliveryOrderDetail;
    }

    @Override
    public String getActivityTitle() {
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_delivery_view_cart_distributor);

        mRlDelTopContent = (RelativeLayout) findViewById(R.id.del_order_store_info);
        mTvDelOrderListTitleBarTitle = (TextViewPlus) findViewById(R.id.del_order_list_title_bar_tv_title);
        mTvDelOrderListTitleBarSerial = (TextViewPlus) findViewById(R.id.del_order_list_title_bar_tv_serial);
        mTvDelStoreAddress = (TextViewPlus) findViewById(R.id.del_store_address_value);
        mTvDelStoreAdminName = (TextViewPlus) findViewById(R.id.del_store_admin_value);
        mTvDelStorePhone = (TextViewPlus) findViewById(R.id.del_store_phone_value);
        mTvDelSumOrderPrice = (TextViewPlus) findViewById(R.id.del_sum_order_price);
        mTvDelSumOrderCash = (TextViewPlus) findViewById(R.id.del_sum_order_cash);
        mTvDelSumOrderCheck = (TextViewPlus) findViewById(R.id.del_sum_order_check);

        mLLDelOrderProductsContainer = (LinearLayout) findViewById(R.id.del_order_products_container);

        mBtnDelConfirmDelivery = (ButtonPlus) findViewById(R.id.del_sum_order_confirm_btn);
        mBtnDelConfirmDelivery.setOnClickListener(onConfirmDeliver);

        mCartDist = (CartDistributor) getIntent().getSerializableExtra(cartDistParamName);

        fillData();
    }

    private void fillData(){
        if(mCartDist!= null){
            mTvDelOrderListTitleBarTitle.setText("لیست تحویل " + mCartDist.getStoreName());
            mTvDelOrderListTitleBarSerial.setText(mCartDist.getSerial());

            mTvDelStoreAddress.setText(mCartDist.getStoreAddress());
            mTvDelStoreAdminName.setText(mCartDist.getStoreManagerName());
            mTvDelStorePhone.setText(mCartDist.getStoreManagerMobile());

            Integer rowNumber = 1;
            for (CartDistributorProduct cartProduct : mCartDist.getPackageSet()) {
                DeliveryOrderProductRow row = new DeliveryOrderProductRow( this,this);
                row.setRowValues( cartProduct , rowNumber++);
                mLLDelOrderProductsContainer.addView( row );
            }

            mTvDelSumOrderPrice.setText(String.format("%,d", mCartDist.getCartPrice().getAmountMustPay()));
            mTvDelSumOrderCash.setText(String.format("%,d", mCartDist.getCartPrice().getAmountCache()));
            mTvDelSumOrderCheck.setText(String.format("%,d", mCartDist.getCartPrice().getAmountCheck()));
        }
    }

    private View.OnClickListener onConfirmDeliver = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO complete action
        }
    };
}
