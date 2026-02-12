package ir.joorjens.joorapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.DiscountBigListAdapter;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostBigListDistributorProductAdapter;
import ir.joorjens.joorapp.adapters.TheMostBigListProductAdapter;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.TitleBar2;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.DistributorPackageAPIs;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;
import ir.joorjens.joorapp.webService.ProductAPIs;
import okhttp3.Cache;

public class ShowAllTheMostActivity extends HomeBaseActivity {

    private enum ContentType{
        Newest, Cheapest, TopSale, Discount,SearchDProduct
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshShowAllTheMost = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            StaticHelperFunctions.recreate(ShowAllTheMostActivity.this, true);
            mRefreshLayout.setRefreshing(false);
        }
    };

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;

        // 1 = down; -1 = up; 0 = up or down
        if(mRecyclerViewProducts.canScrollVertically(-1))
            HomeBaseActivity.setFabVisibility(true);
        else
            HomeBaseActivity.setFabVisibility(false);
        HomeBaseActivity.setFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewProducts.smoothScrollToPosition(0);
            }
        });

//        if(!mMainScroll.canScrollVertically(1)){
//            mRefreshLayout.setEnabled(true);
//        }

        Bundle b = getIntent().getExtras();

        mColorResourceId = b.getInt("color_id",-1);
        mIconResourceId = b.getInt("icon_id");
        mTitle = b.getString("title");
        if(mColorResourceId == -1)
            mTitleBar.setVisibility(View.GONE);
        else {
            mTitleBar.setVisibility(View.VISIBLE);
            mTitleBar.setColor(mColorResourceId);
            mTitleBar.setIcon(mIconResourceId);
            mTitleBar.setTitle(mTitle);
        }

        switch (mColorResourceId) {
            case R.color.cheap_color:
                mDProductAdapter = new TheMostBigListDistributorProductAdapter(this, null, mColorResourceId);
                mDProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        if (isActive() && !mDataReachedEnd) {
                            DistributorProductAPIs.getCheapestDProducts(ShowAllTheMostActivity.this,
                                    Authenticator.loadAuthenticationToken(), mOffset, 1);
                        }
                    }
                });
                prepareRecyclerView(ContentType.Cheapest);
                DistributorProductAPIs.getCheapestDProducts(this, Authenticator.loadAuthenticationToken(),mOffset , 1);
                break;
            case R.color.new_color:
                mProductAdapter = new TheMostBigListProductAdapter(this, mColorResourceId);
                mProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                    if(!mDataReachedEnd)
                        ProductAPIs.getNewestProducts(ShowAllTheMostActivity.this,
                                Authenticator.loadAuthenticationToken(), mOffset, 1);
                    }
                });
                prepareRecyclerView(ContentType.Newest);
                ProductAPIs.getNewestProducts(this, Authenticator.loadAuthenticationToken(),mOffset, 1);
                break;
            case R.color.full_sale_color:
                mProductAdapter = new TheMostBigListProductAdapter(this, mColorResourceId);
                mProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                    if(!mDataReachedEnd)
                        ProductAPIs.getTopSellingProducts(ShowAllTheMostActivity.this,
                                Authenticator.loadAuthenticationToken(), mOffset, 1);
                    }
                });
                prepareRecyclerView(ContentType.TopSale);
                ProductAPIs.getTopSellingProducts(this, Authenticator.loadAuthenticationToken(), mOffset, 1);
                break;
            case R.color.discount_color:
                mPackageAdapter = new DiscountBigListAdapter(this, null);
                mPackageAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        if (!mDataReachedEnd)
                            DistributorPackageAPIs.getDiscountDPackages(ShowAllTheMostActivity.this,
                                    Authenticator.loadAuthenticationToken(), mOffset, 1);
                    }
                });
                prepareRecyclerView(ContentType.Discount);
                DistributorPackageAPIs.getDiscountDPackages(this, Authenticator.loadAuthenticationToken(), mOffset, 1);
                break;
            case R.color.show_in_bundling_or_discount_color:
                String pid = b.getInt("productId")+"";
                String bOrd = b.getBoolean("bundlingOrDiscount") + "";
                options = new HashMap<>();
                options.put("productId", pid);
                options.put("bundlingOrDiscount", bOrd);
                DistributorPackageAPIs.searchDPackages(this, Authenticator.loadAuthenticationToken(),options, mOffset, 1);
                mPackageAdapter = new DiscountBigListAdapter(this, null);
                mPackageAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        if (!mDataReachedEnd)
                            DistributorPackageAPIs.searchDPackages(ShowAllTheMostActivity.this,
                                    Authenticator.loadAuthenticationToken(), options, mOffset, 1);
                    }
                });
                prepareRecyclerView(ContentType.Discount);
                break;
            case -1:
                mOffset = 0;
                Bundle searchOptions = b.getBundle("search_options");
                boolean isBundle = b.getBoolean("is_bundle", false);
                options = new HashMap<>();
                for (String key : searchOptions.keySet()) {
                    options.put(key, searchOptions.getString(key));
                }

                if(isBundle){
                    DistributorPackageAPIs.searchDPackages(this, Authenticator.loadAuthenticationToken(), options, mOffset, 1);
                    mPackageAdapter = new DiscountBigListAdapter(this, null);
                    mPackageAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                        @Override
                        public void onEndReached(int position) {
                            if (!mDataReachedEnd)
                                DistributorPackageAPIs.searchDPackages(ShowAllTheMostActivity.this,
                                        Authenticator.loadAuthenticationToken(), options, mOffset, 1);
                        }
                    });
                    prepareRecyclerView(ContentType.Discount);
                }
                else {
                    DistributorProductAPIs.searchDProducts(this, Authenticator.loadAuthenticationToken(), options, mOffset, 1);
                    mDProductAdapter = new TheMostBigListDistributorProductAdapter(this, null, mColorResourceId);
                    mDProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                        @Override
                        public void onEndReached(int position) {
                            if (isActive() && !mDataReachedEnd) {
                                DistributorProductAPIs.searchDProducts(ShowAllTheMostActivity.this,
                                        Authenticator.loadAuthenticationToken(), options, mOffset, 1);
                            }
                        }
                    });
                    prepareRecyclerView(ContentType.SearchDProduct);
                }
                break;
        }
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

    private Map<String, String> options;

    int mColorResourceId = -1;
    int mIconResourceId;
    String mTitle;
    TitleBar2 mTitleBar;
    TextViewPlus mMessageTextView;
    RecyclerView mRecyclerViewProducts;

    private SwipeRefreshLayout mRefreshLayout;
    private NestedScrollView mMainScroll;

    private int mOffset = 0;
    private boolean mDataReachedEnd =false;

    private TheMostBigListDistributorProductAdapter mDProductAdapter;
    private TheMostBigListProductAdapter mProductAdapter;
    private DiscountBigListAdapter mPackageAdapter;

    private void prepareRecyclerView(ContentType contentType){
        mOffset = 0;
        mDataReachedEnd = false;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.satm_rcv_products);
        recyclerView.removeAllViews();
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowAllTheMostActivity.this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShowAllTheMostActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(this, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        try {
            LinearSnapHelper helper_cheap = new LinearSnapHelper();
            helper_cheap.attachToRecyclerView(recyclerView);
        }catch (Exception ex){}


        switch(contentType){
            case Cheapest:
                mOffset = CacheContainer.get().getCheapOffset();
                mDataReachedEnd = CacheContainer.get().isCheapReachedEnd();
                recyclerView.setAdapter(mDProductAdapter);
                break;
            case Newest:
                mOffset = CacheContainer.get().getNewOffset();
                mDataReachedEnd = CacheContainer.get().isNewReachedEnd();
                recyclerView.setAdapter(mProductAdapter);
                break;
            case TopSale:
                mOffset = CacheContainer.get().getTopSaleOffset();
                mDataReachedEnd = CacheContainer.get().isTopSaleReachedEnd();
                recyclerView.setAdapter(mProductAdapter);
                break;
            case Discount:
                mOffset = CacheContainer.get().getDiscountOffset();
                mDataReachedEnd = CacheContainer.get().isDiscountReachedEnd();
                recyclerView.setAdapter(mPackageAdapter);
                break;
            case SearchDProduct:
                mOffset = 0;
                mDataReachedEnd = false;
                recyclerView.setAdapter(mDProductAdapter);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_show_all_the_most, ActivityKeys.ShowAllTheMost);

        mRecyclerViewProducts = (RecyclerView) findViewById(R.id.satm_rcv_products);
        ViewCompat.setNestedScrollingEnabled(mRecyclerViewProducts, true);
        mRecyclerViewProducts.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(mRecyclerViewProducts.canScrollVertically(-1))
                    HomeBaseActivity.setFabVisibility(true);
                else
                    HomeBaseActivity.setFabVisibility(false);
            }
        });

        mTitleBar = (TitleBar2) findViewById(R.id.tb2_satm);

        mMessageTextView = (TextViewPlus) findViewById( R.id.tv_shown_all_most_result_message );

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.satm_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshShowAllTheMost);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color
        ,R.color.new_color);

    }

    private void showTheMostItems(ContentType contentType) {

        int size = 0;
        switch(contentType){
            case Cheapest:
                size = CacheContainer.get().getCheapProducts().size();
                mDProductAdapter.notifyDataSetChanged();
                break;
            case Newest:
                size = CacheContainer.get().getNewProducts().size();
                mProductAdapter.notifyDataSetChanged();
                break;
            case TopSale:
                size = CacheContainer.get().getTopSaleProducts().size();
                mProductAdapter.notifyDataSetChanged();
                break;
            case Discount:
                size = CacheContainer.get().getDiscountPackages().size();
                mPackageAdapter.notifyDataSetChanged();
                break;
        }


        if(size > 0){
            mMessageTextView.setVisibility(View.GONE);
        }
        else{
            mMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showTheMostDProducts(ResultList<DistributorProduct> productList, int colorId) {
        ArrayList<DistributorProduct> products = (ArrayList<DistributorProduct>) productList.getResult();
        mDProductAdapter.addNewItems(products);
        if(mDProductAdapter.getItemCount() > 0){
            mMessageTextView.setVisibility(View.GONE);
        }
        else{
            mMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showDiscountDPackages(ResultList<DistributorPackage> packageList) {
        ArrayList<DistributorPackage> packages = (ArrayList<DistributorPackage>) packageList.getResult();
        mPackageAdapter.addNewItems(packages);
        if(mPackageAdapter.getItemCount() > 0){
            mMessageTextView.setVisibility(View.GONE);
        }
        else{
            mMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        int colorId = -1;
        switch (apiCode) {
            case getCheapestDProducts:
                colorId = R.color.cheap_color;
                break;
            case getNewestProducts:
                colorId = R.color.new_color;
                break;
            case getTopSellingProducts:
                colorId = R.color.full_sale_color;
                break;
            case getDiscountDProducts:
                colorId = R.color.discount_color;
                break;
        }

        if(apiCode == APICode.getDiscountDProducts ){
            ResultList<DistributorPackage> packages = (ResultList<DistributorPackage>) data;
            mDataReachedEnd = CacheContainer.get().addDiscountPackages(packages);
            mOffset = CacheContainer.get().getDiscountOffset();
            showTheMostItems(ContentType.Discount);
        }
        if(apiCode == APICode.searchDistributorPackages ){
            ResultList<DistributorPackage> packages = (ResultList<DistributorPackage>) data;
            if((mOffset +1)* APICreator.maxResult < packages.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;
            showDiscountDPackages(packages);
        }
        else if(apiCode == APICode.getCheapestDProducts){
            ResultList<DistributorProduct> dProducts = (ResultList<DistributorProduct>) data;
            mDataReachedEnd = CacheContainer.get().addCheapProducts(dProducts);
            mOffset = CacheContainer.get().getCheapOffset();
            showTheMostItems(ContentType.Cheapest);
        }

        else if(apiCode == APICode.searchDistributorProduct){
            ResultList<DistributorProduct> dProducts = (ResultList<DistributorProduct>) data;
            if((mOffset +1)* APICreator.maxResult < dProducts.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;
            showTheMostDProducts(dProducts, colorId);
        }

        else if(apiCode == APICode.getNewestProducts){
            ResultList<Product> products = (ResultList<Product>) data;
            mDataReachedEnd = CacheContainer.get().addNewProducts(products);
            mOffset = CacheContainer.get().getNewOffset();
            showTheMostItems(ContentType.Newest);
        }

        else if(apiCode == APICode.getTopSellingProducts){
            ResultList<Product> products = (ResultList<Product>) data;
            mDataReachedEnd = CacheContainer.get().addTopSaleProducts(products);
            mOffset = CacheContainer.get().getTopSaleOffset();
            showTheMostItems(ContentType.TopSale);
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode != APICode.getCart )
            StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

}
