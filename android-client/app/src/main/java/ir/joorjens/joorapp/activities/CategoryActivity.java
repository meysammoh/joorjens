package ir.joorjens.joorapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
//import ir.joorjens.joorapp.adapters.CategoriesExpandableListAdapter;
import ir.joorjens.joorapp.adapters.CategoryExpandableListAdapter;
import ir.joorjens.joorapp.customViews.NonScrollExpandableListView;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ProductCategory;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CategoryAPIs;

public class CategoryActivity extends HomeBaseActivity {


    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;
        processAllCategories( CacheContainer.get().getCategories());
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

    NonScrollExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
        List<KeyValueChildItem> expandableListTitle;
    ScrollView scroll;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_category, ActivityKeys.Category);
        expandableListView = (NonScrollExpandableListView) findViewById(R.id.expandableListView);
        ViewCompat.setNestedScrollingEnabled(expandableListView, false);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent showAllCheapIntent = new Intent(getApplicationContext(), ShowAllTheMostActivity.class);
                showAllCheapIntent.putExtra("icon_id", R.drawable.search_result);
                showAllCheapIntent.putExtra("title", "نتیجه جستجو");
                Bundle searchBundle = new Bundle();
                searchBundle.putString("productCategoryTypeId", (
                                expandableListTitle.get(groupPosition).getChild().get(
                                childPosition)).getId() +"");
                showAllCheapIntent.putExtra("search_options", searchBundle);
                StaticHelperFunctions.openActivity(CategoryActivity.this, showAllCheapIntent);
                return false;
            }
        });

        //CategoryAPIs.getHierarchicalCategories(this,Authenticator.loadAuthenticationToken(),1);

    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
//        super.onServiceSuccess(apiCode, data,requestCode);
//         if(apiCode==APICode.getHierarchicalCategories){
//            List<KeyValueChildItem> cats = (List<KeyValueChildItem>) data;
//
//            processAllCategories(cats);
//        }
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

    private void processAllCategories(List<KeyValueChildItem> cats){
        if(cats == null){
            return;
        }
        expandableListTitle = new ArrayList<>();

        for (KeyValueChildItem cat : cats) {
            expandableListTitle.add( cat );
        }
        //Collections.sort(expandableListTitle);
        expandableListAdapter = new CategoryExpandableListAdapter(this, expandableListTitle);
        expandableListView.setAdapter(expandableListAdapter);
    }
}
