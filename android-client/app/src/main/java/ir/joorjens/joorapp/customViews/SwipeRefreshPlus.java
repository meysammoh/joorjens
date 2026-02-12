package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by ZM on 5/17/2018.
 */

public class SwipeRefreshPlus extends SwipeRefreshLayout {
    public SwipeRefreshPlus(Context context) {
        super(context);
    }

    public SwipeRefreshPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        return true;
    }
}
