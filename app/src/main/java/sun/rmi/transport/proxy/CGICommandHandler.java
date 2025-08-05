package sun.rmi.transport.proxy;

/* compiled from: CGIHandler.java */
/* loaded from: rt.jar:sun/rmi/transport/proxy/CGICommandHandler.class */
interface CGICommandHandler {
    String getName();

    void execute(String str) throws CGIClientException, CGIServerException;
}
