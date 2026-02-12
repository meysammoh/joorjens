package ir.joorjens.joorapp.webService.CachingAnnotation;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.utils.StaticHelperFunctions;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZM on 5/13/2018.
 */

public class CacheInterceptor implements Interceptor {

    private final Map<Integer, String> registration;

    public CacheInterceptor(Map<Integer, String> registration) {
        this.registration = registration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String info = registration.get(identify(request));
        if (info != null) {
            int cacheTime = 3600;
            if(info  == "get_cart"){
                cacheTime = 2400;
            }else if(info == "sdpr"){
                cacheTime = 1000;
            }

            response = response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + cacheTime)
                    .build();
        }
        return response;
    }

    private Integer identify(Request request) {
        // make sure this is the same method you use in the CallAdapter
        return (request.url() + request.method()).hashCode();
    }


}
