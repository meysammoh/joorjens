package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.CartDistributor;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZM on 6/13/2018.
 */

public class DelivererAPIs {

    public static void getDelivererOrderList(final ActivityServiceListener listener,
                                             String cookie,
                                             final int requestCode) {
        APIInterface api = APICreator.GetAPI();
        final Call<ResultList<CartDistributor>> call = api.searchCartDist(cookie);
        call.enqueue(new Callback<ResultList<CartDistributor>>() {
            @Override
            public void onResponse(Call<ResultList<CartDistributor>> call, Response<ResultList<CartDistributor>> response) {
                if (!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchCartDist, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchCartDist, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<CartDistributor>> call, Throwable t) {
                if (!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchCartDist, t, requestCode);
            }
        });
    }
}
