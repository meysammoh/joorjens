package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

public class AutoResizeTextView extends TextViewPlus {
    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);

        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(getTypeface());
        float textSize = getTextSize();
        paint.setTextSize(textSize);
        String text = getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > w)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

    }
}
