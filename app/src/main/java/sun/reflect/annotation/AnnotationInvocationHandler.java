package sun.reflect.annotation;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotationInvocationHandler.class */
class AnnotationInvocationHandler implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 6182022883658399397L;
    private final Class<? extends Annotation> type;
    private final Map<String, Object> memberValues;
    private volatile transient Method[] memberMethods = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AnnotationInvocationHandler.class.desiredAssertionStatus();
    }

    AnnotationInvocationHandler(Class<? extends Annotation> cls, Map<String, Object> map) {
        Class<?>[] interfaces = cls.getInterfaces();
        if (!cls.isAnnotation() || interfaces.length != 1 || interfaces[0] != Annotation.class) {
            throw new AnnotationFormatError("Attempt to create proxy for a non-annotation type.");
        }
        this.type = cls;
        this.memberValues = map;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) {
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (name.equals("equals") && parameterTypes.length == 1 && parameterTypes[0] == Object.class) {
            return equalsImpl(objArr[0]);
        }
        if (parameterTypes.length != 0) {
            throw new AssertionError((Object) "Too many parameters for an annotation method");
        }
        switch (name) {
            case "toString":
                return toStringImpl();
            case "hashCode":
                return Integer.valueOf(hashCodeImpl());
            case "annotationType":
                return this.type;
            default:
                Object objCloneArray = this.memberValues.get(name);
                if (objCloneArray == null) {
                    throw new IncompleteAnnotationException(this.type, name);
                }
                if (objCloneArray instanceof ExceptionProxy) {
                    throw ((ExceptionProxy) objCloneArray).generateException();
                }
                if (objCloneArray.getClass().isArray() && Array.getLength(objCloneArray) != 0) {
                    objCloneArray = cloneArray(objCloneArray);
                }
                return objCloneArray;
        }
    }

    private Object cloneArray(Object obj) {
        Class<?> cls = obj.getClass();
        if (cls == byte[].class) {
            return ((byte[]) obj).clone();
        }
        if (cls == char[].class) {
            return ((char[]) obj).clone();
        }
        if (cls == double[].class) {
            return ((double[]) obj).clone();
        }
        if (cls == float[].class) {
            return ((float[]) obj).clone();
        }
        if (cls == int[].class) {
            return ((int[]) obj).clone();
        }
        if (cls == long[].class) {
            return ((long[]) obj).clone();
        }
        if (cls == short[].class) {
            return ((short[]) obj).clone();
        }
        if (cls == boolean[].class) {
            return ((boolean[]) obj).clone();
        }
        return ((Object[]) obj).clone();
    }

    private String toStringImpl() {
        StringBuilder sb = new StringBuilder(128);
        sb.append('@');
        sb.append(this.type.getName());
        sb.append('(');
        boolean z2 = true;
        for (Map.Entry<String, Object> entry : this.memberValues.entrySet()) {
            if (z2) {
                z2 = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(memberValueToString(entry.getValue()));
        }
        sb.append(')');
        return sb.toString();
    }

    private static String memberValueToString(Object obj) {
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            return obj.toString();
        }
        if (cls == byte[].class) {
            return Arrays.toString((byte[]) obj);
        }
        if (cls == char[].class) {
            return Arrays.toString((char[]) obj);
        }
        if (cls == double[].class) {
            return Arrays.toString((double[]) obj);
        }
        if (cls == float[].class) {
            return Arrays.toString((float[]) obj);
        }
        if (cls == int[].class) {
            return Arrays.toString((int[]) obj);
        }
        if (cls == long[].class) {
            return Arrays.toString((long[]) obj);
        }
        if (cls == short[].class) {
            return Arrays.toString((short[]) obj);
        }
        if (cls == boolean[].class) {
            return Arrays.toString((boolean[]) obj);
        }
        return Arrays.toString((Object[]) obj);
    }

    private Boolean equalsImpl(Object obj) throws IllegalArgumentException {
        Object objInvoke;
        if (obj == this) {
            return true;
        }
        if (!this.type.isInstance(obj)) {
            return false;
        }
        for (Method method : getMemberMethods()) {
            String name = method.getName();
            Object obj2 = this.memberValues.get(name);
            AnnotationInvocationHandler annotationInvocationHandlerAsOneOfUs = asOneOfUs(obj);
            if (annotationInvocationHandlerAsOneOfUs != null) {
                objInvoke = annotationInvocationHandlerAsOneOfUs.memberValues.get(name);
            } else {
                try {
                    objInvoke = method.invoke(obj, new Object[0]);
                } catch (IllegalAccessException e2) {
                    throw new AssertionError(e2);
                } catch (InvocationTargetException e3) {
                    return false;
                }
            }
            if (!memberValueEquals(obj2, objInvoke)) {
                return false;
            }
        }
        return true;
    }

    private AnnotationInvocationHandler asOneOfUs(Object obj) throws IllegalArgumentException {
        if (Proxy.isProxyClass(obj.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj);
            if (invocationHandler instanceof AnnotationInvocationHandler) {
                return (AnnotationInvocationHandler) invocationHandler;
            }
            return null;
        }
        return null;
    }

    private static boolean memberValueEquals(Object obj, Object obj2) {
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            return obj.equals(obj2);
        }
        if ((obj instanceof Object[]) && (obj2 instanceof Object[])) {
            return Arrays.equals((Object[]) obj, (Object[]) obj2);
        }
        if (obj2.getClass() != cls) {
            return false;
        }
        if (cls == byte[].class) {
            return Arrays.equals((byte[]) obj, (byte[]) obj2);
        }
        if (cls == char[].class) {
            return Arrays.equals((char[]) obj, (char[]) obj2);
        }
        if (cls == double[].class) {
            return Arrays.equals((double[]) obj, (double[]) obj2);
        }
        if (cls == float[].class) {
            return Arrays.equals((float[]) obj, (float[]) obj2);
        }
        if (cls == int[].class) {
            return Arrays.equals((int[]) obj, (int[]) obj2);
        }
        if (cls == long[].class) {
            return Arrays.equals((long[]) obj, (long[]) obj2);
        }
        if (cls == short[].class) {
            return Arrays.equals((short[]) obj, (short[]) obj2);
        }
        if ($assertionsDisabled || cls == boolean[].class) {
            return Arrays.equals((boolean[]) obj, (boolean[]) obj2);
        }
        throw new AssertionError();
    }

    private Method[] getMemberMethods() {
        if (this.memberMethods == null) {
            this.memberMethods = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: sun.reflect.annotation.AnnotationInvocationHandler.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Method[] run2() throws SecurityException {
                    Method[] declaredMethods = AnnotationInvocationHandler.this.type.getDeclaredMethods();
                    AnnotationInvocationHandler.this.validateAnnotationMethods(declaredMethods);
                    AccessibleObject.setAccessible(declaredMethods, true);
                    return declaredMethods;
                }
            });
        }
        return this.memberMethods;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003c, code lost:
    
        r6 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0064 A[PHI: r11
  0x0064: PHI (r11v1 java.lang.Class<?>) = (r11v0 java.lang.Class<?>), (r11v2 java.lang.Class<?>) binds: [B:15:0x004d, B:17:0x005c] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void validateAnnotationMethods(java.lang.reflect.Method[] r5) {
        /*
            Method dump skipped, instructions count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.reflect.annotation.AnnotationInvocationHandler.validateAnnotationMethods(java.lang.reflect.Method[]):void");
    }

    private int hashCodeImpl() {
        int iHashCode = 0;
        for (Map.Entry<String, Object> entry : this.memberValues.entrySet()) {
            iHashCode += (127 * entry.getKey().hashCode()) ^ memberValueHashCode(entry.getValue());
        }
        return iHashCode;
    }

    private static int memberValueHashCode(Object obj) {
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            return obj.hashCode();
        }
        if (cls == byte[].class) {
            return Arrays.hashCode((byte[]) obj);
        }
        if (cls == char[].class) {
            return Arrays.hashCode((char[]) obj);
        }
        if (cls == double[].class) {
            return Arrays.hashCode((double[]) obj);
        }
        if (cls == float[].class) {
            return Arrays.hashCode((float[]) obj);
        }
        if (cls == int[].class) {
            return Arrays.hashCode((int[]) obj);
        }
        if (cls == long[].class) {
            return Arrays.hashCode((long[]) obj);
        }
        if (cls == short[].class) {
            return Arrays.hashCode((short[]) obj);
        }
        if (cls == boolean[].class) {
            return Arrays.hashCode((boolean[]) obj);
        }
        return Arrays.hashCode((Object[]) obj);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Class cls = (Class) fields.get("type", (Object) null);
        Map map = (Map) fields.get("memberValues", (Object) null);
        try {
            AnnotationType annotationType = AnnotationType.getInstance(cls);
            Map<String, Class<?>> mapMemberTypes = annotationType.memberTypes();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                Object value = null;
                Class<?> cls2 = mapMemberTypes.get(str);
                if (cls2 != null) {
                    value = entry.getValue();
                    if (!cls2.isInstance(value) && !(value instanceof ExceptionProxy)) {
                        value = new AnnotationTypeMismatchExceptionProxy(objectToString(value)).setMember(annotationType.members().get(str));
                    }
                }
                linkedHashMap.put(str, value);
            }
            UnsafeAccessor.setType(this, cls);
            UnsafeAccessor.setMemberValues(this, linkedHashMap);
        } catch (IllegalArgumentException e2) {
            throw new InvalidObjectException("Non-annotation type in annotation serial stream");
        }
    }

    private static String objectToString(Object obj) {
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotationInvocationHandler$UnsafeAccessor.class */
    private static class UnsafeAccessor {
        private static final Unsafe unsafe;
        private static final long typeOffset;
        private static final long memberValuesOffset;

        private UnsafeAccessor() {
        }

        static {
            try {
                unsafe = Unsafe.getUnsafe();
                typeOffset = unsafe.objectFieldOffset(AnnotationInvocationHandler.class.getDeclaredField("type"));
                memberValuesOffset = unsafe.objectFieldOffset(AnnotationInvocationHandler.class.getDeclaredField("memberValues"));
            } catch (Exception e2) {
                throw new ExceptionInInitializerError(e2);
            }
        }

        static void setType(AnnotationInvocationHandler annotationInvocationHandler, Class<? extends Annotation> cls) {
            unsafe.putObject(annotationInvocationHandler, typeOffset, cls);
        }

        static void setMemberValues(AnnotationInvocationHandler annotationInvocationHandler, Map<String, Object> map) {
            unsafe.putObject(annotationInvocationHandler, memberValuesOffset, map);
        }
    }
}
