package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.CustomFontHelper;

/**
 * Created by mohsen on 8/22/17.
 */

public class NumberPickerPlus extends NumberPicker {
    Typeface type;

    public NumberPickerPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        type = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/yl.ttf");
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);

        type = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/yl.ttf");
        updateView(child);
    }

    private void updateView(View view) {

        if (view instanceof EditText) {
            ((EditText) view).setTypeface(type);
            ((EditText) view).setTextSize(25);
            ((EditText) view).setTextColor(getResources().getColor(
                    R.color.color_black60));
        }

    }
}
