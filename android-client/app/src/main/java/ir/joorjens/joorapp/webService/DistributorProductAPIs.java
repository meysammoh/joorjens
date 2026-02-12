package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
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

public class DistributorProductAPIs {
    public  static String ProductBarcodeParam = "productbarcode";
    //TODO Use concurrency control to avoid problems
    //private static List<DistributorProduct> mProductDistributors;


    private static void searchDistributorProduct(final ActivityServiceListener listener,
                                                 String cookie,
                                                 @Nullable Map<String, String> options,
                                                 final int requestCode,
                                                 final APICode calleAPI){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<DistributorProduct>> call = api.searchDistributorProduct(cookie, options);

        call.enqueue(new Callback<ResultList<DistributorProduct>>() {
            @Override
            public void onResponse(Call<ResultList<DistributorProduct>> call, Response<ResultList<DistributorProduct>> response) {
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
            public void onFailure(Call<ResultList<DistributorProduct>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(calleAPI, t, requestCode);
            }
        });
    }

    private static void searchProductDistributors(final ActivityServiceListener listener,
                                                 final String cookie,
                                                 @Nullable final Map<String, String> options,
                                                 final int requestCode,
                                                 final APICode calleAPI){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<DistributorProduct>> call = api.searchDistributorProduct(cookie, options);

        call.enqueue(new Callback<ResultList<DistributorProduct>>() {
            @Override
            public void onResponse(Call<ResultList<DistributorProduct>> call, Response<ResultList<DistributorProduct>> response) {
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
            public void onFailure(Call<ResultList<DistributorProduct>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(calleAPI, t, requestCode);
            }
        });
    }
    private static void viewDistributorProduct(final ActivityServiceListener listener,
                                                 String cookie,
                                                 String productId,
                                                 final int requestCode,
                                                 final APICode callerAPI){
        APIInterface api = APICreator.GetAPI();
        Map<String, String> options = new HashMap();
        options.put("id", productId);
        options.put("onlyStocks","true");
        options.put("block","false");
        Call<DistributorProduct> call = api.viewDistributorProduct(cookie, options);

        call.enqueue(new Callback<DistributorProduct>() {
            @Override
            public void onResponse(Call<DistributorProduct> call, Response<DistributorProduct> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(callerAPI, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(callerAPI, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistributorProduct> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(callerAPI, t, requestCode);
            }
        });
    }

    public static void getCheapestDProducts(ActivityServiceListener listener,
                                           String cookie,
                                            int offset,
                                           int requestCode){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdPrice)+"");
        options.put("asc","true");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchDistributorProduct(listener, cookie, options, requestCode, APICode.getCheapestDProducts);
    }

    public static void getAllDProducts(ActivityServiceListener listener,
                                            String cookie,
                                            int offset,
                                            int requestCode){
        Map<String, String> options = new HashMap();
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchDistributorProduct(listener, cookie, options, requestCode, APICode.getAllDProducts);
    }

    public static void getNewestDProducts(ActivityServiceListener listener,
                                           String cookie,
                                          int offset,
                                           int requestCode){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdTime)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchDistributorProduct(listener, cookie, options, requestCode, APICode.getNewestDProducts);
    }

    public static void getTopSellingDProducts(ActivityServiceListener listener,
                                          String cookie,
                                              int offset,
                                          int requestCode){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchDistributorProduct(listener, cookie, options, requestCode, APICode.getTopSellingDProducts);
    }

    public static void searchDProducts(ActivityServiceListener listener,
                                              String cookie,
                                                Map<String, String> options,
                                       int offset,
                                              int requestCode){
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        options.put("onlyStocks","true");
        options.put("block","false");
        searchDistributorProduct(listener, cookie, options, requestCode, APICode.searchDistributorProduct);
    }

    public static void getDistributorProduct(ActivityServiceListener listener,
                                             String cookie,
                                             int requestCode,
                                             String productId){
        viewDistributorProduct(listener, cookie, productId, requestCode, APICode.getDistributorProduct);
    }

    public static void getProductDistributors(ActivityServiceListener listener,
                                             String cookie,
                                             int requestCode,
                                             String productId){
        Map<String, String> options = new HashMap();
        options.put("productId",productId);
        options.put("block","false");
        searchProductDistributors(listener, cookie, options, requestCode, APICode.searchProductDistributors);
    }
    public static void getProductDistributorsByBarcode(ActivityServiceListener listener,
                                              String cookie,
                                              int requestCode,
                                              String barcode){
        Map<String, String> options = new HashMap();
        options.put(ProductBarcodeParam,barcode);
        options.put("block","false");
        searchProductDistributors(listener, cookie, options, requestCode, APICode.searchProductDistributors);
    }

    public static void getSimilarCheapestDistributorProducts(ActivityServiceListener listener,
                                                String cookie,
                                                int offset,
                                                int requestCode,
                                                Integer categoryTypeId){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdPrice)+"");
        options.put("asc","true");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("productCategoryTypeId",categoryTypeId+"");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        APICode calledAPI = calledAPI = APICode.getSimilarCheapDProducts;
        searchProductDistributors(listener, cookie, options, requestCode, calledAPI);
    }
    public static void getSimilarNewestDistributorProducts(ActivityServiceListener listener,
                                                             String cookie,
                                                             int requestCode,
                                                             Integer categoryTypeId){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdTime)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("productCategoryTypeId",categoryTypeId+"");
        APICode calledAPI = calledAPI = APICode.getSimilarNewDProducts;;
        searchProductDistributors(listener, cookie, options, requestCode, calledAPI);
    }
    public static void getSimilarTopSellingDistributorProducts(ActivityServiceListener listener,
                                                           String cookie,
                                                           int requestCode,
                                                           Integer categoryTypeId){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("productCategoryTypeId",categoryTypeId+"");
        APICode calledAPI = calledAPI = APICode.getSimilarNewDProducts;;
        searchProductDistributors(listener, cookie, options, requestCode, calledAPI);
    }
}
