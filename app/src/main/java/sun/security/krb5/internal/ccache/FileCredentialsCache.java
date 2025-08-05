package sun.security.krb5.internal.ccache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.LoginOptions;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.util.SecurityProperties;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/FileCredentialsCache.class */
public class FileCredentialsCache extends CredentialsCache implements FileCCacheConstants {
    public int version;
    public Tag tag;
    public PrincipalName primaryPrincipal;
    private Vector<Credentials> credentialsList;
    private static String dir;
    private static boolean DEBUG = Krb5.DEBUG;
    private List<CredentialsCache.ConfigEntry> configEntries = new ArrayList();

    public static synchronized FileCredentialsCache acquireInstance(PrincipalName principalName, String str) {
        try {
            FileCredentialsCache fileCredentialsCache = new FileCredentialsCache();
            if (str == null) {
                cacheName = getDefaultCacheName();
            } else {
                cacheName = checkValidation(str);
            }
            if (cacheName == null || !new File(cacheName).exists()) {
                return null;
            }
            if (principalName != null) {
                fileCredentialsCache.primaryPrincipal = principalName;
            }
            fileCredentialsCache.load(cacheName);
            return fileCredentialsCache;
        } catch (IOException e2) {
            if (DEBUG) {
                e2.printStackTrace();
                return null;
            }
            return null;
        } catch (KrbException e3) {
            if (DEBUG) {
                e3.printStackTrace();
                return null;
            }
            return null;
        }
    }

    public static FileCredentialsCache acquireInstance() {
        return acquireInstance(null, null);
    }

    static synchronized FileCredentialsCache New(PrincipalName principalName, String str) {
        try {
            FileCredentialsCache fileCredentialsCache = new FileCredentialsCache();
            cacheName = checkValidation(str);
            if (cacheName == null) {
                return null;
            }
            fileCredentialsCache.init(principalName, cacheName);
            return fileCredentialsCache;
        } catch (IOException | KrbException e2) {
            return null;
        }
    }

    static synchronized FileCredentialsCache New(PrincipalName principalName) {
        try {
            FileCredentialsCache fileCredentialsCache = new FileCredentialsCache();
            cacheName = getDefaultCacheName();
            fileCredentialsCache.init(principalName, cacheName);
            return fileCredentialsCache;
        } catch (IOException e2) {
            if (DEBUG) {
                e2.printStackTrace();
                return null;
            }
            return null;
        } catch (KrbException e3) {
            if (DEBUG) {
                e3.printStackTrace();
                return null;
            }
            return null;
        }
    }

    private FileCredentialsCache() {
    }

    boolean exists(String str) {
        if (new File(str).exists()) {
            return true;
        }
        return false;
    }

    synchronized void init(PrincipalName principalName, String str) throws IOException, KrbException {
        this.primaryPrincipal = principalName;
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        Throwable th = null;
        try {
            CCacheOutputStream cCacheOutputStream = new CCacheOutputStream(fileOutputStream);
            Throwable th2 = null;
            try {
                this.version = FileCCacheConstants.KRB5_FCC_FVNO_3;
                cCacheOutputStream.writeHeader(this.primaryPrincipal, this.version);
                if (cCacheOutputStream != null) {
                    if (0 != 0) {
                        try {
                            cCacheOutputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                    } else {
                        cCacheOutputStream.close();
                    }
                }
                load(str);
            } catch (Throwable th4) {
                if (cCacheOutputStream != null) {
                    if (0 != 0) {
                        try {
                            cCacheOutputStream.close();
                        } catch (Throwable th5) {
                            th2.addSuppressed(th5);
                        }
                    } else {
                        cCacheOutputStream.close();
                    }
                }
                throw th4;
            }
        } finally {
            if (fileOutputStream != null) {
                if (0 != 0) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    fileOutputStream.close();
                }
            }
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x00fd: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:43:0x00fd */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00f8: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:41:0x00f8 */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r9v1, types: [sun.security.krb5.internal.ccache.CCacheInputStream] */
    synchronized void load(String str) throws IOException, KrbException {
        ?? r9;
        ?? r10;
        FileInputStream fileInputStream = new FileInputStream(str);
        Throwable th = null;
        try {
            try {
                CCacheInputStream cCacheInputStream = new CCacheInputStream(fileInputStream);
                Throwable th2 = null;
                this.version = cCacheInputStream.readVersion();
                if (this.version == 1284) {
                    this.tag = cCacheInputStream.readTag();
                } else {
                    this.tag = null;
                    if (this.version == 1281 || this.version == 1282) {
                        cCacheInputStream.setNativeByteOrder();
                    }
                }
                PrincipalName principal = cCacheInputStream.readPrincipal(this.version);
                if (this.primaryPrincipal == null) {
                    this.primaryPrincipal = principal;
                } else if (!this.primaryPrincipal.match(principal)) {
                    throw new IOException("Primary principals don't match.");
                }
                this.credentialsList = new Vector<>();
                while (cCacheInputStream.available() > 0) {
                    Object cred = cCacheInputStream.readCred(this.version);
                    if (cred != null) {
                        if (cred instanceof Credentials) {
                            this.credentialsList.addElement((Credentials) cred);
                        } else {
                            addConfigEntry((CredentialsCache.ConfigEntry) cred);
                        }
                    }
                }
                if (cCacheInputStream != null) {
                    if (0 != 0) {
                        try {
                            cCacheInputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                    } else {
                        cCacheInputStream.close();
                    }
                }
                if (fileInputStream != null) {
                    if (0 == 0) {
                        fileInputStream.close();
                        return;
                    }
                    try {
                        fileInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                }
            } catch (Throwable th5) {
                if (r9 != 0) {
                    if (r10 != 0) {
                        try {
                            r9.close();
                        } catch (Throwable th6) {
                            r10.addSuppressed(th6);
                        }
                    } else {
                        r9.close();
                    }
                }
                throw th5;
            }
        } catch (Throwable th7) {
            if (fileInputStream != null) {
                if (0 != 0) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th8) {
                        th.addSuppressed(th8);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th7;
        }
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public synchronized void update(Credentials credentials) {
        if (this.credentialsList != null) {
            if (this.credentialsList.isEmpty()) {
                this.credentialsList.addElement(credentials);
                return;
            }
            boolean z2 = false;
            for (int i2 = 0; i2 < this.credentialsList.size(); i2++) {
                Credentials credentialsElementAt = this.credentialsList.elementAt(i2);
                if (match(credentials.sname.getNameStrings(), credentialsElementAt.sname.getNameStrings()) && credentials.sname.getRealmString().equalsIgnoreCase(credentialsElementAt.sname.getRealmString())) {
                    z2 = true;
                    if (credentials.endtime.getTime() >= credentialsElementAt.endtime.getTime()) {
                        if (DEBUG) {
                            System.out.println(" >>> FileCredentialsCache Ticket matched, overwrite the old one.");
                        }
                        this.credentialsList.removeElementAt(i2);
                        this.credentialsList.addElement(credentials);
                    }
                }
            }
            if (!z2) {
                if (DEBUG) {
                    System.out.println(" >>> FileCredentialsCache Ticket not exactly matched, add new one into cache.");
                }
                this.credentialsList.addElement(credentials);
            }
        }
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public synchronized PrincipalName getPrimaryPrincipal() {
        return this.primaryPrincipal;
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public synchronized void save() throws Asn1Exception, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(cacheName);
        Throwable th = null;
        try {
            CCacheOutputStream cCacheOutputStream = new CCacheOutputStream(fileOutputStream);
            Throwable th2 = null;
            try {
                try {
                    cCacheOutputStream.writeHeader(this.primaryPrincipal, this.version);
                    Credentials[] credsList = getCredsList();
                    if (credsList != null) {
                        for (Credentials credentials : credsList) {
                            cCacheOutputStream.addCreds(credentials);
                        }
                    }
                    Iterator<CredentialsCache.ConfigEntry> it = getConfigEntries().iterator();
                    while (it.hasNext()) {
                        cCacheOutputStream.addConfigEntry(this.primaryPrincipal, it.next());
                    }
                    if (cCacheOutputStream != null) {
                        if (0 != 0) {
                            try {
                                cCacheOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            cCacheOutputStream.close();
                        }
                    }
                    if (fileOutputStream != null) {
                        if (0 == 0) {
                            fileOutputStream.close();
                            return;
                        }
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    }
                } catch (Throwable th5) {
                    if (cCacheOutputStream != null) {
                        if (th2 != null) {
                            try {
                                cCacheOutputStream.close();
                            } catch (Throwable th6) {
                                th2.addSuppressed(th6);
                            }
                        } else {
                            cCacheOutputStream.close();
                        }
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                th2 = th7;
                throw th7;
            }
        } catch (Throwable th8) {
            if (fileOutputStream != null) {
                if (0 != 0) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th9) {
                        th.addSuppressed(th9);
                    }
                } else {
                    fileOutputStream.close();
                }
            }
            throw th8;
        }
    }

    boolean match(String[] strArr, String[] strArr2) {
        if (strArr.length != strArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (!strArr[i2].equalsIgnoreCase(strArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public synchronized Credentials[] getCredsList() {
        if (this.credentialsList == null || this.credentialsList.isEmpty()) {
            return null;
        }
        Credentials[] credentialsArr = new Credentials[this.credentialsList.size()];
        for (int i2 = 0; i2 < this.credentialsList.size(); i2++) {
            credentialsArr[i2] = this.credentialsList.elementAt(i2);
        }
        return credentialsArr;
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public Credentials getCreds(LoginOptions loginOptions, PrincipalName principalName) {
        if (loginOptions == null) {
            return getCreds(principalName);
        }
        Credentials[] credsList = getCredsList();
        if (credsList == null) {
            return null;
        }
        for (int i2 = 0; i2 < credsList.length; i2++) {
            if (principalName.match(credsList[i2].sname) && credsList[i2].flags.match(loginOptions)) {
                return credsList[i2];
            }
        }
        return null;
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public void addConfigEntry(CredentialsCache.ConfigEntry configEntry) {
        this.configEntries.add(configEntry);
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public List<CredentialsCache.ConfigEntry> getConfigEntries() {
        return Collections.unmodifiableList(this.configEntries);
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public Credentials getCreds(PrincipalName principalName) {
        Credentials[] credsList = getCredsList();
        if (credsList == null) {
            return null;
        }
        for (int i2 = 0; i2 < credsList.length; i2++) {
            if (principalName.match(credsList[i2].sname)) {
                return credsList[i2];
            }
        }
        return null;
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public sun.security.krb5.Credentials getInitialCreds() {
        boolean z2;
        Credentials defaultCreds = getDefaultCreds();
        if (defaultCreds == null) {
            return null;
        }
        sun.security.krb5.Credentials krbCreds = defaultCreds.setKrbCreds();
        CredentialsCache.ConfigEntry configEntry = getConfigEntry("proxy_impersonator");
        if (configEntry == null) {
            if (DEBUG) {
                System.out.println("get normal credential");
            }
            return krbCreds;
        }
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("jdk.security.krb5.default.initiate.credential");
        if (strPrivilegedGetOverridable == null) {
            strPrivilegedGetOverridable = "always-impersonate";
        }
        switch (strPrivilegedGetOverridable) {
            case "no-impersonate":
                if (DEBUG) {
                    System.out.println("get normal credential");
                }
                return krbCreds;
            case "try-impersonate":
                z2 = false;
                break;
            case "always-impersonate":
                z2 = true;
                break;
            default:
                throw new RuntimeException("Invalid jdk.security.krb5.default.initiate.credential");
        }
        try {
            PrincipalName principalName = new PrincipalName(new String(configEntry.getData(), StandardCharsets.UTF_8));
            if (!krbCreds.getClient().equals(principalName)) {
                if (DEBUG) {
                    System.out.println("proxy_impersonator does not match service name");
                }
                if (z2) {
                    return null;
                }
                return krbCreds;
            }
            PrincipalName primaryPrincipal = getPrimaryPrincipal();
            Credentials credentials = null;
            Credentials[] credsList = getCredsList();
            int length = credsList.length;
            int i2 = 0;
            while (true) {
                if (i2 < length) {
                    Credentials credentials2 = credsList[i2];
                    if (!credentials2.getClientPrincipal().equals(primaryPrincipal) || !credentials2.getServicePrincipal().equals(principalName)) {
                        i2++;
                    } else {
                        credentials = credentials2;
                    }
                }
            }
            if (credentials == null) {
                if (DEBUG) {
                    System.out.println("Cannot find evidence ticket in ccache");
                }
                if (z2) {
                    return null;
                }
                return krbCreds;
            }
            if (DEBUG) {
                System.out.println("Get proxied credential");
            }
            return krbCreds.setProxy(credentials.setKrbCreds());
        } catch (KrbException e2) {
            if (DEBUG) {
                System.out.println("Impersonation with ccache failed");
            }
            if (z2) {
                return null;
            }
            return krbCreds;
        }
    }

    @Override // sun.security.krb5.internal.ccache.CredentialsCache
    public Credentials getDefaultCreds() {
        Credentials[] credsList = getCredsList();
        if (credsList == null) {
            return null;
        }
        for (int length = credsList.length - 1; length >= 0; length--) {
            if (credsList[length].sname.toString().startsWith(PrincipalName.TGS_DEFAULT_SRV_NAME) && credsList[length].sname.getNameStrings()[1].equals(credsList[length].sname.getRealm().toString())) {
                return credsList[length];
            }
        }
        return null;
    }

    public static String getDefaultCacheName() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.krb5.internal.ccache.FileCredentialsCache.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                String strSubstring = System.getenv("KRB5CCNAME");
                if (strSubstring != null && strSubstring.length() >= 5 && strSubstring.regionMatches(true, 0, "FILE:", 0, 5)) {
                    strSubstring = strSubstring.substring(5);
                }
                return strSubstring;
            }
        });
        if (str != null) {
            if (DEBUG) {
                System.out.println(">>>KinitOptions cache name is " + str);
            }
            return str;
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("os.name"));
        if (str2 != null && !str2.startsWith("Windows")) {
            try {
                Class<?> cls = Class.forName("com.sun.security.auth.module.UnixSystem");
                String str3 = File.separator + "tmp" + File.separator + "krb5cc_" + ((Long) cls.getMethod("getUid", new Class[0]).invoke(cls.getConstructor(new Class[0]).newInstance(new Object[0]), new Object[0])).longValue();
                if (DEBUG) {
                    System.out.println(">>>KinitOptions cache name is " + str3);
                }
                return str3;
            } catch (Exception e2) {
                if (DEBUG) {
                    System.out.println("Exception in obtaining uid for Unix platforms Using user's home directory");
                    e2.printStackTrace();
                }
            }
        }
        String str4 = (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
        String str5 = (String) AccessController.doPrivileged(new GetPropertyAction("user.home"));
        if (str5 == null) {
            str5 = (String) AccessController.doPrivileged(new GetPropertyAction("user.dir"));
        }
        String str6 = str4 != null ? str5 + File.separator + "krb5cc_" + str4 : str5 + File.separator + "krb5cc";
        if (DEBUG) {
            System.out.println(">>>KinitOptions cache name is " + str6);
        }
        return str6;
    }

    public static String checkValidation(String str) {
        String canonicalPath;
        if (str == null) {
            return null;
        }
        try {
            canonicalPath = new File(str).getCanonicalPath();
            File file = new File(canonicalPath);
            if (!file.exists()) {
                if (!new File(file.getParent()).isDirectory()) {
                    canonicalPath = null;
                }
            }
        } catch (IOException e2) {
            canonicalPath = null;
        }
        return canonicalPath;
    }

    private static String exec(String str) {
        String line;
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        Vector vector = new Vector();
        while (stringTokenizer.hasMoreTokens()) {
            vector.addElement(stringTokenizer.nextToken());
        }
        final String[] strArr = new String[vector.size()];
        vector.copyInto(strArr);
        try {
            Process process = (Process) AccessController.doPrivileged(new PrivilegedAction<Process>() { // from class: sun.security.krb5.internal.ccache.FileCredentialsCache.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Process run2() {
                    try {
                        return Runtime.getRuntime().exec(strArr);
                    } catch (IOException e2) {
                        if (FileCredentialsCache.DEBUG) {
                            e2.printStackTrace();
                            return null;
                        }
                        return null;
                    }
                }
            });
            if (process == null) {
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "8859_1"));
            if (strArr.length == 1 && strArr[0].equals("/usr/bin/env")) {
                while (true) {
                    String line2 = bufferedReader.readLine();
                    line = line2;
                    if (line2 != null) {
                        if (line.length() >= 11 && line.substring(0, 11).equalsIgnoreCase("KRB5CCNAME=")) {
                            line = line.substring(11);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return line;
        } catch (Exception e2) {
            if (DEBUG) {
                e2.printStackTrace();
                return null;
            }
            return null;
        }
    }
}
