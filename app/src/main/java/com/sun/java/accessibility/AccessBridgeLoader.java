package com.sun.java.accessibility;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import jdk.Exported;

/* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridgeLoader.class
 */
@Exported(false)
/* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridgeLoader.class */
abstract class AccessBridgeLoader {
    boolean useJAWT_DLL;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.sun.java.accessibility.AccessBridgeLoader.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                System.loadLibrary("JavaAccessBridge");
                return null;
            }
        }, (AccessControlContext) null, new RuntimePermission("loadLibrary.JavaAccessBridge"));
    }

    AccessBridgeLoader() {
        this.useJAWT_DLL = false;
        String property = System.getProperty("java.version");
        if (property != null) {
            this.useJAWT_DLL = property.compareTo("1.4.1") >= 0;
        }
        if (this.useJAWT_DLL) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.sun.java.accessibility.AccessBridgeLoader.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    System.loadLibrary("JAWT");
                    System.loadLibrary("JAWTAccessBridge");
                    return null;
                }
            }, (AccessControlContext) null, new RuntimePermission("loadLibrary.JAWT"), new RuntimePermission("loadLibrary.JAWTAccessBridge"));
        }
    }
}
