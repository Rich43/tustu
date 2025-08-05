package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;
import sun.reflect.ConstantPool;
import sun.reflect.annotation.TypeAnnotation;

/* loaded from: rt.jar:sun/reflect/annotation/TypeAnnotationParser.class */
public final class TypeAnnotationParser {
    private static final TypeAnnotation[] EMPTY_TYPE_ANNOTATION_ARRAY = new TypeAnnotation[0];
    private static final byte CLASS_TYPE_PARAMETER = 0;
    private static final byte METHOD_TYPE_PARAMETER = 1;
    private static final byte CLASS_EXTENDS = 16;
    private static final byte CLASS_TYPE_PARAMETER_BOUND = 17;
    private static final byte METHOD_TYPE_PARAMETER_BOUND = 18;
    private static final byte FIELD = 19;
    private static final byte METHOD_RETURN = 20;
    private static final byte METHOD_RECEIVER = 21;
    private static final byte METHOD_FORMAL_PARAMETER = 22;
    private static final byte THROWS = 23;
    private static final byte LOCAL_VARIABLE = 64;
    private static final byte RESOURCE_VARIABLE = 65;
    private static final byte EXCEPTION_PARAMETER = 66;
    private static final byte INSTANCEOF = 67;
    private static final byte NEW = 68;
    private static final byte CONSTRUCTOR_REFERENCE = 69;
    private static final byte METHOD_REFERENCE = 70;
    private static final byte CAST = 71;
    private static final byte CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
    private static final byte METHOD_INVOCATION_TYPE_ARGUMENT = 73;
    private static final byte CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
    private static final byte METHOD_REFERENCE_TYPE_ARGUMENT = 75;

    public static AnnotatedType buildAnnotatedType(byte[] bArr, ConstantPool constantPool, AnnotatedElement annotatedElement, Class<?> cls, Type type, TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget) {
        TypeAnnotation[] typeAnnotations = parseTypeAnnotations(bArr, constantPool, annotatedElement, cls);
        ArrayList arrayList = new ArrayList(typeAnnotations.length);
        for (TypeAnnotation typeAnnotation : typeAnnotations) {
            if (typeAnnotation.getTargetInfo().getTarget() == typeAnnotationTarget) {
                arrayList.add(typeAnnotation);
            }
        }
        TypeAnnotation[] typeAnnotationArr = (TypeAnnotation[]) arrayList.toArray(EMPTY_TYPE_ANNOTATION_ARRAY);
        return AnnotatedTypeFactory.buildAnnotatedType(type, TypeAnnotation.LocationInfo.BASE_LOCATION, typeAnnotationArr, typeAnnotationArr, annotatedElement);
    }

    public static AnnotatedType[] buildAnnotatedTypes(byte[] bArr, ConstantPool constantPool, AnnotatedElement annotatedElement, Class<?> cls, Type[] typeArr, TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget) {
        TypeAnnotation[] typeAnnotationArr;
        int length = typeArr.length;
        AnnotatedType[] annotatedTypeArr = new AnnotatedType[length];
        Arrays.fill(annotatedTypeArr, AnnotatedTypeFactory.EMPTY_ANNOTATED_TYPE);
        ArrayList[] arrayListArr = new ArrayList[length];
        TypeAnnotation[] typeAnnotations = parseTypeAnnotations(bArr, constantPool, annotatedElement, cls);
        for (TypeAnnotation typeAnnotation : typeAnnotations) {
            TypeAnnotation.TypeAnnotationTargetInfo targetInfo = typeAnnotation.getTargetInfo();
            if (targetInfo.getTarget() == typeAnnotationTarget) {
                int count = targetInfo.getCount();
                if (arrayListArr[count] == null) {
                    arrayListArr[count] = new ArrayList(typeAnnotations.length);
                }
                arrayListArr[count].add(typeAnnotation);
            }
        }
        for (int i2 = 0; i2 < length; i2++) {
            ArrayList arrayList = arrayListArr[i2];
            if (arrayList != null) {
                typeAnnotationArr = (TypeAnnotation[]) arrayList.toArray(new TypeAnnotation[arrayList.size()]);
            } else {
                typeAnnotationArr = EMPTY_TYPE_ANNOTATION_ARRAY;
            }
            TypeAnnotation[] typeAnnotationArr2 = typeAnnotationArr;
            annotatedTypeArr[i2] = AnnotatedTypeFactory.buildAnnotatedType(typeArr[i2], TypeAnnotation.LocationInfo.BASE_LOCATION, typeAnnotationArr2, typeAnnotationArr2, annotatedElement);
        }
        return annotatedTypeArr;
    }

    public static AnnotatedType buildAnnotatedSuperclass(byte[] bArr, ConstantPool constantPool, Class<?> cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass == null) {
            return AnnotatedTypeFactory.EMPTY_ANNOTATED_TYPE;
        }
        return buildAnnotatedType(bArr, constantPool, cls, cls, genericSuperclass, TypeAnnotation.TypeAnnotationTarget.CLASS_EXTENDS);
    }

    public static AnnotatedType[] buildAnnotatedInterfaces(byte[] bArr, ConstantPool constantPool, Class<?> cls) {
        if (cls == Object.class || cls.isArray() || cls.isPrimitive() || cls == Void.TYPE) {
            return AnnotatedTypeFactory.EMPTY_ANNOTATED_TYPE_ARRAY;
        }
        return buildAnnotatedTypes(bArr, constantPool, cls, cls, cls.getGenericInterfaces(), TypeAnnotation.TypeAnnotationTarget.CLASS_IMPLEMENTS);
    }

    public static <D extends GenericDeclaration> Annotation[] parseTypeVariableAnnotations(D d2, int i2) {
        GenericDeclaration genericDeclaration;
        TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget;
        if (d2 instanceof Class) {
            genericDeclaration = (Class) d2;
            typeAnnotationTarget = TypeAnnotation.TypeAnnotationTarget.CLASS_TYPE_PARAMETER;
        } else if (d2 instanceof Executable) {
            genericDeclaration = (Executable) d2;
            typeAnnotationTarget = TypeAnnotation.TypeAnnotationTarget.METHOD_TYPE_PARAMETER;
        } else {
            throw new AssertionError((Object) ("Unknown GenericDeclaration " + ((Object) d2) + "\nthis should not happen."));
        }
        List<TypeAnnotation> listFilter = TypeAnnotation.filter(parseAllTypeAnnotations(genericDeclaration), typeAnnotationTarget);
        ArrayList arrayList = new ArrayList(listFilter.size());
        for (TypeAnnotation typeAnnotation : listFilter) {
            if (typeAnnotation.getTargetInfo().getCount() == i2) {
                arrayList.add(typeAnnotation.getAnnotation());
            }
        }
        return (Annotation[]) arrayList.toArray(new Annotation[0]);
    }

    public static <D extends GenericDeclaration> AnnotatedType[] parseAnnotatedBounds(Type[] typeArr, D d2, int i2) {
        return parseAnnotatedBounds(typeArr, d2, i2, TypeAnnotation.LocationInfo.BASE_LOCATION);
    }

    private static <D extends GenericDeclaration> AnnotatedType[] parseAnnotatedBounds(Type[] typeArr, D d2, int i2, TypeAnnotation.LocationInfo locationInfo) {
        List<TypeAnnotation> listFetchBounds = fetchBounds(d2);
        if (typeArr != null) {
            int i3 = 0;
            AnnotatedType[] annotatedTypeArr = new AnnotatedType[typeArr.length];
            if (typeArr.length > 0) {
                Type type = typeArr[0];
                if (!(type instanceof Class) || ((Class) type).isInterface()) {
                    i3 = 1;
                }
            }
            for (int i4 = 0; i4 < typeArr.length; i4++) {
                ArrayList arrayList = new ArrayList(listFetchBounds.size());
                for (TypeAnnotation typeAnnotation : listFetchBounds) {
                    TypeAnnotation.TypeAnnotationTargetInfo targetInfo = typeAnnotation.getTargetInfo();
                    if (targetInfo.getSecondaryIndex() == i4 + i3 && targetInfo.getCount() == i2) {
                        arrayList.add(typeAnnotation);
                    }
                }
                annotatedTypeArr[i4] = AnnotatedTypeFactory.buildAnnotatedType(typeArr[i4], locationInfo, (TypeAnnotation[]) arrayList.toArray(EMPTY_TYPE_ANNOTATION_ARRAY), (TypeAnnotation[]) listFetchBounds.toArray(EMPTY_TYPE_ANNOTATION_ARRAY), d2);
            }
            return annotatedTypeArr;
        }
        return new AnnotatedType[0];
    }

    private static <D extends GenericDeclaration> List<TypeAnnotation> fetchBounds(D d2) {
        TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget;
        GenericDeclaration genericDeclaration;
        if (d2 instanceof Class) {
            typeAnnotationTarget = TypeAnnotation.TypeAnnotationTarget.CLASS_TYPE_PARAMETER_BOUND;
            genericDeclaration = (Class) d2;
        } else {
            typeAnnotationTarget = TypeAnnotation.TypeAnnotationTarget.METHOD_TYPE_PARAMETER_BOUND;
            genericDeclaration = (Executable) d2;
        }
        return TypeAnnotation.filter(parseAllTypeAnnotations(genericDeclaration), typeAnnotationTarget);
    }

    static TypeAnnotation[] parseAllTypeAnnotations(AnnotatedElement annotatedElement) {
        Class<?> declaringClass;
        byte[] rawExecutableTypeAnnotations;
        JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
        if (annotatedElement instanceof Class) {
            declaringClass = (Class) annotatedElement;
            rawExecutableTypeAnnotations = javaLangAccess.getRawClassTypeAnnotations(declaringClass);
        } else if (annotatedElement instanceof Executable) {
            declaringClass = ((Executable) annotatedElement).getDeclaringClass();
            rawExecutableTypeAnnotations = javaLangAccess.getRawExecutableTypeAnnotations((Executable) annotatedElement);
        } else {
            return EMPTY_TYPE_ANNOTATION_ARRAY;
        }
        return parseTypeAnnotations(rawExecutableTypeAnnotations, javaLangAccess.getConstantPool(declaringClass), annotatedElement, declaringClass);
    }

    private static TypeAnnotation[] parseTypeAnnotations(byte[] bArr, ConstantPool constantPool, AnnotatedElement annotatedElement, Class<?> cls) {
        if (bArr == null) {
            return EMPTY_TYPE_ANNOTATION_ARRAY;
        }
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        int i2 = byteBufferWrap.getShort() & 65535;
        ArrayList arrayList = new ArrayList(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            TypeAnnotation typeAnnotation = parseTypeAnnotation(byteBufferWrap, constantPool, annotatedElement, cls);
            if (typeAnnotation != null) {
                arrayList.add(typeAnnotation);
            }
        }
        return (TypeAnnotation[]) arrayList.toArray(EMPTY_TYPE_ANNOTATION_ARRAY);
    }

    static Map<Class<? extends Annotation>, Annotation> mapTypeAnnotations(TypeAnnotation[] typeAnnotationArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (TypeAnnotation typeAnnotation : typeAnnotationArr) {
            Annotation annotation = typeAnnotation.getAnnotation();
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            if (AnnotationType.getInstance(clsAnnotationType).retention() == RetentionPolicy.RUNTIME && linkedHashMap.put(clsAnnotationType, annotation) != 0) {
                throw new AnnotationFormatError("Duplicate annotation for class: " + ((Object) clsAnnotationType) + ": " + ((Object) annotation));
            }
        }
        return linkedHashMap;
    }

    private static TypeAnnotation parseTypeAnnotation(ByteBuffer byteBuffer, ConstantPool constantPool, AnnotatedElement annotatedElement, Class<?> cls) {
        try {
            TypeAnnotation.TypeAnnotationTargetInfo targetInfo = parseTargetInfo(byteBuffer);
            TypeAnnotation.LocationInfo locationInfo = TypeAnnotation.LocationInfo.parseLocationInfo(byteBuffer);
            Annotation annotation = AnnotationParser.parseAnnotation(byteBuffer, constantPool, cls, false);
            if (targetInfo == null) {
                return null;
            }
            return new TypeAnnotation(targetInfo, locationInfo, annotation, annotatedElement);
        } catch (IllegalArgumentException | BufferUnderflowException e2) {
            throw new AnnotationFormatError(e2);
        }
    }

    private static TypeAnnotation.TypeAnnotationTargetInfo parseTargetInfo(ByteBuffer byteBuffer) {
        TypeAnnotation.TypeAnnotationTargetInfo typeAnnotationTargetInfo;
        int i2 = byteBuffer.get() & 255;
        switch (i2) {
            case 0:
            case 1:
                int i3 = byteBuffer.get() & 255;
                if (i2 == 0) {
                    typeAnnotationTargetInfo = new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.CLASS_TYPE_PARAMETER, i3);
                } else {
                    typeAnnotationTargetInfo = new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.METHOD_TYPE_PARAMETER, i3);
                }
                return typeAnnotationTargetInfo;
            case 16:
                short s2 = byteBuffer.getShort();
                if (s2 == -1) {
                    return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.CLASS_EXTENDS);
                }
                if (s2 >= 0) {
                    return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.CLASS_IMPLEMENTS, s2);
                }
                break;
            case 17:
                return parse2ByteTarget(TypeAnnotation.TypeAnnotationTarget.CLASS_TYPE_PARAMETER_BOUND, byteBuffer);
            case 18:
                return parse2ByteTarget(TypeAnnotation.TypeAnnotationTarget.METHOD_TYPE_PARAMETER_BOUND, byteBuffer);
            case 19:
                return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.FIELD);
            case 20:
                return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.METHOD_RETURN);
            case 21:
                return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.METHOD_RECEIVER);
            case 22:
                return new TypeAnnotation.TypeAnnotationTargetInfo(TypeAnnotation.TypeAnnotationTarget.METHOD_FORMAL_PARAMETER, byteBuffer.get() & 255);
            case 23:
                return parseShortTarget(TypeAnnotation.TypeAnnotationTarget.THROWS, byteBuffer);
            case 64:
            case 65:
                int i4 = byteBuffer.getShort();
                for (int i5 = 0; i5 < i4; i5++) {
                    byteBuffer.getShort();
                    byteBuffer.getShort();
                    byteBuffer.getShort();
                }
                return null;
            case 66:
                byteBuffer.get();
                return null;
            case 67:
            case 68:
            case 69:
            case 70:
                byteBuffer.getShort();
                return null;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                byteBuffer.getShort();
                byteBuffer.get();
                return null;
        }
        throw new AnnotationFormatError("Could not parse bytes for type annotations");
    }

    private static TypeAnnotation.TypeAnnotationTargetInfo parseShortTarget(TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget, ByteBuffer byteBuffer) {
        return new TypeAnnotation.TypeAnnotationTargetInfo(typeAnnotationTarget, byteBuffer.getShort() & 65535);
    }

    private static TypeAnnotation.TypeAnnotationTargetInfo parse2ByteTarget(TypeAnnotation.TypeAnnotationTarget typeAnnotationTarget, ByteBuffer byteBuffer) {
        return new TypeAnnotation.TypeAnnotationTargetInfo(typeAnnotationTarget, byteBuffer.get() & 255, byteBuffer.get() & 255);
    }
}
