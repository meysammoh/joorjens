package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;

/**
 * Created by Mohsen on 10/17/2017.
 */

public class ImageViewPlus extends LinearLayout {

    private RelativeLayout mManiBorder;

    public AppCompatImageView getImageView() {
        return mImage;
    }

    private AppCompatImageView mImage;
    private AppCompatImageView mDiscountImage;
    private TextViewPlus mDiscountText;

    public Integer getCount() {
        return mCount;
    }

    public void setCount(Integer count) {
        mCount = (count == null) ? 0 : count;
        if(mCount > 0){
            mTvCount.setVisibility(VISIBLE);
            mTvCount.setText(mCount+"");
        }else{
            mTvCount.setVisibility(GONE);
        }
    }

    private TextViewPlus mTvCount;

    private String mText;
    private boolean mSquare;
    private  Integer mCount;

    public void setShowDiscountValue(boolean showDiscountValue) {
        this.mShowDiscountValue = showDiscountValue;
    }

    private boolean mShowDiscountValue;
    private float mPercent;
    private float mLmp, mRmp, mTmp, mBmp; // margin percents
    private float mTextSize;
    private Context mContext;

    public void setDiscountValue(Double dValue){
        mText =   "%" + dValue.intValue();
        mDiscountText.setText(mText);
        mDiscountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
    }

    public ImageViewPlus(Context context) {
        super(context);
        mContext = context;
    }

    public ImageViewPlus(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public ImageViewPlus(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public void setImage(String imageURl){
        StaticHelperFunctions.loadImage(mContext, imageURl, mImage);
    }

    public void setImageResource(int resId){
        mImage.setImageResource(resId);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Context c = getContext();
            View view = inflate(c, R.layout.image_view_plus, null);
            mManiBorder = (RelativeLayout) view.findViewById(R.id.ivp_main_border);
            mImage = (AppCompatImageView) view.findViewById(R.id.ivp_img);
            mDiscountImage = (AppCompatImageView) view.findViewById(R.id.ivp_discount_image);
            mDiscountText = (TextViewPlus) view.findViewById(R.id.ivp_discount_text);
            mTvCount = (TextViewPlus) view.findViewById(R.id.ivp_count);

            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ImageViewPlus);
            mPercent = attributes.getFloat(R.styleable.ImageViewPlus_iv_percent, 0);
            mSquare = attributes.getBoolean(R.styleable.ImageViewPlus_iv_square, false);
            mShowDiscountValue = attributes.getBoolean(R.styleable.ImageViewPlus_iv_show_discount_value, false);
            mText = attributes.getString(R.styleable.ImageViewPlus_iv_discount_value);
            mTextSize = attributes.getFloat(R.styleable.ImageViewPlus_iv_text_size, 12);
            mLmp = attributes.getFloat(R.styleable.ImageViewPlus_iv_left_margin, 0);
            mRmp = attributes.getFloat(R.styleable.ImageViewPlus_iv_right_margin, 0);
            mTmp = attributes.getFloat(R.styleable.ImageViewPlus_iv_top_margin, 0);
            mBmp = attributes.getFloat(R.styleable.ImageViewPlus_iv_bottom_margin, 0);
            mImage.setImageResource(attributes.getResourceId(R.styleable.ImageViewPlus_iv_image_source, 0));
            boolean st = attributes.getBoolean(R.styleable.ImageViewPlus_iv_stretch, false);
            if(st){
                mImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            addView(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int displayW = getResources().getDisplayMetrics().widthPixels;
        int newHeight = getMeasuredHeight();
        int newWidth = getMeasuredWidth();

        if (mPercent != 0) {
            newHeight = (int) (displayW * mPercent);
        }

        if(mSquare) {
            newWidth = newHeight;
        }

        setMeasuredDimension(newHeight, newHeight);
        super.onMeasure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

        ViewGroup.LayoutParams imgParams = mImage.getLayoutParams();
        imgParams.height = newHeight;
        imgParams.width = newWidth;
        mImage.setLayoutParams(imgParams);
        mImage.requestLayout();

        MarginLayoutParams notifLParams = (MarginLayoutParams) mTvCount.getLayoutParams();
        notifLParams.leftMargin = (newWidth - mTvCount.getMeasuredWidth()) / 2;
        mTvCount.setLayoutParams(notifLParams);
        mTvCount.requestLayout();

        //mDiscountText.setText(mText);

        if(!mShowDiscountValue){
            mDiscountText.setVisibility(GONE);
            mDiscountImage.setVisibility(GONE);
        }
        else{
//            int textHolderH = (int)(newHeight* 1);
//            ViewGroup.LayoutParams mDiscountTextLayoutParams = mDiscountText.getLayoutParams();
//            mDiscountTextLayoutParams.height = textHolderH;
//            mDiscountTextLayoutParams.width = textHolderH;
//            mDiscountText.setLayoutParams(mDiscountTextLayoutParams);
//            mDiscountText.requestLayout();

//            ViewGroup.LayoutParams mDiscountImageLayoutParams =  mDiscountImage.getLayoutParams();
//            mDiscountImageLayoutParams.height = textHolderH;
//            mDiscountImageLayoutParams.width = textHolderH;
//            mDiscountImage.setLayoutParams(mDiscountImageLayoutParams);

            mDiscountText.setVisibility(VISIBLE);
            mDiscountImage.setVisibility(VISIBLE);
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        if(mLmp != 0){
            params.leftMargin = (int)(displayW * mLmp);
        }
        if(mRmp != 0){
            params.rightMargin = (int)(displayW * mRmp);
        }
        if(mTmp != 0){
            params.topMargin = (int)(displayW * mTmp);
        }
        if(mBmp != 0){
            params.bottomMargin = (int)(displayW * mBmp);
        }

        setLayoutParams(params);
        requestLayout();
    }

//    private void correctWidth(TextViewPlus textView, int desiredWidth)
//    {
//        Paint paint = new Paint();
//        Rect bounds = new Rect();
//
//        paint.setTypeface(textView.getTypeface());
//        float textSize = textView.getTextSize();
//        paint.setTextSize(textSize);
//        String text = textView.getText().toString();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//
//        while (bounds.width() < desiredWidth)
//        {
//            textSize++;
//            paint.setTextSize(textSize);
//            paint.getTextBounds(text, 0, text.length(), bounds);
//        }
//
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//    }

//    public static float getFitTextSize(TextViewPlus textView, float width) {
//        TextPaint paint = textView.getPaint();
//        float nowWidth = paint.measureText(textView.getText().toString());
//        float newSize = (float) width / nowWidth * paint.getTextSize() * width;
//        return newSize;
//    }
}
