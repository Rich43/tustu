package sun.security.krb5.internal.ccache;

import java.io.IOException;
import java.util.List;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.LoginOptions;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/CredentialsCache.class */
public abstract class CredentialsCache {
    static String cacheName;
    static CredentialsCache singleton = null;
    private static boolean DEBUG = Krb5.DEBUG;

    public abstract PrincipalName getPrimaryPrincipal();

    public abstract void update(Credentials credentials);

    public abstract void save() throws IOException, KrbException;

    public abstract Credentials[] getCredsList();

    public abstract Credentials getDefaultCreds();

    public abstract sun.security.krb5.Credentials getInitialCreds();

    public abstract Credentials getCreds(PrincipalName principalName);

    public abstract Credentials getCreds(LoginOptions loginOptions, PrincipalName principalName);

    public abstract void addConfigEntry(ConfigEntry configEntry);

    public abstract List<ConfigEntry> getConfigEntries();

    public static CredentialsCache getInstance(PrincipalName principalName) {
        return FileCredentialsCache.acquireInstance(principalName, null);
    }

    public static CredentialsCache getInstance(String str) {
        if (str.length() >= 5 && str.substring(0, 5).equalsIgnoreCase("FILE:")) {
            return FileCredentialsCache.acquireInstance(null, str.substring(5));
        }
        return FileCredentialsCache.acquireInstance(null, str);
    }

    public static CredentialsCache getInstance(PrincipalName principalName, String str) {
        if (str != null && str.length() >= 5 && str.regionMatches(true, 0, "FILE:", 0, 5)) {
            return FileCredentialsCache.acquireInstance(principalName, str.substring(5));
        }
        return FileCredentialsCache.acquireInstance(principalName, str);
    }

    public static CredentialsCache getInstance() {
        return FileCredentialsCache.acquireInstance();
    }

    public static CredentialsCache create(PrincipalName principalName, String str) {
        if (str == null) {
            throw new RuntimeException("cache name error");
        }
        if (str.length() >= 5 && str.regionMatches(true, 0, "FILE:", 0, 5)) {
            return FileCredentialsCache.New(principalName, str.substring(5));
        }
        return FileCredentialsCache.New(principalName, str);
    }

    public static CredentialsCache create(PrincipalName principalName) {
        return FileCredentialsCache.New(principalName);
    }

    public static String cacheName() {
        return cacheName;
    }

    public ConfigEntry getConfigEntry(String str) {
        List<ConfigEntry> configEntries = getConfigEntries();
        if (configEntries != null) {
            for (ConfigEntry configEntry : configEntries) {
                if (configEntry.getName().equals(str)) {
                    return configEntry;
                }
            }
            return null;
        }
        return null;
    }

    /* loaded from: rt.jar:sun/security/krb5/internal/ccache/CredentialsCache$ConfigEntry.class */
    public static class ConfigEntry {
        private final String name;
        private final PrincipalName princ;
        private final byte[] data;

        public ConfigEntry(String str, PrincipalName principalName, byte[] bArr) {
            this.name = str;
            this.princ = principalName;
            this.data = bArr;
        }

        public String getName() {
            return this.name;
        }

        public PrincipalName getPrinc() {
            return this.princ;
        }

        public byte[] getData() {
            return this.data;
        }

        public String toString() {
            return this.name + (this.princ != null ? "." + ((Object) this.princ) : "") + ": " + new String(this.data);
        }

        public PrincipalName getSName() {
            try {
                return new PrincipalName("krb5_ccache_conf_data/" + this.name + (this.princ != null ? "/" + ((Object) this.princ) : "") + "@X-CACHECONF:");
            } catch (RealmException e2) {
                throw new AssertionError(e2);
            }
        }
    }
}
