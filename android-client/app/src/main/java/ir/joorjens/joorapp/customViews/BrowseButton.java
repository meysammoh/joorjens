package ir.joorjens.joorapp.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by Mohsen on 10/4/2017.
 */

public class BrowseButton extends FrameLayout {

    private Activity mActivity;
    private StaticHelperFunctions.selectDialogListener mImageListener;
    public BrowseButton(@NonNull Context context) {
        super(context);
        init(null);
    }

    public BrowseButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BrowseButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public int getSelectRequestCode() {
        return mSelectRequestCode;
    }
    public void setSelectRequestCode(int requestCode) {
        this.mSelectRequestCode = requestCode;
    }
    public int getCropRequestCode() {
        return mCropRequestCode;
    }
    public void setCropRequestCode(int requestCode) {
        this.mCropRequestCode = requestCode;
    }

    private int mSelectRequestCode;
    private int mCropRequestCode;

    public String getFullPath() {
        return mFullPath;
    }

    private String mFullPath;
    private TextViewPlus mTextView;
    private ButtonPlus mButtonPlus;

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.browse_button, null);
        mTextView = (TextViewPlus) view.findViewById(R.id.browse_button_text);
        mButtonPlus = (ButtonPlus) view.findViewById(R.id.browse_button_button);

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.BrowseButton);

            mTextView.setText(attributes.getString(R.styleable.BrowseButton_text_view_text));
            mButtonPlus.setText(attributes.getString(R.styleable.BrowseButton_button_text));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, attributes.getFloat(R.styleable.BrowseButton_android_textSize,15));
            setTextColor(attributes.getColor(R.styleable.BrowseButton_android_textColor, Color.BLACK));
        }

        addView(view);

        mButtonPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticHelperFunctions.selectDialog(mActivity, mImageListener, mSelectRequestCode);
            }
        });
    }

    public void setText(String text) {
        mFullPath = text;
        mTextView.setVisibility(INVISIBLE);
        try {
            mTextView.setText(mFullPath.substring(mFullPath.lastIndexOf("/") +1));
        } catch (Exception e) {
            e.printStackTrace();
            mTextView.setText(mFullPath);
        }
    }

    public void showText(){
        mTextView.setVisibility(VISIBLE);
    }
    public void hideText(){
        mTextView.setVisibility(INVISIBLE);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }
    public void setParent(Activity parent) {
        mActivity = parent;
    }

    public void setImageListener(StaticHelperFunctions.selectDialogListener imageListener) {
        this.mImageListener = imageListener;
    }
}
