package ir.joorjens.joorapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.AdvertisingAdapter;
import ir.joorjens.joorapp.models.Advertising;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.UserAPIs;

public class ConnectionAttemptActivity extends Activity implements ActivityServiceListener
{
        // ------------------------------ isActive ---------------------------
        private boolean _isActive;
        @Override
        protected void onResume() {
            super.onResume();
            _isActive = true;
        }
        @Override
        protected void onStop() {
            _isActive = false;
            super.onStop();
        }
        @Override
        public boolean isActive() {
            return _isActive;
        }
        // ------------------------------ isActive ---------------------------

    @BindView(R.id.txt_wait) TextView mTxtWait;
    @BindView(R.id.btn_wait_retry) TextView mTxtWaitRetry;
    @BindView(R.id.prgbar_wait) ProgressBar mPrgbarWait;
    private static final int GET_PROFILE_ID = 1;

    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_attempt);
        ButterKnife.bind(this);
        mTxtWaitRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnectionStatus();
            }
        });

        checkConnectionStatus();
    }

    private void checkConnectionStatus(){
        mPrgbarWait.setVisibility(View.VISIBLE);
        mTxtWaitRetry.setVisibility(View.GONE);
        boolean online = StaticHelperFunctions.isOnline();
        if(online){
            chooseStartPage();
        }else{
            mTxtWait.setText("خطا در اتصال به اینترنت");
            mPrgbarWait.setVisibility(View.GONE);
            mTxtWaitRetry.setVisibility(View.VISIBLE);
        }
    }

    private void chooseStartPage(){
        mPrgbarWait.setVisibility(View.VISIBLE);
        mTxtWaitRetry.setVisibility(View.GONE);
        mTxtWait.setText("در حال اتصال به سرور ...");

        Authenticator.Initialize(this);
        if (Authenticator.hasRegisterdUser()) {
            // check if token is valid or expire
            authToken = Authenticator.loadAuthenticationToken();
            UserAPIs.getProfile(this, authToken, 0);
        }
        else {
            // go to login page
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        Profile p = (Profile)data;
        Authenticator.saveAccount(p, authToken);

        // open start activity
        Intent myIntent = new Intent(ConnectionAttemptActivity.this, StartActivity.class);
        startActivity(myIntent);
        finish();

    }


    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        mPrgbarWait.setVisibility(View.GONE);
        mTxtWaitRetry.setVisibility(View.VISIBLE);
        ServiceResponse sresp = (ServiceResponse)data;
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        Throwable sresp = (Throwable)data;
        mPrgbarWait.setVisibility(View.GONE);
        mTxtWaitRetry.setVisibility(View.VISIBLE);
        mTxtWait.setText("خطا در برقراری ارتباط.");
        //StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
