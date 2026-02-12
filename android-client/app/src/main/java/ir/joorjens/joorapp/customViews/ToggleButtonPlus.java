package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.view.View;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by meysam on 8/22/17.
 */

public class ToggleButtonPlus extends ButtonPlus {

    private boolean mSelected = false;
    private Drawable mSelectedBgDrawable  = getResources().getDrawable(R.drawable.rounded_cheap_color);
    private int mSelectedTextColor = Color.WHITE;
    private Drawable mNormalBGDrawable = getResources().getDrawable(R.drawable.rounded_cheap_color_no_fill);
    private int mNormalTextColor = Color.GRAY;

    public ToggleButtonPlus(Context context) {
        super(context);
        init(null);

    }

    public ToggleButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
        init(attrs);
    }

    public ToggleButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
        init(attrs);

    }
    private void init( AttributeSet attrs ){
        if(attrs != null){
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButtonPlus);
            mSelected = attributes.getBoolean(R.styleable.ToggleButtonPlus_selected, false);
            mSelectedBgDrawable = attributes.getDrawable(R.styleable.ToggleButtonPlus_selected_bg_drawable);
            mSelectedTextColor = attributes.getColor(R.styleable.ToggleButtonPlus_selected_text_color, mSelectedTextColor);
            mNormalBGDrawable = attributes.getDrawable(R.styleable.ToggleButtonPlus_normal_bg_drawable);
            mNormalTextColor = attributes.getColor(R.styleable.ToggleButtonPlus_normal_text_color, mNormalTextColor);

            setSelected(mSelected);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    @Override
    public boolean isSelected() {
        return mSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.mSelected = selected;
        if(selected){
            setBackground(mSelectedBgDrawable);
            setTextColor(mSelectedTextColor);
        }
        else{
            setBackground( mNormalBGDrawable );
            setTextColor(mNormalTextColor);

        }
    }

    private void toggle(){
        setSelected(!mSelected);
    }

}
