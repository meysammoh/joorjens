package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.KeyValueChildItem;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ProductCategory;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meysammoh on 14.11.17.
 */

public class CategoryAPIs {
    private static final int ProductDetailTypeId = 1210;
    private static final int ProductCategoryTypeId = 1310;
    private static List<ProductCategory> mCategories;
    private static void pairCategory(final ActivityServiceListener listener,
                                      String cookie,
                                      long parentId,
                                      final int requestCode,
                                      final APICode callerAPI){
        APIInterface api = APICreator.GetAPI();
        Call<List<PairResultItem>> call = api.pairCategories(cookie, parentId);

        call.enqueue(new Callback<List<PairResultItem>>() {
            @Override
            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {
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
            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(callerAPI, t, requestCode);
            }
        });
    }
    public static void getHierarchicalCategories(final ActivityServiceListener listener,
                                     String cookie,
                                     final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<List<KeyValueChildItem>> call = api.getHierarchicalCategories(cookie);

        call.enqueue(new Callback<List<KeyValueChildItem>>() {
            @Override
            public void onResponse(Call<List<KeyValueChildItem>> call, Response<List<KeyValueChildItem>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getHierarchicalCategories, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getHierarchicalCategories, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KeyValueChildItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getHierarchicalCategories, t, requestCode);
            }
        });
    }
    private static void searchCategory(final ActivityServiceListener listener,
                                       final String cookie,
                                       final int parentId,
                                       final int offset,
                                       final int requestCode,
                                       final APICode callerAPI){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<ProductCategory>> call = api.searchCategory(cookie, parentId,offset);

        call.enqueue(new Callback<ResultList<ProductCategory>>() {
            @Override
            public void onResponse(Call<ResultList<ProductCategory>> call, Response<ResultList<ProductCategory>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        if (mCategories != null)
                            mCategories.addAll(response.body().getResult());
                        else
                            mCategories = response.body().getResult();
                        if (response.body().getTotal() <= response.body().getOffset() * response.body().getMax()) {
                            listener.onServiceSuccess(callerAPI, mCategories, requestCode);
                        } else {
                            searchCategory(listener, cookie, parentId, offset + 1, requestCode, callerAPI);
                        }
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
            public void onFailure(Call<ResultList<ProductCategory>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(callerAPI, t, requestCode);
            }
        });
    }
    public static void getFirstLevelCategoryPairs(ActivityServiceListener listener,
                                     String cookie,
                                     int requestCode){
        pairCategory(listener,cookie, ProductCategoryTypeId,requestCode, APICode.getFirstLevelCategoryPairs);
    }

    public static void getSubCategoryPairsOf(ActivityServiceListener listener, String cookie, int requestCode, Long parentId) {
        pairCategory(listener,cookie, parentId,requestCode, APICode.getSubCategoryPairsOf);
    }

    public static void getAllCategories(ActivityServiceListener listener,
                                                  String cookie,
                                                  int requestCode){
        if(mCategories != null)
            mCategories.clear();
        searchCategory( listener,cookie, 0,0,requestCode, APICode.getAllCategories);
    }
}
