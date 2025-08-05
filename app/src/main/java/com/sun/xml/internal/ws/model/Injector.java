package com.sun.xml.internal.ws.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/Injector.class */
final class Injector {
    private static final Logger LOGGER = Logger.getLogger(Injector.class.getName());
    private static final Method defineClass;
    private static final Method resolveClass;
    private static final Method getPackage;
    private static final Method definePackage;

    Injector() {
    }

    static {
        try {
            defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass", Class.class);
            getPackage = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
            definePackage = ClassLoader.class.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class);
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.xml.internal.ws.model.Injector.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Injector.defineClass.setAccessible(true);
                    Injector.resolveClass.setAccessible(true);
                    Injector.getPackage.setAccessible(true);
                    Injector.definePackage.setAccessible(true);
                    return null;
                }
            });
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodError(e2.getMessage());
        }
    }

    static synchronized Class inject(ClassLoader cl, String className, byte[] image) throws IllegalArgumentException {
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException e2) {
            try {
                int packIndex = className.lastIndexOf(46);
                if (packIndex != -1) {
                    String pkgname = className.substring(0, packIndex);
                    Package pkg = (Package) getPackage.invoke(cl, pkgname);
                    if (pkg == null) {
                        definePackage.invoke(cl, pkgname, null, null, null, null, null, null, null);
                    }
                }
                Class c2 = (Class) defineClass.invoke(cl, className.replace('/', '.'), image, 0, Integer.valueOf(image.length));
                resolveClass.invoke(cl, c2);
                return c2;
            } catch (IllegalAccessException e3) {
                LOGGER.log(Level.FINE, "Unable to inject " + className, (Throwable) e3);
                throw new WebServiceException(e3);
            } catch (InvocationTargetException e4) {
                LOGGER.log(Level.FINE, "Unable to inject " + className, (Throwable) e4);
                throw new WebServiceException(e4);
            }
        }
    }
}
