package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 10/7/2017.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultList<T> {

    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("result")
    @Expose
    private ArrayList<T> result = null;

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ArrayList<T> getResult() {
        return result;
    }

    public void setResult(ArrayList<T> result) {
        this.result = result;
    }

}
