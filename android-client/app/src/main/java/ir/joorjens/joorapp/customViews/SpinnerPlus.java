package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.NoData;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.adapters.SpinnerPlusItem;
import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by Mohsen on 10/5/2017.
 */
public class SpinnerPlus<T extends SpinnerPlusItem> extends AppCompatSpinner{

    int mLayoutId;

    public SpinnerPlus(Context context) {
        super(context);

        ArrayList<NoData> ndl = new ArrayList<>();
        ndl.add(new NoData("انتخاب کنید", -1));

        SpinnerPlusAdapter adapter = new SpinnerPlusAdapter(getContext(),R.layout.spinner_item_default_fc, ndl);
        setAdapter(adapter);
    }

    public SpinnerPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(attrs != null){
            TypedArray attributeSet = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerPlus);
            mLayoutId = attributeSet.getResourceId(R.styleable.SpinnerPlus_sp_layout, -1);
        }

        ArrayList<NoData> ndl = new ArrayList<>();
        ndl.add(new NoData("انتخاب کنید", -1));
        SpinnerPlusAdapter adapter = new SpinnerPlusAdapter(getContext(),R.layout.spinner_item_default_fc, ndl);
        if(mLayoutId != -1){
            adapter = new SpinnerPlusAdapter(getContext(),mLayoutId, ndl);
        }
        setAdapter(adapter);
    }

    public void setItems(ArrayList<T> items){
        SpinnerPlusAdapter adapter = new SpinnerPlusAdapter(getContext(), mLayoutId, items);
        setAdapter(adapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = getMeasuredHeight();
        setDropDownVerticalOffset(h);
    }
}