package sun.awt.image;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/awt/image/NativeLibLoader.class */
class NativeLibLoader {
    NativeLibLoader() {
    }

    static void loadLibraries() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.image.NativeLibLoader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("awt");
                return null;
            }
        });
    }
}
