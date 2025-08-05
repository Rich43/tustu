package com.sun.media.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import javax.sound.sampled.AudioPermission;

/* loaded from: rt.jar:com/sun/media/sound/JSSecurityManager.class */
final class JSSecurityManager {
    private JSSecurityManager() {
    }

    private static boolean hasSecurityManager() {
        return System.getSecurityManager() != null;
    }

    static void checkRecordPermission() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AudioPermission("record"));
        }
    }

    static void loadProperties(final Properties properties, final String str) {
        if (hasSecurityManager()) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.media.sound.JSSecurityManager.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        JSSecurityManager.loadPropertiesImpl(properties, str);
                        return null;
                    }
                });
                return;
            } catch (Exception e2) {
                loadPropertiesImpl(properties, str);
                return;
            }
        }
        loadPropertiesImpl(properties, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadPropertiesImpl(Properties properties, String str) {
        String property = System.getProperty("java.home");
        try {
            if (property == null) {
                throw new Error("Can't find java.home ??");
            }
            FileInputStream fileInputStream = new FileInputStream(new File(new File(property, "lib"), str).getCanonicalPath());
            try {
                properties.load(new BufferedInputStream(fileInputStream));
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Throwable th) {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
        }
    }

    static Thread createThread(Runnable runnable, String str, boolean z2, int i2, boolean z3) {
        Thread thread = new Thread(runnable);
        if (str != null) {
            thread.setName(str);
        }
        thread.setDaemon(z2);
        if (i2 >= 0) {
            thread.setPriority(i2);
        }
        if (z3) {
            thread.start();
        }
        return thread;
    }

    static synchronized <T> List<T> getProviders(final Class<T> cls) {
        ArrayList arrayList = new ArrayList(7);
        final Iterator it = (Iterator) AccessController.doPrivileged(new PrivilegedAction<Iterator<T>>() { // from class: com.sun.media.sound.JSSecurityManager.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Iterator<T> run2() {
                return ServiceLoader.load(cls).iterator();
            }
        });
        PrivilegedAction<Boolean> privilegedAction = new PrivilegedAction<Boolean>() { // from class: com.sun.media.sound.JSSecurityManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(it.hasNext());
            }
        };
        while (((Boolean) AccessController.doPrivileged(privilegedAction)).booleanValue()) {
            try {
                Object next = it.next();
                if (cls.isInstance(next)) {
                    arrayList.add(0, next);
                }
            } catch (Throwable th) {
            }
        }
        return arrayList;
    }
}
