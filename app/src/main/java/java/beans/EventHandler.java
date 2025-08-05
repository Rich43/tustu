package java.beans;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/EventHandler.class */
public class EventHandler implements InvocationHandler {
    private Object target;
    private String action;
    private final String eventPropertyName;
    private final String listenerMethodName;
    private final AccessControlContext acc = AccessController.getContext();

    @ConstructorProperties({"target", "action", "eventPropertyName", "listenerMethodName"})
    public EventHandler(Object obj, String str, String str2, String str3) {
        this.target = obj;
        this.action = str;
        if (obj == null) {
            throw new NullPointerException("target must be non-null");
        }
        if (str == null) {
            throw new NullPointerException("action must be non-null");
        }
        this.eventPropertyName = str2;
        this.listenerMethodName = str3;
    }

    public Object getTarget() {
        return this.target;
    }

    public String getAction() {
        return this.action;
    }

    public String getEventPropertyName() {
        return this.eventPropertyName;
    }

    public String getListenerMethodName() {
        return this.listenerMethodName;
    }

    private Object applyGetters(Object obj, String str) {
        if (str == null || str.equals("")) {
            return obj;
        }
        int iIndexOf = str.indexOf(46);
        if (iIndexOf == -1) {
            iIndexOf = str.length();
        }
        String strSubstring = str.substring(0, iIndexOf);
        String strSubstring2 = str.substring(Math.min(iIndexOf + 1, str.length()));
        Method method = null;
        if (obj != null) {
            try {
                method = Statement.getMethod(obj.getClass(), "get" + NameGenerator.capitalize(strSubstring), new Class[0]);
                if (method == null) {
                    method = Statement.getMethod(obj.getClass(), BeanAdapter.IS_PREFIX + NameGenerator.capitalize(strSubstring), new Class[0]);
                }
                if (method == null) {
                    method = Statement.getMethod(obj.getClass(), strSubstring, new Class[0]);
                }
            } catch (Exception e2) {
                throw new RuntimeException("Failed to call method: " + strSubstring + " on " + obj, e2);
            }
        }
        if (method == null) {
            throw new RuntimeException("No method called: " + strSubstring + " defined on " + obj);
        }
        return applyGetters(MethodUtil.invoke(method, obj, new Object[0]), strSubstring2);
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(final Object obj, final Method method, final Object[] objArr) {
        AccessControlContext accessControlContext = this.acc;
        if (accessControlContext == null && System.getSecurityManager() != null) {
            throw new SecurityException("AccessControlContext is not set");
        }
        return AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.beans.EventHandler.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return EventHandler.this.invokeInternal(obj, method, objArr);
            }
        }, accessControlContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object invokeInternal(Object obj, Method method, Object[] objArr) {
        Object[] objArr2;
        Class[] clsArr;
        String name = method.getName();
        if (method.getDeclaringClass() == Object.class) {
            if (name.equals("hashCode")) {
                return new Integer(System.identityHashCode(obj));
            }
            if (name.equals("equals")) {
                return obj == objArr[0] ? Boolean.TRUE : Boolean.FALSE;
            }
            if (name.equals("toString")) {
                return obj.getClass().getName() + '@' + Integer.toHexString(obj.hashCode());
            }
        }
        if (this.listenerMethodName == null || this.listenerMethodName.equals(name)) {
            if (this.eventPropertyName == null) {
                objArr2 = new Object[0];
                clsArr = new Class[0];
            } else {
                Object objApplyGetters = applyGetters(objArr[0], getEventPropertyName());
                objArr2 = new Object[]{objApplyGetters};
                Class[] clsArr2 = new Class[1];
                clsArr2[0] = objApplyGetters == null ? null : objApplyGetters.getClass();
                clsArr = clsArr2;
            }
            try {
                int iLastIndexOf = this.action.lastIndexOf(46);
                if (iLastIndexOf != -1) {
                    this.target = applyGetters(this.target, this.action.substring(0, iLastIndexOf));
                    this.action = this.action.substring(iLastIndexOf + 1);
                }
                Method method2 = Statement.getMethod(this.target.getClass(), this.action, clsArr);
                if (method2 == null) {
                    method2 = Statement.getMethod(this.target.getClass(), "set" + NameGenerator.capitalize(this.action), clsArr);
                }
                if (method2 == null) {
                    throw new RuntimeException("No method called " + this.action + " on " + ((Object) this.target.getClass()) + (clsArr.length == 0 ? " with no arguments" : " with argument " + ((Object) clsArr[0])));
                }
                return MethodUtil.invoke(method2, this.target, objArr2);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw ((RuntimeException) targetException);
                }
                throw new RuntimeException(targetException);
            }
        }
        return null;
    }

    public static <T> T create(Class<T> cls, Object obj, String str) {
        return (T) create(cls, obj, str, null, null);
    }

    public static <T> T create(Class<T> cls, Object obj, String str, String str2) {
        return (T) create(cls, obj, str, str2, null);
    }

    public static <T> T create(Class<T> cls, Object obj, String str, String str2, String str3) {
        final EventHandler eventHandler = new EventHandler(obj, str, str2, str3);
        if (cls == null) {
            throw new NullPointerException("listenerInterface must be non-null");
        }
        final ClassLoader classLoader = getClassLoader(cls);
        final Class[] clsArr = {cls};
        return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: java.beans.EventHandler.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public T run2() {
                return (T) Proxy.newProxyInstance(classLoader, clsArr, eventHandler);
            }
        });
    }

    private static ClassLoader getClassLoader(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        ClassLoader classLoader = cls.getClassLoader();
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }
}
