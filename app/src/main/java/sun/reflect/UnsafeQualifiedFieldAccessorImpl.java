package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeQualifiedFieldAccessorImpl.class */
abstract class UnsafeQualifiedFieldAccessorImpl extends UnsafeFieldAccessorImpl {
    protected final boolean isReadOnly;

    UnsafeQualifiedFieldAccessorImpl(Field field, boolean z2) {
        super(field);
        this.isReadOnly = z2;
    }
}
