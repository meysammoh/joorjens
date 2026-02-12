package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.customViews.SpinnerPlus;

/**
 * Created by Mohsen on 10/6/2017.
 */

public class SpinnerPlusAdapter<T extends SpinnerPlusItem> extends ArrayAdapter<T> {

    private Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "fonts/yl.ttf");

    private List<SpinnerPlusItem> items;

    public SpinnerPlusAdapter(@NonNull Context context,
                              @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        items = (List<SpinnerPlusItem>)objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(items.get(position).getFriendlyName());
        view.setTypeface(font);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(items.get(position).getFriendlyName());
        view.setTypeface(font);
        return view;
    }
}
