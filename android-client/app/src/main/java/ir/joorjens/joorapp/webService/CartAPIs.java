package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.activities.BaseActivity;
import ir.joorjens.joorapp.models.Cart;
import ir.joorjens.joorapp.models.CartDistributorProduct;
import ir.joorjens.joorapp.models.CartStatusUpdateResponse;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.FavoriteItem;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.EnumHelper;
import ir.joorjens.joorapp.webService.params.AddCartParams;
import ir.joorjens.joorapp.webService.params.ChangePasswordParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meysammoh on 15.03.18.
 */

public class CartAPIs {
    public static String SerialParamName = "serial";
    public static String TimeFromParamName = "timefrom";
    public static String TimeToParamName = "timeto";
    public static String IsFinishedParamName = "finished";
    enum FavoriteMode{
        Fav_Count,
        Fav_Cost
    }
    public static void addCart(
            final ActivityServiceListener listener,
            String authToken,
            AddCartParams params,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<Cart> call = api.addCart(authToken, params);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.addCart, response.body(), reqCode);
                        CacheContainer.get().updateCart(response.body());
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.addCart, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.addCart, t, reqCode);
            }
        });
    }

    public static void removeCart(
            final ActivityServiceListener listener,
            String authToken,
            AddCartParams params,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<Cart> call = api.removeCart(authToken, params);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.removeCart, response.body(), reqCode);
                        CacheContainer.get().updateCart(response.body());
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.removeCart, sresp, reqCode);
                        CacheContainer.get().updateCartEmpty();
                    }
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.removeCart, t, reqCode);
            }
        });
    }

    public static void getCart(final ActivityServiceListener listener,
                               String cookie,
                               final int requestCode){
        APIInterface api = APICreator.GetAPI();
        final Call<Cart> call = api.getCart(cookie, new HashMap<String, String>());
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getCart, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getCart, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getCart, t, requestCode);
            }
        });
    }

    public static void searchCart(final ActivityServiceListener listener,
                               String cookie, HashMap<String, String> params,
                               final int requestCode){
        APIInterface api = APICreator.GetAPI();
        //HashMap<String, String> params = new HashMap<>();
        //if(!serial.isEmpty())
            //params.put(, serial);
        final Call<ResultList<Cart>> call = api.searchCart(cookie, params);
        call.enqueue(new Callback<ResultList<Cart>>() {
            @Override
            public void onResponse(Call<ResultList<Cart>> call, Response<ResultList<Cart>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchCart, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchCart, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Cart>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchCart, t, requestCode);
            }
        });
    }
    public static void finalizeCart(final ActivityServiceListener listener,
                               String cookie,
                               final int requestCode){
        APIInterface api = APICreator.GetAPI();
        final Call<Cart> call = api.finalizeCart(cookie, new HashMap<String, String>());
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.finalizeCart, response.body(), requestCode);
                        // because after finalizing get_cart returns null and cart can find wirh searchCart
                        CacheContainer.get().updateCartEmpty();
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.finalizeCart, sresp, requestCode);
                        CacheContainer.get().updateCartEmpty();
                    }
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.finalizeCart, t, requestCode);
            }
        });
    }

    public static void updatePackageDeliverStatus(final ActivityServiceListener listener,
                                    String cookie, long cartEntityId, long newStatusId,
                                    final int requestCode){
        APIInterface api = APICreator.GetAPI();
        final Call<CartStatusUpdateResponse> call = api.updateCartEntityStatus(cookie, cartEntityId+"", newStatusId+"");
        call.enqueue(new Callback<CartStatusUpdateResponse>() {
            @Override
            public void onResponse(Call<CartStatusUpdateResponse> call, Response<CartStatusUpdateResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.updateCartEntity, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.updateCartEntity, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<CartStatusUpdateResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.updateCartEntity, t, requestCode);
            }
        });
    }

    private static void getFavorites(final ActivityServiceListener listener,
                                                 String cookie,
                                                    FavoriteMode favMode,
                                                 final int requestCode){
        Map<String, String> options = new HashMap<>();
        if(favMode == FavoriteMode.Fav_Count){
            options.put("orderTypeId", "142");
            options.put("asc", "false");
        }
        else if(favMode == FavoriteMode.Fav_Cost){
            //orderTypeId=141&asc=false
            options.put("orderTypeId", "141");
            options.put("asc", "false");
        }


        APIInterface api = APICreator.GetAPI();
        Call<List<FavoriteItem>> call = api.getFavorites(cookie, options);

        call.enqueue(new Callback<List<FavoriteItem>>() {
            @Override
            public void onResponse(Call<List<FavoriteItem>> call, Response<List<FavoriteItem>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getFavorites, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getFavorites, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getFavorites, t, requestCode);
            }
        });
    }

    public static void getFavoritesByCost(final ActivityServiceListener listener,
                                     String cookie,
                                     final int requestCode){
        getFavorites( listener,cookie,FavoriteMode.Fav_Cost,requestCode);
    }

    public static void getFavoritesByCount(final ActivityServiceListener listener,
                                          String cookie,
                                          final int requestCode){
        getFavorites( listener,cookie,FavoriteMode.Fav_Count,requestCode);
    }

}
