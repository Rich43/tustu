package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.nio.ByteBuffer;
import sun.misc.SharedSecrets;
import sun.reflect.CallerSensitive;
import sun.reflect.MethodAccessor;
import sun.reflect.Reflection;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationType;
import sun.reflect.annotation.ExceptionProxy;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.MethodRepository;
import sun.reflect.generics.scope.MethodScope;

/* loaded from: rt.jar:java/lang/reflect/Method.class */
public final class Method extends Executable {
    private Class<?> clazz;
    private int slot;
    private String name;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;
    private Class<?>[] exceptionTypes;
    private int modifiers;
    private transient String signature;
    private transient MethodRepository genericInfo;
    private byte[] annotations;
    private byte[] parameterAnnotations;
    private byte[] annotationDefault;
    private volatile MethodAccessor methodAccessor;
    private Method root;

    private String getGenericSignature() {
        return this.signature;
    }

    private GenericsFactory getFactory() {
        return CoreReflectionFactory.make(this, MethodScope.make(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.reflect.Executable
    public MethodRepository getGenericInfo() {
        if (this.genericInfo == null) {
            this.genericInfo = MethodRepository.make(getGenericSignature(), getFactory());
        }
        return this.genericInfo;
    }

    Method(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2, int i3, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.clazz = cls;
        this.name = str;
        this.parameterTypes = clsArr;
        this.returnType = cls2;
        this.exceptionTypes = clsArr2;
        this.modifiers = i2;
        this.slot = i3;
        this.signature = str2;
        this.annotations = bArr;
        this.parameterAnnotations = bArr2;
        this.annotationDefault = bArr3;
    }

    Method copy() {
        if (this.root != null) {
            throw new IllegalArgumentException("Can not copy a non-root Method");
        }
        Method method = new Method(this.clazz, this.name, this.parameterTypes, this.returnType, this.exceptionTypes, this.modifiers, this.slot, this.signature, this.annotations, this.parameterAnnotations, this.annotationDefault);
        method.root = this;
        method.methodAccessor = this.methodAccessor;
        return method;
    }

    @Override // java.lang.reflect.Executable
    Executable getRoot() {
        return this.root;
    }

    @Override // java.lang.reflect.Executable
    boolean hasGenericInformation() {
        return getGenericSignature() != null;
    }

    @Override // java.lang.reflect.Executable
    byte[] getAnnotationBytes() {
        return this.annotations;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public Class<?> getDeclaringClass() {
        return this.clazz;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public String getName() {
        return this.name;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public int getModifiers() {
        return this.modifiers;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.reflect.Executable, java.lang.reflect.GenericDeclaration
    public TypeVariable<Method>[] getTypeParameters() {
        if (getGenericSignature() != null) {
            return getGenericInfo().getTypeParameters();
        }
        return new TypeVariable[0];
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public Type getGenericReturnType() {
        if (getGenericSignature() != null) {
            return getGenericInfo().getReturnType();
        }
        return getReturnType();
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
        if (obj != null && (obj instanceof Method)) {
            Method method = (Method) obj;
            if (getDeclaringClass() != method.getDeclaringClass() || getName() != method.getName() || !this.returnType.equals(method.getReturnType())) {
                return false;
            }
            return equalParamTypes(this.parameterTypes, method.parameterTypes);
        }
        return false;
    }

    public int hashCode() {
        return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
    }

    public String toString() {
        return sharedToString(Modifier.methodModifiers(), isDefault(), this.parameterTypes, this.exceptionTypes);
    }

    @Override // java.lang.reflect.Executable
    void specificToStringHeader(StringBuilder sb) {
        sb.append(getReturnType().getTypeName()).append(' ');
        sb.append(getDeclaringClass().getTypeName()).append('.');
        sb.append(getName());
    }

    @Override // java.lang.reflect.Executable
    public String toGenericString() {
        return sharedToGenericString(Modifier.methodModifiers(), isDefault());
    }

    @Override // java.lang.reflect.Executable
    void specificToGenericStringHeader(StringBuilder sb) {
        sb.append(getGenericReturnType().getTypeName()).append(' ');
        sb.append(getDeclaringClass().getTypeName()).append('.');
        sb.append(getName());
    }

    @CallerSensitive
    public Object invoke(Object obj, Object... objArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        MethodAccessor methodAccessorAcquireMethodAccessor = this.methodAccessor;
        if (methodAccessorAcquireMethodAccessor == null) {
            methodAccessorAcquireMethodAccessor = acquireMethodAccessor();
        }
        return methodAccessorAcquireMethodAccessor.invoke(obj, objArr);
    }

    public boolean isBridge() {
        return (getModifiers() & 64) != 0;
    }

    @Override // java.lang.reflect.Executable
    public boolean isVarArgs() {
        return super.isVarArgs();
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.Member
    public boolean isSynthetic() {
        return super.isSynthetic();
    }

    public boolean isDefault() {
        return (getModifiers() & 1033) == 1 && getDeclaringClass().isInterface();
    }

    private MethodAccessor acquireMethodAccessor() {
        MethodAccessor methodAccessorNewMethodAccessor = null;
        if (this.root != null) {
            methodAccessorNewMethodAccessor = this.root.getMethodAccessor();
        }
        if (methodAccessorNewMethodAccessor != null) {
            this.methodAccessor = methodAccessorNewMethodAccessor;
        } else {
            methodAccessorNewMethodAccessor = reflectionFactory.newMethodAccessor(this);
            setMethodAccessor(methodAccessorNewMethodAccessor);
        }
        return methodAccessorNewMethodAccessor;
    }

    MethodAccessor getMethodAccessor() {
        return this.methodAccessor;
    }

    void setMethodAccessor(MethodAccessor methodAccessor) {
        this.methodAccessor = methodAccessor;
        if (this.root != null) {
            this.root.setMethodAccessor(methodAccessor);
        }
    }

    public Object getDefaultValue() {
        if (this.annotationDefault == null) {
            return null;
        }
        Object memberValue = AnnotationParser.parseMemberValue(AnnotationType.invocationHandlerReturnType(getReturnType()), ByteBuffer.wrap(this.annotationDefault), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
        if (memberValue instanceof ExceptionProxy) {
            throw new AnnotationFormatError("Invalid default: " + ((Object) this));
        }
        return memberValue;
    }

    @Override // java.lang.reflect.Executable, java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        return (T) super.getAnnotation(cls);
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
    public AnnotatedType getAnnotatedReturnType() {
        return getAnnotatedReturnType0(getGenericReturnType());
    }

    @Override // java.lang.reflect.Executable
    void handleParameterNumberMismatch(int i2, int i3) {
        throw new AnnotationFormatError("Parameter annotations don't match number of parameters");
    }
}
