package ir.joorjens.joorapp.customViews;

/**
 * Created by meysam on 6/2/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.CustomFontHelper;

public class TextViewPlus extends AppCompatTextView  {

    private float mHPercent, mWPercent;
    private boolean sizeAdapted;
    private float mLmp, mRmp, mTmp, mBmp; // margin percents

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
        init(attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
        init(attrs);
    }

    public TextViewPlus(Context context) {
        super(context);
    }

    private void init(AttributeSet attrs) {
        sizeAdapted = false;
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
            mHPercent = attributes.getFloat(R.styleable.TextViewPlus_hPercent, 0);
            mWPercent = attributes.getFloat(R.styleable.TextViewPlus_wPercent, 0);
            mLmp = attributes.getFloat(R.styleable.TextViewPlus_tv_left_margin, 0);
            mRmp = attributes.getFloat(R.styleable.TextViewPlus_tv_right_margin, 0);
            mTmp = attributes.getFloat(R.styleable.TextViewPlus_tv_top_margin, 0);
            mBmp = attributes.getFloat(R.styleable.TextViewPlus_tv_bottom_margin, 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int w = getResources().getDisplayMetrics().widthPixels;
//        float d = getResources().getDisplayMetrics().density;
//        float factor = (float)(720);// (480* 1.5);
//        if(!sizeAdapted) {
//            float newSize = (getTextSize() * factor) / (w*d);
//            sizeAdapted = true;
//            setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
//        }
//
//        if(mHPercent != 0) {
//            int size = (int) (w * mHPercent);
//            super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
//                    MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
//        }
//
//        if(mWPercent != 0) {
//            int size = (int) (w * mWPercent);
//            super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
//                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
//        }
//
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
//        if(mLmp != 0){
//            params.leftMargin = (int)(w * mLmp);
//        }
//        if(mRmp != 0){
//            params.rightMargin = (int)(w * mRmp);
//        }
//        if(mTmp != 0){
//            params.topMargin = (int)(w * mTmp);
//        }
//        if(mBmp != 0){
//            params.bottomMargin = (int)(w * mBmp);
//        }
//
//        setLayoutParams(params);
//        requestLayout();
    }
}