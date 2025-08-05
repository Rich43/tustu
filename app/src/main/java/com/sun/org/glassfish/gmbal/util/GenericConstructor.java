package com.sun.org.glassfish.gmbal.util;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/util/GenericConstructor.class */
public class GenericConstructor<T> {
    private final Object lock = new Object();
    private String typeName;
    private Class<T> resultType;
    private Class<?> type;
    private Class<?>[] signature;
    private Constructor constructor;

    public GenericConstructor(Class<T> type, String className, Class<?>... signature) {
        this.resultType = type;
        this.typeName = className;
        this.signature = (Class[]) signature.clone();
    }

    private void getConstructor() {
        synchronized (this.lock) {
            if (this.type == null || this.constructor == null) {
                try {
                    this.type = Class.forName(this.typeName);
                    this.constructor = (Constructor) AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor>() { // from class: com.sun.org.glassfish.gmbal.util.GenericConstructor.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Constructor run() throws Exception {
                            Constructor<T> declaredConstructor;
                            synchronized (GenericConstructor.this.lock) {
                                declaredConstructor = GenericConstructor.this.type.getDeclaredConstructor(GenericConstructor.this.signature);
                            }
                            return declaredConstructor;
                        }
                    });
                } catch (Exception exc) {
                    Logger.getLogger("com.sun.org.glassfish.gmbal.util").log(Level.FINE, "Failure in getConstructor", (Throwable) exc);
                }
            }
        }
    }

    public synchronized T create(Object... args) {
        T t2;
        synchronized (this.lock) {
            T result = null;
            for (int ctr = 0; ctr <= 1; ctr++) {
                getConstructor();
                if (this.constructor == null) {
                    break;
                }
                try {
                    result = this.resultType.cast(this.constructor.newInstance(args));
                    break;
                } catch (Exception exc) {
                    this.constructor = null;
                    Logger.getLogger("com.sun.org.glassfish.gmbal.util").log(Level.WARNING, "Error invoking constructor", (Throwable) exc);
                }
            }
            t2 = result;
        }
        return t2;
    }
}
