package ir.joorjens.joorapp.utils;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import ir.joorjens.joorapp.JJApp;

public class PicassoCache {

    private static Picasso mInstance = null;

    public static Picasso get(Context context){
        if(mInstance == null){
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context,Integer.MAX_VALUE));
            mInstance = builder.build();
        }

        return mInstance;
    }
}
