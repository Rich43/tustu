package sun.security.provider;

import java.security.AccessController;
import java.security.Provider;
import java.util.LinkedHashMap;
import sun.security.action.PutAllAction;
import sun.security.rsa.SunRsaSignEntries;

/* loaded from: rt.jar:sun/security/provider/VerificationProvider.class */
public final class VerificationProvider extends Provider {
    private static final long serialVersionUID = 7482667077568930381L;
    private static final boolean ACTIVE;

    static {
        boolean z2;
        try {
            Class.forName("sun.security.provider.Sun");
            Class.forName("sun.security.rsa.SunRsaSign");
            z2 = false;
        } catch (ClassNotFoundException e2) {
            z2 = true;
        }
        ACTIVE = z2;
    }

    public VerificationProvider() {
        super("SunJarVerification", 1.8d, "Jar Verification Provider");
        if (!ACTIVE) {
            return;
        }
        if (System.getSecurityManager() == null) {
            SunEntries.putEntries(this);
            SunRsaSignEntries.putEntries(this);
        } else {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            SunEntries.putEntries(linkedHashMap);
            SunRsaSignEntries.putEntries(linkedHashMap);
            AccessController.doPrivileged(new PutAllAction(this, linkedHashMap));
        }
    }
}
