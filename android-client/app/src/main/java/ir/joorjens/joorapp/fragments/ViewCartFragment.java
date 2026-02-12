package ir.joorjens.joorapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.OrdersFilterListActivity;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.adapters.OrderDistributorExpandableListAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.OrderProductRow;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.CartStatusUpdateResponse;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.PromotionAPIs;

public class ViewCartFragment extends Fragment  implements ActivityServiceListener  {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();

        if(!mMainScroll.canScrollVertically(1)){
            mRefreshLayout.setEnabled(true);
        }

        _orderListTbTvOrderNumberLable.setVisibility(View.INVISIBLE);
        _orderListTbTvOrderNumberValue.setVisibility(View.INVISIBLE);

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

    private enum ShowDataMode{
        Distributor, AllEditMode, AllFinalizedMode, Deliverer
    }

    private String _cartSerial;
    private Cart _cart;
    private int lastExpandedPosition = -1;

    public static String DistributorOrAllParamName = "distOrAll"; //A tag name for boolean param in bundle showing if layout should categorize distributors
    //public static String OrderSerialParamName = "orderSerial"; //A tag name for boolean param in bundle showing if layout should categorize distributors
    private boolean _distOrAll = false;
    private TextViewPlus _orderListTbTvOrderNumberValue;
    private TextViewPlus _orderListTbTvOrderNumberLable;
    private LinearLayout _ordersContainer;
    private ExpandableListView _expandableListView;
    private LinearLayout _orderLLSummaryEM;
    private LinearLayout _orderLLSummaryAM;
    private ButtonPlus _btnOrderPreviousPage;
    private ButtonPlus _btnOrderPastOrders;
    private ButtonPlus _btnOrderConfirm;
    private ButtonPlus _btnOrderShowByDistributor;
    private TextViewPlus _orderTvProfitAM;
    private TextViewPlus _orderTvProfitEM;
    private TextViewPlus _orderTvPriceAM;
    private TextViewPlus _orderTvOrderPrice;
    private TextViewPlus _orderTvCredit;
    private LinearLayout _orderLlCreditContainer;
    private TextViewPlus _orderTvFinalPrice;
    private NestedScrollView mMainScroll;
    private SwipeRefreshLayout mRefreshLayout;

    public ViewCartFragment() {

    }

    public static ViewCartFragment newInstance(){
        ViewCartFragment fragment = new ViewCartFragment();
        return fragment;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            decideGetOrSearchCart();
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_cart, container, false);

        mMainScroll = (NestedScrollView) v.findViewById(R.id.del_order_scroll);
        mMainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == 0){
                    mRefreshLayout.setEnabled(true);
                }else{
                    mRefreshLayout.setEnabled(false);
                }
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.del_view_cart_dist_refresh);
        mRefreshLayout.setOnRefreshListener(onRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        _orderLLSummaryEM = (LinearLayout) v.findViewById(R.id.order_ll_summary_em);
        _orderLLSummaryAM = (LinearLayout) v.findViewById(R.id.order_ll_summary_am);
        _ordersContainer = (LinearLayout) v.findViewById(R.id.del_order_products_container);
        _expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView_order_distributors);
        _orderListTbTvOrderNumberValue = (TextViewPlus) v.findViewById(R.id.order_list_tb_tv_order_number_value);
        _orderListTbTvOrderNumberLable = (TextViewPlus) v.findViewById(R.id.order_list_tb_tv_order_number_label);
        _btnOrderPreviousPage = (ButtonPlus) v.findViewById(R.id.order_btn_previous_page);
        _btnOrderPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        _btnOrderConfirm = (ButtonPlus) v.findViewById(R.id.order_btn_confirm);
        _btnOrderPastOrders = (ButtonPlus) v.findViewById(R.id.order_btn_past_orders);
        _btnOrderPastOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderListIntent = new Intent(getContext(), OrdersFilterListActivity.class);
                orderListIntent.putExtra(OrdersFilterListActivity.KeyShowPastOrdersTab, true);
                StaticHelperFunctions.openActivity(getContext(), orderListIntent);
            }
        });
        _btnOrderConfirm.setOnClickListener(confirmOrderButtonClicked);
        _btnOrderShowByDistributor = (ButtonPlus) v.findViewById(R.id.order_btn_show_by_distributor);
        _btnOrderShowByDistributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                fillData(ShowDataMode.Distributor);
            }
        });

        _orderTvPriceAM = (TextViewPlus) v.findViewById(R.id.order_price_am);
        _orderTvProfitAM = (TextViewPlus) v.findViewById(R.id.order_profit_am);
        _orderTvProfitEM = (TextViewPlus) v.findViewById(R.id.order_profit_em);

        _orderTvOrderPrice = (TextViewPlus) v.findViewById(R.id.order_price);
        _orderTvCredit = (TextViewPlus) v.findViewById(R.id.order_credit);
        _orderLlCreditContainer = (LinearLayout) v.findViewById(R.id.order_credit_container);
        _orderLlCreditContainer.setVisibility(View.GONE);
        _orderTvFinalPrice = (TextViewPlus) v.findViewById(R.id.order_price_final);

        Bundle args = getArguments();
        _cartSerial = args.getString(ViewPastCartsActivity.CartSerialParamName, "");
        _distOrAll = args.getBoolean(ViewPastCartsActivity.DistOrAllParamName, false);

        decideGetOrSearchCart();

        return v;
    }

    private void decideGetOrSearchCart(){
        if(_cartSerial.isEmpty()){
            _cart = CacheContainer.get().getCart();
            clearData();
            if(_cart != null) {
                if (_distOrAll)
                    fillData(ShowDataMode.Distributor);
                else
                    fillData(ShowDataMode.AllEditMode);
            }
        }
        else{
            if(getActivity().getClass() == ViewPastCartsActivity.class)
            {
                this._cart = ((ViewPastCartsActivity)getActivity()).getExistingCart( _cartSerial );
                if( this._cart == null){
                    HashMap<String, String> params = new HashMap<>();
                    if(!_cartSerial.isEmpty())
                        params.put( CartAPIs.SerialParamName, _cartSerial);
                    CartAPIs.searchCart(this, Authenticator.loadAuthenticationToken(),params, 0);
                }
                else{
                    clearData();
                    fillData(ShowDataMode.AllFinalizedMode);
                }
            }
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchCart){
            clearData();
            ResultList<Cart> res = (ResultList<Cart>) data;
            if(res.getTotal() > 0) {
                _cart =  res.getResult().get(0);
                fillData(ShowDataMode.AllFinalizedMode);
            }
        }
        else if(apiCode == APICode.getCart ){
            //

        }
        else if(apiCode == APICode.addCart ){
            clearData();
            _cart = (Cart) data;

            fillData(ShowDataMode.AllEditMode);
        }
        else if( apiCode == APICode.removeCart){
            clearData();
            _cart = (Cart) data;
            fillData(ShowDataMode.AllEditMode);
        }
        else if(apiCode == APICode.finalizeCart){
            clearData();
            _cart = (Cart) data;
            fillData(ShowDataMode.AllFinalizedMode);
        }

        else if(apiCode == APICode.searchPromotion)
        {
            ServiceResponse resp = (ServiceResponse) data;
            StaticHelperFunctions.showYesNoDialog(getActivity(),resp.getMessage(),
                    StaticHelperFunctions.MessageType.Info,YesListener, NoListener,
                    getResources().getString( R.string.label_add_new_product),
                    getResources().getString( R.string.label_continue_without_adding_product));
        }
        else if(apiCode == APICode.updateCartEntity)
        {
            CartStatusUpdateResponse cartState = (CartStatusUpdateResponse) data;
            if(cartState.getCartDistributor()){
                //TODO rate2
                Log.d("****************" , "show god damn rate page for god sake");
            }
        }

        if(_cart!=null){
            _cartSerial = _cart.getSerial() == null ? "" : _cart.getSerial();
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchPromotion)
        {
            //No promotion, so we can finalize order :)
            CartAPIs.finalizeCart( this, Authenticator.loadAuthenticationToken(), 0);
        }
        else if (apiCode == APICode.removeCart){
            ServiceResponse sr = (ServiceResponse)data;
            if(sr.getCode() == 4110) // empty cart
            {
                clearData();
                //((HomeBaseActivity)getActivity()).setCartCount( 0);
                // set visibility to GONE

            }
        }

        else {
            StaticHelperFunctions.showMessage(this.getActivity(), ((ServiceResponse) data).getMessage(), StaticHelperFunctions.MessageType.Alert);
            if(apiCode == APICode.addCart){
                fillData(ShowDataMode.AllEditMode);
            }
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
        if(apiCode == APICode.addCart){
            fillData(ShowDataMode.AllEditMode);
        }

    }

    private View.OnClickListener confirmOrderButtonClicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            StaticHelperFunctions.showYesNoDialog(getActivity(), "آیا از ثبت سفارش اطمینان دارید؟",
                    StaticHelperFunctions.MessageType.Warning, YesOrder, NoOrder, "بله","خیر");

        }
    };
    SweetAlertDialog.OnSweetClickListener YesOrder = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            PromotionAPIs.searchPromotion(ViewCartFragment.this ,Authenticator.loadAuthenticationToken(),_cart.getCartPrice().getAllPrice().toString(),0);
            sweetAlertDialog.dismissWithAnimation();
        }
    };
    SweetAlertDialog.OnSweetClickListener NoOrder = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismissWithAnimation();
        }
    };

    private void fillData(ShowDataMode mode){

        //////
        clearData();

        if(_cart.getCartPrice().getCount() == 0){
            return;
        }

        _orderListTbTvOrderNumberLable.setVisibility(View.VISIBLE);
        _orderListTbTvOrderNumberValue.setVisibility(View.VISIBLE);
        _orderListTbTvOrderNumberValue.setText(_cart.getSerial());

        _orderTvPriceAM.setText("مبلغ سفارش: "+String.format("%,d", _cart.getCartPrice().getAllPriceDiscount()) + " تومان");
        _orderTvProfitAM.setText("سود سفارش: "+String.format("%,d", _cart.getCartPrice().getYourProfit()) + " تومان");
        _orderTvProfitEM.setText(String.format("%,d", _cart.getCartPrice().getYourProfit()) + " تومان");

        _orderTvOrderPrice.setText(String.format("%,d", _cart.getCartPrice().getAllPrice()) + " تومان");
        _orderTvCredit.setText(String.format("%,d", Authenticator.loadAccount().getCredit()) + " تومان");
        if(Authenticator.loadAccount().getCredit() > 0)
            _orderLlCreditContainer.setVisibility(View.VISIBLE);
        _orderTvFinalPrice.setText(String.format("%,d", _cart.getCartPrice().getAllPriceDiscount()) + " تومان");

        if(mode == ShowDataMode.AllEditMode){
            _orderLLSummaryEM.setVisibility(View.VISIBLE);
            fillDataByAllProducts(true);
        }
        else if(mode == ShowDataMode.AllFinalizedMode){
            _orderLLSummaryAM.setVisibility(View.VISIBLE);
            fillDataByAllProducts(false);
        }
        else if(mode == ShowDataMode.Distributor){
            _expandableListView.setVisibility(View.VISIBLE);
            fillDataByDistributors();
        }
    }

    private void fillDataByAllProducts(boolean isEditMode){

        Integer rowNumber = 1;
        for ( CartDistributor cartDist : _cart.getDistributorSet()) {
            for (CartDistributorProduct cartProduct : cartDist.getPackageSet()) {
                OrderProductRow row = new OrderProductRow( this.getContext(),this , isEditMode);
                row.setRowValues( cartProduct , rowNumber++);
                _ordersContainer.addView( row );
            }

        }
    }

    private void fillDataByDistributors(){

        OrderDistributorExpandableListAdapter expandableListAdapter;
        expandableListAdapter = new OrderDistributorExpandableListAdapter(this.getContext(), _cart.getDistributorSet());
        expandableListAdapter.setServiceListener( this );
        //expandableListAdapter.addSumRow(new CartDistributorProduct());// TODO add profit and price?

        _expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    _expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        _expandableListView.setAdapter(expandableListAdapter);

    }

    private void clearData(){
        _ordersContainer.removeAllViews();
        ExpandableListAdapter expandableListAdapter = null;
        _expandableListView.setAdapter(expandableListAdapter);
        _orderLLSummaryEM.setVisibility(View.GONE);
        _orderLLSummaryAM.setVisibility(View.GONE);
        _expandableListView.setVisibility(View.GONE);
    }

    private SweetAlertDialog.OnSweetClickListener YesListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismissWithAnimation();
        }
    };

    private SweetAlertDialog.OnSweetClickListener NoListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            CartAPIs.finalizeCart( ViewCartFragment.this, Authenticator.loadAuthenticationToken(), 0);
            sweetAlertDialog.dismissWithAnimation();
        }
    };
}
