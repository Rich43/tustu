package javax.management;

import com.sun.jmx.mbeanserver.Introspector;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

/* loaded from: rt.jar:javax/management/JMX.class */
public class JMX {
    static final JMX proof = new JMX();
    public static final String DEFAULT_VALUE_FIELD = "defaultValue";
    public static final String IMMUTABLE_INFO_FIELD = "immutableInfo";
    public static final String INTERFACE_CLASS_NAME_FIELD = "interfaceClassName";
    public static final String LEGAL_VALUES_FIELD = "legalValues";
    public static final String MAX_VALUE_FIELD = "maxValue";
    public static final String MIN_VALUE_FIELD = "minValue";
    public static final String MXBEAN_FIELD = "mxbean";
    public static final String OPEN_TYPE_FIELD = "openType";
    public static final String ORIGINAL_TYPE_FIELD = "originalType";

    private JMX() {
    }

    public static <T> T newMBeanProxy(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls) {
        return (T) newMBeanProxy(mBeanServerConnection, objectName, cls, false);
    }

    public static <T> T newMBeanProxy(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls, boolean z2) {
        return (T) createProxy(mBeanServerConnection, objectName, cls, z2, false);
    }

    public static <T> T newMXBeanProxy(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls) {
        return (T) newMXBeanProxy(mBeanServerConnection, objectName, cls, false);
    }

    public static <T> T newMXBeanProxy(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls, boolean z2) {
        return (T) createProxy(mBeanServerConnection, objectName, cls, z2, true);
    }

    public static boolean isMXBeanInterface(Class<?> cls) {
        if (!cls.isInterface()) {
            return false;
        }
        if (!Modifier.isPublic(cls.getModifiers()) && !Introspector.ALLOW_NONPUBLIC_MBEAN) {
            return false;
        }
        MXBean mXBean = (MXBean) cls.getAnnotation(MXBean.class);
        if (mXBean != null) {
            return mXBean.value();
        }
        return cls.getName().endsWith("MXBean");
    }

    private static <T> T createProxy(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls, boolean z2, boolean z3) {
        Class[] clsArr;
        try {
            if (z3) {
                Introspector.testComplianceMXBeanInterface(cls);
            } else {
                Introspector.testComplianceMBeanInterface(cls);
            }
            MBeanServerInvocationHandler mBeanServerInvocationHandler = new MBeanServerInvocationHandler(mBeanServerConnection, objectName, z3);
            if (z2) {
                clsArr = new Class[]{cls, NotificationEmitter.class};
            } else {
                clsArr = new Class[]{cls};
            }
            return cls.cast(Proxy.newProxyInstance(cls.getClassLoader(), clsArr, mBeanServerInvocationHandler));
        } catch (NotCompliantMBeanException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
}
