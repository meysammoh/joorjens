package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.BaseActivity;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.activities.ShowAllTheMostActivity;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.adapters.TheMostListProductAdapter;
import ir.joorjens.joorapp.fragments.AlertDialogFragment;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.SpacesItemDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;

/**
 * Created by meysammoh on 17.11.17.
 */

public class ProductView extends FrameLayout {

    List<DistributorProduct> mDistributorProducts;
    //Product mProduct;
    DistributorPackage mPackage;
    int mProductId = 0;
    int mColorId = R.color.discount_color;
    int mColor;
    ImageViewPlus mProductImageView;
    TextViewPlus mProductTitle;
    LinearLayout mProductProperties;
    LinearLayout mDistributorsContainer;
    LinearLayout mLlTopContent;

    NestedScrollView mSvMainScrollBar;
    LinearLayout mLlVpFiltersContainer;
    LinearLayout mLlVpFake;

    EditText mOrderNumberEditText;
    EditText mOrderNumberEditTextFake;

    ButtonPlus mBtnShowDProductInDiscount;
    ButtonPlus mBtnShowDProductInBundling;

    private SwipeRefreshLayout mRefreshLayout;

    TitleBar2 mTitleBar;
    int mTitleBarIconId;
    int mTitleBarColorId;
    String mTitleBarTitle;

    private Context mContext;

    boolean mBuysWithCheck = false;
    boolean mSortByCheap = false;

    SwipeRefreshLayout.OnRefreshListener onRefreshProduct = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            mRefreshLayout.setRefreshing(true);
            ProductActivity pa = (ProductActivity)mContext;
            StaticHelperFunctions.recreate(pa, true);
            mRefreshLayout.setRefreshing(false);
        }
    };

    public void setColor(int colorId) {
        if (colorId == R.color.discount_color) {
            mProductImageView.setShowDiscountValue(true);
        }
        mColor = colorId;
        mColorId = R.color.discount_color;
    }

    public ProductView(@NonNull Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public ProductView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //mColor = colorId;
        init(attrs);
    }

    public ProductView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //mColor = colorId;
        mContext = context;
        init(attrs);
    }

    public void setTitleBarOptions(int color, int icon, String title) {
        mTitleBarColorId = color;
        mTitleBarIconId = icon;
        mTitleBarTitle = title;
        if (mTitleBar != null) {
            if (mTitleBarColorId == -1) {
                mTitleBar.setVisibility(GONE);
            } else {
                mTitleBar.setIcon(mTitleBarIconId);
                mTitleBar.setColor(mTitleBarColorId);
                mTitleBar.setTitle(mTitleBarTitle);
            }
        }
    }

    public void hideTitleBar(){
        mTitleBar.setVisibility(GONE);
    }

    public void setFindInDiscountBtnCount(int count) {
        mBtnShowDProductInDiscount.setTag(count);
    }

    public void setFindInBundlingBtnCount(int count) {
        mBtnShowDProductInBundling.setTag(count);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_product, null);

        mTitleBar = (TitleBar2) view.findViewById(R.id.pv_title_bar);

        mProductImageView = (ImageViewPlus) view.findViewById(R.id.vp_product_img);
        mProductTitle = (TextViewPlus) view.findViewById(R.id.vp_product_title);
        mProductProperties = (LinearLayout) view.findViewById(R.id.vp_product_properties_container);
        mDistributorsContainer = (LinearLayout) view.findViewById(R.id.vp_product_distributors_container);
        mOrderNumberEditText = (EditText) view.findViewById(R.id.vp_order_number);
        mOrderNumberEditTextFake = (EditText) view.findViewById(R.id.vp_order_number_fake);
        mLlTopContent = (LinearLayout) view.findViewById(R.id.pv_top_content);

        mBtnShowDProductInBundling = (ButtonPlus) view.findViewById(R.id.pv_btn_show_dProduct_in_bundling_packs);
        mBtnShowDProductInBundling.setOnClickListener(findDProductInBundling);

        mBtnShowDProductInDiscount = (ButtonPlus) view.findViewById(R.id.pv_btn_show_dProduct_in_discount_packs);
        mBtnShowDProductInDiscount.setOnClickListener(findDProductInDiscount);

        mLlVpFiltersContainer = (LinearLayout) view.findViewById(R.id.vp_filters_container);
        mLlVpFake = (LinearLayout) view.findViewById(R.id.vp_filters_container_fake);
        mSvMainScrollBar = (NestedScrollView) view.findViewById(R.id.pv_main_scroll);
        mSvMainScrollBar.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {
                    mRefreshLayout.setEnabled(true);
                    //BaseActivity.setSwipeEnabled(true);
                } else {
                    mRefreshLayout.setEnabled(false);
                    //BaseActivity.setSwipeEnabled(false);
                }

                if (scrollY >= mLlTopContent.getMeasuredHeight()) {
                    if (mLlVpFake.getVisibility() == GONE) {
                        mLlVpFake.setVisibility(VISIBLE);
                        mLlVpFake.bringToFront();
                        mLlVpFake.requestFocus();
                        mOrderNumberEditTextFake.setSelection(mOrderNumberEditTextFake.getText().length());
                    }
                } else if (scrollY < mLlTopContent.getMeasuredHeight()) {
                    if (mLlVpFake.getVisibility() == VISIBLE) {
                        mLlVpFake.setVisibility(GONE);
                    }
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pv_refresh);
        mRefreshLayout.setOnRefreshListener(onRefreshProduct);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        addView(view);
    }

    private OnClickListener findDProductInBundling = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBtnShowDProductInBundling != null && mBtnShowDProductInBundling.getTag() != null) {
                if (mBtnShowDProductInBundling.getTag().toString().equals("0")) {
                    StaticHelperFunctions.showConfirmDialog((ProductActivity) mContext,
                            "این محصول در باندلینگ یافت نشد", StaticHelperFunctions.MessageType.Warning,
                            null, "باشه");

                } else {
                    Intent showAllCheapIntent = new Intent(mContext, ShowAllTheMostActivity.class);
                    showAllCheapIntent.putExtra("color_id", R.color.show_in_bundling_or_discount_color);
                    showAllCheapIntent.putExtra("icon_id", R.drawable.all_discount_logo);
                    showAllCheapIntent.putExtra("title", "باندلینگ");
                    showAllCheapIntent.putExtra("productId", mProductId);
                    showAllCheapIntent.putExtra("bundlingOrDiscount", true);
                    StaticHelperFunctions.openActivity(mContext, showAllCheapIntent);
                }
            }
        }
    };
    private OnClickListener findDProductInDiscount = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mBtnShowDProductInDiscount != null && mBtnShowDProductInDiscount.getTag() != null) {
                if (mBtnShowDProductInDiscount.getTag().toString().equals("0")) {

                    StaticHelperFunctions.showConfirmDialog((ProductActivity) mContext,
                            "این محصول در بسته های تخفیفی یافت نشد", StaticHelperFunctions.MessageType.Warning,
                            null, "باشه");

                } else {
                    Intent showAllCheapIntent = new Intent(mContext, ShowAllTheMostActivity.class);
                    showAllCheapIntent.putExtra("color_id", R.color.show_in_bundling_or_discount_color);
                    showAllCheapIntent.putExtra("icon_id", R.drawable.all_discount_logo);
                    showAllCheapIntent.putExtra("title", "بسته های تخفیفی");
                    showAllCheapIntent.putExtra("productId", mProductId);
                    showAllCheapIntent.putExtra("bundlingOrDiscount", false);
                    StaticHelperFunctions.openActivity(mContext, showAllCheapIntent);
                }
            }
        }
    };

    public void filterResultByCheck(boolean buysWithCheck, boolean sortByCheap) {
        clearDistributorProducts();
        setProductDistributors(mDistributorProducts, buysWithCheck, sortByCheap);
    }

    public void setProductDistributors(List<DistributorProduct> dProducts, boolean buysWithCheck, boolean sortByCheap) {
        if (dProducts == null) {
            if(!mSvMainScrollBar.canScrollVertically(1)){
                mRefreshLayout.setEnabled(true);
            }
            return;
        }

        // just show 1 cheapest distributor product
        if (mColor == R.color.cheap_color && dProducts.size() > 1) {
            Collections.sort(dProducts, new Comparator<DistributorProduct>() {
                @Override
                public int compare(DistributorProduct o1, DistributorProduct o2) {
                    return o1.getPriceMin().compareTo(o2.getPriceMin());
                }
            });

            dProducts = dProducts.subList(0, 1);
        }
        if (sortByCheap) {

            Collections.sort(dProducts, new Comparator<DistributorProduct>() {
                @Override
                public int compare(DistributorProduct o1, DistributorProduct o2) {
                    return o2.getPrice().compareTo(o1.getPrice());
                }
            });
        }
        mLlVpFiltersContainer.setVisibility(VISIBLE);
        // show distributor products
        mBuysWithCheck = buysWithCheck;
        mDistributorProducts = dProducts;
        if (mDistributorProducts.size() < 1)
            return;
        DistributorProduct firstDist = mDistributorProducts.get(0);
        mProductId = firstDist.getProductId();
        StaticHelperFunctions.loadImage(getContext(), firstDist.getProductImage(), mProductImageView.getImageView());

//        String weightDetail = " ";
//        for (ProductDetails prodDetail : firstDist.getProductDetails()) {
//            if (prodDetail.getProductDetailTypeName() != null && prodDetail.getProductDetailTypeName().contains("وزن")) {
//                if(prodDetail.getValue() != null && !prodDetail.getValue().equals("")) {
//                    weightDetail += prodDetail.getValue() + " " ;
//                    break;
//                }
//            }
//        }

        String allDetails = "";
        for(ProductDetails detail : firstDist.getProductDetails()){
            if(detail.getProductDetailTypeName().equals("مشخصات کالا")&& !detail.getValue().equals("")){
                allDetails += detail.getValue();
                break;
            }
        }

        mProductTitle.setText(firstDist.getProductName() + " " + firstDist.getProductBrandTypeName());

//        mProductTitle.setText(firstDist.getProductName() + " " + firstDist.getProductBrandTypeName() +
//                " => " + firstDist.getId() + "  " + firstDist.getProductId()+"");

        //if (mColorId != -1)
        //    mProductTitle.setTextColor(ContextCompat.getColor(getContext(), mColorId));


        ProductPropertiesRow2 property2 = new ProductPropertiesRow2(this.getContext());
        property2.setColor(-1, 1);
        property2.setRowValues("", allDetails);
        mProductProperties.addView(property2);

//        String allDetails = "";
//        for (ProductDetails prodDetail : firstDist.getProductDetails()) {
//            if (prodDetail.getValue() != null && !prodDetail.getValue().equals("")) {
//                allDetails += prodDetail.getValue() + " ";
//            }
//        }
//        if (!allDetails.equals("")) {
//            property2 = new ProductPropertiesRow2(this.getContext());
//            property2.setColor(-1, 1);
//            property2.setRowValues("", allDetails);
//            mProductProperties.addView(property2);
//        }

        property2 = new ProductPropertiesRow2(this.getContext());
        property2.setColor(R.color.red1, 1);
        property2.setRowValues(getResources().getString(R.string.label_consumer_price), String.format("%,d", firstDist.getProductPriceConsumer()) + " تومان");
        mProductProperties.addView(property2);


        for (DistributorProduct distPro : dProducts) {
            if (buysWithCheck == false || distPro.getSupportCheck() == buysWithCheck) {
                ProductDistributorsRow prodDist;
                prodDist = new ProductDistributorsRow(mContext, mColor);

                prodDist.setRowValues(distPro);
                mDistributorsContainer.addView(prodDist);
            }
        }

        for (int i = 0; i < mDistributorsContainer.getChildCount(); i++) {
            ProductDistributorsRow prodDist = (ProductDistributorsRow) mDistributorsContainer.getChildAt(i);
            prodDist.showDiscounts();
        }

    }

    public void orderNumberChanged() {
        for (int i = 0; i < mDistributorsContainer.getChildCount(); i++) {
            ProductDistributorsRow prodDist = (ProductDistributorsRow) mDistributorsContainer.getChildAt(i);
            prodDist.showDiscounts();
            if (!mOrderNumberEditText.getText().toString().isEmpty())
                prodDist.updateComputations(Integer.parseInt(mOrderNumberEditText.getText().toString()),
                        mBuysWithCheck);
        }
    }

    public void clearDistributorProducts() {
        mProductImageView.setImage(null);
        mProductTitle.setText("");
        mDistributorsContainer.removeAllViews();
        mProductProperties.removeAllViews();
    }

    public void prepareSimilarRecyclerView(MyBaseAdapter.ContentType contentType,
                                            TheMostListProductAdapter productAdapter,
                                            TheMostListDistributorProductAdapter distributorProductAdapter){
        RecyclerView recyclerView_similar = (RecyclerView) findViewById(R.id.pv_rcv_similar);
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

        switch (contentType){
            case DProduct:
                recyclerView_similar.setAdapter(distributorProductAdapter);
                break;
            case Product:
                recyclerView_similar.setAdapter(productAdapter);
                break;
        }
    }

//    public void setSimilarDistributorProducts(List<DistributorProduct> products, int typeId) {
//
//        TheMostListDistributorProductAdapter adapter = new TheMostListDistributorProductAdapter(mContext, products, typeId);
//
//        RecyclerView recyclerView_similar = (RecyclerView) findViewById(R.id.pv_rcv_similar);
//        LinearLayoutManager mLayoutManager_new = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
//        recyclerView_similar.setLayoutManager(mLayoutManager_new);
//        recyclerView_similar.addItemDecoration(new SpacesItemDecoration(mContext, false));
//        recyclerView_similar.setItemAnimator(new DefaultItemAnimator());
//        recyclerView_similar.setAdapter(adapter);
//    }

//    public void setSimilarProducts(List<Product> products, int typeId) {
//
//        TheMostListProductAdapter adapter = new TheMostListProductAdapter(mContext, products, typeId);
//
//        RecyclerView recyclerView_similar = (RecyclerView) findViewById(R.id.pv_rcv_similar);
//        LinearLayoutManager mLayoutManager_new = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
//        recyclerView_similar.setLayoutManager(mLayoutManager_new);
//        recyclerView_similar.addItemDecoration(new SpacesItemDecoration(mContext, false));
//        recyclerView_similar.setItemAnimator(new DefaultItemAnimator());
//        recyclerView_similar.setAdapter(adapter);
//    }
}