package ir.joorjens.joorapp.customViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.adapters.MyBaseAdapter;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Packaging;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.DistributorProductAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by Meysam on 04.03.18.
 */

public class ProductDistributorsRow extends TableRow {
    private int mColorId;
    private TextViewPlus mDistTitle;
    private TextViewPlus mOffersCash;
    private TextViewPlus mTvPriceOne;
    private TextViewPlus mOffersCheck;
    private RatingBar mDistRate;
    private TextViewPlus mDeliveryTime;
    private TextViewPlus mMessage;
    private LinearLayout mPriceRangeList;
    private TextViewPlus mComputations;
    private LinearLayout mAddCartButton;
    private TextViewPlus mAddCartPriceButton;
    private TreeMap<Integer, Integer> mPricesRange;
    private RelativeLayout mMainLayout;
    private Vector<Discount> mDiscounts;
    private DistributorProduct mDistributorProduct;
    private int mOrderCount;
    private boolean mBuyWithCheck;
    private Context mContext;
    public ProductDistributorsRow(Context context, int colorId) {
        super(context);
        mContext = context;
        init(colorId);
    }

//    public ProductDistributorsRow(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
    private void init(int colorId) {
        mOrderCount = 0;
        View view = inflate(mContext , R.layout.view_product_distributor_row, null);
        mDistTitle = (TextViewPlus) view.findViewById(R.id.dppr_tv_distributor_name);

        mTvPriceOne = (TextViewPlus) view.findViewById(R.id.dppr_tv_price_one);
        mOffersCash = (TextViewPlus) view.findViewById(R.id.dppr_tv_offers_cash);
        mOffersCheck = (TextViewPlus) view.findViewById(R.id.dppr_tv_offers_check);
        mDistRate = (RatingBar) view.findViewById(R.id.dppr_rb_distributor_rate);
        mDeliveryTime = (TextViewPlus) view.findViewById(R.id.dppr_tv_delivery_time);
        mMessage = (TextViewPlus) view.findViewById(R.id.dppr_tv_message);
        mPriceRangeList = (LinearLayout) view.findViewById(R.id.dppr_packs_container);
        mComputations = (TextViewPlus) view.findViewById(R.id.dppr_tv_computations_result);
        mAddCartButton = (LinearLayout) view.findViewById(R.id.dppr_btn_add_card);
        mAddCartPriceButton = (TextViewPlus)view.findViewById(R.id.dppr_btn_price);
        mMainLayout = (RelativeLayout) view.findViewById(R.id.dppr_main_container);

        mPricesRange = new TreeMap<>();
        setColor(colorId);
        addView(view);
    }
    public void setRowValues(DistributorProduct distributorProduct){
        mDistributorProduct = distributorProduct;
        mDistTitle.setText( mDistributorProduct.getDistributorName());
        mOffersCheck.setVisibility( mDistributorProduct.getSupportCheck() ? VISIBLE : GONE);
        mDistRate.setRating((float) mDistributorProduct.getDistributorRate());
        mDeliveryTime.setText(mDistributorProduct.getMaxDelivery()+"");
        mMessage.setText("");
        mTvPriceOne.setText("قیمت هر عدد " + String.format("%,d",distributorProduct.getPrice())+ " تومان");

        boolean addedOneCount = false;
        DistributorProductPriceRangeRow packageRow;
        if(mColorId == R.color.cheap_color){
            packageRow = new DistributorProductPriceRangeRow(this.getContext());
            packageRow.setRowValues(mDistributorProduct.getPriceMin());
            mPriceRangeList.addView(packageRow);
        }else {
            if (mDistributorProduct.getPackages() != null)
                for (Packaging pack :
                        mDistributorProduct.getPackages()) {
                    packageRow = new DistributorProductPriceRangeRow(this.getContext());

                    packageRow.setRowValues(pack.getCountFrom(), pack.getCountTo(), pack.getPriceDistributorWithDiscount());
                    if (pack.getCountFrom() <= 1)
                        addedOneCount = true;
                    //TODO ask khalil which price we should use
                    mPricesRange.put(pack.getCountFrom() - 1, pack.getPriceDistributorWithDiscount());
                    mPriceRangeList.addView(packageRow);
                }
        }


        mPricesRange.put( -1, distributorProduct.getPrice() );
        mComputations.setText(getResources().getString( R.string.label_no_discount_available));
        mAddCartPriceButton.setText("تعداد را وارد کنید");
        mAddCartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }

    void setColor(int colorId) {
        mColorId = colorId;
        if (mColorId != -1) {
            switch(mColorId){
                case R.color.cheap_color:
                    mAddCartButton.setBackground(getResources().getDrawable(R.drawable.rounded_cheap_color));
                    break;
                case R.color.new_color:
                    mAddCartButton.setBackground(getResources().getDrawable(R.drawable.rounded_new_color));
                    break;
                case R.color.full_sale_color:
                    mAddCartButton.setBackground(getResources().getDrawable(R.drawable.rounded_topsale_color));
                    break;
            }
        }
    }
    void updateComputations( int newOrderNumber, boolean buyWithCheck ){
        mOrderCount = newOrderNumber;
        mBuyWithCheck = buyWithCheck;
        //Discount based on order count

        int onePrice = mPricesRange.get( mPricesRange.lowerKey( newOrderNumber ) );
        int totalPriceWithoutDiscount = onePrice * newOrderNumber;
        int newPrice =  totalPriceWithoutDiscount;
        String discountsSummary = "";
        //other Discounts
        for (Discount discount :
                mDistributorProduct.getDiscounts()) {
            if(discount.getValidTime()) {
                if (discount.getType() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountOffer)) {
                    //int coef = (int) Math.floor( newOrderNumber/ discount.getBuyCount() );
                    //newPrice -= onePrice * discount.getOfferCount()* coef;
                    //mMessage.setText("هر " + discount.getBuyCount() + " = تا" + discount.getOfferCount() + " تا رایگان");
                    //mMessage.setVisibility(VISIBLE);
                } else if (discount.getType() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountCash)) {
                    if(!buyWithCheck){
                        newPrice -= (int) ( totalPriceWithoutDiscount * discount.getPercent() * .01f);
                    }

                } else if (discount.getType() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountSpecial)) {
                    newPrice -= (int) ( totalPriceWithoutDiscount * discount.getPercent() * .01f);
                }
//                if( !discountsSummary.isEmpty())
//                    discountsSummary+="+ ";
//                discountsSummary += discount.getDiscountStr();
            }
        }
        //mComputations.setText(discountsSummary.isEmpty() ? getResources().getString( R.string.label_no_discount_available )
        //    : discountsSummary);

        String sNewPrice = String.format("%,d", newPrice);
        mAddCartPriceButton.setText(sNewPrice + " تومان");
    }

    void showDiscounts( ){
        String discountsSummary = "";
        for (Discount discount : mDistributorProduct.getDiscounts()) {
            if(discount.getValidTime()) {
                if (discount.getType() == EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DiscountOffer)) {
                    mMessage.setText(discount.getDiscountStr());
                    mMessage.setVisibility(VISIBLE);
                }
                else {
                    discountsSummary += discount.getDiscountStr() + " + ";
                }
            }
        }
        if(discountsSummary.endsWith("+ ")){
            discountsSummary = discountsSummary.substring(0, discountsSummary.length()-2);
        }

        mComputations.setText(discountsSummary.isEmpty() ? getResources().getString( R.string.label_no_discount_available )
                : discountsSummary);
    }

    void addToCart(){
        if(mOrderCount <= 0){
            StaticHelperFunctions.showConfirmDialog((Activity) mContext, "لطفاً ابتدا تعداد را وارد نمائید",
                    StaticHelperFunctions.MessageType.Warning, null, "باشه");
        }
        else {
            AddCartParams params = new AddCartParams(EnumHelper.getEnumCode(EnumHelper.AllEnumNames.CartProduct),
                    mOrderCount, mBuyWithCheck, mDistributorProduct.getId(), 0);
            CartAPIs.addCart((ActivityServiceListener) getContext(), Authenticator.loadAuthenticationToken(), params, 0);
        }
    }
}
