package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Advertising;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by ZM on 5/18/2018.
 */

public class AdvertisingAdapter extends RecyclerView.Adapter<AdvertisingAdapter.AdvertisingItemHolder>{

    private Context mContext;
    private List<Advertising> mItems;

    public AdvertisingAdapter(Context context, List<Advertising> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public AdvertisingItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_advertising, parent, false);

        return new AdvertisingItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdvertisingItemHolder holder, int position) {
        Advertising item = mItems.get(position);
        // set holder params here with fields of 'item'
        holder.mImgAdvPic.setBackgroundResource(item.getImageId());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class AdvertisingItemHolder extends RecyclerView.ViewHolder{

        private ImageView mImgAdvPic;
        public AdvertisingItemHolder(View itemView) {
            super(itemView);
            mImgAdvPic = (ImageView) itemView.findViewById(R.id.adv_img);
        }
    }
}
