package ir.joorjens.joorapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.HashMap;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.DistributorActivity;
import ir.joorjens.joorapp.customViews.DistributorRow;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.DistributorsAPIs;

public class DistributorListByNameFragment extends Fragment implements ActivityServiceListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
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

    private NestedScrollView mMainScroll;
    private SwipeRefreshLayout mRefreshLayout;

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            loadDists();
            mRefreshLayout.setRefreshing(false);
        }
    };

    LinearLayout distsLinearLayout;
    TextViewPlus _tvDistNotFound;

    private int lastExpandedPosition = -1;

    public DistributorListByNameFragment() {

    }

    public static DistributorListByNameFragment newInstance(){
        DistributorListByNameFragment fragment = new DistributorListByNameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_distributor_list_by_name, container, false);

        distsLinearLayout = (LinearLayout) v.findViewById(R.id.distlist_container_distributors);
        _tvDistNotFound = (TextViewPlus) v.findViewById(R.id.fr_dis_list_by_name_dist_not_found);

        mMainScroll = (NestedScrollView) v.findViewById(R.id.dist_list_by_name_main_scroll);
        mMainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    mRefreshLayout.setEnabled(true);
                } else {
                    mRefreshLayout.setEnabled(false);
                }

                if(scrollY < oldScrollY){
                    ((DistributorActivity)getActivity()).setTopTabsVisibility(true);
                }
                else{
                    ((DistributorActivity)getActivity()).setTopTabsVisibility(false);
                }
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.dist_by_date_refresh);
        mRefreshLayout.setOnRefreshListener(onRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        ((DistributorActivity)getActivity()).setTopTabsVisibility(false);

        if(!mMainScroll.canScrollVertically(1)){
            mRefreshLayout.setEnabled(true);
        }

        _isActive = true;

        loadDists();
    }

    private void loadDists(){
        HashMap<String, String> params = new HashMap<>();
        String distName = getArguments().getString(DistributorActivity.DistributorNameParam, "");
        String distBrands = getArguments().getString(DistributorActivity.DistributorBrandListParam, "");
        String distCategories = getArguments().getString(DistributorActivity.DistributorCategoryListParam, "");
        if( !distName.isEmpty() ) {
            params.put(DistributorActivity.DistributorNameParam, distName);
            params.put("like", "true");
        }
        if( !distBrands.isEmpty() )
            params.put(DistributorActivity.DistributorBrandListParam, distBrands);
        if( !distCategories.isEmpty() )
            params.put(DistributorActivity.DistributorCategoryListParam, distCategories);
        params.put("ordertypeid", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdName)+"");
        params.put("asc", "false");

        DistributorsAPIs.searchDistributors( this, Authenticator.loadAuthenticationToken() ,params, 0);
    }

    //
    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        ResultList<Distributor> result = (ResultList<Distributor>) data;
        HashMap<Integer, Distributor> distributors = new HashMap<>();
        distsLinearLayout.removeAllViews();
        if(result.getTotal() > 0){
            _tvDistNotFound.setVisibility(View.GONE);
            for(Distributor dist: result.getResult()){
                DistributorRow distrow = new DistributorRow(getContext());
                distrow.setRowValues(dist);
                distsLinearLayout.addView( distrow);
                distributors.put( dist.getId(), dist);
            }
            ((DistributorActivity)getActivity()).setDistributors( distributors);
        }else{
            _tvDistNotFound.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this.getActivity(), ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }


}
