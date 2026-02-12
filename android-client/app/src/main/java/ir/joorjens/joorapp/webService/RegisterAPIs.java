package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.AreaResult;
import ir.joorjens.joorapp.models.EnumValues;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Store;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 10/7/2017.
 */

public class RegisterAPIs {

    public static void searchArea(final ActivityServiceListener listener,
                                  final int adType,
                                  int parentId, String cookie,
                                  final int reqCode){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<AreaResult>> call = api.searchArea(cookie, adType, parentId);
        call.enqueue(new Callback<ResultList<AreaResult>>() {
            @Override
            public void onResponse(Call<ResultList<AreaResult>> call, Response<ResultList<AreaResult>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchArea, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchArea, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<AreaResult>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchArea, t, reqCode);
            }
        });
    }
}
