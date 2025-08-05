package sun.security.provider;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.NetPermission;
import java.net.SocketPermission;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.Security;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;
import java.util.Random;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import org.icepdf.core.util.PdfOps;
import sun.misc.JavaSecurityProtectionDomainAccess;
import sun.misc.SharedSecrets;
import sun.net.www.ParseUtil;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.provider.PolicyParser;
import sun.security.util.Debug;
import sun.security.util.PolicyUtil;
import sun.security.util.PropertyExpander;
import sun.security.util.ResourcesMgr;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/provider/PolicyFile.class */
public class PolicyFile extends Policy {
    private static final String NONE = "NONE";
    private static final String P11KEYSTORE = "PKCS11";
    private static final String SELF = "${{self}}";
    private static final String X500PRINCIPAL = "javax.security.auth.x500.X500Principal";
    private static final String POLICY = "java.security.policy";
    private static final String SECURITY_MANAGER = "java.security.manager";
    private static final String POLICY_URL = "policy.url.";
    private static final String AUTH_POLICY = "java.security.auth.policy";
    private static final String AUTH_POLICY_URL = "auth.policy.url.";
    private static final int DEFAULT_CACHE_SIZE = 1;
    private volatile PolicyInfo policyInfo;
    private boolean constructed = false;
    private boolean expandProperties = true;
    private boolean ignoreIdentityScope = true;
    private boolean allowSystemProperties = true;
    private boolean notUtf8 = false;
    private URL url;
    private static final Debug debug = Debug.getInstance("policy");
    private static final Class[] PARAMS0 = new Class[0];
    private static final Class[] PARAMS1 = {String.class};
    private static final Class[] PARAMS2 = {String.class, String.class};

    public PolicyFile() {
        init((URL) null);
    }

    public PolicyFile(URL url) {
        this.url = url;
        init(url);
    }

    private void init(URL url) {
        int i2;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.provider.PolicyFile.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                PolicyFile.this.expandProperties = "true".equalsIgnoreCase(Security.getProperty("policy.expandProperties"));
                PolicyFile.this.ignoreIdentityScope = "true".equalsIgnoreCase(Security.getProperty("policy.ignoreIdentityScope"));
                PolicyFile.this.allowSystemProperties = "true".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"));
                PolicyFile.this.notUtf8 = "false".equalsIgnoreCase(System.getProperty("sun.security.policy.utf8"));
                return System.getProperty("sun.security.policy.numcaches");
            }
        });
        if (str != null) {
            try {
                i2 = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                i2 = 1;
            }
        } else {
            i2 = 1;
        }
        PolicyInfo policyInfo = new PolicyInfo(i2);
        initPolicyFile(policyInfo, url);
        this.policyInfo = policyInfo;
    }

    private void initPolicyFile(final PolicyInfo policyInfo, final URL url) {
        if (url != null) {
            if (debug != null) {
                debug.println("reading " + ((Object) url));
            }
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.PolicyFile.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    if (!PolicyFile.this.init(url, policyInfo)) {
                        PolicyFile.this.initStaticPolicy(policyInfo);
                        return null;
                    }
                    return null;
                }
            });
        } else {
            if (!initPolicyFile(POLICY, POLICY_URL, policyInfo)) {
                initStaticPolicy(policyInfo);
            }
            initPolicyFile(AUTH_POLICY, AUTH_POLICY_URL, policyInfo);
        }
    }

    private boolean initPolicyFile(final String str, final String str2, final PolicyInfo policyInfo) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.provider.PolicyFile.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                URL url;
                URL url2;
                boolean z2 = false;
                if (PolicyFile.this.allowSystemProperties) {
                    String property = System.getProperty(str);
                    if (property != null) {
                        boolean z3 = false;
                        if (property.startsWith("=")) {
                            z3 = true;
                            property = property.substring(1);
                        }
                        try {
                            String strExpand = PropertyExpander.expand(property);
                            File file = new File(strExpand);
                            if (file.exists()) {
                                url2 = ParseUtil.fileToEncodedURL(new File(file.getCanonicalPath()));
                            } else {
                                url2 = new URL(strExpand);
                            }
                            if (PolicyFile.debug != null) {
                                PolicyFile.debug.println("reading " + ((Object) url2));
                            }
                            if (PolicyFile.this.init(url2, policyInfo)) {
                                z2 = true;
                            }
                        } catch (Exception e2) {
                            if (PolicyFile.debug != null) {
                                PolicyFile.debug.println("caught exception: " + ((Object) e2));
                            }
                        }
                        if (z3) {
                            if (PolicyFile.debug != null) {
                                PolicyFile.debug.println("overriding other policies!");
                            }
                            return Boolean.valueOf(z2);
                        }
                    }
                }
                int i2 = 1;
                while (true) {
                    String property2 = Security.getProperty(str2 + i2);
                    if (property2 != null) {
                        try {
                            String strReplace = PropertyExpander.expand(property2).replace(File.separatorChar, '/');
                            if (property2.startsWith("file:${java.home}/") || property2.startsWith("file:${user.home}/")) {
                                url = new File(strReplace.substring(5)).toURI().toURL();
                            } else {
                                url = new URI(strReplace).toURL();
                            }
                            if (PolicyFile.debug != null) {
                                PolicyFile.debug.println("reading " + ((Object) url));
                            }
                            if (PolicyFile.this.init(url, policyInfo)) {
                                z2 = true;
                            }
                        } catch (Exception e3) {
                            if (PolicyFile.debug != null) {
                                PolicyFile.debug.println("Debug info only. Error reading policy " + ((Object) e3));
                                e3.printStackTrace();
                            }
                        }
                        i2++;
                    } else {
                        return Boolean.valueOf(z2);
                    }
                }
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean init(URL url, PolicyInfo policyInfo) {
        boolean z2 = false;
        PolicyParser policyParser = new PolicyParser(this.expandProperties);
        InputStreamReader inputStreamReader = null;
        try {
            try {
                inputStreamReader = this.notUtf8 ? new InputStreamReader(PolicyUtil.getInputStream(url)) : new InputStreamReader(PolicyUtil.getInputStream(url), "UTF-8");
                policyParser.read(inputStreamReader);
                KeyStore keyStore = null;
                try {
                    keyStore = PolicyUtil.getKeyStore(url, policyParser.getKeyStoreUrl(), policyParser.getKeyStoreType(), policyParser.getKeyStoreProvider(), policyParser.getStorePassURL(), debug);
                } catch (Exception e2) {
                    if (debug != null) {
                        debug.println("Debug info only. Ignoring exception.");
                        e2.printStackTrace();
                    }
                }
                Enumeration<PolicyParser.GrantEntry> enumerationGrantElements = policyParser.grantElements();
                while (enumerationGrantElements.hasMoreElements()) {
                    addGrantEntry(enumerationGrantElements.nextElement2(), keyStore, policyInfo);
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                        z2 = true;
                    } catch (IOException e3) {
                    }
                } else {
                    z2 = true;
                }
            } catch (PolicyParser.ParsingException e4) {
                System.err.println(new MessageFormat(ResourcesMgr.getString("java.security.policy.error.parsing.policy.message")).format(new Object[]{url, e4.getLocalizedMessage()}));
                if (debug != null) {
                    e4.printStackTrace();
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                        z2 = true;
                    } catch (IOException e5) {
                    }
                } else {
                    z2 = true;
                }
            } catch (Exception e6) {
                if (debug != null) {
                    debug.println("error parsing " + ((Object) url));
                    debug.println(e6.toString());
                    e6.printStackTrace();
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                        z2 = true;
                    } catch (IOException e7) {
                    }
                } else {
                    z2 = true;
                }
            }
            return z2;
        } catch (Throwable th) {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e8) {
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initStaticPolicy(final PolicyInfo policyInfo) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.provider.PolicyFile.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                PolicyEntry policyEntry = new PolicyEntry(new CodeSource((URL) null, (Certificate[]) null));
                policyEntry.add(SecurityConstants.LOCAL_LISTEN_PERMISSION);
                policyEntry.add(new PropertyPermission("java.version", "read"));
                policyEntry.add(new PropertyPermission("java.vendor", "read"));
                policyEntry.add(new PropertyPermission("java.vendor.url", "read"));
                policyEntry.add(new PropertyPermission("java.class.version", "read"));
                policyEntry.add(new PropertyPermission("os.name", "read"));
                policyEntry.add(new PropertyPermission("os.version", "read"));
                policyEntry.add(new PropertyPermission("os.arch", "read"));
                policyEntry.add(new PropertyPermission("file.separator", "read"));
                policyEntry.add(new PropertyPermission("path.separator", "read"));
                policyEntry.add(new PropertyPermission("line.separator", "read"));
                policyEntry.add(new PropertyPermission("java.specification.version", "read"));
                policyEntry.add(new PropertyPermission("java.specification.maintenance.version", "read"));
                policyEntry.add(new PropertyPermission("java.specification.vendor", "read"));
                policyEntry.add(new PropertyPermission("java.specification.name", "read"));
                policyEntry.add(new PropertyPermission("java.vm.specification.version", "read"));
                policyEntry.add(new PropertyPermission("java.vm.specification.vendor", "read"));
                policyEntry.add(new PropertyPermission("java.vm.specification.name", "read"));
                policyEntry.add(new PropertyPermission("java.vm.version", "read"));
                policyEntry.add(new PropertyPermission("java.vm.vendor", "read"));
                policyEntry.add(new PropertyPermission("java.vm.name", "read"));
                policyInfo.policyEntries.add(policyEntry);
                String[] extDirs = PolicyParser.parseExtDirs("${{java.ext.dirs}}", 0);
                if (extDirs != null && extDirs.length > 0) {
                    for (String str : extDirs) {
                        try {
                            PolicyEntry policyEntry2 = new PolicyEntry(PolicyFile.this.canonicalizeCodebase(new CodeSource(new URL(str), (Certificate[]) null), false));
                            policyEntry2.add(SecurityConstants.ALL_PERMISSION);
                            policyInfo.policyEntries.add(policyEntry2);
                        } catch (Exception e2) {
                        }
                    }
                    return null;
                }
                return null;
            }
        });
    }

    private CodeSource getCodeSource(PolicyParser.GrantEntry grantEntry, KeyStore keyStore, PolicyInfo policyInfo) throws MalformedURLException {
        URL url;
        Certificate[] certificates = null;
        if (grantEntry.signedBy != null) {
            certificates = getCertificates(keyStore, grantEntry.signedBy, policyInfo);
            if (certificates == null) {
                if (debug != null) {
                    debug.println("  -- No certs for alias '" + grantEntry.signedBy + "' - ignoring entry");
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
        return canonicalizeCodebase(new CodeSource(url, certificates), false);
    }

    private void addGrantEntry(PolicyParser.GrantEntry grantEntry, KeyStore keyStore, PolicyInfo policyInfo) {
        CodeSource codeSource;
        Certificate[] certificates;
        Permission policyFile;
        Certificate[] certificates2;
        if (debug != null) {
            debug.println("Adding policy entry: ");
            debug.println("  signedBy " + grantEntry.signedBy);
            debug.println("  codeBase " + grantEntry.codeBase);
            if (grantEntry.principals != null) {
                Iterator<PolicyParser.PrincipalEntry> it = grantEntry.principals.iterator();
                while (it.hasNext()) {
                    debug.println(Constants.INDENT + it.next().toString());
                }
            }
        }
        try {
            codeSource = getCodeSource(grantEntry, keyStore, policyInfo);
        } catch (Exception e2) {
            System.err.println(new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Entry.message")).format(new Object[]{e2.toString()}));
        }
        if (codeSource == null || !replacePrincipals(grantEntry.principals, keyStore)) {
            return;
        }
        PolicyEntry policyEntry = new PolicyEntry(codeSource, grantEntry.principals);
        Enumeration<PolicyParser.PermissionEntry> enumerationPermissionElements = grantEntry.permissionElements();
        while (enumerationPermissionElements.hasMoreElements()) {
            PolicyParser.PermissionEntry permissionEntryNextElement2 = enumerationPermissionElements.nextElement2();
            try {
                try {
                    try {
                        expandPermissionName(permissionEntryNextElement2, keyStore);
                        if (permissionEntryNextElement2.permission.equals("javax.security.auth.PrivateCredentialPermission") && permissionEntryNextElement2.name.endsWith(" self")) {
                            permissionEntryNextElement2.name = permissionEntryNextElement2.name.substring(0, permissionEntryNextElement2.name.indexOf("self")) + SELF;
                        }
                        if (permissionEntryNextElement2.name != null && permissionEntryNextElement2.name.indexOf(SELF) != -1) {
                            if (permissionEntryNextElement2.signedBy != null) {
                                certificates2 = getCertificates(keyStore, permissionEntryNextElement2.signedBy, policyInfo);
                            } else {
                                certificates2 = null;
                            }
                            policyFile = new SelfPermission(permissionEntryNextElement2.permission, permissionEntryNextElement2.name, permissionEntryNextElement2.action, certificates2);
                        } else {
                            policyFile = getInstance(permissionEntryNextElement2.permission, permissionEntryNextElement2.name, permissionEntryNextElement2.action);
                        }
                        policyEntry.add(policyFile);
                        if (debug != null) {
                            debug.println(Constants.INDENT + ((Object) policyFile));
                        }
                    } catch (Exception e3) {
                        System.err.println(new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Permission.perm.message")).format(new Object[]{permissionEntryNextElement2.permission, e3.toString()}));
                    }
                } catch (InvocationTargetException e4) {
                    System.err.println(new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Permission.perm.message")).format(new Object[]{permissionEntryNextElement2.permission, e4.getTargetException().toString()}));
                }
            } catch (ClassNotFoundException e5) {
                if (permissionEntryNextElement2.signedBy != null) {
                    certificates = getCertificates(keyStore, permissionEntryNextElement2.signedBy, policyInfo);
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
            }
        }
        policyInfo.policyEntries.add(policyEntry);
        if (debug != null) {
            debug.println();
        }
    }

    private static final Permission getInstance(String str, String str2, String str3) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        Class<?> cls = Class.forName(str, false, null);
        Permission knownInstance = getKnownInstance(cls, str2, str3);
        if (knownInstance != null) {
            return knownInstance;
        }
        if (!Permission.class.isAssignableFrom(cls)) {
            throw new ClassCastException(str + " is not a Permission");
        }
        if (str2 == null && str3 == null) {
            try {
                return (Permission) cls.getConstructor(PARAMS0).newInstance(new Object[0]);
            } catch (NoSuchMethodException e2) {
                try {
                    return (Permission) cls.getConstructor(PARAMS1).newInstance(str2);
                } catch (NoSuchMethodException e3) {
                    return (Permission) cls.getConstructor(PARAMS2).newInstance(str2, str3);
                }
            }
        }
        if (str2 != null && str3 == null) {
            try {
                return (Permission) cls.getConstructor(PARAMS1).newInstance(str2);
            } catch (NoSuchMethodException e4) {
                return (Permission) cls.getConstructor(PARAMS2).newInstance(str2, str3);
            }
        }
        return (Permission) cls.getConstructor(PARAMS2).newInstance(str2, str3);
    }

    private static final Permission getKnownInstance(Class<?> cls, String str, String str2) {
        if (cls.equals(FilePermission.class)) {
            return new FilePermission(str, str2);
        }
        if (cls.equals(SocketPermission.class)) {
            return new SocketPermission(str, str2);
        }
        if (cls.equals(RuntimePermission.class)) {
            return new RuntimePermission(str, str2);
        }
        if (cls.equals(PropertyPermission.class)) {
            return new PropertyPermission(str, str2);
        }
        if (cls.equals(NetPermission.class)) {
            return new NetPermission(str, str2);
        }
        if (cls.equals(AllPermission.class)) {
            return SecurityConstants.ALL_PERMISSION;
        }
        return null;
    }

    private Certificate[] getCertificates(KeyStore keyStore, String str, PolicyInfo policyInfo) {
        Certificate certificate;
        ArrayList arrayList = null;
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strTrim = stringTokenizer.nextToken().trim();
            i2++;
            synchronized (policyInfo.aliasMapping) {
                certificate = (Certificate) policyInfo.aliasMapping.get(strTrim);
                if (certificate == null && keyStore != null) {
                    try {
                        certificate = keyStore.getCertificate(strTrim);
                    } catch (KeyStoreException e2) {
                    }
                    if (certificate != null) {
                        policyInfo.aliasMapping.put(strTrim, certificate);
                        policyInfo.aliasMapping.put(certificate, strTrim);
                    }
                }
            }
            if (certificate != null) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(certificate);
            }
        }
        if (arrayList != null && i2 == arrayList.size()) {
            Certificate[] certificateArr = new Certificate[arrayList.size()];
            arrayList.toArray(certificateArr);
            return certificateArr;
        }
        return null;
    }

    @Override // java.security.Policy
    public void refresh() {
        init(this.url);
    }

    @Override // java.security.Policy
    public boolean implies(ProtectionDomain protectionDomain, Permission permission) {
        JavaSecurityProtectionDomainAccess.ProtectionDomainCache pdMapping = this.policyInfo.getPdMapping();
        PermissionCollection permissionCollection = pdMapping.get(protectionDomain);
        if (permissionCollection != null) {
            return permissionCollection.implies(permission);
        }
        PermissionCollection permissions = getPermissions(protectionDomain);
        if (permissions == null) {
            return false;
        }
        pdMapping.put(protectionDomain, permissions);
        return permissions.implies(permission);
    }

    @Override // java.security.Policy
    public PermissionCollection getPermissions(ProtectionDomain protectionDomain) {
        Permissions permissions = new Permissions();
        if (protectionDomain == null) {
            return permissions;
        }
        getPermissions(permissions, protectionDomain);
        PermissionCollection permissions2 = protectionDomain.getPermissions();
        if (permissions2 != null) {
            synchronized (permissions2) {
                Enumeration<Permission> enumerationElements = permissions2.elements();
                while (enumerationElements.hasMoreElements()) {
                    permissions.add(enumerationElements.nextElement2());
                }
            }
        }
        return permissions;
    }

    @Override // java.security.Policy
    public PermissionCollection getPermissions(CodeSource codeSource) {
        return getPermissions(new Permissions(), codeSource);
    }

    private PermissionCollection getPermissions(Permissions permissions, ProtectionDomain protectionDomain) {
        if (debug != null) {
            debug.println("getPermissions:\n\t" + printPD(protectionDomain));
        }
        final CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            return permissions;
        }
        return getPermissions(permissions, (CodeSource) AccessController.doPrivileged(new PrivilegedAction<CodeSource>() { // from class: sun.security.provider.PolicyFile.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CodeSource run2() {
                return PolicyFile.this.canonicalizeCodebase(codeSource, true);
            }
        }), protectionDomain.getPrincipals());
    }

    private PermissionCollection getPermissions(Permissions permissions, final CodeSource codeSource) {
        if (codeSource == null) {
            return permissions;
        }
        return getPermissions(permissions, (CodeSource) AccessController.doPrivileged(new PrivilegedAction<CodeSource>() { // from class: sun.security.provider.PolicyFile.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CodeSource run2() {
                return PolicyFile.this.canonicalizeCodebase(codeSource, true);
            }
        }), null);
    }

    private Permissions getPermissions(Permissions permissions, CodeSource codeSource, Principal[] principalArr) {
        Certificate[] certificates;
        PolicyInfo policyInfo = this.policyInfo;
        Iterator<PolicyEntry> it = policyInfo.policyEntries.iterator();
        while (it.hasNext()) {
            addPermissions(permissions, codeSource, principalArr, it.next());
        }
        synchronized (policyInfo.identityPolicyEntries) {
            Iterator<PolicyEntry> it2 = policyInfo.identityPolicyEntries.iterator();
            while (it2.hasNext()) {
                addPermissions(permissions, codeSource, principalArr, it2.next());
            }
        }
        if (!this.ignoreIdentityScope && (certificates = codeSource.getCertificates()) != null) {
            for (int i2 = 0; i2 < certificates.length; i2++) {
                if (policyInfo.aliasMapping.get(certificates[i2]) == null && checkForTrustedIdentity(certificates[i2], policyInfo)) {
                    permissions.add(SecurityConstants.ALL_PERMISSION);
                }
            }
        }
        return permissions;
    }

    private void addPermissions(Permissions permissions, final CodeSource codeSource, Principal[] principalArr, final PolicyEntry policyEntry) {
        if (debug != null) {
            debug.println("evaluate codesources:\n\tPolicy CodeSource: " + ((Object) policyEntry.getCodeSource()) + "\n\tActive CodeSource: " + ((Object) codeSource));
        }
        if (!((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.provider.PolicyFile.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return new Boolean(policyEntry.getCodeSource().implies(codeSource));
            }
        })).booleanValue()) {
            if (debug != null) {
                debug.println("evaluation (codesource) failed");
                return;
            }
            return;
        }
        List<PolicyParser.PrincipalEntry> principals = policyEntry.getPrincipals();
        if (debug != null) {
            ArrayList arrayList = new ArrayList();
            if (principalArr != null) {
                for (int i2 = 0; i2 < principalArr.length; i2++) {
                    arrayList.add(new PolicyParser.PrincipalEntry(principalArr[i2].getClass().getName(), principalArr[i2].getName()));
                }
            }
            debug.println("evaluate principals:\n\tPolicy Principals: " + ((Object) principals) + "\n\tActive Principals: " + ((Object) arrayList));
        }
        if (principals == null || principals.isEmpty()) {
            addPerms(permissions, principalArr, policyEntry);
            if (debug != null) {
                debug.println("evaluation (codesource/principals) passed");
                return;
            }
            return;
        }
        if (principalArr == null || principalArr.length == 0) {
            if (debug != null) {
                debug.println("evaluation (principals) failed");
                return;
            }
            return;
        }
        for (PolicyParser.PrincipalEntry principalEntry : principals) {
            if (!principalEntry.isWildcardClass()) {
                if (principalEntry.isWildcardName()) {
                    if (!wildcardPrincipalNameImplies(principalEntry.principalClass, principalArr)) {
                        if (debug != null) {
                            debug.println("evaluation (principal name wildcard) failed");
                            return;
                        }
                        return;
                    }
                } else {
                    Subject subject = new Subject(true, new HashSet(Arrays.asList(principalArr)), Collections.EMPTY_SET, Collections.EMPTY_SET);
                    try {
                        Class<?> cls = Class.forName(principalEntry.principalClass, false, Thread.currentThread().getContextClassLoader());
                        if (!Principal.class.isAssignableFrom(cls)) {
                            throw new ClassCastException(principalEntry.principalClass + " is not a Principal");
                        }
                        Principal principal = (Principal) cls.getConstructor(PARAMS1).newInstance(principalEntry.principalName);
                        if (debug != null) {
                            debug.println("found Principal " + principal.getClass().getName());
                        }
                        if (!principal.implies(subject)) {
                            if (debug != null) {
                                debug.println("evaluation (principal implies) failed");
                                return;
                            }
                            return;
                        }
                    } catch (Exception e2) {
                        if (debug != null) {
                            e2.printStackTrace();
                        }
                        if (!principalEntry.implies(subject)) {
                            if (debug != null) {
                                debug.println("evaluation (default principal implies) failed");
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        }
        if (debug != null) {
            debug.println("evaluation (codesource/principals) passed");
        }
        addPerms(permissions, principalArr, policyEntry);
    }

    private static boolean wildcardPrincipalNameImplies(String str, Principal[] principalArr) {
        for (Principal principal : principalArr) {
            if (str.equals(principal.getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    private void addPerms(Permissions permissions, Principal[] principalArr, PolicyEntry policyEntry) {
        for (int i2 = 0; i2 < policyEntry.permissions.size(); i2++) {
            Permission permission = policyEntry.permissions.get(i2);
            if (debug != null) {
                debug.println("  granting " + ((Object) permission));
            }
            if (permission instanceof SelfPermission) {
                expandSelf((SelfPermission) permission, policyEntry.getPrincipals(), principalArr, permissions);
            } else {
                permissions.add(permission);
            }
        }
    }

    private void expandSelf(SelfPermission selfPermission, List<PolicyParser.PrincipalEntry> list, Principal[] principalArr, Permissions permissions) {
        if (list == null || list.isEmpty()) {
            if (debug != null) {
                debug.println("Ignoring permission " + selfPermission.getSelfType() + " with target name (" + selfPermission.getSelfName() + ").  No Principal(s) specified in the grant clause.  SELF-based target names are only valid in the context of a Principal-based grant entry.");
                return;
            }
            return;
        }
        int length = 0;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int iIndexOf = selfPermission.getSelfName().indexOf(SELF, length);
            if (iIndexOf == -1) {
                break;
            }
            sb.append(selfPermission.getSelfName().substring(length, iIndexOf));
            Iterator<PolicyParser.PrincipalEntry> it = list.iterator();
            while (it.hasNext()) {
                String[][] principalInfo = getPrincipalInfo(it.next(), principalArr);
                for (int i2 = 0; i2 < principalInfo.length; i2++) {
                    if (i2 != 0) {
                        sb.append(", ");
                    }
                    sb.append(principalInfo[i2][0] + " \"" + principalInfo[i2][1] + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            length = iIndexOf + SELF.length();
        }
        sb.append(selfPermission.getSelfName().substring(length));
        if (debug != null) {
            debug.println("  expanded:\n\t" + selfPermission.getSelfName() + "\n  into:\n\t" + sb.toString());
        }
        try {
            permissions.add(getInstance(selfPermission.getSelfType(), sb.toString(), selfPermission.getSelfActions()));
        } catch (ClassNotFoundException e2) {
            Class<?> cls = null;
            synchronized (permissions) {
                Enumeration<Permission> enumerationElements = permissions.elements();
                while (true) {
                    if (!enumerationElements.hasMoreElements()) {
                        break;
                    }
                    Permission permissionNextElement2 = enumerationElements.nextElement2();
                    if (permissionNextElement2.getClass().getName().equals(selfPermission.getSelfType())) {
                        cls = permissionNextElement2.getClass();
                        break;
                    }
                }
                if (cls == null) {
                    permissions.add(new UnresolvedPermission(selfPermission.getSelfType(), sb.toString(), selfPermission.getSelfActions(), selfPermission.getCerts()));
                    return;
                }
                try {
                    if (selfPermission.getSelfActions() == null) {
                        try {
                            permissions.add((Permission) cls.getConstructor(PARAMS1).newInstance(sb.toString()));
                        } catch (NoSuchMethodException e3) {
                            permissions.add((Permission) cls.getConstructor(PARAMS2).newInstance(sb.toString(), selfPermission.getSelfActions()));
                        }
                    } else {
                        permissions.add((Permission) cls.getConstructor(PARAMS2).newInstance(sb.toString(), selfPermission.getSelfActions()));
                    }
                } catch (Exception e4) {
                    if (debug != null) {
                        debug.println("self entry expansion  instantiation failed: " + e4.toString());
                    }
                }
            }
        } catch (Exception e5) {
            if (debug != null) {
                debug.println(e5.toString());
            }
        }
    }

    private String[][] getPrincipalInfo(PolicyParser.PrincipalEntry principalEntry, Principal[] principalArr) {
        if (!principalEntry.isWildcardClass() && !principalEntry.isWildcardName()) {
            String[][] strArr = new String[1][2];
            strArr[0][0] = principalEntry.principalClass;
            strArr[0][1] = principalEntry.principalName;
            return strArr;
        }
        if (!principalEntry.isWildcardClass() && principalEntry.isWildcardName()) {
            ArrayList<Principal> arrayList = new ArrayList();
            for (int i2 = 0; i2 < principalArr.length; i2++) {
                if (principalEntry.principalClass.equals(principalArr[i2].getClass().getName())) {
                    arrayList.add(principalArr[i2]);
                }
            }
            String[][] strArr2 = new String[arrayList.size()][2];
            int i3 = 0;
            for (Principal principal : arrayList) {
                strArr2[i3][0] = principal.getClass().getName();
                strArr2[i3][1] = principal.getName();
                i3++;
            }
            return strArr2;
        }
        String[][] strArr3 = new String[principalArr.length][2];
        for (int i4 = 0; i4 < principalArr.length; i4++) {
            strArr3[i4][0] = principalArr[i4].getClass().getName();
            strArr3[i4][1] = principalArr[i4].getName();
        }
        return strArr3;
    }

    protected Certificate[] getSignerCertificates(CodeSource codeSource) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public CodeSource canonicalizeCodebase(CodeSource codeSource, boolean z2) {
        String file;
        int iIndexOf;
        String strDecode = null;
        CodeSource codeSource2 = codeSource;
        URL location = codeSource.getLocation();
        if (location != null) {
            if (location.getProtocol().equals("jar") && (iIndexOf = (file = location.getFile()).indexOf("!/")) != -1) {
                try {
                    location = new URL(file.substring(0, iIndexOf));
                } catch (MalformedURLException e2) {
                }
            }
            if (location.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
                String host = location.getHost();
                if (host == null || host.equals("") || host.equals("~") || host.equalsIgnoreCase("localhost")) {
                    strDecode = ParseUtil.decode(location.getFile().replace('/', File.separatorChar));
                }
            }
        }
        if (strDecode != null) {
            try {
                URL urlFileToEncodedURL = ParseUtil.fileToEncodedURL(new File(canonPath(strDecode)));
                if (z2) {
                    codeSource2 = new CodeSource(urlFileToEncodedURL, getSignerCertificates(codeSource));
                } else {
                    codeSource2 = new CodeSource(urlFileToEncodedURL, codeSource.getCertificates());
                }
            } catch (IOException e3) {
                if (z2) {
                    codeSource2 = new CodeSource(codeSource.getLocation(), getSignerCertificates(codeSource));
                }
            }
        } else if (z2) {
            codeSource2 = new CodeSource(codeSource.getLocation(), getSignerCertificates(codeSource));
        }
        return codeSource2;
    }

    private static String canonPath(String str) throws IOException {
        if (str.endsWith("*")) {
            String canonicalPath = new File(str.substring(0, str.length() - 1) + LanguageTag.SEP).getCanonicalPath();
            return canonicalPath.substring(0, canonicalPath.length() - 1) + "*";
        }
        return new File(str).getCanonicalPath();
    }

    private String printPD(ProtectionDomain protectionDomain) {
        Principal[] principals = protectionDomain.getPrincipals();
        String string = "<no principals>";
        if (principals != null && principals.length > 0) {
            StringBuilder sb = new StringBuilder("(principals ");
            for (int i2 = 0; i2 < principals.length; i2++) {
                sb.append(principals[i2].getClass().getName() + " \"" + principals[i2].getName() + PdfOps.DOUBLE_QUOTE__TOKEN);
                if (i2 < principals.length - 1) {
                    sb.append(", ");
                } else {
                    sb.append(")");
                }
            }
            string = sb.toString();
        }
        return "PD CodeSource: " + ((Object) protectionDomain.getCodeSource()) + "\n\tPD ClassLoader: " + ((Object) protectionDomain.getClassLoader()) + "\n\tPD Principals: " + string;
    }

    private boolean replacePrincipals(List<PolicyParser.PrincipalEntry> list, KeyStore keyStore) {
        if (list == null || list.isEmpty() || keyStore == null) {
            return true;
        }
        for (PolicyParser.PrincipalEntry principalEntry : list) {
            if (principalEntry.isReplaceName()) {
                String dn = getDN(principalEntry.principalName, keyStore);
                if (dn == null) {
                    return false;
                }
                if (debug != null) {
                    debug.println("  Replacing \"" + principalEntry.principalName + "\" with javax.security.auth.x500.X500Principal/\"" + dn + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
                principalEntry.principalClass = "javax.security.auth.x500.X500Principal";
                principalEntry.principalName = dn;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0184, code lost:
    
        r0.append(r8.name.substring(r10));
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0195, code lost:
    
        if (sun.security.provider.PolicyFile.debug == null) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0198, code lost:
    
        sun.security.provider.PolicyFile.debug.println("  Permission name expanded from:\n\t" + r8.name + "\nto\n\t" + r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x01c3, code lost:
    
        r8.name = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x01cc, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void expandPermissionName(sun.security.provider.PolicyParser.PermissionEntry r8, java.security.KeyStore r9) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 461
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.PolicyFile.expandPermissionName(sun.security.provider.PolicyParser$PermissionEntry, java.security.KeyStore):void");
    }

    private String getDN(String str, KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(str);
            if (certificate == null || !(certificate instanceof X509Certificate)) {
                if (debug != null) {
                    debug.println("  -- No certificate for '" + str + "' - ignoring entry");
                    return null;
                }
                return null;
            }
            return new X500Principal(((X509Certificate) certificate).getSubjectX500Principal().toString()).getName();
        } catch (Exception e2) {
            if (debug != null) {
                debug.println("  Error retrieving certificate for '" + str + "': " + e2.toString());
                return null;
            }
            return null;
        }
    }

    private boolean checkForTrustedIdentity(Certificate certificate, PolicyInfo policyInfo) {
        return false;
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyFile$PolicyEntry.class */
    private static class PolicyEntry {
        private final CodeSource codesource;
        final List<Permission> permissions;
        private final List<PolicyParser.PrincipalEntry> principals;

        PolicyEntry(CodeSource codeSource, List<PolicyParser.PrincipalEntry> list) {
            this.codesource = codeSource;
            this.permissions = new ArrayList();
            this.principals = list;
        }

        PolicyEntry(CodeSource codeSource) {
            this(codeSource, null);
        }

        List<PolicyParser.PrincipalEntry> getPrincipals() {
            return this.principals;
        }

        void add(Permission permission) {
            this.permissions.add(permission);
        }

        CodeSource getCodeSource() {
            return this.codesource;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(ResourcesMgr.getString("LPARAM"));
            sb.append((Object) getCodeSource());
            sb.append("\n");
            for (int i2 = 0; i2 < this.permissions.size(); i2++) {
                Permission permission = this.permissions.get(i2);
                sb.append(ResourcesMgr.getString("SPACE"));
                sb.append(ResourcesMgr.getString("SPACE"));
                sb.append((Object) permission);
                sb.append(ResourcesMgr.getString("NEWLINE"));
            }
            sb.append(ResourcesMgr.getString("RPARAM"));
            sb.append(ResourcesMgr.getString("NEWLINE"));
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyFile$SelfPermission.class */
    private static class SelfPermission extends Permission {
        private static final long serialVersionUID = -8315562579967246806L;
        private String type;
        private String name;
        private String actions;
        private Certificate[] certs;

        public SelfPermission(String str, String str2, String str3, Certificate[] certificateArr) {
            super(str);
            if (str == null) {
                throw new NullPointerException(ResourcesMgr.getString("type.can.t.be.null"));
            }
            this.type = str;
            this.name = str2;
            this.actions = str3;
            if (certificateArr != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= certificateArr.length) {
                        break;
                    }
                    if (certificateArr[i2] instanceof X509Certificate) {
                        i2++;
                    } else {
                        this.certs = (Certificate[]) certificateArr.clone();
                        break;
                    }
                }
                if (this.certs == null) {
                    int i3 = 0;
                    int i4 = 0;
                    while (i3 < certificateArr.length) {
                        i4++;
                        while (i3 + 1 < certificateArr.length && ((X509Certificate) certificateArr[i3]).getIssuerDN().equals(((X509Certificate) certificateArr[i3 + 1]).getSubjectDN())) {
                            i3++;
                        }
                        i3++;
                    }
                    if (i4 == certificateArr.length) {
                        this.certs = (Certificate[]) certificateArr.clone();
                    }
                    if (this.certs == null) {
                        ArrayList arrayList = new ArrayList();
                        int i5 = 0;
                        while (i5 < certificateArr.length) {
                            arrayList.add(certificateArr[i5]);
                            while (i5 + 1 < certificateArr.length && ((X509Certificate) certificateArr[i5]).getIssuerDN().equals(((X509Certificate) certificateArr[i5 + 1]).getSubjectDN())) {
                                i5++;
                            }
                            i5++;
                        }
                        this.certs = new Certificate[arrayList.size()];
                        arrayList.toArray(this.certs);
                    }
                }
            }
        }

        @Override // java.security.Permission
        public boolean implies(Permission permission) {
            return false;
        }

        @Override // java.security.Permission
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SelfPermission)) {
                return false;
            }
            SelfPermission selfPermission = (SelfPermission) obj;
            if (!this.type.equals(selfPermission.type) || !this.name.equals(selfPermission.name) || !this.actions.equals(selfPermission.actions) || this.certs.length != selfPermission.certs.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.certs.length; i2++) {
                boolean z2 = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= selfPermission.certs.length) {
                        break;
                    }
                    if (!this.certs[i2].equals(selfPermission.certs[i3])) {
                        i3++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    return false;
                }
            }
            for (int i4 = 0; i4 < selfPermission.certs.length; i4++) {
                boolean z3 = false;
                int i5 = 0;
                while (true) {
                    if (i5 >= this.certs.length) {
                        break;
                    }
                    if (!selfPermission.certs[i4].equals(this.certs[i5])) {
                        i5++;
                    } else {
                        z3 = true;
                        break;
                    }
                }
                if (!z3) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.security.Permission
        public int hashCode() {
            int iHashCode = this.type.hashCode();
            if (this.name != null) {
                iHashCode ^= this.name.hashCode();
            }
            if (this.actions != null) {
                iHashCode ^= this.actions.hashCode();
            }
            return iHashCode;
        }

        @Override // java.security.Permission
        public String getActions() {
            return "";
        }

        public String getSelfType() {
            return this.type;
        }

        public String getSelfName() {
            return this.name;
        }

        public String getSelfActions() {
            return this.actions;
        }

        public Certificate[] getCerts() {
            return this.certs;
        }

        @Override // java.security.Permission
        public String toString() {
            return "(SelfPermission " + this.type + " " + this.name + " " + this.actions + ")";
        }
    }

    /* loaded from: rt.jar:sun/security/provider/PolicyFile$PolicyInfo.class */
    private static class PolicyInfo {
        private static final boolean verbose = false;
        final List<PolicyEntry> policyEntries = new ArrayList();
        final List<PolicyEntry> identityPolicyEntries = Collections.synchronizedList(new ArrayList(2));
        final Map<Object, Object> aliasMapping = Collections.synchronizedMap(new HashMap(11));
        private final JavaSecurityProtectionDomainAccess.ProtectionDomainCache[] pdMapping;
        private Random random;

        PolicyInfo(int i2) {
            this.pdMapping = new JavaSecurityProtectionDomainAccess.ProtectionDomainCache[i2];
            JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess = SharedSecrets.getJavaSecurityProtectionDomainAccess();
            for (int i3 = 0; i3 < i2; i3++) {
                this.pdMapping[i3] = javaSecurityProtectionDomainAccess.getProtectionDomainCache();
            }
            if (i2 > 1) {
                this.random = new Random();
            }
        }

        JavaSecurityProtectionDomainAccess.ProtectionDomainCache getPdMapping() {
            if (this.pdMapping.length == 1) {
                return this.pdMapping[0];
            }
            return this.pdMapping[Math.abs(this.random.nextInt() % this.pdMapping.length)];
        }
    }
}
