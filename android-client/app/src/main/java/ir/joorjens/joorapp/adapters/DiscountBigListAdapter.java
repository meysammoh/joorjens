package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.CrossLineTextView;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;

/**
 * Created by Mohsen on 11/2/2017.
 */

public class DiscountBigListAdapter extends RecyclerView.Adapter<DiscountBigListAdapter.DiscountBigItemHolder> {

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

    public DiscountBigListAdapter(Context context, List<DistributorPackage> items) {
        mContext = context;
        if(items == null){
            mItems = new ArrayList<>();
        }
        else
            mItems = items;
    }

    @Override
    public DiscountBigItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.the_most_big_items, parent, false);

        return new DiscountBigItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiscountBigItemHolder holder, int position) {
        DistributorPackage item = mItems.get(position);
        holder.mTvText1.setText(item.getName());
        holder.mTvText1_1.setVisibility(View.GONE);
        holder.mTvText2.setText("شامل " + item.getPackageProducts().size() + " نوع کالا");
        holder.mTvText3.setVisibility(View.GONE);
        holder.mLlDiscContainer.setVisibility(View.VISIBLE);
        holder.mTvPriceDisc.setText(String.format("%,d", item.getAllPriceWithDiscount()));
        holder.mTvPriceNoDisc.setText(String.format("%,d", item.getAllPrice()));

        holder.mIvThumbnail.setDiscountValue(item.getAllDiscountPercent());

        StaticHelperFunctions.loadImage(mContext, item.getImage(), holder.mIvThumbnail.getImageView());

        holder.mRlMainBorder.setTag(item.getId());
        holder.mRlMainBorder.setOnClickListener(onShowDetailClick);

        if(position == mItems.size() -1){
            mEndListener.onEndReached(position);
        }
    }

    private View.OnClickListener onShowDetailClick = new View.OnClickListener(){

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

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class DiscountBigItemHolder extends RecyclerView.ViewHolder {

        private TextViewPlus mTvText1, mTvText1_1, mTvText2, mTvText3, mTvPriceDisc;
        private CrossLineTextView mTvPriceNoDisc;
        private LinearLayout mLlDiscContainer;
        private ImageViewPlus mIvThumbnail;
        protected LinearLayout mRlMainBorder;

        public DiscountBigItemHolder(View itemView) {
            super(itemView);
            mTvText1 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text1);
            mTvText1_1 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text1_1);
            mTvText2 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text2);
            mTvText3 = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text3);
            mTvPriceNoDisc = (CrossLineTextView) itemView.findViewById(R.id.tv_tmbi_text_price_no_disc);
            mTvPriceDisc = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text_price_disc);
            mLlDiscContainer = (LinearLayout) itemView.findViewById(R.id.ll_tmbi_disc_container);
            mIvThumbnail = (ImageViewPlus) itemView.findViewById(R.id.img_tmbi_thumbnail);
            mIvThumbnail.setShowDiscountValue(true);
            mRlMainBorder = (LinearLayout) itemView.findViewById(R.id.rl_tmbi_main_border);
        }
    }
}
