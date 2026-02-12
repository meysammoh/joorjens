package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Date;

import ir.joorjens.joorapp.R;

/**
 * Created by mohsen on 5/4/2018.
 */

public class PersianDatePicker extends FrameLayout{

    private Context mContext;
    private ImageViewPlus mIvDropDownClicker;
    private TextViewPlus mTvSelectedDate;
    private ir.smartlab.persiandatepicker.PersianDatePicker mPdp;

    public PersianDatePicker(@NonNull Context context) {
        super(context);
        mContext = context;
        init(null );
    }

    public PersianDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public PersianDatePicker(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private OnClickListener openCloseDropDown = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mTvSelectedDate.setText(mPdp.getDisplayPersianDate().getPersianShortDate());
            if(mPdp.getVisibility() == VISIBLE ){
                mPdp.setVisibility(GONE);
            }else{
                mPdp.setVisibility(VISIBLE);
            }
        }
    };

    private void init(AttributeSet attributeSet){
        View view = inflate(mContext, R.layout.view_persian_date_picker, null);
        mIvDropDownClicker = (ImageViewPlus) view.findViewById(R.id.iv_drop_down_clicker);
        mIvDropDownClicker.setOnClickListener(openCloseDropDown);
        mTvSelectedDate = (TextViewPlus) view.findViewById(R.id.tv_selected_date);
        mPdp = (ir.smartlab.persiandatepicker.PersianDatePicker) view.findViewById(R.id.pdp);
        mTvSelectedDate.setText(mPdp.getDisplayPersianDate().getPersianShortDate());

        addView(view);
    }

    public String getSelectedDatePersian(){
        return mPdp.getDisplayPersianDate().getPersianShortDate();
    }

    public Date getSelectedDateJava(){
        return mPdp.getDisplayDate();
    }
}
