package javax.management;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.jmx.mbeanserver.MXBeanProxy;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/MBeanServerInvocationHandler.class */
public class MBeanServerInvocationHandler implements InvocationHandler {
    private static final WeakHashMap<Class<?>, WeakReference<MXBeanProxy>> mxbeanProxies = new WeakHashMap<>();
    private final MBeanServerConnection connection;
    private final ObjectName objectName;
    private final boolean isMXBean;

    public MBeanServerInvocationHandler(MBeanServerConnection mBeanServerConnection, ObjectName objectName) {
        this(mBeanServerConnection, objectName, false);
    }

    public MBeanServerInvocationHandler(MBeanServerConnection mBeanServerConnection, ObjectName objectName, boolean z2) {
        if (mBeanServerConnection == null) {
            throw new IllegalArgumentException("Null connection");
        }
        if (Proxy.isProxyClass(mBeanServerConnection.getClass()) && MBeanServerInvocationHandler.class.isAssignableFrom(Proxy.getInvocationHandler(mBeanServerConnection).getClass())) {
            throw new IllegalArgumentException("Wrapping MBeanServerInvocationHandler");
        }
        if (objectName == null) {
            throw new IllegalArgumentException("Null object name");
        }
        this.connection = mBeanServerConnection;
        this.objectName = objectName;
        this.isMXBean = z2;
    }

    public MBeanServerConnection getMBeanServerConnection() {
        return this.connection;
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    public boolean isMXBean() {
        return this.isMXBean;
    }

    public static <T> T newProxyInstance(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class<T> cls, boolean z2) {
        return (T) JMX.newMBeanProxy(mBeanServerConnection, objectName, cls, z2);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Exception {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.equals(NotificationBroadcaster.class) || declaringClass.equals(NotificationEmitter.class)) {
            return invokeBroadcasterMethod(obj, method, objArr);
        }
        if (shouldDoLocally(obj, method)) {
            return doLocally(obj, method, objArr);
        }
        try {
            if (isMXBean()) {
                return findMXBeanProxy(declaringClass).invoke(this.connection, this.objectName, method, objArr);
            }
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();
            int length = objArr == null ? 0 : objArr.length;
            if (name.startsWith("get") && name.length() > 3 && length == 0 && !returnType.equals(Void.TYPE)) {
                return this.connection.getAttribute(this.objectName, name.substring(3));
            }
            if (name.startsWith(BeanAdapter.IS_PREFIX) && name.length() > 2 && length == 0 && (returnType.equals(Boolean.TYPE) || returnType.equals(Boolean.class))) {
                return this.connection.getAttribute(this.objectName, name.substring(2));
            }
            if (name.startsWith("set") && name.length() > 3 && length == 1 && returnType.equals(Void.TYPE)) {
                this.connection.setAttribute(this.objectName, new Attribute(name.substring(3), objArr[0]));
                return null;
            }
            String[] strArr = new String[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                strArr[i2] = parameterTypes[i2].getName();
            }
            return this.connection.invoke(this.objectName, name, objArr, strArr);
        } catch (MBeanException e2) {
            throw e2.getTargetException();
        } catch (RuntimeErrorException e3) {
            throw e3.getTargetError();
        } catch (RuntimeMBeanException e4) {
            throw e4.getTargetException();
        }
    }

    private static MXBeanProxy findMXBeanProxy(Class<?> cls) {
        MXBeanProxy mXBeanProxy;
        synchronized (mxbeanProxies) {
            WeakReference<MXBeanProxy> weakReference = mxbeanProxies.get(cls);
            MXBeanProxy mXBeanProxy2 = weakReference == null ? null : weakReference.get();
            if (mXBeanProxy2 == null) {
                try {
                    mXBeanProxy2 = new MXBeanProxy(cls);
                    mxbeanProxies.put(cls, new WeakReference<>(mXBeanProxy2));
                } catch (IllegalArgumentException e2) {
                    IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Cannot make MXBean proxy for " + cls.getName() + ": " + e2.getMessage(), e2.getCause());
                    illegalArgumentException.setStackTrace(e2.getStackTrace());
                    throw illegalArgumentException;
                }
            }
            mXBeanProxy = mXBeanProxy2;
        }
        return mXBeanProxy;
    }

    private Object invokeBroadcasterMethod(Object obj, Method method, Object[] objArr) throws Exception {
        String name = method.getName();
        int length = objArr == null ? 0 : objArr.length;
        if (name.equals("addNotificationListener")) {
            if (length != 3) {
                throw new IllegalArgumentException("Bad arg count to addNotificationListener: " + length);
            }
            this.connection.addNotificationListener(this.objectName, (NotificationListener) objArr[0], (NotificationFilter) objArr[1], objArr[2]);
            return null;
        }
        if (name.equals("removeNotificationListener")) {
            NotificationListener notificationListener = (NotificationListener) objArr[0];
            switch (length) {
                case 1:
                    this.connection.removeNotificationListener(this.objectName, notificationListener);
                    return null;
                case 3:
                    this.connection.removeNotificationListener(this.objectName, notificationListener, (NotificationFilter) objArr[1], objArr[2]);
                    return null;
                default:
                    throw new IllegalArgumentException("Bad arg count to removeNotificationListener: " + length);
            }
        }
        if (name.equals("getNotificationInfo")) {
            if (objArr != null) {
                throw new IllegalArgumentException("getNotificationInfo has args");
            }
            return this.connection.getMBeanInfo(this.objectName).getNotifications();
        }
        throw new IllegalArgumentException("Bad method name: " + name);
    }

    private boolean shouldDoLocally(Object obj, Method method) {
        String name = method.getName();
        if ((name.equals("hashCode") || name.equals("toString")) && method.getParameterTypes().length == 0 && isLocal(obj, method)) {
            return true;
        }
        if (name.equals("equals") && Arrays.equals(method.getParameterTypes(), new Class[]{Object.class}) && isLocal(obj, method)) {
            return true;
        }
        if (name.equals("finalize") && method.getParameterTypes().length == 0) {
            return true;
        }
        return false;
    }

    private Object doLocally(Object obj, Method method, Object[] objArr) throws IllegalArgumentException {
        String name = method.getName();
        if (name.equals("equals")) {
            if (this == objArr[0]) {
                return true;
            }
            if (!(objArr[0] instanceof Proxy)) {
                return false;
            }
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(objArr[0]);
            if (invocationHandler == null || !(invocationHandler instanceof MBeanServerInvocationHandler)) {
                return false;
            }
            MBeanServerInvocationHandler mBeanServerInvocationHandler = (MBeanServerInvocationHandler) invocationHandler;
            return Boolean.valueOf(this.connection.equals(mBeanServerInvocationHandler.connection) && this.objectName.equals(mBeanServerInvocationHandler.objectName) && obj.getClass().equals(objArr[0].getClass()));
        }
        if (name.equals("toString")) {
            return (isMXBean() ? "MX" : PdfOps.M_TOKEN) + "BeanProxy(" + ((Object) this.connection) + "[" + ((Object) this.objectName) + "])";
        }
        if (name.equals("hashCode")) {
            return Integer.valueOf(this.objectName.hashCode() + this.connection.hashCode());
        }
        if (name.equals("finalize")) {
            return null;
        }
        throw new RuntimeException("Unexpected method name: " + name);
    }

    private static boolean isLocal(Object obj, Method method) throws SecurityException {
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if (interfaces == null) {
            return true;
        }
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> cls : interfaces) {
            try {
                cls.getMethod(name, parameterTypes);
                return false;
            } catch (NoSuchMethodException e2) {
            }
        }
        return true;
    }
}
