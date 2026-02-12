package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 10/15/2017.
 */

public class TitleBar extends FrameLayout{
    private TextViewPlus mTbTvTitle;
    private TextViewPlus mTbTvViewAll;
    private ImageViewPlus mTbImgViewAll;
    private ASLinearLayout mllAllContainer;
    private Context mContext;
    //private RelativeLayout mMainLayout;

    public TitleBar(@NonNull Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public void setOnClick(OnClickListener listener){
        mTbImgViewAll.setOnClickListener(listener);
        mTbTvViewAll.setOnClickListener(listener);
        mllAllContainer.setOnClickListener(listener);
    }


    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.title_bar, null);
        mTbTvTitle = (TextViewPlus) view.findViewById(R.id.title_bar_tv_title);
        mTbTvViewAll = (TextViewPlus) view.findViewById(R.id.title_bar_tv_view_all);
        mTbImgViewAll = (ImageViewPlus) view.findViewById(R.id.title_bar_img_view_all);
        mllAllContainer = (ASLinearLayout) view.findViewById(R.id.all_container);
        //mMainLayout = (RelativeLayout) view.findViewById(R.id.title_bar_boarder);

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);

            mTbTvTitle.setText(attributes.getString(R.styleable.TitleBar_title_text));
            mTbTvViewAll.setText(attributes.getString(R.styleable.TitleBar_view_all_text));
            int textColor = 0xFF0000FF;
            textColor = attributes.getColor(R.styleable.TitleBar_title_text_color, textColor);
            mTbTvTitle.setTextColor(textColor);
        }

        addView(view);
    }
}
