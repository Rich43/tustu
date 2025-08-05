package com.sun.xml.internal.bind.api;

import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/Utils.class */
final class Utils {
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
    static final Navigator<Type, Class, Field, Method> REFLECTION_NAVIGATOR;

    static {
        try {
            final Class refNav = Class.forName("com.sun.xml.internal.bind.v2.model.nav.ReflectionNavigator");
            Method getInstance = (Method) AccessController.doPrivileged(new PrivilegedAction<Method>() { // from class: com.sun.xml.internal.bind.api.Utils.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Method run2() throws SecurityException {
                    try {
                        Method getInstance2 = refNav.getDeclaredMethod("getInstance", new Class[0]);
                        getInstance2.setAccessible(true);
                        return getInstance2;
                    } catch (NoSuchMethodException e2) {
                        throw new IllegalStateException("ReflectionNavigator.getInstance can't be found");
                    }
                }
            });
            REFLECTION_NAVIGATOR = (Navigator) getInstance.invoke(null, new Object[0]);
        } catch (ClassNotFoundException e2) {
            throw new IllegalStateException("Can't find ReflectionNavigator class");
        } catch (IllegalAccessException e3) {
            throw new IllegalStateException("ReflectionNavigator.getInstance method is inaccessible");
        } catch (SecurityException e4) {
            LOGGER.log(Level.FINE, "Unable to access ReflectionNavigator.getInstance", (Throwable) e4);
            throw e4;
        } catch (InvocationTargetException e5) {
            throw new IllegalStateException("ReflectionNavigator.getInstance throws the exception");
        }
    }

    private Utils() {
    }
}
