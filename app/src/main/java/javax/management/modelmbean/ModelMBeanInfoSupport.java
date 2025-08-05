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
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.RuntimeOperationsException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: rt.jar:javax/management/modelmbean/ModelMBeanInfoSupport.class */
public class ModelMBeanInfoSupport extends MBeanInfo implements ModelMBeanInfo {
    private static final long oldSerialVersionUID = -3944083498453227709L;
    private static final long newSerialVersionUID = -1935722590756516193L;
    private static final ObjectStreamField[] oldSerialPersistentFields = {new ObjectStreamField("modelMBeanDescriptor", Descriptor.class), new ObjectStreamField("mmbAttributes", MBeanAttributeInfo[].class), new ObjectStreamField("mmbConstructors", MBeanConstructorInfo[].class), new ObjectStreamField("mmbNotifications", MBeanNotificationInfo[].class), new ObjectStreamField("mmbOperations", MBeanOperationInfo[].class), new ObjectStreamField("currClass", String.class)};
    private static final ObjectStreamField[] newSerialPersistentFields = {new ObjectStreamField("modelMBeanDescriptor", Descriptor.class), new ObjectStreamField("modelMBeanAttributes", MBeanAttributeInfo[].class), new ObjectStreamField("modelMBeanConstructors", MBeanConstructorInfo[].class), new ObjectStreamField("modelMBeanNotifications", MBeanNotificationInfo[].class), new ObjectStreamField("modelMBeanOperations", MBeanOperationInfo[].class)};
    private static final long serialVersionUID;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat;
    private Descriptor modelMBeanDescriptor;
    private MBeanAttributeInfo[] modelMBeanAttributes;
    private MBeanConstructorInfo[] modelMBeanConstructors;
    private MBeanNotificationInfo[] modelMBeanNotifications;
    private MBeanOperationInfo[] modelMBeanOperations;
    private static final String ATTR = "attribute";
    private static final String OPER = "operation";
    private static final String NOTF = "notification";
    private static final String CONS = "constructor";
    private static final String MMB = "mbean";
    private static final String ALL = "all";
    private static final String currClass = "ModelMBeanInfoSupport";
    private static final ModelMBeanAttributeInfo[] NO_ATTRIBUTES;
    private static final ModelMBeanConstructorInfo[] NO_CONSTRUCTORS;
    private static final ModelMBeanNotificationInfo[] NO_NOTIFICATIONS;
    private static final ModelMBeanOperationInfo[] NO_OPERATIONS;

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
        NO_ATTRIBUTES = new ModelMBeanAttributeInfo[0];
        NO_CONSTRUCTORS = new ModelMBeanConstructorInfo[0];
        NO_NOTIFICATIONS = new ModelMBeanNotificationInfo[0];
        NO_OPERATIONS = new ModelMBeanOperationInfo[0];
    }

    public ModelMBeanInfoSupport(ModelMBeanInfo modelMBeanInfo) {
        super(modelMBeanInfo.getClassName(), modelMBeanInfo.getDescription(), modelMBeanInfo.getAttributes(), modelMBeanInfo.getConstructors(), modelMBeanInfo.getOperations(), modelMBeanInfo.getNotifications());
        this.modelMBeanDescriptor = null;
        this.modelMBeanAttributes = modelMBeanInfo.getAttributes();
        this.modelMBeanConstructors = modelMBeanInfo.getConstructors();
        this.modelMBeanOperations = modelMBeanInfo.getOperations();
        this.modelMBeanNotifications = modelMBeanInfo.getNotifications();
        try {
            this.modelMBeanDescriptor = validDescriptor(modelMBeanInfo.getMBeanDescriptor());
        } catch (MBeanException e2) {
            this.modelMBeanDescriptor = validDescriptor(null);
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfo(ModelMBeanInfo)", "Could not get a valid modelMBeanDescriptor, setting a default Descriptor");
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfo(ModelMBeanInfo)", ToolWindow.QUIT);
        }
    }

    public ModelMBeanInfoSupport(String str, String str2, ModelMBeanAttributeInfo[] modelMBeanAttributeInfoArr, ModelMBeanConstructorInfo[] modelMBeanConstructorInfoArr, ModelMBeanOperationInfo[] modelMBeanOperationInfoArr, ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr) {
        this(str, str2, modelMBeanAttributeInfoArr, modelMBeanConstructorInfoArr, modelMBeanOperationInfoArr, modelMBeanNotificationInfoArr, null);
    }

    public ModelMBeanInfoSupport(String str, String str2, ModelMBeanAttributeInfo[] modelMBeanAttributeInfoArr, ModelMBeanConstructorInfo[] modelMBeanConstructorInfoArr, ModelMBeanOperationInfo[] modelMBeanOperationInfoArr, ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr, Descriptor descriptor) {
        super(str, str2, modelMBeanAttributeInfoArr != null ? modelMBeanAttributeInfoArr : NO_ATTRIBUTES, modelMBeanConstructorInfoArr != null ? modelMBeanConstructorInfoArr : NO_CONSTRUCTORS, modelMBeanOperationInfoArr != null ? modelMBeanOperationInfoArr : NO_OPERATIONS, modelMBeanNotificationInfoArr != null ? modelMBeanNotificationInfoArr : NO_NOTIFICATIONS);
        this.modelMBeanDescriptor = null;
        this.modelMBeanAttributes = modelMBeanAttributeInfoArr;
        this.modelMBeanConstructors = modelMBeanConstructorInfoArr;
        this.modelMBeanOperations = modelMBeanOperationInfoArr;
        this.modelMBeanNotifications = modelMBeanNotificationInfoArr;
        this.modelMBeanDescriptor = validDescriptor(descriptor);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfoSupport(String,String,ModelMBeanAttributeInfo[],ModelMBeanConstructorInfo[],ModelMBeanOperationInfo[],ModelMBeanNotificationInfo[],Descriptor)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.MBeanInfo, javax.management.modelmbean.ModelMBeanInfo
    public Object clone() {
        return new ModelMBeanInfoSupport(this);
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor[] getDescriptors(String str) throws MBeanException, RuntimeOperationsException {
        Descriptor[] descriptorArr;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptors(String)", "Entry");
        }
        if (str == null || str.equals("")) {
            str = "all";
        }
        if (str.equalsIgnoreCase(MMB)) {
            descriptorArr = new Descriptor[]{this.modelMBeanDescriptor};
        } else if (str.equalsIgnoreCase("attribute")) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
            int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
            descriptorArr = new Descriptor[length];
            for (int i2 = 0; i2 < length; i2++) {
                descriptorArr[i2] = ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i2]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(OPER)) {
            MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
            int length2 = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
            descriptorArr = new Descriptor[length2];
            for (int i3 = 0; i3 < length2; i3++) {
                descriptorArr[i3] = ((ModelMBeanOperationInfo) mBeanOperationInfoArr[i3]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(CONS)) {
            MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
            int length3 = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
            descriptorArr = new Descriptor[length3];
            for (int i4 = 0; i4 < length3; i4++) {
                descriptorArr[i4] = ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i4]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase(NOTF)) {
            MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
            int length4 = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
            descriptorArr = new Descriptor[length4];
            for (int i5 = 0; i5 < length4; i5++) {
                descriptorArr[i5] = ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i5]).getDescriptor();
            }
        } else if (str.equalsIgnoreCase("all")) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr2 = this.modelMBeanAttributes;
            int length5 = mBeanAttributeInfoArr2 != null ? mBeanAttributeInfoArr2.length : 0;
            MBeanOperationInfo[] mBeanOperationInfoArr2 = this.modelMBeanOperations;
            int length6 = mBeanOperationInfoArr2 != null ? mBeanOperationInfoArr2.length : 0;
            MBeanConstructorInfo[] mBeanConstructorInfoArr2 = this.modelMBeanConstructors;
            int length7 = mBeanConstructorInfoArr2 != null ? mBeanConstructorInfoArr2.length : 0;
            MBeanNotificationInfo[] mBeanNotificationInfoArr2 = this.modelMBeanNotifications;
            int length8 = mBeanNotificationInfoArr2 != null ? mBeanNotificationInfoArr2.length : 0;
            int i6 = length5 + length7 + length6 + length8 + 1;
            descriptorArr = new Descriptor[i6];
            descriptorArr[i6 - 1] = this.modelMBeanDescriptor;
            int i7 = 0;
            for (int i8 = 0; i8 < length5; i8++) {
                descriptorArr[i7] = ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr2[i8]).getDescriptor();
                i7++;
            }
            for (int i9 = 0; i9 < length7; i9++) {
                descriptorArr[i7] = ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr2[i9]).getDescriptor();
                i7++;
            }
            for (int i10 = 0; i10 < length6; i10++) {
                descriptorArr[i7] = ((ModelMBeanOperationInfo) mBeanOperationInfoArr2[i10]).getDescriptor();
                i7++;
            }
            for (int i11 = 0; i11 < length8; i11++) {
                descriptorArr[i7] = ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr2[i11]).getDescriptor();
                i7++;
            }
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occurred trying to find the descriptors of the MBean");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptors(String)", ToolWindow.QUIT);
        }
        return descriptorArr;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setDescriptors(Descriptor[] descriptorArr) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptors(Descriptor[])", "Entry");
        }
        if (descriptorArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor list is invalid"), "Exception occurred trying to set the descriptors of the MBeanInfo");
        }
        if (descriptorArr.length == 0) {
            return;
        }
        for (Descriptor descriptor : descriptorArr) {
            setDescriptor(descriptor, null);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptors(Descriptor[])", ToolWindow.QUIT);
        }
    }

    public Descriptor getDescriptor(String str) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptor(String)", "Entry");
        }
        return getDescriptor(str, null);
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor getDescriptor(String str, String str2) throws MBeanException, RuntimeOperationsException {
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor is invalid"), "Exception occurred trying to set the descriptors of the MBeanInfo");
        }
        if (MMB.equalsIgnoreCase(str2)) {
            return (Descriptor) this.modelMBeanDescriptor.clone();
        }
        if ("attribute".equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanAttributeInfo attribute = getAttribute(str);
            if (attribute != null) {
                return attribute.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (OPER.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanOperationInfo operation = getOperation(str);
            if (operation != null) {
                return operation.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (CONS.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanConstructorInfo constructor = getConstructor(str);
            if (constructor != null) {
                return constructor.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (NOTF.equalsIgnoreCase(str2) || str2 == null) {
            ModelMBeanNotificationInfo notification = getNotification(str);
            if (notification != null) {
                return notification.getDescriptor();
            }
            if (str2 != null) {
                return null;
            }
        }
        if (str2 == null) {
            return null;
        }
        throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occurred trying to find the descriptors of the MBean");
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setDescriptor(Descriptor descriptor, String str) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "Entry");
        }
        if (descriptor == null) {
            descriptor = new DescriptorSupport();
        }
        if (str == null || str.equals("")) {
            str = (String) descriptor.getFieldValue("descriptorType");
            if (str == null) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "descriptorType null in both String parameter and Descriptor, defaulting to mbean");
                str = MMB;
            }
        }
        String className = (String) descriptor.getFieldValue("name");
        if (className == null) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "descriptor name null, defaulting to " + getClassName());
            className = getClassName();
        }
        boolean z2 = false;
        if (str.equalsIgnoreCase(MMB)) {
            setMBeanDescriptor(descriptor);
            z2 = true;
        } else if (str.equalsIgnoreCase("attribute")) {
            MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
            int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (className.equals(mBeanAttributeInfoArr[i2].getName())) {
                    z2 = true;
                    ModelMBeanAttributeInfo modelMBeanAttributeInfo = (ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i2];
                    modelMBeanAttributeInfo.setDescriptor(descriptor);
                    if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "Setting descriptor to " + ((Object) descriptor) + "\t\n local: AttributeInfo descriptor is " + ((Object) modelMBeanAttributeInfo.getDescriptor()) + "\t\n modelMBeanInfo: AttributeInfo descriptor is " + ((Object) getDescriptor(className, "attribute")));
                    }
                }
            }
        } else if (str.equalsIgnoreCase(OPER)) {
            MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
            int length2 = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
            for (int i3 = 0; i3 < length2; i3++) {
                if (className.equals(mBeanOperationInfoArr[i3].getName())) {
                    z2 = true;
                    ((ModelMBeanOperationInfo) mBeanOperationInfoArr[i3]).setDescriptor(descriptor);
                }
            }
        } else if (str.equalsIgnoreCase(CONS)) {
            MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
            int length3 = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
            for (int i4 = 0; i4 < length3; i4++) {
                if (className.equals(mBeanConstructorInfoArr[i4].getName())) {
                    z2 = true;
                    ((ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i4]).setDescriptor(descriptor);
                }
            }
        } else if (str.equalsIgnoreCase(NOTF)) {
            MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
            int length4 = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
            for (int i5 = 0; i5 < length4; i5++) {
                if (className.equals(mBeanNotificationInfoArr[i5].getName())) {
                    z2 = true;
                    ((ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i5]).setDescriptor(descriptor);
                }
            }
        } else {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid descriptor type: " + str), "Exception occurred trying to set the descriptors of the MBean");
        }
        if (!z2) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor name is invalid: type=" + str + "; name=" + className), "Exception occurred trying to set the descriptors of the MBean");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanAttributeInfo getAttribute(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanAttributeInfo modelMBeanAttributeInfo = null;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute Name is null"), "Exception occurred trying to get the ModelMBeanAttributeInfo of the MBean");
        }
        MBeanAttributeInfo[] mBeanAttributeInfoArr = this.modelMBeanAttributes;
        int length = mBeanAttributeInfoArr != null ? mBeanAttributeInfoArr.length : 0;
        for (int i2 = 0; i2 < length && modelMBeanAttributeInfo == null; i2++) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", "\t\n this.getAttributes() MBeanAttributeInfo Array " + i2 + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) ((ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i2]).getDescriptor()) + "\t\n this.modelMBeanAttributes MBeanAttributeInfo Array " + i2 + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) ((ModelMBeanAttributeInfo) this.modelMBeanAttributes[i2]).getDescriptor()));
            }
            if (str.equals(mBeanAttributeInfoArr[i2].getName())) {
                modelMBeanAttributeInfo = (ModelMBeanAttributeInfo) mBeanAttributeInfoArr[i2].clone();
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", ToolWindow.QUIT);
        }
        return modelMBeanAttributeInfo;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanOperationInfo getOperation(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanOperationInfo modelMBeanOperationInfo = null;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getOperation(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("inName is null"), "Exception occurred trying to get the ModelMBeanOperationInfo of the MBean");
        }
        MBeanOperationInfo[] mBeanOperationInfoArr = this.modelMBeanOperations;
        int length = mBeanOperationInfoArr != null ? mBeanOperationInfoArr.length : 0;
        for (int i2 = 0; i2 < length && modelMBeanOperationInfo == null; i2++) {
            if (str.equals(mBeanOperationInfoArr[i2].getName())) {
                modelMBeanOperationInfo = (ModelMBeanOperationInfo) mBeanOperationInfoArr[i2].clone();
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getOperation(String)", ToolWindow.QUIT);
        }
        return modelMBeanOperationInfo;
    }

    public ModelMBeanConstructorInfo getConstructor(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanConstructorInfo modelMBeanConstructorInfo = null;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getConstructor(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Constructor name is null"), "Exception occurred trying to get the ModelMBeanConstructorInfo of the MBean");
        }
        MBeanConstructorInfo[] mBeanConstructorInfoArr = this.modelMBeanConstructors;
        int length = mBeanConstructorInfoArr != null ? mBeanConstructorInfoArr.length : 0;
        for (int i2 = 0; i2 < length && modelMBeanConstructorInfo == null; i2++) {
            if (str.equals(mBeanConstructorInfoArr[i2].getName())) {
                modelMBeanConstructorInfo = (ModelMBeanConstructorInfo) mBeanConstructorInfoArr[i2].clone();
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getConstructor(String)", ToolWindow.QUIT);
        }
        return modelMBeanConstructorInfo;
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public ModelMBeanNotificationInfo getNotification(String str) throws MBeanException, RuntimeOperationsException {
        ModelMBeanNotificationInfo modelMBeanNotificationInfo = null;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getNotification(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Notification name is null"), "Exception occurred trying to get the ModelMBeanNotificationInfo of the MBean");
        }
        MBeanNotificationInfo[] mBeanNotificationInfoArr = this.modelMBeanNotifications;
        int length = mBeanNotificationInfoArr != null ? mBeanNotificationInfoArr.length : 0;
        for (int i2 = 0; i2 < length && modelMBeanNotificationInfo == null; i2++) {
            if (str.equals(mBeanNotificationInfoArr[i2].getName())) {
                modelMBeanNotificationInfo = (ModelMBeanNotificationInfo) mBeanNotificationInfoArr[i2].clone();
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getNotification(String)", ToolWindow.QUIT);
        }
        return modelMBeanNotificationInfo;
    }

    @Override // javax.management.MBeanInfo, javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        return getMBeanDescriptorNoException();
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public Descriptor getMBeanDescriptor() throws MBeanException {
        return getMBeanDescriptorNoException();
    }

    private Descriptor getMBeanDescriptorNoException() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getMBeanDescriptorNoException()", "Entry");
        }
        if (this.modelMBeanDescriptor == null) {
            this.modelMBeanDescriptor = validDescriptor(null);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getMBeanDescriptorNoException()", "Exit, returning: " + ((Object) this.modelMBeanDescriptor));
        }
        return (Descriptor) this.modelMBeanDescriptor.clone();
    }

    @Override // javax.management.modelmbean.ModelMBeanInfo
    public void setMBeanDescriptor(Descriptor descriptor) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setMBeanDescriptor(Descriptor)", "Entry");
        }
        this.modelMBeanDescriptor = validDescriptor(descriptor);
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
            descriptorSupport.setField("name", getClassName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getClassName());
        }
        if (z2 && descriptorSupport.getFieldValue("descriptorType") == null) {
            descriptorSupport.setField("descriptorType", MMB);
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"mbean\"");
        }
        if (descriptorSupport.getFieldValue("displayName") == null) {
            descriptorSupport.setField("displayName", getClassName());
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getClassName());
        }
        if (descriptorSupport.getFieldValue("persistPolicy") == null) {
            descriptorSupport.setField("persistPolicy", "never");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor persistPolicy to \"never\"");
        }
        if (descriptorSupport.getFieldValue("log") == null) {
            descriptorSupport.setField("log", PdfOps.F_TOKEN);
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor \"log\" field to \"F\"");
        }
        if (descriptorSupport.getFieldValue("visibility") == null) {
            descriptorSupport.setField("visibility", "1");
            JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor visibility to 1");
        }
        if (!descriptorSupport.isValid()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + descriptorSupport.toString());
        }
        if (!((String) descriptorSupport.getFieldValue("descriptorType")).equalsIgnoreCase(MMB)) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: mbean , was: " + descriptorSupport.getFieldValue("descriptorType"));
        }
        return descriptorSupport;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (compat) {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.modelMBeanDescriptor = (Descriptor) fields.get("modelMBeanDescriptor", (Object) null);
            if (fields.defaulted("modelMBeanDescriptor")) {
                throw new NullPointerException("modelMBeanDescriptor");
            }
            this.modelMBeanAttributes = (MBeanAttributeInfo[]) fields.get("mmbAttributes", (Object) null);
            if (fields.defaulted("mmbAttributes")) {
                throw new NullPointerException("mmbAttributes");
            }
            this.modelMBeanConstructors = (MBeanConstructorInfo[]) fields.get("mmbConstructors", (Object) null);
            if (fields.defaulted("mmbConstructors")) {
                throw new NullPointerException("mmbConstructors");
            }
            this.modelMBeanNotifications = (MBeanNotificationInfo[]) fields.get("mmbNotifications", (Object) null);
            if (fields.defaulted("mmbNotifications")) {
                throw new NullPointerException("mmbNotifications");
            }
            this.modelMBeanOperations = (MBeanOperationInfo[]) fields.get("mmbOperations", (Object) null);
            if (fields.defaulted("mmbOperations")) {
                throw new NullPointerException("mmbOperations");
            }
            return;
        }
        objectInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (compat) {
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("modelMBeanDescriptor", this.modelMBeanDescriptor);
            putFieldPutFields.put("mmbAttributes", this.modelMBeanAttributes);
            putFieldPutFields.put("mmbConstructors", this.modelMBeanConstructors);
            putFieldPutFields.put("mmbNotifications", this.modelMBeanNotifications);
            putFieldPutFields.put("mmbOperations", this.modelMBeanOperations);
            putFieldPutFields.put("currClass", currClass);
            objectOutputStream.writeFields();
            return;
        }
        objectOutputStream.defaultWriteObject();
    }
}
