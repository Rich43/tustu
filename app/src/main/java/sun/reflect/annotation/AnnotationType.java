package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotationType.class */
public class AnnotationType {
    private final Map<String, Class<?>> memberTypes;
    private final Map<String, Object> memberDefaults;
    private final Map<String, Method> members;
    private final RetentionPolicy retention;
    private final boolean inherited;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AnnotationType.class.desiredAssertionStatus();
    }

    public static AnnotationType getInstance(Class<? extends Annotation> cls) {
        JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
        AnnotationType annotationType = javaLangAccess.getAnnotationType(cls);
        if (annotationType == null) {
            annotationType = new AnnotationType(cls);
            if (!javaLangAccess.casAnnotationType(cls, null, annotationType)) {
                annotationType = javaLangAccess.getAnnotationType(cls);
                if (!$assertionsDisabled && annotationType == null) {
                    throw new AssertionError();
                }
            }
        }
        return annotationType;
    }

    private AnnotationType(final Class<? extends Annotation> cls) {
        if (!cls.isAnnotation()) {
            throw new IllegalArgumentException("Not an annotation type");
        }
        Method[] methodArr = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: sun.reflect.annotation.AnnotationType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Method[] run2() {
                return cls.getDeclaredMethods();
            }
        });
        this.memberTypes = new HashMap(methodArr.length + 1, 1.0f);
        this.memberDefaults = new HashMap(0);
        this.members = new HashMap(methodArr.length + 1, 1.0f);
        for (Method method : methodArr) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isAbstract(method.getModifiers()) && !method.isSynthetic()) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException(((Object) method) + " has params");
                }
                String name = method.getName();
                this.memberTypes.put(name, invocationHandlerReturnType(method.getReturnType()));
                this.members.put(name, method);
                Object defaultValue = method.getDefaultValue();
                if (defaultValue != null) {
                    this.memberDefaults.put(name, defaultValue);
                }
            }
        }
        if (cls != Retention.class && cls != Inherited.class) {
            JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
            Map<Class<? extends Annotation>, Annotation> selectAnnotations = AnnotationParser.parseSelectAnnotations(javaLangAccess.getRawClassAnnotations(cls), javaLangAccess.getConstantPool(cls), cls, Retention.class, Inherited.class);
            Retention retention = (Retention) selectAnnotations.get(Retention.class);
            this.retention = retention == null ? RetentionPolicy.CLASS : retention.value();
            this.inherited = selectAnnotations.containsKey(Inherited.class);
            return;
        }
        this.retention = RetentionPolicy.RUNTIME;
        this.inherited = false;
    }

    public static Class<?> invocationHandlerReturnType(Class<?> cls) {
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        if (cls == Character.TYPE) {
            return Character.class;
        }
        if (cls == Double.TYPE) {
            return Double.class;
        }
        if (cls == Float.TYPE) {
            return Float.class;
        }
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        if (cls == Long.TYPE) {
            return Long.class;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        if (cls == Boolean.TYPE) {
            return Boolean.class;
        }
        return cls;
    }

    public Map<String, Class<?>> memberTypes() {
        return this.memberTypes;
    }

    public Map<String, Method> members() {
        return this.members;
    }

    public Map<String, Object> memberDefaults() {
        return this.memberDefaults;
    }

    public RetentionPolicy retention() {
        return this.retention;
    }

    public boolean isInherited() {
        return this.inherited;
    }

    public String toString() {
        return "Annotation Type:\n   Member types: " + ((Object) this.memberTypes) + "\n   Member defaults: " + ((Object) this.memberDefaults) + "\n   Retention policy: " + ((Object) this.retention) + "\n   Inherited: " + this.inherited;
    }
}
