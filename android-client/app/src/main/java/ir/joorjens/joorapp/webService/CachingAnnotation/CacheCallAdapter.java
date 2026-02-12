package ir.joorjens.joorapp.webService.CachingAnnotation;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * Created by ZM on 5/13/2018.
 */

public class CacheCallAdapter<R,T> implements CallAdapter<R,T> {

    private final CallAdapter<R,T> adapter;
    private final Map<Integer, String> registration;
    private final String info;

    public CacheCallAdapter(CallAdapter<R, T> adapter, Map<Integer, String> registration, String info) {
        this.adapter = adapter;
        this.registration = registration;
        this.info = info;
    }

    @Override
    public Type responseType() {
        return adapter.responseType();
    }

    @Override
    public T adapt(Call<R> call) {
        Request request = call.request();
        registration.put(identify(request), info);
        return adapter.adapt(call);
    }

    private Integer identify(Request request) {
        // this is very experimental but it does the job currently
        return (request.url() + request.method()).hashCode();
    }
}
