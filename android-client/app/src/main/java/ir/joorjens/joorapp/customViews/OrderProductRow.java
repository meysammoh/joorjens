package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.ViewPastCartsActivity;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.OrderStatusController;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by Meysam on 04.03.18.
 */

public class OrderProductRow extends TableRow {
    private TextViewPlus _orderProductTitle;
    private EditTextPlus _etOrderCount;
    private TextViewPlus _tvOrderCountAm;
    private TextViewPlus _orderRowNumber;
    private TextViewPlus _orderTvDelete;
    private ImageViewPlus _orderImgDelete;
    private TextViewPlus _tvPriceOne;
    private TextViewPlus _tvPackingType;
    private ButtonPlus _btnEditSubmit;
    private TextViewPlus _tvPriceTotal;
    private CartDistributorProduct _cartDistributorProduct;
    private ActivityServiceListener _listener;
    private ToggleButtonPlus _orderBtnConfirmDelivery;
    private boolean _isEditMode = false;
    private Context mContext;

    public OrderProductRow(Context context, ActivityServiceListener listener, boolean isEditMode) {
        super(context);
        mContext = context;
        _listener = listener;
        init(null, isEditMode);
    }

    public OrderProductRow(Context context, AttributeSet attrs, ActivityServiceListener listener, boolean isEditMode) {
        super(context, attrs);
        mContext = context;
        _listener = listener;
        init(attrs, isEditMode);
    }
    private void init(AttributeSet attrs, boolean editMode) {
        _isEditMode = editMode;
        View view = inflate(getContext(), R.layout.view_order_product_row, null);
        _orderProductTitle = (TextViewPlus) view.findViewById(R.id.order_tv_product_name);
        _etOrderCount = (EditTextPlus) view.findViewById(R.id.order_et_count);
        _etOrderCount.setEnabled(false);
        _tvOrderCountAm = (TextViewPlus) view.findViewById(R.id.del_order_tv_count_am);
        _tvOrderCountAm.setVisibility(GONE);

        _orderRowNumber = (TextViewPlus) view.findViewById(R.id.order_tv_row_number);

        _btnEditSubmit = (ButtonPlus) view.findViewById(R.id.order_btn_edit_submit);
        _btnEditSubmit.setOnClickListener(editCountListener);

        _orderImgDelete = (ImageViewPlus) view.findViewById(R.id.order_img_delete);
        _orderTvDelete = (TextViewPlus) view.findViewById(R.id.order_tv_delete);
        _tvPriceOne = (TextViewPlus) view.findViewById(R.id.del_order_tv_price_one);
        _tvPriceTotal = (TextViewPlus) view.findViewById(R.id.order_tv_price);
        _orderImgDelete.setOnClickListener( deleteListener );
        _orderTvDelete.setOnClickListener( deleteListener );
        _orderBtnConfirmDelivery = (ToggleButtonPlus) view.findViewById(R.id.order_btn_confirm_delivery);

        if(!editMode){
            _btnEditSubmit.setVisibility(GONE);
            _orderImgDelete.setVisibility(GONE);
            _orderTvDelete.setVisibility(GONE);
            //_etOrderCount.setBackground(null);
            //_etOrderCount.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            _etOrderCount.setVisibility(GONE);
            _tvOrderCountAm.setVisibility(VISIBLE);
        }

        addView(view);
    }
    private OnClickListener deleteListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            StaticHelperFunctions.showYesNoDialog((ViewPastCartsActivity)mContext,
                    "آیا از حذف اطمینان دارید؟", StaticHelperFunctions.MessageType.Warning,YesDeleteListener ,
                    NoDeleteListener, "بله", "خیر");
        }
    };
    SweetAlertDialog.OnSweetClickListener YesDeleteListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            AddCartParams params = new AddCartParams(_cartDistributorProduct.getType(),
                    _cartDistributorProduct.getCartPrice().getCount(), false,
                    _cartDistributorProduct.getDistributorProductId(), _cartDistributorProduct.getDistributorPackageId());
            CartAPIs.removeCart( _listener, Authenticator.loadAuthenticationToken(), params, 0);
            sweetAlertDialog.dismissWithAnimation();
        }
    };
    SweetAlertDialog.OnSweetClickListener NoDeleteListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog.dismissWithAnimation();
        }
    };



    private OnClickListener editCountListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_etOrderCount.isEnabled() == true){
                _etOrderCount.setEnabled(false);
                _btnEditSubmit.setText(getResources().getString(R.string.lbl_edit));

                // call api to change order count
                Integer count = Integer.valueOf(_etOrderCount.getText().toString());
                AddCartParams params = new AddCartParams(_cartDistributorProduct.getType(),
                        count, _cartDistributorProduct.getBuyByCheck(),
                        _cartDistributorProduct.getDistributorProductId(), _cartDistributorProduct.getDistributorPackageId());
                CartAPIs.addCart( _listener, Authenticator.loadAuthenticationToken(), params, 0);
            }
            else{
                _etOrderCount.setEnabled(true);
                _btnEditSubmit.setText(getResources().getString(R.string.lbl_submit));
                _etOrderCount.requestFocus();
                _etOrderCount.selectAll();
                InputMethodManager imm = (InputMethodManager) JJApp.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(_etOrderCount, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };

    public void setRowValues(CartDistributorProduct cartDistributorProduct, Integer rowNumber){
        _cartDistributorProduct = cartDistributorProduct;
        if(cartDistributorProduct.getDistributorProductId() != 0) {
            _orderProductTitle.setText(cartDistributorProduct.getDistributorProductName());
        }
        else if(cartDistributorProduct.getDistributorPackageId() != 0)
            _orderProductTitle.setText(cartDistributorProduct.getDistributorProductName());

        _etOrderCount.setText(cartDistributorProduct.getCartPrice().getCount().toString());
        _tvOrderCountAm.setText(cartDistributorProduct.getCartPrice().getCount().toString());
        _orderRowNumber.setText(rowNumber.toString());
        _tvPriceOne.setText(String.format("%,d",cartDistributorProduct.getCartPrice().getPackPrice()) + " تومان");

        _tvPriceTotal.setText(String.format("%,d",cartDistributorProduct.getCartPrice().getAllPriceDiscount()) + " تومان");

        _orderBtnConfirmDelivery.setVisibility(
                OrderStatusController.getInstance().canBeDelivered( cartDistributorProduct.getOrderStatusTypeId() )? VISIBLE : INVISIBLE );
        _orderBtnConfirmDelivery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CartAPIs.updatePackageDeliverStatus(_listener, Authenticator.loadAuthenticationToken(),
                        _cartDistributorProduct.getId(),
                        OrderStatusController.getInstance().getDeliveredState(),1);
                v.setVisibility(INVISIBLE);

            }
        });

    }

    void setColor(int colorId) {


    }

}
