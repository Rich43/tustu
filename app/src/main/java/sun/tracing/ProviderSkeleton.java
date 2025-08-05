package sun.tracing;

import com.sun.tracing.Probe;
import com.sun.tracing.Provider;
import com.sun.tracing.ProviderName;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

/* loaded from: rt.jar:sun/tracing/ProviderSkeleton.class */
public abstract class ProviderSkeleton implements InvocationHandler, Provider {
    protected Class<? extends Provider> providerType;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected boolean active = false;
    protected HashMap<Method, ProbeSkeleton> probes = new HashMap<>();

    protected abstract ProbeSkeleton createProbe(Method method);

    static {
        $assertionsDisabled = !ProviderSkeleton.class.desiredAssertionStatus();
    }

    protected ProviderSkeleton(Class<? extends Provider> cls) {
        this.providerType = cls;
    }

    public void init() {
        for (Method method : (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: sun.tracing.ProviderSkeleton.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Method[] run() {
                return ProviderSkeleton.this.providerType.getDeclaredMethods();
            }
        })) {
            if (method.getReturnType() != Void.TYPE) {
                throw new IllegalArgumentException("Return value of method is not void");
            }
            this.probes.put(method, createProbe(method));
        }
        this.active = true;
    }

    public <T extends Provider> T newProxyInstance() {
        return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: sun.tracing.ProviderSkeleton.2
            /* JADX WARN: Incorrect return type in method signature: ()TT; */
            @Override // java.security.PrivilegedAction
            public Provider run() {
                return (Provider) Proxy.newProxyInstance(ProviderSkeleton.this.providerType.getClassLoader(), new Class[]{ProviderSkeleton.this.providerType}, this);
            }
        });
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass != this.providerType) {
            try {
                if (declaringClass == Provider.class || declaringClass == Object.class) {
                    return method.invoke(this, objArr);
                }
                throw new SecurityException();
            } catch (IllegalAccessException e2) {
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
            } catch (InvocationTargetException e3) {
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
            }
        }
        triggerProbe(method, objArr);
        return null;
    }

    @Override // com.sun.tracing.Provider
    public Probe getProbe(Method method) {
        if (this.active) {
            return this.probes.get(method);
        }
        return null;
    }

    public void dispose() {
        this.active = false;
        this.probes.clear();
    }

    protected String getProviderName() {
        return getAnnotationString(this.providerType, ProviderName.class, this.providerType.getSimpleName());
    }

    protected static String getAnnotationString(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str) {
        String str2 = (String) getAnnotationValue(annotatedElement, cls, "value", str);
        return str2.isEmpty() ? str : str2;
    }

    protected static Object getAnnotationValue(AnnotatedElement annotatedElement, Class<? extends Annotation> cls, String str, Object obj) throws IllegalArgumentException {
        Object objInvoke = obj;
        try {
            objInvoke = cls.getMethod(str, new Class[0]).invoke(annotatedElement.getAnnotation(cls), new Object[0]);
        } catch (IllegalAccessException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (NoSuchMethodException e3) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (NullPointerException e4) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (InvocationTargetException e5) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        return objInvoke;
    }

    protected void triggerProbe(Method method, Object[] objArr) {
        ProbeSkeleton probeSkeleton;
        if (this.active && (probeSkeleton = this.probes.get(method)) != null) {
            probeSkeleton.uncheckedTrigger(objArr);
        }
    }
}
