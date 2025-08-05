package sun.corba;

import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

/* loaded from: rt.jar:sun/corba/Bridge.class */
public final class Bridge {
    private static final Class[] NO_ARGS = new Class[0];
    private static final Permission getBridgePermission = new BridgePermission("getBridge");
    private static Bridge bridge = null;
    private final Method latestUserDefinedLoaderMethod = getLatestUserDefinedLoaderMethod();
    private final Unsafe unsafe = getUnsafe();
    private final ReflectionFactory reflectionFactory = (ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
    public static final long INVALID_FIELD_OFFSET = -1;

    private Method getLatestUserDefinedLoaderMethod() {
        return (Method) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.corba.Bridge.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws SecurityException {
                try {
                    Method declaredMethod = ObjectInputStream.class.getDeclaredMethod("latestUserDefinedLoader", Bridge.NO_ARGS);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                } catch (NoSuchMethodException e2) {
                    Error error = new Error("java.io.ObjectInputStream latestUserDefinedLoader " + ((Object) e2));
                    error.initCause(e2);
                    throw error;
                }
            }
        });
    }

    private Unsafe getUnsafe() {
        try {
            return (Unsafe) ((Field) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.corba.Bridge.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() throws SecurityException {
                    try {
                        Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
                        declaredField.setAccessible(true);
                        return declaredField;
                    } catch (NoSuchFieldException e2) {
                        Error error = new Error("Could not access Unsafe");
                        error.initCause(e2);
                        throw error;
                    }
                }
            })).get(null);
        } catch (Throwable th) {
            Error error = new Error("Could not access Unsafe");
            error.initCause(th);
            throw error;
        }
    }

    private Bridge() {
    }

    public static final synchronized Bridge get() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(getBridgePermission);
        }
        if (bridge == null) {
            bridge = new Bridge();
        }
        return bridge;
    }

    public final ClassLoader getLatestUserDefinedLoader() {
        try {
            return (ClassLoader) this.latestUserDefinedLoaderMethod.invoke(null, NO_ARGS);
        } catch (IllegalAccessException e2) {
            Error error = new Error("sun.corba.Bridge.latestUserDefinedLoader: " + ((Object) e2));
            error.initCause(e2);
            throw error;
        } catch (InvocationTargetException e3) {
            Error error2 = new Error("sun.corba.Bridge.latestUserDefinedLoader: " + ((Object) e3));
            error2.initCause(e3);
            throw error2;
        }
    }

    public final int getInt(Object obj, long j2) {
        return this.unsafe.getInt(obj, j2);
    }

    public final void putInt(Object obj, long j2, int i2) {
        this.unsafe.putInt(obj, j2, i2);
    }

    public final Object getObject(Object obj, long j2) {
        return this.unsafe.getObject(obj, j2);
    }

    public final void putObject(Object obj, long j2, Object obj2) {
        this.unsafe.putObject(obj, j2, obj2);
    }

    public final boolean getBoolean(Object obj, long j2) {
        return this.unsafe.getBoolean(obj, j2);
    }

    public final void putBoolean(Object obj, long j2, boolean z2) {
        this.unsafe.putBoolean(obj, j2, z2);
    }

    public final byte getByte(Object obj, long j2) {
        return this.unsafe.getByte(obj, j2);
    }

    public final void putByte(Object obj, long j2, byte b2) {
        this.unsafe.putByte(obj, j2, b2);
    }

    public final short getShort(Object obj, long j2) {
        return this.unsafe.getShort(obj, j2);
    }

    public final void putShort(Object obj, long j2, short s2) {
        this.unsafe.putShort(obj, j2, s2);
    }

    public final char getChar(Object obj, long j2) {
        return this.unsafe.getChar(obj, j2);
    }

    public final void putChar(Object obj, long j2, char c2) {
        this.unsafe.putChar(obj, j2, c2);
    }

    public final long getLong(Object obj, long j2) {
        return this.unsafe.getLong(obj, j2);
    }

    public final void putLong(Object obj, long j2, long j3) {
        this.unsafe.putLong(obj, j2, j3);
    }

    public final float getFloat(Object obj, long j2) {
        return this.unsafe.getFloat(obj, j2);
    }

    public final void putFloat(Object obj, long j2, float f2) {
        this.unsafe.putFloat(obj, j2, f2);
    }

    public final double getDouble(Object obj, long j2) {
        return this.unsafe.getDouble(obj, j2);
    }

    public final void putDouble(Object obj, long j2, double d2) {
        this.unsafe.putDouble(obj, j2, d2);
    }

    public final long objectFieldOffset(Field field) {
        return this.unsafe.objectFieldOffset(field);
    }

    public final void throwException(Throwable th) {
        this.unsafe.throwException(th);
    }

    public final Constructor newConstructorForSerialization(Class cls, Constructor constructor) {
        return this.reflectionFactory.newConstructorForSerialization(cls, constructor);
    }
}
