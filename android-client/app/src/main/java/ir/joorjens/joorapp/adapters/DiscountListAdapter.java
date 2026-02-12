package ir.joorjens.joorapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.CrossLineTextView;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;

/**
 * Created by mohsen on 10/18/2017.
 */

public class DiscountListAdapter extends RecyclerView.Adapter<DiscountListAdapter.DiscountItemHolder>{

    public void addNewItems(List<DistributorPackage> newItems){
        mItems.addAll(newItems);
        notifyDataSetChanged();
    }

    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    private Context mContext;
    private List<DistributorPackage> mItems;
    private int mColorId = R.color.discount_color;
    private int mParentIconResourceId = R.drawable.all_discount_logo;
    private String mParentTitle = "بسته های تخفیفی";

    public  DiscountListAdapter(Context context, List<DistributorPackage> items){
        mContext = context;
        if(items == null){
            mItems = new ArrayList<>();
        }
        else
            mItems = items;
    }
    @Override
    public DiscountItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discount_item, parent, false);

        return new DiscountItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiscountItemHolder holder, int position) {
        DistributorPackage item = mItems.get(position);
        holder.mIvThumbnail.setDiscountValue(item.getAllDiscountPercent());
        holder.mTvBrandName.setText(item.getName());
        holder.mTvOldPrice.setText(String.format("%,d",item.getAllPrice()));
        holder.mTvNewPrice.setText(String.format("%,d",item.getAllPriceWithDiscount()));

        //holder.mIvThumbnail.getLayoutParams().width = 80;
        StaticHelperFunctions.loadImage(mContext, item.getImage(), holder.mIvThumbnail.getImageView());

        holder.mLlBody.setTag(item.getId());
        holder.mLlBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showProductIntent = new Intent(mContext, ProductActivity.class);
                showProductIntent.putExtra("product_id", v.getTag() + "");
                showProductIntent.putExtra("color_id", mColorId + "");
                showProductIntent.putExtra("icon_id", mParentIconResourceId + "");
                showProductIntent.putExtra("title", mParentTitle + "");
                StaticHelperFunctions.openActivity(mContext, showProductIntent);
            }
        });

        if(position == mItems.size() -1){
            if(mEndListener != null)
                mEndListener.onEndReached(position);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class DiscountItemHolder extends RecyclerView.ViewHolder{

        private TextViewPlus mTvBrandName, mTvNewPrice;
        private CrossLineTextView mTvOldPrice;
        private ImageViewPlus mIvThumbnail;
        private LinearLayout mLlBody;
        public DiscountItemHolder(View itemView) {
            super(itemView);
            mTvBrandName = (TextViewPlus)itemView.findViewById(R.id.discount_item_brand_name);
            mTvOldPrice =  (CrossLineTextView)itemView.findViewById(R.id.discount_item_old_price);
            mTvNewPrice = (TextViewPlus)itemView.findViewById(R.id.discount_item_new_price);
            mIvThumbnail = (ImageViewPlus) itemView.findViewById(R.id.discount_item_image);
            mLlBody = (LinearLayout) itemView.findViewById(R.id.discount_item_body);
        }
    }

}
