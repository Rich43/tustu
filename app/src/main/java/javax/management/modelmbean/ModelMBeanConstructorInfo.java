package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.util.logging.Level;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanConstructorInfo.class */
public class ModelMBeanConstructorInfo extends MBeanConstructorInfo implements DescriptorAccess {
    private static final long oldSerialVersionUID = -4440125391095574518L;
    private static final long newSerialVersionUID = 3862947819818064362L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("consDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("consDescriptor", Descriptor.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor consDescriptor;
    private static final String currClass = "ModelMBeanConstructorInfo";

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

    public ModelMBeanConstructorInfo(String str, Constructor<?> constructor) {
        super(str, constructor);
        this.consDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,Constructor)", "Entry");
        }
        this.consDescriptor = validDescriptor(null);
    }

    public ModelMBeanConstructorInfo(String str, Constructor<?> constructor, Descriptor descriptor) {
        super(str, constructor);
        this.consDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,Constructor,Descriptor)", "Entry");
        }
        this.consDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr) {
        super(str, str2, mBeanParameterInfoArr);
        this.consDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,String,MBeanParameterInfo[])", "Entry");
        }
        this.consDescriptor = validDescriptor(null);
    }

    public ModelMBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, Descriptor descriptor) {
        super(str, str2, mBeanParameterInfoArr);
        this.consDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,String,MBeanParameterInfo[],Descriptor)", "Entry");
        }
        this.consDescriptor = validDescriptor(descriptor);
    }

    ModelMBeanConstructorInfo(ModelMBeanConstructorInfo modelMBeanConstructorInfo) {
        super(modelMBeanConstructorInfo.getName(), modelMBeanConstructorInfo.getDescription(), modelMBeanConstructorInfo.getSignature());
        this.consDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(ModelMBeanConstructorInfo)", "Entry");
        }
        this.consDescriptor = validDescriptor(this.consDescriptor);
    }

    @Override // javax.management.MBeanConstructorInfo
    public Object clone() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "clone()", "Entry");
        }
        return new ModelMBeanConstructorInfo(this);
    }

    @Override // javax.management.MBeanFeatureInfo, javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "getDescriptor()", "Entry");
        }
        if (this.consDescriptor == null) {
            this.consDescriptor = validDescriptor(null);
        }
        return (Descriptor) this.consDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "setDescriptor()", "Entry");
        }
        this.consDescriptor = validDescriptor(descriptor);
    }

    @Override // javax.management.MBeanConstructorInfo
    public String toString() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "toString()", "Entry");
        }
        String strConcat = "ModelMBeanConstructorInfo: " + getName() + " ; Description: " + getDescription() + " ; Descriptor: " + ((Object) getDescriptor()) + " ; Signature: ";
        for (MBeanParameterInfo mBeanParameterInfo : getSignature()) {
            strConcat = strConcat.concat(mBeanParameterInfo.getType() + ", ");
        }
        return strConcat;
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
            descriptorSupport.setField("descriptorType", "operation");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"operation\"");
        }
        if (descriptorSupport.getFieldValue("displayName") == null) {
            descriptorSupport.setField("displayName", getName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
        }
        if (descriptorSupport.getFieldValue(SOAP12NamespaceConstants.ATTR_ACTOR) == null) {
            descriptorSupport.setField(SOAP12NamespaceConstants.ATTR_ACTOR, "constructor");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor role field to \"constructor\"");
        }
        if (!descriptorSupport.isValid()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + descriptorSupport.toString());
        }
        if (!getName().equalsIgnoreCase((String) descriptorSupport.getFieldValue("name"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + descriptorSupport.getFieldValue("name"));
        }
        if (!"operation".equalsIgnoreCase((String) descriptorSupport.getFieldValue("descriptorType"))) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"operation\" , was: " + descriptorSupport.getFieldValue("descriptorType"));
        }
        if (!((String) descriptorSupport.getFieldValue(SOAP12NamespaceConstants.ATTR_ACTOR)).equalsIgnoreCase("constructor")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"role\" field does not match the object described.  Expected: \"constructor\" , was: " + descriptorSupport.getFieldValue(SOAP12NamespaceConstants.ATTR_ACTOR));
        }
        return descriptorSupport;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("consDescriptor", this.consDescriptor);
            putFieldPutFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
