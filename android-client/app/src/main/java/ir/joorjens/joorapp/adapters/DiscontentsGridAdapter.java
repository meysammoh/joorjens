package ir.joorjens.joorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.ToggleButtonPlus;
import ir.joorjens.joorapp.fragments.DistributorRateFragment;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CacheContainer;

/**
 * Created by ZM on 7/17/2018.
 */

public class DiscontentsGridAdapter extends BaseAdapter {

    private Context mContext;

    public DiscontentsGridAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return CacheContainer.get().getDistributorDiscontents().size();
    }

    @Override
    public Object getItem(int position) {
        try{
            return CacheContainer.get().getDistributorDiscontents().get(position);
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        try{
            return CacheContainer.get().getDistributorDiscontents().get(position).getFirst();
        }catch (Exception ex){
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PairResultItem item = CacheContainer.get().getDistributorDiscontents().get(position);

        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.view_discontents, null);
        }

        ToggleButtonPlus tgb = (ToggleButtonPlus) convertView.findViewById(R.id.tgb_discontent);
        tgb.setText(item.getSecond()+"");
        tgb.setTag(item.getFirst());

        return convertView;
    }
}
