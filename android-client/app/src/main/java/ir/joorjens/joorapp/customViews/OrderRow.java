package ir.joorjens.joorapp.customViews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.fragments.ViewCartFragment;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by Meysam on 04.03.18.
 */

public class OrderRow extends TableRow {
    private TextViewPlus _title;
    private TextViewPlus _count;
    private LinearLayout _llCount;
    private TextViewPlus _cost;
    private TextViewPlus _costTitle;
    private TextViewPlus _profit;
    private TextViewPlus _profitTitle;
    private TextViewPlus _date;
    private ButtonPlus _moreButton;

    public OrderRow(Context context) {
        super(context);
        init(null);
    }

    public OrderRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_order_row, null);
        _title = (TextViewPlus) view.findViewById(R.id.tv_order_row_title);
        _date = (TextViewPlus) view.findViewById(R.id.tv_order_row_date );
        _cost = (TextViewPlus) view.findViewById(R.id.tv_order_row_cost );
        _costTitle = (TextViewPlus) view.findViewById(R.id.tv_order_row_cost_title );
        _count = (TextViewPlus) view.findViewById(R.id.tv_order_row_count );
        _llCount = (LinearLayout) view.findViewById(R.id.ll_count);
        _llCount.setVisibility(GONE);
        _profit = (TextViewPlus) view.findViewById(R.id.tv_order_row_profit );
        _profitTitle = (TextViewPlus) view.findViewById(R.id.tv_order_row_profit_title );
        _moreButton = (ButtonPlus) view.findViewById(R.id.btn_order_more_details );

        addView(view);
    }
    public void setRowValues(final String orderSerial, String cost, String profit, String date,
                             OnClickListener moreClickListener){
        String title = getResources().getString(R.string.order_number_label )+ " "+orderSerial;
        _title.setText(title);
        _cost.setText(cost);
        _profit.setText(profit);
        if(date.isEmpty())
            _date.setVisibility(GONE);
        else
            _date.setText(date);
        _moreButton.setTag( orderSerial );
        _moreButton.setOnClickListener( moreClickListener );
    }

    public void setMonthRowValues(final String date, String count, String cost, String profit,
                             OnClickListener moreClickListener){
        String y = date.substring(0,4);
        Integer m = Integer.valueOf(date.substring(4));
        String title = StaticHelperFunctions.PersianMonth.get(m) + " ماه " + y;
        _title.setText(title);
        _cost.setText(cost);
        _llCount.setVisibility(VISIBLE);
        _count.setText(count);
        _profit.setText(profit);
        _date.setVisibility(GONE);
        _moreButton.setTag( date );
        _moreButton.setOnClickListener( moreClickListener );
        _costTitle.setText("هزینه سفارشات: ");
        _profitTitle.setText("سود سفارشات: ");
    }

    void setColor(int colorId) {


    }

}
