package ir.joorjens.joorapp.webService;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Cache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.CartPrice;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.Message;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;

/**
 * Created by Mohsen on 5/15/2018.
 */

public class CacheContainer  implements ActivityServiceListener {

    private boolean mInitCompleted = false;

    private List<DistributorProduct> mCheapProducts;
    private int mCheapOffset = 0;
    private boolean mCheapReachedEnd = false;

    private List<Product> mNewProducts;
    private int mNewOffset = 0;
    private boolean mNewReachedEnd = false;

    private List<Product> mTopSaleProducts;
    private int mTopSaleOffset = 0;
    private boolean mTopSaleReachedEnd = false;

    private List<DistributorPackage> mDiscountPackages;
    private int mDiscountOffset = 0;
    private boolean mDiscountReachedEnd = false;

    private List<DistributorProduct> mAllProducts;
    private int mAllOffset = 0;
    private boolean mAllReachedEnd = false;

    // adds new items and says data reached to end or not
    public boolean addCheapProducts(ResultList<DistributorProduct> cheaps){
        mCheapProducts.addAll(cheaps.getResult());

        mCheapReachedEnd = true;
        if((mCheapOffset +1)* APICreator.maxResult < cheaps.getTotal()) {
            mCheapOffset++;
            mCheapReachedEnd = false;
        }
        return mCheapReachedEnd;
    }
    public boolean addNewProducts(ResultList<Product> news){
        mNewProducts.addAll(news.getResult());

        mNewReachedEnd = true;
        if((mNewOffset +1)* APICreator.maxResult < news.getTotal()) {
            mNewOffset++;
            mNewReachedEnd = false;
        }
        return mNewReachedEnd;
    }
    public boolean addTopSaleProducts(ResultList<Product> topSales){
        mTopSaleProducts.addAll(topSales.getResult());

        mTopSaleReachedEnd = true;
        if((mTopSaleOffset +1)* APICreator.maxResult < topSales.getTotal()) {
            mTopSaleOffset++;
            mTopSaleReachedEnd = false;
        }
        return mTopSaleReachedEnd;
    }
    public boolean addDiscountPackages(ResultList<DistributorPackage> packages){
        mDiscountPackages.addAll(packages.getResult());

        mDiscountReachedEnd = true;
        if((mDiscountOffset +1)* APICreator.maxResult < packages.getTotal()) {
            mDiscountOffset++;
            mDiscountReachedEnd = false;
        }
        return mDiscountReachedEnd;
    }
    public boolean addAllProducts(ResultList<DistributorProduct> alls){
        mAllProducts.addAll(alls.getResult());

        mAllReachedEnd = true;
        if((mAllOffset +1)* APICreator.maxResult < alls.getTotal()) {
            mAllOffset++;
            mAllReachedEnd = false;
        }
        return mAllReachedEnd;
    }

    public boolean isCheapReachedEnd() {
        return mCheapReachedEnd;
    }

    public boolean isNewReachedEnd() {
        return mNewReachedEnd;
    }

    public boolean isTopSaleReachedEnd() {
        return mTopSaleReachedEnd;
    }

    public boolean isDiscountReachedEnd() {
        return mDiscountReachedEnd;
    }

    public boolean isAllReachedEnd() {
        return mAllReachedEnd;
    }

    public int getCheapOffset() {
        return mCheapOffset;
    }

    public int getNewOffset() {
        return mNewOffset;
    }

    public int getTopSaleOffset() {
        return mTopSaleOffset;
    }

    public int getDiscountOffset() {
        return mDiscountOffset;
    }

    public int getAllOffset() {
        return mAllOffset;
    }

    public List<DistributorProduct> getCheapProducts() {
        return mCheapProducts;
    }

    public List<Product> getNewProducts() {
        return mNewProducts;
    }

    public List<Product> getTopSaleProducts() {
        return mTopSaleProducts;
    }

    public List<DistributorPackage> getDiscountPackages() {
        return mDiscountPackages;
    }

    public List<DistributorProduct> getAllProducts() {
        return mAllProducts;
    }


    private static CacheContainer mInstance = null;
    public static CacheContainer get(){
        if(mInstance == null){
            mInstance = new CacheContainer();
        }
        return mInstance;
    }

    private CacheContainer(){

        mCheapProducts = new ArrayList<>();
        mTopSaleProducts = new ArrayList<>();
        mNewProducts = new ArrayList<>();
        mDiscountPackages = new ArrayList<>();
        mAllProducts = new ArrayList<>();

        mCartEventsListeners = new HashSet<>();
        mInitListeners = new HashSet<>();
        mCategoriesUpdateHandler = new Handler();
        mDistributorsUpdateHandler = new Handler();
        mBrandsUpdateHandler = new Handler();
        mMessagesUpdateHandler = new Handler();

        mCategoriesUpdateChecker = new Runnable() {
            @Override
            public void run() {
                try{
                    CategoryAPIs.getHierarchicalCategories(
                            CacheContainer.this, Authenticator.loadAuthenticationToken(), 1);
                }finally {
                    mCategoriesUpdateHandler.postDelayed(mCategoriesUpdateChecker, mCategoriesUpdatePeriodMills);
                }
            }
        };

        mDistributorsUpdateChecker = new Runnable() {
            @Override
            public void run() {
                try{
                    DistributorsAPIs.getPairDistributors(
                            CacheContainer.this, Authenticator.loadAuthenticationToken(),1);
                }finally {
                    mDistributorsUpdateHandler.postDelayed(mDistributorsUpdateChecker, mDistributorsUpdatePeriodMills);
                }
            }
        };

        mBrandsUpdateChecker = new Runnable() {
            @Override
            public void run() {
                try{
                    BrandAPIs.getPairBrands(
                            CacheContainer.this, Authenticator.loadAuthenticationToken(),1);
                }finally {
                    mBrandsUpdateHandler.postDelayed(mBrandsUpdateChecker, mBrandsUpdatePeriodMills);
                }
            }
        };

        mMessagesUpdateChecker = new Runnable() {
            @Override
            public void run() {
                try{
                    MessageAPIs.getMyMessages(CacheContainer.this,Authenticator.loadAuthenticationToken(),
                            0, 50, 1 );
                }finally {
                    mMessagesUpdateHandler.postDelayed(mMessagesUpdateChecker, mMessagesUpdatePeriodMills);
                }
            }
        };
    }

    private void checkInitCompleted(){
        if(!mInitCompleted && mCartUpdated && mBrandUpdated && /*mMessagesUpdated &&*/
                mDistributorDiscontentUpdated &&
                mCategoryUpdated && mDistributorUpdated){
            mInitCompleted = true;
            for(InitializeListener listener : mInitListeners){
                listener.onInitCompleted();
            }
        }
    }

    @Override
    public void onServiceSuccess(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.getCart){
            mFreshCart = (Cart)data;
            updateCart(mFreshCart);
            mCartUpdated = true;
        }
        if(apiCode == APICode.getHierarchicalCategories){
            mFreshCategories = (List<KeyValueChildItem>)data;
            mCategoryUpdated = true;
        }
        else if(apiCode == APICode.pairDistributors){
            mFreshDistributors = (List<PairResultItem>)data;
            mDistributorUpdated = true;
        }
        else if(apiCode == APICode.pairBrands){
            mFreshBrands = (List<PairResultItem>)data;
            mBrandUpdated = true;
        }
        else if(apiCode == APICode.searchMessages){
            mFreshMessages = ((ResultList<Message>)data).getResult();
            mMessagesUpdated = true;
        }
        else if(apiCode == APICode.getDistributorDiscontentTypes){
            mFreshDistributorDiscontents = (List<PairResultItem>) data;
            mDistributorDiscontentUpdated = true;
        }

        checkInitCompleted();
    }

    @Override
    public void onServiceFail(APICode apiCode, Object data, int requestCode) {
        if(apiCode == APICode.getCart){
            mCartUpdated = true;
            updateCartEmpty();
        }
        checkInitCompleted();
        //TODO retry?
    }

    @Override
    public void onNetworkFail(APICode apiCode, Object data, int requestCode) {
        //TODO retry?
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public void startUpdating(){
        mCategoriesUpdateChecker.run();
        mDistributorsUpdateChecker.run();
        mBrandsUpdateChecker.run();
        mMessagesUpdateChecker.run();
        CartAPIs.getCart(CacheContainer.this, Authenticator.loadAuthenticationToken(), 1);
        DistributorsAPIs.getAllDistributorDiscontents(this, Authenticator.loadAuthenticationToken(), 1);
    }
    public void stopUpdating(){
        mCategoriesUpdateHandler.removeCallbacks(mCategoriesUpdateChecker);
        mDistributorsUpdateHandler.removeCallbacks(mDistributorsUpdateChecker);
        mBrandsUpdateHandler.removeCallbacks(mBrandsUpdateChecker);
    }

    public interface CartEventsListener{
        public void onCartDataChanged(Cart updatedCart);
    }
    public interface InitializeListener{
        public void onInitCompleted();
    }

    private int mCategoriesUpdatePeriodMills = 60 * 30 * 1000; // 30 min
    private int mDistributorsUpdatePeriodMills = 60 * 30 * 1000; // 30 min
    private int mBrandsUpdatePeriodMills = 60 * 30 * 1000; // 30 min
    private int mMessagesUpdatePeriodMills = 60 * 5 * 1000; // 5 min

    private boolean mCartUpdated = false;
    private boolean mCategoryUpdated = false;
    private boolean mDistributorUpdated = false;
    private boolean mBrandUpdated = false;
    private boolean mMessagesUpdated = false;
    private boolean mDistributorDiscontentUpdated = false;


    private Handler mCategoriesUpdateHandler;
    private Handler mDistributorsUpdateHandler;
    private Handler mBrandsUpdateHandler;
    private Handler mMessagesUpdateHandler;
    private Runnable mCategoriesUpdateChecker;
    private Runnable mDistributorsUpdateChecker;
    private Runnable mBrandsUpdateChecker;
    private Runnable mMessagesUpdateChecker;

    private Set<CartEventsListener> mCartEventsListeners;
    private Set<InitializeListener> mInitListeners;

    private Cart mFreshCart;
    private List<KeyValueChildItem> mFreshCategories;
    private List<PairResultItem> mFreshDistributors;
    private List<PairResultItem> mFreshBrands;
    private List<Message> mFreshMessages;
    private List<PairResultItem> mFreshDistributorDiscontents;

    public void addCartListener(CartEventsListener listener){
        mCartEventsListeners.add(listener);
    }
    public void removeCartListener(CartEventsListener listener){
        mCartEventsListeners.remove(listener);
    }
    public void addInitListener(InitializeListener listener){
        mInitListeners.add(listener);
    }

    public void updateCart(Cart cart){
        mFreshCart = cart;
        for(CartEventsListener listener : mCartEventsListeners){
            listener.onCartDataChanged(mFreshCart);
        }
    }
    public void updateCartEmpty(){
        mFreshCart = null;
        for(CartEventsListener listener : mCartEventsListeners){
            listener.onCartDataChanged(mFreshCart);
        }
    }

    public Cart getCart(){
        return mFreshCart;
    }

    public List<KeyValueChildItem> getCategories(){
        return mFreshCategories;
    }

    public List<PairResultItem> getDistributors(){
        return mFreshDistributors;
    }

    public List<PairResultItem> getBrands(){
        return mFreshBrands;
    }

    public List<Message> getMessages(){
        return mFreshMessages;
    }

    public List<PairResultItem> getDistributorDiscontents(){
        return mFreshDistributorDiscontents;
    }

    public int getUnreadMessagesCount(){
        int count = 0;
        if(mFreshMessages == null){
            return 0;
        }
        for(Message msg : mFreshMessages){
            if(msg.getMessageReceivers().get(0).getSeen() == false){
                count++;
            }
        }

        return count;
    }

}
