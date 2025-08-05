package sun.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import sun.rmi.transport.LiveRef;

/* loaded from: rt.jar:sun/rmi/server/UnicastRef2.class */
public class UnicastRef2 extends UnicastRef {
    private static final long serialVersionUID = 1829537514995881838L;

    public UnicastRef2() {
    }

    public UnicastRef2(LiveRef liveRef) {
        super(liveRef);
    }

    @Override // sun.rmi.server.UnicastRef, java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "UnicastRef2";
    }

    @Override // sun.rmi.server.UnicastRef, java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        this.ref.write(objectOutput, true);
    }

    @Override // sun.rmi.server.UnicastRef, java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.ref = LiveRef.read(objectInput, true);
    }
}
