package ir.joorjens.joorapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.adapters.DeliveryOrderListAdapter;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.DelivererAPIs;
import ir.joorjens.joorapp.webService.DistributorsAPIs;

public class DeliveryOrdersListFragment extends Fragment implements ActivityServiceListener{

    private boolean mActive = false;
    private DeliveryOrderListAdapter mAdapter;
    private RecyclerView mRcvOrders;
   // private View.OnClickListener onItemClickListener;

    public DeliveryOrdersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_delivery_orders_list, container, false);

        mAdapter = new DeliveryOrderListAdapter(getContext(), null);

        mRcvOrders = (RecyclerView) v.findViewById(R.id.del_rcv_order_list);
        mRcvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRcvOrders.setAdapter(mAdapter);

        return v;
    }

//    public void setItemClickListener(View.OnClickListener itemClickListener){
//        onItemClickListener = itemClickListener;
//    }

    private void loadData(){
        DelivererAPIs.getDelivererOrderList(this, Authenticator.loadAuthenticationToken(), 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActive = true;

        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mActive = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mActive = false;
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.searchCartDist){
            ResultList<CartDistributor> cartDistts = (ResultList<CartDistributor>) data;
            mAdapter.addNewItems(cartDistts.getResult());
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {

    }

    @Override
    public boolean isActive() {
        return mActive;
    }
}
