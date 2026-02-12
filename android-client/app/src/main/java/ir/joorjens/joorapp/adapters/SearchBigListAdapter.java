package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;

/**
 * Created by Mohsen on 11/2/2017.
 */

public class SearchBigListAdapter extends RecyclerView.Adapter<SearchBigListAdapter.TheMostBigItemHolder> {

    private Context mContext;
    private List<Product> mItems;
    private int mParentIconResourceId = R.drawable.search_result;
    private String mParentTitle =  "نتیجه جستجو";

    public SearchBigListAdapter(Context context, List<Product> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TheMostBigItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.the_most_big_items, parent, false);

        return new TheMostBigItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheMostBigItemHolder holder, int position) {
        Product item = mItems.get(position);
        holder.mTvBrandName.setText(item.getName() + " " + item.getProductCategoryTypeName());
        String allDetails = "";
        for(ProductDetails detail : item.getProductDetails()){
            if(detail.getValue() != null && detail.getValue() != ""){
                allDetails += detail.getValue() + " ";
            }
        }
        holder.mTvPackingType.setText(allDetails);
        StaticHelperFunctions.loadImage(mContext, item.getImage(), holder.mIvThumbnail.getImageView());
        holder.mRlMainBorder.setTag(item.getId());
        holder.mRlMainBorder.setOnClickListener(onShowProduct);
        holder.mBtnShowDetail.setTag(item.getId());
        //holder.mBtnShowDetail.setBackground();
        holder.mBtnShowDetail.setOnClickListener(onShowProduct);
    }

    private View.OnClickListener onShowProduct = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent showProductIntent = new Intent(mContext, ProductActivity.class);
            showProductIntent.putExtra("product_id", v.getTag() + "");
            showProductIntent.putExtra("color_id", -1 + "");
            showProductIntent.putExtra("icon_id", mParentIconResourceId + "");
            showProductIntent.putExtra("title", mParentTitle + "");
            StaticHelperFunctions.openActivity(mContext, showProductIntent);
        }
    };

    public int getItemCount() {
        return mItems.size();
    }

    public class TheMostBigItemHolder extends RecyclerView.ViewHolder {

        private TextViewPlus mTvBrandName, mTvPackingType;
        private ImageViewPlus mIvThumbnail;
        private ButtonPlus mBtnShowDetail;
        private LinearLayout mRlMainBorder;

        public TheMostBigItemHolder(View itemView) {
            super(itemView);
            mTvBrandName = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text1);
            mTvPackingType = (TextViewPlus) itemView.findViewById(R.id.tv_tmbi_text2);
            mIvThumbnail = (ImageViewPlus) itemView.findViewById(R.id.img_tmbi_thumbnail);
            mRlMainBorder = (LinearLayout) itemView.findViewById(R.id.rl_tmbi_main_border);

            ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
                @Override
                public Shader resize(int width, int height) {
                    LinearGradient linearGradient = new LinearGradient(0, 0, mBtnShowDetail.getWidth(), 0,
                            new int[] {
                                    ContextCompat.getColor(SearchBigListAdapter.this.mContext, R.color.color_fourth),
                                    ContextCompat.getColor(SearchBigListAdapter.this.mContext, R.color.color_third),
                                    ContextCompat.getColor(SearchBigListAdapter.this.mContext, R.color.color_second),
                                    ContextCompat.getColor(SearchBigListAdapter.this.mContext, R.color.color_first) }, //substitute the correct colors for these
                            new float[] {
                                    0, 0.33f, 0.66f, 1 },
                            Shader.TileMode.REPEAT);
                    return linearGradient;
                }
            };
            PaintDrawable paint = new PaintDrawable();
            paint.setShape(new RectShape());
            paint.setShaderFactory(shaderFactory);
            mBtnShowDetail.setBackground(paint);
        }
    }
}
