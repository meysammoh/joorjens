package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.OrderStatusController;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by mohsen on 04.03.18.
 */

public class DeliveryOrderProductRow extends TableRow {
    private TextViewPlus _orderProductTitle;
    private TextViewPlus _orderRowNumber;
    private TextViewPlus _etOrderCount;
    private TextViewPlus _tvPriceOne;
    private TextViewPlus _tvPriceTotal;
    private CartDistributorProduct _cartDistributorProduct;
    private ActivityServiceListener _listener;
    private Context mContext;

    public DeliveryOrderProductRow(Context context, ActivityServiceListener listener) {
        super(context);
        mContext = context;
        _listener = listener;
        init(null);
    }

    public DeliveryOrderProductRow(Context context, AttributeSet attrs, ActivityServiceListener listener) {
        super(context, attrs);
        mContext = context;
        _listener = listener;
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_delivery_order_product_row, null);
        _orderProductTitle = (TextViewPlus) view.findViewById(R.id.del_order_tv_product_name);
        _orderRowNumber = (TextViewPlus) view.findViewById(R.id.del_order_tv_row_number);
        _etOrderCount = (TextViewPlus) view.findViewById(R.id.del_order_tv_count);
        _tvPriceOne = (TextViewPlus) view.findViewById(R.id.del_order_tv_price_one);
        _tvPriceTotal = (TextViewPlus) view.findViewById(R.id.del_order_tv_price_total);

        addView(view);
    }

    public void setRowValues(CartDistributorProduct cartDistributorProduct, Integer rowNumber){
        _cartDistributorProduct = cartDistributorProduct;

        if(cartDistributorProduct.getDistributorProductId() != 0) {
            _orderProductTitle.setText(cartDistributorProduct.getDistributorProductName());
        }
        else if(cartDistributorProduct.getDistributorPackageId() != 0)
            _orderProductTitle.setText(cartDistributorProduct.getDistributorProductName());

        _etOrderCount.setText(cartDistributorProduct.getCartPrice().getCount().toString());
        _orderRowNumber.setText(rowNumber.toString());
        _tvPriceOne.setText(String.format("%,d",cartDistributorProduct.getCartPrice().getPackPrice()) + " تومان");

        //_tvPackingType.setText();
        _tvPriceTotal.setText(String.format("%,d",cartDistributorProduct.getCartPrice().getAllPriceDiscount()) + " تومان");

    }

    void setColor(int colorId) {


    }

}
