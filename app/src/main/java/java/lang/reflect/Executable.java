package java.lang.reflect;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import org.icepdf.core.util.PdfOps;
import sun.misc.SharedSecrets;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.TypeAnnotation;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.repository.ConstructorRepository;

/* loaded from: rt.jar:java/lang/reflect/Executable.class */
public abstract class Executable extends AccessibleObject implements Member, GenericDeclaration {
    private volatile transient boolean hasRealParameterData;
    private volatile transient Parameter[] parameters;
    private volatile transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    abstract byte[] getAnnotationBytes();

    abstract Executable getRoot();

    abstract boolean hasGenericInformation();

    abstract ConstructorRepository getGenericInfo();

    abstract void specificToStringHeader(StringBuilder sb);

    abstract void specificToGenericStringHeader(StringBuilder sb);

    public abstract Class<?> getDeclaringClass();

    public abstract String getName();

    public abstract int getModifiers();

    public abstract TypeVariable<?>[] getTypeParameters();

    public abstract Class<?>[] getParameterTypes();

    private native Parameter[] getParameters0();

    native byte[] getTypeAnnotationBytes0();

    public abstract Class<?>[] getExceptionTypes();

    public abstract String toGenericString();

    public abstract Annotation[][] getParameterAnnotations();

    abstract void handleParameterNumberMismatch(int i2, int i3);

    public abstract AnnotatedType getAnnotatedReturnType();

    Executable() {
    }

    boolean equalParamTypes(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length == clsArr2.length) {
            for (int i2 = 0; i2 < clsArr.length; i2++) {
                if (clsArr[i2] != clsArr2[i2]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    Annotation[][] parseParameterAnnotations(byte[] bArr) {
        return AnnotationParser.parseParameterAnnotations(bArr, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
    }

    void separateWithCommas(Class<?>[] clsArr, StringBuilder sb) {
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            sb.append(clsArr[i2].getTypeName());
            if (i2 < clsArr.length - 1) {
                sb.append(",");
            }
        }
    }

    void printModifiersIfNonzero(StringBuilder sb, int i2, boolean z2) {
        int modifiers = getModifiers() & i2;
        if (modifiers != 0 && !z2) {
            sb.append(Modifier.toString(modifiers)).append(' ');
            return;
        }
        int i3 = modifiers & 7;
        if (i3 != 0) {
            sb.append(Modifier.toString(i3)).append(' ');
        }
        if (z2) {
            sb.append("default ");
        }
        int i4 = modifiers & (-8);
        if (i4 != 0) {
            sb.append(Modifier.toString(i4)).append(' ');
        }
    }

    String sharedToString(int i2, boolean z2, Class<?>[] clsArr, Class<?>[] clsArr2) {
        try {
            StringBuilder sb = new StringBuilder();
            printModifiersIfNonzero(sb, i2, z2);
            specificToStringHeader(sb);
            sb.append('(');
            separateWithCommas(clsArr, sb);
            sb.append(')');
            if (clsArr2.length > 0) {
                sb.append(" throws ");
                separateWithCommas(clsArr2, sb);
            }
            return sb.toString();
        } catch (Exception e2) {
            return "<" + ((Object) e2) + ">";
        }
    }

    String sharedToGenericString(int i2, boolean z2) {
        String string;
        try {
            StringBuilder sb = new StringBuilder();
            printModifiersIfNonzero(sb, i2, z2);
            TypeVariable<?>[] typeParameters = getTypeParameters();
            if (typeParameters.length > 0) {
                boolean z3 = true;
                sb.append('<');
                for (TypeVariable<?> typeVariable : typeParameters) {
                    if (!z3) {
                        sb.append(',');
                    }
                    sb.append(typeVariable.toString());
                    z3 = false;
                }
                sb.append("> ");
            }
            specificToGenericStringHeader(sb);
            sb.append('(');
            Type[] genericParameterTypes = getGenericParameterTypes();
            for (int i3 = 0; i3 < genericParameterTypes.length; i3++) {
                String typeName = genericParameterTypes[i3].getTypeName();
                if (isVarArgs() && i3 == genericParameterTypes.length - 1) {
                    typeName = typeName.replaceFirst("\\[\\]$", "...");
                }
                sb.append(typeName);
                if (i3 < genericParameterTypes.length - 1) {
                    sb.append(',');
                }
            }
            sb.append(')');
            Type[] genericExceptionTypes = getGenericExceptionTypes();
            if (genericExceptionTypes.length > 0) {
                sb.append(" throws ");
                for (int i4 = 0; i4 < genericExceptionTypes.length; i4++) {
                    if (genericExceptionTypes[i4] instanceof Class) {
                        string = ((Class) genericExceptionTypes[i4]).getName();
                    } else {
                        string = genericExceptionTypes[i4].toString();
                    }
                    sb.append(string);
                    if (i4 < genericExceptionTypes.length - 1) {
                        sb.append(',');
                    }
                }
            }
            return sb.toString();
        } catch (Exception e2) {
            return "<" + ((Object) e2) + ">";
        }
    }

    public int getParameterCount() {
        throw new AbstractMethodError();
    }

    public Type[] getGenericParameterTypes() {
        if (hasGenericInformation()) {
            return getGenericInfo().getParameterTypes();
        }
        return getParameterTypes();
    }

    Type[] getAllGenericParameterTypes() {
        if (!hasGenericInformation()) {
            return getParameterTypes();
        }
        boolean zHasRealParameterData = hasRealParameterData();
        Type[] genericParameterTypes = getGenericParameterTypes();
        Class<?>[] parameterTypes = getParameterTypes();
        Type[] typeArr = new Type[parameterTypes.length];
        Parameter[] parameters = getParameters();
        int i2 = 0;
        if (!zHasRealParameterData) {
            return genericParameterTypes.length == parameterTypes.length ? genericParameterTypes : parameterTypes;
        }
        for (int i3 = 0; i3 < typeArr.length; i3++) {
            Parameter parameter = parameters[i3];
            if (parameter.isSynthetic() || parameter.isImplicit()) {
                typeArr[i3] = parameterTypes[i3];
            } else {
                typeArr[i3] = genericParameterTypes[i2];
                i2++;
            }
        }
        return typeArr;
    }

    public Parameter[] getParameters() {
        return (Parameter[]) privateGetParameters().clone();
    }

    private Parameter[] synthesizeAllParams() {
        int parameterCount = getParameterCount();
        Parameter[] parameterArr = new Parameter[parameterCount];
        for (int i2 = 0; i2 < parameterCount; i2++) {
            parameterArr[i2] = new Parameter(Constants.ELEMNAME_ARG_STRING + i2, 0, this, i2);
        }
        return parameterArr;
    }

    private void verifyParameters(Parameter[] parameterArr) {
        if (getParameterTypes().length != parameterArr.length) {
            throw new MalformedParametersException("Wrong number of parameters in MethodParameters attribute");
        }
        for (Parameter parameter : parameterArr) {
            String realName = parameter.getRealName();
            int modifiers = parameter.getModifiers();
            if (realName != null && (realName.isEmpty() || realName.indexOf(46) != -1 || realName.indexOf(59) != -1 || realName.indexOf(91) != -1 || realName.indexOf(47) != -1)) {
                throw new MalformedParametersException("Invalid parameter name \"" + realName + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            if (modifiers != (modifiers & 36880)) {
                throw new MalformedParametersException("Invalid parameter modifiers");
            }
        }
    }

    private Parameter[] privateGetParameters() {
        Parameter[] parameters0 = this.parameters;
        if (parameters0 == null) {
            try {
                parameters0 = getParameters0();
                if (parameters0 == null) {
                    this.hasRealParameterData = false;
                    parameters0 = synthesizeAllParams();
                } else {
                    this.hasRealParameterData = true;
                    verifyParameters(parameters0);
                }
                this.parameters = parameters0;
            } catch (IllegalArgumentException e2) {
                throw new MalformedParametersException("Invalid constant pool index");
            }
        }
        return parameters0;
    }

    boolean hasRealParameterData() {
        if (this.parameters == null) {
            privateGetParameters();
        }
        return this.hasRealParameterData;
    }

    byte[] getTypeAnnotationBytes() {
        return getTypeAnnotationBytes0();
    }

    public Type[] getGenericExceptionTypes() {
        if (hasGenericInformation()) {
            Type[] exceptionTypes = getGenericInfo().getExceptionTypes();
            if (exceptionTypes.length > 0) {
                return exceptionTypes;
            }
        }
        return getExceptionTypes();
    }

    public boolean isVarArgs() {
        return (getModifiers() & 128) != 0;
    }

    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    Annotation[][] sharedGetParameterAnnotations(Class<?>[] clsArr, byte[] bArr) {
        int length = clsArr.length;
        if (bArr == null) {
            return new Annotation[length][0];
        }
        Annotation[][] parameterAnnotations = parseParameterAnnotations(bArr);
        if (parameterAnnotations.length != length) {
            handleParameterNumberMismatch(parameterAnnotations.length, length);
        }
        return parameterAnnotations;
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return cls.cast(declaredAnnotations().get(cls));
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(declaredAnnotations(), cls);
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return AnnotationParser.toArray(declaredAnnotations());
    }

    private Map<Class<? extends Annotation>, Annotation> declaredAnnotations() {
        Map<Class<? extends Annotation>, Annotation> map = this.declaredAnnotations;
        Map<Class<? extends Annotation>, Annotation> annotations = map;
        if (map == null) {
            synchronized (this) {
                Map<Class<? extends Annotation>, Annotation> map2 = this.declaredAnnotations;
                annotations = map2;
                if (map2 == null) {
                    Executable root = getRoot();
                    if (root != null) {
                        annotations = root.declaredAnnotations();
                    } else {
                        annotations = AnnotationParser.parseAnnotations(getAnnotationBytes(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
                    }
                    this.declaredAnnotations = annotations;
                }
            }
        }
        return annotations;
    }

    AnnotatedType getAnnotatedReturnType0(Type type) {
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), type, TypeAnnotation.TypeAnnotationTarget.METHOD_RETURN);
    }

    public AnnotatedType getAnnotatedReceiverType() {
        if (Modifier.isStatic(getModifiers())) {
            return null;
        }
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), getDeclaringClass(), TypeAnnotation.TypeAnnotationTarget.METHOD_RECEIVER);
    }

    public AnnotatedType[] getAnnotatedParameterTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), getAllGenericParameterTypes(), TypeAnnotation.TypeAnnotationTarget.METHOD_FORMAL_PARAMETER);
    }

    public AnnotatedType[] getAnnotatedExceptionTypes() {
        return TypeAnnotationParser.buildAnnotatedTypes(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), getGenericExceptionTypes(), TypeAnnotation.TypeAnnotationTarget.THROWS);
    }
}
