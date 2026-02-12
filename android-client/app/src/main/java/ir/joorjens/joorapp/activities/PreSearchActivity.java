package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.ProductGridAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ScrollableGridView;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.ColumnItemDecoration;
import ir.joorjens.joorapp.utils.EqualSpacingItemDecoration;
import ir.joorjens.joorapp.utils.GridDividerDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;

public class PreSearchActivity extends HomeBaseActivity {

    private ButtonPlus mBtnGotoSearch;
    private SwipeRefreshLayout mRefreshLayout;
    private int mOffset = 0;
    private boolean mDataReachedEnd =false;
    private ProductGridAdapter mProductAdapter;
    private GridView mGridProduct;
    private int prevSY = 0, currSY = 0;


    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onStop() {
        _isActive = false;
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;

        mOffset = CacheContainer.get().getAllOffset();
        mDataReachedEnd = CacheContainer.get().isAllReachedEnd();

        // 1 = down; -1 = up; 0 = up or down
        if(mGridProduct.canScrollVertically(-1))
            HomeBaseActivity.setFabVisibility(true);
        else
            HomeBaseActivity.setFabVisibility(false);

        HomeBaseActivity.setFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridProduct.smoothScrollToPositionFromTop(0,0);
            }
        });
    }

    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    @Override
    public String getActivityTitle() {
        return "جستجوی محصولات";
    }

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.PreSearch;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshShowAllTheMost = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            StaticHelperFunctions.recreate(PreSearchActivity.this, true);
            mRefreshLayout.setRefreshing(false);
        }
    };

    private void prepareGrid(){
        mOffset = 0;
        mDataReachedEnd = false;
        //mGridProduct.removeAllViews();
        mGridProduct.setAdapter(mProductAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_pre_search, ActivityKeys.PreSearch);

        mBtnGotoSearch = (ButtonPlus) findViewById(R.id.ps_btn_goto_search);
        mBtnGotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(PreSearchActivity.this, SearchProductActivity.class);
                searchIntent.putExtra("logo_id", 5);
                StaticHelperFunctions.openActivity(PreSearchActivity.this, searchIntent);
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ps_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshShowAllTheMost);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color
                ,R.color.new_color);

        mGridProduct = (GridView) findViewById(R.id.grid_product);
        ViewCompat.setNestedScrollingEnabled(mGridProduct, false);


        mGridProduct.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                currSY = getGridScrollY();
                //Log.d("sc", "___________________________cu : " + currSY + " ps: " + prevSY);
                if(currSY > prevSY){
                    PreSearchActivity.super.setBottomBarVisibility(false);
                }else if (currSY < prevSY){
                    PreSearchActivity.super.setBottomBarVisibility(true);
                }
                prevSY = currSY;

                // 1 = down; -1 = up; 0 = up or down
                if(mGridProduct.canScrollVertically(-1)) {
                    HomeBaseActivity.setFabVisibility(true);
                    mRefreshLayout.setEnabled(false);
                }
                else {
                    HomeBaseActivity.setFabVisibility(false);
                    mRefreshLayout.setEnabled(true);
                }
            }
        });

        mProductAdapter = new ProductGridAdapter(this);
        mProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if (isActive() && !mDataReachedEnd) {
                    DistributorProductAPIs.getAllDProducts(PreSearchActivity.this,
                            Authenticator.loadAuthenticationToken(), mOffset, 1);
                }
            }
        });

        prepareGrid();
        DistributorProductAPIs.getAllDProducts(this, Authenticator.loadAuthenticationToken(), mOffset, 1);
    }

    public int getGridScrollY()
    {
        int pos, itemY = 0;
        View view;

        pos = mGridProduct.getFirstVisiblePosition();
        view = mGridProduct.getChildAt(0);

        if(view != null)
            itemY = view.getTop();

        return Math.abs(itemY);
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.getAllDProducts ){
            ResultList<DistributorProduct> products = (ResultList<DistributorProduct>) data;
            mDataReachedEnd = CacheContainer.get().addAllProducts(products);
            mOffset = CacheContainer.get().getAllOffset();
            mProductAdapter.notifyDataSetChanged();

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int wInDp = (int) (dm.widthPixels);
            int space = 10;
            int itemWidth = (wInDp - (space * 4))/3;
            mGridProduct.setColumnWidth(itemWidth);
            mGridProduct.setVerticalSpacing(space);
            mGridProduct.setPadding(0,space,0,space);
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
