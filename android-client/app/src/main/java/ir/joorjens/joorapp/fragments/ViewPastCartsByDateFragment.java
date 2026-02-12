package ir.joorjens.joorapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.BaseActivity;
import ir.joorjens.joorapp.activities.OrdersFilterListActivity;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.customViews.OrderRow;
import ir.joorjens.joorapp.customViews.OrderSummaryRow;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.SolarCalendar;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CartAPIs;

public class ViewPastCartsByDateFragment extends Fragment  implements ActivityServiceListener {


    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();

        if(!mMainScroll.canScrollVertically(1)){
            //BaseActivity.setSwipeEnabled(true);
            mRefreshLayout.setEnabled(true);
        }
//        BaseActivity.setOnRefreshListener(onRefresh);


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

    private HashMap<String, String> params;
    private NestedScrollView mMainScroll;
    private SwipeRefreshLayout mRefreshLayout;

    private String _rangeName = "";
    private LinearLayout _listContainer;
    private View.OnClickListener onItemClickListener;
    private SweetAlertDialog mLoadingDialog;
    public ViewPastCartsByDateFragment() {

    }

    public static ViewPastCartsByDateFragment newInstance(){
        ViewPastCartsByDateFragment fragment = new ViewPastCartsByDateFragment();
        return fragment;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            CartAPIs.searchCart(ViewPastCartsByDateFragment.this, Authenticator.loadAuthenticationToken(), params, 0);
            mRefreshLayout.setRefreshing(false);
        }
    };

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
        params = new HashMap<>();
        String from = "", to = "";
        Boolean finished = null;
        if( getArguments().containsKey( ViewPastCartsActivity.IsFinishedParamName) )
            finished = getArguments().getBoolean( ViewPastCartsActivity.IsFinishedParamName);

        from = getArguments().getString( ViewPastCartsActivity.TimeFromParamName,"");
        to = getArguments().getString( ViewPastCartsActivity.TimeToParamName,"");
        _rangeName = getArguments().getString( ViewPastCartsActivity.TimeRangeParamName,"");
        if(!from.isEmpty())
            params.put( CartAPIs.TimeFromParamName, from);
        if(!to.isEmpty())
            params.put( CartAPIs.TimeToParamName, to);
        if( finished != null )
            params.put( CartAPIs.IsFinishedParamName, finished.toString());
        CartAPIs.searchCart(this, Authenticator.loadAuthenticationToken(), params, 0);
        return v;
    }
    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchCart){
            ResultList<Cart> result = (ResultList<Cart>) data;
            TreeMap<String,Cart> bySerial = new TreeMap<>();
            ArrayList<Cart> allCarts = result.getResult();

            for(int i = 0; i < allCarts.size(); i++){
                bySerial.put(allCarts.get(i).getSerial(), allCarts.get(i));
            }
            if(getActivity().getClass() == ViewPastCartsActivity.class)
                ((ViewPastCartsActivity)getActivity()).setAllCarts( null, bySerial  );

            _listContainer.removeAllViews();
            int count = 0;
            long price = 0;
            long profit = 0;

            for (int i = 0; i < allCarts.size(); i++){
                final Cart cart = allCarts.get(i);
                    count++;
                    price+=cart.getCartPrice().getAllPrice();
                    profit+=cart.getCartPrice().getYourProfit();

                OrderRow orderRow = new OrderRow(this.getContext());
                Calendar cal = Calendar.getInstance();
                long timeMilli = cart.getCreatedTime()*1000l;
                cal.setTimeInMillis(timeMilli);
                SolarCalendar jalCal = new SolarCalendar(cal.getTime());

                orderRow.setRowValues( cart.getSerial(), price+"", profit+"",
                        getResources().getString(R.string.word_date) + ": " + jalCal.getSolarDateString() , onItemClickListener);
                _listContainer.addView( orderRow );
            }
            OrderSummaryRow summaryRow = new OrderSummaryRow(this.getContext());
            summaryRow.setRowValues( getResources().getString(R.string.word_orders) +" "+ _rangeName , count, price, profit);
            _listContainer.addView( summaryRow, 0 );
        }
    }

    public void setItemClickListener(View.OnClickListener itemClickListener){
        onItemClickListener = itemClickListener;
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        //Toast.makeText(this.getActivity(),((ServiceResponse)data).getMessage(), Toast.LENGTH_LONG).show();
        StaticHelperFunctions.showMessage(getActivity().findViewById(R.id.rootView),
                ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
