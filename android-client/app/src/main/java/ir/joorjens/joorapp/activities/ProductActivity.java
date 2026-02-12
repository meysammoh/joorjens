package ir.joorjens.joorapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.DiscountListAdapter;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.adapters.TheMostListProductAdapter;
import ir.joorjens.joorapp.customViews.CheckBoxPlus;
import ir.joorjens.joorapp.customViews.PackageView;
import ir.joorjens.joorapp.customViews.ProductView;
import ir.joorjens.joorapp.customViews.RadioButtonPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.SpacesItemDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.DistributorPackageAPIs;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;
import ir.joorjens.joorapp.webService.ProductAPIs;

public class ProductActivity extends HomeBaseActivity {



    public enum ActionType{
        PA_SEARCH_ID,
        PA_SEARCH_BARCODE,
    }
    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
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

    private int BundlingRequestCode = 10;
    private int DiscountRequestCode = 11;

    private String mProductId = "";
    private String mProductBarcode = "";
    private Integer mProductCategoryTypeId;
    private ProductView mProductView;
    private PackageView mPackageView;
    //TitleBar2 mTitleBar;
    private int mColorId = -1;
    int mIconResourceId;
    String mTitle;
    private boolean productHasDistributor = false;
    private LinearLayout mLlMainLayout;

    LinearLayout mLlPayPriorityContainer;
    LinearLayout mLlPayPriorityContainerFake;

    RadioButtonPlus mCheckRadioButton;
    RadioButtonPlus mCheckRadioButtonFake;
    RadioButtonPlus mCashRadioButton;
    RadioButtonPlus mCashRadioButtonFake;

    CheckBoxPlus mCheapCheckbox;
    CheckBoxPlus mCheapCheckboxFake;

    EditText mOrderNumberEditText;
    EditText mOrderNumberEditTextFake;
    NestedScrollView mProductScrollView;
    NestedScrollView mPackageScrollView;
    ActionType _actionType;

    private int mOffset = 0;
    private boolean mDataReachedEnd =false;
    private TheMostListDistributorProductAdapter mSimilarDProductAdapter;
    private TheMostListProductAdapter mSimilarProductAdapter;
    private DiscountListAdapter mSimilarPackageAdapter;

//    private void prepareRecyclerView(MyBaseAdapter.ContentType contentType){
//        RecyclerView recyclerView_similar = (RecyclerView) findViewById(R.id.pa_rcv_similar);
//        ViewCompat.setNestedScrollingEnabled(recyclerView_similar, false);
//        recyclerView_similar.removeAllViews();
//        LinearLayoutManager layoutManager_similar = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
//        recyclerView_similar.setLayoutManager(layoutManager_similar);
//        recyclerView_similar.addItemDecoration(new SpacesItemDecoration(this, false));
//        recyclerView_similar.setItemAnimator(new DefaultItemAnimator());
//        LinearSnapHelper helper_cheap = new LinearSnapHelper();
//        helper_cheap.attachToRecyclerView(recyclerView_similar);
//
//        switch (contentType){
//            case DProduct:
//                recyclerView_similar.setAdapter(mSimilarDProductAdapter);
//                break;
//            case DPackage:
//                recyclerView_similar.setAdapter(mSimilarPackageAdapter);
//                break;
//            case Product:
//                recyclerView_similar.setAdapter(mSimilarProductAdapter);
//                break;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_product, ActivityKeys.Product);

        mLlMainLayout = (LinearLayout) findViewById(R.id.product_main_layout);
        mLlMainLayout.setVisibility(View.INVISIBLE);

        mPackageView = (PackageView) findViewById(R.id.pv_package_view);
        mProductView = (ProductView) findViewById(R.id.pv_product_view);

        mLlPayPriorityContainer = (LinearLayout) findViewById(R.id.pay_priority_container);
        mLlPayPriorityContainerFake = (LinearLayout) findViewById(R.id.pay_priority_container_fake);

        mCheckRadioButton = (RadioButtonPlus) findViewById(R.id.vp_check_radio);
        mCheckRadioButton.setOnCheckedChangeListener(checkedChangeCheckListener);
        mCheckRadioButtonFake = (RadioButtonPlus) findViewById(R.id.vp_check_radio_fake);
        mCheckRadioButtonFake.setOnCheckedChangeListener(checkedChangeCheckListenerFake);
        mCashRadioButton = (RadioButtonPlus) findViewById(R.id.vp_cash_radio);
        mCashRadioButton.setOnCheckedChangeListener(checkedChangeCashListener);
        mCashRadioButtonFake = (RadioButtonPlus) findViewById(R.id.vp_cash_radio_fake);
        mCashRadioButtonFake.setOnCheckedChangeListener(checkedChangeCashListenerFake);



        mOrderNumberEditText = (EditText) findViewById( R.id.vp_order_number);
        mOrderNumberEditTextFake = (EditText) findViewById( R.id.vp_order_number_fake);
        mOrderNumberEditText.addTextChangedListener(textChangedListener);
        mOrderNumberEditTextFake.addTextChangedListener(textChangedListenerFake);

        mCheapCheckbox = (CheckBoxPlus) findViewById(R.id.vp_cheap_checkbox);
        mCheapCheckbox.setOnCheckedChangeListener(checkedChangeCheapListener);
        mCheapCheckboxFake = (CheckBoxPlus) findViewById(R.id.vp_cheap_checkbox_fake);
        mCheapCheckboxFake.setOnCheckedChangeListener(checkedChangeCheapListenerFake);
        mProductScrollView = (NestedScrollView)findViewById(R.id.pv_main_scroll);
        mPackageScrollView = (NestedScrollView)findViewById(R.id.pkgv_main_scroll);
    }

    private TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null) {
                if (v.getId() == mOrderNumberEditText.getId()){
                    mOrderNumberEditTextFake.setText(s.toString());
                }
            }
            if(mProductView != null)
                mProductView.orderNumberChanged( );
        }
    };
    private TextWatcher textChangedListenerFake = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null) {
                if (v.getId() == mOrderNumberEditTextFake.getId()){
                    mOrderNumberEditText.setText(s.toString());
                }
            }
            if(mProductView != null)
                mProductView.orderNumberChanged( );
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeCheckListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCheckRadioButtonFake.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener checkedChangeCheckListenerFake = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCheckRadioButton.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener checkedChangeCashListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCashRadioButtonFake.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener checkedChangeCashListenerFake = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCashRadioButton.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeCheapListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCheapCheckboxFake.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener checkedChangeCheapListenerFake = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View v = ProductActivity.this.getCurrentFocus();
            if(v != null){
                mCheapCheckbox.setChecked(isChecked);
                if(mProductView != null)
                    mProductView.filterResultByCheck(mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        mDataReachedEnd = false;
        mOffset = 0;

        _isActive = true;

        if(getIntent()!= null && getIntent().getExtras() != null){
            _actionType = (ActionType) getIntent().getExtras().getSerializable("action");


            Bundle b = getIntent().getExtras();
            if (b.containsKey("color_id")) {
                mColorId = Integer.parseInt(b.getString("color_id"));
            }
            if (b.containsKey("icon_id"))
                mIconResourceId = Integer.parseInt(b.getString("icon_id"));
            if (b.containsKey("title"))
                mTitle = b.getString("title");

            if(mColorId == R.color.show_all_color){
                // hide toolbar
                mProductView.hideTitleBar();
            }
            mProductView.setTitleBarOptions(mColorId, mIconResourceId, mTitle);
            mCashRadioButton.setChecked(true);
            mCashRadioButtonFake.setChecked(true);
            mProductView.setVisibility(View.GONE);
            mPackageView.setVisibility(View.GONE);

            //=====================
            if(mColorId == R.color.cheap_color) {
                mSimilarDProductAdapter = new TheMostListDistributorProductAdapter(this, null, mColorId);
                mSimilarDProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        if (isActive() && !mDataReachedEnd) {
                            DistributorProductAPIs.getSimilarCheapestDistributorProducts(ProductActivity.this,
                                    Authenticator.loadAuthenticationToken(),mOffset, 1, mProductCategoryTypeId);
                        }
                    }
                });
                mProductView.prepareSimilarRecyclerView(MyBaseAdapter.ContentType.DProduct, null,
                        mSimilarDProductAdapter);
            }
            else if(mColorId == R.color.discount_color) {
                mSimilarPackageAdapter = new DiscountListAdapter(this, null);
                mSimilarPackageAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        if (isActive() && !mDataReachedEnd)
                            DistributorPackageAPIs.getDiscountDPackages(ProductActivity.this,
                                    Authenticator.loadAuthenticationToken(), mOffset, 1);
                    }
                });
                mPackageView.prepareSimilarRecyclerView(mSimilarPackageAdapter);
            }

            else{
                mSimilarProductAdapter = new TheMostListProductAdapter(this, null, mColorId);
                mSimilarProductAdapter.setEndListener(new MyBaseAdapter.onEndReachedListener() {
                    @Override
                    public void onEndReached(int position) {
                        switch (mColorId) {
                            case R.color.new_color:
                                if(isActive() && !mDataReachedEnd)
                                    ProductAPIs.getSimilarNewestProducts(ProductActivity.this,
                                        Authenticator.loadAuthenticationToken(),mOffset, 1, mProductCategoryTypeId);
                                break;
                            case R.color.full_sale_color:
                                if(isActive() && !mDataReachedEnd)
                                    ProductAPIs.getSimilarTopSellingProducts(ProductActivity.this,
                                        Authenticator.loadAuthenticationToken(),mOffset ,1, mProductCategoryTypeId);
                                break;
                            case -1:
                                if(isActive() && !mDataReachedEnd)
                                    ProductAPIs.getSimilarProducts(ProductActivity.this,
                                            Authenticator.loadAuthenticationToken(),mOffset ,1, mProductCategoryTypeId);
                                break;
                        }
                    }
                });
                mProductView.prepareSimilarRecyclerView(MyBaseAdapter.ContentType.Product, mSimilarProductAdapter,
                        null);
            }
            //===============================


            if (getIntent().getExtras().containsKey(SearchProductActivity.ProductBarcodeParam)) {
                mProductBarcode = getIntent().getExtras().getString(SearchProductActivity.ProductBarcodeParam);
            }

            if (getIntent().getExtras().containsKey("product_id")) {
                mProductId = getIntent().getExtras().getString("product_id");
                mOrderNumberEditText.setText("");
                mOrderNumberEditTextFake.setText("");
            }

            mProductView.setTitleBarOptions(mColorId, mIconResourceId, mTitle);
            if(mColorId == R.color.discount_color || mColorId == R.color.show_in_bundling_or_discount_color){

                mPackageView.setVisibility(View.VISIBLE);
                mProductView.setVisibility(View.GONE);
            }
            else{
                mPackageView.setVisibility(View.GONE);
                mProductView.setVisibility(View.VISIBLE);
            }
            if(mColorId == R.color.cheap_color){
                mLlPayPriorityContainer.setVisibility(View.GONE);
                mLlPayPriorityContainerFake.setVisibility(View.GONE);
                mCheapCheckbox.setVisibility(View.GONE);
                mCheapCheckboxFake.setVisibility(View.GONE);
            }
            fetchResult();
            mProductScrollView.fullScroll(ScrollView.FOCUS_UP);
            mPackageScrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }
    void fetchResult(){
        if(mColorId == R.color.discount_color || mColorId == R.color.show_in_bundling_or_discount_color){
            DistributorPackageAPIs.getDistributorPackage(this, Authenticator.loadAuthenticationToken(), 0, mProductId);

            // for filling similar products
            if(mColorId == R.color.discount_color){
                DistributorPackageAPIs.getDiscountDPackages(this, Authenticator.loadAuthenticationToken(),mOffset, 1);
            }
        }
        else{
            String idBc = "";
            boolean idOrBarcode = true;
            if(_actionType == ActionType.PA_SEARCH_BARCODE) {
                if( !mProductBarcode.isEmpty() ) {
                    idBc = mProductBarcode;
                    idOrBarcode = false;
                    DistributorProductAPIs.getProductDistributorsByBarcode(this, Authenticator.loadAuthenticationToken(), 0, mProductBarcode);
                }
            }
            else
                if (!mProductId.isEmpty()) {
                    idBc = mProductId;
                    DistributorProductAPIs.getProductDistributors(this, Authenticator.loadAuthenticationToken(), 0, mProductId);
                }


            // send enabling of show in discount or bundling packs buttons
            DistributorPackageAPIs.countProductInDPackage(this, Authenticator.loadAuthenticationToken(),
                    idBc, true, idOrBarcode, BundlingRequestCode);
            DistributorPackageAPIs.countProductInDPackage(this, Authenticator.loadAuthenticationToken(),
                    idBc, false, idOrBarcode, DiscountRequestCode);
        }
    }
    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        super.onServiceSuccess(apiCode, data, requestCode);

        if(apiCode == APICode.getDistributorPackage){
            DistributorPackage dPackage = (DistributorPackage)data;
            mPackageView.setVisibility(View.VISIBLE);
            mPackageView.clearDistributorPackages();
            mPackageView.setDistributorPackage(dPackage);
            mLlMainLayout.setVisibility(View.VISIBLE);
        }
        else if (apiCode == APICode.searchProductDistributors) {
            mProductView.setVisibility(View.VISIBLE);
            mProductView.clearDistributorProducts();
            ResultList<DistributorProduct> dProducts = (ResultList<DistributorProduct>)data;
            mProductView.setColor(mColorId);
            if( dProducts.getResult().size() > 0) {

                if(dProducts.getResult().get(0).getStock() < dProducts.getResult().get(0).getMinOrder()){
                    StaticHelperFunctions.showConfirmDialog(this, "در حال حاضر موجودی این کالا کافی نمیباشد",
                            StaticHelperFunctions.MessageType.Warning, onProductDistNotFound, "بازگشت به صفحه قبل");
                }
                else {
                    productHasDistributor = true;
                    mProductView.setProductDistributors(dProducts.getResult(), mCheckRadioButton.isChecked(), mCheapCheckbox.isChecked());
                    mProductCategoryTypeId = dProducts.getResult().get(0).getProductCategoryTypeId();

                    // for filling similar products
                    if (mColorId == R.color.cheap_color)
                        DistributorProductAPIs.getSimilarCheapestDistributorProducts(
                                this, Authenticator.loadAuthenticationToken(), mOffset, 1, mProductCategoryTypeId);
                    else if (mColorId == R.color.new_color)
                        ProductAPIs.getSimilarNewestProducts(
                                this, Authenticator.loadAuthenticationToken(), mOffset, 1, mProductCategoryTypeId);
                    else if (mColorId == R.color.full_sale_color)
                        ProductAPIs.getSimilarTopSellingProducts(
                                this, Authenticator.loadAuthenticationToken(), mOffset, 1, mProductCategoryTypeId);
                    else if (mColorId == -1) // search
                        ProductAPIs.getSimilarProducts(
                                this, Authenticator.loadAuthenticationToken(), mOffset, 1, mProductCategoryTypeId);

                    mLlMainLayout.setVisibility(View.VISIBLE);
                }
            }
            else{
                StaticHelperFunctions.showConfirmDialog(this, "کالا توسط هیچ تأمین کننده ای عرضه نشده است",
                        StaticHelperFunctions.MessageType.Warning, onProductDistNotFound, "بازگشت به صفحه قبل");
            }
        }

        else if (apiCode == APICode.addCart) {
            Cart cart = (Cart) data;

            StaticHelperFunctions.showTimedDialog(this, "کالا به سبد خرید اضافه شد",
                    StaticHelperFunctions.MessageType.Confirm, 2 ,addToCartSuccess);

        }
        else if(apiCode == APICode.getSimilarCheapDProducts){
            ResultList<DistributorProduct> dProducts = (ResultList<DistributorProduct>) data;

            Integer pId = Integer.parseInt(mProductId);
            for(int i = 0; i < dProducts.getResult().size(); i++){
                if(dProducts.getResult().get(i).getProductId() ==  pId){
                    dProducts.getResult().remove(i);
                    break;
                }
            }

            if((mOffset +1)* APICreator.maxResult < dProducts.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;

            //mSimilarDProductAdapter.addNewItems(dProducts.getResult());
        }

        else if(apiCode == APICode.getSimilarNewProducts||
                apiCode == APICode.getSimilarTopSaleProducts ||
                apiCode == APICode.getSimilarProducts){
            ResultList<Product> products = (ResultList<Product>) data;

            Integer pId = Integer.parseInt(mProductId);
            for(int i = 0; i < products.getResult().size(); i++){
                if(products.getResult().get(i).getId() ==  pId){
                    products.getResult().remove(i);
                    break;
                }
            }

            if((mOffset +1)* APICreator.maxResult < products.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;

            mSimilarProductAdapter.addNewItems(products.getResult());
        }

        else if(apiCode == APICode.getDiscountDProducts){
            ResultList<DistributorPackage> packages = (ResultList<DistributorPackage>) data;

            Integer pId = Integer.parseInt(mProductId);
            for(int i = 0; i < packages.getResult().size(); i++){
                if(packages.getResult().get(i).getId() ==  pId){
                    packages.getResult().remove(i);
                    break;
                }
            }

            if((mOffset +1)* APICreator.maxResult < packages.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;

            mSimilarPackageAdapter.addNewItems(packages.getResult());
        }
        else if(apiCode == APICode.countProductInDPackage){
            ServiceResponse resp = (ServiceResponse)data;
            int findCount = Integer.parseInt(resp.getMessage());
            if(requestCode == BundlingRequestCode){
                if(mProductView != null){
                    mProductView.setFindInBundlingBtnCount(findCount);
                }
            }
            else if(requestCode == DiscountRequestCode){
                if(mProductView != null){
                    mProductView.setFindInDiscountBtnCount(findCount);
                }
            }
        }
    }

    private SweetAlertDialog.OnSweetClickListener onProductDistNotFound = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismissWithAnimation();
            onBackPressed();
        }
    };

    StaticHelperFunctions.DialogShowListener showListener = new StaticHelperFunctions.DialogShowListener() {
        @Override
        public void onDialogShow(StaticHelperFunctions.DialogUI dialogUI) {
            dialogUI.changeTypeToSuccess( "کالا به سبد خرید اضافه شد");
        }
    };
    StaticHelperFunctions.DialogTimeFinishedListener addToCartSuccess = new StaticHelperFunctions.DialogTimeFinishedListener() {
        @Override
        public void onDialogFinished(DialogInterface dialog) {
            dialog.dismiss();
            ((Activity) ProductActivity.this).onBackPressed();
        }
    };

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.countProductInDPackage){
            if(requestCode == BundlingRequestCode){
                if(mProductView != null){
                    mProductView.setFindInBundlingBtnCount(0);
                }
            }
            else if(requestCode == DiscountRequestCode){
                if(mProductView != null){
                    mProductView.setFindInDiscountBtnCount(0);
                }
            }
        }

//        else if( apiCode != APICode.getCart ) {
//
//            StaticHelperFunctions.showMessage(this, ((ServiceResponse) data).getMessage(), StaticHelperFunctions.MessageType.Alert);
//        }

        if (apiCode == APICode.searchProductDistributors){
            mProductView.setProductDistributors(null, false, false);
        }
        if(apiCode == APICode.getDistributorPackage){
            mPackageView.setDistributorPackage(null);
        }
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        if (apiCode == APICode.searchProductDistributors){
            mProductView.setProductDistributors(null, false, false);
        }
        if(apiCode == APICode.getDistributorPackage){
            mPackageView.setDistributorPackage(null);
        }
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }
}
