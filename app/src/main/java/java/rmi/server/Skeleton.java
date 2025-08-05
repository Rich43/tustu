package java.rmi.server;

import java.rmi.Remote;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/Skeleton.class */
public interface Skeleton {
    @Deprecated
    void dispatch(Remote remote, RemoteCall remoteCall, int i2, long j2) throws Exception;

    @Deprecated
    Operation[] getOperations();
}
