package ir.joorjens.joorapp.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import ir.joorjens.joorapp.R;
import ir.joorjens.joorapp.adapters.ActivityKeys;
import ir.joorjens.joorapp.customViews.ButtonPlus;
import ir.joorjens.joorapp.customViews.FavoriteRow;
import ir.joorjens.joorapp.customViews.TextViewPlus;
import ir.joorjens.joorapp.models.FavoriteItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import ir.joorjens.joorapp.webService.APICode;
import ir.joorjens.joorapp.webService.CartAPIs;

public class ShowFavoritesActivity extends HomeBaseActivity {

    private int _currentTabBtnId;
    private ButtonPlus mBtnCountTab, mBtnCostTab;
    private LinearLayout mLLCountTab, mLLCostTab;
    // ------------------------------ isActive ---------------------------
    private boolean _isActive;
    @Override
    protected void onResume() {
        super.onResume();
        _isActive = true;

        CartAPIs.getFavoritesByCost(this, Authenticator.loadAuthenticationToken(), 1);
    }
    @Override
    protected void onStop() {
        _isActive = false;
        super.onStop();
    }
    @Override
    public boolean isActive() {
        return _isActive;
    }
    // ------------------------------ isActive ---------------------------
    TextViewPlus mMessageTextView;
    LinearLayout favoriteContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_show_favorites, ActivityKeys.ShowFavorites);

        favoriteContainer = (LinearLayout) findViewById(R.id.favorites_ll_items);
        ViewCompat.setNestedScrollingEnabled(favoriteContainer, false);

        mMessageTextView = (TextViewPlus) findViewById( R.id.tv_shown_favorite_result_message );

        _currentTabBtnId = R.id.fav_btn_tab_cost;
        if (savedInstanceState == null) {

        }

        mBtnCountTab = (ButtonPlus) findViewById(R.id.fav_btn_tab_count);
        mBtnCostTab = (ButtonPlus) findViewById(R.id.fav_btn_tab_cost);
        mBtnCountTab.setOnClickListener(changeTab);
        mBtnCostTab.setOnClickListener(changeTab);
        mBtnCountTab.setTextColor(getResources().getColor(R.color.color_black90));
        mBtnCostTab.setTextColor(getResources().getColor(R.color.color_first));

        mLLCountTab = (LinearLayout) findViewById(R.id.fav_ll_tab_count);
        mLLCostTab = (LinearLayout) findViewById(R.id.fav_ll_tab_cost);
        mLLCountTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
        mLLCostTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));

    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        // TODO handle more than 50 items

        if (apiCode == APICode.getFavorites){
            List<FavoriteItem> favs = (List<FavoriteItem>) data;
            favoriteContainer.removeAllViews();
            if(requestCode == 1) {
                for (int i = 0; i < favs.size(); i++) {
                    FavoriteItem fav = favs.get(i);
                    FavoriteRow favRow = new FavoriteRow(this, this, false);
                    favRow.setRowValues(fav);
                    favoriteContainer.addView(favRow);
                }
            }
            else if(requestCode == 2) {
                for (int i = 0; i < favs.size(); i++) {
                    FavoriteItem fav = favs.get(i);
                    FavoriteRow favRow = new FavoriteRow(this, this, true);
                    favRow.setRowValues(fav);
                    favoriteContainer.addView(favRow);
                }
            }
        }
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode != APICode.getCart )
        StaticHelperFunctions.showMessage(this, ((ServiceResponse)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        StaticHelperFunctions.showMessage(this, ((Throwable)data).getMessage(), StaticHelperFunctions.MessageType.Alert);
    }

    private View.OnClickListener changeTab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if( v.getId() != _currentTabBtnId) {
                if (v.getId() == R.id.fav_btn_tab_cost) {
                    mLLCountTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mLLCostTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mBtnCountTab.setTextColor(getResources().getColor(R.color.color_black90));
                    mBtnCostTab.setTextColor(getResources().getColor(R.color.color_first));
                    CartAPIs.getFavoritesByCost(ShowFavoritesActivity.this, Authenticator.loadAuthenticationToken(), 1);

                } else if (v.getId() == R.id.fav_btn_tab_count) {
                    mLLCostTab.setBackground(getResources().getDrawable(R.drawable.unselected_tab_bg));
                    mLLCountTab.setBackground(getResources().getDrawable(R.drawable.selected_tab_bg));
                    mBtnCostTab.setTextColor(getResources().getColor(R.color.color_black90));
                    mBtnCountTab.setTextColor(getResources().getColor(R.color.color_first));

                    CartAPIs.getFavoritesByCount(ShowFavoritesActivity.this, Authenticator.loadAuthenticationToken(), 2);
                }
                _currentTabBtnId = v.getId();
            }
        }
    };

}
