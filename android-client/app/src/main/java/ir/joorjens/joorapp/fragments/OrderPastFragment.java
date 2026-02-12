package ir.joorjens.joorapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ShowFavoritesActivity;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

public class OrderPastFragment extends Fragment {



    public OrderPastFragment() {

    }

    public static OrderPastFragment newInstance(){
        OrderPastFragment fragment = new OrderPastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_orders, container, false);

        ButtonPlus twoWeeksButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_two_weeks);
        ButtonPlus oneMonthButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_one_month);
        ButtonPlus threeMonthButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_three_months);
        ButtonPlus sixMonthButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_six_months);
        ButtonPlus oneYearButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_one_year);
        ButtonPlus favoriteButton = (ButtonPlus) v.findViewById(R.id.btn_past_orders_favorite);
        ButtonPlus allByMonth = (ButtonPlus) v.findViewById(R.id.btn_past_orders_all_by_month);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(v.getId());
            }
        };

        twoWeeksButton.setOnClickListener( listener);
        oneMonthButton.setOnClickListener( listener);
        threeMonthButton.setOnClickListener( listener);
        sixMonthButton.setOnClickListener( listener);
        oneYearButton.setOnClickListener( listener);
        favoriteButton.setOnClickListener( listener);
        allByMonth.setOnClickListener(listener);

        return v;
    }

    private void openFragment(int buttonId){

        String from="";
        String to="";
        String rangeName = "";
        boolean favoriteView = false;
        boolean allPastOrdersByMonth = false;
        switch (buttonId){
            case R.id.btn_past_orders_two_weeks:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                from = StaticHelperFunctions.getTimeInSeconds(-14)+"";
                rangeName = getResources().getString(R.string.label_past_orders_two_weeks);
                break;
            case R.id.btn_past_orders_one_month:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                from = StaticHelperFunctions.getTimeInSeconds(-30)+"";
                rangeName = getResources().getString(R.string.label_past_orders_one_month);
                break;
            case R.id.btn_past_orders_three_months:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                from = StaticHelperFunctions.getTimeInSeconds(-90)+"";
                rangeName = getResources().getString(R.string.label_past_orders_three_months);
                break;
            case R.id.btn_past_orders_six_months:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                from = StaticHelperFunctions.getTimeInSeconds(-180)+"";
                rangeName = getResources().getString(R.string.label_past_orders_six_months);
                break;
            case R.id.btn_past_orders_one_year:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                from = StaticHelperFunctions.getTimeInSeconds(-30)+"";
                rangeName = getResources().getString(R.string.label_past_orders_one_year);
                break;
            case R.id.btn_past_orders_all_by_month:
                allPastOrdersByMonth = true;
                rangeName = getResources().getString(R.string.label_past_orders_by_month);
                break;
            case R.id.btn_past_orders_favorite:
                //to = StaticHelperFunctions.getTimeInSeconds(0)+"";
                favoriteView = true;
                break;

        }

        if(!favoriteView) {
            Intent viewCartIntent = new Intent(OrderPastFragment.this.getActivity(), ViewPastCartsActivity.class);
            if (!from.isEmpty())
                viewCartIntent.putExtra(ViewPastCartsActivity.TimeFromParamName, from);
            if (!to.isEmpty())
                viewCartIntent.putExtra(ViewPastCartsActivity.TimeToParamName, to);
            if (!rangeName.isEmpty())
                viewCartIntent.putExtra(ViewPastCartsActivity.TimeRangeParamName, rangeName);
            viewCartIntent.putExtra(ViewPastCartsActivity.AllCartsByMonthParamName, allPastOrdersByMonth);

            StaticHelperFunctions.openActivity(getContext(), viewCartIntent);
        }
        else{
            Intent viewFavIntent = new Intent(OrderPastFragment.this.getActivity(), ShowFavoritesActivity.class);
            StaticHelperFunctions.openActivity(getContext(), viewFavIntent);
        }
    }

}
