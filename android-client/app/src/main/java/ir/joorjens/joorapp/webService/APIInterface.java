package ir.joorjens.joorapp.webService;

import android.support.annotation.InterpolatorRes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.models.*;
import ir.joorjens.joorapp.webService.CachingAnnotation.Cacheable;
import ir.joorjens.joorapp.webService.params.*;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Mohsen on 8/2/2017.
 */

public interface APIInterface {
    @Headers({"Content-Type: application/json"})

    // ------------------------------------ Banner APIs --------------------------------------------
    @GET("banner/search/")
    public Call<ResultList<Banner>> searchBanner(@Header("Cookie") String authToken, @QueryMap Map<String, String> options);

    // --------------------------------------- User APIs -------------------------------------------
    @POST("customer/login/")
    public Call<ServiceResponse> login(@Body LoginParams params);

    @POST("customer/signup/")
    public Call<ServiceResponse> signUp(@Body SignUpParams params);

    @POST("customer/activate/")
    public Call<Profile> activate(@Body ActivationParams params);

    @POST("customer/resendActivationCode/")
    public Call<ServiceResponse> resendActivationCode(@Body HashMap<String, String> mobileNumber);

    @GET("customer/profile/")
    public Call<Profile> getProfile(@Header("Cookie") String authToken);

    @POST("customer/update/")
    public Call<Profile> updateProfile(@Header("Cookie") String authToken,
                                       @Body Profile profile);

    @POST("customer/forgetPassword/")
    public Call<ServiceResponse> forgetPassword(@Body HashMap<String, String> mobileNumber);

    @POST("customer/forgetPasswordVerify/")
    public Call<ServiceResponse> forgetPasswordVerify(@Body ForgetPasswordVerifyParams params);

    @POST("customer/logout/")
    public Call<ServiceResponse> logout(@Header("Cookie") String authToken);

    @POST("customer/resetPassword/")
    public Call<ServiceResponse> changePassword(@Header("Cookie") String authToken,
                                                @Body ChangePasswordParams params);


    // --------------------------------------- Register APIs -------------------------------------------
    @GET("area/search/")
    public Call<ResultList<AreaResult>> searchArea(@Header("Cookie") String authToken,
                                                   @Query("adType") int adType,
                                                   @Query("parentId") int parentId);
    @GET("configEnum/hierarchical/")
    public Call<Map<Integer, List<EnumValues>>> getEnumValues(@Header("Cookie") String authToken,
                                             @Query("typeIds") String typeIds);


    // --------------------------------------- Store APIs -------------------------------------------
    @POST("store/insert/")
    public Call<ServiceResponse> insertStore(@Header("Cookie") String authToken,
                                             @Body Store store);
    @GET("store/search/")
    public Call<ResultList<Store>> viewStore(@Header("Cookie") String authToken,
                                 @Query("managerId") int storeManagerId);


    // --------------------------------------- Product APIs -------------------------------------------
    @GET("product/search/")
    public Call<ResultList<Product>> searchProduct(@Header("Cookie") String authToken,
                                                   @QueryMap Map<String, String> options);
    @GET("product/view/")
    public Call<Product> viewProduct(@Header("Cookie") String authToken,
                                     @Query("id") String productId);


    // --------------------------------------- Category APIs -------------------------------------------
    @GET("productCategoryType/pair/")
    public Call<List<PairResultItem>> pairCategories(@Header("Cookie") String authToken,
                                                           @Query("parentId") long parentId);

    @GET("productCategoryType/search/")
    public Call<ResultList<ProductCategory>> searchCategory(@Header("Cookie") String authToken,
                                                   @Query("parentId") int parentId,
                                                            @Query("offset") int offset);
    @GET("productCategoryType/hierarchical/")
    public Call<List<KeyValueChildItem>> getHierarchicalCategories(@Header("Cookie") String authToken);


    // ------------------------ Distributor Product and Package APIs -------------------------------
    @Cacheable("sdpr")
    @GET("distributorProduct/search/")
    public Call<ResultList<DistributorProduct>> searchDistributorProduct(@Header("Cookie") String authToken,
                                                @QueryMap Map<String, String> options);
    @GET("distributorProduct/view/")
    public Call<DistributorProduct> viewDistributorProduct(@Header("Cookie") String authToken,
                                                           @QueryMap Map<String, String> options);

    @GET("distributorPackage/search/")
    public Call<ResultList<DistributorPackage>> searchDistributorPackage(@Header("Cookie") String authToken,
                                                                         @QueryMap Map<String, String> options);
    @GET("distributorPackage/view/")
    public Call<DistributorPackage> viewDistributorPackage(@Header("Cookie") String authToken,
                                                                         @QueryMap Map<String, String> options);

    @GET("distributorPackage/productCountIn/")
    public Call<ServiceResponse> countProductInDistributorPackage(@Header("Cookie") String authToken,
                                                                  @QueryMap Map<String, String> options);

    // ------------------------ Cart APIs -------------------------------
    @POST("cart/add/")
    public Call<Cart> addCart(@Header("Cookie") String authToken,
                                             @Body AddCartParams params);

    @GET("cart/get/")
    public Call<Cart> getCartOffline(@Header("Cookie") String authToken,
                                                           @QueryMap Map<String, String> options);

    @GET("cart/get/")
    public Call<Cart> getCart(@Header("Cookie") String authToken,
                              @QueryMap Map<String, String> options);

    @GET("cart/search/")
    public Call<ResultList<Cart>> searchCart(@Header("Cookie") String authToken,
                              @QueryMap Map<String, String> options);

    @POST("cart/finalize/")
    public Call<Cart> finalizeCart(@Header("Cookie")  String cookie, @QueryMap HashMap<String, String> options);

    @HTTP(method = "DELETE", path = "cart/remove/", hasBody = true)
    public Call<Cart> removeCart(@Header("Cookie")  String cookie,
                                 @Body AddCartParams params);

    @POST("cart/update/{statusIds}/{entityId}/")
    public Call<CartStatusUpdateResponse> updateCartEntityStatus(@Header("Cookie") String authToken,
                                                 @Path(value = "entityId", encoded = true) String entityId,
                                                        @Path(value = "statusIds", encoded = true) String statusIds);

    @GET("cart/favorite/")
    public Call<List<FavoriteItem>> getFavorites(@Header("Cookie") String authToken,
                                                                         @QueryMap Map<String, String> options);
    //-------------------------- Promotion APIs -------------------------
    @GET("promotion/message/{buyingAmount}/")
    public Call<ServiceResponse> searchPromotion(@Header("Cookie") String authToken,
                                                 @Path(value = "buyingAmount", encoded = true) String buyingAmount);

    //-------------------------- Brand APIs -------------------------
    @GET("productBrandType/pair/")
    public Call<List<PairResultItem>> pairBrands(@Header("Cookie") String cookie,
                                                 @Query("firstlevel") boolean firstLevel);
    //-------------------------- Distributor APIs -------------------------
    @GET("distributor/pair/")
    public Call<List<PairResultItem>> pairDistributors(@Header("Cookie") String cookie);
    @GET("distributor/search/")
    public Call<ResultList<Distributor>> searchDistributors(@Header("Cookie") String cookie,
                                                            @QueryMap Map<String, String> options);
    @GET("distributorDiscontentType/pair/")
    public Call<List<PairResultItem>> getDistributorDiscontentTypes(@Header("Cookie") String cookie);

    @POST("distributorRateDiscontent/upsert/")
    public Call<ServiceResponse> distributorRateDiscontent(@Header("Cookie") String authToken,
                                               @Body DistributorRateParams params);

    //-------------------------- Transaction APIs -------------------------
    @GET("transaction/search/")
    public Call<ResultList<Transaction>> searchTransactions(@Header("Cookie") String cookie,
                                                @QueryMap Map<String, String> options);

    //-------------------------- Order Status APIs ------------------------
    @GET("orderStatusType/pair/")
    public Call<List<PairResultItem>> pairOrderStatusType(@Header("Cookie") String cookie,
                                                 @Query("firstlevel") boolean firstLevel);

    //-------------------------- Deliverer APIs ------------------------
    @GET("cartDistributor/search/")
    public Call<ResultList<CartDistributor>> searchCartDist(@Header("Cookie") String cookie);


    //-------------------------- Dashboard APIs ------------------------
    @GET("dashboard/sale/")
    public Call<ResultList<DashboardSale>> dashboardSale(@Header("Cookie") String cookie,
                                                   @QueryMap Map<String, String> filters);


    //-------------------------- Message APIs ------------------------
    @GET("message/search/")
    public Call<ResultList<Message>> searchMessages(@Header("Cookie") String cookie,
                                                    @QueryMap Map<String, String> filters);

    @POST("message/receiver/seen/")
    public Call<ServiceResponse> seenMessageReceiver(@Header("Cookie") String cookie,
                                                     @Body RequestBody body);

}
