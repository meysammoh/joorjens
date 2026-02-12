package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableRow;

import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 17.11.17.
 */

public class DistributorProductPriceRangeRow extends TableRow {
    TextViewPlus mTvName;
    TextViewPlus mTvValue;
    private int mCountFrom, mCountTo, mPrice;
    public DistributorProductPriceRangeRow(Context context) {
        super(context);
        init(null);
    }

    public DistributorProductPriceRangeRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_distributor_product_properties_row, null);
        mTvName = (TextViewPlus) view.findViewById(R.id.dppr_name);
        mTvValue = (TextViewPlus) view.findViewById(R.id.dppr_value);
        addView(view);
    }
    public void setRowValues(int minPrice){
        mTvName.setText( getResources().getString( R.string.label_price_one ));
        mTvValue.setText(String.format("%,d",minPrice) + "تومان");
    }
    public void setRowValues( int countFrom, int countTo, int price){
        mCountFrom = countFrom;
        mCountTo = countTo;
        mPrice = price;

        String text1 = String.format("%,d",price);
        String text2 = " تومان";
        SpannableString s1 = new SpannableString(text1);
        SpannableString s2 = new SpannableString(text2);
        s1.setSpan(new RelativeSizeSpan(1f), 0, text1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        s2.setSpan(new RelativeSizeSpan(.7f), 0, text2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence chPrice = TextUtils.concat(s1," ", s2);


        String packCount = (countFrom > 0 ?
                getResources().getString( R.string.label_from ) + " " + countFrom : "" )+ " " +
                getResources().getString( R.string.label_to ) + " " + countTo;
        mTvName.setText( packCount );
        mTvValue.setText( chPrice );
    }

    public int getCountFrom() {
        return mCountFrom;
    }

    public int getCountTo() {
        return mCountTo;
    }

    public int getPrice() {
        return mPrice;
    }
}
