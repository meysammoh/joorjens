package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
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

public class TheMostBigListProductAdapter extends RecyclerView.Adapter<TheMostBigListProductAdapter.TheMostBigItemHolder> {

    private Context mContext;
    //private List<Product> mItems;
    private int mColorId = -1;
    private int mParentIconResourceId = -1;
    private String mParentTitle = "";

//    public void addNewItems(List<Product> newItems){
//        mItems.addAll(newItems);
//        notifyDataSetChanged();
//    }

    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    public TheMostBigListProductAdapter(Context context, int colorId) {
        mContext = context;
//        if(items == null)
//            mItems = new ArrayList<>();
//        else
//            mItems = items;

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
        }
    }

    @Override
    public TheMostBigItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.the_most_big_items, parent, false);

        return new TheMostBigItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheMostBigItemHolder holder, int position) {
        Product item = null;
        if(mColorId == R.color.full_sale_color){
            item = CacheContainer.get().getTopSaleProducts().get(position);
            if(position == CacheContainer.get().getTopSaleProducts().size() -1){
                mEndListener.onEndReached(position);
            }
        }
        else if(mColorId == R.color.new_color){
            item = CacheContainer.get().getNewProducts().get(position);
            if(position == CacheContainer.get().getNewProducts().size() -1){
                mEndListener.onEndReached(position);
            }
        }

        holder.mTvText1.setText(item.getName() + " " + item.getProductBrandTypeName());
        //holder.mTvText1_1.setText("برند: " + item.getProductBrandTypeName());
        holder.mLlDiscContainer.setVisibility(View.GONE);
        holder.mTvText3.setVisibility(View.GONE);

        String allDetails = "";
        for(ProductDetails detail : item.getProductDetails()){
            if(detail.getProductDetailTypeName().equals("مشخصات کالا") && !detail.getValue().equals("")){
                allDetails += detail.getValue();
                break;
            }
        }
        //if(allDetails.isEmpty())
            holder.mTvText2.setVisibility(View.GONE);
        //else
            holder.mTvText1_1.setText(allDetails);
            //holder.mTvText2.setText( allDetails );

        StaticHelperFunctions.loadImage(mContext, item.getImage(), holder.mIvThumbnail.getImageView());

        holder.mRlMainBorder.setTag(item.getId());
        holder.mRlMainBorder.setOnClickListener(onShowProduct);
    }

    private View.OnClickListener onShowProduct = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent showProductIntent = new Intent(mContext, ProductActivity.class);
            showProductIntent.putExtra("product_id", v.getTag() + "");
            showProductIntent.putExtra("color_id", mColorId + "");
            showProductIntent.putExtra("icon_id", mParentIconResourceId + "");
            showProductIntent.putExtra("title", mParentTitle + "");
            StaticHelperFunctions.openActivity(mContext, showProductIntent);
        }
    };

    public int getItemCount() {
        if(mColorId == R.color.full_sale_color){
            return CacheContainer.get().getTopSaleProducts().size();
        }
        else if(mColorId == R.color.new_color){
            return CacheContainer.get().getNewProducts().size();
        }

        return 0;
    }

    public class TheMostBigItemHolder extends RecyclerView.ViewHolder {

        private TextViewPlus mTvText1, mTvText1_1, mTvText2, mTvText3;
        private LinearLayout mLlDiscContainer;
        private ImageViewPlus mIvThumbnail;
        private LinearLayout mRlMainBorder;

        public TheMostBigItemHolder(View itemView) {
            super(itemView);
            mTvText1 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text1);
            mTvText1_1 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text1_1);
            mTvText2 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text2);
            mTvText3 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text3);
            mLlDiscContainer = (LinearLayout) itemView.findViewById(R.id.ll_tmbi_disc_container);
            mIvThumbnail = (ImageViewPlus) itemView.findViewById(R.id.img_tmbi_thumbnail);
            mRlMainBorder = (LinearLayout) itemView.findViewById(R.id.rl_tmbi_main_border);
        }
    }
}
