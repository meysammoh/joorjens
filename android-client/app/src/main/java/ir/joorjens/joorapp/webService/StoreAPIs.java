package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsen on 4/13/2018.
 */

public class StoreAPIs {

    public static void insertStore(final ActivityServiceListener listener,
                                   String cookie,
                                   Store store,
                                   final int reqCode){
        APIInterface api = APICreator.GetAPI();
        Call<ServiceResponse> call = api.insertStore(cookie, store);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.insertStore, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.insertStore, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.insertStore, t, reqCode);
            }
        });
    }

    public static void viewStore(final ActivityServiceListener listener,
                                   String cookie,
                                    int storeManagerId,
                                   final int reqCode){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<Store>> call = api.viewStore(cookie, storeManagerId);
        call.enqueue(new Callback<ResultList<Store>>() {
            @Override
            public void onResponse(Call<ResultList<Store>> call, Response<ResultList<Store>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.viewStore, response.body().getResult().get(0), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.insertStore, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Store>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.insertStore, t, reqCode);
            }
        });
    }
}
