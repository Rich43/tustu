package com.sun.xml.internal.ws.spi.db;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/MethodSetter.class */
public class MethodSetter extends PropertySetterBase {
    private Method method;

    public MethodSetter(Method m2) {
        this.method = m2;
        this.type = m2.getParameterTypes()[0];
    }

    public Method getMethod() {
        return this.method;
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertySetter
    public <A> A getAnnotation(Class<A> cls) {
        return (A) this.method.getAnnotation(cls);
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertySetter
    public void set(final Object instance, Object resource) {
        final Object[] args = {resource};
        if (this.method.isAccessible()) {
            try {
                this.method.invoke(instance, args);
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: com.sun.xml.internal.ws.spi.db.MethodSetter.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IllegalAccessException {
                    if (!MethodSetter.this.method.isAccessible()) {
                        MethodSetter.this.method.setAccessible(true);
                    }
                    try {
                        MethodSetter.this.method.invoke(instance, args);
                        return null;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        return null;
                    }
                }
            });
        } catch (PrivilegedActionException e3) {
            e3.printStackTrace();
        }
    }
}
