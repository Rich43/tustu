package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import sun.reflect.ConstantPool;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.scope.ClassScope;
import sun.reflect.generics.tree.TypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotationParser.class */
public class AnnotationParser {
    private static final Annotation[] EMPTY_ANNOTATIONS_ARRAY;
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AnnotationParser.class.desiredAssertionStatus();
        EMPTY_ANNOTATIONS_ARRAY = new Annotation[0];
        EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    }

    public static Map<Class<? extends Annotation>, Annotation> parseAnnotations(byte[] bArr, ConstantPool constantPool, Class<?> cls) {
        if (bArr == null) {
            return Collections.emptyMap();
        }
        try {
            return parseAnnotations2(bArr, constantPool, cls, null);
        } catch (IllegalArgumentException e2) {
            throw new AnnotationFormatError(e2);
        } catch (BufferUnderflowException e3) {
            throw new AnnotationFormatError("Unexpected end of annotations.");
        }
    }

    @SafeVarargs
    static Map<Class<? extends Annotation>, Annotation> parseSelectAnnotations(byte[] bArr, ConstantPool constantPool, Class<?> cls, Class<? extends Annotation>... clsArr) {
        if (bArr == null) {
            return Collections.emptyMap();
        }
        try {
            return parseAnnotations2(bArr, constantPool, cls, clsArr);
        } catch (IllegalArgumentException e2) {
            throw new AnnotationFormatError(e2);
        } catch (BufferUnderflowException e3) {
            throw new AnnotationFormatError("Unexpected end of annotations.");
        }
    }

    private static Map<Class<? extends Annotation>, Annotation> parseAnnotations2(byte[] bArr, ConstantPool constantPool, Class<?> cls, Class<? extends Annotation>[] clsArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        int i2 = byteBufferWrap.getShort() & 65535;
        for (int i3 = 0; i3 < i2; i3++) {
            Annotation annotation2 = parseAnnotation2(byteBufferWrap, constantPool, cls, false, clsArr);
            if (annotation2 != null) {
                Class<? extends Annotation> clsAnnotationType = annotation2.annotationType();
                if (AnnotationType.getInstance(clsAnnotationType).retention() == RetentionPolicy.RUNTIME && linkedHashMap.put(clsAnnotationType, annotation2) != 0) {
                    throw new AnnotationFormatError("Duplicate annotation for class: " + ((Object) clsAnnotationType) + ": " + ((Object) annotation2));
                }
            }
        }
        return linkedHashMap;
    }

    public static Annotation[][] parseParameterAnnotations(byte[] bArr, ConstantPool constantPool, Class<?> cls) {
        try {
            return parseParameterAnnotations2(bArr, constantPool, cls);
        } catch (IllegalArgumentException e2) {
            throw new AnnotationFormatError(e2);
        } catch (BufferUnderflowException e3) {
            throw new AnnotationFormatError("Unexpected end of parameter annotations.");
        }
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.annotation.Annotation[], java.lang.annotation.Annotation[][]] */
    private static Annotation[][] parseParameterAnnotations2(byte[] bArr, ConstantPool constantPool, Class<?> cls) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        int i2 = byteBufferWrap.get() & 255;
        ?? r0 = new Annotation[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = byteBufferWrap.getShort() & 65535;
            ArrayList arrayList = new ArrayList(i4);
            for (int i5 = 0; i5 < i4; i5++) {
                Annotation annotation = parseAnnotation(byteBufferWrap, constantPool, cls, false);
                if (annotation != null && AnnotationType.getInstance(annotation.annotationType()).retention() == RetentionPolicy.RUNTIME) {
                    arrayList.add(annotation);
                }
            }
            r0[i3] = (Annotation[]) arrayList.toArray(EMPTY_ANNOTATIONS_ARRAY);
        }
        return r0;
    }

    static Annotation parseAnnotation(ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls, boolean z2) {
        return parseAnnotation2(byteBuffer, constantPool, cls, z2, null);
    }

    private static Annotation parseAnnotation2(ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls, boolean z2, Class<? extends Annotation>[] clsArr) {
        Class<?> classAt;
        int i2 = byteBuffer.getShort() & 65535;
        String uTF8At = "[unknown]";
        try {
            try {
                uTF8At = constantPool.getUTF8At(i2);
                classAt = parseSig(uTF8At, cls);
            } catch (NoClassDefFoundError e2) {
                if (z2) {
                    throw new TypeNotPresentException(uTF8At, e2);
                }
                skipAnnotation(byteBuffer, false);
                return null;
            } catch (TypeNotPresentException e3) {
                if (z2) {
                    throw e3;
                }
                skipAnnotation(byteBuffer, false);
                return null;
            }
        } catch (IllegalArgumentException e4) {
            classAt = constantPool.getClassAt(i2);
        }
        if (clsArr != null && !contains(clsArr, classAt)) {
            skipAnnotation(byteBuffer, false);
            return null;
        }
        try {
            AnnotationType annotationType = AnnotationType.getInstance(classAt);
            Map<String, Class<?>> mapMemberTypes = annotationType.memberTypes();
            LinkedHashMap linkedHashMap = new LinkedHashMap(annotationType.memberDefaults());
            int i3 = byteBuffer.getShort() & 65535;
            for (int i4 = 0; i4 < i3; i4++) {
                String uTF8At2 = constantPool.getUTF8At(byteBuffer.getShort() & 65535);
                Class<?> cls2 = mapMemberTypes.get(uTF8At2);
                if (cls2 == null) {
                    skipMemberValue(byteBuffer);
                } else {
                    Object memberValue = parseMemberValue(cls2, byteBuffer, constantPool, cls);
                    if (memberValue instanceof AnnotationTypeMismatchExceptionProxy) {
                        ((AnnotationTypeMismatchExceptionProxy) memberValue).setMember(annotationType.members().get(uTF8At2));
                    }
                    linkedHashMap.put(uTF8At2, memberValue);
                }
            }
            return annotationForMap(classAt, linkedHashMap);
        } catch (IllegalArgumentException e5) {
            skipAnnotation(byteBuffer, false);
            return null;
        }
    }

    public static Annotation annotationForMap(final Class<? extends Annotation> cls, final Map<String, Object> map) {
        return (Annotation) AccessController.doPrivileged(new PrivilegedAction<Annotation>() { // from class: sun.reflect.annotation.AnnotationParser.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Annotation run2() {
                return (Annotation) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new AnnotationInvocationHandler(cls, map));
            }
        });
    }

    public static Object parseMemberValue(Class<?> cls, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls2) {
        Object annotation;
        byte b2 = byteBuffer.get();
        switch (b2) {
            case 64:
                annotation = parseAnnotation(byteBuffer, constantPool, cls2, true);
                break;
            case 91:
                return parseArray(cls, byteBuffer, constantPool, cls2);
            case 99:
                annotation = parseClassValue(byteBuffer, constantPool, cls2);
                break;
            case 101:
                return parseEnumValue(cls, byteBuffer, constantPool, cls2);
            default:
                annotation = parseConst(b2, byteBuffer, constantPool);
                break;
        }
        if (!(annotation instanceof ExceptionProxy) && !cls.isInstance(annotation)) {
            annotation = new AnnotationTypeMismatchExceptionProxy(((Object) annotation.getClass()) + "[" + annotation + "]");
        }
        return annotation;
    }

    private static Object parseConst(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        int i3 = byteBuffer.getShort() & 65535;
        switch (i2) {
            case 66:
                return Byte.valueOf((byte) constantPool.getIntAt(i3));
            case 67:
                return Character.valueOf((char) constantPool.getIntAt(i3));
            case 68:
                return Double.valueOf(constantPool.getDoubleAt(i3));
            case 70:
                return Float.valueOf(constantPool.getFloatAt(i3));
            case 73:
                return Integer.valueOf(constantPool.getIntAt(i3));
            case 74:
                return Long.valueOf(constantPool.getLongAt(i3));
            case 83:
                return Short.valueOf((short) constantPool.getIntAt(i3));
            case 90:
                return Boolean.valueOf(constantPool.getIntAt(i3) != 0);
            case 115:
                return constantPool.getUTF8At(i3);
            default:
                throw new AnnotationFormatError("Invalid member-value tag in annotation: " + i2);
        }
    }

    private static Object parseClassValue(ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls) {
        int i2 = byteBuffer.getShort() & 65535;
        try {
            try {
                return parseSig(constantPool.getUTF8At(i2), cls);
            } catch (NoClassDefFoundError e2) {
                return new TypeNotPresentExceptionProxy("[unknown]", e2);
            } catch (TypeNotPresentException e3) {
                return new TypeNotPresentExceptionProxy(e3.typeName(), e3.getCause());
            }
        } catch (IllegalArgumentException e4) {
            return constantPool.getClassAt(i2);
        }
    }

    private static Class<?> parseSig(String str, Class<?> cls) {
        if (str.equals("V")) {
            return Void.TYPE;
        }
        TypeSignature typeSig = SignatureParser.make().parseTypeSig(str);
        Reifier reifierMake = Reifier.make(CoreReflectionFactory.make(cls, ClassScope.make(cls)));
        typeSig.accept(reifierMake);
        return toClass(reifierMake.getResult());
    }

    static Class<?> toClass(Type type) {
        if (type instanceof GenericArrayType) {
            return Array.newInstance(toClass(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        }
        return (Class) type;
    }

    private static Object parseEnumValue(Class<? extends Enum> cls, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls2) {
        String uTF8At = constantPool.getUTF8At(byteBuffer.getShort() & 65535);
        String uTF8At2 = constantPool.getUTF8At(byteBuffer.getShort() & 65535);
        if (!uTF8At.endsWith(";")) {
            if (!cls.getName().equals(uTF8At)) {
                return new AnnotationTypeMismatchExceptionProxy(uTF8At + "." + uTF8At2);
            }
        } else if (cls != parseSig(uTF8At, cls2)) {
            return new AnnotationTypeMismatchExceptionProxy(uTF8At + "." + uTF8At2);
        }
        try {
            return Enum.valueOf(cls, uTF8At2);
        } catch (IllegalArgumentException e2) {
            return new EnumConstantNotPresentExceptionProxy(cls, uTF8At2);
        }
    }

    private static Object parseArray(Class<?> cls, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls2) {
        int i2 = byteBuffer.getShort() & 65535;
        Class<?> componentType = cls.getComponentType();
        if (componentType == Byte.TYPE) {
            return parseByteArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Character.TYPE) {
            return parseCharArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Double.TYPE) {
            return parseDoubleArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Float.TYPE) {
            return parseFloatArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Integer.TYPE) {
            return parseIntArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Long.TYPE) {
            return parseLongArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Short.TYPE) {
            return parseShortArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Boolean.TYPE) {
            return parseBooleanArray(i2, byteBuffer, constantPool);
        }
        if (componentType == String.class) {
            return parseStringArray(i2, byteBuffer, constantPool);
        }
        if (componentType == Class.class) {
            return parseClassArray(i2, byteBuffer, constantPool, cls2);
        }
        if (componentType.isEnum()) {
            return parseEnumArray(i2, componentType, byteBuffer, constantPool, cls2);
        }
        if ($assertionsDisabled || componentType.isAnnotation()) {
            return parseAnnotationArray(i2, componentType, byteBuffer, constantPool, cls2);
        }
        throw new AssertionError();
    }

    private static Object parseByteArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        byte[] bArr = new byte[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 66) {
                bArr[i3] = (byte) constantPool.getIntAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : bArr;
    }

    private static Object parseCharArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        char[] cArr = new char[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 67) {
                cArr[i3] = (char) constantPool.getIntAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : cArr;
    }

    private static Object parseDoubleArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        double[] dArr = new double[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 68) {
                dArr[i3] = constantPool.getDoubleAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : dArr;
    }

    private static Object parseFloatArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        float[] fArr = new float[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 70) {
                fArr[i3] = constantPool.getFloatAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : fArr;
    }

    private static Object parseIntArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        int[] iArr = new int[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 73) {
                iArr[i3] = constantPool.getIntAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : iArr;
    }

    private static Object parseLongArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        long[] jArr = new long[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 74) {
                jArr[i3] = constantPool.getLongAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : jArr;
    }

    private static Object parseShortArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        short[] sArr = new short[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 83) {
                sArr[i3] = (short) constantPool.getIntAt(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : sArr;
    }

    private static Object parseBooleanArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        boolean[] zArr = new boolean[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 90) {
                zArr[i3] = constantPool.getIntAt(byteBuffer.getShort() & 65535) != 0;
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : zArr;
    }

    private static Object parseStringArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool) {
        String[] strArr = new String[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 115) {
                strArr[i3] = constantPool.getUTF8At(byteBuffer.getShort() & 65535);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : strArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Object parseClassArray(int i2, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls) {
        Class[] clsArr = new Class[i2];
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 99) {
                clsArr[i3] = parseClassValue(byteBuffer, constantPool, cls);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : clsArr;
    }

    private static Object parseEnumArray(int i2, Class<? extends Enum<?>> cls, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls2) {
        Object[] objArr = (Object[]) Array.newInstance(cls, i2);
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 101) {
                objArr[i3] = parseEnumValue(cls, byteBuffer, constantPool, cls2);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : objArr;
    }

    private static Object parseAnnotationArray(int i2, Class<? extends Annotation> cls, ByteBuffer byteBuffer, ConstantPool constantPool, Class<?> cls2) {
        Object[] objArr = (Object[]) Array.newInstance(cls, i2);
        boolean z2 = false;
        byte b2 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            b2 = byteBuffer.get();
            if (b2 == 64) {
                objArr[i3] = parseAnnotation(byteBuffer, constantPool, cls2, true);
            } else {
                skipMemberValue(b2, byteBuffer);
                z2 = true;
            }
        }
        return z2 ? exceptionProxy(b2) : objArr;
    }

    private static ExceptionProxy exceptionProxy(int i2) {
        return new AnnotationTypeMismatchExceptionProxy("Array with component tag: " + i2);
    }

    private static void skipAnnotation(ByteBuffer byteBuffer, boolean z2) {
        if (z2) {
            byteBuffer.getShort();
        }
        int i2 = byteBuffer.getShort() & 65535;
        for (int i3 = 0; i3 < i2; i3++) {
            byteBuffer.getShort();
            skipMemberValue(byteBuffer);
        }
    }

    private static void skipMemberValue(ByteBuffer byteBuffer) {
        skipMemberValue(byteBuffer.get(), byteBuffer);
    }

    private static void skipMemberValue(int i2, ByteBuffer byteBuffer) {
        switch (i2) {
            case 64:
                skipAnnotation(byteBuffer, true);
                break;
            case 91:
                skipArray(byteBuffer);
                break;
            case 101:
                byteBuffer.getInt();
                break;
            default:
                byteBuffer.getShort();
                break;
        }
    }

    private static void skipArray(ByteBuffer byteBuffer) {
        int i2 = byteBuffer.getShort() & 65535;
        for (int i3 = 0; i3 < i2; i3++) {
            skipMemberValue(byteBuffer);
        }
    }

    private static boolean contains(Object[] objArr, Object obj) {
        for (Object obj2 : objArr) {
            if (obj2 == obj) {
                return true;
            }
        }
        return false;
    }

    public static Annotation[] toArray(Map<Class<? extends Annotation>, Annotation> map) {
        return (Annotation[]) map.values().toArray(EMPTY_ANNOTATION_ARRAY);
    }

    static Annotation[] getEmptyAnnotationArray() {
        return EMPTY_ANNOTATION_ARRAY;
    }
}
