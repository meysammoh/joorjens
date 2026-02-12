package ir.joorjens.tools;

import ir.joorjens.common.Utility;

import java.io.Serializable;
import java.util.*;

/**
 * Created by mehdi on 5/13/18.
 */
public class PageInfo implements Serializable {

    public String url, code, title, brand, price;
    public final List<String> cats = new ArrayList<>();
    public final Set<String> images = new HashSet<>();
    public final Map<String, String> specs = new HashMap<>();

    public int getPrice() {
        return price != null ? Integer.parseInt(price) : 0;
    }
}
