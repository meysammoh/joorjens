package ir.joorjens.joorapp.customViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import ir.joorjens.joorapp.R;

/**
 * Created by Mohsen on 5/9/2018.
 */

public class CircularProgress extends View {

    public interface CircularProgressEventListener{
        public void onProgressStopped(int percent);
        public void onProgressEnd();
    }

    public void setProgressFinishedListener(CircularProgressEventListener listener) {
        mListener = listener;
    }

    private CircularProgressEventListener mListener;


    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mCirclePaint;
    private Paint mEraserPaint;
    private RectF mCircleOuterBounds;
    private RectF mCircleInnerBounds;
    private float mCircleSweepAngle = 0f;
    private ValueAnimator mTimerAnimator;
    private int mCrossLength;
    private float mPercent;

    private float mThickness;
    private int mColor;
    private boolean mStarted;

    public boolean isStarted() {
        return mStarted;
    }
    public void setStarted(boolean mStarted) {
        this.mStarted = mStarted;
    }

    public CircularProgress(Context context) {
        super(context);
        init(null);
    }

    public CircularProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mListener = null;
        mStarted = false;
        mColor = Color.GRAY;

        if(attrs != null){
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgress);
            mThickness = attributes.getFloat(R.styleable.CircularProgress_cp_thickness, 8);
            mColor = attributes.getColor(R.styleable.CircularProgress_cp_color, mColor);
            mCircleSweepAngle = attributes.getFloat(R.styleable.CircularProgress_cp_start_value, 0f);
            mStarted = attributes.getBoolean(R.styleable.CircularProgress_cp_started, false);
        }

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mColor);
        mCirclePaint.setStrokeWidth(mThickness);

        mEraserPaint = new Paint();
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setColor(Color.TRANSPARENT);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.TRANSPARENT);
            mCanvas = new Canvas(mBitmap);
            mCrossLength = getWidth() / 8;
        }

        super.onSizeChanged(w, h, oldw, oldh);
        updateBounds();
    }

    private void updateBounds() {
        mCircleOuterBounds = new RectF(0, 0, getWidth(), getHeight());
        mCircleInnerBounds = new RectF(
                mCircleOuterBounds.left + mThickness,
                mCircleOuterBounds.top + mThickness,
                mCircleOuterBounds.right - mThickness,
                mCircleOuterBounds.bottom - mThickness);
        invalidate();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        if(mStarted) {
            if (mCircleSweepAngle > 0f) {
                mCanvas.drawArc(mCircleOuterBounds, 270, mCircleSweepAngle, true, mCirclePaint);
                mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
                mCanvas.drawLine((getWidth() / 2) - mCrossLength, (getHeight() / 2) - mCrossLength,
                        (getWidth() / 2) + mCrossLength, (getHeight() / 2) + mCrossLength, mCirclePaint);
                mCanvas.drawLine((getWidth() / 2) + mCrossLength, (getHeight() / 2) - mCrossLength,
                        (getWidth() / 2) - mCrossLength, (getHeight() / 2) + mCrossLength, mCirclePaint);
            }
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public void start(int secs) {
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawProgress((float) animation.getAnimatedValue());
            }
        });
        mTimerAnimator.start();
        mStarted = true;
    }

    public void stop(){
        mTimerAnimator.cancel();
        mStarted = false;
        draw(mCanvas);
        if(mListener != null){
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            double dPercent = Double.valueOf(formatter.format(mPercent));
            mListener.onProgressStopped((int)(dPercent*100));
        }
    }

    private void drawProgress(float progress) {
        mPercent = progress;
        mCircleSweepAngle = 360 * progress;
        invalidate();
        if(progress == 1){
            mStarted = false;
            if(mListener != null){
                mListener.onProgressEnd();
            }
        }
    }
}
