package ir.joorjens.joorapp.adapters;

/**
 * Created by meysammoh on 14.11.17.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.customViews.ToggleButtonPlus;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.OrderStatusController;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.webService.CartAPIs;

public class OrderDistributorExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CartDistributor> _cartDistributors;
    private ActivityServiceListener _listener;
    public OrderDistributorExpandableListAdapter(Context context, List<CartDistributor> cartDistributors) {
        this.context = context;
        _cartDistributors = cartDistributors;
        for(CartDistributor cdist: _cartDistributors){
            CartDistributorProduct cdp = new CartDistributorProduct();
            cdist.getPackageSet().add(cdist.getPackageSize(), cdp);
        }
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return _cartDistributors.get(listPosition).getPackageSet().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        final CartDistributorProduct cartDistributorProduct = ((CartDistributorProduct) getChild(listPosition, expandedListPosition));

        String expandedListText = "";
        if(cartDistributorProduct.getDistributorProductId() != null &&
                cartDistributorProduct.getDistributorProductId() != 0)
            expandedListText =cartDistributorProduct.getDistributorProductName();
        else if(cartDistributorProduct.getDistributorPackageId() != null &&
                cartDistributorProduct.getDistributorPackageId() != 0)
            expandedListText =cartDistributorProduct.getDistributorProductName();

        if (convertView == null) {

            if (expandedListPosition == _cartDistributors.get(listPosition).getPackageSize()){
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_order_distributor_item_summary, null);

                ButtonPlus _btnOrdersexpandedListSumLessDetail = (ButtonPlus)
                        convertView.findViewById(R.id.btn_ordersexpandedList_sum_less_detail);
                _btnOrdersexpandedListSumLessDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ExpandableListView) parent).collapseGroup(listPosition);
                    }
                });
                TextViewPlus _tvOrdersexpandedListSumPrice = (TextViewPlus)
                        convertView.findViewById(R.id.tv_ordersexpandedList_sum_price);
                TextViewPlus _tvOrdersexpandedListSumProfit = (TextViewPlus)
                        convertView.findViewById(R.id.tv__ordersexpandedList_sum_profit);
                _tvOrdersexpandedListSumPrice.setText(String.format("%,d",
                        _cartDistributors.get(listPosition).getCartPrice().getAllPrice()) + " تومان");
                _tvOrdersexpandedListSumProfit.setText(String.format("%,d",
                        _cartDistributors.get(listPosition).getCartPrice().getYourProfit()) + " تومان");

            }else {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_order_distributor_item, null);

                TextViewPlus expandedListRowNumberTextView = (TextViewPlus)
                        convertView.findViewById(R.id.tv_ordersexpandedList_row_number);
                expandedListRowNumberTextView.setText((expandedListPosition+1)+"");

                TextViewPlus expandedListTextView = (TextViewPlus) convertView
                        .findViewById(R.id.tv_ordersexpandedList_ItemTitle);
                expandedListTextView.setText(expandedListText);

                TextViewPlus itemStatusTextView = (TextViewPlus) convertView
                        .findViewById(R.id.tv_ordersexpandedList_item_status);
                itemStatusTextView.setText(cartDistributorProduct.getOrderStatusTypeName());

                TextViewPlus itemPriceTextView = (TextViewPlus) convertView
                        .findViewById(R.id.tv_ordersexpandedList_item_price);

                ToggleButtonPlus deliveryConfirmButton = (ToggleButtonPlus) convertView
                        .findViewById(R.id.btn_ordersexpandedList_itemـconfirm_delivery);
//                deliveryConfirmButton.setVisibility(
//                        OrderStatusController.getInstance().canBeDelivered( cartDistributorProduct.getOrderStatusTypeId() )? View.VISIBLE : View.INVISIBLE );
//                deliveryConfirmButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CartAPIs.updatePackageDeliverStatus(_listener, Authenticator.loadAuthenticationToken(),
//                                cartDistributorProduct.getId(),
//                                OrderStatusController.getInstance().getDeliveredState(),1);
//                        //v.setVisibility(View.INVISIBLE);
//                    }
//                });
                itemPriceTextView.setText(String.format("%,d",
                        cartDistributorProduct.getCartPrice().getAllPrice()/
                                cartDistributorProduct.getCartPrice().getCount()) +" تومان");

                TextViewPlus itemCountTextView = (TextViewPlus) convertView
                        .findViewById(R.id.tv_ordersexpandedList_item_count);
                itemCountTextView.setText(cartDistributorProduct.getCartPrice().getCount() + " عدد");



                TextViewPlus totalPriceTextView = (TextViewPlus) convertView
                        .findViewById(R.id.tv_ordersexpandedList_item_total_price);
                totalPriceTextView.setText(String.format("%,d",
                        cartDistributorProduct.getCartPrice().getAllPrice()) + " تومان");
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return _cartDistributors.get(listPosition).getPackageSet().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return _cartDistributors.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return _cartDistributors.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return _cartDistributors.get(listPosition).getId();
    }

    @Override
    public View getGroupView(final int listPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        String listTitle = ((CartDistributor) getGroup(listPosition)).getDistributorName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_order_distributor_group, null);
        }

        final LinearLayout llExpadableListViewGroupContainer =
                (LinearLayout) convertView.findViewById(R.id.ordersexpanded_group_container);
        if(!isExpanded) {
            ViewGroup.LayoutParams params = llExpadableListViewGroupContainer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llExpadableListViewGroupContainer.setLayoutParams(params);
            llExpadableListViewGroupContainer.setVisibility(View.VISIBLE);
        }
        else {
            ViewGroup.LayoutParams params = llExpadableListViewGroupContainer.getLayoutParams();
            params.height = 0;
            llExpadableListViewGroupContainer.setLayoutParams(params);
            llExpadableListViewGroupContainer.setVisibility(View.VISIBLE);
        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.tv_ordersexpandedList_GroupTitle);

        listTitleTextView.setText(listTitle);
        final ButtonPlus moreButton = (ButtonPlus) convertView.findViewById(R.id.list_group_more_details);
        TextViewPlus orderSerialTV = (TextViewPlus) convertView
                .findViewById(R.id.tv_ordersexpandedList_GroupNumber);
        orderSerialTV.setText( ((CartDistributor) getGroup(listPosition)).getSerial() );

        TextViewPlus orderPriceTV = (TextViewPlus) convertView
                .findViewById(R.id.tv_ordersexpandedList_GroupCost);

        Long price = ((CartDistributor) getGroup(listPosition)).getCartPrice().getAllPrice();
        String allPrice = String.format("%,d", price);
        orderPriceTV.setText( allPrice + " تومان" );

        TextViewPlus orderStatusTV = (TextViewPlus) convertView
                .findViewById(R.id.tv_ordersexpandedList_GroupStatus);
        orderStatusTV.setText( ((CartDistributor) getGroup(listPosition)).getOrderStatus().get(0).getSecond());

        View divider = convertView.findViewById(R.id.list_group_divider);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isExpanded) {
                    ((ExpandableListView) parent).expandGroup(listPosition);
                    llExpadableListViewGroupContainer.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setServiceListener(ActivityServiceListener listener){
        _listener = listener;
    }

}

