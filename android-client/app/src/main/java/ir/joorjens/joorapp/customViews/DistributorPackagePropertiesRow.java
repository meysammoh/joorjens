package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.NoData;

/**
 * Created by Mohsen on 17.11.17.
 */

public class DistributorPackagePropertiesRow extends TableRow {
    TextViewPlus mPropertyItemInside;
    TextViewPlus mPropertyCount;
    TextViewPlus mPropertyPriceWithoutDiscount;
    boolean mIsHeader = false;
    private LinearLayout mMainLayout;
    public DistributorPackagePropertiesRow(Context context, boolean isHeader) {
        super(context);
        mIsHeader = isHeader;
        init(null);
    }

    public DistributorPackagePropertiesRow(Context context, AttributeSet attrs, boolean isHeader) {
        super(context, attrs);
        mIsHeader = isHeader;
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = null;
        if(mIsHeader){
            view = inflate(getContext(), R.layout.view_distributor_package_properties_header_row, null);
        }else {
            view = inflate(getContext(), R.layout.view_distributor_package_properties_row, null);
        }
        mPropertyItemInside = (TextViewPlus) view.findViewById(R.id.dpkpr_tv_item_inside);
        mPropertyCount = (TextViewPlus) view.findViewById(R.id.dpkpr_tv_count);
        mPropertyPriceWithoutDiscount = (TextViewPlus) view.findViewById(R.id.dpkpr_tv_price_without_discount);
        mMainLayout = (LinearLayout) view.findViewById(R.id.dppr_main_container);
        addView(view);
    }
    public void setRowValues( String itemInside, String count, String priceWithoutDiscount){
        mPropertyItemInside.setText(itemInside);
        mPropertyCount.setText( count );
        mPropertyPriceWithoutDiscount.setText(priceWithoutDiscount);
    }
    void setColor(int colorId){
        if(colorId != -1){
            mPropertyItemInside.setTextColor(ContextCompat.getColor(this.getContext(), colorId));
            mPropertyCount.setTextColor(ContextCompat.getColor(this.getContext(), colorId));
            mPropertyPriceWithoutDiscount.setTextColor(ContextCompat.getColor(this.getContext(), colorId));
        }
    }
}
