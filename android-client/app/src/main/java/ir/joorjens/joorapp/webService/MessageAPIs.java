package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Message;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.Authenticator;
import ir.joorjens.joorapp.webService.params.SeenMessageParams;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageAPIs {

    public static void getMyMessages(final ActivityServiceListener listener,
                                     String cookie,
                                     int offset,
                                     int max,
                                     final int requestCode){

        Map<String, String> options = new HashMap<>();
        options.put("send","false");
        options.put("receiverid", Authenticator.loadAccount().getId()+"");
        options.put("offset", offset+"");
        options.put("max", max+"");

        APIInterface api = APICreator.GetAPI();
        Call<ResultList<Message>> call = api.searchMessages(cookie, options);

        call.enqueue(new Callback<ResultList<Message>>() {
            @Override
            public void onResponse(Call<ResultList<Message>> call, Response<ResultList<Message>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchMessages, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchMessages, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Message>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchMessages, t, requestCode);
            }
        });
    }

    public static void setMessagesStatus(final ActivityServiceListener listener,
                                     String cookie,
                                     SeenMessageParams params,
                                     final int requestCode){

        APIInterface api = APICreator.GetAPI();
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), params.toString());
        Call<ServiceResponse> call = api.seenMessageReceiver(cookie, body);

        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.seenMessageReceiver, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.seenMessageReceiver, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.seenMessageReceiver, t, requestCode);
            }
        });
    }
}
