package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;

import com.squareup.picasso.Picasso;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.FavoriteItem;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by Meysam on 04.03.18.
 */

public class FavoriteRow extends TableRow {
    private TextViewPlus _orderFavTitle;
    private TextViewPlus _orderFavAmount;
    private ImageViewPlus _orderFavImg;
    private ButtonPlus _orderFavBtnAgain;
    private ActivityServiceListener _listener;
    private boolean _isCount = false;
    public FavoriteRow(Context context, ActivityServiceListener listener, boolean isCount) {
        super(context);
        _listener = listener;
        init(null);
        _isCount = isCount;
    }

    public FavoriteRow(Context context, AttributeSet attrs, ActivityServiceListener listener) {
        super(context, attrs);
        _listener = listener;
        init(attrs);
    }
    private void init(AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.view_favorite, null);
        _orderFavTitle = (TextViewPlus) view.findViewById(R.id.tv_favorite_item_text1);
        _orderFavAmount = (TextViewPlus) view.findViewById(R.id.tv_favorite_item_amount);

        _orderFavImg = (ImageViewPlus) view.findViewById(R.id.img_favorite_item_image);
        _orderFavBtnAgain = (ButtonPlus) view.findViewById(R.id.btn_favorite_item_order_again);
        _orderFavBtnAgain.setOnClickListener( orderAgainListener );

        addView(view);
    }

    private OnClickListener orderAgainListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    public void setRowValues(FavoriteItem favItem){
        _orderFavTitle.setText( favItem.getProductName() );
        String unit = _isCount ? " عدد" : " تومان";

        _orderFavAmount.setText( favItem.getAmount()+unit);
        StaticHelperFunctions.loadImage(getContext(), favItem.getProductImage(), _orderFavImg.getImageView());

    }

    void setColor(int colorId) {


    }

}
