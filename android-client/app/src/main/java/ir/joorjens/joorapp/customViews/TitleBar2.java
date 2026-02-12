package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.joorjens.joorapp.R;

/**
 * Created by mohsen on 11/3/2017.
 */

public class TitleBar2 extends FrameLayout {

    private TextViewPlus mTb2TvTitle;
    private TextViewPlus mTb2TvMessage;
    private ImageViewPlus mTb2Img;
    private RelativeLayout mMainLayout;

    public void setTitle(String title){
        mTb2TvTitle.setText(title);
    }
    public void setIcon(int iconId){
        if(iconId > -1)
            mTb2Img.setImageResource(iconId);
    }
    public void setColor(int colorId){
        if(colorId > -1) {
            mMainLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), colorId));
        }
        else{
            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, mMainLayout.getWidth(), 0,
                            new int[] {
                                    ContextCompat.getColor(TitleBar2.this.getContext(), R.color.color_fourth),
                                    ContextCompat.getColor(TitleBar2.this.getContext(), R.color.color_third),
                                    ContextCompat.getColor(TitleBar2.this.getContext(), R.color.color_second),
                                    ContextCompat.getColor(TitleBar2.this.getContext(), R.color.color_first) }, //substitute the correct colors for these
                            new float[] {
                                    0, 0.33f, 0.66f, 1 },
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);
            mMainLayout.setBackground(paint);
        }
    }

    public TitleBar2(@NonNull Context context) {
        super(context);
        init(null);
    }

    public TitleBar2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleBar2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.title_bar2, null);
        mTb2TvTitle = (TextViewPlus) view.findViewById(R.id.title_bar2_tv_title);
        mTb2Img = (ImageViewPlus) view.findViewById(R.id.title_bar2_img);
        mMainLayout = (RelativeLayout) view.findViewById(R.id.title_bar2_boarder);
        mTb2TvMessage = (TextViewPlus) view.findViewById(R.id.title_bar2_tv_message);
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar2);

            mTb2TvTitle.setText(attributes.getString(R.styleable.TitleBar2_title2_text));
            //mTb2Img.setImageResource(R.styleable.TitleBar2_tile2_image);
            mMainLayout.setBackground(attributes.getDrawable(R.styleable.TitleBar2_android_background));
        }

        addView(view);
    }

    public String getMessage() {
        return mTb2TvMessage.getText().toString();
    }

    public void setMessage(String message, int visi) {
        this.mTb2TvMessage.setText( message );
        this.mTb2TvMessage.setVisibility( visi );
    }
}
