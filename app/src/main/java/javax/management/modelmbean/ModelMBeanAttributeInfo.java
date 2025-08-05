package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.logging.Level;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanAttributeInfo.class */
public class ModelMBeanAttributeInfo extends MBeanAttributeInfo implements DescriptorAccess {
    private static final long oldSerialVersionUID = 7098036920755973145L;
    private static final long newSerialVersionUID = 6181543027787327345L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("attrDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("attrDescriptor", Descriptor.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor attrDescriptor;
    private static final String currClass = "ModelMBeanAttributeInfo";

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

    public ModelMBeanAttributeInfo(String str, String str2, Method method, Method method2) throws IntrospectionException {
        super(str, str2, method, method2);
        this.attrDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,Method,Method)", "Entry", str);
        }
        this.attrDescriptor = validDescriptor(null);
    }

    public ModelMBeanAttributeInfo(String str, String str2, Method method, Method method2, Descriptor descriptor) throws IntrospectionException {
        super(str, str2, method, method2);
        this.attrDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,Method,Method,Descriptor)", "Entry", str);
        }
        this.attrDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanAttributeInfo(String str, String str2, String str3, boolean z2, boolean z3, boolean z4) {
        super(str, str2, str3, z2, z3, z4);
        this.attrDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,String,boolean,boolean,boolean)", "Entry", str);
        }
        this.attrDescriptor = validDescriptor(null);
    }

    public ModelMBeanAttributeInfo(String str, String str2, String str3, boolean z2, boolean z3, boolean z4, Descriptor descriptor) {
        super(str, str2, str3, z2, z3, z4);
        this.attrDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,String,boolean,boolean,boolean,Descriptor)", "Entry", str);
        }
        this.attrDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanAttributeInfo(ModelMBeanAttributeInfo modelMBeanAttributeInfo) {
        super(modelMBeanAttributeInfo.getName(), modelMBeanAttributeInfo.getType(), modelMBeanAttributeInfo.getDescription(), modelMBeanAttributeInfo.isReadable(), modelMBeanAttributeInfo.isWritable(), modelMBeanAttributeInfo.isIs());
        this.attrDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(ModelMBeanAttributeInfo)", "Entry");
        }
        this.attrDescriptor = validDescriptor(modelMBeanAttributeInfo.getDescriptor());
    }

    @Override // javax.management.MBeanFeatureInfo, javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "getDescriptor()", "Entry");
        }
        if (this.attrDescriptor == null) {
            this.attrDescriptor = validDescriptor(null);
        }
        return (Descriptor) this.attrDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        this.attrDescriptor = validDescriptor(descriptor);
    }

    @Override // javax.management.MBeanAttributeInfo
    public Object clone() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "clone()", "Entry");
        }
        return new ModelMBeanAttributeInfo(this);
    }

    @Override // javax.management.MBeanAttributeInfo
    public String toString() {
        return "ModelMBeanAttributeInfo: " + getName() + " ; Description: " + getDescription() + " ; Types: " + getType() + " ; isReadable: " + isReadable() + " ; isWritable: " + isWritable() + " ; Descriptor: " + ((Object) getDescriptor());
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
            descriptorSupport.setField("descriptorType", "attribute");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"attribute\"");
        }
        if (descriptorSupport.getFieldValue("displayName") == null) {
            descriptorSupport.setField("displayName", getName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
        }
        if (!descriptorSupport.isValid()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + descriptorSupport.toString());
        }
        if (!getName().equalsIgnoreCase((String) descriptorSupport.getFieldValue("name"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + descriptorSupport.getFieldValue("name"));
        }
        if (!"attribute".equalsIgnoreCase((String) descriptorSupport.getFieldValue("descriptorType"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"attribute\" , was: " + descriptorSupport.getFieldValue("descriptorType"));
        }
        return descriptorSupport;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("attrDescriptor", this.attrDescriptor);
            putFieldPutFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
