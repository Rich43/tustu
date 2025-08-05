package sun.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/* loaded from: rt.jar:sun/reflect/UnsafeFieldAccessorFactory.class */
class UnsafeFieldAccessorFactory {
    UnsafeFieldAccessorFactory() {
    }

    static FieldAccessor newFieldAccessor(Field field, boolean z2) {
        Class<?> type = field.getType();
        boolean zIsStatic = Modifier.isStatic(field.getModifiers());
        boolean zIsFinal = Modifier.isFinal(field.getModifiers());
        boolean z3 = zIsFinal || Modifier.isVolatile(field.getModifiers());
        boolean z4 = zIsFinal && (zIsStatic || !z2);
        if (zIsStatic) {
            UnsafeFieldAccessorImpl.unsafe.ensureClassInitialized(field.getDeclaringClass());
            if (!z3) {
                if (type == Boolean.TYPE) {
                    return new UnsafeStaticBooleanFieldAccessorImpl(field);
                }
                if (type == Byte.TYPE) {
                    return new UnsafeStaticByteFieldAccessorImpl(field);
                }
                if (type == Short.TYPE) {
                    return new UnsafeStaticShortFieldAccessorImpl(field);
                }
                if (type == Character.TYPE) {
                    return new UnsafeStaticCharacterFieldAccessorImpl(field);
                }
                if (type == Integer.TYPE) {
                    return new UnsafeStaticIntegerFieldAccessorImpl(field);
                }
                if (type == Long.TYPE) {
                    return new UnsafeStaticLongFieldAccessorImpl(field);
                }
                if (type == Float.TYPE) {
                    return new UnsafeStaticFloatFieldAccessorImpl(field);
                }
                if (type == Double.TYPE) {
                    return new UnsafeStaticDoubleFieldAccessorImpl(field);
                }
                return new UnsafeStaticObjectFieldAccessorImpl(field);
            }
            if (type == Boolean.TYPE) {
                return new UnsafeQualifiedStaticBooleanFieldAccessorImpl(field, z4);
            }
            if (type == Byte.TYPE) {
                return new UnsafeQualifiedStaticByteFieldAccessorImpl(field, z4);
            }
            if (type == Short.TYPE) {
                return new UnsafeQualifiedStaticShortFieldAccessorImpl(field, z4);
            }
            if (type == Character.TYPE) {
                return new UnsafeQualifiedStaticCharacterFieldAccessorImpl(field, z4);
            }
            if (type == Integer.TYPE) {
                return new UnsafeQualifiedStaticIntegerFieldAccessorImpl(field, z4);
            }
            if (type == Long.TYPE) {
                return new UnsafeQualifiedStaticLongFieldAccessorImpl(field, z4);
            }
            if (type == Float.TYPE) {
                return new UnsafeQualifiedStaticFloatFieldAccessorImpl(field, z4);
            }
            if (type == Double.TYPE) {
                return new UnsafeQualifiedStaticDoubleFieldAccessorImpl(field, z4);
            }
            return new UnsafeQualifiedStaticObjectFieldAccessorImpl(field, z4);
        }
        if (!z3) {
            if (type == Boolean.TYPE) {
                return new UnsafeBooleanFieldAccessorImpl(field);
            }
            if (type == Byte.TYPE) {
                return new UnsafeByteFieldAccessorImpl(field);
            }
            if (type == Short.TYPE) {
                return new UnsafeShortFieldAccessorImpl(field);
            }
            if (type == Character.TYPE) {
                return new UnsafeCharacterFieldAccessorImpl(field);
            }
            if (type == Integer.TYPE) {
                return new UnsafeIntegerFieldAccessorImpl(field);
            }
            if (type == Long.TYPE) {
                return new UnsafeLongFieldAccessorImpl(field);
            }
            if (type == Float.TYPE) {
                return new UnsafeFloatFieldAccessorImpl(field);
            }
            if (type == Double.TYPE) {
                return new UnsafeDoubleFieldAccessorImpl(field);
            }
            return new UnsafeObjectFieldAccessorImpl(field);
        }
        if (type == Boolean.TYPE) {
            return new UnsafeQualifiedBooleanFieldAccessorImpl(field, z4);
        }
        if (type == Byte.TYPE) {
            return new UnsafeQualifiedByteFieldAccessorImpl(field, z4);
        }
        if (type == Short.TYPE) {
            return new UnsafeQualifiedShortFieldAccessorImpl(field, z4);
        }
        if (type == Character.TYPE) {
            return new UnsafeQualifiedCharacterFieldAccessorImpl(field, z4);
        }
        if (type == Integer.TYPE) {
            return new UnsafeQualifiedIntegerFieldAccessorImpl(field, z4);
        }
        if (type == Long.TYPE) {
            return new UnsafeQualifiedLongFieldAccessorImpl(field, z4);
        }
        if (type == Float.TYPE) {
            return new UnsafeQualifiedFloatFieldAccessorImpl(field, z4);
        }
        if (type == Double.TYPE) {
            return new UnsafeQualifiedDoubleFieldAccessorImpl(field, z4);
        }
        return new UnsafeQualifiedObjectFieldAccessorImpl(field, z4);
    }
}
