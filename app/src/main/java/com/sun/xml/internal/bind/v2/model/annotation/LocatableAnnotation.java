package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/LocatableAnnotation.class */
public class LocatableAnnotation implements InvocationHandler, Locatable, Location {
    private final Annotation core;
    private final Locatable upstream;
    private static final Map<Class, Quick> quicks = new HashMap();

    public static <A extends Annotation> A create(A annotation, Locatable parentSourcePos) {
        if (annotation == null) {
            return null;
        }
        Class<? extends Annotation> type = annotation.annotationType();
        if (quicks.containsKey(type)) {
            return quicks.get(type).newInstance(parentSourcePos, annotation);
        }
        ClassLoader cl = SecureLoader.getClassClassLoader(LocatableAnnotation.class);
        try {
            Class<?> loadableT = Class.forName(type.getName(), false, cl);
            if (loadableT != type) {
                return annotation;
            }
            return (A) Proxy.newProxyInstance(cl, new Class[]{type, Locatable.class}, new LocatableAnnotation(annotation, parentSourcePos));
        } catch (ClassNotFoundException e2) {
            return annotation;
        } catch (IllegalArgumentException e3) {
            return annotation;
        }
    }

    LocatableAnnotation(Annotation core, Locatable upstream) {
        this.core = core;
        this.upstream = upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getDeclaringClass() == Locatable.class) {
                return method.invoke(this, args);
            }
            if (Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException();
            }
            return method.invoke(this.core, args);
        } catch (InvocationTargetException e2) {
            if (e2.getTargetException() != null) {
                throw e2.getTargetException();
            }
            throw e2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Location
    public String toString() {
        return this.core.toString();
    }

    static {
        for (Quick q2 : Init.getAll()) {
            quicks.put(q2.annotationType(), q2);
        }
    }
}
