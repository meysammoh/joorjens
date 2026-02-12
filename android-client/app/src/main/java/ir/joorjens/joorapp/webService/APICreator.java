package ir.joorjens.joorapp.webService;

import android.content.Context;
import android.os.Environment;
import android.widget.SimpleCursorAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.client.android.Contents;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.joorjens.joorapp.BuildConfig;
import ir.joorjens.joorapp.JJApp;
import ir.joorjens.joorapp.webService.CachingAnnotation.CacheCallAdapter;
import ir.joorjens.joorapp.webService.CachingAnnotation.CacheCallAdapterFactory;
import ir.joorjens.joorapp.webService.CachingAnnotation.CacheInterceptor;
import ir.joorjens.joorapp.webService.CachingAnnotation.OfflineInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.Retrofit2Platform;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit.Builder;

/**
 * Created by Mohsen on 8/2/2017.
 */

public class APICreator {
    private static String mImagesBaseAddr = "http://79.175.163.13/";
    private static String BaseURI = "http://79.175.163.13:5000/api/v1/";

    public static final int maxResult = 10;

    private static APIInterface api = null;
    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    public static String getImagesBaseAddress(){
        return mImagesBaseAddr;
    }

    public static void setIpAndPort(String ip_port){
        BaseURI = "http://" + ip_port + "/api/v1/";
        api = null;
    }

    private static Cache getCache() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "UCC" + File.separator);
        root.mkdirs();
        final String fname = "sagchache";
        final File sdImageMainDirectory = new File(root, fname);


        //File cacheDir = new File(JJApp.getAppContext().getCacheDir(), "sagchache");
        Cache cache = new Cache(sdImageMainDirectory, DISK_CACHE_SIZE);
        return cache;
    }

    public static APIInterface GetAPI(){
        if(api != null){
            return api;
        }
        else {

            if(BuildConfig.FLAVOR.contains("beta")){
                BaseURI = "http://185.2.15.134:5000/api/v1/";
                mImagesBaseAddr = "http://185.2.15.134/";
            }

            // use SparseArray on android! I just did this to make clear what happens here
            //Map<Integer, String> annotationRegistration = new HashMap<>();
            //List<CallAdapter.Factory> factories = new ArrayList<>();
            // add all your CallAdapter.Factories here
            //factories.add(new CacheCallAdapter())
            // add default CallAdapter as the last item. This is mandatory!
            //factories.add(Retrofit2Platform.defaultCallAdapterFactory(null));

            //CacheCallAdapterFactory callAdapterFactory =
            //        new CacheCallAdapterFactory(factories, annotationRegistration);

            OkHttpClient client = new OkHttpClient.Builder()
                    //.addInterceptor(new OfflineInterceptor(annotationRegistration))
                    //.addNetworkInterceptor(new CacheInterceptor(annotationRegistration))
                    //.cache(getCache())
                    .build();



            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit adapter = new Builder()
                    .baseUrl(BaseURI)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = adapter.create(APIInterface.class);

            return api;
        }
    }
}
