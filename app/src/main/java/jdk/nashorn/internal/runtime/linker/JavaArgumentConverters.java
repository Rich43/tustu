package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/JavaArgumentConverters.class */
final class JavaArgumentConverters {
    private static final MethodHandle TO_BOOLEAN = findOwnMH("toBoolean", Boolean.class, Object.class);
    private static final MethodHandle TO_STRING = findOwnMH("toString", String.class, Object.class);
    private static final MethodHandle TO_DOUBLE = findOwnMH("toDouble", Double.class, Object.class);
    private static final MethodHandle TO_NUMBER = findOwnMH("toNumber", Number.class, Object.class);
    private static final MethodHandle TO_LONG = findOwnMH("toLong", Long.class, Object.class);
    private static final MethodHandle TO_LONG_PRIMITIVE = findOwnMH("toLongPrimitive", Long.TYPE, Object.class);
    private static final MethodHandle TO_CHAR = findOwnMH("toChar", Character.class, Object.class);
    private static final MethodHandle TO_CHAR_PRIMITIVE = findOwnMH("toCharPrimitive", Character.TYPE, Object.class);
    private static final Map<Class<?>, MethodHandle> CONVERTERS = new HashMap();

    static {
        CONVERTERS.put(Number.class, TO_NUMBER);
        CONVERTERS.put(String.class, TO_STRING);
        CONVERTERS.put(Boolean.TYPE, JSType.TO_BOOLEAN.methodHandle());
        CONVERTERS.put(Boolean.class, TO_BOOLEAN);
        CONVERTERS.put(Character.TYPE, TO_CHAR_PRIMITIVE);
        CONVERTERS.put(Character.class, TO_CHAR);
        CONVERTERS.put(Double.TYPE, JSType.TO_NUMBER.methodHandle());
        CONVERTERS.put(Double.class, TO_DOUBLE);
        CONVERTERS.put(Long.TYPE, TO_LONG_PRIMITIVE);
        CONVERTERS.put(Long.class, TO_LONG);
        putLongConverter(Byte.class);
        putLongConverter(Short.class);
        putLongConverter(Integer.class);
        putDoubleConverter(Float.class);
    }

    private JavaArgumentConverters() {
    }

    static MethodHandle getConverter(Class<?> targetType) {
        return CONVERTERS.get(targetType);
    }

    private static Boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj == null || obj == ScriptRuntime.UNDEFINED) {
            return null;
        }
        if (obj instanceof Number) {
            double num = ((Number) obj).doubleValue();
            return Boolean.valueOf((num == 0.0d || Double.isNaN(num)) ? false : true);
        }
        if (JSType.isString(obj)) {
            return Boolean.valueOf(((CharSequence) obj).length() > 0);
        }
        if (obj instanceof ScriptObject) {
            return true;
        }
        throw assertUnexpectedType(obj);
    }

    private static Character toChar(Object o2) {
        if (o2 == null) {
            return null;
        }
        if (o2 instanceof Number) {
            int ival = ((Number) o2).intValue();
            if (ival >= 0 && ival <= 65535) {
                return Character.valueOf((char) ival);
            }
            throw ECMAErrors.typeError("cant.convert.number.to.char", new String[0]);
        }
        String s2 = toString(o2);
        if (s2 == null) {
            return null;
        }
        if (s2.length() != 1) {
            throw ECMAErrors.typeError("cant.convert.string.to.char", new String[0]);
        }
        return Character.valueOf(s2.charAt(0));
    }

    static char toCharPrimitive(Object obj0) {
        Character c2 = toChar(obj0);
        if (c2 == null) {
            return (char) 0;
        }
        return c2.charValue();
    }

    static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSType.toString(obj);
    }

    private static Double toDouble(Object obj0) {
        Object primitive = obj0;
        while (true) {
            Object obj = primitive;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Double) {
                return (Double) obj;
            }
            if (obj instanceof Number) {
                return Double.valueOf(((Number) obj).doubleValue());
            }
            if (obj instanceof String) {
                return Double.valueOf(JSType.toNumber((String) obj));
            }
            if (obj instanceof ConsString) {
                return Double.valueOf(JSType.toNumber(obj.toString()));
            }
            if (obj instanceof Boolean) {
                return Double.valueOf(((Boolean) obj).booleanValue() ? 1.0d : 0.0d);
            }
            if (obj instanceof ScriptObject) {
                primitive = JSType.toPrimitive(obj, (Class<?>) Number.class);
            } else {
                if (obj == ScriptRuntime.UNDEFINED) {
                    return Double.valueOf(Double.NaN);
                }
                throw assertUnexpectedType(obj);
            }
        }
    }

    private static Number toNumber(Object obj0) {
        Object primitive = obj0;
        while (true) {
            Object obj = primitive;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Number) {
                return (Number) obj;
            }
            if (obj instanceof String) {
                return Double.valueOf(JSType.toNumber((String) obj));
            }
            if (obj instanceof ConsString) {
                return Double.valueOf(JSType.toNumber(obj.toString()));
            }
            if (obj instanceof Boolean) {
                return Double.valueOf(((Boolean) obj).booleanValue() ? 1.0d : 0.0d);
            }
            if (obj instanceof ScriptObject) {
                primitive = JSType.toPrimitive(obj, (Class<?>) Number.class);
            } else {
                if (obj == ScriptRuntime.UNDEFINED) {
                    return Double.valueOf(Double.NaN);
                }
                throw assertUnexpectedType(obj);
            }
        }
    }

    private static Long toLong(Object obj0) {
        Object primitive = obj0;
        while (true) {
            Object obj = primitive;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Long) {
                return (Long) obj;
            }
            if (obj instanceof Integer) {
                return Long.valueOf(((Integer) obj).longValue());
            }
            if (obj instanceof Double) {
                Double d2 = (Double) obj;
                if (Double.isInfinite(d2.doubleValue())) {
                    return 0L;
                }
                return Long.valueOf(d2.longValue());
            }
            if (obj instanceof Float) {
                Float f2 = (Float) obj;
                if (Float.isInfinite(f2.floatValue())) {
                    return 0L;
                }
                return Long.valueOf(f2.longValue());
            }
            if (obj instanceof Number) {
                return Long.valueOf(((Number) obj).longValue());
            }
            if (JSType.isString(obj)) {
                return Long.valueOf(JSType.toLong(obj));
            }
            if (obj instanceof Boolean) {
                return Long.valueOf(((Boolean) obj).booleanValue() ? 1L : 0L);
            }
            if (obj instanceof ScriptObject) {
                primitive = JSType.toPrimitive(obj, (Class<?>) Number.class);
            } else {
                if (obj == ScriptRuntime.UNDEFINED) {
                    return null;
                }
                throw assertUnexpectedType(obj);
            }
        }
    }

    private static AssertionError assertUnexpectedType(Object obj) {
        return new AssertionError((Object) ("Unexpected type" + obj.getClass().getName() + ". Guards should have prevented this"));
    }

    private static long toLongPrimitive(Object obj0) {
        Long l2 = toLong(obj0);
        if (l2 == null) {
            return 0L;
        }
        return l2.longValue();
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), JavaArgumentConverters.class, name, Lookup.MH.type(rtype, types));
    }

    private static void putDoubleConverter(Class<?> targetType) {
        Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(JSType.TO_NUMBER.methodHandle(), JSType.TO_NUMBER.methodHandle().type().changeReturnType(primitive)));
        CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(TO_DOUBLE, findOwnMH(primitive.getName() + "Value", targetType, Double.class)));
    }

    private static void putLongConverter(Class<?> targetType) {
        Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(TO_LONG_PRIMITIVE, TO_LONG_PRIMITIVE.type().changeReturnType(primitive)));
        CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(TO_LONG, findOwnMH(primitive.getName() + "Value", targetType, Long.class)));
    }

    private static Byte byteValue(Long l2) {
        if (l2 == null) {
            return null;
        }
        return Byte.valueOf(l2.byteValue());
    }

    private static Short shortValue(Long l2) {
        if (l2 == null) {
            return null;
        }
        return Short.valueOf(l2.shortValue());
    }

    private static Integer intValue(Long l2) {
        if (l2 == null) {
            return null;
        }
        return Integer.valueOf(l2.intValue());
    }

    private static Float floatValue(Double d2) {
        if (d2 == null) {
            return null;
        }
        return Float.valueOf(d2.floatValue());
    }
}
