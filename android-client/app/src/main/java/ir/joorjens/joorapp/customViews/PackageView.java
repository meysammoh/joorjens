package ir.joorjens.joorapp.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.BaseActivity;
import ir.joorjens.joorapp.activities.HomeActivity;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.adapters.DiscountListAdapter;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.adapters.TheMostListProductAdapter;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.PackageProduct;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.SpacesItemDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by mohsen on 18.03.29.
 */

public class PackageView extends FrameLayout {

    private NestedScrollView mMainScroll;

    DistributorPackage mPackage;
    int mColorId = R.color.discount_color;
    ImageViewPlus mProductImageView;
    TextViewPlus mProductTitle;
    LinearLayout mPackageProperties;
    LinearLayout mLLAddToCard;
    EditTextPlus mOrderNumberEditText;
    TextViewPlus mTxtOrderPrice;
    ButtonPlus mBtnPrevPage;
    ButtonPlus mBtnFirstPage;
    private SwipeRefreshLayout mRefreshLayout;


    TitleBar2 mTitleBar;
    private Context mContext;

    private Integer mAllPriceAfterDiscount = 0;

    SwipeRefreshLayout.OnRefreshListener onRefreshPackage = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            mRefreshLayout.setRefreshing(true);
            ProductActivity pa = (ProductActivity)mContext;
            StaticHelperFunctions.recreate(pa, true);
            mRefreshLayout.setRefreshing(false);
        }
    };

    public PackageView(@NonNull Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public PackageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public PackageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_package, null);

        mMainScroll = (NestedScrollView) view.findViewById(R.id.pkgv_main_scroll);
        mMainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == 0){
                    mRefreshLayout.setEnabled(true);
                    //BaseActivity.setSwipeEnabled(true);
                }else{
                    mRefreshLayout.setEnabled(false);
                    //BaseActivity.setSwipeEnabled(false);
                }
            }
        });

        mTitleBar = (TitleBar2) view.findViewById(R.id.pkv_title_bar);
        mTitleBar.setIcon(R.drawable.all_discount_logo);
        mProductImageView = (ImageViewPlus) view.findViewById(R.id.vpk_package_img);
        mProductTitle = (TextViewPlus) view.findViewById(R.id.vpk_package_title);
        mPackageProperties = (LinearLayout) view.findViewById(R.id.vpk_package_properties_container);
        mTxtOrderPrice = (TextViewPlus) view.findViewById(R.id.vpk_btn_price_text);
        mOrderNumberEditText = (EditTextPlus) view.findViewById(R.id.vpk_order_number);
        mOrderNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                orderNumberChanged();
            }
        });
        mLLAddToCard = (LinearLayout) view.findViewById(R.id.vpk_btn_add_card);
        mLLAddToCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        mBtnPrevPage = (ButtonPlus) view.findViewById(R.id.pkgv_prev_page);
        mBtnPrevPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).onBackPressed();
            }
        });
        mBtnFirstPage = (ButtonPlus) view.findViewById(R.id.pkgv_first_page);
        mBtnFirstPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getContext(), HomeActivity.class);
                StaticHelperFunctions.openActivity(getContext(), homeIntent);
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pkgv_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshPackage);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        addView(view);
    }

    public void setDistributorPackage(DistributorPackage dPackage){
        if (dPackage == null) {
            if(!mMainScroll.canScrollVertically(1)){
                mRefreshLayout.setEnabled(true);
            }
            return;
        }

        mPackage = dPackage;
        mProductImageView.setDiscountValue(dPackage.getAllDiscountPercent());
        StaticHelperFunctions.loadImage(getContext(), dPackage.getImage(), mProductImageView.getImageView());
        mProductTitle.setText(dPackage.getName());
        if (mColorId != -1)
            mProductTitle.setTextColor(ContextCompat.getColor(getContext(), mColorId));

//        ProductPropertiesRow property = new ProductPropertiesRow(this.getContext());
//        property.setColor(mColorId, 1);
//        property.setRowValues("دسته بندی:", "؟؟؟؟");
//        mPackageProperties.addView(property);

        DistributorPackagePropertiesRow pkProperty = new DistributorPackagePropertiesRow(this.getContext(), true);
        pkProperty.setColor(mColorId);
        pkProperty.setRowValues("محتویات","تعداد","قبل از تخفیف");
        mPackageProperties.addView(pkProperty);

        for(PackageProduct pp : dPackage.getPackageProducts()) {
            pkProperty = new DistributorPackagePropertiesRow(this.getContext(), false);
            pkProperty.setRowValues(pp.getDistributorProductName(), pp.getCount()+" عدد",
                    String.format("%,d",pp.getDistributorProductPrice())+ " تومان");
            mPackageProperties.addView(pkProperty);
        }

        ProductPropertiesRow property = new ProductPropertiesRow(this.getContext());
        property.setRowValues(" "," ");
        mPackageProperties.addView(property);

        DistributorPackagePropertiesSumRow pksProperty = new DistributorPackagePropertiesSumRow(this.getContext());
        pksProperty.setColor(mColorId);
        pksProperty.setRowValues("قیمت کل قبل از تخفیف", String.format("%,d",dPackage.getAllPrice()));
        mPackageProperties.addView(pksProperty);

        property = new ProductPropertiesRow(this.getContext());
        property.setColor(mColorId, 2);
        property.setRowValues("قیمت کل پس از تخفیف",String.format("%,d",dPackage.getAllPriceWithDiscount())+" تومان" );
        mPackageProperties.addView(property);
        mAllPriceAfterDiscount = dPackage.getAllPriceWithDiscount();
    }

    public void clearDistributorPackages(){
        mProductImageView.setImage(null);
        mProductTitle.setText("");
        mPackageProperties.removeAllViews();
    }

    void addToCart(){
        try {
            AddCartParams params = new AddCartParams(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.CartPackage),
                    Integer.valueOf(mOrderNumberEditText.getText().toString()), false, 0, mPackage.getId());
            CartAPIs.addCart((ActivityServiceListener) getContext(), Authenticator.loadAuthenticationToken(), params, 0);
        }catch (Exception ex){
            StaticHelperFunctions.showConfirmDialog((Activity) mContext, "لطفاً ابتدا تعداد را وارد نمائید",
                    StaticHelperFunctions.MessageType.Warning, null, "باشه");
        }
    }

    public void orderNumberChanged( ){
        try {
            Integer prc = mAllPriceAfterDiscount * Integer.valueOf(mOrderNumberEditText.getText().toString());
            mTxtOrderPrice.setText(String.format("%,d", prc) + " تومان");
        }catch (Exception ex){}
    }

    public void prepareSimilarRecyclerView(DiscountListAdapter discountListAdapter){
        RecyclerView recyclerView_similar = (RecyclerView) findViewById(R.id.pkgv_rcv_similar);
        ViewCompat.setNestedScrollingEnabled(recyclerView_similar, false);
        recyclerView_similar.removeAllViews();
        LinearLayoutManager layoutManager_similar = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
        recyclerView_similar.setLayoutManager(layoutManager_similar);
        recyclerView_similar.addItemDecoration(new SpacesItemDecoration(mContext, false));
        recyclerView_similar.setItemAnimator(new DefaultItemAnimator());

        try {
            LinearSnapHelper helper_cheap = new LinearSnapHelper();
            helper_cheap.attachToRecyclerView(recyclerView_similar);
        }catch (Exception ex){}

        recyclerView_similar.setAdapter(discountListAdapter);
    }

//    public void setSimilarPackages(List<DistributorPackage> packages){
//        DiscountListAdapter adapter = new DiscountListAdapter(mContext, packages);
//
//        RecyclerView recyclerViewSimilar = (RecyclerView) findViewById(R.id.pkgv_rcv_similar);
//        LinearLayoutManager mLayoutManager_new = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
//        recyclerViewSimilar.setLayoutManager(mLayoutManager_new);
//        recyclerViewSimilar.addItemDecoration(new SpacesItemDecoration(mContext, false));
//        recyclerViewSimilar.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewSimilar.setAdapter(adapter);
//    }
}
