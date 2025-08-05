package java.rmi.server;

import java.io.InvalidObjectException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.WeakHashMap;
import sun.rmi.server.Util;
import sun.rmi.server.WeakClassHashMap;

/* loaded from: rt.jar:java/rmi/server/RemoteObjectInvocationHandler.class */
public class RemoteObjectInvocationHandler extends RemoteObject implements InvocationHandler {
    private static final long serialVersionUID = 2;
    private static final boolean allowFinalizeInvocation;
    private static final MethodToHash_Maps methodToHash_Maps;

    static {
        final String str = "sun.rmi.server.invocationhandler.allowFinalizeInvocation";
        String str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.rmi.server.RemoteObjectInvocationHandler.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty(str);
            }
        });
        if ("".equals(str2)) {
            allowFinalizeInvocation = true;
        } else {
            allowFinalizeInvocation = Boolean.parseBoolean(str2);
        }
        methodToHash_Maps = new MethodToHash_Maps();
    }

    public RemoteObjectInvocationHandler(RemoteRef remoteRef) {
        super(remoteRef);
        if (remoteRef == null) {
            throw new NullPointerException();
        }
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        if (!Proxy.isProxyClass(obj.getClass())) {
            throw new IllegalArgumentException("not a proxy");
        }
        if (Proxy.getInvocationHandler(obj) != this) {
            throw new IllegalArgumentException("handler mismatch");
        }
        if (method.getDeclaringClass() == Object.class) {
            return invokeObjectMethod(obj, method, objArr);
        }
        if ("finalize".equals(method.getName()) && method.getParameterCount() == 0 && !allowFinalizeInvocation) {
            return null;
        }
        return invokeRemoteMethod(obj, method, objArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Object invokeObjectMethod(java.lang.Object r6, java.lang.reflect.Method r7, java.lang.Object[] r8) throws java.lang.IllegalArgumentException {
        /*
            r5 = this;
            r0 = r7
            java.lang.String r0 = r0.getName()
            r9 = r0
            r0 = r9
            java.lang.String r1 = "hashCode"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L18
            r0 = r5
            int r0 = r0.hashCode()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            return r0
        L18:
            r0 = r9
            java.lang.String r1 = "equals"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L5d
            r0 = r8
            r1 = 0
            r0 = r0[r1]
            r10 = r0
            r0 = r6
            r1 = r10
            if (r0 == r1) goto L54
            r0 = r10
            if (r0 == 0) goto L58
            r0 = r10
            java.lang.Class r0 = r0.getClass()
            boolean r0 = java.lang.reflect.Proxy.isProxyClass(r0)
            if (r0 == 0) goto L58
            r0 = r10
            java.lang.reflect.InvocationHandler r0 = java.lang.reflect.Proxy.getInvocationHandler(r0)
            r1 = r0
            r11 = r1
            boolean r0 = r0 instanceof java.rmi.server.RemoteObjectInvocationHandler
            if (r0 == 0) goto L58
            r0 = r5
            r1 = r11
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L58
        L54:
            r0 = 1
            goto L59
        L58:
            r0 = 0
        L59:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L5d:
            r0 = r9
            java.lang.String r1 = "toString"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6d
            r0 = r5
            r1 = r6
            java.lang.String r0 = r0.proxyToString(r1)
            return r0
        L6d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "unexpected Object method: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r7
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.rmi.server.RemoteObjectInvocationHandler.invokeObjectMethod(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }

    private Object invokeRemoteMethod(Object obj, Method method, Object[] objArr) throws Exception {
        try {
            if (!(obj instanceof Remote)) {
                throw new IllegalArgumentException("proxy not Remote instance");
            }
            Class<?> declaringClass = method.getDeclaringClass();
            if (!Remote.class.isAssignableFrom(declaringClass)) {
                throw new RemoteException("Method is not Remote: " + ((Object) declaringClass) + "::" + ((Object) method));
            }
            return this.ref.invoke((Remote) obj, method, objArr, getMethodHash(method));
        } catch (Exception e2) {
            e = e2;
            if (!(e instanceof RuntimeException)) {
                try {
                    Method method2 = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
                    Class<?> cls = e.getClass();
                    for (Class<?> cls2 : method2.getExceptionTypes()) {
                        if (cls2.isAssignableFrom(cls)) {
                            throw e;
                        }
                    }
                    e = new UnexpectedException("unexpected exception", e);
                } catch (NoSuchMethodException e3) {
                    throw ((IllegalArgumentException) new IllegalArgumentException().initCause(e3));
                }
            }
            throw e;
        }
    }

    private String proxyToString(Object obj) {
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if (interfaces.length == 0) {
            return "Proxy[" + ((Object) this) + "]";
        }
        String name = interfaces[0].getName();
        if (name.equals("java.rmi.Remote") && interfaces.length > 1) {
            name = interfaces[1].getName();
        }
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf >= 0) {
            name = name.substring(iLastIndexOf + 1);
        }
        return "Proxy[" + name + "," + ((Object) this) + "]";
    }

    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("no data in stream; class: " + getClass().getName());
    }

    private static long getMethodHash(Method method) {
        return methodToHash_Maps.get(method.getDeclaringClass()).get(method).longValue();
    }

    /* loaded from: rt.jar:java/rmi/server/RemoteObjectInvocationHandler$MethodToHash_Maps.class */
    private static class MethodToHash_Maps extends WeakClassHashMap<Map<Method, Long>> {
        @Override // sun.rmi.server.WeakClassHashMap
        protected /* bridge */ /* synthetic */ Map<Method, Long> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        MethodToHash_Maps() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // sun.rmi.server.WeakClassHashMap
        protected Map<Method, Long> computeValue(Class<?> cls) {
            return new WeakHashMap<Method, Long>() { // from class: java.rmi.server.RemoteObjectInvocationHandler.MethodToHash_Maps.1
                @Override // java.util.WeakHashMap, java.util.AbstractMap, java.util.Map
                public synchronized Long get(Object obj) {
                    Long lValueOf = (Long) super.get(obj);
                    if (lValueOf == null) {
                        Method method = (Method) obj;
                        lValueOf = Long.valueOf(Util.computeMethodHash(method));
                        put(method, lValueOf);
                    }
                    return lValueOf;
                }
            };
        }
    }
}
