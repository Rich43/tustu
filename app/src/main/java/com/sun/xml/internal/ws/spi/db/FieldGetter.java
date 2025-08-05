package com.sun.xml.internal.ws.spi.db;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/FieldGetter.class */
public class FieldGetter extends PropertyGetterBase {
    protected Field field;

    public FieldGetter(Field f2) {
        this.field = f2;
        this.type = f2.getType();
    }

    public Field getField() {
        return this.field;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/FieldGetter$PrivilegedGetter.class */
    static class PrivilegedGetter implements PrivilegedExceptionAction {
        private Object value;
        private Field field;
        private Object instance;

        public PrivilegedGetter(Field field, Object instance) {
            this.field = field;
            this.instance = instance;
        }

        @Override // java.security.PrivilegedExceptionAction
        public Object run() throws IllegalAccessException {
            if (!this.field.isAccessible()) {
                this.field.setAccessible(true);
            }
            this.value = this.field.get(this.instance);
            return null;
        }
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyGetter
    public Object get(Object instance) {
        if (this.field.isAccessible()) {
            try {
                return this.field.get(instance);
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        PrivilegedGetter privilegedGetter = new PrivilegedGetter(this.field, instance);
        try {
            AccessController.doPrivileged(privilegedGetter);
        } catch (PrivilegedActionException e3) {
            e3.printStackTrace();
        }
        return privilegedGetter.value;
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyGetter
    public <A> A getAnnotation(Class<A> cls) {
        return (A) this.field.getAnnotation(cls);
    }
}
