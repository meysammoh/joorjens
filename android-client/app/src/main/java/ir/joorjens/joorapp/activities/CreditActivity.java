package ir.joorjens.joorapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.utils.Authenticator;

public class CreditActivity extends ControlPanelBaseActivity implements ActivityServiceListener {

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
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    private TextViewPlus mTvCredit;
    private ButtonPlus mBtnCredit2ht;
    private ButtonPlus mBtnCredit5ht;
    private ButtonPlus mBtnCredit1m;
    private ButtonPlus mBtnCreditDesired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_credit);

        mTvCredit = (TextViewPlus) findViewById(R.id.tv_credit);
        mBtnCredit2ht = (ButtonPlus) findViewById(R.id.btn_credit_2ht);
        mBtnCredit5ht = (ButtonPlus) findViewById(R.id.btn_credit_5ht);
        mBtnCredit1m = (ButtonPlus) findViewById(R.id.btn_credit_1m);
        mBtnCreditDesired = (ButtonPlus) findViewById(R.id.btn_credit_desired);

        Profile p = Authenticator.loadAccount();
        mTvCredit.setText(String.format("%,d", p.getCredit()) + " تومان");
        int i1 = 200000, i2 = 500000, i3 = 1000000;
        mBtnCredit2ht.setText(String.format("%,d", i1) + " تومان");
        mBtnCredit5ht.setText(String.format("%,d", i2) + " تومان");
        mBtnCredit1m.setText(String.format("%,d", i3) + " تومان");
    }

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.Credit;
    }

    @Override
    public String getActivityTitle() {
        return "اعتبار" ;
    }
}
