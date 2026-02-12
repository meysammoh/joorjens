package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import ir.joorjens.joorapp.R;

/**
 * Created by meysammoh on 17.11.17.
 */

public class ProductPropertiesRow extends TableRow {
    TextViewPlus mPropertyTitle;
    TextViewPlus mPropertyValue;
    private LinearLayout mMainLayout;
    public ProductPropertiesRow(Context context) {
        super(context);
        init(null);
    }

    public ProductPropertiesRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_product_properties_row, null);
        mPropertyTitle = (TextViewPlus) view.findViewById(R.id.ppr_tv_title);
        mPropertyValue = (TextViewPlus) view.findViewById(R.id.ppr_tv_value);
        mMainLayout = (LinearLayout) view.findViewById(R.id.ppr_main_container);
        addView(view);
    }
    public void setRowValues( String title, String value){
        mPropertyValue.setText( value );
        mPropertyTitle.setText( title );
    }
    void setColor(int colorId, int colsCount) {
        int cId = colorId;
        if (colorId == -1) cId = R.color.discount_color;

        if (colsCount == 1)
            mPropertyTitle.setTextColor(ContextCompat.getColor(this.getContext(), cId));
        else if (colsCount == 2) {
            mPropertyTitle.setTextColor(ContextCompat.getColor(this.getContext(), cId));
            mPropertyValue.setTextColor(ContextCompat.getColor(this.getContext(), cId));
        }
    }
}
