package com.sun.java.browser.dom;

/* loaded from: rt.jar:com/sun/java/browser/dom/DOMAccessException.class */
public class DOMAccessException extends Exception {
    private Throwable ex;
    private String msg;

    public DOMAccessException() {
        this(null, null);
    }

    public DOMAccessException(String str) {
        this(null, str);
    }

    public DOMAccessException(Exception exc) {
        this(exc, null);
    }

    public DOMAccessException(Exception exc, String str) {
        this.ex = exc;
        this.msg = str;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.msg;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.ex;
    }
}
