package sun.security.krb5.internal.tools;

import java.io.IOException;
import java.time.Instant;
import sun.security.krb5.Config;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.ccache.FileCredentialsCache;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/krb5/internal/tools/KinitOptions.class */
class KinitOptions {
    public int action;
    public short forwardable;
    public short proxiable;
    public KerberosTime lifetime;
    public KerberosTime renewable_lifetime;
    public String target_service;
    public String keytab_file;
    public String cachename;
    private PrincipalName principal;
    public String realm;
    char[] password;
    public boolean keytab;
    private boolean DEBUG;
    private boolean includeAddresses;
    private boolean useKeytab;
    private String ktabName;

    public KinitOptions() throws RealmException, RuntimeException {
        this.action = 1;
        this.forwardable = (short) 0;
        this.proxiable = (short) 0;
        this.password = null;
        this.DEBUG = Krb5.DEBUG;
        this.includeAddresses = true;
        this.useKeytab = false;
        this.cachename = FileCredentialsCache.getDefaultCacheName();
        if (this.cachename == null) {
            throw new RuntimeException("default cache name error");
        }
        this.principal = getDefaultPrincipal();
    }

    public void setKDCRealm(String str) throws RealmException {
        this.realm = str;
    }

    public String getKDCRealm() {
        if (this.realm == null && this.principal != null) {
            return this.principal.getRealmString();
        }
        return null;
    }

    public KinitOptions(String[] strArr) throws IOException, RuntimeException, KrbException {
        this.action = 1;
        this.forwardable = (short) 0;
        this.proxiable = (short) 0;
        this.password = null;
        this.DEBUG = Krb5.DEBUG;
        this.includeAddresses = true;
        this.useKeytab = false;
        String str = null;
        int i2 = 0;
        while (i2 < strArr.length) {
            if (strArr[i2].equals("-f")) {
                this.forwardable = (short) 1;
            } else if (strArr[i2].equals("-p")) {
                this.proxiable = (short) 1;
            } else if (strArr[i2].equals("-c")) {
                if (strArr[i2 + 1].startsWith(LanguageTag.SEP)) {
                    throw new IllegalArgumentException("input format  not correct:  -c  option must be followed by the cache name");
                }
                i2++;
                this.cachename = strArr[i2];
                if (this.cachename.length() >= 5 && this.cachename.substring(0, 5).equalsIgnoreCase("FILE:")) {
                    this.cachename = this.cachename.substring(5);
                }
            } else if (strArr[i2].equals("-A")) {
                this.includeAddresses = false;
            } else if (strArr[i2].equals("-k")) {
                this.useKeytab = true;
            } else if (strArr[i2].equals("-t")) {
                if (this.ktabName != null) {
                    throw new IllegalArgumentException("-t option/keytab file name repeated");
                }
                if (i2 + 1 < strArr.length) {
                    i2++;
                    this.ktabName = strArr[i2];
                    this.useKeytab = true;
                } else {
                    throw new IllegalArgumentException("-t option requires keytab file name");
                }
            } else if (strArr[i2].equals("-R")) {
                this.action = 2;
            } else if (strArr[i2].equals("-l")) {
                i2++;
                this.lifetime = getTime(Config.duration(strArr[i2]));
            } else if (strArr[i2].equals("-r")) {
                i2++;
                this.renewable_lifetime = getTime(Config.duration(strArr[i2]));
            } else if (strArr[i2].equalsIgnoreCase("-help")) {
                printHelp();
                System.exit(0);
            } else if (str == null) {
                str = strArr[i2];
                try {
                    this.principal = new PrincipalName(str);
                } catch (Exception e2) {
                    throw new IllegalArgumentException("invalid Principal name: " + str + e2.getMessage());
                }
            } else if (this.password == null) {
                this.password = strArr[i2].toCharArray();
            } else {
                throw new IllegalArgumentException("too many parameters");
            }
            i2++;
        }
        if (this.cachename == null) {
            this.cachename = FileCredentialsCache.getDefaultCacheName();
            if (this.cachename == null) {
                throw new RuntimeException("default cache name error");
            }
        }
        if (this.principal == null) {
            this.principal = getDefaultPrincipal();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    sun.security.krb5.PrincipalName getDefaultPrincipal() {
        /*
            Method dump skipped, instructions count: 219
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.krb5.internal.tools.KinitOptions.getDefaultPrincipal():sun.security.krb5.PrincipalName");
    }

    void printHelp() {
        System.out.println("Usage:\n\n1. Initial ticket request:\n    kinit [-A] [-f] [-p] [-c cachename] [-l lifetime] [-r renewable_time]\n          [[-k [-t keytab_file_name]] [principal] [password]");
        System.out.println("2. Renew a ticket:\n    kinit -R [-c cachename] [principal]");
        System.out.println("\nAvailable options to Kerberos 5 ticket request:");
        System.out.println("\t-A   do not include addresses");
        System.out.println("\t-f   forwardable");
        System.out.println("\t-p   proxiable");
        System.out.println("\t-c   cache name (i.e., FILE:\\d:\\myProfiles\\mykrb5cache)");
        System.out.println("\t-l   lifetime");
        System.out.println("\t-r   renewable time (total lifetime a ticket can be renewed)");
        System.out.println("\t-k   use keytab");
        System.out.println("\t-t   keytab file name");
        System.out.println("\tprincipal   the principal name (i.e., qweadf@ATHENA.MIT.EDU qweadf)");
        System.out.println("\tpassword    the principal's Kerberos password");
    }

    public boolean getAddressOption() {
        return this.includeAddresses;
    }

    public boolean useKeytabFile() {
        return this.useKeytab;
    }

    public String keytabFileName() {
        return this.ktabName;
    }

    public PrincipalName getPrincipal() {
        return this.principal;
    }

    private KerberosTime getTime(int i2) {
        return new KerberosTime(Instant.now().plusSeconds(i2));
    }
}
