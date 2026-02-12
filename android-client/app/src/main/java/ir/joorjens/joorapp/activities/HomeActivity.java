package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.AdvertisingAdapter;
import ir.joorjens.joorapp.adapters.DiscountListAdapter;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.adapters.TheMostListProductAdapter;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextSliderView;
import ir.joorjens.joorapp.customViews.TitleBar;
import ir.joorjens.joorapp.models.Advertising;
import ir.joorjens.joorapp.models.Banner;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.OrderStatusController;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.CustomFontHelper;
import ir.joorjens.joorapp.utils.SpacesItemDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.AdvertisingAPIs;
import ir.joorjens.joorapp.webService.BannerAPIs;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.DistributorPackageAPIs;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;
import ir.joorjens.joorapp.webService.MessageAPIs;
import ir.joorjens.joorapp.webService.ProductAPIs;
import ir.joorjens.joorapp.webService.TypeAPIs;
import ir.joorjens.joorapp.webService.params.SeenMessageParams;

public class HomeActivity extends HomeBaseActivity implements ActivityServiceListener {

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------

    TitleBar mTbHpDiscount;
    TitleBar mTbHpFullSale;
    TitleBar mTbHpCheap;
    TitleBar mTbHpNew;

    ImageViewPlus mImgHpDiscount;
    ImageViewPlus mImgHpFullSale;
    ImageViewPlus mImgHpCheap;
    ImageViewPlus mImgHpNew;

    private int rcvAdvPosition = 0;
    private Handler mRcvAdvScrollHandler = new Handler();
    private Runnable mAdvScroller;

    private NestedScrollView mScrollView;
    private SwipeRefreshLayout mRefreshLayout;

    private DiscreteScrollView dsv1;
    private DiscreteScrollView dsv2;
    private int mAdvertisingShowTimeMills = 5000;

    private int cheapOffset =0, newOffset =0, topSaleOffset =0, discountOffset =0;
    private boolean cheapReachedEnd =false, newReachedEnd =false, topSaleReachedEnd =false, discountReachedEnd =false;

    private RecyclerView recyclerView_cheap, recyclerView_new, recyclerView_top_sale, recyclerView_discount;
    private TheMostListDistributorProductAdapter cheapAdapter;
    private TheMostListProductAdapter newAdapter;
    private TheMostListProductAdapter topSaleAdapter;
    private DiscountListAdapter discountAdapter;


    SliderLayout mainSlider;

    private SwipeRefreshLayout.OnRefreshListener onRefreshHome = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            StaticHelperFunctions.recreate(HomeActivity.this,true);
            mRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.Home;
    }

    @Override
    public String getActivityTitle() {
        return "خانه";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_home, ActivityKeys.Home);

        cheapAdapter = new TheMostListDistributorProductAdapter(this, null, R.color.cheap_color);
        cheapAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if(!cheapReachedEnd)
                    DistributorProductAPIs.getCheapestDProducts(HomeActivity.this,
                            Authenticator.loadAuthenticationToken(), cheapOffset, 1);
            }
        });

        newAdapter = new TheMostListProductAdapter(this, null, R.color.new_color);
        newAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if(!newReachedEnd)
                    ProductAPIs.getNewestProducts(HomeActivity.this,
                            Authenticator.loadAuthenticationToken(), newOffset, 1);
            }
        });
        topSaleAdapter = new TheMostListProductAdapter(this, null, R.color.full_sale_color);
        topSaleAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if(!topSaleReachedEnd)
                    ProductAPIs.getTopSellingProducts(HomeActivity.this,
                            Authenticator.loadAuthenticationToken(), topSaleOffset, 1);
            }
        });

        discountAdapter = new DiscountListAdapter(this, null);
        discountAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if(!discountReachedEnd)
                    DistributorPackageAPIs.getDiscountDPackages(HomeActivity.this,
                            Authenticator.loadAuthenticationToken(), discountOffset, 1);
            }
        });

        dsv1 = (DiscreteScrollView) findViewById(R.id.dsv1);
        ViewCompat.setNestedScrollingEnabled(dsv1, false);
        dsv1.setSlideOnFling(true);
        dsv1.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(1f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());
        dsv2 = (DiscreteScrollView) findViewById(R.id.dsv2);
        ViewCompat.setNestedScrollingEnabled(dsv2, false);
        dsv2.setSlideOnFling(true);
        dsv2.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1f)
                .setMinScale(1f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());

        mAdvScroller = new Runnable() {
            @Override
            public void run() {

                if(dsv1.getAdapter() != null ) {

                    rcvAdvPosition = dsv1.getCurrentItem();
                    int newPos = ++rcvAdvPosition > dsv1.getAdapter().getItemCount() - 1 ? 0 : rcvAdvPosition;
                    dsv1.smoothScrollToPosition(newPos);
                }
                if(dsv2.getAdapter() != null ) {

                    rcvAdvPosition = dsv2.getCurrentItem();
                    int newPos = ++rcvAdvPosition > dsv2.getAdapter().getItemCount() - 1 ? 0 : rcvAdvPosition;
                    dsv2.smoothScrollToPosition(newPos);
                }
                mRcvAdvScrollHandler.postDelayed(mAdvScroller, mAdvertisingShowTimeMills);
            }
        };

        mAdvScroller.run();

        mScrollView = (NestedScrollView) findViewById(R.id.home_scroll);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("TAG", "______________________onScrollChange: " + scrollY + "");
                if(scrollY == 0){
                    mRefreshLayout.setEnabled(true);
                }else{
                    mRefreshLayout.setEnabled(false);
                }
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.home_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshHome);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        mainSlider = (SliderLayout)findViewById(R.id.main_slider);
        BannerAPIs.getAllBanners(this, Authenticator.loadAuthenticationToken(), 0);

        prepareRecyclerViews();
        setShowAllButtonsOnClick();
        loadTheMostItems();
        loadAdvertising();
        TypeAPIs.getPairStatusTypes(this, Authenticator.loadAuthenticationToken(), 100);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mScrollView.canScrollVertically(1)){
            // data not loaded
            mRefreshLayout.setEnabled(true);
        }

        if(mainSlider != null)
            mainSlider.startAutoCycle();
        _isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        _isActive = false;
    }

    private void setShowAllButtonsOnClick(){
        mTbHpCheap = (TitleBar) findViewById(R.id.hp_cheap_tb);
        mTbHpFullSale = (TitleBar) findViewById(R.id.hp_full_sale_tb);
        mTbHpNew = (TitleBar) findViewById(R.id.hp_new_tb);
        mTbHpDiscount = (TitleBar) findViewById(R.id.hp_discount_tb);

        mImgHpCheap = (ImageViewPlus) findViewById(R.id.hp_img_cheap);
        mImgHpFullSale = (ImageViewPlus) findViewById(R.id.hp_img_full_sale);
        mImgHpNew = (ImageViewPlus) findViewById(R.id.hp_img_new);
        mImgHpDiscount = (ImageViewPlus) findViewById(R.id.hp_img_discount);


        mImgHpCheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllCheapIntent = new Intent(HomeActivity.this, ShowAllTheMostActivity.class);
                showAllCheapIntent.putExtra("color_id", R.color.cheap_color);
                showAllCheapIntent.putExtra("icon_id", R.drawable.all_cheap_logo);
                showAllCheapIntent.putExtra("title", "ارزان ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllCheapIntent);
            }
        });
        mTbHpCheap.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllCheapIntent = new Intent(HomeActivity.this, ShowAllTheMostActivity.class);
                showAllCheapIntent.putExtra("color_id", R.color.cheap_color);
                showAllCheapIntent.putExtra("icon_id", R.drawable.all_cheap_logo);
                showAllCheapIntent.putExtra("title", "ارزان ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllCheapIntent);
            }
        });


        mImgHpFullSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllFullSaleIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllFullSaleIntent.putExtra("color_id", R.color.full_sale_color);
                showAllFullSaleIntent.putExtra("icon_id", R.drawable.all_full_sale_logo);
                showAllFullSaleIntent.putExtra("title", "پرفروش ترین ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllFullSaleIntent);
            }
        });
        mTbHpFullSale.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllFullSaleIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllFullSaleIntent.putExtra("color_id", R.color.full_sale_color);
                showAllFullSaleIntent.putExtra("icon_id", R.drawable.all_full_sale_logo);
                showAllFullSaleIntent.putExtra("title", "پرفروش ترین ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllFullSaleIntent);
            }
        });



        mImgHpNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllNewIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllNewIntent.putExtra("color_id", R.color.new_color);
                showAllNewIntent.putExtra("icon_id", R.drawable.all_new_logo);
                showAllNewIntent.putExtra("title", "جدید ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllNewIntent);
            }
        });
        mTbHpNew.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllNewIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllNewIntent.putExtra("color_id", R.color.new_color);
                showAllNewIntent.putExtra("icon_id", R.drawable.all_new_logo);
                showAllNewIntent.putExtra("title", "جدید ها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllNewIntent);
            }
        });



        mImgHpDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllDiscountIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllDiscountIntent.putExtra("color_id", R.color.discount_color);
                showAllDiscountIntent.putExtra("icon_id", R.drawable.all_discount_logo);
                showAllDiscountIntent.putExtra("title", "تخفیف دارها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllDiscountIntent);
            }
        });
        mTbHpDiscount.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showAllDiscountIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllDiscountIntent.putExtra("color_id", R.color.discount_color);
                showAllDiscountIntent.putExtra("icon_id", R.drawable.all_discount_logo);
                showAllDiscountIntent.putExtra("title", "تخفیف دارها");
                StaticHelperFunctions.openActivity(HomeActivity.this, showAllDiscountIntent);
            }
        });
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onServiceSuccess(apiCode, data, requestCode);
        }
        else if(apiCode == APICode.getCheapestDProducts) {
            ResultList<DistributorProduct> dProductList = (ResultList<DistributorProduct>) data;
            cheapReachedEnd = CacheContainer.get().addCheapProducts(dProductList);
            cheapOffset = CacheContainer.get().getCheapOffset();
            cheapAdapter.notifyDataSetChanged();
        }

        else if(apiCode == APICode.getNewestProducts) {
            ResultList<Product> productList = (ResultList<Product>) data;
            newReachedEnd = CacheContainer.get().addNewProducts(productList);
            newOffset = CacheContainer.get().getNewOffset();
            newAdapter.notifyDataSetChanged();
        }
        else if(apiCode == APICode.getTopSellingProducts) {
            ResultList<Product> productList = (ResultList<Product>) data;
            topSaleReachedEnd = CacheContainer.get().addTopSaleProducts(productList);
            topSaleOffset = CacheContainer.get().getTopSaleOffset();
            topSaleAdapter.notifyDataSetChanged();
        }

        else if(apiCode == APICode.getDiscountDProducts) {
            ResultList<DistributorPackage> dProductList = (ResultList<DistributorPackage>) data;
            discountReachedEnd = CacheContainer.get().addDiscountPackages(dProductList);
            discountOffset = CacheContainer.get().getDiscountOffset();
            discountAdapter.notifyDataSetChanged();
        }
        else if(apiCode == APICode.getAllBanners){
            ArrayList<Banner> banners = ((ResultList<Banner>)data).getResult();
            for(Banner banner : banners) {
                String url = APICreator.getImagesBaseAddress() + banner.getImage();
                TextSliderView ss = new TextSliderView(this);
                Bundle b = new Bundle();
                b.putString("link", banner.getLink());
                try {
                    ss.description(banner.getTitle())
                            .image(url)
                            .bundle(b)
                            .setOnSliderClickListener(sliderCL);
                            //.wait(banner.getWeight()*1000);
                    mainSlider.addSlider(ss);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(apiCode == APICode.pairStatusTypes){
            OrderStatusController.getInstance().initializeOrderStatus((List<PairResultItem>)data);
        }
        else if(apiCode == APICode.searchAdvertising){

            //TODO get real data
            ResultList<Advertising> fakeResult = new ResultList<>();
            ArrayList<Advertising> fakeDat = new ArrayList<>();
            for(int i = 0; i<7;i++){
                fakeDat.add(new Advertising(getResources().getString(R.string.lbl_advertising) + (i+1)+"",
                        R.drawable.jj_adv_logo_pic));
            }
            fakeResult.setResult(fakeDat);
            showAdvertising(fakeResult);
        }

    }

    private BaseSliderView.OnSliderClickListener sliderCL = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            String link = slider.getBundle().getString("link");
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(browser);

            mainSlider.startAutoCycle();
        }
    };

    @Override
    protected void onStop() {
        if(mainSlider != null)
            mainSlider.stopAutoCycle();
        _isActive = false;
        super.onStop();
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        super.onServiceFail(apiCode, data,requestCode);
        if (apiCode == APICode.logout) {
            super.onServiceFail(apiCode, data, requestCode);
        }
        else if(apiCode == APICode.getNewestProducts){
            StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.logout) {
            super.onNetworkFail(apiCode, data, requestCode);
        }
        else if(apiCode == APICode.getNewestProducts ){
            StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
        }
    }

    private void prepareRecyclerViews(){
        recyclerView_cheap = (RecyclerView) findViewById(R.id.hp_rcv_cheap);
        ViewCompat.setNestedScrollingEnabled(recyclerView_cheap, false);
        recyclerView_cheap.removeAllViews();
        LinearLayoutManager layoutManager_cheap = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView_cheap.setLayoutManager(layoutManager_cheap);
        recyclerView_cheap.addItemDecoration(new SpacesItemDecoration(this, false));
        recyclerView_cheap.setItemAnimator(new DefaultItemAnimator());
        cheapOffset = CacheContainer.get().getCheapOffset();
        cheapReachedEnd = CacheContainer.get().isCheapReachedEnd();
        recyclerView_cheap.setAdapter(cheapAdapter);
        LinearSnapHelper helper_cheap = new LinearSnapHelper();
        helper_cheap.attachToRecyclerView(recyclerView_cheap);

        recyclerView_new = (RecyclerView) findViewById(R.id.hp_rcv_new);
        ViewCompat.setNestedScrollingEnabled(recyclerView_new, false);
        recyclerView_new.removeAllViews();
        LinearLayoutManager layoutManager_new = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView_new.setLayoutManager(layoutManager_new);
        recyclerView_new.addItemDecoration(new SpacesItemDecoration(this, false));
        recyclerView_new.setItemAnimator(new DefaultItemAnimator());
        newOffset = CacheContainer.get().getCheapOffset();
        newReachedEnd = CacheContainer.get().isCheapReachedEnd();
        recyclerView_new.setAdapter(newAdapter);
        LinearSnapHelper helper_new = new LinearSnapHelper();
        helper_new.attachToRecyclerView(recyclerView_new);

        recyclerView_top_sale = (RecyclerView) findViewById(R.id.hp_rcv_full_sale);
        ViewCompat.setNestedScrollingEnabled(recyclerView_top_sale, false);
        recyclerView_top_sale.removeAllViews();
        LinearLayoutManager layoutManager_top = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView_top_sale.setLayoutManager(layoutManager_top);
        recyclerView_top_sale.addItemDecoration(new SpacesItemDecoration(this, false));
        recyclerView_top_sale.setItemAnimator(new DefaultItemAnimator());
        topSaleOffset = CacheContainer.get().getCheapOffset();
        topSaleReachedEnd = CacheContainer.get().isCheapReachedEnd();
        recyclerView_top_sale.setAdapter(topSaleAdapter);
        LinearSnapHelper helper_top = new LinearSnapHelper();
        helper_top.attachToRecyclerView(recyclerView_top_sale);

        recyclerView_discount = (RecyclerView) findViewById(R.id.hp_rcv_discount);
        ViewCompat.setNestedScrollingEnabled(recyclerView_discount, false);
        recyclerView_discount.removeAllViews();
        LinearLayoutManager layoutManager_discount = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerView_discount.setLayoutManager(layoutManager_discount);
        recyclerView_discount.addItemDecoration(new SpacesItemDecoration(this, false));
        recyclerView_discount.setItemAnimator(new DefaultItemAnimator());
        discountOffset = CacheContainer.get().getCheapOffset();
        discountReachedEnd = CacheContainer.get().isCheapReachedEnd();
        recyclerView_discount.setAdapter(discountAdapter);
        LinearSnapHelper helper_discount = new LinearSnapHelper();
        helper_discount.attachToRecyclerView(recyclerView_discount);
    }

    private void showAdvertising(ResultList<Advertising> advertisingList){
        AdvertisingAdapter adapter = new AdvertisingAdapter(this, advertisingList.getResult());
        InfiniteScrollAdapter wrapper1 = InfiniteScrollAdapter.wrap(adapter);
        InfiniteScrollAdapter wrapper2 = InfiniteScrollAdapter.wrap(adapter);
        dsv1.setAdapter(wrapper1);
        dsv2.setAdapter(wrapper2);
    }

    private void loadTheMostItems(){
        DistributorPackageAPIs.getDiscountDPackages(this, Authenticator.loadAuthenticationToken(), discountOffset, 1);
        DistributorProductAPIs.getCheapestDProducts(this, Authenticator.loadAuthenticationToken(), cheapOffset, 1);
        ProductAPIs.getNewestProducts(this, Authenticator.loadAuthenticationToken(), newOffset,1);
        ProductAPIs.getTopSellingProducts(this, Authenticator.loadAuthenticationToken(), topSaleOffset,1);
    }

    private void loadAdvertising(){
        AdvertisingAPIs.searchAdvertising(this, Authenticator.loadAuthenticationToken(), 1);
    }

    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {

        if(super.closeDrawerMenu()){
            return;
        }
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //finishAffinity();
            //moveTaskToBack(true);
            //android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast toast = Toast.makeText(this, "برای خروج یک بار دیگر دکمه بازگشت را فشار دهید", Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        CustomFontHelper.setCustomFont(toastTV, "fonts/yr.ttf", this);
        toast.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
