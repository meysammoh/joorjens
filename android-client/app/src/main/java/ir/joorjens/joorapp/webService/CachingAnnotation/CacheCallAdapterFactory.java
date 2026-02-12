package ir.joorjens.joorapp.webService.CachingAnnotation;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Created by ZM on 5/13/2018.
 */

public class CacheCallAdapterFactory extends CallAdapter.Factory {

    private final List<CallAdapter.Factory> callAdapterFactories;
    private final Map<Integer, String> registration;

    public CacheCallAdapterFactory(List<CallAdapter.Factory> callAdapterFactories, Map<Integer, String> registration) {
        this.callAdapterFactories = callAdapterFactories;
        this.registration = registration;
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Cacheable annotation = getAnnotation(annotations);
        for (int i = 0; i < callAdapterFactories.size(); i++) {
            CallAdapter<?,?> adapter = callAdapterFactories
                    .get(i).get(returnType, annotations, retrofit);
            if (adapter != null) {
                if (annotation != null) {
                    // get whatever info you need from your annotation
                    String info = annotation.value()[0];
                    return new CacheCallAdapter<>(adapter, registration, info);
                }
                return adapter;
            }
        }
        return null;
    }

    private Cacheable getAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (Cacheable.class == annotation.annotationType()) {
                return (Cacheable) annotation;
            }
        }
        return null;
    }
}
