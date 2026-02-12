package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.squareup.picasso.Picasso;

import java.util.TreeMap;
import java.util.Vector;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.DistributorActivity;
import ir.joorjens.joorapp.fragments.DistributorDetailsFragment;
import ir.joorjens.joorapp.models.Discount;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.Packaging;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICreator;
import ir.joorjens.joorapp.webService.CartAPIs;
import ir.joorjens.joorapp.webService.params.AddCartParams;

/**
 * Created by Meysam on 04.03.18.
 */

public class DistributorRow extends TableRow {
    private int mColorId;
    private ImageViewPlus distLogo;
    private RatingBar distRate;
    private TextViewPlus distTitle;
    private TextViewPlus tvDistRate;
    private ButtonPlus moreBtn;
private Distributor _distributor;
    public DistributorRow(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_distributor_row, null);
        distLogo = (ImageViewPlus) view.findViewById(R.id.distrow_img_logo);

        distRate = (RatingBar) view.findViewById(R.id.distrow_rb_rate);
        distTitle = (TextViewPlus) view.findViewById(R.id.distrow_tvp_title);
        tvDistRate = (TextViewPlus) view.findViewById(R.id.distrow_tvp_score);
        moreBtn = (ButtonPlus) view.findViewById(R.id.distrow_btn_more_details);

        addView(view);
    }
    public void setRowValues(final Distributor distributor){
        _distributor = distributor;
        StaticHelperFunctions.loadImage(getContext(), distributor.getImageBanner(), distLogo.getImageView());
        distRate.setRating( distributor.getRate());
        distTitle.setText( distributor.getName());
        tvDistRate.setText("(امتیاز: " + distributor.getRate() + ")");
        moreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DistributorDetailsFragment currentFragment = DistributorDetailsFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(DistributorActivity.DistributorIdParam, distributor.getId());
                currentFragment.setArguments( bundle );
                FragmentTransaction ft = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("DistributorDetailsFragment");
                ft.replace(R.id.distlist_fragment_container, currentFragment).commit();
            }
        });
    }

}
