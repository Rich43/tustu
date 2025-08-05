package com.sun.xml.internal.ws.spi.db;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/MethodGetter.class */
public class MethodGetter extends PropertyGetterBase {
    private Method method;

    public MethodGetter(Method m2) {
        this.method = m2;
        this.type = m2.getReturnType();
    }

    public Method getMethod() {
        return this.method;
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyGetter
    public <A> A getAnnotation(Class<A> cls) {
        return (A) this.method.getAnnotation(cls);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/MethodGetter$PrivilegedGetter.class */
    static class PrivilegedGetter implements PrivilegedExceptionAction {
        private Object value;
        private Method method;
        private Object instance;

        public PrivilegedGetter(Method m2, Object instance) {
            this.method = m2;
            this.instance = instance;
        }

        @Override // java.security.PrivilegedExceptionAction
        public Object run() throws IllegalAccessException {
            if (!this.method.isAccessible()) {
                this.method.setAccessible(true);
            }
            try {
                this.value = this.method.invoke(this.instance, new Object[0]);
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyGetter
    public Object get(Object instance) {
        Object[] args = new Object[0];
        try {
            if (this.method.isAccessible()) {
                return this.method.invoke(instance, args);
            }
            PrivilegedGetter privilegedGetter = new PrivilegedGetter(this.method, instance);
            try {
                AccessController.doPrivileged(privilegedGetter);
            } catch (PrivilegedActionException e2) {
                e2.printStackTrace();
            }
            return privilegedGetter.value;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
