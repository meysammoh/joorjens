package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Banner;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meysam on 4/6/2018.
 */

public class BrandAPIs {

    public static void getPairBrands(final ActivityServiceListener listener,
                                     String cookie,
                                     final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<List<PairResultItem>> call = api.pairBrands(cookie, true);
        call.enqueue(new Callback<List<PairResultItem>>() {
            @Override
            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {

                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.pairBrands, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.pairBrands, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.pairBrands, t, requestCode);
            }
        });
    }
}
