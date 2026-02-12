package ir.joorjens.joorapp.utils;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by batman on 4/21/2018.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       BottomNavigationView child, @NonNull
                                               View directTargetChild, @NonNull View target,
                                       int axes)
    {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        //child.setTranslationY(Math.max(0f, Math.min(child.getHeight(),child.getTranslationY()+dy)));

        if (dy > 0) {
            slideDown(child);
        } else if (dy < 0) {
            slideUp(child);
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed)
    {

        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }
    boolean upStart = false, downStart = false;
    private void slideUp(BottomNavigationView child) {
        if(!upStart)
        child.animate().translationY(.0f).setDuration(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                upStart = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                upStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                upStart = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void slideDown(BottomNavigationView child) {
        if(!downStart)
        child.animate().translationY(height).setDuration(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                downStart = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                downStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                downStart = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
