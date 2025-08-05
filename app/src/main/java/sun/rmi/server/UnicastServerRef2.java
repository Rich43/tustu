package sun.rmi.server;

import java.io.ObjectOutput;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteRef;
import sun.misc.ObjectInputFilter;
import sun.rmi.transport.LiveRef;

/* loaded from: rt.jar:sun/rmi/server/UnicastServerRef2.class */
public class UnicastServerRef2 extends UnicastServerRef {
    private static final long serialVersionUID = -2289703812660767614L;

    public UnicastServerRef2() {
    }

    public UnicastServerRef2(LiveRef liveRef) {
        super(liveRef);
    }

    public UnicastServerRef2(LiveRef liveRef, ObjectInputFilter objectInputFilter) {
        super(liveRef, objectInputFilter);
    }

    public UnicastServerRef2(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) {
        super(new LiveRef(i2, rMIClientSocketFactory, rMIServerSocketFactory));
    }

    public UnicastServerRef2(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory, ObjectInputFilter objectInputFilter) {
        super(new LiveRef(i2, rMIClientSocketFactory, rMIServerSocketFactory), objectInputFilter);
    }

    @Override // sun.rmi.server.UnicastServerRef, sun.rmi.server.UnicastRef, java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "UnicastServerRef2";
    }

    @Override // sun.rmi.server.UnicastServerRef
    protected RemoteRef getClientRef() {
        return new UnicastRef2(this.ref);
    }
}
