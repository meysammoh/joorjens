package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by meysam on 6/2/17.
 */

public class SquareButton extends View {
    public SquareButton(final Context context) {
        super(context);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"abbas",Toast.LENGTH_SHORT).show();
//            }
//        });
        //setBackgroundResource();
    }

    public SquareButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"abbas",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public SquareButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"abbas",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size); // make it square


    }

}
