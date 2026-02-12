package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 12/15/2017.
 */

public class ASRelativeLayout extends RelativeLayout {

    private float mPercent;
    private float mLmp, mRmp, mTmp, mBmp; // margin percents

    public ASRelativeLayout(Context context) {
        super(context);
    }

    public ASRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ASRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ASRelativeLayout);
            mPercent = attributes.getFloat(R.styleable.ASRelativeLayout_rl_percent, 0);
            mLmp = attributes.getFloat(R.styleable.ASRelativeLayout_rl_left_margin, 0);
            mRmp = attributes.getFloat(R.styleable.ASRelativeLayout_rl_right_margin, 0);
            mTmp = attributes.getFloat(R.styleable.ASRelativeLayout_rl_top_margin, 0);
            mBmp = attributes.getFloat(R.styleable.ASRelativeLayout_rl_bottom_margin, 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getResources().getDisplayMetrics().widthPixels;
        if(mPercent != 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int size = (int)(w * mPercent);
            super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
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
