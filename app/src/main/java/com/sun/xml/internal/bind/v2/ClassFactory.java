package com.sun.xml.internal.bind.v2;

import com.sun.xml.internal.bind.Util;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/ClassFactory.class */
public final class ClassFactory {
    private static final Class[] emptyClass = new Class[0];
    private static final Object[] emptyObject = new Object[0];
    private static final Logger logger = Util.getClassLogger();
    private static final ThreadLocal<Map<Class, WeakReference<Constructor>>> tls = new ThreadLocal<Map<Class, WeakReference<Constructor>>>() { // from class: com.sun.xml.internal.bind.v2.ClassFactory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Map<Class, WeakReference<Constructor>> initialValue() {
            return new WeakHashMap();
        }
    };

    public static void cleanCache() {
        if (tls != null) {
            try {
                tls.remove();
            } catch (Exception e2) {
                logger.log(Level.WARNING, "Unable to clean Thread Local cache of classes used in Unmarshaller: {0}", e2.getLocalizedMessage());
            }
        }
    }

    public static <T> T create0(Class<T> clazz) throws IllegalAccessException, InstantiationException, SecurityException, InvocationTargetException {
        NoSuchMethodError exp;
        Map<Class, WeakReference<Constructor>> m2 = tls.get();
        Constructor<T> cons = null;
        WeakReference<Constructor> consRef = m2.get(clazz);
        if (consRef != null) {
            cons = consRef.get();
        }
        if (cons == null) {
            try {
                cons = clazz.getDeclaredConstructor(emptyClass);
                int classMod = clazz.getModifiers();
                if (!Modifier.isPublic(classMod) || !Modifier.isPublic(cons.getModifiers())) {
                    try {
                        cons.setAccessible(true);
                    } catch (SecurityException e2) {
                        logger.log(Level.FINE, "Unable to make the constructor of " + ((Object) clazz) + " accessible", (Throwable) e2);
                        throw e2;
                    }
                }
                m2.put(clazz, new WeakReference<>(cons));
            } catch (NoSuchMethodException e3) {
                logger.log(Level.INFO, "No default constructor found on " + ((Object) clazz), (Throwable) e3);
                if (clazz.getDeclaringClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
                    exp = new NoSuchMethodError(Messages.NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS.format(clazz.getName()));
                } else {
                    exp = new NoSuchMethodError(e3.getMessage());
                }
                exp.initCause(e3);
                throw exp;
            }
        }
        return cons.newInstance(emptyObject);
    }

    public static <T> T create(Class<T> cls) {
        try {
            return (T) create0(cls);
        } catch (IllegalAccessException e2) {
            logger.log(Level.INFO, "failed to create a new instance of " + ((Object) cls), (Throwable) e2);
            throw new IllegalAccessError(e2.toString());
        } catch (InstantiationException e3) {
            logger.log(Level.INFO, "failed to create a new instance of " + ((Object) cls), (Throwable) e3);
            throw new InstantiationError(e3.toString());
        } catch (InvocationTargetException e4) {
            Throwable targetException = e4.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            throw new IllegalStateException(targetException);
        }
    }

    public static Object create(Method method) {
        Throwable errorMsg;
        try {
            return method.invoke(null, emptyObject);
        } catch (ExceptionInInitializerError eie) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), (Throwable) eie);
            errorMsg = eie;
            NoSuchMethodError exp = new NoSuchMethodError(errorMsg.getMessage());
            exp.initCause(errorMsg);
            throw exp;
        } catch (IllegalAccessException e2) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), (Throwable) e2);
            throw new IllegalAccessError(e2.toString());
        } catch (IllegalArgumentException iae) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), (Throwable) iae);
            errorMsg = iae;
            NoSuchMethodError exp2 = new NoSuchMethodError(errorMsg.getMessage());
            exp2.initCause(errorMsg);
            throw exp2;
        } catch (NullPointerException npe) {
            logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), npe);
            errorMsg = npe;
            NoSuchMethodError exp22 = new NoSuchMethodError(errorMsg.getMessage());
            exp22.initCause(errorMsg);
            throw exp22;
        } catch (InvocationTargetException ive) {
            Throwable target = ive.getTargetException();
            if (target instanceof RuntimeException) {
                throw ((RuntimeException) target);
            }
            if (target instanceof Error) {
                throw ((Error) target);
            }
            throw new IllegalStateException(target);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Class<? extends T> inferImplClass(Class<T> cls, Class[] knownImplClasses) {
        if (!cls.isInterface()) {
            return cls;
        }
        for (Class cls2 : knownImplClasses) {
            if (cls.isAssignableFrom(cls2)) {
                return cls2.asSubclass(cls);
            }
        }
        return null;
    }
}
