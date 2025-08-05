package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.security.AccessController;
import java.util.logging.Level;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.MBeanNotificationInfo;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanNotificationInfo.class */
public class ModelMBeanNotificationInfo extends MBeanNotificationInfo implements DescriptorAccess {
    private static final long oldSerialVersionUID = -5211564525059047097L;
    private static final long newSerialVersionUID = -7445681389570207141L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("notificationDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("notificationDescriptor", Descriptor.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor notificationDescriptor;
    private static final String currClass = "ModelMBeanNotificationInfo";

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

    public ModelMBeanNotificationInfo(String[] strArr, String str, String str2) {
        this(strArr, str, str2, null);
    }

    public ModelMBeanNotificationInfo(String[] strArr, String str, String str2, Descriptor descriptor) {
        super(strArr, str, str2);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), currClass, "Entry");
        }
        this.notificationDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanNotificationInfo(ModelMBeanNotificationInfo modelMBeanNotificationInfo) {
        this(modelMBeanNotificationInfo.getNotifTypes(), modelMBeanNotificationInfo.getName(), modelMBeanNotificationInfo.getDescription(), modelMBeanNotificationInfo.getDescriptor());
    }

    @Override // javax.management.MBeanNotificationInfo
    public Object clone() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "clone()", "Entry");
        }
        return new ModelMBeanNotificationInfo(this);
    }

    @Override // javax.management.MBeanFeatureInfo, javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "getDescriptor()", "Entry");
        }
        if (this.notificationDescriptor == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "getDescriptor()", "Descriptor value is null, setting descriptor to default values");
            }
            this.notificationDescriptor = validDescriptor(null);
        }
        return (Descriptor) this.notificationDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "setDescriptor(Descriptor)", "Entry");
        }
        this.notificationDescriptor = validDescriptor(descriptor);
    }

    @Override // javax.management.MBeanNotificationInfo
    public String toString() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "toString()", "Entry");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ModelMBeanNotificationInfo: ").append(getName());
        sb.append(" ; Description: ").append(getDescription());
        sb.append(" ; Descriptor: ").append((Object) getDescriptor());
        sb.append(" ; Types: ");
        String[] notifTypes = getNotifTypes();
        for (int i2 = 0; i2 < notifTypes.length; i2++) {
            if (i2 > 0) {
                sb.append(", ");
            }
            sb.append(notifTypes[i2]);
        }
        return sb.toString();
    }

    private Descriptor validDescriptor(Descriptor descriptor) throws RuntimeOperationsException {
        Descriptor descriptorSupport;
        boolean z2 = descriptor == null;
        if (z2) {
            descriptorSupport = new DescriptorSupport();
            JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
        } else {
            descriptorSupport = (Descriptor) descriptor.clone();
        }
        if (z2 && descriptorSupport.getFieldValue("name") == null) {
            descriptorSupport.setField("name", getName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getName());
        }
        if (z2 && descriptorSupport.getFieldValue("descriptorType") == null) {
            descriptorSupport.setField("descriptorType", "notification");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"notification\"");
        }
        if (descriptorSupport.getFieldValue("displayName") == null) {
            descriptorSupport.setField("displayName", getName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
        }
        if (descriptorSupport.getFieldValue("severity") == null) {
            descriptorSupport.setField("severity", "6");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor severity field to 6");
        }
        if (!descriptorSupport.isValid()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + descriptorSupport.toString());
        }
        if (!getName().equalsIgnoreCase((String) descriptorSupport.getFieldValue("name"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + descriptorSupport.getFieldValue("name"));
        }
        if (!"notification".equalsIgnoreCase((String) descriptorSupport.getFieldValue("descriptorType"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"notification\" , was: " + descriptorSupport.getFieldValue("descriptorType"));
        }
        return descriptorSupport;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("notificationDescriptor", this.notificationDescriptor);
            putFieldPutFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
