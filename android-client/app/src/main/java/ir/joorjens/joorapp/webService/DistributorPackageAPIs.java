package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.DistributorPackage;
import ir.joorjens.joorapp.models.DistributorProduct;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.EnumHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 12/15/2017.
 */

public class DistributorPackageAPIs {
    public  static String ProductBarcodeParam = "productbarcode";

    private static void searchDistributorPackage(final ActivityServiceListener listener,
                                                 String cookie,
                                                 @Nullable Map<String, String> options,
                                                 final int requestCode,
                                                 final APICode calleAPI){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<DistributorPackage>> call = api.searchDistributorPackage(cookie, options);

        call.enqueue(new Callback<ResultList<DistributorPackage>>() {
            @Override
            public void onResponse(Call<ResultList<DistributorPackage>> call, Response<ResultList<DistributorPackage>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(calleAPI, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(calleAPI, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<DistributorPackage>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(calleAPI, t, requestCode);
            }
        });
    }

    private static void viewDistributorPackage(final ActivityServiceListener listener,
                                                 String cookie,
                                                 String packageId,
                                                 final int requestCode,
                                                 final APICode calleAPI){
        APIInterface api = APICreator.GetAPI();
        Map<String,String> options = new HashMap<>();
        options.put("id", packageId);
        Call<DistributorPackage> call = api.viewDistributorPackage(cookie, options);

        call.enqueue(new Callback<DistributorPackage>() {
            @Override
            public void onResponse(Call<DistributorPackage> call, Response<DistributorPackage> response) {
                if(!listener.isActive())
                    call.cancel();

                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(calleAPI, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(calleAPI, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistributorPackage> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(calleAPI, t, requestCode);
            }
        });
    }


    public static void getDiscountDPackages(ActivityServiceListener listener,
                                          String cookie,
                                            int offset,
                                          int requestCode){
        Map<String, String> options = new HashMap();
        options.put("bundlingOrDiscount","false");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        options.put("block", "false");
        options.put("expired","false");
        searchDistributorPackage(listener, cookie, options, requestCode, APICode.getDiscountDProducts);
    }


    public static void getDistributorPackage(ActivityServiceListener listener,
                                             String cookie,
                                             int requestCode,
                                             String id){
        viewDistributorPackage(listener, cookie, id, requestCode, APICode.getDistributorPackage);
    }

    public static void searchDPackages(ActivityServiceListener listener,
                                       String cookie,
                                       Map<String, String> options,
                                       int offset,
                                       int requestCode){
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        options.put("block", "false");
        options.put("expired","false");
        searchDistributorPackage(listener, cookie, options, requestCode, APICode.searchDistributorPackages);
    }

    public static void countProductInDPackage(final ActivityServiceListener listener,
                                              String cookie,
                                              String productId,
                                              boolean bundlingOrDiscount,
                                              boolean idOrBarcode,
                                              final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Map<String,String> options = new HashMap<>();
        if(idOrBarcode)
            options.put("productId", productId+"");
        else
            options.put("productbarcode", productId);
        options.put("bundlingOrDiscount", bundlingOrDiscount+"");
        Call<ServiceResponse> call = api.countProductInDistributorPackage(cookie,options);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();

                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.countProductInDPackage, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.countProductInDPackage, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.countProductInDPackage, t, requestCode);
            }
        });
    }

}
