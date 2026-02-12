package ir.joorjens.joorapp.customViews;

/**
 * Created by ZM on 5/8/2018.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class CircularTextView extends TextViewPlus
{
    private float strokeWidth;
    int strokeColor,solidColor;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        //circlePaint.setColor(solidColor);
        circlePaint.setColor(Color.argb(210,255,0,0));
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

//        Paint strokePaint = new Paint();
//        strokePaint.setColor(strokeColor);
//        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        //canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, circlePaint);

        super.draw(canvas);
    }

//    public void setStrokeWidth(int dp)
//    {
//        float scale = getContext().getResources().getDisplayMetrics().density;
//        strokeWidth = dp*scale;
//
//    }

//    public void setStrokeColor(String color)
//    {
//        strokeColor = Color.parseColor(color);
//    }
//
//    public void setSolidColor(String color)
//    {
//        solidColor = Color.parseColor(color);
//
//    }
}