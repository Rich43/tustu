package java.rmi.server;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/RemoteStub.class */
public abstract class RemoteStub extends RemoteObject {
    private static final long serialVersionUID = -1585587260594494182L;

    protected RemoteStub() {
    }

    protected RemoteStub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    @Deprecated
    protected static void setRef(RemoteStub remoteStub, RemoteRef remoteRef) {
        throw new UnsupportedOperationException();
    }
}
