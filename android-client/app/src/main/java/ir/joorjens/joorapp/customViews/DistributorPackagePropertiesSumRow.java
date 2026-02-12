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

public class DistributorPackagePropertiesSumRow extends TableRow {
    TextViewPlus mPropertyName;
    TextViewPlus mPropertyValue;

    public DistributorPackagePropertiesSumRow(Context context) {
        super(context);
        init(null);
    }

    public DistributorPackagePropertiesSumRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_distributor_package_properties_sum_row, null);
        mPropertyName = (TextViewPlus) view.findViewById(R.id.dpkpsr_tv_name);
        mPropertyValue = (TextViewPlus) view.findViewById(R.id.dpkpsr_tv_value);
        addView(view);
    }
    public void setRowValues( String name, String value){
        mPropertyName.setText( name);
        mPropertyValue.setText( value);

    }
    void setColor(int colorId){
        if(colorId != -1) {
            mPropertyName.setTextColor(ContextCompat.getColor(this.getContext(), colorId));
            mPropertyValue.setTextColor(ContextCompat.getColor(this.getContext(), colorId));
        }
    }
}
