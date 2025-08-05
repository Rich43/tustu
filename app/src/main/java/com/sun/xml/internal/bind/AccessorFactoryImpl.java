package com.sun.xml.internal.bind;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/bind/AccessorFactoryImpl.class */
public class AccessorFactoryImpl implements InternalAccessorFactory {
    private static AccessorFactoryImpl instance = new AccessorFactoryImpl();

    private AccessorFactoryImpl() {
    }

    public static AccessorFactoryImpl getInstance() {
        return instance;
    }

    @Override // com.sun.xml.internal.bind.AccessorFactory
    public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly) {
        return readOnly ? new Accessor.ReadOnlyFieldReflection(field) : new Accessor.FieldReflection(field);
    }

    @Override // com.sun.xml.internal.bind.InternalAccessorFactory
    public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly, boolean supressWarning) {
        return readOnly ? new Accessor.ReadOnlyFieldReflection(field, supressWarning) : new Accessor.FieldReflection(field, supressWarning);
    }

    @Override // com.sun.xml.internal.bind.AccessorFactory
    public Accessor createPropertyAccessor(Class bean, Method getter, Method setter) {
        if (getter == null) {
            return new Accessor.SetterOnlyReflection(setter);
        }
        if (setter == null) {
            return new Accessor.GetterOnlyReflection(getter);
        }
        return new Accessor.GetterSetterReflection(getter, setter);
    }
}
