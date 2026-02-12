package ir.joorjens.joorapp.webService;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.joorjens.joorapp.activities.ActivityServiceListener;
import ir.joorjens.joorapp.models.Profile;
import ir.joorjens.joorapp.models.ServiceResponse;
import ir.joorjens.joorapp.webService.params.ActivationParams;
import ir.joorjens.joorapp.webService.params.ChangePasswordParams;
import ir.joorjens.joorapp.webService.params.ForgetPasswordVerifyParams;
import ir.joorjens.joorapp.webService.params.LoginParams;
import ir.joorjens.joorapp.webService.params.SignUpParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mohsen on 8/4/2017.
 */

public class UserAPIs {

    public static void login(
            final ActivityServiceListener listener,
            LoginParams params,
            final int reqCode) {

        APIInterface api = APICreator.GetAPI();
        Call<ServiceResponse> call = api.login(params);
        call.enqueue(new Callback<ServiceResponse>() {

            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.login, response, reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.login, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.login, t, reqCode);
            }
        });
    }

    public static void signUp(
            final ActivityServiceListener listener,
            SignUpParams params,
            final int reqCode) {

        APIInterface api = APICreator.GetAPI();
        Call<ServiceResponse> call = api.signUp(params);
        call.enqueue(new Callback<ServiceResponse>() {

            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.signUp, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.signUp, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.signUp, t, reqCode);
            }
        });
    }

    public static void getProfile(
            final ActivityServiceListener listener,
            String authToken,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<Profile> call = api.getProfile(authToken);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.getProfile, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.getProfile, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.getProfile, t, reqCode);
            }
        });
    }

    public static void updateProfile(
            final ActivityServiceListener listener,
            String authToken,
            Profile profile,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<Profile> call = api.updateProfile(authToken, profile);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.updateProfile, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.updateProfile, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.updateProfile, t, reqCode);
            }
        });
    }

    public static void activate(
            final ActivityServiceListener listener,
            ActivationParams params,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<Profile> call = api.activate(params);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.activate, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.activate, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.activate, t, reqCode);
            }
        });
    }

    public static void resendActivationCode(
            final ActivityServiceListener listener,
            String mobileNumber,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        HashMap<String,String> arg = new HashMap<>();
        arg.put("mobileNumber",mobileNumber);
        final Call<ServiceResponse> call = api.resendActivationCode(arg);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.resendActivationCode, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.resendActivationCode, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.resendActivationCode, t, reqCode);
            }
        });
    }

    public static void forgetPassword(
            final ActivityServiceListener listener,
            String mobileNumber,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        HashMap<String,String> arg = new HashMap<>();
        arg.put("mobileNumber",mobileNumber);
        final Call<ServiceResponse> call = api.forgetPassword(arg);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.forgetPassword, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.forgetPassword, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.forgetPassword, t, reqCode);
            }
        });
    }

    public static void forgetPasswordVerify(final ActivityServiceListener listener,
                                            ForgetPasswordVerifyParams params,
                                            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<ServiceResponse> call = api.forgetPasswordVerify(params);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.forgetPasswordVerify, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.forgetPasswordVerify, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.forgetPasswordVerify, t, reqCode);
            }
        });
    }

    public static void logout(
            final ActivityServiceListener listener,
            String authToken,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<ServiceResponse> call = api.logout(authToken);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.logout, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.logout, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.logout, t, reqCode);
            }
        });
    }

    public static void changePassword(
            final ActivityServiceListener listener,
            String authToken,
            ChangePasswordParams params,
            final int reqCode){

        APIInterface api = APICreator.GetAPI();
        final Call<ServiceResponse> call = api.changePassword(authToken, params);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(!listener.isActive())
                    call.cancel();
                else {
                    if (response.isSuccessful()) {
                        listener.onServiceSuccess(APICode.changePassword, response.body(), reqCode);
                    } else {
                        ServiceResponse sresp = null;
                        try {
                            Gson gson = new Gson();
                            sresp = gson.fromJson(response.errorBody().string(), ServiceResponse.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listener.onServiceFail(APICode.changePassword, sresp, reqCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                if(!listener.isActive())
                    call.cancel();
                else
                    listener.onNetworkFail(APICode.changePassword, t, reqCode);
            }
        });
    }
}
