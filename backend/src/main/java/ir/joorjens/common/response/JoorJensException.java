package ir.joorjens.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JoorJensException extends Exception {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------------------------------------
    public static JoorJensException getInstanceByFormat(ExceptionCode exceptionCode, Object... objects) {
        return new JoorJensException(String.format(exceptionCode.getMessage(), objects), exceptionCode);
    }
    //------------------------------------------------------------------------------

    @JsonIgnore
    private final ResponseMessage responseMessage;

    public JoorJensException(String msg, ExceptionCode exceptionCode) {
        this.responseMessage = new ResponseMessage(msg, exceptionCode);
    }

    public JoorJensException(ExceptionCode exceptionCode) {
        this(exceptionCode.getMessage(), exceptionCode);
    }

    public JoorJensException(ExceptionCode exceptionCode, String... param) {
        this(String.format(exceptionCode.getMessage(), param), exceptionCode);
    }

    public JoorJensException(ExceptionCode exceptionCode, String param, double size) {
        this(String.format(exceptionCode.getMessage(), param, size), exceptionCode);
    }

    public JoorJensException(String msg) {
        super(msg);
        msg = msg.toLowerCase();
        ExceptionCode exceptionCode = ExceptionCode.get(msg);
        if (exceptionCode == null) {
            exceptionCode = ExceptionCode.getContains(msg);
        }
        if (exceptionCode == null) {
            if (msg.contains(ExceptionCode.UK_.getMessageKey())) {
                exceptionCode = ExceptionCode.UK_;
            } else {
                exceptionCode = ExceptionCode.EXCEPTION;
            }
        }
        this.responseMessage = new ResponseMessage(exceptionCode);
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public int getErrorCode() {
        return (responseMessage != null) ? responseMessage.getCode() : 0;
    }
}