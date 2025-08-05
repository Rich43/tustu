package java.rmi.activation;

import java.io.Serializable;
import java.rmi.server.UID;

/* loaded from: rt.jar:java/rmi/activation/ActivationGroupID.class */
public class ActivationGroupID implements Serializable {
    private ActivationSystem system;
    private UID uid = new UID();
    private static final long serialVersionUID = -1648432278909740833L;

    public ActivationGroupID(ActivationSystem activationSystem) {
        this.system = activationSystem;
    }

    public ActivationSystem getSystem() {
        return this.system;
    }

    public int hashCode() {
        return this.uid.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ActivationGroupID) {
            ActivationGroupID activationGroupID = (ActivationGroupID) obj;
            return this.uid.equals(activationGroupID.uid) && this.system.equals(activationGroupID.system);
        }
        return false;
    }
}
