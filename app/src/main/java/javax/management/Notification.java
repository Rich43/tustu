package javax.management;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.Date;
import java.util.EventObject;

/* loaded from: rt.jar:javax/management/Notification.class */
public class Notification extends EventObject {
    private static final long oldSerialVersionUID = 1716977971058914352L;
    private static final long newSerialVersionUID = -7516092053498031989L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("message", String.class), new ObjectStreamField("sequenceNumber", Long.TYPE), new ObjectStreamField("source", Object.class), new ObjectStreamField("sourceObjectName", ObjectName.class), new ObjectStreamField("timeStamp", Long.TYPE), new ObjectStreamField("type", String.class), new ObjectStreamField("userData", Object.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("message", String.class), new ObjectStreamField("sequenceNumber", Long.TYPE), new ObjectStreamField("source", Object.class), new ObjectStreamField("timeStamp", Long.TYPE), new ObjectStreamField("type", String.class), new ObjectStreamField("userData", Object.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private String type;
    private long sequenceNumber;
    private long timeStamp;
    private Object userData;
    private String message;
    protected Object source;

    static {
        compat = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form"));
            compat = str != null && str.equals("1.0");
        } catch (Exception e2) {
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
    }

    public Notification(String str, Object obj, long j2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j2;
        this.timeStamp = new Date().getTime();
    }

    public Notification(String str, Object obj, long j2, String str2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j2;
        this.timeStamp = new Date().getTime();
        this.message = str2;
    }

    public Notification(String str, Object obj, long j2, long j3) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j2;
        this.timeStamp = j3;
    }

    public Notification(String str, Object obj, long j2, long j3, String str2) {
        super(obj);
        this.userData = null;
        this.message = "";
        this.source = null;
        this.source = obj;
        this.type = str;
        this.sequenceNumber = j2;
        this.timeStamp = j3;
        this.message = str2;
    }

    public void setSource(Object obj) {
        super.source = obj;
        this.source = obj;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long j2) {
        this.sequenceNumber = j2;
    }

    public String getType() {
        return this.type;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j2) {
        this.timeStamp = j2;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getUserData() {
        return this.userData;
    }

    public void setUserData(Object obj) {
        this.userData = obj;
    }

    @Override // java.util.EventObject
    public String toString() {
        return super.toString() + "[type=" + this.type + "][message=" + this.message + "]";
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        super.source = this.source;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("type", this.type);
            putFieldPutFields.put("sequenceNumber", this.sequenceNumber);
            putFieldPutFields.put("timeStamp", this.timeStamp);
            putFieldPutFields.put("userData", this.userData);
            putFieldPutFields.put("message", this.message);
            putFieldPutFields.put("source", this.source);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
