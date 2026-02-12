package ir.joorjens.joorapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 11/7/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private boolean mHorV;
    private int w;

    public SpacesItemDecoration(Context context, boolean isHorizontal) {
        mDivider = context.getResources().getDrawable(R.drawable.recycler_view_item_divider);
        mHorV = isHorizontal;
        w = context.getResources().getDisplayMetrics().widthPixels;
        w = (int)(w * .031);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if(mHorV) {
            // vertical space
            //outRect.bottom = 20;
            //outRect.bottom = w;
        }
        else{
            outRect.left = 20;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if(mHorV) {
//            // vertical space
//            int left = parent.getPaddingLeft();
//            int right = parent.getWidth() - parent.getPaddingRight();
//
//            int childCount = parent.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View child = parent.getChildAt(i);
//
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//                int top = child.getBottom() + params.bottomMargin;
//                int bottom = top + mDivider.getIntrinsicHeight();
//
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
        }
        else{
            // horizontal space
            int top = parent.getPaddingTop() - 15;
            int bottom = parent.getHeight() - parent.getPaddingBottom() - 30;

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int left = child.getRight() + params.rightMargin +10 ;
                int right = left + mDivider.getIntrinsicWidth();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
