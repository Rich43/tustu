package java.rmi.activation;

import java.io.Serializable;
import java.rmi.MarshalledObject;

/* loaded from: rt.jar:java/rmi/activation/ActivationDesc.class */
public final class ActivationDesc implements Serializable {
    private ActivationGroupID groupID;
    private String className;
    private String location;
    private MarshalledObject<?> data;
    private boolean restart;
    private static final long serialVersionUID = 7455834104417690957L;

    public ActivationDesc(String str, String str2, MarshalledObject<?> marshalledObject) throws ActivationException {
        this(ActivationGroup.internalCurrentGroupID(), str, str2, marshalledObject, false);
    }

    public ActivationDesc(String str, String str2, MarshalledObject<?> marshalledObject, boolean z2) throws ActivationException {
        this(ActivationGroup.internalCurrentGroupID(), str, str2, marshalledObject, z2);
    }

    public ActivationDesc(ActivationGroupID activationGroupID, String str, String str2, MarshalledObject<?> marshalledObject) {
        this(activationGroupID, str, str2, marshalledObject, false);
    }

    public ActivationDesc(ActivationGroupID activationGroupID, String str, String str2, MarshalledObject<?> marshalledObject, boolean z2) {
        if (activationGroupID == null) {
            throw new IllegalArgumentException("groupID can't be null");
        }
        this.groupID = activationGroupID;
        this.className = str;
        this.location = str2;
        this.data = marshalledObject;
        this.restart = z2;
    }

    public ActivationGroupID getGroupID() {
        return this.groupID;
    }

    public String getClassName() {
        return this.className;
    }

    public String getLocation() {
        return this.location;
    }

    public MarshalledObject<?> getData() {
        return this.data;
    }

    public boolean getRestartMode() {
        return this.restart;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ActivationDesc) {
            ActivationDesc activationDesc = (ActivationDesc) obj;
            if (this.groupID != null ? this.groupID.equals(activationDesc.groupID) : activationDesc.groupID == null) {
                if (this.className != null ? this.className.equals(activationDesc.className) : activationDesc.className == null) {
                    if (this.location != null ? this.location.equals(activationDesc.location) : activationDesc.location == null) {
                        if (this.data != null ? this.data.equals(activationDesc.data) : activationDesc.data == null) {
                            if (this.restart == activationDesc.restart) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.location == null ? 0 : this.location.hashCode() << 24) ^ (this.groupID == null ? 0 : this.groupID.hashCode() << 16)) ^ (this.className == null ? 0 : this.className.hashCode() << 9)) ^ (this.data == null ? 0 : this.data.hashCode() << 1)) ^ (this.restart ? 1 : 0);
    }
}
