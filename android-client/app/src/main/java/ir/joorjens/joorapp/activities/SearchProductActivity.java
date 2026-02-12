package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.adapters.SpinnerPlusAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.CheckBoxPlus;
import ir.joorjens.joorapp.customViews.EditTextPlus;
import ir.joorjens.joorapp.customViews.SpinnerPlus;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.BrandAPIs;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CategoryAPIs;
import ir.joorjens.joorapp.webService.DistributorsAPIs;

public class SearchProductActivity extends HomeBaseActivity {

    public  static String ProductNameParam = "productname";
    public  static String PackageNameParam = "packagename";
    public  static String ProductBarcodeParam = "productbarcode";
    public  static String ProductCategoryListParam = "productCategoryTypeId";
    public  static String ProductCategoryListParentParam = "typesareparent";
    public  static String ProductBrandListParam = "productbrandtypeid";
    public  static String ProductDistributorListListParam = "distributorid";
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

    private EditTextPlus mEditTextBarcodeSN;
    private EditTextPlus mEditTextName;
    private EditTextPlus mEditTextAdvSearchName;
    private SpinnerPlus mSpinnerCategory;
    private ButtonPlus mBtnSearch;
    private ButtonPlus mBtnAdvancedSearch;
    List<KeyValueChildItem> expandableListTitle;
    LinearLayout _normalSearchContainer;
    LinearLayout _advancedSearchContainer;
    LinearLayout _advancedSearchCategoriesFilterContainer;
    LinearLayout _advancedSearchCategoriesContainer;
    LinearLayout _advancedSearchBrandsFilterContainer;
    LinearLayout _advancedSearchBrandsContainer;
    LinearLayout _advancedSearchDistributorContainer;
    RadioGroup _advancedSearchSpecialRadioGroup;
    RadioGroup _advancedSearchPriceOrderRadioGroup;
    LinearLayout _advancedSearchPriceOrderContainer;
    RadioGroup _advancedSearchSellOrderRadioGroup;
    LinearLayout _advancedSearchSellOrderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_search_product, ActivityKeys.SearchProduct);

        mEditTextBarcodeSN = (EditTextPlus) findViewById(R.id.et_spa_product_category);
        mEditTextName = (EditTextPlus) findViewById(R.id.et_spa_product_name);
        mSpinnerCategory = (SpinnerPlus) findViewById(R.id.sp_spa_product_category);
        mBtnSearch = (ButtonPlus) findViewById(R.id.btn_spa_search);
        mBtnSearch.setOnClickListener(searchButtonClicked);
        _normalSearchContainer = (LinearLayout) findViewById(R.id.spa_main);
        _advancedSearchContainer = (LinearLayout) findViewById(R.id.spa_adv_main);
        _advancedSearchContainer.setVisibility(View.GONE);

        _advancedSearchCategoriesFilterContainer = (LinearLayout) findViewById(R.id.container_adv_search_category_filter);
        _advancedSearchCategoriesContainer = (LinearLayout) findViewById(R.id.container_adv_search_categories);

        _advancedSearchBrandsFilterContainer = (LinearLayout) findViewById(R.id.container_adv_search_brand_filter);
        _advancedSearchBrandsContainer = (LinearLayout) findViewById(R.id.container_adv_search_brands);

        _advancedSearchDistributorContainer = (LinearLayout) findViewById(R.id.container_adv_search_distributors);
        mBtnAdvancedSearch = (ButtonPlus) findViewById(R.id.btn_spa_adv_search);
        mBtnAdvancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdvancedSearchVisibility( true );
            }
        });
        mEditTextAdvSearchName = (EditTextPlus) findViewById(R.id.edit_txt_adv_search_name);
        _advancedSearchSpecialRadioGroup = (RadioGroup) findViewById( R.id.radio_group_adv_search_special_filter);
        _advancedSearchSpecialRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                _advancedSearchPriceOrderContainer.setVisibility( checkedId == R.id.radio_adv_search_cheapest_group ||
                        checkedId == R.id.radio_adv_search_discount_group ?
                        View.GONE : View.VISIBLE);
                _advancedSearchSellOrderContainer.setVisibility( checkedId == R.id.radio_adv_search_top_selling_group ||
                        checkedId == R.id.radio_adv_search_discount_group ?
                        View.GONE : View.VISIBLE);

                _advancedSearchBrandsFilterContainer.setVisibility( checkedId == R.id.radio_adv_search_discount_group ?
                        View.GONE : View.VISIBLE);
                _advancedSearchCategoriesFilterContainer.setVisibility( checkedId == R.id.radio_adv_search_discount_group ?
                        View.GONE : View.VISIBLE);

            }
        });
        _advancedSearchPriceOrderRadioGroup = (RadioGroup) findViewById( R.id.radio_group_adv_search_price_filter);
        _advancedSearchPriceOrderContainer = (LinearLayout) findViewById( R.id.container_adv_search_price_filter);
        _advancedSearchSellOrderRadioGroup = (RadioGroup) findViewById( R.id.radio_group_adv_search_sell_filter);
        _advancedSearchSellOrderContainer = (LinearLayout) findViewById( R.id.container_adv_search_sell_filter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        _isActive = true;
        setAdvancedSearchVisibility(false);
//        if(getIntent().getExtras() != null) {
//            String barcodeSN = getIntent().getExtras().getString(PRODUCT_BARCODE_SN_TAG);
//            if (barcodeSN != null && barcodeSN != "") {
//                mEditTextBarcodeSN.setText(barcodeSN);
//                getIntent().getExtras().putString(PRODUCT_BARCODE_SN_TAG, "");
//                gotoProductViewByBarcode(barcodeSN);
//            }
//
//        }


        //CategoryAPIs.getHierarchicalCategories(this, Authenticator.loadAuthenticationToken(), 1);
        //BrandAPIs.getPairBrands( this, Authenticator.loadAuthenticationToken(), 0);
        //DistributorsAPIs.getPairDistributors( this, Authenticator.loadAuthenticationToken(), 0 );

        processAllCategories(CacheContainer.get().getCategories());
        processAllBrands(CacheContainer.get().getBrands());
        processAllDistributors(CacheContainer.get().getDistributors());
    }

    @Override
    public String getActivityTitle() {
        return "جستجوی محصولات";
    }

    @Override
    public ActivityKeys getActivityId() {
        return ActivityKeys.SearchProduct;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        super.onServiceSuccess(apiCode, data,requestCode);
//        if(apiCode==APICode.getHierarchicalCategories){
//            List<KeyValueChildItem> cats = (List<KeyValueChildItem>) data;
//
//            processAllCategories(cats);
//        }
//        else if(apiCode==APICode.pairBrands){
//            List<PairResultItem> brands = (List<PairResultItem>) data;
//
//            processAllBrands(brands);
//        }
//        else if(apiCode==APICode.pairDistributors){
//            List<PairResultItem> distributors = (List<PairResultItem>) data;
//
//            processAllDistributors(distributors);
//        }
    }

    private void processAllDistributors(List<PairResultItem> distributors) {
        if(distributors == null)
            return;

        _advancedSearchDistributorContainer.removeAllViews();
        for(PairResultItem pair: distributors){
            CheckBoxPlus checkBox = (CheckBoxPlus)getLayoutInflater().inflate(R.layout.checkbox_plus_style, null);
            checkBox.setText(pair.getSecond());
            checkBox.setId( pair.getItemId());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 8, 0, 8);
            _advancedSearchDistributorContainer.addView( checkBox , layoutParams);
        }
    }

    private void processAllBrands(List<PairResultItem> brands) {
        if(brands == null)
            return;

        _advancedSearchBrandsContainer.removeAllViews();
        for(PairResultItem pair: brands){
            CheckBoxPlus checkBox = (CheckBoxPlus)getLayoutInflater().inflate(R.layout.checkbox_plus_style, null);
            checkBox.setText(pair.getSecond());
            checkBox.setId( pair.getItemId());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 8, 0, 8);
            _advancedSearchBrandsContainer.addView( checkBox, layoutParams );
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        super.onServiceSuccess(apiCode, data,requestCode);
        if(apiCode != APICode.getCart )
            StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    protected void ProcessBarcodeResult(String barcodeSN) {

        mEditTextBarcodeSN.setText(barcodeSN);
        //Go to product
        gotoProductViewByBarcode(barcodeSN);
    }
    void gotoProductViewByBarcode(String barcodeSN){
        Intent showProduct = new Intent(getApplicationContext(), ProductActivity.class);
        showProduct.putExtra("icon_id", R.drawable.search_result + "");
        showProduct.putExtra("title", "نتیجه جستجو");
        showProduct.putExtra(ProductBarcodeParam, barcodeSN);
        StaticHelperFunctions.openActivity(SearchProductActivity.this, showProduct);
    }

    private View.OnClickListener searchButtonClicked = new View.OnClickListener() {
        public void onClick(View v) {
            Bundle searchBundle;
            if( mBtnAdvancedSearch.getVisibility() == View.VISIBLE)
                searchBundle = makeNormalSearchParams();
            else
                searchBundle = makeAdvancedSearchParams();

            Intent showAllCheapIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
            int groupId = _advancedSearchSpecialRadioGroup.getCheckedRadioButtonId();
            if( groupId == R.id.radio_adv_search_discount_group){
                showAllCheapIntent.putExtra("is_bundle",true);
                searchBundle.putString("bundlingordiscount","false");
            }

            showAllCheapIntent.putExtra("icon_id", R.drawable.search_result);
            showAllCheapIntent.putExtra("title", "نتیجه جستجو");
            showAllCheapIntent.putExtra("search_options", searchBundle);
            StaticHelperFunctions.openActivity(SearchProductActivity.this, showAllCheapIntent);
        }
    };

    private Bundle makeNormalSearchParams(){

        Bundle searchBundle = new Bundle();
        if (!mEditTextBarcodeSN.getText().toString().equals(""))
            searchBundle.putString(ProductBarcodeParam, mEditTextBarcodeSN.getText().toString());
        if (!mEditTextName.getText().toString().equals("")) {
            searchBundle.putString(ProductNameParam, mEditTextName.getText().toString());
            searchBundle.putString("like", "true");

        }
        if (mSpinnerCategory.getSelectedItemPosition() > 0) {
            searchBundle.putString(ProductCategoryListParam, ((KeyValueChildItem) mSpinnerCategory.getSelectedItem()).getId() + "");
            searchBundle.putString(ProductCategoryListParentParam, "true");
        }
        return searchBundle;
    }

    private Bundle makeAdvancedSearchParams(){
        Bundle searchBundle = new Bundle();



        // Special pages filters
        int groupId = _advancedSearchSpecialRadioGroup.getCheckedRadioButtonId();
        if(groupId == R.id.radio_adv_search_discount_group) {
            if (!mEditTextAdvSearchName.getText().toString().equals("")) {
                searchBundle.putString(PackageNameParam, mEditTextAdvSearchName.getText().toString());
                searchBundle.putString("like", "true");
            }
        }
        else {
            if (!mEditTextAdvSearchName.getText().toString().equals("")) {
                searchBundle.putString(ProductNameParam, mEditTextAdvSearchName.getText().toString());
                searchBundle.putString("like", "true");
            }
            if (groupId == R.id.radio_adv_search_cheapest_group) {
                addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdPrice) + "",
                        "true", searchBundle);
            } else if (groupId == R.id.radio_adv_search_newest_group) {
                addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdTime) + "",
                        "false", searchBundle);
            } else if (groupId == R.id.radio_adv_search_top_selling_group) {

                addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling) + "",
                        "false", searchBundle);
            }

            if (_advancedSearchPriceOrderContainer.getVisibility() == View.VISIBLE) {
                if (_advancedSearchPriceOrderRadioGroup.getCheckedRadioButtonId() == R.id.radio_adv_search_price_asc)
                    addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdPrice) + "",
                            "true", searchBundle);
                else if (_advancedSearchPriceOrderRadioGroup.getCheckedRadioButtonId() == R.id.radio_adv_search_price_dsc)
                    addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdPrice) + "",
                            "false", searchBundle);
            }
            if (_advancedSearchSellOrderContainer.getVisibility() == View.VISIBLE) {
                if (_advancedSearchSellOrderRadioGroup.getCheckedRadioButtonId() == R.id.radio_adv_search_sell_asc)
                    addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling) + "",
                            "true", searchBundle);
                else if (_advancedSearchSellOrderRadioGroup.getCheckedRadioButtonId() == R.id.radio_adv_search_sell_dsc)
                    addOrderTypeFilter(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling) + "",
                            "false", searchBundle);
            }
            //Categories filter
            String catIds = "";
            for (int catCounter = 0; catCounter < _advancedSearchCategoriesContainer.getChildCount();
                 catCounter++) {
                CheckBoxPlus catBox = (CheckBoxPlus) _advancedSearchCategoriesContainer.getChildAt(catCounter);
                if (catBox.isChecked()) {
                    if (!catIds.isEmpty())
                        catIds += ",";
                    catIds += catBox.getId();
                }
            }
            if (!catIds.isEmpty())
                searchBundle.putString(ProductCategoryListParam, catIds);

            //Brands filter
            String brandIds = "";
            for (int brandCounter = 0; brandCounter < _advancedSearchBrandsContainer.getChildCount();
                 brandCounter++) {
                CheckBoxPlus brandBox = (CheckBoxPlus) _advancedSearchBrandsContainer.getChildAt(brandCounter);
                if (brandBox.isChecked()) {
                    if (!brandIds.isEmpty())
                        brandIds += ",";
                    brandIds += brandBox.getId();
                }
            }
            if (!brandIds.isEmpty())
                searchBundle.putString(ProductBrandListParam, brandIds);
        }
        //Distributor filter
        String distributorIds = "";
        for (int distCounter = 0; distCounter < _advancedSearchDistributorContainer.getChildCount();
             distCounter++) {
            CheckBoxPlus distBox = (CheckBoxPlus) _advancedSearchDistributorContainer.getChildAt(distCounter);
            if (distBox.isChecked()) {
                if (!distributorIds.isEmpty())
                    distributorIds += ",";
                distributorIds += distBox.getId();
            }
        }
        if (!distributorIds.isEmpty())
            searchBundle.putString(ProductDistributorListListParam, distributorIds);
        return searchBundle;
    }
    private void addOrderTypeFilter( String newOrderTypeId, String asc, Bundle searchBundle){
        String ids = searchBundle.getString("orderTypeId","");
        String ascs = searchBundle.getString("asc","");
        if(!ids.contains(newOrderTypeId)) {
            if (!ids.isEmpty()) {
                ids += ",";
                ascs += ",";
            }
            ids+=newOrderTypeId;
            ascs+=asc;
        }
        searchBundle.putString( "orderTypeId", ids);
        searchBundle.putString( "asc", ascs);
    }

    private void processAllCategories(List<KeyValueChildItem> cats){
        if(cats == null)
            return;

        expandableListTitle = new ArrayList<>();

        for (KeyValueChildItem cat : cats) {
            expandableListTitle.add( cat );
            addToAdvancedSearchCategories( cat );
        }
        KeyValueChildItem item = new KeyValueChildItem();
        item.setId(new Long(0));
        item.setName(getResources().getString(R.string.label_choose));
        expandableListTitle.add(0,item);
        SpinnerPlusAdapter<KeyValueChildItem> adapter = new SpinnerPlusAdapter<>(this, R.layout.spinner_item_default_fc, expandableListTitle);
        // attaching data adapter to spinner
        mSpinnerCategory.setAdapter(adapter);
    }

    private void addToAdvancedSearchCategories(KeyValueChildItem cat) {
        CheckBoxPlus checkBox = (CheckBoxPlus)getLayoutInflater().inflate(R.layout.checkbox_plus_style, null);
        checkBox.setText(cat.getName());
        checkBox.setId( cat.getItemId());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 8, 0, 8);
        _advancedSearchCategoriesContainer.addView( checkBox, layoutParams );
    }

    public boolean setAdvancedSearchVisibility( boolean show ){
        boolean showingAdvanced = _advancedSearchContainer.getVisibility() == View.VISIBLE;
        if(show == showingAdvanced)
            return false;
        if(show) {
            int showNormal =  View.GONE ;
            mBtnAdvancedSearch.setVisibility(showNormal);
            _normalSearchContainer.setVisibility(showNormal);

            _advancedSearchContainer.setVisibility(View.VISIBLE );
            return true;
        }
        else{
            int showNormal =  View.VISIBLE ;
            mBtnAdvancedSearch.setVisibility(showNormal);
            _normalSearchContainer.setVisibility(showNormal);

            _advancedSearchContainer.setVisibility(View.GONE );
            return true;
        }

    }
    public void onBackPressed() {
        if(setAdvancedSearchVisibility(false))
            return;
        super.onBackPressed();
    }
}
