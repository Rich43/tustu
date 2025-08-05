package javax.management.modelmbean;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javax.management.Attribute;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeOperationsException;
import javax.management.ServiceNotFoundException;
import javax.management.loading.ClassLoaderRepository;
import org.icepdf.core.util.PdfOps;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: rt.jar:javax/management/modelmbean/RequiredModelMBean.class */
public class RequiredModelMBean implements ModelMBean, MBeanRegistration, NotificationEmitter {
    ModelMBeanInfo modelMBeanInfo;
    private NotificationBroadcasterSupport generalBroadcaster = null;
    private NotificationBroadcasterSupport attributeBroadcaster = null;
    private Object managedResource = null;
    private boolean registered = false;
    private transient MBeanServer server = null;
    private final AccessControlContext acc = AccessController.getContext();
    private static Set<String> rmmbMethodNames;
    private static final String[] primitiveTypes;
    private static final String[] primitiveWrappers;
    private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
    private static final Class<?>[] primitiveClasses = {Integer.TYPE, Long.TYPE, Boolean.TYPE, Double.TYPE, Float.TYPE, Short.TYPE, Byte.TYPE, Character.TYPE};
    private static final Map<String, Class<?>> primitiveClassMap = new HashMap();

    static {
        for (int i2 = 0; i2 < primitiveClasses.length; i2++) {
            Class<?> cls = primitiveClasses[i2];
            primitiveClassMap.put(cls.getName(), cls);
        }
        primitiveTypes = new String[]{Boolean.TYPE.getName(), Byte.TYPE.getName(), Character.TYPE.getName(), Short.TYPE.getName(), Integer.TYPE.getName(), Long.TYPE.getName(), Float.TYPE.getName(), Double.TYPE.getName(), Void.TYPE.getName()};
        primitiveWrappers = new String[]{Boolean.class.getName(), Byte.class.getName(), Character.class.getName(), Short.class.getName(), Integer.class.getName(), Long.class.getName(), Float.class.getName(), Double.class.getName(), Void.class.getName()};
    }

    public RequiredModelMBean() throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean()", "Entry");
        }
        this.modelMBeanInfo = createDefaultModelMBeanInfo();
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean()", ToolWindow.QUIT);
        }
    }

    public RequiredModelMBean(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean(MBeanInfo)", "Entry");
        }
        setModelMBeanInfo(modelMBeanInfo);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean(MBeanInfo)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBean
    public void setModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "Entry");
        }
        if (modelMBeanInfo == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo is null: Raising exception.");
            }
            throw new RuntimeOperationsException(new IllegalArgumentException("ModelMBeanInfo must not be null"), "Exception occurred trying to initialize the ModelMBeanInfo of the RequiredModelMBean");
        }
        if (this.registered) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "RequiredMBean is registered: Raising exception.");
            }
            throw new RuntimeOperationsException(new IllegalStateException("cannot call setModelMBeanInfo while ModelMBean is registered"), "Exception occurred trying to set the ModelMBeanInfo of the RequiredModelMBean");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "Setting ModelMBeanInfo to " + printModelMBeanInfo(modelMBeanInfo));
            int length = 0;
            if (modelMBeanInfo.getNotifications() != null) {
                length = modelMBeanInfo.getNotifications().length;
            }
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo notifications has " + length + " elements");
        }
        this.modelMBeanInfo = (ModelMBeanInfo) modelMBeanInfo.clone();
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "set mbeanInfo to: " + printModelMBeanInfo(this.modelMBeanInfo));
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBean
    public void setManagedResource(Object obj, String str) throws InvalidTargetObjectTypeException, MBeanException, InstanceNotFoundException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Entry");
        }
        if (str == null || !str.equalsIgnoreCase("objectReference")) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Managed Resource Type is not supported: " + str);
            }
            throw new InvalidTargetObjectTypeException(str);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Managed Resource is valid");
        }
        this.managedResource = obj;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object, String)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.PersistentMBean
    public void load() throws MBeanException, InstanceNotFoundException, RuntimeOperationsException {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
        throw new MBeanException(serviceNotFoundException, serviceNotFoundException.getMessage());
    }

    @Override // javax.management.PersistentMBean
    public void store() throws MBeanException, InstanceNotFoundException, RuntimeOperationsException {
        ServiceNotFoundException serviceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
        throw new MBeanException(serviceNotFoundException, serviceNotFoundException.getMessage());
    }

    private Object resolveForCacheValue(Descriptor descriptor) throws MBeanException, RuntimeOperationsException {
        String string;
        boolean z2;
        boolean z3;
        boolean zIsLoggable = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "Entry");
        }
        Object obj = null;
        if (descriptor == null) {
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "Input Descriptor is null");
            }
            return null;
        }
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "descriptor is " + ((Object) descriptor));
        }
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (mBeanDescriptor == null && zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "MBean Descriptor is null");
        }
        Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
        if (fieldValue != null) {
            string = fieldValue.toString();
        } else {
            string = null;
        }
        if (string == null && mBeanDescriptor != null) {
            Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
            if (fieldValue2 != null) {
                string = fieldValue2.toString();
            } else {
                string = null;
            }
        }
        if (string != null) {
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "currencyTimeLimit: " + string);
            }
            long jLongValue = new Long(string).longValue() * 1000;
            if (jLongValue < 0) {
                z2 = false;
                z3 = true;
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", jLongValue + ": never Cached");
                }
            } else if (jLongValue == 0) {
                z2 = true;
                z3 = false;
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "always valid Cache");
                }
            } else {
                Object fieldValue3 = descriptor.getFieldValue("lastUpdatedTimeStamp");
                String string2 = fieldValue3 != null ? fieldValue3.toString() : null;
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "lastUpdatedTimeStamp: " + string2);
                }
                if (string2 == null) {
                    string2 = "0";
                }
                long jLongValue2 = new Long(string2).longValue();
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "currencyPeriod:" + jLongValue + " lastUpdatedTimeStamp:" + jLongValue2);
                }
                long time = new Date().getTime();
                if (time < jLongValue2 + jLongValue) {
                    z2 = true;
                    z3 = false;
                    if (zIsLoggable) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", " timed valid Cache for " + time + " < " + (jLongValue2 + jLongValue));
                    }
                } else {
                    z2 = false;
                    z3 = true;
                    if (zIsLoggable) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "timed expired cache for " + time + " > " + (jLongValue2 + jLongValue));
                    }
                }
            }
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "returnCachedValue:" + z2 + " resetValue: " + z3);
            }
            if (z2) {
                Object fieldValue4 = descriptor.getFieldValue("value");
                if (fieldValue4 != null) {
                    obj = fieldValue4;
                    if (zIsLoggable) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "valid Cache value: " + fieldValue4);
                    }
                } else {
                    obj = null;
                    if (zIsLoggable) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "no Cached value");
                    }
                }
            }
            if (z3) {
                descriptor.removeField("lastUpdatedTimeStamp");
                descriptor.removeField("value");
                obj = null;
                this.modelMBeanInfo.setDescriptor(descriptor, null);
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "reset cached value to null");
                }
            }
        }
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", ToolWindow.QUIT);
        }
        return obj;
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "Entry");
        }
        if (this.modelMBeanInfo == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "modelMBeanInfo is null");
            }
            this.modelMBeanInfo = createDefaultModelMBeanInfo();
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "ModelMBeanInfo is " + this.modelMBeanInfo.getClassName() + " for " + this.modelMBeanInfo.getDescription());
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", printModelMBeanInfo(this.modelMBeanInfo));
        }
        return (MBeanInfo) this.modelMBeanInfo.clone();
    }

    private String printModelMBeanInfo(ModelMBeanInfo modelMBeanInfo) {
        StringBuilder sb = new StringBuilder();
        if (modelMBeanInfo == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "printModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo to print is null, printing local ModelMBeanInfo");
            }
            modelMBeanInfo = this.modelMBeanInfo;
        }
        sb.append("\nMBeanInfo for ModelMBean is:");
        sb.append("\nCLASSNAME: \t" + modelMBeanInfo.getClassName());
        sb.append("\nDESCRIPTION: \t" + modelMBeanInfo.getDescription());
        try {
            sb.append("\nMBEAN DESCRIPTOR: \t" + ((Object) modelMBeanInfo.getMBeanDescriptor()));
        } catch (Exception e2) {
            sb.append("\nMBEAN DESCRIPTOR: \t is invalid");
        }
        sb.append("\nATTRIBUTES");
        MBeanAttributeInfo[] attributes = modelMBeanInfo.getAttributes();
        if (attributes != null && attributes.length > 0) {
            for (MBeanAttributeInfo mBeanAttributeInfo : attributes) {
                ModelMBeanAttributeInfo modelMBeanAttributeInfo = (ModelMBeanAttributeInfo) mBeanAttributeInfo;
                sb.append(" ** NAME: \t" + modelMBeanAttributeInfo.getName());
                sb.append("    DESCR: \t" + modelMBeanAttributeInfo.getDescription());
                sb.append("    TYPE: \t" + modelMBeanAttributeInfo.getType() + "    READ: \t" + modelMBeanAttributeInfo.isReadable() + "    WRITE: \t" + modelMBeanAttributeInfo.isWritable());
                sb.append("    DESCRIPTOR: " + modelMBeanAttributeInfo.getDescriptor().toString());
            }
        } else {
            sb.append(" ** No attributes **");
        }
        sb.append("\nCONSTRUCTORS");
        MBeanConstructorInfo[] constructors = modelMBeanInfo.getConstructors();
        if (constructors != null && constructors.length > 0) {
            for (MBeanConstructorInfo mBeanConstructorInfo : constructors) {
                ModelMBeanConstructorInfo modelMBeanConstructorInfo = (ModelMBeanConstructorInfo) mBeanConstructorInfo;
                sb.append(" ** NAME: \t" + modelMBeanConstructorInfo.getName());
                sb.append("    DESCR: \t" + modelMBeanConstructorInfo.getDescription());
                sb.append("    PARAM: \t" + modelMBeanConstructorInfo.getSignature().length + " parameter(s)");
                sb.append("    DESCRIPTOR: " + modelMBeanConstructorInfo.getDescriptor().toString());
            }
        } else {
            sb.append(" ** No Constructors **");
        }
        sb.append("\nOPERATIONS");
        MBeanOperationInfo[] operations = modelMBeanInfo.getOperations();
        if (operations != null && operations.length > 0) {
            for (MBeanOperationInfo mBeanOperationInfo : operations) {
                ModelMBeanOperationInfo modelMBeanOperationInfo = (ModelMBeanOperationInfo) mBeanOperationInfo;
                sb.append(" ** NAME: \t" + modelMBeanOperationInfo.getName());
                sb.append("    DESCR: \t" + modelMBeanOperationInfo.getDescription());
                sb.append("    PARAM: \t" + modelMBeanOperationInfo.getSignature().length + " parameter(s)");
                sb.append("    DESCRIPTOR: " + modelMBeanOperationInfo.getDescriptor().toString());
            }
        } else {
            sb.append(" ** No operations ** ");
        }
        sb.append("\nNOTIFICATIONS");
        MBeanNotificationInfo[] notifications = modelMBeanInfo.getNotifications();
        if (notifications != null && notifications.length > 0) {
            for (MBeanNotificationInfo mBeanNotificationInfo : notifications) {
                ModelMBeanNotificationInfo modelMBeanNotificationInfo = (ModelMBeanNotificationInfo) mBeanNotificationInfo;
                sb.append(" ** NAME: \t" + modelMBeanNotificationInfo.getName());
                sb.append("    DESCR: \t" + modelMBeanNotificationInfo.getDescription());
                sb.append("    DESCRIPTOR: " + modelMBeanNotificationInfo.getDescriptor().toString());
            }
        } else {
            sb.append(" ** No notifications **");
        }
        sb.append(" ** ModelMBean: End of MBeanInfo ** ");
        return sb.toString();
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws Exception {
        String strSubstring;
        Object obj;
        Class<?> cls;
        boolean zIsLoggable = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Method name must not be null"), "An exception occurred while trying to invoke a method on a RequiredModelMBean");
        }
        String strSubstring2 = null;
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf > 0) {
            strSubstring2 = str.substring(0, iLastIndexOf);
            strSubstring = str.substring(iLastIndexOf + 1);
        } else {
            strSubstring = str;
        }
        int iIndexOf = strSubstring.indexOf("(");
        if (iIndexOf > 0) {
            strSubstring = strSubstring.substring(0, iIndexOf);
        }
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Finding operation " + str + " as " + strSubstring);
        }
        ModelMBeanOperationInfo operation = this.modelMBeanInfo.getOperation(strSubstring);
        if (operation == null) {
            String str2 = "Operation " + str + " not in ModelMBeanInfo";
            throw new MBeanException(new ServiceNotFoundException(str2), str2);
        }
        Descriptor descriptor = operation.getDescriptor();
        if (descriptor == null) {
            throw new MBeanException(new ServiceNotFoundException("Operation descriptor null"), "Operation descriptor null");
        }
        Object objResolveForCacheValue = resolveForCacheValue(descriptor);
        if (objResolveForCacheValue != null) {
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Returning cached value");
            }
            return objResolveForCacheValue;
        }
        if (strSubstring2 == null) {
            strSubstring2 = (String) descriptor.getFieldValue(Constants.ATTRNAME_CLASS);
        }
        String str3 = (String) descriptor.getFieldValue("name");
        if (str3 == null) {
            throw new MBeanException(new ServiceNotFoundException("Method descriptor must include `name' field"), "Method descriptor must include `name' field");
        }
        String str4 = (String) descriptor.getFieldValue("targetType");
        if (str4 != null && !str4.equalsIgnoreCase("objectReference")) {
            String str5 = "Target type must be objectReference: " + str4;
            throw new MBeanException(new InvalidTargetObjectTypeException(str5), str5);
        }
        Object fieldValue = descriptor.getFieldValue("targetObject");
        if (zIsLoggable && fieldValue != null) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Found target object in descriptor");
        }
        Method methodFindRMMBMethod = findRMMBMethod(str3, fieldValue, strSubstring2, strArr);
        if (methodFindRMMBMethod != null) {
            obj = this;
        } else {
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "looking for method in managedResource class");
            }
            if (fieldValue != null) {
                obj = fieldValue;
            } else {
                obj = this.managedResource;
                if (obj == null) {
                    throw new MBeanException(new ServiceNotFoundException("managedResource for invoke " + str + " is null"));
                }
            }
            if (strSubstring2 != null) {
                try {
                    final Object obj2 = obj;
                    final String str6 = strSubstring2;
                    final ClassNotFoundException[] classNotFoundExceptionArr = new ClassNotFoundException[1];
                    cls = (Class) javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Class<?>>() { // from class: javax.management.modelmbean.RequiredModelMBean.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Class<?> run2() {
                            try {
                                ReflectUtil.checkPackageAccess(str6);
                                return Class.forName(str6, false, obj2.getClass().getClassLoader());
                            } catch (ClassNotFoundException e2) {
                                classNotFoundExceptionArr[0] = e2;
                                return null;
                            }
                        }
                    }, AccessController.getContext(), this.acc);
                    if (classNotFoundExceptionArr[0] != null) {
                        throw classNotFoundExceptionArr[0];
                    }
                } catch (ClassNotFoundException e2) {
                    throw new ReflectionException(e2, "class for invoke " + str + " not found");
                }
            } else {
                cls = obj.getClass();
            }
            methodFindRMMBMethod = resolveMethod(cls, str3, strArr);
        }
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "found " + str3 + ", now invoking");
        }
        Object objInvokeMethod = invokeMethod(str, methodFindRMMBMethod, obj, objArr);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "successfully invoked method");
        }
        if (objInvokeMethod != null) {
            cacheResult(operation, descriptor, objInvokeMethod);
        }
        return objInvokeMethod;
    }

    private Method resolveMethod(Class<?> cls, String str, final String[] strArr) throws ReflectionException {
        final Class<?>[] clsArr;
        final boolean zIsLoggable = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "resolving " + cls.getName() + "." + str);
        }
        if (strArr == null) {
            clsArr = null;
        } else {
            AccessControlContext context = AccessController.getContext();
            final ReflectionException[] reflectionExceptionArr = new ReflectionException[1];
            final ClassLoader classLoader = cls.getClassLoader();
            clsArr = new Class[strArr.length];
            javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: javax.management.modelmbean.RequiredModelMBean.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    for (int i2 = 0; i2 < strArr.length; i2++) {
                        if (zIsLoggable) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "resolve type " + strArr[i2]);
                        }
                        clsArr[i2] = (Class) RequiredModelMBean.primitiveClassMap.get(strArr[i2]);
                        if (clsArr[i2] == null) {
                            try {
                                ReflectUtil.checkPackageAccess(strArr[i2]);
                                clsArr[i2] = Class.forName(strArr[i2], false, classLoader);
                            } catch (ClassNotFoundException e2) {
                                if (zIsLoggable) {
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "class not found");
                                }
                                reflectionExceptionArr[0] = new ReflectionException(e2, "Parameter class not found");
                            }
                        }
                    }
                    return null;
                }
            }, context, this.acc);
            if (reflectionExceptionArr[0] != null) {
                throw reflectionExceptionArr[0];
            }
        }
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException e2) {
            throw new ReflectionException(e2, "Target method not found: " + cls.getName() + "." + str);
        }
    }

    private Method findRMMBMethod(String str, Object obj, final String str2, String[] strArr) {
        Class<?> cls;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "looking for method in RequiredModelMBean class");
        }
        if (!isRMMBMethodName(str) || obj != null) {
            return null;
        }
        final Class<RequiredModelMBean> cls2 = RequiredModelMBean.class;
        if (str2 == null) {
            cls = RequiredModelMBean.class;
        } else {
            cls = (Class) javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Class<?>>() { // from class: javax.management.modelmbean.RequiredModelMBean.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Class<?> run2() {
                    try {
                        ReflectUtil.checkPackageAccess(str2);
                        Class<?> cls3 = Class.forName(str2, false, cls2.getClassLoader());
                        if (!cls2.isAssignableFrom(cls3)) {
                            return null;
                        }
                        return cls3;
                    } catch (ClassNotFoundException e2) {
                        return null;
                    }
                }
            }, AccessController.getContext(), this.acc);
        }
        if (cls == null) {
            return null;
        }
        try {
            return resolveMethod(cls, str, strArr);
        } catch (ReflectionException e2) {
            return null;
        }
    }

    private Object invokeMethod(String str, final Method method, final Object obj, final Object[] objArr) throws Exception {
        try {
            final Throwable[] thArr = new Throwable[1];
            Object objDoIntersectionPrivilege = javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Object>() { // from class: javax.management.modelmbean.RequiredModelMBean.4
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    try {
                        ReflectUtil.checkPackageAccess(method.getDeclaringClass());
                        return MethodUtil.invoke(method, obj, objArr);
                    } catch (IllegalAccessException e2) {
                        thArr[0] = e2;
                        return null;
                    } catch (InvocationTargetException e3) {
                        thArr[0] = e3;
                        return null;
                    }
                }
            }, AccessController.getContext(), this.acc);
            if (thArr[0] != null) {
                if (thArr[0] instanceof Exception) {
                    throw ((Exception) thArr[0]);
                }
                if (thArr[0] instanceof Error) {
                    throw ((Error) thArr[0]);
                }
            }
            return objDoIntersectionPrivilege;
        } catch (Error e2) {
            throw new RuntimeErrorException(e2, "Error occurred in RequiredModelMBean while trying to invoke operation " + str);
        } catch (IllegalAccessException e3) {
            throw new ReflectionException(e3, "IllegalAccessException occurred in RequiredModelMBean while trying to invoke operation " + str);
        } catch (RuntimeErrorException e4) {
            throw new RuntimeOperationsException(e4, "RuntimeException occurred in RequiredModelMBean while trying to invoke operation " + str);
        } catch (RuntimeException e5) {
            throw new RuntimeOperationsException(e5, "RuntimeException occurred in RequiredModelMBean while trying to invoke operation " + str);
        } catch (InvocationTargetException e6) {
            Throwable targetException = e6.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw new MBeanException((RuntimeException) targetException, "RuntimeException thrown in RequiredModelMBean while trying to invoke operation " + str);
            }
            if (targetException instanceof Error) {
                throw new RuntimeErrorException((Error) targetException, "Error occurred in RequiredModelMBean while trying to invoke operation " + str);
            }
            if (targetException instanceof ReflectionException) {
                throw ((ReflectionException) targetException);
            }
            throw new MBeanException((Exception) targetException, "Exception thrown in RequiredModelMBean while trying to invoke operation " + str);
        } catch (Exception e7) {
            throw new ReflectionException(e7, "Exception occurred in RequiredModelMBean while trying to invoke operation " + str);
        }
    }

    private void cacheResult(ModelMBeanOperationInfo modelMBeanOperationInfo, Descriptor descriptor, Object obj) throws MBeanException, RuntimeOperationsException {
        String string;
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
        if (fieldValue != null) {
            string = fieldValue.toString();
        } else {
            string = null;
        }
        if (string == null && mBeanDescriptor != null) {
            Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
            if (fieldValue2 != null) {
                string = fieldValue2.toString();
            } else {
                string = null;
            }
        }
        if (string != null && !string.equals("-1")) {
            descriptor.setField("value", obj);
            descriptor.setField("lastUpdatedTimeStamp", String.valueOf(new Date().getTime()));
            this.modelMBeanInfo.setDescriptor(descriptor, "operation");
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String,Object[],Object[])", "new descriptor is " + ((Object) descriptor));
            }
        }
    }

    private static synchronized boolean isRMMBMethodName(String str) {
        if (rmmbMethodNames == null) {
            try {
                HashSet hashSet = new HashSet();
                for (Method method : RequiredModelMBean.class.getMethods()) {
                    hashSet.add(method.getName());
                }
                rmmbMethodNames = hashSet;
            } catch (Exception e2) {
                return true;
            }
        }
        return rmmbMethodNames.contains(str);
    }

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws Exception {
        boolean zIsInstance;
        final Exception[] excArr;
        Class cls;
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeName must not be null"), "Exception occurred trying to get attribute of a RequiredModelMBean");
        }
        boolean zIsLoggable = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Entry with " + str);
        }
        try {
            if (this.modelMBeanInfo == null) {
                throw new AttributeNotFoundException("getAttribute failed: ModelMBeanInfo not found for " + str);
            }
            ModelMBeanAttributeInfo attribute = this.modelMBeanInfo.getAttribute(str);
            Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
            if (attribute == null) {
                throw new AttributeNotFoundException("getAttribute failed: ModelMBeanAttributeInfo not found for " + str);
            }
            Descriptor descriptor = attribute.getDescriptor();
            if (descriptor != null) {
                if (!attribute.isReadable()) {
                    throw new AttributeNotFoundException("getAttribute failed: " + str + " is not readable ");
                }
                Object objResolveForCacheValue = resolveForCacheValue(descriptor);
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "*** cached value is " + objResolveForCacheValue);
                }
                if (objResolveForCacheValue == null) {
                    if (zIsLoggable) {
                        JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "**** cached value is null - getting getMethod");
                    }
                    String str2 = (String) descriptor.getFieldValue("getMethod");
                    if (str2 != null) {
                        if (zIsLoggable) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "invoking a getMethod for " + str);
                        }
                        Object objInvoke = invoke(str2, new Object[0], new String[0]);
                        if (objInvoke != null) {
                            if (zIsLoggable) {
                                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "got a non-null response from getMethod\n");
                            }
                            objResolveForCacheValue = objInvoke;
                            Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
                            String string = fieldValue != null ? fieldValue.toString() : null;
                            if (string == null && mBeanDescriptor != null) {
                                Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
                                string = fieldValue2 != null ? fieldValue2.toString() : null;
                            }
                            if (string != null && !string.equals("-1")) {
                                if (zIsLoggable) {
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "setting cached value and lastUpdatedTime in descriptor");
                                }
                                descriptor.setField("value", objResolveForCacheValue);
                                descriptor.setField("lastUpdatedTimeStamp", String.valueOf(new Date().getTime()));
                                attribute.setDescriptor(descriptor);
                                this.modelMBeanInfo.setDescriptor(descriptor, "attribute");
                                if (zIsLoggable) {
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "new descriptor is " + ((Object) descriptor));
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "AttributeInfo descriptor is " + ((Object) attribute.getDescriptor()));
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "modelMBeanInfo: AttributeInfo descriptor is " + this.modelMBeanInfo.getDescriptor(str, "attribute").toString());
                                }
                            }
                        } else {
                            if (zIsLoggable) {
                                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "got a null response from getMethod\n");
                            }
                            objResolveForCacheValue = null;
                        }
                    } else {
                        String str3 = "";
                        objResolveForCacheValue = descriptor.getFieldValue("value");
                        if (objResolveForCacheValue == null) {
                            str3 = "default ";
                            objResolveForCacheValue = descriptor.getFieldValue("default");
                        }
                        if (zIsLoggable) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "could not find getMethod for " + str + ", returning descriptor " + str3 + "value");
                        }
                    }
                }
                final String type = attribute.getType();
                if (objResolveForCacheValue != null) {
                    String name = objResolveForCacheValue.getClass().getName();
                    if (!type.equals(name)) {
                        boolean z2 = false;
                        boolean z3 = false;
                        boolean z4 = false;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= primitiveTypes.length) {
                                break;
                            }
                            if (!type.equals(primitiveTypes[i2])) {
                                i2++;
                            } else {
                                z3 = true;
                                if (name.equals(primitiveWrappers[i2])) {
                                    z4 = true;
                                }
                            }
                        }
                        if (z3) {
                            if (!z4) {
                                z2 = true;
                            }
                        } else {
                            try {
                                final Class<?> cls2 = objResolveForCacheValue.getClass();
                                excArr = new Exception[1];
                                cls = (Class) javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Class<?>>() { // from class: javax.management.modelmbean.RequiredModelMBean.5
                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // java.security.PrivilegedAction
                                    /* renamed from: run */
                                    public Class<?> run2() {
                                        try {
                                            ReflectUtil.checkPackageAccess(type);
                                            return Class.forName(type, true, cls2.getClassLoader());
                                        } catch (Exception e2) {
                                            excArr[0] = e2;
                                            return null;
                                        }
                                    }
                                }, AccessController.getContext(), this.acc);
                            } catch (Exception e2) {
                                zIsInstance = false;
                                if (zIsLoggable) {
                                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Exception: ", (Throwable) e2);
                                }
                            }
                            if (excArr[0] != null) {
                                throw excArr[0];
                            }
                            zIsInstance = cls.isInstance(objResolveForCacheValue);
                            if (!zIsInstance) {
                                z2 = true;
                            }
                        }
                        if (z2) {
                            if (zIsLoggable) {
                                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Wrong response type '" + type + PdfOps.SINGLE_QUOTE_TOKEN);
                            }
                            throw new MBeanException(new InvalidAttributeValueException("Wrong value type received for get attribute"), "An exception occurred while trying to get an attribute value through a RequiredModelMBean");
                        }
                    }
                }
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", ToolWindow.QUIT);
                }
                return objResolveForCacheValue;
            }
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "getMethod failed " + str + " not in attributeDescriptor\n");
            }
            throw new MBeanException(new InvalidAttributeValueException("Unable to resolve attribute value, no getMethod defined in descriptor for attribute"), "An exception occurred while trying to get an attribute value through a RequiredModelMBean");
        } catch (AttributeNotFoundException e3) {
            throw e3;
        } catch (MBeanException e4) {
            throw e4;
        } catch (Exception e5) {
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "getMethod failed with " + e5.getMessage() + " exception type " + e5.getClass().toString());
            }
            throw new MBeanException(e5, "An exception occurred while trying to get an attribute value: " + e5.getMessage());
        }
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", "Entry");
        }
        if (strArr == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames must not be null"), "Exception occurred trying to get attributes of a RequiredModelMBean");
        }
        AttributeList attributeList = new AttributeList();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            try {
                attributeList.add(new Attribute(strArr[i2], getAttribute(strArr[i2])));
            } catch (Exception e2) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", "Failed to get \"" + strArr[i2] + "\": ", (Throwable) e2);
                }
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", ToolWindow.QUIT);
        }
        return attributeList;
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws Exception {
        boolean zIsLoggable = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute()", "Entry");
        }
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attribute must not be null"), "Exception occurred trying to set an attribute of a RequiredModelMBean");
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        boolean z2 = false;
        ModelMBeanAttributeInfo attribute2 = this.modelMBeanInfo.getAttribute(name);
        if (attribute2 == null) {
            throw new AttributeNotFoundException("setAttribute failed: " + name + " is not found ");
        }
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        Descriptor descriptor = attribute2.getDescriptor();
        if (descriptor != null) {
            if (!attribute2.isWritable()) {
                throw new AttributeNotFoundException("setAttribute failed: " + name + " is not writable ");
            }
            String str = (String) descriptor.getFieldValue("setMethod");
            String str2 = (String) descriptor.getFieldValue("getMethod");
            String type = attribute2.getType();
            Object attribute3 = "Unknown";
            try {
                attribute3 = getAttribute(name);
            } catch (Throwable th) {
            }
            Attribute attribute4 = new Attribute(name, attribute3);
            if (str == null) {
                if (value != null) {
                    try {
                        Class<?> clsLoadClass = loadClass(type);
                        if (!clsLoadClass.isInstance(value)) {
                            throw new InvalidAttributeValueException(clsLoadClass.getName() + " expected, " + value.getClass().getName() + " received.");
                        }
                    } catch (ClassNotFoundException e2) {
                        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "Class " + type + " for attribute " + name + " not found: ", (Throwable) e2);
                        }
                    }
                }
                z2 = true;
            } else {
                invoke(str, new Object[]{value}, new String[]{type});
            }
            Object fieldValue = descriptor.getFieldValue("currencyTimeLimit");
            String string = fieldValue != null ? fieldValue.toString() : null;
            if (string == null && mBeanDescriptor != null) {
                Object fieldValue2 = mBeanDescriptor.getFieldValue("currencyTimeLimit");
                string = fieldValue2 != null ? fieldValue2.toString() : null;
            }
            boolean z3 = (string == null || string.equals("-1")) ? false : true;
            if (str == null && !z3 && str2 != null) {
                throw new MBeanException(new ServiceNotFoundException("No setMethod field is defined in the descriptor for " + name + " attribute and caching is not enabled for it"));
            }
            if (z3 || z2) {
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "setting cached value of " + name + " to " + value);
                }
                descriptor.setField("value", value);
                if (z3) {
                    descriptor.setField("lastUpdatedTimeStamp", String.valueOf(new Date().getTime()));
                }
                attribute2.setDescriptor(descriptor);
                this.modelMBeanInfo.setDescriptor(descriptor, "attribute");
                if (zIsLoggable) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "new descriptor is " + ((Object) descriptor) + ". AttributeInfo descriptor is " + ((Object) attribute2.getDescriptor()) + ". AttributeInfo descriptor is " + ((Object) this.modelMBeanInfo.getDescriptor(name, "attribute")));
                }
            }
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "sending sendAttributeNotification");
            }
            sendAttributeChangeNotification(attribute4, attribute);
            if (zIsLoggable) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", ToolWindow.QUIT);
                return;
            }
            return;
        }
        if (zIsLoggable) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "setMethod failed " + name + " not in attributeDescriptor\n");
        }
        throw new InvalidAttributeValueException("Unable to resolve attribute value, no defined in descriptor for attribute");
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "Entry");
        }
        if (attributeList == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributes must not be null"), "Exception occurred trying to set attributes of a RequiredModelMBean");
        }
        AttributeList attributeList2 = new AttributeList();
        for (Attribute attribute : attributeList.asList()) {
            try {
                setAttribute(attribute);
                attributeList2.add(attribute);
            } catch (Exception e2) {
                attributeList2.remove(attribute);
            }
        }
        return attributeList2;
    }

    private ModelMBeanInfo createDefaultModelMBeanInfo() {
        return new ModelMBeanInfoSupport(getClass().getName(), "Default ModelMBean", null, null, null, null);
    }

    private synchronized void writeToLog(String str, String str2) throws Exception {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Notification Logging to " + str + ": " + str2);
        }
        if (str == null || str2 == null) {
            if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Bad input parameters, will not log this entry.");
                return;
            }
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(str, true);
        try {
            try {
                PrintStream printStream = new PrintStream(fileOutputStream);
                printStream.println(str2);
                printStream.close();
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Successfully opened log " + str);
                }
            } catch (Exception e2) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Exception " + e2.toString() + " trying to write to the Notification log file " + str);
                }
                throw e2;
            }
        } finally {
            fileOutputStream.close();
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
        }
        if (notificationListener == null) {
            throw new IllegalArgumentException("notification listener must not be null");
        }
        if (this.generalBroadcaster == null) {
            this.generalBroadcaster = new NotificationBroadcasterSupport();
        }
        this.generalBroadcaster.addNotificationListener(notificationListener, notificationFilter, obj);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", "NotificationListener added");
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener)", "Entry");
        }
        if (this.generalBroadcaster == null) {
            throw new ListenerNotFoundException("No notification listeners registered");
        }
        this.generalBroadcaster.removeNotificationListener(notificationListener);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
        }
        if (this.generalBroadcaster == null) {
            throw new ListenerNotFoundException("No notification listeners registered");
        }
        this.generalBroadcaster.removeNotificationListener(notificationListener, notificationFilter, obj);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener, NotificationFilter, Object)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendNotification(Notification notification) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "Entry");
        }
        if (notification == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("notification object must not be null"), "Exception occurred trying to send a notification from a RequiredModelMBean");
        }
        Descriptor descriptor = this.modelMBeanInfo.getDescriptor(notification.getType(), "notification");
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (descriptor != null) {
            String str = (String) descriptor.getFieldValue("log");
            if (str == null && mBeanDescriptor != null) {
                str = (String) mBeanDescriptor.getFieldValue("log");
            }
            if (str != null && (str.equalsIgnoreCase("t") || str.equalsIgnoreCase("true"))) {
                String str2 = (String) descriptor.getFieldValue("logfile");
                if (str2 == null && mBeanDescriptor != null) {
                    str2 = (String) mBeanDescriptor.getFieldValue("logfile");
                }
                if (str2 != null) {
                    try {
                        writeToLog(str2, "LogMsg: " + new Date(notification.getTimeStamp()).toString() + " " + notification.getType() + " " + notification.getMessage() + " Severity = " + ((String) descriptor.getFieldValue("severity")));
                    } catch (Exception e2) {
                        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "Failed to log " + notification.getType() + " notification: ", (Throwable) e2);
                        }
                    }
                }
            }
        }
        if (this.generalBroadcaster != null) {
            this.generalBroadcaster.sendNotification(notification);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "sendNotification sent provided notification object");
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", " Exit");
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendNotification(String str) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", "Entry");
        }
        if (str == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("notification message must not be null"), "Exception occurred trying to send a text notification from a ModelMBean");
        }
        sendNotification(new Notification("jmx.modelmbean.generic", this, 1L, str));
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", "Notification sent");
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", ToolWindow.QUIT);
        }
    }

    private static final boolean hasNotification(ModelMBeanInfo modelMBeanInfo, String str) {
        if (modelMBeanInfo == null) {
            return false;
        }
        try {
            return modelMBeanInfo.getNotification(str) != null;
        } catch (MBeanException e2) {
            return false;
        } catch (RuntimeOperationsException e3) {
            return false;
        }
    }

    private static final ModelMBeanNotificationInfo makeGenericInfo() {
        return new ModelMBeanNotificationInfo(new String[]{"jmx.modelmbean.generic"}, "GENERIC", "A text notification has been issued by the managed resource", new DescriptorSupport("name=GENERIC", "descriptorType=notification", "log=T", "severity=6", "displayName=jmx.modelmbean.generic"));
    }

    private static final ModelMBeanNotificationInfo makeAttributeChangeInfo() {
        return new ModelMBeanNotificationInfo(new String[]{AttributeChangeNotification.ATTRIBUTE_CHANGE}, "ATTRIBUTE_CHANGE", "Signifies that an observed MBean attribute value has changed", new DescriptorSupport("name=ATTRIBUTE_CHANGE", "descriptorType=notification", "log=T", "severity=6", "displayName=jmx.attribute.change"));
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getNotificationInfo()", "Entry");
        }
        boolean zHasNotification = hasNotification(this.modelMBeanInfo, "GENERIC");
        boolean zHasNotification2 = hasNotification(this.modelMBeanInfo, "ATTRIBUTE_CHANGE");
        ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr = (ModelMBeanNotificationInfo[]) this.modelMBeanInfo.getNotifications();
        ModelMBeanNotificationInfo[] modelMBeanNotificationInfoArr2 = new ModelMBeanNotificationInfo[(modelMBeanNotificationInfoArr == null ? 0 : modelMBeanNotificationInfoArr.length) + (zHasNotification ? 0 : 1) + (zHasNotification2 ? 0 : 1)];
        int i2 = 0;
        if (!zHasNotification) {
            i2 = 0 + 1;
            modelMBeanNotificationInfoArr2[0] = makeGenericInfo();
        }
        if (!zHasNotification2) {
            int i3 = i2;
            i2++;
            modelMBeanNotificationInfoArr2[i3] = makeAttributeChangeInfo();
        }
        int length = modelMBeanNotificationInfoArr.length;
        int i4 = i2;
        for (int i5 = 0; i5 < length; i5++) {
            modelMBeanNotificationInfoArr2[i4 + i5] = modelMBeanNotificationInfoArr[i5];
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getNotificationInfo()", ToolWindow.QUIT);
        }
        return modelMBeanNotificationInfoArr2;
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void addAttributeChangeNotificationListener(NotificationListener notificationListener, String str, Object obj) throws MBeanException, IllegalArgumentException, RuntimeOperationsException {
        String string;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Entry");
        }
        if (notificationListener == null) {
            throw new IllegalArgumentException("Listener to be registered must not be null");
        }
        if (this.attributeBroadcaster == null) {
            this.attributeBroadcaster = new NotificationBroadcasterSupport();
        }
        AttributeChangeNotificationFilter attributeChangeNotificationFilter = new AttributeChangeNotificationFilter();
        MBeanAttributeInfo[] attributes = this.modelMBeanInfo.getAttributes();
        boolean z2 = false;
        if (str == null) {
            if (attributes != null && attributes.length > 0) {
                for (MBeanAttributeInfo mBeanAttributeInfo : attributes) {
                    attributeChangeNotificationFilter.enableAttribute(mBeanAttributeInfo.getName());
                }
            }
        } else {
            if (attributes != null && attributes.length > 0) {
                int i2 = 0;
                while (true) {
                    if (i2 >= attributes.length) {
                        break;
                    }
                    if (!str.equals(attributes[i2].getName())) {
                        i2++;
                    } else {
                        z2 = true;
                        attributeChangeNotificationFilter.enableAttribute(str);
                        break;
                    }
                }
            }
            if (!z2) {
                throw new RuntimeOperationsException(new IllegalArgumentException("The attribute name does not exist"), "Exception occurred trying to add an AttributeChangeNotification listener");
            }
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            Vector<String> enabledAttributes = attributeChangeNotificationFilter.getEnabledAttributes();
            if (enabledAttributes.size() > 1) {
                string = "[" + enabledAttributes.firstElement() + ", ...]";
            } else {
                string = enabledAttributes.toString();
            }
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Set attribute change filter to " + string);
        }
        this.attributeBroadcaster.addNotificationListener(notificationListener, attributeChangeNotificationFilter, obj);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Notification listener added for " + str);
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void removeAttributeChangeNotificationListener(NotificationListener notificationListener, String str) throws MBeanException, ListenerNotFoundException, RuntimeOperationsException {
        if (notificationListener == null) {
            throw new ListenerNotFoundException("Notification listener is null");
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeAttributeChangeNotificationListener(NotificationListener, String)", "Entry");
        }
        if (this.attributeBroadcaster == null) {
            throw new ListenerNotFoundException("No attribute change notification listeners registered");
        }
        MBeanAttributeInfo[] attributes = this.modelMBeanInfo.getAttributes();
        boolean z2 = false;
        if (attributes != null && attributes.length > 0) {
            int i2 = 0;
            while (true) {
                if (i2 >= attributes.length) {
                    break;
                }
                if (!attributes[i2].getName().equals(str)) {
                    i2++;
                } else {
                    z2 = true;
                    break;
                }
            }
        }
        if (!z2 && str != null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid attribute name"), "Exception occurred trying to remove attribute change notification listener");
        }
        this.attributeBroadcaster.removeNotificationListener(notificationListener);
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeAttributeChangeNotificationListener(NotificationListener, String)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendAttributeChangeNotification(AttributeChangeNotification attributeChangeNotification) throws MBeanException, RuntimeOperationsException {
        String str;
        String str2;
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Entry");
        }
        if (attributeChangeNotification == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attribute change notification object must not be null"), "Exception occurred trying to send attribute change notification of a ModelMBean");
        }
        Object oldValue = attributeChangeNotification.getOldValue();
        Object newValue = attributeChangeNotification.getNewValue();
        if (oldValue == null) {
            oldValue = FXMLLoader.NULL_KEYWORD;
        }
        if (newValue == null) {
            newValue = FXMLLoader.NULL_KEYWORD;
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Sending AttributeChangeNotification with " + attributeChangeNotification.getAttributeName() + attributeChangeNotification.getAttributeType() + attributeChangeNotification.getNewValue() + attributeChangeNotification.getOldValue());
        }
        Descriptor descriptor = this.modelMBeanInfo.getDescriptor(attributeChangeNotification.getType(), "notification");
        Descriptor mBeanDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
        if (descriptor != null) {
            String str3 = (String) descriptor.getFieldValue("log");
            if (str3 == null && mBeanDescriptor != null) {
                str3 = (String) mBeanDescriptor.getFieldValue("log");
            }
            if (str3 != null && (str3.equalsIgnoreCase("t") || str3.equalsIgnoreCase("true"))) {
                String str4 = (String) descriptor.getFieldValue("logfile");
                if (str4 == null && mBeanDescriptor != null) {
                    str4 = (String) mBeanDescriptor.getFieldValue("logfile");
                }
                if (str4 != null) {
                    try {
                        writeToLog(str4, "LogMsg: " + new Date(attributeChangeNotification.getTimeStamp()).toString() + " " + attributeChangeNotification.getType() + " " + attributeChangeNotification.getMessage() + " Name = " + attributeChangeNotification.getAttributeName() + " Old value = " + oldValue + " New value = " + newValue);
                    } catch (Exception e2) {
                        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
                            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Failed to log " + attributeChangeNotification.getType() + " notification: ", (Throwable) e2);
                        }
                    }
                }
            }
        } else if (mBeanDescriptor != null && (str = (String) mBeanDescriptor.getFieldValue("log")) != null && ((str.equalsIgnoreCase("t") || str.equalsIgnoreCase("true")) && (str2 = (String) mBeanDescriptor.getFieldValue("logfile")) != null)) {
            try {
                writeToLog(str2, "LogMsg: " + new Date(attributeChangeNotification.getTimeStamp()).toString() + " " + attributeChangeNotification.getType() + " " + attributeChangeNotification.getMessage() + " Name = " + attributeChangeNotification.getAttributeName() + " Old value = " + oldValue + " New value = " + newValue);
            } catch (Exception e3) {
                if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
                    JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Failed to log " + attributeChangeNotification.getType() + " notification: ", (Throwable) e3);
                }
            }
        }
        if (this.attributeBroadcaster != null) {
            this.attributeBroadcaster.sendNotification(attributeChangeNotification);
        }
        if (this.generalBroadcaster != null) {
            this.generalBroadcaster.sendNotification(attributeChangeNotification);
        }
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "sent notification");
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", ToolWindow.QUIT);
        }
    }

    @Override // javax.management.modelmbean.ModelMBeanNotificationBroadcaster
    public void sendAttributeChangeNotification(Attribute attribute, Attribute attribute2) throws MBeanException, RuntimeOperationsException {
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(Attribute, Attribute)", "Entry");
        }
        if (attribute == null || attribute2 == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute object must not be null"), "Exception occurred trying to send attribute change notification of a ModelMBean");
        }
        if (!attribute.getName().equals(attribute2.getName())) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute names are not the same"), "Exception occurred trying to send attribute change notification of a ModelMBean");
        }
        Object value = attribute2.getValue();
        Object value2 = attribute.getValue();
        String name = "unknown";
        if (value != null) {
            name = value.getClass().getName();
        }
        if (value2 != null) {
            name = value2.getClass().getName();
        }
        sendAttributeChangeNotification(new AttributeChangeNotification(this, 1L, new Date().getTime(), "AttributeChangeDetected", attribute.getName(), name, attribute.getValue(), attribute2.getValue()));
        if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(Attribute, Attribute)", ToolWindow.QUIT);
        }
    }

    protected ClassLoaderRepository getClassLoaderRepository() {
        return MBeanServerFactory.getClassLoaderRepository(this.server);
    }

    private Class<?> loadClass(final String str) throws ClassNotFoundException {
        final ClassNotFoundException[] classNotFoundExceptionArr = new ClassNotFoundException[1];
        Class<?> cls = (Class) javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Class<?>>() { // from class: javax.management.modelmbean.RequiredModelMBean.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Class<?> run2() throws ClassNotFoundException {
                try {
                    ReflectUtil.checkPackageAccess(str);
                    return Class.forName(str);
                } catch (ClassNotFoundException e2) {
                    ClassLoaderRepository classLoaderRepository = RequiredModelMBean.this.getClassLoaderRepository();
                    try {
                        if (classLoaderRepository == null) {
                            throw new ClassNotFoundException(str);
                        }
                        return classLoaderRepository.loadClass(str);
                    } catch (ClassNotFoundException e3) {
                        classNotFoundExceptionArr[0] = e3;
                        return null;
                    }
                }
            }
        }, AccessController.getContext(), this.acc);
        if (classNotFoundExceptionArr[0] != null) {
            throw classNotFoundExceptionArr[0];
        }
        return cls;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        if (objectName == null) {
            throw new NullPointerException("name of RequiredModelMBean to registered is null");
        }
        this.server = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
        this.registered = bool.booleanValue();
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
        this.registered = false;
        this.server = null;
    }
}
