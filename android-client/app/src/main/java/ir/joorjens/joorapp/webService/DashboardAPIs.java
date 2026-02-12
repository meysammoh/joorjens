package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.DashboardSale;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.utils.EnumHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsen on 7/1/2018.
 */

public class DashboardAPIs {
    public static void getYearSaleByMonth(final ActivityServiceListener listener,
                                          String cookie,
                                          final int requestCode){

        APIInterface api = APICreator.GetAPI();
        Map<String, String> filters = new HashMap<>();
        filters.put("timeStampId",EnumHelper.getEnumCode(EnumHelper.AllEnumNames.DashboardTimeMonth)+"");
        Call<ResultList<DashboardSale>> call = api.dashboardSale(cookie, filters);
        call.enqueue(new Callback<ResultList<DashboardSale>>() {
            @Override
            public void onResponse(Call<ResultList<DashboardSale>> call, Response<ResultList<DashboardSale>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getDashboardSaleByMonth, response.body().getResult(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getDashboardSaleByMonth, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<DashboardSale>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getDashboardSaleByMonth, t, requestCode);
            }
        });
    }
}
