package ir.joorjens.joorapp.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.TurnoverActivity;
import ir.joorjens.joorapp.adapters.SpinnerDatePickerAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlusOpenCloseEvent;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.TransactionAPIs;
import ir.smartlab.persiandatepicker.PersianDatePicker;

public class TurnoverSearchFragment extends DialogFragment {

    public static String ParamNameRequestCodeInteger = "_requestCode";
    private int _requestCode;

    private ir.joorjens.joorapp.customViews.PersianDatePicker _spinnerDateFrom;
    private ir.joorjens.joorapp.customViews.PersianDatePicker _spinnerDateTo;
    private ButtonPlus _btnSearch;
    private ActivityServiceListener _asl;
    DialogListener mListener;

    public interface DialogListener {
        public void onDialogClick(DialogFragment dialog, int requestCode);
    }



    public TurnoverSearchFragment() {
    }

    public void setListener(DialogListener listener){
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_search_turnover, container, false);

        ButtonPlus button = (ButtonPlus) dialogView.findViewById( R.id.fsto_btn_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogClick( TurnoverSearchFragment.this, _requestCode);
                dismiss();
            }
        });

        ImageViewPlus imgExit = (ImageViewPlus) dialogView.findViewById(R.id.fsto_img_close);
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ////////////
        _asl = (ActivityServiceListener) getActivity();
        _spinnerDateFrom = (ir.joorjens.joorapp.customViews.PersianDatePicker) dialogView.findViewById(R.id.pdp_from);
        _spinnerDateTo = (ir.joorjens.joorapp.customViews.PersianDatePicker) dialogView.findViewById(R.id.pdp_to);
        _btnSearch = (ButtonPlus) dialogView.findViewById(R.id.fsto_btn_search);
        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionAPIs.searchProduct(_asl, Authenticator.loadAuthenticationToken(),
                        Authenticator.loadAccount().getId(),
                        //21,
                        _spinnerDateFrom.getSelectedDateJava().getTime()/1000,
                        _spinnerDateTo.getSelectedDateJava().getTime()/1000, 1);
                dismiss();
            }
        });


        return dialogView;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        // Call super onResume after sizing

        super.onResume();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
