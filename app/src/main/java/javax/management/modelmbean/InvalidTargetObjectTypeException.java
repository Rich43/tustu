package javax.management.modelmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;

/* loaded from: rt.jar:javax/management/modelmbean/InvalidTargetObjectTypeException.class */
public class InvalidTargetObjectTypeException extends Exception {
    private static final long oldSerialVersionUID = 3711724570458346634L;
    private static final long newSerialVersionUID = 1190536278266811217L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("msgStr", String.class), new ObjectStreamField("relatedExcept", Exception.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("exception", Exception.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    Exception exception;

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

    public InvalidTargetObjectTypeException() {
        super("InvalidTargetObjectTypeException: ");
        this.exception = null;
    }

    public InvalidTargetObjectTypeException(String str) {
        super("InvalidTargetObjectTypeException: " + str);
        this.exception = null;
    }

    public InvalidTargetObjectTypeException(Exception exc, String str) {
        super("InvalidTargetObjectTypeException: " + str + (exc != null ? "\n\t triggered by:" + exc.toString() : ""));
        this.exception = exc;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.exception = (Exception) fields.get("relatedExcept", (Object) null);
            if (fields.defaulted("relatedExcept")) {
                throw new NullPointerException("relatedExcept");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("relatedExcept", this.exception);
            putFieldPutFields.put("msgStr", this.exception != null ? this.exception.getMessage() : "");
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
