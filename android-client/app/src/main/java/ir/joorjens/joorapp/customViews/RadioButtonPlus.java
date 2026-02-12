package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import ir.joorjens.joorapp.R;

import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by Mohsen on 10/6/2017.
 */

public class RadioButtonPlus extends AppCompatRadioButton {
    public int getTypeId() {
        return mTypeId;
    }

    public void setTypeId(int mTypeId) {
        this.mTypeId = mTypeId;
    }

    private int mTypeId;
    public RadioButtonPlus(Context context) {
        super(context);
    }

    public RadioButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public RadioButtonPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

}
