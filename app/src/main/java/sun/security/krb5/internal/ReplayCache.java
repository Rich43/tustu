package sun.security.krb5.internal;

import java.security.AccessController;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.internal.rcache.AuthTimeWithHash;
import sun.security.krb5.internal.rcache.DflCache;
import sun.security.krb5.internal.rcache.MemoryCache;

/* loaded from: rt.jar:sun/security/krb5/internal/ReplayCache.class */
public abstract class ReplayCache {
    public abstract void checkAndStore(KerberosTime kerberosTime, AuthTimeWithHash authTimeWithHash) throws KrbApErrException;

    public static ReplayCache getInstance(String str) {
        if (str == null) {
            return new MemoryCache();
        }
        if (str.equals("dfl") || str.startsWith("dfl:")) {
            return new DflCache(str);
        }
        if (str.equals(Separation.COLORANT_NONE)) {
            return new ReplayCache() { // from class: sun.security.krb5.internal.ReplayCache.1
                @Override // sun.security.krb5.internal.ReplayCache
                public void checkAndStore(KerberosTime kerberosTime, AuthTimeWithHash authTimeWithHash) throws KrbApErrException {
                }
            };
        }
        throw new IllegalArgumentException("Unknown type: " + str);
    }

    public static ReplayCache getInstance() {
        return getInstance((String) AccessController.doPrivileged(new GetPropertyAction("sun.security.krb5.rcache")));
    }
}
