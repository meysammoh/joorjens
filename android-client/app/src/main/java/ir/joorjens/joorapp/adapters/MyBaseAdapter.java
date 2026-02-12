package ir.joorjens.joorapp.adapters;

/**
 * Created by ZM on 5/28/2018.
 */

public class MyBaseAdapter {
    public interface onEndReachedListener{
        public void onEndReached(int position);
    }

    public enum ContentType{
        DPackage, DProduct, Product
    }
}
