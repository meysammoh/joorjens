package ir.joorjens.joorapp.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.fragments.RecoveryMobileFragment;

public class RecoverPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        if (savedInstanceState == null) {
            RecoveryMobileFragment newFragment = RecoveryMobileFragment.newInstance("");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.recovery_fragment_container, newFragment).commit();
        }
    }
}
