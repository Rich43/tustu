package javax.management.modelmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/management/modelmbean/XMLParseException.class */
public class XMLParseException extends Exception {
    private static final long oldSerialVersionUID = -7780049316655891976L;
    private static final long newSerialVersionUID = 3176664577895105181L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("msgStr", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = new ObjectStreamField[0];
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;

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

    public XMLParseException() {
        super("XML Parse Exception.");
    }

    public XMLParseException(String str) {
        super("XML Parse Exception: " + str);
    }

    public XMLParseException(Exception exc, String str) {
        super("XML Parse Exception: " + str + CallSiteDescriptor.TOKEN_DELIMITER + exc.toString());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            objectOutputStream.putFields().put("msgStr", getMessage());
            objectOutputStream.writeFields();
        } else {
            objectOutputStream.defaultWriteObject();
        }
    }
}
