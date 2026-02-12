package ir.joorjens.joorapp.utils;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import ir.joorjens.joorapp.JJApp;

/**
 * Created by ZM on 7/10/2018.
 */

public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private int displayMode;
    private float w;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    //public EqualSpacingItemDecoration(int spacing) {
    //    this(spacing, -1);
    //}

    public EqualSpacingItemDecoration(int displayMode) {
        this.displayMode = displayMode;
        DisplayMetrics metrics =  JJApp.getAppContext().getResources().getDisplayMetrics();
        int wpx = metrics.widthPixels;
        w = wpx * metrics.density;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        this.spacing = (int)(w - (3*view.getLayoutParams().width)) / 4 ;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = spacing;
                outRect.right = position == itemCount - 1 ? spacing : 0;
                outRect.top = spacing;
                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = spacing;
                outRect.right = spacing;
                outRect.top = spacing;
                outRect.bottom = position == itemCount - 1 ? spacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    //int rows = itemCount / cols;

                    outRect.left = spacing;
                    outRect.right = spacing;
                    outRect.top = spacing;
                    outRect.bottom = spacing;

//                    outRect.left = 0;
//                    if(position % cols == 0){
//                        outRect.left = spacing;
//                    }
//                    outRect.right = spacing;
//
//                    outRect.top = 0;
//                    if(position < cols){
//                        outRect.top = spacing;
//                    }
//                    outRect.bottom = spacing;




//                    outRect.left = spacing;
//                    outRect.right = position % cols == cols - 1 ? spacing : 0;
//                    outRect.top = spacing;
//                    outRect.bottom = position / cols == rows - 1 ? spacing : 0;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}