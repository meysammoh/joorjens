package ir.joorjens.common.clazz;

import java.io.Serializable;

public class Third<F, S, T> implements Serializable {
    private static final long serialVersionUID = 1395L;

    private F first;
    private S second;
    private T third;

    public Third() {
    }

    public Third(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }
}