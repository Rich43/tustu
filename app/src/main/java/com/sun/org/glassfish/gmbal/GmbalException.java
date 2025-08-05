package com.sun.org.glassfish.gmbal;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/GmbalException.class */
public class GmbalException extends RuntimeException {
    private static final long serialVersionUID = -7478444176079980162L;

    public GmbalException(String msg) {
        super(msg);
    }

    public GmbalException(String msg, Throwable thr) {
        super(msg, thr);
    }
}
