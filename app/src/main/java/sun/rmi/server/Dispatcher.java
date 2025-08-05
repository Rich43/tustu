package sun.rmi.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.server.RemoteCall;

/* loaded from: rt.jar:sun/rmi/server/Dispatcher.class */
public interface Dispatcher {
    void dispatch(Remote remote, RemoteCall remoteCall) throws IOException;
}
