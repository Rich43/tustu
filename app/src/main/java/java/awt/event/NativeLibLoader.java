package java.awt.event;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:java/awt/event/NativeLibLoader.class */
class NativeLibLoader {
    NativeLibLoader() {
    }

    static void loadLibraries() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.event.NativeLibLoader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("awt");
                return null;
            }
        });
    }
}
