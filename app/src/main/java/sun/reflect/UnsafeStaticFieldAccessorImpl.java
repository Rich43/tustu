package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeStaticFieldAccessorImpl.class */
abstract class UnsafeStaticFieldAccessorImpl extends UnsafeFieldAccessorImpl {
    protected final Object base;

    static {
        Reflection.registerFieldsToFilter(UnsafeStaticFieldAccessorImpl.class, "base");
    }

    UnsafeStaticFieldAccessorImpl(Field field) {
        super(field);
        this.base = unsafe.staticFieldBase(field);
    }
}
