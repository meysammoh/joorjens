package ir.joorjens.joorapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.DeliveryViewCartDistributorActivity;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.CrossLineTextView;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by mohsen on 10/18/2017.
 */

public class DeliveryOrderListAdapter extends RecyclerView.Adapter<DeliveryOrderListAdapter.DeliveryOrderItemHolder>{

    public void addNewItems(List<CartDistributor> newItems){
        mItems.addAll(newItems);
        notifyDataSetChanged();
    }

    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    private Context mContext;
    private List<CartDistributor> mItems;
    private int mColorId = R.color.discount_color;
    private int mParentIconResourceId = R.drawable.all_discount_logo;
    private String mParentTitle = "بسته های تخفیفی";

    public DeliveryOrderListAdapter(Context context, List<CartDistributor> items){
        mContext = context;
        if(items == null){
            mItems = new ArrayList<>();
        }
        else
            mItems = items;
    }
    @Override
    public DeliveryOrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_delivery_order_row, parent, false);

        return new DeliveryOrderItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeliveryOrderItemHolder holder, int position) {
        final CartDistributor item = mItems.get(position);
        holder.mTvStoreName.setText(item.getStoreName());
        holder.mTvOrderSerial.setText("شماره سفارش: " + item.getSerial());
        holder.mTvPrice.setText(String.format("%,d",item.getCartPrice().getAllPriceDiscount())+" تومان");
        holder.mTvArea.setText("منطقه "+item.getStoreAreaZoneName());

        holder.mBtnMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showDeliveryOrderIntent = new Intent(mContext, DeliveryViewCartDistributorActivity.class);
                showDeliveryOrderIntent.putExtra(DeliveryViewCartDistributorActivity.cartDistParamName, item);
                StaticHelperFunctions.openActivity(mContext, showDeliveryOrderIntent);
            }
        });

        if(position == mItems.size() -1){
            if(mEndListener != null)
                mEndListener.onEndReached(position);
        }
    }

    @Override
    public int getItemCount() {
        if(mItems != null)
            return mItems.size();

        return 0;
    }

    public CartDistributor getItem(int position){
        if(mItems != null){
            return mItems.get(position);
        }

        return null;
    }

    public class DeliveryOrderItemHolder extends RecyclerView.ViewHolder{

        private TextViewPlus mTvStoreName, mTvOrderSerial, mTvPrice, mTvArea;
        private ImageViewPlus mIvArea;
        ButtonPlus mBtnMoreDetail;
        public DeliveryOrderItemHolder(View itemView) {
            super(itemView);
            mTvStoreName = (TextViewPlus)itemView.findViewById(R.id.del_store_name);
            mTvOrderSerial = (TextViewPlus)itemView.findViewById(R.id.del_order_serial);
            mTvPrice = (TextViewPlus)itemView.findViewById(R.id.del_tv_price);
            mTvArea = (TextViewPlus)itemView.findViewById(R.id.del_tv_area);
            mIvArea = (ImageViewPlus) itemView.findViewById(R.id.del_iv_area);
            mBtnMoreDetail = (ButtonPlus) itemView.findViewById(R.id.del_btn_more_detail);
        }
    }

}
