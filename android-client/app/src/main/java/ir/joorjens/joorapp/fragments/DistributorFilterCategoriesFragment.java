package ir.joorjens.joorapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.DistributorActivity;
import ir.joorjens.joorapp.activities.ShowAllTheMostActivity;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.adapters.CategoryExpandableListAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.NonScrollExpandableListView;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CacheContainer;
import ir.joorjens.joorapp.webService.CategoryAPIs;

public class DistributorFilterCategoriesFragment extends Fragment implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();
        processAllCategories(CacheContainer.get().getCategories());
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

    NonScrollExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<KeyValueChildItem> expandableListTitle;
    private int lastExpandedPosition = -1;

    public DistributorFilterCategoriesFragment() {

    }

    public static DistributorFilterCategoriesFragment newInstance(){
        DistributorFilterCategoriesFragment fragment = new DistributorFilterCategoriesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distributor_filter_categories, container, false);

        expandableListView = (NonScrollExpandableListView) v.findViewById(R.id.expandableListView);
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

                Intent searchDistIntent = new Intent(getContext(), DistributorActivity.class);
                searchDistIntent.putExtra( DistributorActivity.DistributorCategoryListParam,
                        expandableListTitle.get(groupPosition).getChild().get(
                        childPosition).getId()+"" );
                StaticHelperFunctions.openActivity(getContext(), searchDistIntent);
                return false;
            }
        });

        //CategoryAPIs.getHierarchicalCategories(this, Authenticator.loadAuthenticationToken(),1);

        return v;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {

//        if(apiCode==APICode.getHierarchicalCategories){
//            List<KeyValueChildItem> cats = (List<KeyValueChildItem>) data;
//
//            processAllCategories(cats);
//        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    private void processAllCategories(List<KeyValueChildItem> cats){
        expandableListTitle = new ArrayList<>();

        for (KeyValueChildItem cat : cats) {
            expandableListTitle.add( cat );

        }
        //Collections.sort(expandableListTitle);
        expandableListAdapter = new CategoryExpandableListAdapter(this.getContext(), expandableListTitle);
        expandableListView.setAdapter(expandableListAdapter);
    }

}
