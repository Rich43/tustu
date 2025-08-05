package sun.security.ec;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.HashMap;
import sun.security.action.PutAllAction;

/* loaded from: sunec.jar:sun/security/ec/SunEC.class */
public final class SunEC extends Provider {
    private static final long serialVersionUID = -2279741672933606418L;
    private static boolean useFullImplementation;

    static {
        useFullImplementation = true;
        try {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.ec.SunEC.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    System.loadLibrary("sunec");
                    return null;
                }
            });
        } catch (UnsatisfiedLinkError e2) {
            useFullImplementation = false;
        }
    }

    public SunEC() {
        super("SunEC", 1.8d, "Sun Elliptic Curve provider (EC, ECDSA, ECDH)");
        if (System.getSecurityManager() == null) {
            SunECEntries.putEntries(this, useFullImplementation);
            return;
        }
        HashMap map = new HashMap();
        SunECEntries.putEntries(map, useFullImplementation);
        AccessController.doPrivileged(new PutAllAction(this, map));
    }
}
