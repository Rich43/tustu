package sun.rmi.transport;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Arrays;
import sun.rmi.transport.tcp.TCPEndpoint;

/* loaded from: rt.jar:sun/rmi/transport/LiveRef.class */
public class LiveRef implements Cloneable {
    private final Endpoint ep;
    private final ObjID id;
    private transient Channel ch;
    private final boolean isLocal;

    public LiveRef(ObjID objID, Endpoint endpoint, boolean z2) {
        this.ep = endpoint;
        this.id = objID;
        this.isLocal = z2;
    }

    public LiveRef(int i2) {
        this(new ObjID(), i2);
    }

    public LiveRef(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        this(new ObjID(), i2, rMIClientSocketFactory, rMIServerSocketFactory);
    }

    public LiveRef(ObjID objID, int i2) {
        this(objID, (Endpoint) TCPEndpoint.getLocalEndpoint(i2), true);
    }

    public LiveRef(ObjID objID, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        this(objID, (Endpoint) TCPEndpoint.getLocalEndpoint(i2, rMIClientSocketFactory, rMIServerSocketFactory), true);
    }

    public Object clone() {
        try {
            return (LiveRef) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    public int getPort() {
        return ((TCPEndpoint) this.ep).getPort();
    }

    public RMIClientSocketFactory getClientSocketFactory() {
        return ((TCPEndpoint) this.ep).getClientSocketFactory();
    }

    public RMIServerSocketFactory getServerSocketFactory() {
        return ((TCPEndpoint) this.ep).getServerSocketFactory();
    }

    public void exportObject(Target target) throws RemoteException {
        this.ep.exportObject(target);
    }

    public Channel getChannel() throws RemoteException {
        if (this.ch == null) {
            this.ch = this.ep.getChannel();
        }
        return this.ch;
    }

    public ObjID getObjID() {
        return this.id;
    }

    Endpoint getEndpoint() {
        return this.ep;
    }

    public String toString() {
        String str;
        if (this.isLocal) {
            str = "local";
        } else {
            str = "remote";
        }
        return "[endpoint:" + ((Object) this.ep) + "(" + str + "),objID:" + ((Object) this.id) + "]";
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof LiveRef)) {
            LiveRef liveRef = (LiveRef) obj;
            return this.ep.equals(liveRef.ep) && this.id.equals(liveRef.id) && this.isLocal == liveRef.isLocal;
        }
        return false;
    }

    public boolean remoteEquals(Object obj) {
        if (obj != null && (obj instanceof LiveRef)) {
            LiveRef liveRef = (LiveRef) obj;
            TCPEndpoint tCPEndpoint = (TCPEndpoint) this.ep;
            TCPEndpoint tCPEndpoint2 = (TCPEndpoint) liveRef.ep;
            RMIClientSocketFactory clientSocketFactory = tCPEndpoint.getClientSocketFactory();
            RMIClientSocketFactory clientSocketFactory2 = tCPEndpoint2.getClientSocketFactory();
            if (tCPEndpoint.getPort() != tCPEndpoint2.getPort() || !tCPEndpoint.getHost().equals(tCPEndpoint2.getHost())) {
                return false;
            }
            if ((clientSocketFactory == null) ^ (clientSocketFactory2 == null)) {
                return false;
            }
            if (clientSocketFactory != null && (clientSocketFactory.getClass() != clientSocketFactory2.getClass() || !clientSocketFactory.equals(clientSocketFactory2))) {
                return false;
            }
            return this.id.equals(liveRef.id);
        }
        return false;
    }

    public void write(ObjectOutput objectOutput, boolean z2) throws IOException {
        Remote impl;
        boolean zIsResultStream = false;
        if (objectOutput instanceof ConnectionOutputStream) {
            ConnectionOutputStream connectionOutputStream = (ConnectionOutputStream) objectOutput;
            zIsResultStream = connectionOutputStream.isResultStream();
            if (this.isLocal) {
                Target target = ObjectTable.getTarget(new ObjectEndpoint(this.id, this.ep.getInboundTransport()));
                if (target != null && (impl = target.getImpl()) != null) {
                    connectionOutputStream.saveObject(impl);
                }
            } else {
                connectionOutputStream.saveObject(this);
            }
        }
        if (z2) {
            ((TCPEndpoint) this.ep).write(objectOutput);
        } else {
            ((TCPEndpoint) this.ep).writeHostPortFormat(objectOutput);
        }
        this.id.write(objectOutput);
        objectOutput.writeBoolean(zIsResultStream);
    }

    public static LiveRef read(ObjectInput objectInput, boolean z2) throws IOException, ClassNotFoundException {
        TCPEndpoint hostPortFormat;
        if (z2) {
            hostPortFormat = TCPEndpoint.read(objectInput);
        } else {
            hostPortFormat = TCPEndpoint.readHostPortFormat(objectInput);
        }
        ObjID objID = ObjID.read(objectInput);
        boolean z3 = objectInput.readBoolean();
        LiveRef liveRef = new LiveRef(objID, (Endpoint) hostPortFormat, false);
        if (objectInput instanceof ConnectionInputStream) {
            ConnectionInputStream connectionInputStream = (ConnectionInputStream) objectInput;
            connectionInputStream.saveRef(liveRef);
            if (z3) {
                connectionInputStream.setAckNeeded();
            }
        } else {
            DGCClient.registerRefs(hostPortFormat, Arrays.asList(liveRef));
        }
        return liveRef;
    }
}
