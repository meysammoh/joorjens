package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by Mohsen on 10/22/2017.
 */

public class CrossLineTextView extends TextViewPlus {


    public CrossLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CrossLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CrossLineTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Rect textBounds = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), textBounds );
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(2);
        p.setColor(Color.RED);
        int offsetX = (getWidth() - textBounds.width())/2;
        int offsetY = (getHeight() - textBounds.height())/2;
        canvas.drawLine(offsetX, textBounds.height() + offsetY, textBounds.width()+offsetX,offsetY, p);
    }
}
