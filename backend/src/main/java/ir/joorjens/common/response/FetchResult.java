package ir.joorjens.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FetchResult<T> {
    private int max;
    private int offset;
    private int total;
    private List<T> result;
    private Object info;

    public FetchResult() {
    }

    public FetchResult(int max, int offset) {
        this.max = max;
        this.offset = offset;
    }

    public FetchResult(int max, int offset, int total) {
        this.max = max;
        this.offset = offset;
        this.total = total;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    @NotNull
    public List<T> getResult() {
        return result != null ? result : new ArrayList<>();
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @JsonIgnore
    public int getResultSize() {
        return (result != null) ? result.size() : 0;
    }

    public T getResultAt(int index) {
        return (index < getResultSize()) ? result.get(index) : null;
    }

    public void addResult(T obj) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(obj);
    }

    public void addResult(T obj, int index) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(index, obj);
    }
}
