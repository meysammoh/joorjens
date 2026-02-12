package ir.joorjens.joorapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.adapters.DiscontentsGridAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.KandouBar;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.ToggleButtonPlus;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.DistributorsAPIs;

/**
 * Created by ZM on 5/25/2018.
 */

public class DistributorRateFragment extends DialogFragment implements ActivityServiceListener{
    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public boolean isActive() {
        return misActive;
    }

    public interface DialogListener {
        public void onDialogClick(DialogFragment dialog, int requestCode);
    }

    private boolean misActive;
    private ImageViewPlus mImgClose;
    private TextViewPlus mTvMsg;
    private GridView mGridDistDiscontents;
    private KandouBar mKb;

    private EditTextPlus mTbCmtOther;
    private ButtonPlus mBtnSubmit;

    private ActivityServiceListener mAsl;
    DistributorRateFragment.DialogListener mListener;
    private  String mDistName;
    private int mDistId;
    private int mRequestCode;

    public void setListeners(DistributorRateFragment.DialogListener lister, ActivityServiceListener asl){
        mListener = lister;
        mAsl = asl;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_distibutor_rate, container, false);

        mImgClose = (ImageViewPlus) dialogView.findViewById(R.id.dist_rate_img_close);
        mTvMsg = (TextViewPlus) dialogView.findViewById(R.id.dist_rate_tv_msg);
        mGridDistDiscontents = (GridView) dialogView.findViewById(R.id.grid_dist_discontents);
        ViewCompat.setNestedScrollingEnabled(mGridDistDiscontents, false);
        mKb = (KandouBar) dialogView.findViewById(R.id.dist_rate_kando);
        mTbCmtOther = (EditTextPlus) dialogView.findViewById(R.id.dist_rate_cmt_other);
        mBtnSubmit = (ButtonPlus) dialogView.findViewById(R.id.dist_rate_btn_submit);
        mBtnSubmit.setOnClickListener(doRate);


        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Bundle args = getArguments();
        if(args!=null){
            mDistName = args.getString("dist_name", "");
            mRequestCode = args.getInt("req_code", 0);
            mDistId = args.getInt("dist_id",0);
        }

        mTvMsg.setText(getResources().getString(R.string.msg_dist_rate_1) + "  " +
            mDistName + "  " + getResources().getString(R.string.msg_dist_rate_2));

        // create and set adaptor
        DiscontentsGridAdapter adapter = new DiscontentsGridAdapter(getContext());
        mGridDistDiscontents.setAdapter(adapter);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int wInDp = (int) (dm.widthPixels);
        int space = 10;
        int itemWidth = (wInDp - (space * 3))/2 - 10;
        mGridDistDiscontents.setColumnWidth(itemWidth);
        mGridDistDiscontents.setPadding(0,0,0,0);

        return dialogView;
    }

    private View.OnClickListener doRate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // call rating api
            DistributorsAPIs.rateDistributor(mAsl, Authenticator.loadAuthenticationToken(), 1);
            mListener.onDialogClick(DistributorRateFragment.this, mRequestCode);
            dismiss();
        }
    };

    @Override
    public void onResume() {
        misActive = true;
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
    public void onPause() {
        super.onPause();
        misActive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        misActive = false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


}
