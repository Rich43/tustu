package com.sun.xml.internal.messaging.saaj.soap;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ContextClassloaderLocal.class */
abstract class ContextClassloaderLocal<V> {
    private static final String FAILED_TO_CREATE_NEW_INSTANCE = "FAILED_TO_CREATE_NEW_INSTANCE";
    private WeakHashMap<ClassLoader, V> CACHE = new WeakHashMap<>();

    protected abstract V initialValue() throws Exception;

    ContextClassloaderLocal() {
    }

    public V get() throws Error {
        ClassLoader tccl = getContextClassLoader();
        V instance = this.CACHE.get(tccl);
        if (instance == null) {
            instance = createNewInstance();
            this.CACHE.put(tccl, instance);
        }
        return instance;
    }

    public void set(V instance) {
        this.CACHE.put(getContextClassLoader(), instance);
    }

    private V createNewInstance() {
        try {
            return initialValue();
        } catch (Exception e2) {
            throw new Error(format(FAILED_TO_CREATE_NEW_INSTANCE, getClass().getName()), e2);
        }
    }

    private static String format(String property, Object... args) {
        String text = ResourceBundle.getBundle(ContextClassloaderLocal.class.getName()).getString(property);
        return MessageFormat.format(text, args);
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.internal.messaging.saaj.soap.ContextClassloaderLocal.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }
}
