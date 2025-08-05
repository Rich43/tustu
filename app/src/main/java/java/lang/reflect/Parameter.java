package java.lang.reflect;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import sun.reflect.annotation.AnnotationSupport;

/* loaded from: rt.jar:java/lang/reflect/Parameter.class */
public final class Parameter implements AnnotatedElement {
    private final String name;
    private final int modifiers;
    private final Executable executable;
    private final int index;
    private volatile transient Type parameterTypeCache = null;
    private volatile transient Class<?> parameterClassCache = null;
    private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    Parameter(String str, int i2, Executable executable, int i3) {
        this.name = str;
        this.modifiers = i2;
        this.executable = executable;
        this.index = i3;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Parameter) {
            Parameter parameter = (Parameter) obj;
            return parameter.executable.equals(this.executable) && parameter.index == this.index;
        }
        return false;
    }

    public int hashCode() {
        return this.executable.hashCode() ^ this.index;
    }

    public boolean isNamePresent() {
        return this.executable.hasRealParameterData() && this.name != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String typeName = getParameterizedType().getTypeName();
        sb.append(Modifier.toString(getModifiers()));
        if (0 != this.modifiers) {
            sb.append(' ');
        }
        if (isVarArgs()) {
            sb.append(typeName.replaceFirst("\\[\\]$", "..."));
        } else {
            sb.append(typeName);
        }
        sb.append(' ');
        sb.append(getName());
        return sb.toString();
    }

    public Executable getDeclaringExecutable() {
        return this.executable;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public String getName() {
        if (this.name == null || this.name.equals("")) {
            return Constants.ELEMNAME_ARG_STRING + this.index;
        }
        return this.name;
    }

    String getRealName() {
        return this.name;
    }

    public Type getParameterizedType() {
        Type type = this.parameterTypeCache;
        if (null == type) {
            type = this.executable.getAllGenericParameterTypes()[this.index];
            this.parameterTypeCache = type;
        }
        return type;
    }

    public Class<?> getType() {
        Class<?> cls = this.parameterClassCache;
        if (null == cls) {
            cls = this.executable.getParameterTypes()[this.index];
            this.parameterClassCache = cls;
        }
        return cls;
    }

    public AnnotatedType getAnnotatedType() {
        return this.executable.getAnnotatedParameterTypes()[this.index];
    }

    public boolean isImplicit() {
        return Modifier.isMandated(getModifiers());
    }

    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    public boolean isVarArgs() {
        return this.executable.isVarArgs() && this.index == this.executable.getParameterCount() - 1;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return cls.cast(declaredAnnotations().get(cls));
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(declaredAnnotations(), cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return this.executable.getParameterAnnotations()[this.index];
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        return (T) getAnnotation(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        return (T[]) getAnnotationsByType(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations() {
        if (null == this.declaredAnnotations) {
            this.declaredAnnotations = new HashMap();
            Annotation[] declaredAnnotations = getDeclaredAnnotations();
            for (int i2 = 0; i2 < declaredAnnotations.length; i2++) {
                this.declaredAnnotations.put(declaredAnnotations[i2].annotationType(), declaredAnnotations[i2]);
            }
        }
        return this.declaredAnnotations;
    }
}
