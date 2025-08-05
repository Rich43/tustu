package com.sun.java.browser.dom;

/* loaded from: rt.jar:com/sun/java/browser/dom/DOMUnsupportedException.class */
public class DOMUnsupportedException extends Exception {
    private Throwable ex;
    private String msg;

    public DOMUnsupportedException() {
        this(null, null);
    }

    public DOMUnsupportedException(String str) {
        this(null, str);
    }

    public DOMUnsupportedException(Exception exc) {
        this(exc, null);
    }

    public DOMUnsupportedException(Exception exc, String str) {
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
