package com.sun.xml.internal.ws.spi.db;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/FieldSetter.class */
public class FieldSetter extends PropertySetterBase {
    protected Field field;

    public FieldSetter(Field f2) {
        this.field = f2;
        this.type = f2.getType();
    }

    public Field getField() {
        return this.field;
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertySetter
    public void set(final Object instance, final Object resource) {
        if (this.field.isAccessible()) {
            try {
                this.field.set(instance, resource);
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: com.sun.xml.internal.ws.spi.db.FieldSetter.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IllegalAccessException, IllegalArgumentException {
                    if (!FieldSetter.this.field.isAccessible()) {
                        FieldSetter.this.field.setAccessible(true);
                    }
                    FieldSetter.this.field.set(instance, resource);
                    return null;
                }
            });
        } catch (PrivilegedActionException e3) {
            e3.printStackTrace();
        }
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertySetter
    public <A> A getAnnotation(Class<A> cls) {
        return (A) this.field.getAnnotation(cls);
    }
}
