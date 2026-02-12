package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meysammoh on 05.05.18.
 */

public class TypeAPIs {
    public static void getPairStatusTypes(final ActivityServiceListener listener,
                                     String cookie,
                                     final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<List<PairResultItem>> call = api.pairOrderStatusType(cookie, true);
        call.enqueue(new Callback<List<PairResultItem>>() {
            @Override
            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {

                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.pairStatusTypes, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.pairStatusTypes, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.pairStatusTypes, t, requestCode);
            }
        });
    }
}
