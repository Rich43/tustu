package sun.security.rsa;

import java.security.AccessController;
import java.security.Provider;
import java.util.HashMap;
import sun.security.action.PutAllAction;

/* loaded from: jsse.jar:sun/security/rsa/SunRsaSign.class */
public final class SunRsaSign extends Provider {
    private static final long serialVersionUID = 866040293550393045L;

    public SunRsaSign() {
        super("SunRsaSign", 1.8d, "Sun RSA signature provider");
        if (System.getSecurityManager() == null) {
            SunRsaSignEntries.putEntries(this);
            return;
        }
        HashMap map = new HashMap();
        SunRsaSignEntries.putEntries(map);
        AccessController.doPrivileged(new PutAllAction(this, map));
    }
}
