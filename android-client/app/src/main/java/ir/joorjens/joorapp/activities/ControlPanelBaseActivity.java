package ir.joorjens.joorapp.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;

public class ControlPanelBaseActivity  extends AppCompatActivity implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        _isActive = true;
    }
    @Override
    public void onStop() {
        _isActive = false;
        super.onStop();
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {

    }

    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    FrameLayout mContentFrame;
    //@Override
    public ActivityKeys getActivityId() {
        return null;
    }

   //@Override
    public String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.addContentView(R.layout.activity_control_panel_base);
        setContentView(R.layout.activity_control_panel_base);
        mContentFrame = (FrameLayout) findViewById(R.id.cpba_content_frame);

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.cpba_main), ControlPanelBaseActivity.this);

        TextViewPlus activityTitle = (TextViewPlus)findViewById(R.id.action_bar_activity_title);
        //String tt = getActivityTitle();
        //activityTitle.setText(tt);
    }

    public void addContentView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layoutId, mContentFrame, false);
        mContentFrame.addView(contentView, 0);

        StaticHelperFunctions.setKeyboardAutoHide(findViewById(R.id.cpba_content_frame), ControlPanelBaseActivity.this);
    }
}
