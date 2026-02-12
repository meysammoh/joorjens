package ir.joorjens.joorapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.DistributorActivity;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.adapters.TheMostListDistributorProductAdapter;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.KandouBar;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.SupportArea;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.SpacesItemDecoration;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;
import ir.joorjens.joorapp.webService.DistributorsAPIs;

public class DistributorDetailsFragment extends Fragment implements ActivityServiceListener, DistributorRateFragment.DialogListener{

    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    public void onResume() {
        super.onResume();

        if(!mMainScroll.canScrollVertically(1)){
            mRefreshLayout.setEnabled(true);
        }

        loadAndFillData();

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

    private ImageViewPlus distLogo;
    private ImageViewPlus distBanner;
    private TextViewPlus distTitle;
    private TextViewPlus distAddress;
    private TextViewPlus distPhone;
    private TextViewPlus distEmail;
    private TextViewPlus distDescription;
    private TextViewPlus distExperience;
    private ButtonPlus rateBtn;
    private KandouBar distRate;
    private NestedScrollView mMainScroll;
    private SwipeRefreshLayout mRefreshLayout;
    private TheMostListDistributorProductAdapter mProductAdaptor;
    private int mOffset = 0;
    private int mDistributorId = -1;
    private boolean mDataReachedEnd = false;

    private SwipeRefreshLayout.OnRefreshListener onRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            loadAndFillData();
            mRefreshLayout.setRefreshing(false);
        }
    };

    private void prepareRecyclerView(View v){
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.dist_details_rcv_products);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        recyclerView.removeAllViews();
        LinearLayoutManager layoutManager_cheap = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager_cheap);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getContext(), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mProductAdaptor);
    }

    Distributor _distributor;
    public DistributorDetailsFragment() {

    }

    public static DistributorDetailsFragment newInstance(){
        DistributorDetailsFragment fragment = new DistributorDetailsFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // show tabs and toolbar
        ((DistributorActivity)getActivity()).showTabsAndToolbar(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // hide tabs and toolbar
        ((DistributorActivity)getActivity()).showTabsAndToolbar(false);

        View v = inflater.inflate(R.layout.fragment_distributor_details, container, false);
        distLogo = (ImageViewPlus) v.findViewById(R.id.distdetail_img_logo);
        distBanner = (ImageViewPlus) v.findViewById(R.id.distdetail_img_banner);
        distTitle = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_title);
        distAddress = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_address);
        distPhone = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_phone);
        distEmail = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_email);
        distDescription = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_dist_note);
        distExperience = (TextViewPlus) v.findViewById(R.id.distdetail_tvp_experience);
        mMainScroll = (NestedScrollView) v.findViewById(R.id.dist_details_main_scroll);
        mMainScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == 0){
                    mRefreshLayout.setEnabled(true);
                }else{
                    mRefreshLayout.setEnabled(false);
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.dist_details_refresh);
        mRefreshLayout.setOnRefreshListener(onRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.full_sale_color, R.color.cheap_color, R.color.discount_color);

        rateBtn = (ButtonPlus) v.findViewById(R.id.distdetail_btn_rate);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistributorRateFragment dialog = new DistributorRateFragment();
                dialog.setListeners(DistributorDetailsFragment.this, DistributorDetailsFragment.this);
                Bundle b = new Bundle();
                b.putInt("req_code", 3000);
                b.putString("dist_name", _distributor.getName());
                b.putInt("dist_id", _distributor.getId());
                dialog.setArguments(b);
                dialog.show(getActivity().getSupportFragmentManager(), "DistributorDetailsFragment");
            }
        });
        distRate = (KandouBar) v.findViewById(R.id.distdetail_rb_rate);

        mProductAdaptor = new TheMostListDistributorProductAdapter(getContext(), null, -1);
        mProductAdaptor.setEndListener(new MyBaseAdapter.onEndReachedListener() {
            @Override
            public void onEndReached(int position) {
                if(mDistributorId != -1 && !mDataReachedEnd) {
                    Map<String, String> options = new HashMap<>();
                    //options.put("max", APICreator.maxResult + "");
                    //options.put("offset", mOffset + "");
                    options.put("distributorId", mDistributorId + "");
                    DistributorProductAPIs.searchDProducts(DistributorDetailsFragment.this,
                            Authenticator.loadAuthenticationToken(), options,mOffset, 1);
                }
            }
        });

        prepareRecyclerView(v);

        return v;
    }

    private void loadAndFillData(){

        mProductAdaptor.clear();
        mDataReachedEnd = false;
        mOffset = 0;

        HashMap<String, String> params = new HashMap<>();
        mDistributorId = getArguments().getInt( DistributorActivity.DistributorIdParam,-1);
        if(mDistributorId> -1) {

            // distributor products loading
            Map<String, String> options = new HashMap<>();
            //options.put("max", APICreator.maxResult + "");
            //options.put("offset", mOffset + "");
            options.put("distributorId", mDistributorId + "");
            DistributorProductAPIs.searchDProducts(DistributorDetailsFragment.this,
                    Authenticator.loadAuthenticationToken(), options,mOffset, 1);

            params.put(DistributorActivity.DistributorIdParam, mDistributorId+"");
            _distributor = ((DistributorActivity) getActivity()).getDistributor(mDistributorId);
            if(_distributor == null)
                DistributorsAPIs.searchDistributors(this, Authenticator.loadAuthenticationToken(), params, 0);
            fillData();
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {

        if(apiCode==APICode.searchDistributors){
            ResultList res = (ResultList) data;
            _distributor = null;
            if( res.getTotal() > 0){
                _distributor = (Distributor) res.getResult().get(0);
            }
            fillData();
        }
        else if(apiCode == APICode.searchDistributorProduct){
            ResultList<DistributorProduct> dProductList = (ResultList<DistributorProduct>) data;
            if((mOffset +1)*APICreator.maxResult < dProductList.getTotal())
                mOffset++;
            else
                mDataReachedEnd = true;

            mProductAdaptor.addNewItems(dProductList.getResult());
        }
        else if(apiCode == APICode.rateDistributor){
            //TODO rate
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

     private void fillData(){
        if(_distributor!=null){
            StaticHelperFunctions.loadImage(getContext(), _distributor.getImageBanner(), distLogo.getImageView());
            StaticHelperFunctions.loadImage(getContext(), _distributor.getImageStatute(), distBanner.getImageView());
            distRate.setRating( _distributor.getRate());
            distTitle.setText( _distributor.getName());
            distAddress.setText( _distributor.getAddressRemnant());
            distPhone.setText( _distributor.getTelephone());
            distEmail.setText( _distributor.getEmail());
            long t1= (long)_distributor.getCreatedTime()*1000l;
            distExperience.setText(StaticHelperFunctions.getTimeDiffFromCurent(t1));
            String supportedArea = "";
            for (SupportArea area :
                    _distributor.getSupportAreas()) {
                if(!supportedArea.isEmpty())
                    supportedArea+=",";
                supportedArea += area.getName();
            }
            distDescription.setText("");
            if(supportedArea != "") {
                distDescription.setText(getContext().getResources().getString(R.string.word_supported_areas) + ": " + supportedArea);
            }
        }
     }

    @Override
    public void onDialogClick(DialogFragment dialog, int requestCode) {

    }
}
