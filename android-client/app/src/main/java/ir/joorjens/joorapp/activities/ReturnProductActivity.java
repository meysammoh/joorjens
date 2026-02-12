package ir.joorjens.joorapp.activities;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.fragments.ReturnProductP1Fragment;

public class ReturnProductActivity extends ControlPanelBaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_return_product);

        if (savedInstanceState == null) {
            ReturnProductP1Fragment rpp1Fragment = ReturnProductP1Fragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rpa_fragments_container, rpp1Fragment).commit();
        }

    }

    public ActivityKeys getActivityId() {
        return ActivityKeys.ReturnProduct;
    }

    @Override
    public String getActivityTitle() {
        return "برگشت کالا";
    }

}
