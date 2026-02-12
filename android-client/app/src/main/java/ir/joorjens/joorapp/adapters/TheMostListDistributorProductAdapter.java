package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.fragments.DistributorDetailsFragment;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CacheContainer;

/**
 * Created by Mohsen on 11/2/2017.
 */

public class TheMostListDistributorProductAdapter extends RecyclerView.Adapter<TheMostListDistributorProductAdapter.TheMostItemHolder> {

    public  void clear(){
        if(mItems != null)
            mItems.clear();
    }

    public void addNewItems(List<DistributorProduct> newItems){
        if(mItems == null)
            mItems = new ArrayList<>() ;

        mItems.addAll(newItems);
        notifyDataSetChanged();
    }

    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }


    private Context mContext;
    private List<DistributorProduct> mItems;
    private int mColorId = -1;
    private int mParentIconResourceId = -1;
    private String mParentTitle = "";

    public TheMostListDistributorProductAdapter(Context context, List<DistributorProduct> items, int colorId){
        mContext = context;
        mItems = items;

        mColorId = colorId;
        switch (mColorId) {
            case R.color.cheap_color:
                mParentIconResourceId = R.drawable.all_cheap_logo;
                mParentTitle = "ارزان ها";
                break;
            case R.color.new_color:
                mParentIconResourceId = R.drawable.all_new_logo;
                mParentTitle = "جدید ها";
                break;
            case R.color.full_sale_color:
                mParentIconResourceId = R.drawable.all_full_sale_logo;
                mParentTitle = "پرفروش ها";
                break;
            case R.color.discount_color:
                mParentIconResourceId = R.drawable.all_discount_logo;
                mParentTitle = "بسته های تخفیفی";
                break;

        }
    }

    @Override
    public TheMostItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.the_most_items, parent, false);

        return new TheMostItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheMostItemHolder holder, int position) {
        DistributorProduct item = null;
        int size = 0;

        if(mColorId == -1 /*mItems != null*/){
            item = mItems.get(position);
            size = mItems.size() - 1;
        }else if(mColorId == R.color.cheap_color){
            item = CacheContainer.get().getCheapProducts().get(position);
            size = CacheContainer.get().getCheapProducts().size() - 1;
        }else if(mColorId == R.color.show_all_color){
            item = CacheContainer.get().getAllProducts().get(position);
            size = CacheContainer.get().getAllProducts().size() - 1;
        }

        String nameAndDetails = item.getProductName() + " " + item.getProductBrandTypeName();
        for(ProductDetails detail : item.getProductDetails()){
            if(detail.getValue() != null && detail.getValue() != ""){
                nameAndDetails += " " + detail.getValue();
            }
        }
        holder.mTvBrandName.setText( nameAndDetails );

        if(mColorId == R.color.cheap_color) {
            holder.mTvPrice.setVisibility(View.VISIBLE);
            holder.mTvPrice.setText(String.format("%,d", item.getPriceMin()) + " تومان");
        }
        else if(mColorId == R.color.show_all_color){
            //String allDiscounts = "";
            float fAllDiscounts = 0;
            for(Discount discount : item.getDiscounts()){
                if (discount.getValidTime() && discount.getType() != EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountOffer)) {
                    fAllDiscounts += discount.getPercent();
                    //allDiscounts += discount.getDiscountStr() + " ";
                }
            }

            holder.mIvThumbnail.setShowDiscountValue(false);
            if(fAllDiscounts > 0)
                holder.mIvThumbnail.setShowDiscountValue(true);
            holder.mIvThumbnail.setDiscountValue((double)fAllDiscounts);
            holder.mTvPrice.setVisibility(View.GONE);
        }
        else{
            holder.mTvPrice.setVisibility(View.GONE);
        }

        //holder.mIvThumbnail.getLayoutParams().width = 80;
        StaticHelperFunctions.loadImage(mContext, item.getProductImage(), holder.mIvThumbnail.getImageView());

        holder.mLlBody.setTag(item.getProductId());
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

        if(position == size){
            if(mEndListener != null)
                mEndListener.onEndReached(position);
        }
    }

    @Override
    public int getItemCount() {
        if(mColorId == -1 /*mItems != null*/){
            if(mItems != null)
                return mItems.size();
        }else if(mColorId == R.color.cheap_color){
            return CacheContainer.get().getCheapProducts().size();
        }else if(mColorId == R.color.show_all_color){
            return CacheContainer.get().getAllProducts().size();
        }
        return 0;
    }

    public class TheMostItemHolder extends RecyclerView.ViewHolder{

        private TextViewPlus mTvBrandName, mTvPrice;
        private ImageViewPlus mIvThumbnail;
        private LinearLayout mLlBody;
        public TheMostItemHolder(View itemView) {
            super(itemView);
            mTvBrandName = (TextViewPlus)itemView.findViewById(R.id.the_most_items_brand_name);
            mTvPrice = (TextViewPlus)itemView.findViewById(R.id.the_most_items_price);
            mIvThumbnail = (ImageViewPlus) itemView.findViewById(R.id.the_most_items_image);
            mLlBody = (LinearLayout) itemView.findViewById(R.id.the_most_items_body);
        }
    }
}
