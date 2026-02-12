package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by meysam on 8/22/17.
 */

public class ButtonPlus extends AppCompatButton {

    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}
