package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.customViews.OrderSummaryRow;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CartAPIs;

public class ViewPastCartsAllFragment extends Fragment implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();

        if(!mMainScroll.canScrollVertically(1)){
            mRefreshLayout.setEnabled(true);
        }

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

    private NestedScrollView mMainScroll;
    private SwipeRefreshLayout mRefreshLayout;

    private LinearLayout _listContainer;

    public ViewPastCartsAllFragment() {

    }

    public static ViewPastCartsAllFragment newInstance(){
        ViewPastCartsAllFragment fragment = new ViewPastCartsAllFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_past_carts_by_date, container, false);

        mMainScroll = (NestedScrollView) v.findViewById(R.id.past_carts_by_date_scroll);
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
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.past_carts_by_date_refresh);
        mRefreshLayout.setOnRefreshListener(onRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        _listContainer = (LinearLayout) v.findViewById(R.id.container_fragment_past_carts);
        CartAPIs.searchCart(this, Authenticator.loadAuthenticationToken(),new HashMap<String, String>(), 0);

        return v;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            CartAPIs.searchCart(ViewPastCartsAllFragment.this,
                    Authenticator.loadAuthenticationToken(),new HashMap<String, String>(), 0);
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchCart){
            ResultList<Cart> result = (ResultList<Cart>) data;
            HashMap<String,ArrayList<Cart>> byRange = new HashMap<>();
            TreeMap<String,Cart> bySerial = new TreeMap<>();
            //TODO: make rangeList by Range!
            String tempkey = "همه";
            byRange.put( tempkey, new ArrayList<Cart>() );
            for (Cart cart :
                    result.getResult()) {
                bySerial.put( cart.getSerial(), cart);
                byRange.get(tempkey).add( cart );

            }
            ((ViewPastCartsActivity)getActivity()).setAllCarts( byRange, bySerial  );

            _listContainer.removeAllViews();

            for (String key :
                    byRange.keySet()) {
                int count = 0;
                long price = 0;
                long profit = 0;
                for (Cart cart :
                        byRange.get(key)) {
                    count++;
                    price+=cart.getCartPrice().getAllPrice();
                    profit+=cart.getCartPrice().getYourProfit();
                }

                OrderSummaryRow summaryRow = new OrderSummaryRow(this.getContext());
                summaryRow.setRowValues( key, count, price, profit);
                _listContainer.addView( summaryRow );
            }
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
