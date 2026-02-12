package ir.joorjens.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class ResponseMessage implements Serializable {

    private final String message;
    private final int responseCode;
    private final int code;
    private Object data;

    public ResponseMessage() {
        this(ResponseCode.DONE);
    }

    public ResponseMessage(String message) {
        this(message, HttpServletResponse.SC_OK, 0);
    }

    public ResponseMessage(ExceptionCode exceptionCode) {
        this(exceptionCode.getMessage(), exceptionCode.getHttpCode(), exceptionCode.getErrorCode());
    }

    public ResponseMessage(ResponseCode responseCode) {
        this(responseCode.getMessage(), responseCode.getHttpCode(), 0);
    }

    public ResponseMessage(String message, ExceptionCode exceptionCode) {
        this(message, exceptionCode.getHttpCode(), exceptionCode.getErrorCode());
    }

    public ResponseMessage(String message, int code) {
        this(message, HttpServletResponse.SC_OK, code);
    }

    private ResponseMessage(String message, int responseCode, int code) {
        this.message = message;
        this.responseCode = responseCode;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isOk() {
        return responseCode == HttpServletResponse.SC_OK;
    }
}
