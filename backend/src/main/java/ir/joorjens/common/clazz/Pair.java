package ir.joorjens.common.clazz;

import java.io.Serializable;

public class Pair<F, S> implements Serializable {
    private static final long serialVersionUID = 1395L;

    private F first;
    private S second;

    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
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
}
