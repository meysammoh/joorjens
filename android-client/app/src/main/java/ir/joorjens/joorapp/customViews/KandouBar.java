package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.util.Log;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.CustomFontHelper;
import ir.joorjens.joorapp.utils.FontCache;

/**
 * Created by meysammoh on 25.04.18.
 */

public class KandouBar extends AppCompatRatingBar {
    private Typeface typeFace = Typeface.DEFAULT;
    public KandouBar(Context context) {
        super(context);
        init(null);
    }

    public KandouBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( attrs);
    }

    public KandouBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( attrs );
    }
    void init(  AttributeSet attrs ){
        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
            if(a != null) {
                String font = a.getString(R.styleable.CustomFont_ffont);
                typeFace = FontCache.get(font, getContext());
            }
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int stars = getNumStars();
        float rating = getRating();
        //canvas.translate(get, CENTER_X);
        float pace = ((float) getWidth())/stars;
        float posX = pace /3.3f;
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        float textSize = pace*.5f;
        float posY = pace / 3.3f ;
        paint.setTextSize(textSize);
        paint.setTypeface( typeFace );
        for (int i=0;i<stars;i++) {
            String txt = ""+(i+1);
            Rect txtBounds = new Rect();
            paint.getTextBounds( txt, 0, 1, txtBounds );
            canvas.drawText( txt, posX - txtBounds.width()*.5f, posY + txtBounds.height()*.5f, paint);
            posX+=pace;
        }
    }
}
