package sun.reflect.generics.reflectiveObjects;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.AnnotationType;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.FieldTypeSignature;
import sun.reflect.generics.visitor.Reifier;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:sun/reflect/generics/reflectiveObjects/TypeVariableImpl.class */
public class TypeVariableImpl<D extends GenericDeclaration> extends LazyReflectiveObjectGenerator implements TypeVariable<D> {
    D genericDeclaration;
    private String name;
    private Type[] bounds;
    private FieldTypeSignature[] boundASTs;
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TypeVariableImpl.class.desiredAssertionStatus();
        EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    }

    private TypeVariableImpl(D d2, String str, FieldTypeSignature[] fieldTypeSignatureArr, GenericsFactory genericsFactory) {
        super(genericsFactory);
        this.genericDeclaration = d2;
        this.name = str;
        this.boundASTs = fieldTypeSignatureArr;
    }

    private FieldTypeSignature[] getBoundASTs() {
        if ($assertionsDisabled || this.bounds == null) {
            return this.boundASTs;
        }
        throw new AssertionError();
    }

    public static <T extends GenericDeclaration> TypeVariableImpl<T> make(T t2, String str, FieldTypeSignature[] fieldTypeSignatureArr, GenericsFactory genericsFactory) {
        if (!(t2 instanceof Class) && !(t2 instanceof Method) && !(t2 instanceof Constructor)) {
            throw new AssertionError((Object) ("Unexpected kind of GenericDeclaration" + t2.getClass().toString()));
        }
        return new TypeVariableImpl<>(t2, str, fieldTypeSignatureArr, genericsFactory);
    }

    @Override // java.lang.reflect.TypeVariable
    public Type[] getBounds() {
        if (this.bounds == null) {
            FieldTypeSignature[] boundASTs = getBoundASTs();
            Type[] typeArr = new Type[boundASTs.length];
            for (int i2 = 0; i2 < boundASTs.length; i2++) {
                Reifier reifier = getReifier();
                boundASTs[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.bounds = typeArr;
        }
        return (Type[]) this.bounds.clone();
    }

    @Override // java.lang.reflect.TypeVariable
    public D getGenericDeclaration() throws SecurityException {
        if (this.genericDeclaration instanceof Class) {
            ReflectUtil.checkPackageAccess((Class<?>) this.genericDeclaration);
        } else if ((this.genericDeclaration instanceof Method) || (this.genericDeclaration instanceof Constructor)) {
            ReflectUtil.conservativeCheckMemberAccess((Member) this.genericDeclaration);
        } else {
            throw new AssertionError((Object) "Unexpected kind of GenericDeclaration");
        }
        return this.genericDeclaration;
    }

    @Override // java.lang.reflect.TypeVariable
    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }

    public boolean equals(Object obj) {
        if ((obj instanceof TypeVariable) && obj.getClass() == TypeVariableImpl.class) {
            TypeVariable typeVariable = (TypeVariable) obj;
            return Objects.equals(this.genericDeclaration, typeVariable.getGenericDeclaration()) && Objects.equals(this.name, typeVariable.getName());
        }
        return false;
    }

    public int hashCode() {
        return this.genericDeclaration.hashCode() ^ this.name.hashCode();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T) mapAnnotations(getAnnotations()).get(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T) getAnnotation(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(mapAnnotations(getAnnotations()), cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) getAnnotationsByType(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        int iTypeVarIndex = typeVarIndex();
        if (iTypeVarIndex < 0) {
            throw new AssertionError((Object) "Index must be non-negative.");
        }
        return TypeAnnotationParser.parseTypeVariableAnnotations(getGenericDeclaration(), iTypeVarIndex);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }

    @Override // java.lang.reflect.TypeVariable
    public AnnotatedType[] getAnnotatedBounds() {
        return TypeAnnotationParser.parseAnnotatedBounds(getBounds(), getGenericDeclaration(), typeVarIndex());
    }

    private int typeVarIndex() {
        int i2 = -1;
        for (TypeVariable<?> typeVariable : getGenericDeclaration().getTypeParameters()) {
            i2++;
            if (equals(typeVariable)) {
                return i2;
            }
        }
        return -1;
    }

    private static Map<Class<? extends Annotation>, Annotation> mapAnnotations(Annotation[] annotationArr) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Annotation annotation : annotationArr) {
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            if (AnnotationType.getInstance(clsAnnotationType).retention() == RetentionPolicy.RUNTIME && linkedHashMap.put(clsAnnotationType, annotation) != 0) {
                throw new AnnotationFormatError("Duplicate annotation for class: " + ((Object) clsAnnotationType) + ": " + ((Object) annotation));
            }
        }
        return linkedHashMap;
    }
}
