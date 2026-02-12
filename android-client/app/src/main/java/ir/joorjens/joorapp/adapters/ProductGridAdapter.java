package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CacheContainer;

/**
 * Created by ZM on 7/17/2018.
 */

public class ProductGridAdapter extends BaseAdapter {

    private Context mContext;

    public ProductGridAdapter(Context context) {
        mContext = context;
    }

    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    @Override
    public int getCount() {
        return CacheContainer.get().getAllProducts().size();
    }

    @Override
    public Object getItem(int position) {
        try{
            return CacheContainer.get().getAllProducts().get(position);
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        try{
            return CacheContainer.get().getAllProducts().get(position).getId();
        }catch (Exception ex){
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DistributorProduct dp = CacheContainer.get().getAllProducts().get(position);

        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.view_product_grid_item, null);
        }

        TextViewPlus mTvBrandName = (TextViewPlus)convertView.findViewById(R.id.the_most_items_brand_name);
        TextViewPlus mTvPrice = (TextViewPlus)convertView.findViewById(R.id.the_most_items_price);
        ImageViewPlus mIvThumbnail = (ImageViewPlus) convertView.findViewById(R.id.the_most_items_image);
        LinearLayout mLlBody = (LinearLayout) convertView.findViewById(R.id.the_most_items_body);

        String allDetails = "";
        for(ProductDetails detail : dp.getProductDetails()){
            if(detail.getProductDetailTypeName().equals("مشخصات کالا") && !detail.getValue().equals("")){
                allDetails += detail.getValue();
                break;
            }
        }

        String name = dp.getProductName() + " " + dp.getProductBrandTypeName();
        mTvBrandName.setText( name );

        mTvPrice.setVisibility(View.VISIBLE);
        if(!allDetails.equals(""))
            mTvPrice.setText(allDetails);
        else
            mTvPrice.setVisibility(View.GONE);

        //String allDiscounts = "";
        float fAllDiscounts = 0;
        for(Discount discount : dp.getDiscounts()){
            if (discount.getValidTime() && discount.getType() != EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountOffer)) {
                fAllDiscounts += discount.getPercent();
                //allDiscounts += discount.getDiscountStr() + " ";
            }
        }

        mIvThumbnail.setShowDiscountValue(false);
        if(fAllDiscounts > 0)
            mIvThumbnail.setShowDiscountValue(true);
        mIvThumbnail.setDiscountValue((double)fAllDiscounts);

        //holder.mIvThumbnail.getLayoutParams().width = 80;
        StaticHelperFunctions.loadImage(mContext, dp.getProductImage(), mIvThumbnail.getImageView());

        mLlBody.setTag(dp.getProductId());
        mLlBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showProductIntent = new Intent(mContext, ProductActivity.class);
                showProductIntent.putExtra("product_id", v.getTag() + "");
                //showProductIntent.putExtra("color_id", mColorId + "");
                //showProductIntent.putExtra("icon_id", mParentIconResourceId + "");
                StaticHelperFunctions.openActivity(mContext, showProductIntent);
            }
        });

        if(position == CacheContainer.get().getAllProducts().size() -1){
            if(mEndListener != null)
                mEndListener.onEndReached(position);
        }

        return convertView;
    }
}
