package sun.rmi.transport;

import java.rmi.RemoteException;

/* loaded from: rt.jar:sun/rmi/transport/Endpoint.class */
public interface Endpoint {
    Channel getChannel();

    void exportObject(Target target) throws RemoteException;

    Transport getInboundTransport();

    Transport getOutboundTransport();
}
