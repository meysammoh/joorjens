package ir.joorjens.joorapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.ProductActivity;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.ImageViewPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.Message;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ProductDetails;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.SolarCalendar;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.MessageAPIs;
import ir.joorjens.joorapp.webService.params.SeenMessageParams;

/**
 * Created by Mohsen on 11/2/2017.
 */

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageItemHolder> {

    private Context mContext;
    private List<Message> mItems;

    public void addNewItems(List<Message> newItems){
        mItems.addAll(newItems);
        notifyDataSetChanged();
    }
    private MyBaseAdapter.onEndReachedListener mEndListener;
    public void setEndListener(MyBaseAdapter.onEndReachedListener listener){
        mEndListener = listener;
    }

    public MessagesListAdapter(Context context, List<Message> items){
        mContext = context;
        if(items == null){
            mItems = new ArrayList<>();
        }else{
            mItems = items;
        }
    }

    @Override
    public MessageItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_order_row, parent, false);

        return new MessageItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageItemHolder holder, final int position) {
        final Message item = mItems.get(position);

        holder._sender.setText("فرستنده : " + item.getSenderName());

        if(item.getMessageReceivers().get(0).getSeen() == false){

            holder._sender.setTypeface(holder._sender.getTypeface(), Typeface.BOLD);;
            holder._date.setTypeface(holder._date.getTypeface(), Typeface.BOLD);
            holder._msgTitle.setTypeface(holder._msgTitle.getTypeface(), Typeface.BOLD);
            holder._msg.setTypeface(holder._msg.getTypeface(), Typeface.BOLD);
        }

        Calendar cal = Calendar.getInstance();
        long timeMilli = item.getCreatedTime()*1000l;
        cal.setTimeInMillis(timeMilli);
        SolarCalendar jalCal = new SolarCalendar(cal.getTime());
        holder._date.setText(jalCal.getSolarDateString());

        holder._msg.setText(item.getTitle());

        holder._moreButton.setTag(item);
        holder._moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = (Message)v.getTag();

                StaticHelperFunctions.showConfirmDialog((Activity) mContext,
                        message.getText(), StaticHelperFunctions.MessageType.Normal,null, "بستن");


                if(message.getMessageReceivers().get(0).getSeen() == false) {
                    // change seen status
                    List<Pair<Integer, Integer>> lst = new ArrayList<>();
                    lst.add(new Pair<Integer, Integer>(item.getId(), item.getMessageReceivers().get(0).getId()));
                    SeenMessageParams params = new SeenMessageParams(lst);
                    MessageAPIs.setMessagesStatus((ActivityServiceListener) mContext,
                            Authenticator.loadAuthenticationToken(), params, position);
                }

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

    public Message getItem(int position){
        if(mItems != null){
            return mItems.get(position);
        }

        return null;
    }

    public class MessageItemHolder extends RecyclerView.ViewHolder{

        private TextViewPlus _sender, _date, _msg, _msgTitle;
        private LinearLayout _llCount, _llProfit, _llCost;
        private ButtonPlus _moreButton;
        public MessageItemHolder(View itemView) {
            super(itemView);

            _sender = (TextViewPlus) itemView.findViewById(R.id.tv_order_row_title);
            _date = (TextViewPlus) itemView.findViewById(R.id.tv_order_row_date );
            _msg = (TextViewPlus) itemView.findViewById(R.id.tv_order_row_cost );
            _msgTitle = (TextViewPlus) itemView.findViewById(R.id.tv_order_row_cost_title );
            _msgTitle.setText("عنوان : ");
            _llCount = (LinearLayout) itemView.findViewById(R.id.ll_count);
            _llCost = (LinearLayout) itemView.findViewById(R.id.ll_cost);
            _llProfit = (LinearLayout) itemView.findViewById(R.id.ll_profit);
            _llCount.setVisibility(View.GONE);
            _llCost.setVisibility(View.VISIBLE);
            _llProfit.setVisibility(View.GONE);
            _moreButton = (ButtonPlus) itemView.findViewById(R.id.btn_order_more_details );
            _moreButton.setText("مشاهده متن پیام");
        }
    }
}
