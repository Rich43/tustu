package sun.rmi.transport.proxy;

/* compiled from: CGIHandler.java */
/* loaded from: rt.jar:sun/rmi/transport/proxy/CGIClientException.class */
class CGIClientException extends Exception {
    private static final long serialVersionUID = 8147981687059865216L;

    public CGIClientException(String str) {
        super(str);
    }

    public CGIClientException(String str, Throwable th) {
        super(str, th);
    }
}
