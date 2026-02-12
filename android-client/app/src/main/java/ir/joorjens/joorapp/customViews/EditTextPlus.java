package ir.joorjens.joorapp.customViews;

/**
 * Created by mohsen on 6/2/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.Format;

import ir.joorjens.joorapp.utils.CustomFontHelper;
import ir.joorjens.joorapp.utils.FormatHelper;

public class EditTextPlus extends AppCompatEditText {


    public EditTextPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public EditTextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public EditTextPlus(Context context) {
        super(context);
    }
}