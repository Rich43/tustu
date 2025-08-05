package javax.management;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.DescriptorCache;
import com.sun.jmx.mbeanserver.Introspector;
import com.sun.jmx.mbeanserver.MBeanSupport;
import com.sun.jmx.mbeanserver.MXBeanSupport;
import com.sun.jmx.mbeanserver.StandardMBeanSupport;
import com.sun.jmx.mbeanserver.Util;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import javax.management.openmbean.OpenMBeanAttributeInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfo;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;

/* loaded from: rt.jar:javax/management/StandardMBean.class */
public class StandardMBean implements DynamicMBean, MBeanRegistration {
    private volatile MBeanSupport<?> mbean;
    private volatile MBeanInfo cachedMBeanInfo;
    private static final DescriptorCache descriptors = DescriptorCache.getInstance(JMX.proof);
    private static final Map<Class<?>, Boolean> mbeanInfoSafeMap = new WeakHashMap();

    /* JADX WARN: Multi-variable type inference failed */
    private <T> void construct(T t2, Class<T> cls, boolean z2, boolean z3) throws NotCompliantMBeanException {
        if (t2 == null) {
            if (z2) {
                t2 = Util.cast(this);
            } else {
                throw new IllegalArgumentException("implementation is null");
            }
        }
        if (z3) {
            if (cls == null) {
                cls = (Class) Util.cast(Introspector.getMXBeanInterface(t2.getClass()));
            }
            this.mbean = new MXBeanSupport(t2, cls);
        } else {
            if (cls == null) {
                cls = (Class) Util.cast(Introspector.getStandardMBeanInterface(t2.getClass()));
            }
            this.mbean = new StandardMBeanSupport(t2, cls);
        }
    }

    public <T> StandardMBean(T t2, Class<T> cls) throws NotCompliantMBeanException {
        construct(t2, cls, false, false);
    }

    protected StandardMBean(Class<?> cls) throws NotCompliantMBeanException {
        construct(null, cls, true, false);
    }

    public <T> StandardMBean(T t2, Class<T> cls, boolean z2) {
        try {
            construct(t2, cls, false, z2);
        } catch (NotCompliantMBeanException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    protected StandardMBean(Class<?> cls, boolean z2) {
        try {
            construct(null, cls, true, z2);
        } catch (NotCompliantMBeanException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public void setImplementation(Object obj) throws NotCompliantMBeanException {
        if (obj == null) {
            throw new IllegalArgumentException("implementation is null");
        }
        if (isMXBean()) {
            this.mbean = new MXBeanSupport(obj, (Class) Util.cast(getMBeanInterface()));
        } else {
            this.mbean = new StandardMBeanSupport(obj, (Class) Util.cast(getMBeanInterface()));
        }
    }

    public Object getImplementation() {
        return this.mbean.getResource();
    }

    public final Class<?> getMBeanInterface() {
        return this.mbean.getMBeanInterface();
    }

    public Class<?> getImplementationClass() {
        return this.mbean.getResource().getClass();
    }

    @Override // javax.management.DynamicMBean
    public Object getAttribute(String str) throws MBeanException, AttributeNotFoundException, ReflectionException {
        return this.mbean.getAttribute(str);
    }

    @Override // javax.management.DynamicMBean
    public void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException {
        this.mbean.setAttribute(attribute);
    }

    @Override // javax.management.DynamicMBean
    public AttributeList getAttributes(String[] strArr) {
        return this.mbean.getAttributes(strArr);
    }

    @Override // javax.management.DynamicMBean
    public AttributeList setAttributes(AttributeList attributeList) {
        return this.mbean.setAttributes(attributeList);
    }

    @Override // javax.management.DynamicMBean
    public Object invoke(String str, Object[] objArr, String[] strArr) throws MBeanException, ReflectionException {
        return this.mbean.invoke(str, objArr, strArr);
    }

    @Override // javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        try {
            MBeanInfo cachedMBeanInfo = getCachedMBeanInfo();
            if (cachedMBeanInfo != null) {
                return cachedMBeanInfo;
            }
        } catch (RuntimeException e2) {
            if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MISC_LOGGER.logp(Level.FINEST, MBeanServerFactory.class.getName(), "getMBeanInfo", "Failed to get cached MBeanInfo", (Throwable) e2);
            }
        }
        if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MISC_LOGGER.logp(Level.FINER, MBeanServerFactory.class.getName(), "getMBeanInfo", "Building MBeanInfo for " + getImplementationClass().getName());
        }
        MBeanSupport<?> mBeanSupport = this.mbean;
        MBeanInfo mBeanInfo = mBeanSupport.getMBeanInfo();
        MBeanInfo mBeanInfo2 = new MBeanInfo(getClassName(mBeanInfo), getDescription(mBeanInfo), getAttributes(mBeanInfo), getConstructors(mBeanInfo, mBeanSupport.getResource()), getOperations(mBeanInfo), getNotifications(mBeanInfo), getDescriptor(mBeanInfo, immutableInfo(getClass())));
        try {
            cacheMBeanInfo(mBeanInfo2);
        } catch (RuntimeException e3) {
            if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MISC_LOGGER.logp(Level.FINEST, MBeanServerFactory.class.getName(), "getMBeanInfo", "Failed to cache MBeanInfo", (Throwable) e3);
            }
        }
        return mBeanInfo2;
    }

    protected String getClassName(MBeanInfo mBeanInfo) {
        return mBeanInfo == null ? getImplementationClass().getName() : mBeanInfo.getClassName();
    }

    protected String getDescription(MBeanInfo mBeanInfo) {
        if (mBeanInfo == null) {
            return null;
        }
        return mBeanInfo.getDescription();
    }

    protected String getDescription(MBeanFeatureInfo mBeanFeatureInfo) {
        if (mBeanFeatureInfo == null) {
            return null;
        }
        return mBeanFeatureInfo.getDescription();
    }

    protected String getDescription(MBeanAttributeInfo mBeanAttributeInfo) {
        return getDescription((MBeanFeatureInfo) mBeanAttributeInfo);
    }

    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo) {
        return getDescription((MBeanFeatureInfo) mBeanConstructorInfo);
    }

    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int i2) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getDescription();
    }

    protected String getParameterName(MBeanConstructorInfo mBeanConstructorInfo, MBeanParameterInfo mBeanParameterInfo, int i2) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getName();
    }

    protected String getDescription(MBeanOperationInfo mBeanOperationInfo) {
        return getDescription((MBeanFeatureInfo) mBeanOperationInfo);
    }

    protected int getImpact(MBeanOperationInfo mBeanOperationInfo) {
        if (mBeanOperationInfo == null) {
            return 3;
        }
        return mBeanOperationInfo.getImpact();
    }

    protected String getParameterName(MBeanOperationInfo mBeanOperationInfo, MBeanParameterInfo mBeanParameterInfo, int i2) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getName();
    }

    protected String getDescription(MBeanOperationInfo mBeanOperationInfo, MBeanParameterInfo mBeanParameterInfo, int i2) {
        if (mBeanParameterInfo == null) {
            return null;
        }
        return mBeanParameterInfo.getDescription();
    }

    protected MBeanConstructorInfo[] getConstructors(MBeanConstructorInfo[] mBeanConstructorInfoArr, Object obj) {
        if (mBeanConstructorInfoArr == null) {
            return null;
        }
        if (obj == null || obj == this) {
            return mBeanConstructorInfoArr;
        }
        return null;
    }

    MBeanNotificationInfo[] getNotifications(MBeanInfo mBeanInfo) {
        return null;
    }

    Descriptor getDescriptor(MBeanInfo mBeanInfo, boolean z2) {
        ImmutableDescriptor immutableDescriptor;
        if (mBeanInfo == null || mBeanInfo.getDescriptor() == null || mBeanInfo.getDescriptor().getFieldNames().length == 0) {
            immutableDescriptor = descriptors.get(new ImmutableDescriptor("interfaceClassName=" + getMBeanInterface().getName(), "immutableInfo=" + z2));
        } else {
            Descriptor descriptor = mBeanInfo.getDescriptor();
            HashMap map = new HashMap();
            for (String str : descriptor.getFieldNames()) {
                if (str.equals("immutableInfo")) {
                    map.put(str, Boolean.toString(z2));
                } else {
                    map.put(str, descriptor.getFieldValue(str));
                }
            }
            immutableDescriptor = new ImmutableDescriptor(map);
        }
        return immutableDescriptor;
    }

    protected MBeanInfo getCachedMBeanInfo() {
        return this.cachedMBeanInfo;
    }

    protected void cacheMBeanInfo(MBeanInfo mBeanInfo) {
        this.cachedMBeanInfo = mBeanInfo;
    }

    private boolean isMXBean() {
        return this.mbean.isMXBean();
    }

    private static <T> boolean identicalArrays(T[] tArr, T[] tArr2) {
        if (tArr == tArr2) {
            return true;
        }
        if (tArr == null || tArr2 == null || tArr.length != tArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < tArr.length; i2++) {
            if (tArr[i2] != tArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private static <T> boolean equal(T t2, T t3) {
        if (t2 == t3) {
            return true;
        }
        if (t2 == null || t3 == null) {
            return false;
        }
        return t2.equals(t3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static MBeanParameterInfo customize(MBeanParameterInfo mBeanParameterInfo, String str, String str2) {
        if (equal(str, mBeanParameterInfo.getName()) && equal(str2, mBeanParameterInfo.getDescription())) {
            return mBeanParameterInfo;
        }
        if (mBeanParameterInfo instanceof OpenMBeanParameterInfo) {
            return new OpenMBeanParameterInfoSupport(str, str2, ((OpenMBeanParameterInfo) mBeanParameterInfo).getOpenType(), mBeanParameterInfo.getDescriptor());
        }
        return new MBeanParameterInfo(str, mBeanParameterInfo.getType(), str2, mBeanParameterInfo.getDescriptor());
    }

    private static MBeanConstructorInfo customize(MBeanConstructorInfo mBeanConstructorInfo, String str, MBeanParameterInfo[] mBeanParameterInfoArr) {
        if (equal(str, mBeanConstructorInfo.getDescription()) && identicalArrays(mBeanParameterInfoArr, mBeanConstructorInfo.getSignature())) {
            return mBeanConstructorInfo;
        }
        if (mBeanConstructorInfo instanceof OpenMBeanConstructorInfo) {
            return new OpenMBeanConstructorInfoSupport(mBeanConstructorInfo.getName(), str, paramsToOpenParams(mBeanParameterInfoArr), mBeanConstructorInfo.getDescriptor());
        }
        return new MBeanConstructorInfo(mBeanConstructorInfo.getName(), str, mBeanParameterInfoArr, mBeanConstructorInfo.getDescriptor());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static MBeanOperationInfo customize(MBeanOperationInfo mBeanOperationInfo, String str, MBeanParameterInfo[] mBeanParameterInfoArr, int i2) {
        if (equal(str, mBeanOperationInfo.getDescription()) && identicalArrays(mBeanParameterInfoArr, mBeanOperationInfo.getSignature()) && i2 == mBeanOperationInfo.getImpact()) {
            return mBeanOperationInfo;
        }
        if (mBeanOperationInfo instanceof OpenMBeanOperationInfo) {
            return new OpenMBeanOperationInfoSupport(mBeanOperationInfo.getName(), str, paramsToOpenParams(mBeanParameterInfoArr), ((OpenMBeanOperationInfo) mBeanOperationInfo).getReturnOpenType(), i2, mBeanOperationInfo.getDescriptor());
        }
        return new MBeanOperationInfo(mBeanOperationInfo.getName(), str, mBeanParameterInfoArr, mBeanOperationInfo.getReturnType(), i2, mBeanOperationInfo.getDescriptor());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static MBeanAttributeInfo customize(MBeanAttributeInfo mBeanAttributeInfo, String str) {
        if (equal(str, mBeanAttributeInfo.getDescription())) {
            return mBeanAttributeInfo;
        }
        if (mBeanAttributeInfo instanceof OpenMBeanAttributeInfo) {
            return new OpenMBeanAttributeInfoSupport(mBeanAttributeInfo.getName(), str, ((OpenMBeanAttributeInfo) mBeanAttributeInfo).getOpenType(), mBeanAttributeInfo.isReadable(), mBeanAttributeInfo.isWritable(), mBeanAttributeInfo.isIs(), mBeanAttributeInfo.getDescriptor());
        }
        return new MBeanAttributeInfo(mBeanAttributeInfo.getName(), mBeanAttributeInfo.getType(), str, mBeanAttributeInfo.isReadable(), mBeanAttributeInfo.isWritable(), mBeanAttributeInfo.isIs(), mBeanAttributeInfo.getDescriptor());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static OpenMBeanParameterInfo[] paramsToOpenParams(MBeanParameterInfo[] mBeanParameterInfoArr) {
        if (mBeanParameterInfoArr instanceof OpenMBeanParameterInfo[]) {
            return (OpenMBeanParameterInfo[]) mBeanParameterInfoArr;
        }
        OpenMBeanParameterInfoSupport[] openMBeanParameterInfoSupportArr = new OpenMBeanParameterInfoSupport[mBeanParameterInfoArr.length];
        System.arraycopy(mBeanParameterInfoArr, 0, openMBeanParameterInfoSupportArr, 0, mBeanParameterInfoArr.length);
        return openMBeanParameterInfoSupportArr;
    }

    private MBeanConstructorInfo[] getConstructors(MBeanInfo mBeanInfo, Object obj) {
        MBeanParameterInfo[] mBeanParameterInfoArr;
        MBeanConstructorInfo[] constructors = getConstructors(mBeanInfo.getConstructors(), obj);
        if (constructors == null) {
            return null;
        }
        int length = constructors.length;
        MBeanConstructorInfo[] mBeanConstructorInfoArr = new MBeanConstructorInfo[length];
        for (int i2 = 0; i2 < length; i2++) {
            MBeanConstructorInfo mBeanConstructorInfo = constructors[i2];
            MBeanParameterInfo[] signature = mBeanConstructorInfo.getSignature();
            if (signature != null) {
                int length2 = signature.length;
                mBeanParameterInfoArr = new MBeanParameterInfo[length2];
                for (int i3 = 0; i3 < length2; i3++) {
                    MBeanParameterInfo mBeanParameterInfo = signature[i3];
                    mBeanParameterInfoArr[i3] = customize(mBeanParameterInfo, getParameterName(mBeanConstructorInfo, mBeanParameterInfo, i3), getDescription(mBeanConstructorInfo, mBeanParameterInfo, i3));
                }
            } else {
                mBeanParameterInfoArr = null;
            }
            mBeanConstructorInfoArr[i2] = customize(mBeanConstructorInfo, getDescription(mBeanConstructorInfo), mBeanParameterInfoArr);
        }
        return mBeanConstructorInfoArr;
    }

    private MBeanOperationInfo[] getOperations(MBeanInfo mBeanInfo) {
        MBeanParameterInfo[] mBeanParameterInfoArr;
        MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        if (operations == null) {
            return null;
        }
        int length = operations.length;
        MBeanOperationInfo[] mBeanOperationInfoArr = new MBeanOperationInfo[length];
        for (int i2 = 0; i2 < length; i2++) {
            MBeanOperationInfo mBeanOperationInfo = operations[i2];
            MBeanParameterInfo[] signature = mBeanOperationInfo.getSignature();
            if (signature != null) {
                int length2 = signature.length;
                mBeanParameterInfoArr = new MBeanParameterInfo[length2];
                for (int i3 = 0; i3 < length2; i3++) {
                    MBeanParameterInfo mBeanParameterInfo = signature[i3];
                    mBeanParameterInfoArr[i3] = customize(mBeanParameterInfo, getParameterName(mBeanOperationInfo, mBeanParameterInfo, i3), getDescription(mBeanOperationInfo, mBeanParameterInfo, i3));
                }
            } else {
                mBeanParameterInfoArr = null;
            }
            mBeanOperationInfoArr[i2] = customize(mBeanOperationInfo, getDescription(mBeanOperationInfo), mBeanParameterInfoArr, getImpact(mBeanOperationInfo));
        }
        return mBeanOperationInfoArr;
    }

    private MBeanAttributeInfo[] getAttributes(MBeanInfo mBeanInfo) {
        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
        if (attributes == null) {
            return null;
        }
        int length = attributes.length;
        MBeanAttributeInfo[] mBeanAttributeInfoArr = new MBeanAttributeInfo[length];
        for (int i2 = 0; i2 < length; i2++) {
            MBeanAttributeInfo mBeanAttributeInfo = attributes[i2];
            mBeanAttributeInfoArr[i2] = customize(mBeanAttributeInfo, getDescription(mBeanAttributeInfo));
        }
        return mBeanAttributeInfoArr;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        this.mbean.register(mBeanServer, objectName);
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
        if (!bool.booleanValue()) {
            this.mbean.unregister();
        }
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
        this.mbean.unregister();
    }

    static boolean immutableInfo(Class<? extends StandardMBean> cls) {
        boolean zBooleanValue;
        if (cls == StandardMBean.class || cls == StandardEmitterMBean.class) {
            return true;
        }
        synchronized (mbeanInfoSafeMap) {
            Boolean bool = mbeanInfoSafeMap.get(cls);
            if (bool == null) {
                try {
                    bool = (Boolean) AccessController.doPrivileged(new MBeanInfoSafeAction(cls));
                } catch (Exception e2) {
                    bool = false;
                }
                mbeanInfoSafeMap.put(cls, bool);
                zBooleanValue = bool.booleanValue();
            } else {
                zBooleanValue = bool.booleanValue();
            }
        }
        return zBooleanValue;
    }

    static boolean overrides(Class<?> cls, Class<?> cls2, String str, Class<?>... clsArr) throws SecurityException {
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls3 = superclass;
            if (cls3 != cls2) {
                try {
                    cls3.getDeclaredMethod(str, clsArr);
                    return true;
                } catch (NoSuchMethodException e2) {
                    superclass = cls3.getSuperclass();
                }
            } else {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:javax/management/StandardMBean$MBeanInfoSafeAction.class */
    private static class MBeanInfoSafeAction implements PrivilegedAction<Boolean> {
        private final Class<?> subclass;

        MBeanInfoSafeAction(Class<?> cls) {
            this.subclass = cls;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Boolean run2() {
            if (!StandardMBean.overrides(this.subclass, StandardMBean.class, "cacheMBeanInfo", MBeanInfo.class) && !StandardMBean.overrides(this.subclass, StandardMBean.class, "getCachedMBeanInfo", (Class[]) null) && !StandardMBean.overrides(this.subclass, StandardMBean.class, "getMBeanInfo", (Class[]) null)) {
                if (StandardEmitterMBean.class.isAssignableFrom(this.subclass) && StandardMBean.overrides(this.subclass, StandardEmitterMBean.class, "getNotificationInfo", (Class[]) null)) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}
