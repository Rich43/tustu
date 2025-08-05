package sun.security.provider;

import java.security.AccessController;
import java.security.Provider;
import java.util.LinkedHashMap;
import sun.security.action.PutAllAction;

/* loaded from: jsse.jar:sun/security/provider/Sun.class */
public final class Sun extends Provider {
    private static final long serialVersionUID = 6440182097568097204L;
    private static final String INFO = "SUN (DSA key/parameter generation; DSA signing; SHA-1, MD5 digests; SecureRandom; X.509 certificates; JKS & DKS keystores; PKIX CertPathValidator; PKIX CertPathBuilder; LDAP, Collection CertStores, JavaPolicy Policy; JavaLoginConfig Configuration)";

    public Sun() {
        super("SUN", 1.8d, INFO);
        if (System.getSecurityManager() == null) {
            SunEntries.putEntries(this);
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        SunEntries.putEntries(linkedHashMap);
        AccessController.doPrivileged(new PutAllAction(this, linkedHashMap));
    }
}
