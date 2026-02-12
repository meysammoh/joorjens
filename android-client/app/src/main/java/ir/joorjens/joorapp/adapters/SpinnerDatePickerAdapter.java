package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.smartlab.persiandatepicker.PersianDatePicker;
import ir.smartlab.persiandatepicker.util.PersianCalendar;

/**
 * Created by batman on 4/15/2018.
 */

public class SpinnerDatePickerAdapter extends ArrayAdapter<PersianDatePicker> {

    private LayoutInflater _Inflater;
    private Context _Context;
    private PersianDatePicker[] _items;
    private int _Resource;
    private Context mContext;

    private PersianDatePicker _pdp;
    private TextViewPlus _tvSelectedDate;

    public SpinnerDatePickerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull PersianDatePicker[] objects) {
        super(context, resource, objects);
        _Resource = resource;
        _items = objects;
        _Context = context;
        _Inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public String getSelectedDateAsString(){
        PersianCalendar pCal = _items[0].getDisplayPersianDate();
        return pCal.getPersianShortDate();
        //return _tvSelectedDate.getText().toString();
    }

    public void setTextDate(String s){
        if(_items != null) {
            PersianCalendar pCal = _items[0].getDisplayPersianDate();
            _tvSelectedDate.setText(s);
            Toast.makeText(mContext, _tvSelectedDate.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = _Inflater.inflate(R.layout.view_spinner_date_pecker_open, parent, false);
        _pdp = (PersianDatePicker) view.findViewById(R.id.spinner_date_picker_item_open);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = _Inflater.inflate(R.layout.view_spinner_date_pecker_close, parent, false);
        _tvSelectedDate = (TextViewPlus) view.findViewById(R.id.spinner_date_picker_item_close);
        if(_items != null) {
            PersianCalendar pCal = _items[0].getDisplayPersianDate();
                    _tvSelectedDate.setText(pCal.getPersianShortDate());
        }
        return view;
    }

}
