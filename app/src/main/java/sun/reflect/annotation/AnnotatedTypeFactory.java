package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import sun.reflect.annotation.TypeAnnotation;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory.class */
public final class AnnotatedTypeFactory {
    static final AnnotatedType EMPTY_ANNOTATED_TYPE = new AnnotatedTypeBaseImpl(null, TypeAnnotation.LocationInfo.BASE_LOCATION, new TypeAnnotation[0], new TypeAnnotation[0], null);
    static final AnnotatedType[] EMPTY_ANNOTATED_TYPE_ARRAY = new AnnotatedType[0];

    public static AnnotatedType buildAnnotatedType(Type type, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
        if (type == null) {
            return EMPTY_ANNOTATED_TYPE;
        }
        if (isArray(type)) {
            return new AnnotatedArrayTypeImpl(type, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }
        if (type instanceof Class) {
            return new AnnotatedTypeBaseImpl(type, addNesting(type, locationInfo), typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }
        if (type instanceof TypeVariable) {
            return new AnnotatedTypeVariableImpl((TypeVariable) type, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }
        if (type instanceof ParameterizedType) {
            return new AnnotatedParameterizedTypeImpl((ParameterizedType) type, addNesting(type, locationInfo), typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }
        if (type instanceof WildcardType) {
            return new AnnotatedWildcardTypeImpl((WildcardType) type, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }
        throw new AssertionError((Object) ("Unknown instance of Type: " + ((Object) type) + "\nThis should not happen."));
    }

    private static TypeAnnotation.LocationInfo addNesting(Type type, TypeAnnotation.LocationInfo locationInfo) {
        if (isArray(type)) {
            return locationInfo;
        }
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.getEnclosingClass() == null) {
                return locationInfo;
            }
            if (Modifier.isStatic(cls.getModifiers())) {
                return addNesting(cls.getEnclosingClass(), locationInfo);
            }
            return addNesting(cls.getEnclosingClass(), locationInfo.pushInner());
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getOwnerType() == null) {
                return locationInfo;
            }
            return addNesting(parameterizedType.getOwnerType(), locationInfo.pushInner());
        }
        return locationInfo;
    }

    private static boolean isArray(Type type) {
        if (type instanceof Class) {
            if (((Class) type).isArray()) {
                return true;
            }
            return false;
        }
        if (type instanceof GenericArrayType) {
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory$AnnotatedTypeBaseImpl.class */
    private static class AnnotatedTypeBaseImpl implements AnnotatedType {
        private final Type type;
        private final AnnotatedElement decl;
        private final TypeAnnotation.LocationInfo location;
        private final TypeAnnotation[] allOnSameTargetTypeAnnotations;
        private final Map<Class<? extends Annotation>, Annotation> annotations;

        AnnotatedTypeBaseImpl(Type type, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
            this.type = type;
            this.decl = annotatedElement;
            this.location = locationInfo;
            this.allOnSameTargetTypeAnnotations = typeAnnotationArr2;
            this.annotations = TypeAnnotationParser.mapTypeAnnotations(locationInfo.filter(typeAnnotationArr));
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final Annotation[] getAnnotations() {
            return getDeclaredAnnotations();
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final <T extends Annotation> T getAnnotation(Class<T> cls) {
            return (T) getDeclaredAnnotation(cls);
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
            return (T[]) getDeclaredAnnotationsByType(cls);
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final Annotation[] getDeclaredAnnotations() {
            return (Annotation[]) this.annotations.values().toArray(new Annotation[0]);
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
            return (T) this.annotations.get(cls);
        }

        @Override // java.lang.reflect.AnnotatedElement
        public final <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
            return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(this.annotations, cls);
        }

        @Override // java.lang.reflect.AnnotatedType
        public final Type getType() {
            return this.type;
        }

        final TypeAnnotation.LocationInfo getLocation() {
            return this.location;
        }

        final TypeAnnotation[] getTypeAnnotations() {
            return this.allOnSameTargetTypeAnnotations;
        }

        final AnnotatedElement getDecl() {
            return this.decl;
        }
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory$AnnotatedArrayTypeImpl.class */
    private static final class AnnotatedArrayTypeImpl extends AnnotatedTypeBaseImpl implements AnnotatedArrayType {
        AnnotatedArrayTypeImpl(Type type, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
            super(type, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }

        @Override // java.lang.reflect.AnnotatedArrayType
        public AnnotatedType getAnnotatedGenericComponentType() {
            return AnnotatedTypeFactory.buildAnnotatedType(getComponentType(), getLocation().pushArray(), getTypeAnnotations(), getTypeAnnotations(), getDecl());
        }

        private Type getComponentType() {
            Type type = getType();
            if (type instanceof Class) {
                return ((Class) type).getComponentType();
            }
            return ((GenericArrayType) type).getGenericComponentType();
        }
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory$AnnotatedTypeVariableImpl.class */
    private static final class AnnotatedTypeVariableImpl extends AnnotatedTypeBaseImpl implements AnnotatedTypeVariable {
        AnnotatedTypeVariableImpl(TypeVariable<?> typeVariable, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
            super(typeVariable, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }

        @Override // java.lang.reflect.AnnotatedTypeVariable
        public AnnotatedType[] getAnnotatedBounds() {
            return getTypeVariable().getAnnotatedBounds();
        }

        private TypeVariable<?> getTypeVariable() {
            return (TypeVariable) getType();
        }
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory$AnnotatedParameterizedTypeImpl.class */
    private static final class AnnotatedParameterizedTypeImpl extends AnnotatedTypeBaseImpl implements AnnotatedParameterizedType {
        AnnotatedParameterizedTypeImpl(ParameterizedType parameterizedType, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
            super(parameterizedType, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
        }

        @Override // java.lang.reflect.AnnotatedParameterizedType
        public AnnotatedType[] getAnnotatedActualTypeArguments() {
            Type[] actualTypeArguments = getParameterizedType().getActualTypeArguments();
            AnnotatedType[] annotatedTypeArr = new AnnotatedType[actualTypeArguments.length];
            Arrays.fill(annotatedTypeArr, AnnotatedTypeFactory.EMPTY_ANNOTATED_TYPE);
            int length = getTypeAnnotations().length;
            for (int i2 = 0; i2 < annotatedTypeArr.length; i2++) {
                ArrayList arrayList = new ArrayList(length);
                TypeAnnotation.LocationInfo locationInfoPushTypeArg = getLocation().pushTypeArg((byte) i2);
                for (TypeAnnotation typeAnnotation : getTypeAnnotations()) {
                    if (typeAnnotation.getLocationInfo().isSameLocationInfo(locationInfoPushTypeArg)) {
                        arrayList.add(typeAnnotation);
                    }
                }
                annotatedTypeArr[i2] = AnnotatedTypeFactory.buildAnnotatedType(actualTypeArguments[i2], locationInfoPushTypeArg, (TypeAnnotation[]) arrayList.toArray(new TypeAnnotation[0]), getTypeAnnotations(), getDecl());
            }
            return annotatedTypeArr;
        }

        private ParameterizedType getParameterizedType() {
            return (ParameterizedType) getType();
        }
    }

    /* loaded from: rt.jar:sun/reflect/annotation/AnnotatedTypeFactory$AnnotatedWildcardTypeImpl.class */
    private static final class AnnotatedWildcardTypeImpl extends AnnotatedTypeBaseImpl implements AnnotatedWildcardType {
        private final boolean hasUpperBounds;

        AnnotatedWildcardTypeImpl(WildcardType wildcardType, TypeAnnotation.LocationInfo locationInfo, TypeAnnotation[] typeAnnotationArr, TypeAnnotation[] typeAnnotationArr2, AnnotatedElement annotatedElement) {
            super(wildcardType, locationInfo, typeAnnotationArr, typeAnnotationArr2, annotatedElement);
            this.hasUpperBounds = wildcardType.getLowerBounds().length == 0;
        }

        @Override // java.lang.reflect.AnnotatedWildcardType
        public AnnotatedType[] getAnnotatedUpperBounds() {
            if (!hasUpperBounds()) {
                return new AnnotatedType[0];
            }
            return getAnnotatedBounds(getWildcardType().getUpperBounds());
        }

        @Override // java.lang.reflect.AnnotatedWildcardType
        public AnnotatedType[] getAnnotatedLowerBounds() {
            if (this.hasUpperBounds) {
                return new AnnotatedType[0];
            }
            return getAnnotatedBounds(getWildcardType().getLowerBounds());
        }

        private AnnotatedType[] getAnnotatedBounds(Type[] typeArr) {
            AnnotatedType[] annotatedTypeArr = new AnnotatedType[typeArr.length];
            Arrays.fill(annotatedTypeArr, AnnotatedTypeFactory.EMPTY_ANNOTATED_TYPE);
            TypeAnnotation.LocationInfo locationInfoPushWildcard = getLocation().pushWildcard();
            int length = getTypeAnnotations().length;
            for (int i2 = 0; i2 < annotatedTypeArr.length; i2++) {
                ArrayList arrayList = new ArrayList(length);
                for (TypeAnnotation typeAnnotation : getTypeAnnotations()) {
                    if (typeAnnotation.getLocationInfo().isSameLocationInfo(locationInfoPushWildcard)) {
                        arrayList.add(typeAnnotation);
                    }
                }
                annotatedTypeArr[i2] = AnnotatedTypeFactory.buildAnnotatedType(typeArr[i2], locationInfoPushWildcard, (TypeAnnotation[]) arrayList.toArray(new TypeAnnotation[0]), getTypeAnnotations(), getDecl());
            }
            return annotatedTypeArr;
        }

        private WildcardType getWildcardType() {
            return (WildcardType) getType();
        }

        private boolean hasUpperBounds() {
            return this.hasUpperBounds;
        }
    }
}
