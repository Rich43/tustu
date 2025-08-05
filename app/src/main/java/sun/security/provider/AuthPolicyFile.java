package sun.security.provider;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.security.auth.AuthPermission;
import javax.security.auth.Policy;
import javax.security.auth.PrivateCredentialPermission;
import javax.security.auth.Subject;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.provider.PolicyParser;
import sun.security.util.Debug;
import sun.security.util.PolicyUtil;
import sun.security.util.PropertyExpander;

@Deprecated
/* loaded from: rt.jar:sun/security/provider/AuthPolicyFile.class */
public class AuthPolicyFile extends Policy {
    private static final String AUTH_POLICY = "java.security.auth.policy";
    private static final String SECURITY_MANAGER = "java.security.manager";
    private static final String AUTH_POLICY_URL = "auth.policy.url.";
    private Vector<PolicyEntry> policyEntries;
    private Hashtable<Object, Object> aliasMapping;
    private boolean initialized = false;
    private boolean expandProperties = true;
    private boolean ignoreIdentityScope = true;
    static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: sun.security.provider.AuthPolicyFile.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private static final Debug debug = Debug.getInstance("policy", "\t[Auth Policy]");
    private static final Class<?>[] PARAMS = {String.class, String.class};

    public AuthPolicyFile() {
        String property = System.getProperty(AUTH_POLICY);
        if ((property == null ? System.getProperty(SECURITY_MANAGER) : property) != null) {
            init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void init() {
        if (this.initialized) {
            return;
        }
        this.policyEntries = new Vector<>();
        this.aliasMapping = new Hashtable<>(11);
        initPolicyFile();
        this.initialized = true;
    }

    @Override // javax.security.auth.Policy
    public synchronized void refresh() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("refreshPolicy"));
        }
        this.initialized = false;
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.AuthPolicyFile.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                AuthPolicyFile.this.init();
                return null;
            }
        });
    }

    private KeyStore initKeyStore(URL url, String str, String str2) {
        URL url2;
        KeyStore keyStore;
        if (str != null) {
            try {
                try {
                    url2 = new URL(str);
                } catch (MalformedURLException e2) {
                    url2 = new URL(url, str);
                }
                if (debug != null) {
                    debug.println("reading keystore" + ((Object) url2));
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(PolicyUtil.getInputStream(url2));
                if (str2 != null) {
                    keyStore = KeyStore.getInstance(str2);
                } else {
                    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                }
                keyStore.load(bufferedInputStream, null);
                bufferedInputStream.close();
                return keyStore;
            } catch (Exception e3) {
                if (debug != null) {
                    debug.println("Debug info only. No keystore.");
                    e3.printStackTrace();
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    private void initPolicyFile() {
        URL url;
        String property = Security.getProperty("policy.expandProperties");
        if (property != null) {
            this.expandProperties = property.equalsIgnoreCase("true");
        }
        String property2 = Security.getProperty("policy.ignoreIdentityScope");
        if (property2 != null) {
            this.ignoreIdentityScope = property2.equalsIgnoreCase("true");
        }
        String property3 = Security.getProperty("policy.allowSystemProperty");
        if (property3 != null && property3.equalsIgnoreCase("true")) {
            String property4 = System.getProperty(AUTH_POLICY);
            if (property4 != null) {
                boolean z2 = false;
                if (property4.startsWith("=")) {
                    z2 = true;
                    property4 = property4.substring(1);
                }
                try {
                    String strExpand = PropertyExpander.expand(property4);
                    File file = new File(strExpand);
                    if (file.exists()) {
                        url = new URL("file:" + file.getCanonicalPath());
                    } else {
                        url = new URL(strExpand);
                    }
                    if (debug != null) {
                        debug.println("reading " + ((Object) url));
                    }
                    init(url);
                } catch (Exception e2) {
                    if (debug != null) {
                        debug.println("caught exception: " + ((Object) e2));
                    }
                }
                if (z2) {
                    if (debug != null) {
                        debug.println("overriding other policies!");
                        return;
                    }
                    return;
                }
            }
        }
        int i2 = 1;
        boolean z3 = false;
        while (true) {
            String property5 = Security.getProperty(AUTH_POLICY_URL + i2);
            if (property5 != null) {
                try {
                    String strReplace = PropertyExpander.expand(property5).replace(File.separatorChar, '/');
                    if (debug != null) {
                        debug.println("reading " + strReplace);
                    }
                    init(new URL(strReplace));
                    z3 = true;
                } catch (Exception e3) {
                    if (debug != null) {
                        debug.println("Debug info only. Error reading policy " + ((Object) e3));
                        e3.printStackTrace();
                    }
                }
                i2++;
            } else {
                if (!z3) {
                }
                return;
            }
        }
    }

    private boolean checkForTrustedIdentity(Certificate certificate) {
        return false;
    }

    private void init(URL url) {
        PolicyParser policyParser = new PolicyParser(this.expandProperties);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(PolicyUtil.getInputStream(url));
            Throwable th = null;
            try {
                policyParser.read(inputStreamReader);
                KeyStore keyStoreInitKeyStore = initKeyStore(url, policyParser.getKeyStoreUrl(), policyParser.getKeyStoreType());
                Enumeration<PolicyParser.GrantEntry> enumerationGrantElements = policyParser.grantElements();
                while (enumerationGrantElements.hasMoreElements()) {
                    addGrantEntry(enumerationGrantElements.nextElement2(), keyStoreInitKeyStore);
                }
                if (inputStreamReader != null) {
                    if (0 != 0) {
                        try {
                            inputStreamReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        inputStreamReader.close();
                    }
                }
            } finally {
            }
        } catch (PolicyParser.ParsingException e2) {
            System.err.println(AUTH_POLICY + rb.getString(".error.parsing.") + ((Object) url));
            System.err.println(AUTH_POLICY + rb.getString("COLON") + e2.getMessage());
            if (debug != null) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            if (debug != null) {
                debug.println("error parsing " + ((Object) url));
                debug.println(e3.toString());
                e3.printStackTrace();
            }
        }
    }

    CodeSource getCodeSource(PolicyParser.GrantEntry grantEntry, KeyStore keyStore) throws MalformedURLException {
        URL url;
        Certificate[] certificates = null;
        if (grantEntry.signedBy != null) {
            certificates = getCertificates(keyStore, grantEntry.signedBy);
            if (certificates == null) {
                if (debug != null) {
                    debug.println(" no certs for alias " + grantEntry.signedBy + ", ignoring.");
                    return null;
                }
                return null;
            }
        }
        if (grantEntry.codeBase != null) {
            url = new URL(grantEntry.codeBase);
        } else {
            url = null;
        }
        if (grantEntry.principals == null || grantEntry.principals.size() == 0) {
            return canonicalizeCodebase(new CodeSource(url, certificates), false);
        }
        return canonicalizeCodebase(new SubjectCodeSource(null, grantEntry.principals, url, certificates), false);
    }

    private void addGrantEntry(PolicyParser.GrantEntry grantEntry, KeyStore keyStore) {
        CodeSource codeSource;
        Certificate[] certificates;
        Permission authPolicyFile;
        if (debug != null) {
            debug.println("Adding policy entry: ");
            debug.println("  signedBy " + grantEntry.signedBy);
            debug.println("  codeBase " + grantEntry.codeBase);
            if (grantEntry.principals != null) {
                Iterator<PolicyParser.PrincipalEntry> it = grantEntry.principals.iterator();
                while (it.hasNext()) {
                    PolicyParser.PrincipalEntry next = it.next();
                    debug.println(Constants.INDENT + next.getPrincipalClass() + " " + next.getPrincipalName());
                }
            }
            debug.println();
        }
        try {
            codeSource = getCodeSource(grantEntry, keyStore);
        } catch (Exception e2) {
            System.err.println(AUTH_POLICY + rb.getString(".error.adding.Entry.") + ((Object) grantEntry) + rb.getString("SPACE") + ((Object) e2));
        }
        if (codeSource == null) {
            return;
        }
        PolicyEntry policyEntry = new PolicyEntry(codeSource);
        Enumeration<PolicyParser.PermissionEntry> enumerationPermissionElements = grantEntry.permissionElements();
        while (enumerationPermissionElements.hasMoreElements()) {
            PolicyParser.PermissionEntry permissionEntryNextElement2 = enumerationPermissionElements.nextElement2();
            try {
                if (permissionEntryNextElement2.permission.equals("javax.security.auth.PrivateCredentialPermission") && permissionEntryNextElement2.name.endsWith(" self")) {
                    authPolicyFile = getInstance(permissionEntryNextElement2.permission, permissionEntryNextElement2.name + " \"self\"", permissionEntryNextElement2.action);
                } else {
                    authPolicyFile = getInstance(permissionEntryNextElement2.permission, permissionEntryNextElement2.name, permissionEntryNextElement2.action);
                }
                policyEntry.add(authPolicyFile);
                if (debug != null) {
                    debug.println(Constants.INDENT + ((Object) authPolicyFile));
                }
            } catch (ClassNotFoundException e3) {
                if (permissionEntryNextElement2.signedBy != null) {
                    certificates = getCertificates(keyStore, permissionEntryNextElement2.signedBy);
                } else {
                    certificates = null;
                }
                if (certificates != null || permissionEntryNextElement2.signedBy == null) {
                    UnresolvedPermission unresolvedPermission = new UnresolvedPermission(permissionEntryNextElement2.permission, permissionEntryNextElement2.name, permissionEntryNextElement2.action, certificates);
                    policyEntry.add(unresolvedPermission);
                    if (debug != null) {
                        debug.println(Constants.INDENT + ((Object) unresolvedPermission));
                    }
                }
            } catch (InvocationTargetException e4) {
                System.err.println(AUTH_POLICY + rb.getString(".error.adding.Permission.") + permissionEntryNextElement2.permission + rb.getString("SPACE") + ((Object) e4.getTargetException()));
            } catch (Exception e5) {
                System.err.println(AUTH_POLICY + rb.getString(".error.adding.Permission.") + permissionEntryNextElement2.permission + rb.getString("SPACE") + ((Object) e5));
            }
        }
        this.policyEntries.addElement(policyEntry);
        if (debug != null) {
            debug.println();
        }
    }

    private static final Permission getInstance(String str, String str2, String str3) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        return (Permission) Class.forName(str).getConstructor(PARAMS).newInstance(str2, str3);
    }

    Certificate[] getCertificates(KeyStore keyStore, String str) {
        Vector vector = null;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strTrim = stringTokenizer.nextToken().trim();
            i2++;
            Certificate certificate = (Certificate) this.aliasMapping.get(strTrim);
            if (certificate == null && keyStore != null) {
                try {
                    certificate = keyStore.getCertificate(strTrim);
                } catch (KeyStoreException e2) {
                }
                if (certificate != null) {
                    this.aliasMapping.put(strTrim, certificate);
                    this.aliasMapping.put(certificate, strTrim);
                }
            }
            if (certificate != null) {
                if (vector == null) {
                    vector = new Vector();
                }
                vector.addElement(certificate);
            }
        }
        if (vector != null && i2 == vector.size()) {
            Certificate[] certificateArr = new Certificate[vector.size()];
            vector.copyInto(certificateArr);
            return certificateArr;
        }
        return null;
    }

    private final synchronized Enumeration<PolicyEntry> elements() {
        return this.policyEntries.elements();
    }

    @Override // javax.security.auth.Policy
    public PermissionCollection getPermissions(final Subject subject, final CodeSource codeSource) {
        return (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction<PermissionCollection>() { // from class: sun.security.provider.AuthPolicyFile.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PermissionCollection run2() {
                SubjectCodeSource subjectCodeSource = new SubjectCodeSource(subject, null, codeSource == null ? null : codeSource.getLocation(), codeSource == null ? null : codeSource.getCertificates());
                if (AuthPolicyFile.this.initialized) {
                    return AuthPolicyFile.this.getPermissions(new Permissions(), subjectCodeSource);
                }
                return new PolicyPermissions(AuthPolicyFile.this, subjectCodeSource);
            }
        });
    }

    PermissionCollection getPermissions(CodeSource codeSource) {
        if (this.initialized) {
            return getPermissions(new Permissions(), codeSource);
        }
        return new PolicyPermissions(this, codeSource);
    }

    Permissions getPermissions(Permissions permissions, CodeSource codeSource) {
        Certificate[] certificates;
        if (!this.initialized) {
            init();
        }
        CodeSource[] codeSourceArr = {null};
        codeSourceArr[0] = canonicalizeCodebase(codeSource, true);
        if (debug != null) {
            debug.println("evaluate(" + ((Object) codeSourceArr[0]) + ")\n");
        }
        for (int i2 = 0; i2 < this.policyEntries.size(); i2++) {
            PolicyEntry policyEntryElementAt = this.policyEntries.elementAt(i2);
            if (debug != null) {
                debug.println("PolicyFile CodeSource implies: " + policyEntryElementAt.codesource.toString() + "\n\n\t" + codeSourceArr[0].toString() + "\n\n");
            }
            if (policyEntryElementAt.codesource.implies(codeSourceArr[0])) {
                for (int i3 = 0; i3 < policyEntryElementAt.permissions.size(); i3++) {
                    Permission permissionElementAt = policyEntryElementAt.permissions.elementAt(i3);
                    if (debug != null) {
                        debug.println("  granting " + ((Object) permissionElementAt));
                    }
                    if (!addSelfPermissions(permissionElementAt, policyEntryElementAt.codesource, codeSourceArr[0], permissions)) {
                        permissions.add(permissionElementAt);
                    }
                }
            }
        }
        if (!this.ignoreIdentityScope && (certificates = codeSourceArr[0].getCertificates()) != null) {
            for (int i4 = 0; i4 < certificates.length; i4++) {
                if (this.aliasMapping.get(certificates[i4]) == null && checkForTrustedIdentity(certificates[i4])) {
                    permissions.add(new AllPermission());
                }
            }
        }
        return permissions;
    }

    private boolean addSelfPermissions(Permission permission, CodeSource codeSource, CodeSource codeSource2, Permissions permissions) {
        if (!(permission instanceof PrivateCredentialPermission) || !(codeSource instanceof SubjectCodeSource)) {
            return false;
        }
        PrivateCredentialPermission privateCredentialPermission = (PrivateCredentialPermission) permission;
        SubjectCodeSource subjectCodeSource = (SubjectCodeSource) codeSource;
        String[][] principals = privateCredentialPermission.getPrincipals();
        if (principals.length <= 0 || !principals[0][0].equalsIgnoreCase("self") || !principals[0][1].equalsIgnoreCase("self")) {
            return false;
        }
        if (subjectCodeSource.getPrincipals() == null) {
            return true;
        }
        Iterator<PolicyParser.PrincipalEntry> it = subjectCodeSource.getPrincipals().iterator();
        while (it.hasNext()) {
            String[][] principalInfo = getPrincipalInfo(it.next(), codeSource2);
            for (int i2 = 0; i2 < principalInfo.length; i2++) {
                PrivateCredentialPermission privateCredentialPermission2 = new PrivateCredentialPermission(privateCredentialPermission.getCredentialClass() + " " + principalInfo[i2][0] + " \"" + principalInfo[i2][1] + PdfOps.DOUBLE_QUOTE__TOKEN, "read");
                if (debug != null) {
                    debug.println("adding SELF permission: " + privateCredentialPermission2.toString());
                }
                permissions.add(privateCredentialPermission2);
            }
        }
        return true;
    }

    private String[][] getPrincipalInfo(PolicyParser.PrincipalEntry principalEntry, CodeSource codeSource) {
        if (!principalEntry.getPrincipalClass().equals(PolicyParser.PrincipalEntry.WILDCARD_CLASS) && !principalEntry.getPrincipalName().equals(PolicyParser.PrincipalEntry.WILDCARD_NAME)) {
            String[][] strArr = new String[1][2];
            strArr[0][0] = principalEntry.getPrincipalClass();
            strArr[0][1] = principalEntry.getPrincipalName();
            return strArr;
        }
        if (!principalEntry.getPrincipalClass().equals(PolicyParser.PrincipalEntry.WILDCARD_CLASS) && principalEntry.getPrincipalName().equals(PolicyParser.PrincipalEntry.WILDCARD_NAME)) {
            Set<Principal> principals = null;
            try {
                principals = ((SubjectCodeSource) codeSource).getSubject().getPrincipals(Class.forName(principalEntry.getPrincipalClass(), false, ClassLoader.getSystemClassLoader()));
            } catch (Exception e2) {
                if (debug != null) {
                    debug.println("problem finding Principal Class when expanding SELF permission: " + e2.toString());
                }
            }
            if (principals == null) {
                return new String[0][0];
            }
            String[][] strArr2 = new String[principals.size()][2];
            int i2 = 0;
            for (Principal principal : principals) {
                strArr2[i2][0] = principal.getClass().getName();
                strArr2[i2][1] = principal.getName();
                i2++;
            }
            return strArr2;
        }
        Set<Principal> principals2 = ((SubjectCodeSource) codeSource).getSubject().getPrincipals();
        String[][] strArr3 = new String[principals2.size()][2];
        int i3 = 0;
        for (Principal principal2 : principals2) {
            strArr3[i3][0] = principal2.getClass().getName();
            strArr3[i3][1] = principal2.getName();
            i3++;
        }
        return strArr3;
    }

    Certificate[] getSignerCertificates(CodeSource codeSource) {
        Certificate[] certificates = codeSource.getCertificates();
        if (certificates == null) {
            return null;
        }
        for (Certificate certificate : certificates) {
            if (!(certificate instanceof X509Certificate)) {
                return codeSource.getCertificates();
            }
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < certificates.length) {
            i3++;
            while (i2 + 1 < certificates.length && ((X509Certificate) certificates[i2]).getIssuerDN().equals(((X509Certificate) certificates[i2 + 1]).getSubjectDN())) {
                i2++;
            }
            i2++;
        }
        if (i3 == certificates.length) {
            return certificates;
        }
        ArrayList arrayList = new ArrayList();
        int i4 = 0;
        while (i4 < certificates.length) {
            arrayList.add(certificates[i4]);
            while (i4 + 1 < certificates.length && ((X509Certificate) certificates[i4]).getIssuerDN().equals(((X509Certificate) certificates[i4 + 1]).getSubjectDN())) {
                i4++;
            }
            i4++;
        }
        Certificate[] certificateArr = new Certificate[arrayList.size()];
        arrayList.toArray(certificateArr);
        return certificateArr;
    }

    private CodeSource canonicalizeCodebase(CodeSource codeSource, boolean z2) {
        String canonicalPath;
        CodeSource subjectCodeSource = codeSource;
        if (codeSource.getLocation() != null && codeSource.getLocation().getProtocol().equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
            try {
                String strReplace = codeSource.getLocation().getFile().replace('/', File.separatorChar);
                if (strReplace.endsWith("*")) {
                    String strSubstring = strReplace.substring(0, strReplace.length() - 1);
                    boolean z3 = false;
                    if (strSubstring.endsWith(File.separator)) {
                        z3 = true;
                    }
                    if (strSubstring.equals("")) {
                        strSubstring = System.getProperty("user.dir");
                    }
                    File file = new File(strSubstring);
                    String canonicalPath2 = file.getCanonicalPath();
                    StringBuffer stringBuffer = new StringBuffer(canonicalPath2);
                    if (!canonicalPath2.endsWith(File.separator) && (z3 || file.isDirectory())) {
                        stringBuffer.append(File.separatorChar);
                    }
                    stringBuffer.append('*');
                    canonicalPath = stringBuffer.toString();
                } else {
                    canonicalPath = new File(strReplace).getCanonicalPath();
                }
                URL url = new File(canonicalPath).toURL();
                if (codeSource instanceof SubjectCodeSource) {
                    SubjectCodeSource subjectCodeSource2 = (SubjectCodeSource) codeSource;
                    if (z2) {
                        subjectCodeSource = new SubjectCodeSource(subjectCodeSource2.getSubject(), subjectCodeSource2.getPrincipals(), url, getSignerCertificates(subjectCodeSource2));
                    } else {
                        subjectCodeSource = new SubjectCodeSource(subjectCodeSource2.getSubject(), subjectCodeSource2.getPrincipals(), url, subjectCodeSource2.getCertificates());
                    }
                } else if (z2) {
                    subjectCodeSource = new CodeSource(url, getSignerCertificates(codeSource));
                } else {
                    subjectCodeSource = new CodeSource(url, codeSource.getCertificates());
                }
            } catch (IOException e2) {
                if (z2) {
                    if (!(codeSource instanceof SubjectCodeSource)) {
                        subjectCodeSource = new CodeSource(codeSource.getLocation(), getSignerCertificates(codeSource));
                    } else {
                        SubjectCodeSource subjectCodeSource3 = (SubjectCodeSource) codeSource;
                        subjectCodeSource = new SubjectCodeSource(subjectCodeSource3.getSubject(), subjectCodeSource3.getPrincipals(), subjectCodeSource3.getLocation(), getSignerCertificates(subjectCodeSource3));
                    }
                }
            }
        } else if (z2) {
            if (!(codeSource instanceof SubjectCodeSource)) {
                subjectCodeSource = new CodeSource(codeSource.getLocation(), getSignerCertificates(codeSource));
            } else {
                SubjectCodeSource subjectCodeSource4 = (SubjectCodeSource) codeSource;
                subjectCodeSource = new SubjectCodeSource(subjectCodeSource4.getSubject(), subjectCodeSource4.getPrincipals(), subjectCodeSource4.getLocation(), getSignerCertificates(subjectCodeSource4));
            }
        }
        return subjectCodeSource;
    }

    /* loaded from: rt.jar:sun/security/provider/AuthPolicyFile$PolicyEntry.class */
    private static class PolicyEntry {
        CodeSource codesource;
        Vector<Permission> permissions = new Vector<>();

        PolicyEntry(CodeSource codeSource) {
            this.codesource = codeSource;
        }

        void add(Permission permission) {
            this.permissions.addElement(permission);
        }

        CodeSource getCodeSource() {
            return this.codesource;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(AuthPolicyFile.rb.getString("LPARAM"));
            stringBuffer.append((Object) getCodeSource());
            stringBuffer.append("\n");
            for (int i2 = 0; i2 < this.permissions.size(); i2++) {
                Permission permissionElementAt = this.permissions.elementAt(i2);
                stringBuffer.append(AuthPolicyFile.rb.getString("SPACE"));
                stringBuffer.append(AuthPolicyFile.rb.getString("SPACE"));
                stringBuffer.append((Object) permissionElementAt);
                stringBuffer.append(AuthPolicyFile.rb.getString("NEWLINE"));
            }
            stringBuffer.append(AuthPolicyFile.rb.getString("RPARAM"));
            stringBuffer.append(AuthPolicyFile.rb.getString("NEWLINE"));
            return stringBuffer.toString();
        }
    }
}
