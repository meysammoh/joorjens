package ir.joorjens.model.util;


import com.fasterxml.jackson.core.type.TypeReference;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;

import java.util.List;

/**
 * Created by dev on 12/12/16.
 */
public class FormFactory<T> {

    private final Class<T> classType;

    public FormFactory(Class<T> classType) {
        this.classType = classType;
    }

    @SuppressWarnings("unchecked")
    public T get(String body) throws JoorJensException {
        if (body == null) {
            body = "{}";
        }
        Object o = Utility.fromJson(body, classType);
        if (o == null) {
            throw new JoorJensException("your json is invalid", ExceptionCode.INVALID_JSON);
        }
        return (T) o;
    }

    @SuppressWarnings("unchecked")
    public List<T> getList(String body, TypeReference typeReference) throws JoorJensException {
        if (body == null) {
            body = "{}";
        }

        Object o = Utility.fromJson(body, typeReference);
        if (o == null) {
            throw new JoorJensException("your json[] is invalid", ExceptionCode.INVALID_JSON);
        }
        return (List<T>) o;
    }
}