package sun.rmi.server;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutput;
import java.rmi.activation.ActivationID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteRef;
import sun.rmi.transport.LiveRef;

/* loaded from: rt.jar:sun/rmi/server/ActivatableServerRef.class */
public class ActivatableServerRef extends UnicastServerRef2 {
    private static final long serialVersionUID = 2002967993223003793L;
    private ActivationID id;

    public ActivatableServerRef(ActivationID activationID, int i2) {
        this(activationID, i2, null, null);
    }

    public ActivatableServerRef(ActivationID activationID, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        super(new LiveRef(i2, rMIClientSocketFactory, rMIServerSocketFactory));
        this.id = activationID;
    }

    @Override // sun.rmi.server.UnicastServerRef2, sun.rmi.server.UnicastServerRef, sun.rmi.server.UnicastRef, java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "ActivatableServerRef";
    }

    @Override // sun.rmi.server.UnicastServerRef2, sun.rmi.server.UnicastServerRef
    protected RemoteRef getClientRef() {
        return new ActivatableRef(this.id, new UnicastRef2(this.ref));
    }

    @Override // sun.rmi.server.UnicastServerRef, sun.rmi.server.UnicastRef, java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        throw new NotSerializableException("ActivatableServerRef not serializable");
    }
}
