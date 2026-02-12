package ir.joorjens.joorapp.webService;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.models.Transaction;
import ir.joorjens.joorapp.utils.EnumHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsen on 5/5/2018.
 */

public class TransactionAPIs {

    public static void searchProduct(final ActivityServiceListener listener,
                                      String cookie,
                                      int customerId,
                                      long timeFrom,
                                      long timeTo,
                                      final int requestCode){
        APIInterface api = APICreator.GetAPI();

        Map<String, String> options = new HashMap();
        options.put("customerId", customerId+"");
        if(timeFrom > 0)
            options.put("timeFrom",timeFrom+ "");
        if(timeTo > 0)
            options.put("timeTo",timeTo+ "");

        Call<ResultList<Transaction>> call = api.searchTransactions(cookie, options);

        call.enqueue(new Callback<ResultList<Transaction>>() {
            @Override
            public void onResponse(Call<ResultList<Transaction>> call, Response<ResultList<Transaction>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchTransaction, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchTransaction, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Transaction>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchTransaction, t, requestCode);
            }
        });

    }
}
