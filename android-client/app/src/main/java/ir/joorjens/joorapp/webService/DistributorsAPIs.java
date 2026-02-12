package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Distributor;
import ir.joorjens.joorapp.models.PairResultItem;
import ir.joorjens.joorapp.models.ResultList;
import ir.joorjens.joorapp.models.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meysam on 4/6/2018.
 */

public class DistributorsAPIs {

    public static void getPairDistributors(final ActivityServiceListener listener,
                                           String cookie,
                                           final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<List<PairResultItem>> call = api.pairDistributors(cookie);
        call.enqueue(new Callback<List<PairResultItem>>() {
            @Override
            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.pairDistributors, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.pairDistributors, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.pairDistributors, t, requestCode);
            }
        });
    }
    public static void searchDistributors(final ActivityServiceListener listener,
                                           String cookie,
                                          HashMap<String, String> params,
                                           final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<ResultList<Distributor>> call = api.searchDistributors(cookie, params);
        call.enqueue(new Callback<ResultList<Distributor>>() {
            @Override
            public void onResponse(Call<ResultList<Distributor>> call, Response<ResultList<Distributor>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.searchDistributors, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.searchDistributors, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultList<Distributor>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.searchDistributors, t, requestCode);
            }
        });
    }

    public static void getAllDistributorDiscontents(final ActivityServiceListener listener,
                                          String cookie,
                                          final int requestCode){
        APIInterface api = APICreator.GetAPI();
        Call<List<PairResultItem>> call = api.getDistributorDiscontentTypes(cookie);
        call.enqueue(new Callback<List<PairResultItem>>() {
            @Override
            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getDistributorDiscontentTypes, response.body(), requestCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getDistributorDiscontentTypes, sresp, requestCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getDistributorDiscontentTypes, t, requestCode);
            }
        });
    }

    public static void rateDistributor(final ActivityServiceListener listener,
                                                    String cookie,
                                                    final int requestCode){
//        APIInterface api = APICreator.GetAPI();
//        Call<List<PairResultItem>> call = api.getDistributorDiscontentTypes(cookie);
//        call.enqueue(new Callback<List<PairResultItem>>() {
//            @Override
//            public void onResponse(Call<List<PairResultItem>> call, Response<List<PairResultItem>> response) {
//                if(!listener.isActive())
//                    call.cancel();
//                else {
//                    if (response.isSuccessful()) {
//                        listener.onServiceSuccess(APICode.getDistributorDiscontentTypes, response.body(), requestCode);
//                    } else {
//                        ServiceResponse sresp = null;
//                        try {
//                            Gson gson = new Gson();
//                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        listener.onServiceFail(APICode.getDistributorDiscontentTypes, sresp, requestCode);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PairResultItem>> call, Throwable t) {
//                if(!listener.isActive())
//                    call.cancel();
//                else
//                    listener.onNetworkFail(APICode.getDistributorDiscontentTypes, t, requestCode);
//            }
//        });
    }
}
