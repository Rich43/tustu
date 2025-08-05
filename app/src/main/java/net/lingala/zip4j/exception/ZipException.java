package net.lingala.zip4j.exception;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/exception/ZipException.class */
public class ZipException extends Exception {
    private static final long serialVersionUID = 1;
    private int code;

    public ZipException() {
        this.code = -1;
    }

    public ZipException(String msg) {
        super(msg);
        this.code = -1;
    }

    public ZipException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    public ZipException(String msg, int code) {
        super(msg);
        this.code = -1;
        this.code = code;
    }

    public ZipException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = -1;
        this.code = code;
    }

    public ZipException(Throwable cause) {
        super(cause);
        this.code = -1;
    }

    public ZipException(Throwable cause, int code) {
        super(cause);
        this.code = -1;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
