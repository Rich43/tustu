package com.sun.org.apache.xml.internal.resolver;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/CatalogException.class */
public class CatalogException extends Exception {
    public static final int WRAPPER = 1;
    public static final int INVALID_ENTRY = 2;
    public static final int INVALID_ENTRY_TYPE = 3;
    public static final int NO_XML_PARSER = 4;
    public static final int UNKNOWN_FORMAT = 5;
    public static final int UNPARSEABLE = 6;
    public static final int PARSE_FAILED = 7;
    public static final int UNENDED_COMMENT = 8;
    private Exception exception;
    private int exceptionType;

    public CatalogException(int type, String message) {
        super(message);
        this.exception = null;
        this.exceptionType = 0;
        this.exceptionType = type;
        this.exception = null;
    }

    public CatalogException(int type) {
        super("Catalog Exception " + type);
        this.exception = null;
        this.exceptionType = 0;
        this.exceptionType = type;
        this.exception = null;
    }

    public CatalogException(Exception e2) {
        this.exception = null;
        this.exceptionType = 0;
        this.exceptionType = 1;
        this.exception = e2;
    }

    public CatalogException(String message, Exception e2) {
        super(message);
        this.exception = null;
        this.exceptionType = 0;
        this.exceptionType = 1;
        this.exception = e2;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String message = super.getMessage();
        if (message == null && this.exception != null) {
            return this.exception.getMessage();
        }
        return message;
    }

    public Exception getException() {
        return this.exception;
    }

    public int getExceptionType() {
        return this.exceptionType;
    }

    @Override // java.lang.Throwable
    public String toString() {
        if (this.exception != null) {
            return this.exception.toString();
        }
        return super.toString();
    }
}
