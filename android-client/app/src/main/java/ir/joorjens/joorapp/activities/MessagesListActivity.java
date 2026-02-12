package ir.joorjens.joorapp.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import  ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.MessagesListAdapter;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.models.Message;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.MessageAPIs;

public class MessagesListActivity extends HomeBaseActivity implements ActivityServiceListener{


    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public boolean isActive() {
        return _isActive;
    }

    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;

        MessageAPIs.getMyMessages(MessagesListActivity.this,
                Authenticator.loadAuthenticationToken(), mOffset, APICreator.maxResult, 1);

        mAdapter = new MessagesListAdapter(this, null);
        mAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if (isActive() && !mDataReachedEnd) {
                    MessageAPIs.getMyMessages(MessagesListActivity.this,
                            Authenticator.loadAuthenticationToken(), mOffset, APICreator.maxResult, 1);
                }
            }
        });
        prepareRecyclerView();
    }
    @Override
    public void onStop() {
        super.onStop();
        _isActive = false;
    }
    // ------------------------------ isActive ---------------------------

    private SwipeRefreshLayout.OnRefreshListener onRefreshMessages = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            StaticHelperFunctions.recreate(MessagesListActivity.this, true);
            mRefreshLayout.setRefreshing(false);
        }
    };

    private MessagesListAdapter mAdapter;
    private int mOffset;
    private boolean mDataReachedEnd =false;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRcvMessages;

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.MessagesList;
    }

    @Override
    public String getActivityTitle() {
        return "لیست پیام ها";
    }

    private void prepareRecyclerView(){
        mOffset = 0;
        mDataReachedEnd = false;
        mRcvMessages.removeAllViews();
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessagesListActivity.this,
                LinearLayoutManager.VERTICAL, false);
        mRcvMessages.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(this, true));
        mRcvMessages.setItemAnimator(new DefaultItemAnimator());
        try {
            LinearSnapHelper helper_cheap = new LinearSnapHelper();
            helper_cheap.attachToRecyclerView(mRcvMessages);
        }catch (Exception ex){}

        mRcvMessages.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_messages_list, ActivityKeys.MessagesList);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ma_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshMessages);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color
                ,R.color.new_color);

        mRcvMessages = (RecyclerView) findViewById(R.id.rcv_messages);
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchMessages) {
            ResultList<Message> messages = (ResultList<Message>) data;
            if ((mOffset + 1) * APICreator.maxResult < messages.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;

            mAdapter.addNewItems(messages.getResult());
        }
        else if(apiCode == APICode.seenMessageReceiver){
            // unbold by setting seen = true
            mAdapter.getItem(requestCode).getMessageReceivers().get(0).setSeen(true);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        super.onServiceFail(apiCode, data, requestCode);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        super.onNetworkFail(apiCode, data, requestCode);
    }
}
