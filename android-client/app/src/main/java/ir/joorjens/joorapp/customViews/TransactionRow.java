package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import java.util.Date;
import java.util.TimeZone;

import ir.joorjens.joorapp.R;
import ir.smartlab.persiandatepicker.util.PersianCalendar;

/**
 * Created by mohsen on 5/5/2018.
 */

public class TransactionRow extends TableRow{

    private TextViewPlus _tvDate;
    private TextViewPlus _tvTime;
    private TextViewPlus _tvAmount;
    private TextViewPlus _tvNote;
    private RelativeLayout _rlTitleContainer;


    public TransactionRow(Context context) {
        super(context);
        init(null);
    }

    public TransactionRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        View view  = inflate(getContext(), R.layout.view_transaction_row, null);
        _tvDate = (TextViewPlus) view.findViewById(R.id.tv_date);
        _tvTime = (TextViewPlus) view.findViewById(R.id.tv_time);
        _tvAmount = (TextViewPlus) view.findViewById(R.id.tv_amount);
        _tvNote = (TextViewPlus) view.findViewById(R.id.tv_note);
        _rlTitleContainer = (RelativeLayout) view.findViewById(R.id.rl_title_container);

        addView(view);
    }

    public void setRowValues(Integer invoiceTime, String amountStr, String note, int colorId){
        _rlTitleContainer.setBackgroundColor(getResources().getColor(colorId));
        long millis = (long)invoiceTime * 1000;
        PersianCalendar pc = new PersianCalendar(millis);
        String[] dt = pc.getPersianShortDateTime().split(" ");
        _tvDate.setText(dt[0]);
        _tvTime.setText(dt[1]);
        _tvAmount.setText(amountStr);
        _tvNote.setText(note);
    }
}
