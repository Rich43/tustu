package sun.security.krb5.internal.ccache;

import java.io.File;
import java.io.IOException;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/MemoryCredentialsCache.class */
public abstract class MemoryCredentialsCache extends CredentialsCache {
    public abstract boolean exists(String str);

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public abstract void update(Credentials credentials);

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public abstract void save() throws IOException, KrbException;

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public abstract Credentials[] getCredsList();

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public abstract Credentials getCreds(PrincipalName principalName);

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public abstract PrincipalName getPrimaryPrincipal();

    private static CredentialsCache getCCacheInstance(PrincipalName principalName) {
        return null;
    }

    private static CredentialsCache getCCacheInstance(PrincipalName principalName, File file) {
        return null;
    }
}
