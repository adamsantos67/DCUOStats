package com.aksantos.dcuocensus;

public class DCUOException extends Exception {
    private static final long serialVersionUID = 5452247171173016661L;

    public DCUOException() {
        super();
    }

    public DCUOException(String message) {
        super(message);
    }

    public DCUOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DCUOException(Throwable cause) {
        super(cause);
    }

    protected DCUOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
