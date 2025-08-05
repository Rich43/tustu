package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeQualifiedStaticFieldAccessorImpl.class */
abstract class UnsafeQualifiedStaticFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
    protected final boolean isReadOnly;

    UnsafeQualifiedStaticFieldAccessorImpl(Field field, boolean z2) {
        super(field);
        this.isReadOnly = z2;
    }
}
