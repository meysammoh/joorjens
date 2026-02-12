package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CacheContainer;

/**
 * Created by Mohsen on 11/2/2017.
 */

public class TheMostListProductAdapter extends RecyclerView.Adapter<TheMostListProductAdapter.TheMostItemHolder> {

    private Context mContext;
    private List<Product> mItems;
    private int mColorId = -1;
    private int mParentIconResourceId = -1;
    private String mParentTitle = "";

    public void addNewItems(List<Product> newItems){
        if(mItems == null)
            mItems = new ArrayList<>();

        mItems.addAll(newItems);
        notifyDataSetChanged();
    }
    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    public TheMostListProductAdapter(Context context, List<Product> items, int colorId){
        mContext = context;
        if(items == null){
            mItems = new ArrayList<>();
        }else{
            mItems = items;
        }

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
        Product item = null;
        int size = 0;

        if(mColorId == -1 /*mItems != null*/){
            item = mItems.get(position);
            size = mItems.size() - 1;
        }else if(mColorId == R.color.new_color){
            item = CacheContainer.get().getNewProducts().get(position);
            size = CacheContainer.get().getNewProducts().size() - 1;
        }else if(mColorId == R.color.full_sale_color){
            item = CacheContainer.get().getTopSaleProducts().get(position);
            size = CacheContainer.get().getTopSaleProducts().size() - 1;
        }

        String allDetails = "";
        for(ProductDetails detail : item.getProductDetails()){
            if(detail.getProductDetailTypeName().equals("مشخصات کالا") && !detail.getValue().equals("")){
                allDetails += detail.getValue();
                break;
            }
        }

        String name = item.getName() + " " + item.getProductBrandTypeName();
        holder.mTvBrandName.setText( name );
        if(!allDetails.equals(""))
            holder.mTvPrice.setText(allDetails);
        else
            holder.mTvPrice.setVisibility(View.GONE);

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
        }else if(mColorId == R.color.new_color){
            return CacheContainer.get().getNewProducts().size();
        }else if(mColorId == R.color.full_sale_color){
            return CacheContainer.get().getTopSaleProducts().size();
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
