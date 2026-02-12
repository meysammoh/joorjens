package ir.joorjens.joorapp.webService.CachingAnnotation;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZM on 5/13/2018.
 */

public class OfflineInterceptor implements Interceptor {

    private final Map<Integer, String> registration;

    public OfflineInterceptor(Map<Integer, String> registration) {
        this.registration = registration;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String info = registration.get(identify(request));
        if(info != null && info.endsWith("_offline")) {
            int maxStale = 0;
            if(info == "get_cart_offline") {
                maxStale = 3600;
            }else if(info == "sdpr"){
                maxStale = 2000;
            }
            request = request.newBuilder()
                    .header("Cache-Control",
                            "public, only-if-cached, max-stale=" + maxStale)
                    .build();
            Response response = chain.proceed(request);
            if(response.cacheResponse() == null || response.cacheResponse().code() == 404){
                request = request.newBuilder()
                        .removeHeader("Cache-Control")
                        .build();
            }else{
                return response;
            }
        }
        return chain.proceed(request);
    }

    private Integer identify(Request request) {
        // make sure this is the same method you use in the CallAdapter
        return (request.url() + request.method()).hashCode();
    }


}
