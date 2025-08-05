package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import sun.misc.SharedSecrets;
import sun.reflect.CallerSensitive;
import sun.reflect.ConstructorAccessor;
import sun.reflect.Reflection;
import sun.reflect.annotation.TypeAnnotation;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.ConstructorRepository;
import sun.reflect.generics.scope.ConstructorScope;

/* loaded from: rt.jar:java/lang/reflect/Constructor.class */
public final class Constructor<T> extends Executable {
    private Class<T> clazz;
    private int slot;
    private Class<?>[] parameterTypes;
    private Class<?>[] exceptionTypes;
    private int modifiers;
    private transient String signature;
    private transient ConstructorRepository genericInfo;
    private byte[] annotations;
    private byte[] parameterAnnotations;
    private volatile ConstructorAccessor constructorAccessor;
    private Constructor<T> root;

    private GenericsFactory getFactory() {
        return CoreReflectionFactory.make(this, ConstructorScope.make(this));
    }

    @Override // java.lang.reflect.Executable
    ConstructorRepository getGenericInfo() {
        if (this.genericInfo == null) {
            this.genericInfo = ConstructorRepository.make(getSignature(), getFactory());
        }
        return this.genericInfo;
    }

    @Override // java.lang.reflect.Executable
    Executable getRoot() {
        return this.root;
    }

    Constructor(Class<T> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2, int i3, String str, byte[] bArr, byte[] bArr2) {
        this.clazz = cls;
        this.parameterTypes = clsArr;
        this.exceptionTypes = clsArr2;
        this.modifiers = i2;
        this.slot = i3;
        this.signature = str;
        this.annotations = bArr;
        this.parameterAnnotations = bArr2;
    }

    Constructor<T> copy() {
        if (this.root != null) {
            throw new IllegalArgumentException("Can not copy a non-root Constructor");
        }
        Constructor<T> constructor = new Constructor<>(this.clazz, this.parameterTypes, this.exceptionTypes, this.modifiers, this.slot, this.signature, this.annotations, this.parameterAnnotations);
        constructor.root = this;
        constructor.constructorAccessor = this.constructorAccessor;
        return constructor;
    }

    @Override // java.lang.reflect.Executable
    boolean hasGenericInformation() {
        return getSignature() != null;
    }

    @Override // java.lang.reflect.Executable
    byte[] getAnnotationBytes() {
        return this.annotations;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public Class<T> getDeclaringClass() {
        return this.clazz;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public String getName() {
        return getDeclaringClass().getName();
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public int getModifiers() {
        return this.modifiers;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.reflect.Executable, java.lang.reflect.GenericDeclaration
    public TypeVariable<Constructor<T>>[] getTypeParameters() {
        if (getSignature() != null) {
            return getGenericInfo().getTypeParameters();
        }
        return new TypeVariable[0];
    }

    @Override // java.lang.reflect.Executable
    public Class<?>[] getParameterTypes() {
        return (Class[]) this.parameterTypes.clone();
    }

    @Override // java.lang.reflect.Executable
    public int getParameterCount() {
        return this.parameterTypes.length;
    }

    @Override // java.lang.reflect.Executable
    public Type[] getGenericParameterTypes() {
        return super.getGenericParameterTypes();
    }

    @Override // java.lang.reflect.Executable
    public Class<?>[] getExceptionTypes() {
        return (Class[]) this.exceptionTypes.clone();
    }

    @Override // java.lang.reflect.Executable
    public Type[] getGenericExceptionTypes() {
        return super.getGenericExceptionTypes();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Constructor)) {
            Constructor constructor = (Constructor) obj;
            if (getDeclaringClass() == constructor.getDeclaringClass()) {
                return equalParamTypes(this.parameterTypes, constructor.parameterTypes);
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return getDeclaringClass().getName().hashCode();
    }

    public String toString() {
        return sharedToString(Modifier.constructorModifiers(), false, this.parameterTypes, this.exceptionTypes);
    }

    @Override // java.lang.reflect.Executable
    void specificToStringHeader(StringBuilder sb) {
        sb.append(getDeclaringClass().getTypeName());
    }

    @Override // java.lang.reflect.Executable
    public String toGenericString() {
        return sharedToGenericString(Modifier.constructorModifiers(), false);
    }

    @Override // java.lang.reflect.Executable
    void specificToGenericStringHeader(StringBuilder sb) {
        specificToStringHeader(sb);
    }

    @CallerSensitive
    public T newInstance(Object... objArr) throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, null, this.modifiers);
        }
        if ((this.clazz.getModifiers() & 16384) != 0) {
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        }
        ConstructorAccessor constructorAccessorAcquireConstructorAccessor = this.constructorAccessor;
        if (constructorAccessorAcquireConstructorAccessor == null) {
            constructorAccessorAcquireConstructorAccessor = acquireConstructorAccessor();
        }
        return (T) constructorAccessorAcquireConstructorAccessor.newInstance(objArr);
    }

    @Override // java.lang.reflect.Executable
    public boolean isVarArgs() {
        return super.isVarArgs();
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public boolean isSynthetic() {
        return super.isSynthetic();
    }

    private ConstructorAccessor acquireConstructorAccessor() {
        ConstructorAccessor constructorAccessorNewConstructorAccessor = null;
        if (this.root != null) {
            constructorAccessorNewConstructorAccessor = this.root.getConstructorAccessor();
        }
        if (constructorAccessorNewConstructorAccessor != null) {
            this.constructorAccessor = constructorAccessorNewConstructorAccessor;
        } else {
            constructorAccessorNewConstructorAccessor = reflectionFactory.newConstructorAccessor(this);
            setConstructorAccessor(constructorAccessorNewConstructorAccessor);
        }
        return constructorAccessorNewConstructorAccessor;
    }

    ConstructorAccessor getConstructorAccessor() {
        return this.constructorAccessor;
    }

    void setConstructorAccessor(ConstructorAccessor constructorAccessor) {
        this.constructorAccessor = constructorAccessor;
        if (this.root != null) {
            this.root.setConstructorAccessor(constructorAccessor);
        }
    }

    int getSlot() {
        return this.slot;
    }

    String getSignature() {
        return this.signature;
    }

    byte[] getRawAnnotations() {
        return this.annotations;
    }

    byte[] getRawParameterAnnotations() {
        return this.parameterAnnotations;
    }

    /* JADX WARN: Incorrect return type in method signature: <T::Ljava/lang/annotation/Annotation;>(Ljava/lang/Class<TT;>;)TT; */
    @Override // java.lang.reflect.Executable, java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public Annotation getAnnotation(Class cls) {
        return super.getAnnotation(cls);
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return super.getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.Executable
    public Annotation[][] getParameterAnnotations() {
        return sharedGetParameterAnnotations(this.parameterTypes, this.parameterAnnotations);
    }

    @Override // java.lang.reflect.Executable
    void handleParameterNumberMismatch(int i2, int i3) {
        Class<T> declaringClass = getDeclaringClass();
        if (declaringClass.isEnum() || declaringClass.isAnonymousClass() || declaringClass.isLocalClass()) {
            return;
        }
        if (!declaringClass.isMemberClass() || (declaringClass.isMemberClass() && (declaringClass.getModifiers() & 8) == 0 && i2 + 1 != i3)) {
            throw new AnnotationFormatError("Parameter annotations don't match number of parameters");
        }
    }

    @Override // java.lang.reflect.Executable
    public AnnotatedType getAnnotatedReturnType() {
        return getAnnotatedReturnType0(getDeclaringClass());
    }

    @Override // java.lang.reflect.Executable
    public AnnotatedType getAnnotatedReceiverType() {
        if (getDeclaringClass().getEnclosingClass() == null) {
            return super.getAnnotatedReceiverType();
        }
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), getDeclaringClass().getEnclosingClass(), TypeAnnotation.TypeAnnotationTarget.METHOD_RECEIVER);
    }
}
