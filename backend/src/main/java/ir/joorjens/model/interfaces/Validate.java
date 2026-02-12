package ir.joorjens.model.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.response.JoorJensException;

public interface Validate {
    @JsonIgnore
    boolean isValid() throws JoorJensException;

    @JsonIgnore
    String getEntityName();
}
