package com.sun.xml.internal.ws.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/InjectionPlan.class */
public abstract class InjectionPlan<T, R> {
    public abstract void inject(T t2, R r2);

    public void inject(T instance, Callable<R> resource) {
        try {
            inject((InjectionPlan<T, R>) instance, (T) resource.call());
        } catch (Exception e2) {
            throw new WebServiceException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/InjectionPlan$FieldInjectionPlan.class */
    public static class FieldInjectionPlan<T, R> extends InjectionPlan<T, R> {
        private final Field field;

        public FieldInjectionPlan(Field field) {
            this.field = field;
        }

        @Override // com.sun.xml.internal.ws.util.InjectionPlan
        public void inject(final T instance, final R resource) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.sun.xml.internal.ws.util.InjectionPlan.FieldInjectionPlan.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() throws IllegalArgumentException {
                    try {
                        if (!FieldInjectionPlan.this.field.isAccessible()) {
                            FieldInjectionPlan.this.field.setAccessible(true);
                        }
                        FieldInjectionPlan.this.field.set(instance, resource);
                        return null;
                    } catch (IllegalAccessException e2) {
                        throw new WebServiceException(e2);
                    }
                }
            });
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/InjectionPlan$MethodInjectionPlan.class */
    public static class MethodInjectionPlan<T, R> extends InjectionPlan<T, R> {
        private final Method method;

        public MethodInjectionPlan(Method method) {
            this.method = method;
        }

        @Override // com.sun.xml.internal.ws.util.InjectionPlan
        public void inject(T instance, R resource) {
            InjectionPlan.invokeMethod(this.method, instance, resource);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void invokeMethod(final Method method, final Object instance, final Object... args) {
        if (method == null) {
            return;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.xml.internal.ws.util.InjectionPlan.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws IllegalArgumentException {
                try {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(instance, args);
                    return null;
                } catch (IllegalAccessException e2) {
                    throw new WebServiceException(e2);
                } catch (InvocationTargetException e3) {
                    throw new WebServiceException(e3);
                }
            }
        });
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/InjectionPlan$Compositor.class */
    private static class Compositor<T, R> extends InjectionPlan<T, R> {
        private final Collection<InjectionPlan<T, R>> children;

        public Compositor(Collection<InjectionPlan<T, R>> children) {
            this.children = children;
        }

        @Override // com.sun.xml.internal.ws.util.InjectionPlan
        public void inject(T instance, R res) {
            for (InjectionPlan<T, R> plan : this.children) {
                plan.inject((InjectionPlan<T, R>) instance, (T) res);
            }
        }

        @Override // com.sun.xml.internal.ws.util.InjectionPlan
        public void inject(T instance, Callable<R> resource) {
            if (!this.children.isEmpty()) {
                super.inject((Compositor<T, R>) instance, (Callable) resource);
            }
        }
    }

    public static <T, R> InjectionPlan<T, R> buildInjectionPlan(Class<? extends T> clazz, Class<R> resourceType, boolean isStatic) throws SecurityException {
        List<InjectionPlan<T, R>> plan = new ArrayList<>();
        Class<?> superclass = clazz;
        while (true) {
            Class<?> cl = superclass;
            if (cl != Object.class) {
                for (Field field : cl.getDeclaredFields()) {
                    Resource resource = (Resource) field.getAnnotation(Resource.class);
                    if (resource != null && isInjectionPoint(resource, field.getType(), "Incorrect type for field" + field.getName(), resourceType)) {
                        if (isStatic && !Modifier.isStatic(field.getModifiers())) {
                            throw new WebServiceException("Static resource " + ((Object) resourceType) + " cannot be injected to non-static " + ((Object) field));
                        }
                        plan.add(new FieldInjectionPlan<>(field));
                    }
                }
                superclass = cl.getSuperclass();
            } else {
                Class<?> superclass2 = clazz;
                while (true) {
                    Class<?> cl2 = superclass2;
                    if (cl2 != Object.class) {
                        for (Method method : cl2.getDeclaredMethods()) {
                            Resource resource2 = (Resource) method.getAnnotation(Resource.class);
                            if (resource2 != null) {
                                Class[] paramTypes = method.getParameterTypes();
                                if (paramTypes.length != 1) {
                                    throw new WebServiceException("Incorrect no of arguments for method " + ((Object) method));
                                }
                                if (!isInjectionPoint(resource2, paramTypes[0], "Incorrect argument types for method" + method.getName(), resourceType)) {
                                    continue;
                                } else {
                                    if (isStatic && !Modifier.isStatic(method.getModifiers())) {
                                        throw new WebServiceException("Static resource " + ((Object) resourceType) + " cannot be injected to non-static " + ((Object) method));
                                    }
                                    plan.add(new MethodInjectionPlan<>(method));
                                }
                            }
                        }
                        superclass2 = cl2.getSuperclass();
                    } else {
                        return new Compositor(plan);
                    }
                }
            }
        }
    }

    private static boolean isInjectionPoint(Resource resource, Class fieldType, String errorMessage, Class resourceType) {
        Class t2 = resource.type();
        if (t2.equals(Object.class)) {
            return fieldType.equals(resourceType);
        }
        if (t2.equals(resourceType)) {
            if (fieldType.isAssignableFrom(resourceType)) {
                return true;
            }
            throw new WebServiceException(errorMessage);
        }
        return false;
    }
}
