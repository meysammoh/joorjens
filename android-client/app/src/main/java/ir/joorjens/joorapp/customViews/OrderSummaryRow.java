package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.models.CartDistributorProduct;

/**
 * Created by Meysam on 04.03.18.
 */

public class OrderSummaryRow extends TableRow {
    private TextViewPlus _title;
    private TextViewPlus _count;
    private TextViewPlus _cost;
    private TextViewPlus _profit;

    public OrderSummaryRow(Context context) {
        super(context);
        init(null);
    }

    public OrderSummaryRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_order_summary_row, null);
        _title = (TextViewPlus) view.findViewById(R.id.tv_order_summary_row_title);
        _count = (TextViewPlus) view.findViewById(R.id.tv_order_summary_row_counts );
        _cost = (TextViewPlus) view.findViewById(R.id.tv_order_summary_row_cost );
        _profit = (TextViewPlus) view.findViewById(R.id.tv_order_summary_row_profit );

        addView(view);
    }
    public void setRowValues( String title, int count, long cost, long profit){
        _title.setText(title);
        _count.setText(count + " سفارش");
        _cost.setText(String.format("%,d",cost) + " تومان");
        _profit.setText(String.format("%,d",profit) + " تومان");
    }
    void setColor(int colorId) {


    }

}
