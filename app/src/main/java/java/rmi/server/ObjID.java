package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.AccessController;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/rmi/server/ObjID.class */
public final class ObjID implements Serializable {
    public static final int REGISTRY_ID = 0;
    public static final int ACTIVATOR_ID = 1;
    public static final int DGC_ID = 2;
    private static final long serialVersionUID = -6386392263968365220L;
    private static final AtomicLong nextObjNum = new AtomicLong(0);
    private static final UID mySpace = new UID();
    private static final SecureRandom secureRandom = new SecureRandom();
    private final long objNum;
    private final UID space;

    public ObjID() {
        if (useRandomIDs()) {
            this.space = new UID();
            this.objNum = secureRandom.nextLong();
        } else {
            this.space = mySpace;
            this.objNum = nextObjNum.getAndIncrement();
        }
    }

    public ObjID(int i2) {
        this.space = new UID((short) 0);
        this.objNum = i2;
    }

    private ObjID(long j2, UID uid) {
        this.objNum = j2;
        this.space = uid;
    }

    public void write(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeLong(this.objNum);
        this.space.write(objectOutput);
    }

    public static ObjID read(ObjectInput objectInput) throws IOException {
        return new ObjID(objectInput.readLong(), UID.read(objectInput));
    }

    public int hashCode() {
        return (int) this.objNum;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjID) {
            ObjID objID = (ObjID) obj;
            return this.objNum == objID.objNum && this.space.equals(objID.space);
        }
        return false;
    }

    public String toString() {
        return "[" + (this.space.equals(mySpace) ? "" : ((Object) this.space) + ", ") + this.objNum + "]";
    }

    private static boolean useRandomIDs() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.randomIDs"));
        if (str == null) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }
}
