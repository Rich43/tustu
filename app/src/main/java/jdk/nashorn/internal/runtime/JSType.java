package jdk.nashorn.internal.runtime;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/JSType.class */
public enum JSType {
    UNDEFINED("undefined"),
    NULL("object"),
    BOOLEAN("boolean"),
    NUMBER("number"),
    STRING("string"),
    OBJECT("object"),
    FUNCTION(Constants.EXSLT_ELEMNAME_FUNCTION_STRING);

    private final String typeName;
    public static final long MAX_UINT = 4294967295L;
    private static final MethodHandles.Lookup JSTYPE_LOOKUP;
    public static final CompilerConstants.Call TO_BOOLEAN;
    public static final CompilerConstants.Call TO_BOOLEAN_D;
    public static final CompilerConstants.Call TO_INTEGER;
    public static final CompilerConstants.Call TO_LONG;
    public static final CompilerConstants.Call TO_LONG_D;
    public static final CompilerConstants.Call TO_NUMBER;
    public static final CompilerConstants.Call TO_NUMBER_OPTIMISTIC;
    public static final CompilerConstants.Call TO_STRING;
    public static final CompilerConstants.Call TO_INT32;
    public static final CompilerConstants.Call TO_INT32_L;
    public static final CompilerConstants.Call TO_INT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_INT32_D;
    public static final CompilerConstants.Call TO_UINT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_UINT32_DOUBLE;
    public static final CompilerConstants.Call TO_UINT32;
    public static final CompilerConstants.Call TO_UINT32_D;
    public static final CompilerConstants.Call TO_STRING_D;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_STRING;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_CHARSEQUENCE;
    public static final CompilerConstants.Call THROW_UNWARRANTED;
    public static final CompilerConstants.Call ADD_EXACT;
    public static final CompilerConstants.Call SUB_EXACT;
    public static final CompilerConstants.Call MUL_EXACT;
    public static final CompilerConstants.Call DIV_EXACT;
    public static final CompilerConstants.Call DIV_ZERO;
    public static final CompilerConstants.Call REM_ZERO;
    public static final CompilerConstants.Call REM_EXACT;
    public static final CompilerConstants.Call DECREMENT_EXACT;
    public static final CompilerConstants.Call INCREMENT_EXACT;
    public static final CompilerConstants.Call NEGATE_EXACT;
    public static final CompilerConstants.Call TO_JAVA_ARRAY;
    public static final CompilerConstants.Call VOID_RETURN;
    public static final CompilerConstants.Call IS_STRING;
    public static final CompilerConstants.Call IS_NUMBER;
    private static final List<Type> ACCESSOR_TYPES;
    public static final int TYPE_UNDEFINED_INDEX = -1;
    public static final int TYPE_INT_INDEX = 0;
    public static final int TYPE_DOUBLE_INDEX = 1;
    public static final int TYPE_OBJECT_INDEX = 2;
    public static final List<MethodHandle> CONVERT_OBJECT;
    public static final List<MethodHandle> CONVERT_OBJECT_OPTIMISTIC;
    public static final int UNDEFINED_INT = 0;
    public static final long UNDEFINED_LONG = 0;
    public static final double UNDEFINED_DOUBLE = Double.NaN;
    private static final long MAX_PRECISE_DOUBLE = 9007199254740992L;
    private static final long MIN_PRECISE_DOUBLE = -9007199254740992L;
    public static final List<MethodHandle> GET_UNDEFINED;
    private static final double INT32_LIMIT = 4.294967296E9d;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JSType.class.desiredAssertionStatus();
        JSTYPE_LOOKUP = MethodHandles.lookup();
        TO_BOOLEAN = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Object.class);
        TO_BOOLEAN_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Double.TYPE);
        TO_INTEGER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInteger", Integer.TYPE, Object.class);
        TO_LONG = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Object.class);
        TO_LONG_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Double.TYPE);
        TO_NUMBER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toNumber", Double.TYPE, Object.class);
        TO_NUMBER_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toNumberOptimistic", Double.TYPE, Object.class, Integer.TYPE);
        TO_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toString", String.class, Object.class);
        TO_INT32 = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Object.class);
        TO_INT32_L = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Long.TYPE);
        TO_INT32_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32Optimistic", Integer.TYPE, Object.class, Integer.TYPE);
        TO_INT32_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Double.TYPE);
        TO_UINT32_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32Optimistic", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_UINT32_DOUBLE = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32Double", Double.TYPE, Integer.TYPE);
        TO_UINT32 = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Object.class);
        TO_UINT32_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Double.TYPE);
        TO_STRING_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toString", String.class, Double.TYPE);
        TO_PRIMITIVE_TO_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toPrimitiveToString", String.class, Object.class);
        TO_PRIMITIVE_TO_CHARSEQUENCE = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toPrimitiveToCharSequence", CharSequence.class, Object.class);
        THROW_UNWARRANTED = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "throwUnwarrantedOptimismException", Object.class, Object.class, Integer.TYPE);
        ADD_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "addExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        SUB_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "subExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        MUL_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "mulExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "divExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_ZERO = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "divZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_ZERO = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "remZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "remExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DECREMENT_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "decrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        INCREMENT_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "incrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        NEGATE_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "negateExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_JAVA_ARRAY = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toJavaArray", Object.class, Object.class, Class.class);
        VOID_RETURN = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "voidReturn", Void.TYPE, new Class[0]);
        IS_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "isString", Boolean.TYPE, Object.class);
        IS_NUMBER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "isNumber", Boolean.TYPE, Object.class);
        ACCESSOR_TYPES = Collections.unmodifiableList(Arrays.asList(Type.INT, Type.NUMBER, Type.OBJECT));
        CONVERT_OBJECT = toUnmodifiableList(TO_INT32.methodHandle(), TO_NUMBER.methodHandle(), null);
        CONVERT_OBJECT_OPTIMISTIC = toUnmodifiableList(TO_INT32_OPTIMISTIC.methodHandle(), TO_NUMBER_OPTIMISTIC.methodHandle(), null);
        GET_UNDEFINED = toUnmodifiableList(Lookup.MH.constant(Integer.TYPE, 0), Lookup.MH.constant(Double.TYPE, Double.valueOf(Double.NaN)), Lookup.MH.constant(Object.class, Undefined.getUndefined()));
    }

    JSType(String typeName) {
        this.typeName = typeName;
    }

    public final String typeName() {
        return this.typeName;
    }

    public static JSType of(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (obj instanceof ScriptObject) {
            return obj instanceof ScriptFunction ? FUNCTION : OBJECT;
        }
        if (obj instanceof Boolean) {
            return BOOLEAN;
        }
        if (isString(obj)) {
            return STRING;
        }
        if (isNumber(obj)) {
            return NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return UNDEFINED;
        }
        return Bootstrap.isCallable(obj) ? FUNCTION : OBJECT;
    }

    public static JSType ofNoFunction(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (obj instanceof ScriptObject) {
            return OBJECT;
        }
        if (obj instanceof Boolean) {
            return BOOLEAN;
        }
        if (isString(obj)) {
            return STRING;
        }
        if (isNumber(obj)) {
            return NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return UNDEFINED;
        }
        return OBJECT;
    }

    public static void voidReturn() {
    }

    public static boolean isRepresentableAsInt(long number) {
        return ((long) ((int) number)) == number;
    }

    public static boolean isRepresentableAsInt(double number) {
        return ((double) ((int) number)) == number;
    }

    public static boolean isStrictlyRepresentableAsInt(double number) {
        return isRepresentableAsInt(number) && isNotNegativeZero(number);
    }

    public static boolean isRepresentableAsInt(Object obj) {
        if (obj instanceof Number) {
            return isRepresentableAsInt(((Number) obj).doubleValue());
        }
        return false;
    }

    public static boolean isRepresentableAsLong(double number) {
        return ((double) ((long) number)) == number;
    }

    public static boolean isRepresentableAsDouble(long number) {
        return MAX_PRECISE_DOUBLE >= number && number >= MIN_PRECISE_DOUBLE;
    }

    private static boolean isNotNegativeZero(double number) {
        return Double.doubleToRawLongBits(number) != Long.MIN_VALUE;
    }

    public static boolean isPrimitive(Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED || isString(obj) || isNumber(obj) || (obj instanceof Boolean);
    }

    public static Object toPrimitive(Object obj) {
        return toPrimitive(obj, (Class<?>) null);
    }

    public static Object toPrimitive(Object obj, Class<?> hint) {
        if (obj instanceof ScriptObject) {
            return toPrimitive((ScriptObject) obj, hint);
        }
        if (isPrimitive(obj)) {
            return obj;
        }
        if (hint == Number.class && (obj instanceof Number)) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof JSObject) {
            return toPrimitive((JSObject) obj, hint);
        }
        if (obj instanceof StaticClass) {
            String name = ((StaticClass) obj).getRepresentedClass().getName();
            return new StringBuilder(12 + name.length()).append("[JavaClass ").append(name).append(']').toString();
        }
        return obj.toString();
    }

    private static Object toPrimitive(ScriptObject sobj, Class<?> hint) {
        return requirePrimitive(sobj.getDefaultValue(hint));
    }

    private static Object requirePrimitive(Object result) {
        if (!isPrimitive(result)) {
            throw ECMAErrors.typeError("bad.default.value", result.toString());
        }
        return result;
    }

    public static Object toPrimitive(JSObject jsobj, Class<?> hint) {
        try {
            return requirePrimitive(AbstractJSObject.getDefaultValue(jsobj, hint));
        } catch (UnsupportedOperationException e2) {
            throw new ECMAException(Context.getGlobal().newTypeError(e2.getMessage()), e2);
        }
    }

    public static String toPrimitiveToString(Object obj) {
        return toString(toPrimitive(obj));
    }

    public static CharSequence toPrimitiveToCharSequence(Object obj) {
        return toCharSequence(toPrimitive(obj));
    }

    public static boolean toBoolean(double num) {
        return (num == 0.0d || Double.isNaN(num)) ? false : true;
    }

    public static boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (nullOrUndefined(obj)) {
            return false;
        }
        if (!(obj instanceof Number)) {
            return !isString(obj) || ((CharSequence) obj).length() > 0;
        }
        double num = ((Number) obj).doubleValue();
        return (num == 0.0d || Double.isNaN(num)) ? false : true;
    }

    public static String toString(Object obj) {
        return toStringImpl(obj, false);
    }

    public static CharSequence toCharSequence(Object obj) {
        if (obj instanceof ConsString) {
            return (CharSequence) obj;
        }
        return toString(obj);
    }

    public static boolean isString(Object obj) {
        return (obj instanceof String) || (obj instanceof ConsString);
    }

    public static boolean isNumber(Object obj) {
        if (obj != null) {
            Class<?> c2 = obj.getClass();
            return c2 == Integer.class || c2 == Double.class || c2 == Float.class || c2 == Short.class || c2 == Byte.class;
        }
        return false;
    }

    public static String toString(int num) {
        return Integer.toString(num);
    }

    public static String toString(double num) {
        if (isRepresentableAsInt(num)) {
            return Integer.toString((int) num);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return Constants.ATTRVAL_INFINITY;
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        return NumberToString.stringFor(num);
    }

    public static String toString(double num, int radix) {
        if (!$assertionsDisabled && (radix < 2 || radix > 36)) {
            throw new AssertionError((Object) "invalid radix");
        }
        if (isRepresentableAsInt(num)) {
            return Integer.toString((int) num, radix);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return Constants.ATTRVAL_INFINITY;
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        if (num == 0.0d) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        boolean negative = num < 0.0d;
        double signedNum = negative ? -num : num;
        double intPart = Math.floor(signedNum);
        double decPart = signedNum - intPart;
        do {
            double remainder = intPart % radix;
            sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int) remainder));
            intPart = (intPart - remainder) / radix;
        } while (intPart >= 1.0d);
        if (negative) {
            sb.append('-');
        }
        sb.reverse();
        if (decPart > 0.0d) {
            int dot = sb.length();
            sb.append('.');
            do {
                double decPart2 = decPart * radix;
                double d2 = Math.floor(decPart2);
                sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int) d2));
                decPart = decPart2 - d2;
                if (decPart <= 0.0d) {
                    break;
                }
            } while (sb.length() - dot < 1100);
        }
        return sb.toString();
    }

    public static double toNumber(Object obj) {
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return toNumberGeneric(obj);
    }

    public static double toNumberForEq(Object obj) {
        if (obj == null) {
            return Double.NaN;
        }
        return toNumber(obj);
    }

    public static double toNumberForStrictEq(Object obj) {
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        if (isNumber(obj)) {
            return ((Number) obj).doubleValue();
        }
        return Double.NaN;
    }

    public static Number toNarrowestNumber(long l2) {
        return Double.valueOf(isRepresentableAsInt(l2) ? Integer.valueOf((int) l2).intValue() : Double.valueOf(l2).doubleValue());
    }

    public static double toNumber(Boolean b2) {
        return b2.booleanValue() ? 1.0d : 0.0d;
    }

    public static double toNumber(ScriptObject obj) {
        return toNumber(toPrimitive(obj, (Class<?>) Number.class));
    }

    public static double toNumberOptimistic(Object obj, int programPoint) {
        Class<?> clz;
        if (obj != null && ((clz = obj.getClass()) == Double.class || clz == Integer.class || clz == Long.class)) {
            return ((Number) obj).doubleValue();
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }

    public static double toNumberMaybeOptimistic(Object obj, int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? toNumberOptimistic(obj, programPoint) : toNumber(obj);
    }

    public static int digit(char ch, int radix) {
        return digit(ch, radix, false);
    }

    public static int digit(char ch, int radix, boolean onlyIsoLatin1) {
        char maxInRadix = (char) ((97 + (radix - 1)) - 10);
        char c2 = Character.toLowerCase(ch);
        if (c2 >= 'a' && c2 <= maxInRadix) {
            return Character.digit(ch, radix);
        }
        if (Character.isDigit(ch)) {
            if (!onlyIsoLatin1 || (ch >= '0' && ch <= '9')) {
                return Character.digit(ch, radix);
            }
            return -1;
        }
        return -1;
    }

    public static double toNumber(String str) {
        boolean negative;
        double value;
        int end = str.length();
        if (end == 0) {
            return 0.0d;
        }
        int start = 0;
        char cCharAt = str.charAt(0);
        while (true) {
            char f2 = cCharAt;
            if (Lexer.isJSWhitespace(f2)) {
                start++;
                if (start == end) {
                    return 0.0d;
                }
                cCharAt = str.charAt(start);
            } else {
                while (Lexer.isJSWhitespace(str.charAt(end - 1))) {
                    end--;
                }
                if (f2 == '-') {
                    start++;
                    if (start == end) {
                        return Double.NaN;
                    }
                    f2 = str.charAt(start);
                    negative = true;
                } else {
                    if (f2 == '+') {
                        start++;
                        if (start == end) {
                            return Double.NaN;
                        }
                        f2 = str.charAt(start);
                    }
                    negative = false;
                }
                if (start + 1 < end && f2 == '0' && Character.toLowerCase(str.charAt(start + 1)) == 'x') {
                    value = parseRadix(str.toCharArray(), start + 2, end, 16);
                } else {
                    if (f2 == 'I' && end - start == 8 && str.regionMatches(start, Constants.ATTRVAL_INFINITY, 0, 8)) {
                        return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    }
                    for (int i2 = start; i2 < end; i2++) {
                        char f3 = str.charAt(i2);
                        if ((f3 < '0' || f3 > '9') && f3 != '.' && f3 != 'e' && f3 != 'E' && f3 != '+' && f3 != '-') {
                            return Double.NaN;
                        }
                    }
                    try {
                        value = Double.parseDouble(str.substring(start, end));
                    } catch (NumberFormatException e2) {
                        return Double.NaN;
                    }
                }
                return negative ? -value : value;
            }
        }
    }

    public static int toInteger(Object obj) {
        return (int) toNumber(obj);
    }

    public static long toLong(Object obj) {
        return obj instanceof Long ? ((Long) obj).longValue() : toLong(toNumber(obj));
    }

    public static long toLong(double num) {
        return (long) num;
    }

    public static int toInt32(Object obj) {
        return toInt32(toNumber(obj));
    }

    public static int toInt32Optimistic(Object obj, int programPoint) {
        if (obj != null && obj.getClass() == Integer.class) {
            return ((Integer) obj).intValue();
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }

    public static int toInt32MaybeOptimistic(Object obj, int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? toInt32Optimistic(obj, programPoint) : toInt32(obj);
    }

    public static int toInt32(long num) {
        return (int) ((num < MIN_PRECISE_DOUBLE || num > MAX_PRECISE_DOUBLE) ? (long) (num % INT32_LIMIT) : num);
    }

    public static int toInt32(double num) {
        return (int) doubleToInt32(num);
    }

    public static long toUint32(Object obj) {
        return toUint32(toNumber(obj));
    }

    public static long toUint32(double num) {
        return doubleToInt32(num) & 4294967295L;
    }

    public static long toUint32(int num) {
        return num & 4294967295L;
    }

    public static int toUint32Optimistic(int num, int pp) {
        if (num >= 0) {
            return num;
        }
        throw new UnwarrantedOptimismException(Double.valueOf(toUint32Double(num)), pp, Type.NUMBER);
    }

    public static double toUint32Double(int num) {
        return toUint32(num);
    }

    public static int toUint16(Object obj) {
        return toUint16(toNumber(obj));
    }

    public static int toUint16(int num) {
        return num & 65535;
    }

    public static int toUint16(long num) {
        return ((int) num) & 65535;
    }

    public static int toUint16(double num) {
        return ((int) doubleToInt32(num)) & 65535;
    }

    private static long doubleToInt32(double num) {
        int exponent = Math.getExponent(num);
        if (exponent < 31) {
            return (long) num;
        }
        if (exponent >= 84) {
            return 0L;
        }
        double d2 = num >= 0.0d ? Math.floor(num) : Math.ceil(num);
        return (long) (d2 % INT32_LIMIT);
    }

    public static boolean isFinite(double num) {
        return (Double.isInfinite(num) || Double.isNaN(num)) ? false : true;
    }

    public static Double toDouble(double num) {
        return Double.valueOf(num);
    }

    public static Double toDouble(long num) {
        return Double.valueOf(num);
    }

    public static Double toDouble(int num) {
        return Double.valueOf(num);
    }

    public static Object toObject(boolean bool) {
        return Boolean.valueOf(bool);
    }

    public static Object toObject(int num) {
        return Integer.valueOf(num);
    }

    public static Object toObject(long num) {
        return Long.valueOf(num);
    }

    public static Object toObject(double num) {
        return Double.valueOf(num);
    }

    public static Object toObject(Object obj) {
        return obj;
    }

    public static Object toScriptObject(Object obj) {
        return toScriptObject(Context.getGlobal(), obj);
    }

    public static Object toScriptObject(Global global, Object obj) {
        if (nullOrUndefined(obj)) {
            throw ECMAErrors.typeError(global, "not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (obj instanceof ScriptObject) {
            return obj;
        }
        return global.wrapAsObject(obj);
    }

    public static Object toJavaArray(Object obj, Class<?> componentType) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject) obj).getArray().asArrayOfType(componentType);
        }
        if (obj instanceof JSObject) {
            ArrayLikeIterator<?> itr = ArrayLikeIterator.arrayLikeIterator(obj);
            int len = (int) itr.getLength();
            Object[] res = new Object[len];
            int idx = 0;
            while (itr.hasNext()) {
                int i2 = idx;
                idx++;
                res[i2] = itr.next();
            }
            return convertArray(res, componentType);
        }
        if (obj == null) {
            return null;
        }
        throw new IllegalArgumentException("not a script object");
    }

    public static Object convertArray(Object[] src, Class<?> componentType) throws NegativeArraySizeException {
        if (componentType == Object.class) {
            for (int i2 = 0; i2 < src.length; i2++) {
                Object e2 = src[i2];
                if (e2 instanceof ConsString) {
                    src[i2] = e2.toString();
                }
            }
        }
        int l2 = src.length;
        Object dst = Array.newInstance(componentType, l2);
        MethodHandle converter = Bootstrap.getLinkerServices().getTypeConverter(Object.class, componentType);
        for (int i3 = 0; i3 < src.length; i3++) {
            try {
                Array.set(dst, i3, invoke(converter, src[i3]));
            } catch (Error | RuntimeException e3) {
                throw e3;
            } catch (Throwable t2) {
                throw new RuntimeException(t2);
            }
        }
        return dst;
    }

    public static boolean nullOrUndefined(Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED;
    }

    static String toStringImpl(Object obj, boolean safe) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof ConsString) {
            return obj.toString();
        }
        if (isNumber(obj)) {
            return toString(((Number) obj).doubleValue());
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return "undefined";
        }
        if (obj == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        if (safe && (obj instanceof ScriptObject)) {
            ScriptObject sobj = (ScriptObject) obj;
            Global gobj = Context.getGlobal();
            if (gobj.isError(sobj)) {
                return ECMAException.safeToString(sobj);
            }
            return sobj.safeToString();
        }
        return toString(toPrimitive(obj, (Class<?>) String.class));
    }

    static String trimLeft(String str) {
        int start = 0;
        while (start < str.length() && Lexer.isJSWhitespace(str.charAt(start))) {
            start++;
        }
        return str.substring(start);
    }

    private static Object throwUnwarrantedOptimismException(Object value, int programPoint) {
        throw new UnwarrantedOptimismException(value, programPoint);
    }

    public static int addExact(int x2, int y2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.addExact(x2, y2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(x2 + y2), programPoint);
        }
    }

    public static int subExact(int x2, int y2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.subtractExact(x2, y2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(x2 - y2), programPoint);
        }
    }

    public static int mulExact(int x2, int y2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.multiplyExact(x2, y2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(x2 * y2), programPoint);
        }
    }

    public static int divExact(int x2, int y2, int programPoint) throws UnwarrantedOptimismException {
        try {
            int res = x2 / y2;
            int rem = x2 % y2;
            if (rem == 0) {
                return res;
            }
            throw new UnwarrantedOptimismException(Double.valueOf(x2 / y2), programPoint);
        } catch (ArithmeticException e2) {
            if ($assertionsDisabled || y2 == 0) {
                throw new UnwarrantedOptimismException(Double.valueOf(x2 > 0 ? Double.POSITIVE_INFINITY : x2 < 0 ? Double.NEGATIVE_INFINITY : Double.NaN), programPoint);
            }
            throw new AssertionError();
        }
    }

    public static int divZero(int x2, int y2) {
        if (y2 == 0) {
            return 0;
        }
        return x2 / y2;
    }

    public static int remZero(int x2, int y2) {
        if (y2 == 0) {
            return 0;
        }
        return x2 % y2;
    }

    public static int remExact(int x2, int y2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return x2 % y2;
        } catch (ArithmeticException e2) {
            if ($assertionsDisabled || y2 == 0) {
                throw new UnwarrantedOptimismException(Double.valueOf(Double.NaN), programPoint);
            }
            throw new AssertionError();
        }
    }

    public static int decrementExact(int x2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.decrementExact(x2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(x2 - 1.0d), programPoint);
        }
    }

    public static int incrementExact(int x2, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.incrementExact(x2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(x2 + 1.0d), programPoint);
        }
    }

    public static int negateExact(int x2, int programPoint) throws UnwarrantedOptimismException {
        try {
            if (x2 == 0) {
                throw new UnwarrantedOptimismException(Double.valueOf(-0.0d), programPoint);
            }
            return Math.negateExact(x2);
        } catch (ArithmeticException e2) {
            throw new UnwarrantedOptimismException(Double.valueOf(-x2), programPoint);
        }
    }

    public static int getAccessorTypeIndex(Type type) {
        return getAccessorTypeIndex(type.getTypeClass());
    }

    public static int getAccessorTypeIndex(Class<?> type) {
        if (type == null) {
            return -1;
        }
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Double.TYPE) {
            return 1;
        }
        if (!type.isPrimitive()) {
            return 2;
        }
        return -1;
    }

    public static Type getAccessorType(int index) {
        return ACCESSOR_TYPES.get(index);
    }

    public static int getNumberOfAccessorTypes() {
        return ACCESSOR_TYPES.size();
    }

    private static double parseRadix(char[] chars, int start, int length, int radix) {
        int pos = 0;
        for (int i2 = start; i2 < length; i2++) {
            if (digit(chars[i2], radix) == -1) {
                return Double.NaN;
            }
            pos++;
        }
        if (pos == 0) {
            return Double.NaN;
        }
        double value = 0.0d;
        for (int i3 = start; i3 < start + pos; i3++) {
            value = (value * radix) + digit(chars[i3], radix);
        }
        return value;
    }

    private static double toNumberGeneric(Object obj) {
        if (obj == null) {
            return 0.0d;
        }
        if (obj instanceof String) {
            return toNumber((String) obj);
        }
        if (obj instanceof ConsString) {
            return toNumber(obj.toString());
        }
        if (obj instanceof Boolean) {
            return toNumber((Boolean) obj);
        }
        if (obj instanceof ScriptObject) {
            return toNumber((ScriptObject) obj);
        }
        if (obj instanceof Undefined) {
            return Double.NaN;
        }
        return toNumber(toPrimitive(obj, (Class<?>) Number.class));
    }

    private static Object invoke(MethodHandle mh, Object arg) {
        try {
            return (Object) mh.invoke(arg);
        } catch (Error | RuntimeException e2) {
            throw e2;
        } catch (Throwable t2) {
            throw new RuntimeException(t2);
        }
    }

    public static MethodHandle unboxConstant(Object o2) {
        if (o2 != null) {
            if (o2.getClass() == Integer.class) {
                return Lookup.MH.constant(Integer.TYPE, Integer.valueOf(((Integer) o2).intValue()));
            }
            if (o2.getClass() == Double.class) {
                return Lookup.MH.constant(Double.TYPE, Double.valueOf(((Double) o2).doubleValue()));
            }
        }
        return Lookup.MH.constant(Object.class, o2);
    }

    public static Class<?> unboxedFieldType(Object o2) {
        if (o2 == null) {
            return Object.class;
        }
        if (o2.getClass() == Integer.class) {
            return Integer.TYPE;
        }
        if (o2.getClass() == Double.class) {
            return Double.TYPE;
        }
        return Object.class;
    }

    private static final List<MethodHandle> toUnmodifiableList(MethodHandle... methodHandles) {
        return Collections.unmodifiableList(Arrays.asList(methodHandles));
    }
}
