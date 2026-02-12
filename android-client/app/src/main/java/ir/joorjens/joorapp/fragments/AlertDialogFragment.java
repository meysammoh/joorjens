package ir.joorjens.joorapp.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;

public class AlertDialogFragment extends DialogFragment {

    public static String ParamNameRequestCodeInteger = "_requestCode";
    public static String ParamNameYesBtnString = "yesButtonText";
    public static String ParamNameNoBtnString = "noButtonText";
    public static String ParamNameBodyString = "bodyText";
    public static String ParamNameShowButtonsBoolean = "showButtons";
    private int _requestCode;
    private String _yesButtonText;
    private String _noButtonText;
    private String _bodyText;
    private boolean _showButtons;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int requestCode);
        public void onDialogNegativeClick(DialogFragment dialog, int requestCode);
    }

    NoticeDialogListener mListener;

    public AlertDialogFragment() {
    }

    public void setListener(NoticeDialogListener listener){
        mListener = listener;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (NoticeDialogListener) context;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(context.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _requestCode = getArguments().getInt(ParamNameRequestCodeInteger,0);
        _yesButtonText = getArguments().getString(ParamNameYesBtnString,"Yes");
        _noButtonText = getArguments().getString(ParamNameNoBtnString,"No");
        _bodyText = getArguments().getString(ParamNameBodyString,"");
        _showButtons = getArguments().getBoolean(ParamNameShowButtonsBoolean,true);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_promotion_dialog, null);
        ButtonPlus yesButton = (ButtonPlus) dialogView.findViewById( R.id.btn_dialog_accept);
        yesButton.setText( _yesButtonText);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogPositiveClick( AlertDialogFragment.this, _requestCode);
                dismiss();
            }
        });

        ButtonPlus noButton = (ButtonPlus) dialogView.findViewById( R.id.btn_dialog_reject);
        noButton.setText( _noButtonText );
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogNegativeClick( AlertDialogFragment.this, _requestCode);
                dismiss();
            }
        });

        ImageViewPlus imgExit = (ImageViewPlus) dialogView.findViewById(R.id.img_dialog_exit);
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayout llButtonsContainer = (LinearLayout) dialogView.findViewById(R.id.ll_buttons_container);
        if(_showButtons == false)
            llButtonsContainer.setVisibility(View.GONE);

        TextViewPlus bodyTxtView = (TextViewPlus) dialogView.findViewById( R.id.txt_promotion_body);
        Spannable alert = new SpannableString("هشدار:  ");
        alert.setSpan(new ForegroundColorSpan(
                getResources().getColor(R.color.full_sale_color)), 0, alert.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bodyTxtView.setText("");
        bodyTxtView.append(alert);
        bodyTxtView.append( _bodyText );
        dialogBuilder.setView(dialogView);

        AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        return b;
    }

}
