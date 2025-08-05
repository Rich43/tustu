package sun.security.krb5.internal.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.security.auth.kerberos.KeyTab;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.krb5.Config;
import sun.security.krb5.KrbAsReqBuilder;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.ccache.Credentials;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.util.Password;

/* loaded from: rt.jar:sun/security/krb5/internal/tools/Kinit.class */
public class Kinit {
    private KinitOptions options;
    private static final boolean DEBUG = Krb5.DEBUG;

    public static void main(String[] strArr) {
        String message;
        try {
            new Kinit(strArr);
        } catch (Exception e2) {
            if (e2 instanceof KrbException) {
                message = ((KrbException) e2).krbErrorMessage() + " " + ((KrbException) e2).returnCodeMessage();
            } else {
                message = e2.getMessage();
            }
            if (message != null) {
                System.err.println("Exception: " + message);
            } else {
                System.out.println("Exception: " + ((Object) e2));
            }
            e2.printStackTrace();
            System.exit(-1);
        }
    }

    private Kinit(String[] strArr) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        if (strArr == null || strArr.length == 0) {
            this.options = new KinitOptions();
        } else {
            this.options = new KinitOptions(strArr);
        }
        switch (this.options.action) {
            case 1:
                acquire();
                return;
            case 2:
                renew();
                return;
            default:
                throw new KrbException("kinit does not support action " + this.options.action);
        }
    }

    private void renew() throws IOException, KrbException {
        PrincipalName principal = this.options.getPrincipal();
        String realmAsString = principal.getRealmAsString();
        CredentialsCache credentialsCache = CredentialsCache.getInstance(this.options.cachename);
        if (credentialsCache == null) {
            throw new IOException("Unable to find existing cache file " + this.options.cachename);
        }
        Credentials cCacheCreds = credentialsCache.getCreds(PrincipalName.tgsService(realmAsString, realmAsString)).setKrbCreds().renew().toCCacheCreds();
        CredentialsCache credentialsCacheCreate = CredentialsCache.create(principal, this.options.cachename);
        if (credentialsCacheCreate == null) {
            throw new IOException("Unable to create the cache file " + this.options.cachename);
        }
        credentialsCacheCreate.update(cCacheCreds);
        credentialsCacheCreate.save();
    }

    private void acquire() throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        KeyTab keyTab;
        KrbAsReqBuilder krbAsReqBuilder;
        String string = null;
        PrincipalName principal = this.options.getPrincipal();
        if (principal != null) {
            string = principal.toString();
        }
        if (DEBUG) {
            System.out.println("Principal is " + ((Object) principal));
        }
        char[] password = this.options.password;
        if (!this.options.useKeytabFile()) {
            if (string == null) {
                throw new IllegalArgumentException(" Can not obtain principal name");
            }
            if (password == null) {
                System.out.print("Password for " + string + CallSiteDescriptor.TOKEN_DELIMITER);
                System.out.flush();
                password = Password.readPassword(System.in);
                if (DEBUG) {
                    System.out.println(">>> Kinit console input " + new String(password));
                }
            }
            krbAsReqBuilder = new KrbAsReqBuilder(principal, password);
        } else {
            if (DEBUG) {
                System.out.println(">>> Kinit using keytab");
            }
            if (string == null) {
                throw new IllegalArgumentException("Principal name must be specified.");
            }
            String strKeytabFileName = this.options.keytabFileName();
            if (strKeytabFileName != null && DEBUG) {
                System.out.println(">>> Kinit keytab file name: " + strKeytabFileName);
            }
            if (strKeytabFileName == null) {
                keyTab = KeyTab.getInstance();
            } else {
                keyTab = KeyTab.getInstance(new File(strKeytabFileName));
            }
            krbAsReqBuilder = new KrbAsReqBuilder(principal, keyTab);
        }
        KDCOptions kDCOptions = new KDCOptions();
        setOptions(1, this.options.forwardable, kDCOptions);
        setOptions(3, this.options.proxiable, kDCOptions);
        krbAsReqBuilder.setOptions(kDCOptions);
        String kDCRealm = this.options.getKDCRealm();
        if (kDCRealm == null) {
            kDCRealm = Config.getInstance().getDefaultRealm();
        }
        if (DEBUG) {
            System.out.println(">>> Kinit realm name is " + kDCRealm);
        }
        krbAsReqBuilder.setTarget(PrincipalName.tgsService(kDCRealm, kDCRealm));
        if (DEBUG) {
            System.out.println(">>> Creating KrbAsReq");
        }
        if (this.options.getAddressOption()) {
            krbAsReqBuilder.setAddresses(HostAddresses.getLocalAddresses());
        }
        krbAsReqBuilder.setTill(this.options.lifetime);
        krbAsReqBuilder.setRTime(this.options.renewable_lifetime);
        krbAsReqBuilder.action();
        Credentials cCreds = krbAsReqBuilder.getCCreds();
        krbAsReqBuilder.destroy();
        CredentialsCache credentialsCacheCreate = CredentialsCache.create(principal, this.options.cachename);
        if (credentialsCacheCreate == null) {
            throw new IOException("Unable to create the cache file " + this.options.cachename);
        }
        credentialsCacheCreate.update(cCreds);
        credentialsCacheCreate.save();
        if (this.options.password == null) {
            System.out.println("New ticket is stored in cache file " + this.options.cachename);
        } else {
            Arrays.fill(this.options.password, '0');
        }
        if (password != null) {
            Arrays.fill(password, '0');
        }
        this.options = null;
    }

    private static void setOptions(int i2, int i3, KDCOptions kDCOptions) throws ArrayIndexOutOfBoundsException {
        switch (i3) {
            case -1:
                kDCOptions.set(i2, false);
                break;
            case 1:
                kDCOptions.set(i2, true);
                break;
        }
    }
}
