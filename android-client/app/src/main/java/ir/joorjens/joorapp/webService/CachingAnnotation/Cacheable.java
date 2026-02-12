package ir.joorjens.joorapp.webService.CachingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ZM on 5/12/2018.
 */

@Target(METHOD)
@Retention(RUNTIME)
public @interface Cacheable {
    String[] value();
}
