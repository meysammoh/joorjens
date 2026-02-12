package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Product;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.EnumHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 11/6/2017.
 */

public class ProductAPIs {

    private static void searchProduct(final ActivityServiceListener listener,
                                      String cookie,
                                      @Nullable Map<String, String> options,
                                      final int requestCode,
                                      final APICode callerAPI){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<Product>> call = api.searchProduct(cookie, options);

        call.enqueue(new Callback<ResultList<Product>>() {
            @Override
            public void onResponse(Call<ResultList<Product>> call, Response<ResultList<Product>> response) {
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
            public void onFailure(Call<ResultList<Product>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(callerAPI, t, requestCode);
            }
        });

    }
    private static void viewProduct(final ActivityServiceListener listener,
                                      String cookie,
                                      String product_id,
                                      final int requestCode,
                                      final APICode callerAPI){
        APIInterface api = APICreator.GetAPI();
        Call<Product> call = api.viewProduct(cookie, product_id);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
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
            public void onFailure(Call<Product> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(callerAPI, t, requestCode);
            }
        });

    }

    public static void getNewestProducts(ActivityServiceListener listener,
                                         String cookie,
                                         int offset,
                                         int requestCode){
        Map<String, String> options = new HashMap();
        options.put("ordertypeid", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdTime)+"");
        options.put("asc","false");
        options.put("onlystocks","true");
        options.put("ifProductBlocked","false");
        options.put("block","false");
        options.put("firstpage", "true"); // for filtering products that has at least one distributor
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchProduct(listener, cookie, options, requestCode, APICode.getNewestProducts);
    }

//    public static void getCheapestProducts(ActivityServiceListener listener,
//                                         String cookie,
//                                         int requestCode){
//        //TODO add filter to query map
//        Map<String, String> options = new HashMap();
//        searchProduct(listener, cookie, options, requestCode, APICode.getCheapestProducts);
//    }

    public static void getTopSellingProducts(ActivityServiceListener listener,
                                             String cookie,
                                             int offset,
                                             int requestCode){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("ifProductBlocked","false");
        options.put("block","false");
        options.put("firstPage", "true"); // for filtering products that has at least one distributor
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchProduct(listener, cookie, options, requestCode, APICode.getTopSellingProducts);
    }

//    public static void getDiscountProducts(ActivityServiceListener listener,
//                                           String cookie,
//                                           int requestCode){
//        //TODO add filter to query map
//        Map<String, String> options = new HashMap();
//        searchProduct(listener, cookie, options, requestCode, APICode.getDiscountProducts);
//    }
    public static void searchByOptions(ActivityServiceListener listener,
                                       String cookie,
                                       int requestCode, Map<String, String> options){
        searchProduct(listener, cookie, options, requestCode, APICode.searchProduct);
    }
    public static void getProduct(ActivityServiceListener listener,
                                       String cookie,
                                       int requestCode, String product_id){
        viewProduct(listener, cookie, product_id, requestCode, APICode.getProduct);
    }

    public static void getSimilarNewestProducts(ActivityServiceListener listener,
                                          String cookie,
                                          int offset,
                                          int requestCode,
                                          Integer categoryTypeId){
        Map<String, String> options = new HashMap<>();
        options.put("orderTypeId", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdTime)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("ifProductBlocked","false");
        options.put("block","false");
        options.put("firstPage", "true"); // for filtering products that has at least one distributor
        options.put("productCategoryTypeId", categoryTypeId+"");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchProduct(listener, cookie, options, requestCode, APICode.getSimilarNewProducts);
    }

    public static void getSimilarTopSellingProducts(ActivityServiceListener listener,
                                                  String cookie,
                                                  int offset,
                                                  int requestCode,
                                                  Integer categoryTypeId){
        Map<String, String> options = new HashMap();
        options.put("orderTypeId", EnumHelper.getEnumCode(EnumHelper.AllEnumNames.OrderTypeIdSelling)+"");
        options.put("asc","false");
        options.put("onlyStocks","true");
        options.put("ifProductBlocked","false");
        options.put("block","false");
        options.put("firstPage", "true"); // for filtering products that has at least one distributor
        options.put("productCategoryTypeId",categoryTypeId+"");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchProduct(listener, cookie, options, requestCode, APICode.getSimilarTopSaleProducts);
    }

    public static void getSimilarProducts(ActivityServiceListener listener,
                                                    String cookie,
                                                    int offset,
                                                    int requestCode,
                                                    Integer categoryTypeId){
        Map<String, String> options = new HashMap();
        options.put("onlyStocks","true");
        options.put("block","false");
        options.put("ifProductBlocked","false");
        options.put("firstPage", "true"); // for filtering products that has at least one distributor
        options.put("productCategoryTypeId",categoryTypeId+"");
        options.put("max", APICreator.maxResult+"");
        options.put("offset",offset+"");
        searchProduct(listener, cookie, options, requestCode, APICode.getSimilarProducts);
    }
}
