package sun.net;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/net/PortConfig.class */
public final class PortConfig {
    private static int defaultUpper;
    private static int defaultLower;
    private static final int upper;
    private static final int lower;

    static native int getLower0();

    static native int getUpper0();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.PortConfig.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("net");
                return null;
            }
        });
        int lower0 = getLower0();
        if (lower0 == -1) {
            lower0 = defaultLower;
        }
        lower = lower0;
        int upper0 = getUpper0();
        if (upper0 == -1) {
            upper0 = defaultUpper;
        }
        upper = upper0;
    }

    public static int getLower() {
        return lower;
    }

    public static int getUpper() {
        return upper;
    }
}
