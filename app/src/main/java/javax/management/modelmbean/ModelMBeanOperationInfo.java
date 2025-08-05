package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.logging.Level;
import javax.management.Descriptor;
import javax.management.DescriptorAccess;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanOperationInfo.class */
public class ModelMBeanOperationInfo extends MBeanOperationInfo implements DescriptorAccess {
    private static final long oldSerialVersionUID = 9087646304346171239L;
    private static final long newSerialVersionUID = 6532732096650090465L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("operationDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("operationDescriptor", Descriptor.class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor operationDescriptor;
    private static final String currClass = "ModelMBeanOperationInfo";

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

    public ModelMBeanOperationInfo(String str, Method method) {
        super(str, method);
        this.operationDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,Method)", "Entry");
        }
        this.operationDescriptor = validDescriptor(null);
    }

    public ModelMBeanOperationInfo(String str, Method method, Descriptor descriptor) {
        super(str, method);
        this.operationDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,Method,Descriptor)", "Entry");
        }
        this.operationDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i2) {
        super(str, str2, mBeanParameterInfoArr, str3, i2);
        this.operationDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int)", "Entry");
        }
        this.operationDescriptor = validDescriptor(null);
    }

    public ModelMBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i2, Descriptor descriptor) {
        super(str, str2, mBeanParameterInfoArr, str3, i2);
        this.operationDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int,Descriptor)", "Entry");
        }
        this.operationDescriptor = validDescriptor(descriptor);
    }

    public ModelMBeanOperationInfo(ModelMBeanOperationInfo modelMBeanOperationInfo) {
        super(modelMBeanOperationInfo.getName(), modelMBeanOperationInfo.getDescription(), modelMBeanOperationInfo.getSignature(), modelMBeanOperationInfo.getReturnType(), modelMBeanOperationInfo.getImpact());
        this.operationDescriptor = validDescriptor(null);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(ModelMBeanOperationInfo)", "Entry");
        }
        this.operationDescriptor = validDescriptor(modelMBeanOperationInfo.getDescriptor());
    }

    @Override // javax.management.MBeanOperationInfo
    public Object clone() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "clone()", "Entry");
        }
        return new ModelMBeanOperationInfo(this);
    }

    @Override // javax.management.MBeanFeatureInfo, javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "getDescriptor()", "Entry");
        }
        if (this.operationDescriptor == null) {
            this.operationDescriptor = validDescriptor(null);
        }
        return (Descriptor) this.operationDescriptor.clone();
    }

    @Override // javax.management.DescriptorAccess
    public void setDescriptor(Descriptor descriptor) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "setDescriptor(Descriptor)", "Entry");
        }
        this.operationDescriptor = validDescriptor(descriptor);
    }

    @Override // javax.management.MBeanOperationInfo
    public String toString() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "toString()", "Entry");
        }
        String strConcat = "ModelMBeanOperationInfo: " + getName() + " ; Description: " + getDescription() + " ; Descriptor: " + ((Object) getDescriptor()) + " ; ReturnType: " + getReturnType() + " ; Signature: ";
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
            descriptorSupport.setField(SOAP12NamespaceConstants.ATTR_ACTOR, "operation");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor role field to \"operation\"");
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
        String str = (String) descriptorSupport.getFieldValue(SOAP12NamespaceConstants.ATTR_ACTOR);
        if (!str.equalsIgnoreCase("operation") && !str.equalsIgnoreCase("setter") && !str.equalsIgnoreCase("getter")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"role\" field does not match the object described.  Expected: \"operation\", \"setter\", or \"getter\" , was: " + descriptorSupport.getFieldValue(SOAP12NamespaceConstants.ATTR_ACTOR));
        }
        Object fieldValue = descriptorSupport.getFieldValue("targetType");
        if (fieldValue != null && !(fieldValue instanceof String)) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor field \"targetValue\" is invalid class.  Expected: java.lang.String,  was: " + fieldValue.getClass().getName());
        }
        return descriptorSupport;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("operationDescriptor", this.operationDescriptor);
            putFieldPutFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
