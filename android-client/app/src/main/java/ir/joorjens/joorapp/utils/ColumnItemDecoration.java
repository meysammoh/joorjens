package ir.joorjens.joorapp.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.joorjens.joorapp.JJApp;

/**
 * Created by ZM on 7/9/2018.
 */

public class ColumnItemDecoration extends RecyclerView.ItemDecoration {

    // Horizontal padding
    private final int padding;
    private int mCols;

    public ColumnItemDecoration(int colCount) {
        // Set padding (from resources probably)
        padding = 10;
        mCols = colCount;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        RecyclerView.ViewHolder vh = parent.getChildViewHolder(view);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        float spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
        float totalSpanSize = gridLayoutManager.getSpanCount();

        float n = totalSpanSize / spanSize; // num columns
        float c = layoutParams.getSpanIndex() / spanSize; // column index

        float leftPadding = padding * ((n - c) / n);
        float rightPadding = padding * ((c + 1) / n);

        outRect.left = (int) leftPadding;
        outRect.right = (int) rightPadding;
    }
}