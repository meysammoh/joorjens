package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Banner;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsen on 12/26/2017.
 */

public class BannerAPIs {

    public static void getAllBanners(final ActivityServiceListener listener,
                                     String cookie,
                                     final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Map<String,String> options = new HashMap<>();
        options.put("orderTypeId","92");
        options.put("asc","true");
        Call<ResultList<Banner>> call = api.searchBanner(cookie, options);
        call.enqueue(new Callback<ResultList<Banner>>() {
            @Override
            public void onResponse(Call<ResultList<Banner>> call, Response<ResultList<Banner>> response) {

                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getAllBanners, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getAllBanners, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Banner>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getAllBanners, t, requestCode);
            }
        });
    }
}
