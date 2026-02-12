package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.TreeMap;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.PersianDatePicker;
import ir.joorjens.joorapp.customViews.TitleBar2;
import ir.joorjens.joorapp.fragments.AlertDialogFragment;
import ir.joorjens.joorapp.fragments.OrderPastFragment;
import ir.joorjens.joorapp.fragments.ViewCartFragment;
import ir.joorjens.joorapp.fragments.ViewPastCartsAllByMonthFragment;
import ir.joorjens.joorapp.fragments.ViewPastCartsAllFragment;
import ir.joorjens.joorapp.fragments.ViewPastCartsByDateFragment;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.SolarCalendar;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.smartlab.persiandatepicker.util.PersianCalendar;

/**
 * Created by meysammoh on 16.03.18.
 */

public class ViewPastCartsActivity extends HomeBaseActivity implements ActivityServiceListener{

//    SwipeRefreshLayout.OnRefreshListener onRefreshCart = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            BaseActivity.getRefreshLayout().setRefreshing(true);
//            StaticHelperFunctions.recreate(ViewPastCartsActivity.this, true);
//            BaseActivity.getRefreshLayout().setRefreshing(false);
//        }
//    };

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

    public static String CartSerialParamName="CartSerialString";
    public static String DistOrAllParamName="DistOrAll";
    public static String TimeFromParamName="TimeFrom";
    public static String IsFinishedParamName="finished";
    public static String TimeToParamName="TimeTo";
    public static String TimeRangeParamName="RangeName";
    public static String AllCartsParamName ="AllBoolean";
    public static String AllCartsByMonthParamName ="AllByMonthBoolean";

    private boolean _isAllCarts;
    private LinearLayout summaryHeaderLayout;
    private String _cartSerial;
    private Boolean _distOrAll;
    private String _timeFrom;
    private String _timeTo;


    private HashMap<String, ArrayList<Cart>> _allCartsByRange; // a map from a range name to list of carts, e.g. Key: My Favorite, Value: list of favorites
    private TreeMap<String, Cart> _allCartsBySerial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_view_past_carts, ActivityKeys.ViewPastCarts);

        summaryHeaderLayout = (LinearLayout) findViewById(R.id.container_past_carts_summary_header);
        summaryHeaderLayout.setVisibility(View.GONE);

        //BaseActivity.setOnRefreshListener(onRefreshCart);

    }
    @Override
    protected void onResume() {
        super.onResume();

        _isActive = true;

        _isAllCarts = false;
        if(getIntent().getExtras() != null){

            if(getIntent().getExtras().containsKey(TimeFromParamName) ||
                    getIntent().getExtras().containsKey(TimeToParamName)){
                _timeFrom = getIntent().getExtras().getString(TimeFromParamName,"");
                _timeTo = getIntent().getExtras().getString(TimeToParamName,"");
                Bundle bundle = new Bundle();
                if(!_timeFrom.isEmpty())
                    bundle.putString( TimeFromParamName, _timeFrom);
                if(!_timeTo.isEmpty())
                    bundle.putString( TimeToParamName, _timeTo);
                String rangeName = getIntent().getExtras().getString(TimeRangeParamName,"");
                if(!rangeName.isEmpty())
                    bundle.putString( TimeRangeParamName, rangeName);
                if(getIntent().getExtras().containsKey( IsFinishedParamName ))
                    bundle.putBoolean( IsFinishedParamName, getIntent().getExtras().getBoolean(IsFinishedParamName));
                ViewPastCartsByDateFragment newFragment = ViewPastCartsByDateFragment.newInstance();
                newFragment.setItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOrder( v.getTag().toString());
                    }
                });
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                newFragment.setArguments( bundle );
                ft.replace(R.id.fragment_past_carts_container, newFragment).commit();

            }
            else if(getIntent().getExtras().containsKey(AllCartsParamName)){
                _isAllCarts = getIntent().getExtras().getBoolean(AllCartsParamName);
                ViewPastCartsAllFragment newFragment = ViewPastCartsAllFragment.newInstance();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_past_carts_container, newFragment).commit();
            }
            else if(getIntent().getExtras().containsKey(AllCartsByMonthParamName)){
                //_isAllCarts = getIntent().getExtras().getBoolean(AllCartsParamName);
                ViewPastCartsAllByMonthFragment newFragment = ViewPastCartsAllByMonthFragment.newInstance();
                newFragment.setItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchOrder(v.getTag().toString());
                    }
                });
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_past_carts_container, newFragment).commit();
            }
            else if(getIntent().getExtras().containsKey(DistOrAllParamName)){
                ViewCartFragment newFragment = ViewCartFragment.newInstance();
                _distOrAll = getIntent().getExtras().getBoolean(DistOrAllParamName);
                Bundle bundle = new Bundle();
                bundle.putBoolean(DistOrAllParamName, _distOrAll);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                newFragment.setArguments( bundle );
                ft.replace(R.id.fragment_past_carts_container, newFragment).commit();
            }
            else if(getIntent().getExtras().containsKey(CartSerialParamName)){
                ViewCartFragment newFragment = ViewCartFragment.newInstance();
                _cartSerial = getIntent().getExtras().getString(CartSerialParamName);
                Bundle bundle = new Bundle();
                bundle.putString(CartSerialParamName, _cartSerial);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                newFragment.setArguments( bundle );
                ft.replace(R.id.fragment_past_carts_container, newFragment).commit();
            }
        }
        else {
            _cartSerial = "";
            _distOrAll = false;
            ViewCartFragment newFragment = ViewCartFragment.newInstance( );
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            if(getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey(CartSerialParamName)) {
                    summaryHeaderLayout.setVisibility(View.GONE);
                    _cartSerial = getIntent().getExtras().getString(CartSerialParamName);
                    bundle.putString(CartSerialParamName, _cartSerial);

                }
                if (getIntent().getExtras().containsKey(DistOrAllParamName)) {
                    summaryHeaderLayout.setVisibility(View.GONE);
                    _distOrAll = getIntent().getExtras().getBoolean(DistOrAllParamName);
                    bundle.putBoolean(DistOrAllParamName, _distOrAll);
                }
            }
            newFragment.setArguments( bundle );
            ft.replace(R.id.fragment_past_carts_container, newFragment).commit();
        }
    }

    void searchOrder(String date){
        // date format 139704  tire 1397
        // find dateFrom and dateTo
        Integer y = Integer.valueOf(date.substring(0,4));
        Integer m = Integer.valueOf(date.substring(4));
        PersianCalendar pc = new PersianCalendar();
        pc.setPersianDate(y,m,1);
        long dateFrom = pc.getTimeInMillis() / 1000;
        int endDayOfMonth = m > 6 ? 30 : 31;
        //TODO find real days in month
        pc.setPersianDate(y,m,endDayOfMonth);
        long dateTo = pc.getTimeInMillis() / 1000;

        Intent viewCartIntent = new Intent(ViewPastCartsActivity.this, ViewPastCartsActivity.class);

        viewCartIntent.putExtra(ViewPastCartsActivity.TimeFromParamName, dateFrom+"");
        viewCartIntent.putExtra(ViewPastCartsActivity.TimeToParamName, dateTo+"");
        String title = StaticHelperFunctions.PersianMonth.get(m) + " ماه " + y;
        viewCartIntent.putExtra(ViewPastCartsActivity.TimeRangeParamName, title);
        //viewCartIntent.putExtra(ViewPastCartsActivity.AllCartsByMonthParamName, allPastOrdersByMonth);

        //finish();
        StaticHelperFunctions.openActivity(this, viewCartIntent);

    }

    void showOrder(String serial ){
        Bundle bundle = new Bundle();
        if (!serial.isEmpty())
            bundle.putString(ViewPastCartsActivity.CartSerialParamName, serial);
        ViewCartFragment newFragment = ViewCartFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        ft.replace(R.id.fragment_past_carts_container, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public Cart getExistingCart( String serial){
        if(_allCartsBySerial != null)
            return _allCartsBySerial.get(serial);
        return null;
    }

    public void setAllCarts(HashMap<String, ArrayList<Cart>> allCartsByRange,
                            TreeMap<String, Cart> allCartsBySerial){
        if(_allCartsBySerial == null )
            _allCartsBySerial = new TreeMap<>();
        if(_allCartsByRange == null )
            _allCartsByRange = new HashMap<>();
        if(allCartsBySerial != null )
            _allCartsBySerial.putAll( allCartsBySerial );
        if(allCartsByRange != null )
            _allCartsByRange.putAll( allCartsByRange );
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
