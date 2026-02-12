package ir.joorjens.joorapp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.StreamHandler;

import javax.xml.datatype.Duration;

//import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Mohsen on 9/13/2017.
 */

import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.webService.APICreator;

public class StaticHelperFunctions {

    public static Map<Integer, String> PersianMonth = new HashMap<Integer, String>() {{
        put(1,"فروردین");
        put(2,"اردیبهشت");
        put(3,"خرداد");
        put(4,"تیر");
        put(5,"مرداد");
        put(6,"شهریور");
        put(7,"مهر");
        put(8,"آبان");
        put(9,"آذر");
        put(10,"دی");
        put(11,"بهمن");
        put(12,"اسفند");
    }};

    public static void call(Context context, final String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public static Integer getOnlyNumeric(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length() ; i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return Integer.valueOf(strBuff.toString());
    }

    public static boolean isOnline() {
        Context mContext = JJApp.getAppContext();
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPathFromURI(Context context, Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static String convertToBase64(String filePath) {
        try{
            File file = new File(filePath);
            InputStream fileInput = new FileInputStream(file);
            byte[] fileBytes = new byte[(int)file.length()];
            fileInput.read(fileBytes, 0, fileBytes.length);
            fileInput.close();
            String fileStr = Base64.encodeToString(fileBytes, Base64.DEFAULT);
            return fileStr;

        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static String convertToBase64(Bitmap bmp) {
        try{

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] fileBytes = stream.toByteArray();
            //bmp.recycle();
            String fileStr = Base64.encodeToString(fileBytes, Base64.DEFAULT);
            return "data:image/png;base64,"+fileStr;

        }catch (Exception ex){
            ex.printStackTrace();
            Log.d("TAG", "convertToBase64 error : " + ex.getMessage());
            return "";
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if(v != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setKeyboardAutoHide(View view, final Activity activity) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    StaticHelperFunctions.hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setKeyboardAutoHide(innerView, activity);
            }
        }
    }

    public static Pair<Integer, Integer> getDMs(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        Pair<Integer, Integer> pRet = new Pair<>(displayMetrics.widthPixels, displayMetrics.heightPixels);

        return  pRet;
    }

    public enum MessageType{
        Normal,
        Warning,
        Error,
        Alert,
        Confirm,
        Info,
        Loading
    }
    public static void showMessage_crotone(Activity activity, String message, MessageType messageType ){
        //Toast.makeText(activity, message, Toast.LENGTH_LONG);
        int bcColor = R.color.msg_alert_bc, fcColor = R.color.msg_alert_fc;
        if(messageType == MessageType.Confirm){
            bcColor = R.color.msg_confirm_bc;
            fcColor = R.color.msg_confirm_fc;
        }
        if(messageType == MessageType.Info){
            bcColor = R.color.msg_info_bc;
            fcColor = R.color.msg_info_fc;
        }

        Style style = new Style.Builder()
                .setBackgroundColor(bcColor)
                .setGravity(Gravity.CENTER)
                .setTextColor(fcColor)
                .setHeight(70)
                .build();
        if(message == null) message = "خطایی رخ داده است";
        if(activity != null && style != null) {
            final Crouton crouton = Crouton.makeText(activity, message, style).setConfiguration(
                    new Configuration.Builder()
                            .setDuration(4000).build());
            crouton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crouton.hide(crouton);
                }
            });

            crouton.show();

        }
    }

    // shows dialog of type 'Loading'
    public static SweetAlertDialog showLoadingDialog(final Activity activity, String message){
        int dialogType = SweetAlertDialog.PROGRESS_TYPE ;
        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }

    public interface DialogTimeFinishedListener{
        void onDialogFinished(DialogInterface dialog);
    }
    public interface DialogShowListener{
        void onDialogShow(DialogUI dialogUI);
    }
    public static class DialogUI{
        private FrameLayout _flProgress, _flSuccess;
        private TextView _tv;
        public DialogUI(FrameLayout flp, FrameLayout fls, TextView tv){
            _flProgress = flp;
            _flSuccess = fls;
            _tv = tv;
        }
        public void changeTypeToSuccess(String text){
            _flProgress.setVisibility(View.GONE);
            _flSuccess.setVisibility(View.VISIBLE);
            _tv.setText(text);
        }
    }
    // shows dialog of type 'Loading' and disappears after 'showTime' seconds
    public static SweetAlertDialog showTimedDialog(final Activity activity, String message,
                                                   MessageType messageType, int showTime,
                                                   final DialogTimeFinishedListener finishListener
                                                   /*,final DialogShowListener showListener*/){
        int dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Warning){
            dialogType = SweetAlertDialog.WARNING_TYPE;
        }else if(messageType == MessageType.Error){
            dialogType = SweetAlertDialog.ERROR_TYPE;
        }
        else if(messageType == MessageType.Confirm){
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        }
        else if(messageType == MessageType.Loading){
            dialogType = SweetAlertDialog.PROGRESS_TYPE;
        }

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(true);
            dialog.showCancelButton(false);
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            };
            final android.os.Handler handler  = new android.os.Handler();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handler.removeCallbacks(runnable);
                    finishListener.onDialogFinished(dialog);
                }
            });
            dialog.setTitleText(message);
            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setVisibility(View.GONE);

                    FrameLayout flP = (FrameLayout) alertDialog.findViewById(R.id.progress_dialog);
                    FrameLayout flS = (FrameLayout) alertDialog.findViewById(R.id.success_frame);
                    DialogUI du = new DialogUI(flP, flS, text) ;
                    //showListener.onDialogShow(du);
                }
            });
            dialog.show();
            handler.postDelayed(runnable, showTime * 1000);
            return dialog;
        }
        return null;
    }

    public static SweetAlertDialog showYesNoDialog(final Activity activity, String message,MessageType messageType,
                                                   SweetAlertDialog.OnSweetClickListener yesListener,
                                                   SweetAlertDialog.OnSweetClickListener noListener){
        int dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Warning){
            dialogType = SweetAlertDialog.WARNING_TYPE;
        }else if(messageType == MessageType.Error){
            dialogType = SweetAlertDialog.ERROR_TYPE;
        }
        else if(messageType == MessageType.Confirm){
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        }

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.showCancelButton(true);
            dialog.setConfirmClickListener(yesListener);
            dialog.setCancelClickListener(noListener);

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackgroundColor(activity.getResources().getColor(R.color.discount_color));
                    btn_confirm.setText("باشه");

                    Button btn_cancel = (Button) alertDialog.findViewById(R.id.cancel_button);
                    btn_cancel.setTypeface(font);
                    btn_cancel.setBackgroundColor(activity.getResources().getColor(R.color.discount_color));
                    btn_cancel.setText("بیخیال");
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }
    public static SweetAlertDialog showYesNoDialog(final Activity activity, String message, MessageType messageType,
                                                   SweetAlertDialog.OnSweetClickListener yesListener,
                                                   SweetAlertDialog.OnSweetClickListener noListener,
                                                   final String yesBtnText, final String noBtnText){
        int dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Warning){
            dialogType = SweetAlertDialog.WARNING_TYPE;
        }else if(messageType == MessageType.Error){
            dialogType = SweetAlertDialog.ERROR_TYPE;
        }
        else if(messageType == MessageType.Confirm){
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        }

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.setConfirmClickListener(yesListener);
            dialog.setCancelClickListener(noListener);
            dialog.showCancelButton(true);

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackground(activity.getResources().getDrawable(R.drawable.rounded_cheap_color));
                    btn_confirm.setText(yesBtnText);

                    Button btn_cancel = (Button) alertDialog.findViewById(R.id.cancel_button);
                    btn_cancel.setVisibility(View.VISIBLE);
                    btn_cancel.setTypeface(font);
                    btn_cancel.setBackground(activity.getResources().getDrawable(R.drawable.rounded_topsale_color));
                    btn_cancel.setText(noBtnText);
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }

    public static SweetAlertDialog showSelectDialog(final Activity activity, String message, MessageType messageType,
                                                   SweetAlertDialog.OnSweetClickListener yesListener,
                                                   SweetAlertDialog.OnSweetClickListener noListener,
                                                   final String yesBtnText, final String noBtnText){
        int dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Warning){
            dialogType = SweetAlertDialog.WARNING_TYPE;
        }else if(messageType == MessageType.Error){
            dialogType = SweetAlertDialog.ERROR_TYPE;
        }
        else if(messageType == MessageType.Confirm){
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        }

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(true);
            dialog.setTitleText(message);
            dialog.setConfirmClickListener(yesListener);
            dialog.setCancelClickListener(noListener);
            dialog.showCancelButton(true);

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setWidth(150);
                    btn_confirm.setBackground(activity.getResources().getDrawable(R.drawable.rounded_discount_color));
                    btn_confirm.setText(yesBtnText);

                    Button btn_cancel = (Button) alertDialog.findViewById(R.id.cancel_button);
                    btn_cancel.setWidth(150);
                    btn_cancel.setVisibility(View.VISIBLE);
                    btn_cancel.setTypeface(font);
                    btn_cancel.setBackground(activity.getResources().getDrawable(R.drawable.rounded_discount_color));
                    btn_cancel.setText(noBtnText);
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }


    public interface selectDialogListener {
        public void onCameraSelected(DialogInterface dialog, int requestCode);
        public void onGallerySelected(DialogInterface dialog, int requestCode);
    }
    public static void selectDialog(final Activity activity , final selectDialogListener listener, final int requestCode) {

        // dialog list entries
        final String[] items = {
                "عکس گرفتن",
                "انتخاب از گالری"
        };
        final int[] icons = {
                R.drawable.v_camera,
                R.drawable.v_photo
        };

        ListAdapter adapter = new ArrayAdapter<String>(
                JJApp.getAppContext(), R.layout.view_dialog_items, items) {

            class ViewHolder {
                ImageViewPlus icon;
                TextViewPlus title;
            }
            //ViewHolder holder;

            public View getView(int position, View convertView, ViewGroup parent) {
                final LayoutInflater inflater = (LayoutInflater) JJApp.getAppContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = new ViewHolder();
                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.view_dialog_items, null);

                    holder.icon = (ImageViewPlus) convertView
                            .findViewById(R.id.dialog_item_icon);
                    holder.title = (TextViewPlus) convertView
                            .findViewById(R.id.dialog_item_title);
                    convertView.setTag(holder);
                } else {
                    // view already defined, retrieve view holder
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.title.setText(items[position]);
                holder.icon.setImageResource(icons[position]);
                return convertView;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setTitle("انتخاب عکس");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("عکس گرفتن")) {
                    if(checkCameraPermission(activity))
                        listener.onCameraSelected(dialog, requestCode);
                }
                else if (items[item].equals("انتخاب از گالری")) {
                    if (checkExternalStoragePermission(activity))
                        listener.onGallerySelected(dialog, requestCode);
                }
            }
        });
        builder.show().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static SweetAlertDialog showConfirmDialog(final Activity activity, String message,
                                                   SweetAlertDialog.OnSweetClickListener confirmListener){
        int dialogType = SweetAlertDialog.SUCCESS_TYPE;

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            if(confirmListener != null)
                dialog.setConfirmClickListener(confirmListener);
            else{
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
            }

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackground(activity.getResources().getDrawable(R.drawable.rounded_discount_color));
                    btn_confirm.setText("باشه");
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }
    public static SweetAlertDialog showConfirmDialog(final Activity activity, String message, MessageType messageType,
                                                     SweetAlertDialog.OnSweetClickListener confirmListener,
                                                     final String confirmBtnText){
        int dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Warning)
            dialogType = SweetAlertDialog.WARNING_TYPE;
        else if(messageType == MessageType.Confirm)
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        else if(messageType == MessageType.Alert)
            dialogType = SweetAlertDialog.ERROR_TYPE;

        if(message == null) message = "";
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.setConfirmClickListener(confirmListener);

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackground(activity.getResources().getDrawable(R.drawable.rounded_discount_color));
                    btn_confirm.setText(confirmBtnText);
                }
            });
            dialog.show();
            return dialog;
        }
        return null;
    }


    public static void showMessage(final Activity activity, String message, MessageType messageType
    , SweetAlertDialog.OnSweetClickListener confirmListener,
                                   SweetAlertDialog.OnSweetClickListener cancelListener){
        int dialogType = SweetAlertDialog.SUCCESS_TYPE;
        int bcColor = R.color.msg_alert_bc, fcColor = R.color.msg_alert_fc;
        if(messageType == MessageType.Confirm){
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
            bcColor = R.color.msg_confirm_bc;
            fcColor = R.color.msg_confirm_fc;
        }
        if(messageType == MessageType.Info){
            dialogType = SweetAlertDialog.NORMAL_TYPE;
            bcColor = R.color.msg_info_bc;
            fcColor = R.color.msg_info_fc;
        }
        if(messageType == MessageType.Alert){
            dialogType = SweetAlertDialog.WARNING_TYPE;
            bcColor = R.color.msg_info_bc;
            fcColor = R.color.msg_info_fc;
        }

        if(message == null) {
            message = "خطایی رخ داده است";
        }
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.getProgressHelper().setBarColor(activity.getResources().getColor(R.color.cheap_color));
            dialog.setConfirmClickListener(confirmListener);
            dialog.setCancelClickListener(cancelListener);

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackgroundColor(activity.getResources().getColor(R.color.discount_color));
                    btn_confirm.setText("باشه");
                }
            });

            dialog.show();
        }
    }



    public static void showMessage(final Activity activity, String message, MessageType messageType){
        int dialogType = SweetAlertDialog.SUCCESS_TYPE;
        if(messageType == MessageType.Confirm)
            dialogType = SweetAlertDialog.SUCCESS_TYPE;
        if(messageType == MessageType.Info)
            dialogType = SweetAlertDialog.NORMAL_TYPE;
        if(messageType == MessageType.Alert)
            dialogType = SweetAlertDialog.WARNING_TYPE;

        if(message == null) {
            message = "خطایی رخ داده است";
        }
        if(activity != null) {
            final SweetAlertDialog dialog = new SweetAlertDialog(activity, dialogType);
            dialog.setCancelable(false);
            dialog.setTitleText(message);
            dialog.getProgressHelper().setBarColor(activity.getResources().getColor(R.color.cheap_color));

            dialog.setOnShowListener(new DialogInterface.OnShowListener(){
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = (TextView) alertDialog.findViewById(R.id.title_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/yr.ttf");
                    text.setTypeface(font);
                    text.setGravity(Gravity.CENTER);
                    text.setSingleLine(false);

                    Button btn_confirm = (Button) alertDialog.findViewById(R.id.confirm_button);
                    btn_confirm.setTypeface(font);
                    btn_confirm.setBackground(activity.getResources().getDrawable(R.drawable.rounded_discount_color));
                    btn_confirm.setText("باشه");
                }
            });

            dialog.show();
        }
    }

    public static void showMessage(View rootView, String message, MessageType messageType ){
        Snackbar snackbar = Snackbar
                .make(rootView, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void openActivity(Context context, Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP /*|
                        Intent.FLAG_ACTIVITY_NO_HISTORY */);
        //ComponentName cn = intent.getComponent();
        //Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        context.startActivity(intent);
    }
    public static int getTimeInSeconds(int daysToShift ) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToShift);
        return (int) (calendar.getTimeInMillis() / 1000 );
    }

    /**
     * Helper method to recreate the Activity. This method should be called after a Locale change.
     * @param activity the Activity that will be recreated
     * @param animate a flag indicating if the recreation will be animated or not
     */
    public static void recreate(AppCompatActivity activity, boolean animate) {
        if (animate) {
            Intent restartIntent = new Intent(activity, activity.getClass());

            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                restartIntent.putExtras(extras);
            }
            ActivityCompat.startActivity(
                    activity,
                    restartIntent,
                    ActivityOptionsCompat
                            .makeCustomAnimation(activity, android.R.anim.fade_in, android.R.anim.fade_out)
                            .toBundle()
            );
            activity.finish();
        } else {
            activity.recreate();
        }
    }

    public static void loadImage(final Context context, final String relativeURL, final ImageView imgv){
        Picasso picasso = PicassoCache.get(context);
        picasso.load(APICreator.getImagesBaseAddress() + relativeURL)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imgv, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso picasso = PicassoCache.get(context);
                        picasso.load(APICreator.getImagesBaseAddress() + relativeURL)
                                .error(R.drawable.no_image)
                                .into(imgv);
                    }
                });


//        Picasso.with(context)
//                .load(APICreator.getImagesBaseAddress() + relativeURL)
//                .error(R.drawable.no_image)
//                .into(imgv);
    }

    public static String getTimeDiffFromCurent(long time){
        Date d1 = new Date(time);
        Date d2 = Calendar.getInstance().getTime();
        long diff = d2.getTime()-d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long years = diffDays / 365;

        String ret = years > 0 ? years + " سال" : "کمتر از یک سال";

        return  ret;
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 994;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static boolean checkPermission(final Context context, final String permissionStr,
                                           String msg, final int requestCode)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, permissionStr) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionStr)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage(msg);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{permissionStr},
                                    requestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{permissionStr},
                            requestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkExternalStoragePermission(final Context context){
        return checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE,
                "External storage permission is necessary", MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    public static boolean checkCameraPermission(final Context context){
        return checkPermission(context, Manifest.permission.CAMERA,
                "Camera permission is necessary", MY_PERMISSIONS_REQUEST_CAMERA);
    }
}
