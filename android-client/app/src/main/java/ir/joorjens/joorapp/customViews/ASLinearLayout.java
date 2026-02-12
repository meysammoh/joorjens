package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 12/15/2017.
 */

public class ASLinearLayout extends LinearLayout {

    private float mHPercent, mWPercent;
    private float mLmp, mRmp, mTmp, mBmp; // margin percents

    public ASLinearLayout(Context context) {
        super(context);
    }

    public ASLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ASLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ASLinearLayout);
            mHPercent = attributes.getFloat(R.styleable.ASLinearLayout_ll_h_percent, 0);
            mWPercent = attributes.getFloat(R.styleable.ASLinearLayout_ll_w_percent, 0);
            mLmp = attributes.getFloat(R.styleable.ASLinearLayout_ll_left_margin, 0);
            mRmp = attributes.getFloat(R.styleable.ASLinearLayout_ll_tight_margin, 0);
            mTmp = attributes.getFloat(R.styleable.ASLinearLayout_ll_top_margin, 0);
            mBmp = attributes.getFloat(R.styleable.ASLinearLayout_ll_bottom_margin, 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getResources().getDisplayMetrics().widthPixels;
        if(mHPercent != 0) {
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = (int)(w * mHPercent);
            super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
        }

        if(mWPercent != 0) {
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = (int)(w * mWPercent);
            super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
        }

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if(mLmp != 0){
            params.leftMargin = (int)(w * mLmp);
        }
        if(mRmp != 0){
            params.rightMargin = (int)(w * mRmp);
        }
        if(mTmp != 0){
            params.topMargin = (int)(w * mTmp);
        }
        if(mBmp != 0){
            params.bottomMargin = (int)(w * mBmp);
        }

        setLayoutParams(params);
        requestLayout();
    }
}
