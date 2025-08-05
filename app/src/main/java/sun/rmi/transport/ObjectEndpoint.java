package sun.rmi.transport;

import java.rmi.server.ObjID;

/* loaded from: rt.jar:sun/rmi/transport/ObjectEndpoint.class */
class ObjectEndpoint {
    private final ObjID id;
    private final Transport transport;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ObjectEndpoint.class.desiredAssertionStatus();
    }

    ObjectEndpoint(ObjID objID, Transport transport) {
        if (objID == null) {
            throw new NullPointerException();
        }
        if (!$assertionsDisabled && transport == null && !objID.equals(new ObjID(2))) {
            throw new AssertionError();
        }
        this.id = objID;
        this.transport = transport;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjectEndpoint) {
            ObjectEndpoint objectEndpoint = (ObjectEndpoint) obj;
            return this.id.equals(objectEndpoint.id) && this.transport == objectEndpoint.transport;
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode() ^ (this.transport != null ? this.transport.hashCode() : 0);
    }

    public String toString() {
        return this.id.toString();
    }
}
