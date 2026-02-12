package ir.joorjens.joorapp.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.TransactionRow;
import ir.joorjens.joorapp.fragments.AlertDialogFragment;
import ir.joorjens.joorapp.fragments.TurnoverSearchFragment;
import ir.joorjens.joorapp.fragments.ViewCartFragment;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Transaction;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.TransactionAPIs;

public class TurnoverActivity extends BaseActivity implements ActivityServiceListener, TurnoverSearchFragment.DialogListener{

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

    private ButtonPlus _btnSearch;
    private TextViewPlus _tvNoTransaction;

    private LinearLayout _llTransactionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_turnover);

        _tvNoTransaction = (TextViewPlus) findViewById(R.id.tv_no_transaction);
        _btnSearch = (ButtonPlus) findViewById(R.id.toa_btn_search);
        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TurnoverSearchFragment dialog = new TurnoverSearchFragment();
                dialog.setListener(TurnoverActivity.this);
                Bundle b = new Bundle();
                b.putInt(AlertDialogFragment.ParamNameRequestCodeInteger, 2000);
                dialog.setArguments(b);
                dialog.show(getSupportFragmentManager(), "TurnoverSearchFragment");
            }
        });
        _llTransactionsContainer = (LinearLayout) findViewById(R.id.ll_transactions_container);

        TransactionAPIs.searchProduct(this, Authenticator.loadAuthenticationToken(),
                Authenticator.loadAccount().getId(),0,0,1);
    }

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.Turnover;
    }

    @Override
    public String getActivityTitle() {
        Profile p = Authenticator.loadAccount();
        return "گردش حساب             موجودی: "  +  String.format("%, d", p.getCredit())  + " تومان";
    }

    @Override
    public void onDialogClick(DialogFragment dialog, int requestCode) {
        if(requestCode == 2000){
            // get date from and to and transaction type and do search
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchTransaction){
            ResultList<Transaction> res = (ResultList<Transaction>)data;
            if(res.getResult().size() == 0){
                _tvNoTransaction.setVisibility(View.VISIBLE);
            }else {
                _tvNoTransaction.setVisibility(View.GONE);
                _llTransactionsContainer.removeAllViews();
                for (Transaction transaction : res.getResult()) {
                    int colorId = R.color.full_sale_color;
                    if (transaction.getCredit()) { // discount color
                        colorId = R.color.discount_color;
                    } else if (transaction.getSheba()) { // cheap color
                        colorId = R.color.cheap_color;
                    }

                    TransactionRow row = new TransactionRow(this);
                    row.setRowValues(transaction.getInvoiceTime(), transaction.getAmountStr(),
                            transaction.getNote(), colorId);
                    _llTransactionsContainer.addView(row);
                }
            }
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
