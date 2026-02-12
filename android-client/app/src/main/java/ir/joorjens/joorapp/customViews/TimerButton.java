package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;

/**
 * Created by meysam on 8/9/17.
 */

public class TimerButton extends LinearLayout {
    private ProgressWheel mProgressWheel;
    private TextViewPlus mTextViewPlus;
    private int mTimerDuration = 5; //Seconds

    public int getmTimerDuration() {
        return mTimerDuration;
    }

    public TimerButton setmTimerDuration(int mTimerDuration) {
        this.mTimerDuration = mTimerDuration;
        return this;
    }
    public TimerButton(Context context) {
        super(context);
        initializeViews(context);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public TimerButton(Context context,
                       AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }
    public void startDownCounter(){
        setEnabled(false);
        mProgressWheel.setVisibility(View.VISIBLE);
        new CountDownTimer(mTimerDuration*1000, 5) {

            public void onTick(long millisUntilFinished) {

                int remainingTime=1+(int)Math.ceil(millisUntilFinished*.001);
                mProgressWheel.setProgress(360*(millisUntilFinished*.001f)/mTimerDuration);
                mProgressWheel.setText(""+(remainingTime -1));
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                mProgressWheel.setProgress(360);
                mProgressWheel.setVisibility(View.GONE);

            }
        }.start();
    }
    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progress_button, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        mProgressWheel = (ProgressWheel) this
                .findViewById(R.id.progressBarTwo);

        mTextViewPlus= (TextViewPlus) this
                .findViewById(R.id.progress_text_view);

        mProgressWheel.setVisibility(GONE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The first thing that happen is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We can’t use getWidth() or getHight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        TextViewPlus textView = null;
        ViewGroup row =  this;
        for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
            View view = row.getChildAt(itemPos);
            if (view instanceof TextViewPlus) {
                textView = (TextViewPlus) view; //Found it!
                break;
            }
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(textView!=null){
            height = textView.getMeasuredHeight()+textView.getPaddingTop()+getPaddingBottom();
        }

        setMeasuredDimension( width,height);
    }
}
