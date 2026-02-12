package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.EnumValues;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 10/8/2017.
 */

public class CommonAPIs {

    public static void getEnumValue(final ActivityServiceListener listener,
                                    final String typeIds,
                                    String cookie,
                                    final int reqCode){
        APIInterface api = APICreator.GetAPI();
        Call<Map<Integer, List<EnumValues>>> call = api.getEnumValues(cookie, typeIds);
        call.enqueue(new Callback<Map<Integer, List<EnumValues>>>() {
            @Override
            public void onResponse(Call<Map<Integer, List<EnumValues>>> call, Response<Map<Integer, List<EnumValues>>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getEnumValue, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getEnumValue, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<Integer, List<EnumValues>>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getEnumValue, t, reqCode);
            }
        });
    }
}
