package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import ir.joorjens.joorapp.R;

import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by Mohsen on 3/26/2018.
 */

public class CheckBoxPlus extends AppCompatCheckBox {
    private int id;
    public CheckBoxPlus(Context context) {
        super(context);
        CustomFontHelper.setCustomFont(this, context, null);
    }

    public CheckBoxPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public CheckBoxPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    private void init(){

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
