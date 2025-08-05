package com.sun.jna;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/LastErrorException.class */
public class LastErrorException extends RuntimeException {
    private int errorCode;

    private static String formatMessage(int code) {
        return Platform.isWindows() ? new StringBuffer().append("GetLastError() returned ").append(code).toString() : new StringBuffer().append("errno was ").append(code).toString();
    }

    private static String parseMessage(String m2) {
        try {
            return formatMessage(Integer.parseInt(m2));
        } catch (NumberFormatException e2) {
            return m2;
        }
    }

    public LastErrorException(String msg) {
        super(parseMessage(msg));
        try {
            this.errorCode = Integer.parseInt(msg);
        } catch (NumberFormatException e2) {
            this.errorCode = -1;
        }
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public LastErrorException(int code) {
        super(formatMessage(code));
        this.errorCode = code;
    }
}
