package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.net.URL;

import ir.joorjens.joorapp.R;

/**
 * Created by mohsen on 12/26/2017.
 */

public class TextSliderView extends BaseSliderView{
    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_slider,null);
        setScaleType(ScaleType.CenterInside);
        ImageView target = (ImageView)v.findViewById(R.id.slider_img);
        TextViewPlus description = (TextViewPlus)v.findViewById(R.id.slider_text);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
