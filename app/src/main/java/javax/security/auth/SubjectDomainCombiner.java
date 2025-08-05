package javax.security.auth;

import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import javafx.fxml.FXMLLoader;
import sun.misc.JavaSecurityProtectionDomainAccess;
import sun.misc.SharedSecrets;
import sun.security.util.Debug;

/* loaded from: rt.jar:javax/security/auth/SubjectDomainCombiner.class */
public class SubjectDomainCombiner implements DomainCombiner {
    private Subject subject;
    private WeakKeyValueMap<ProtectionDomain, ProtectionDomain> cachedPDs = new WeakKeyValueMap<>();
    private Set<Principal> principalSet;
    private Principal[] principals;
    private static final Debug debug = Debug.getInstance("combiner", "\t[SubjectDomainCombiner]");
    private static final boolean useJavaxPolicy = Policy.isCustomPolicySet(debug);
    private static final boolean allowCaching;
    private static final JavaSecurityProtectionDomainAccess pdAccess;

    static {
        allowCaching = useJavaxPolicy && cachePolicy();
        pdAccess = SharedSecrets.getJavaSecurityProtectionDomainAccess();
    }

    public SubjectDomainCombiner(Subject subject) {
        this.subject = subject;
        if (subject.isReadOnly()) {
            this.principalSet = subject.getPrincipals();
            this.principals = (Principal[]) this.principalSet.toArray(new Principal[this.principalSet.size()]);
        }
    }

    public Subject getSubject() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AuthPermission("getSubjectFromDomainCombiner"));
        }
        return this.subject;
    }

    @Override // java.security.DomainCombiner
    public ProtectionDomain[] combine(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        if (debug != null) {
            if (this.subject == null) {
                debug.println("null subject");
            } else {
                final Subject subject = this.subject;
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.security.auth.SubjectDomainCombiner.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        SubjectDomainCombiner.debug.println(subject.toString());
                        return null;
                    }
                });
            }
            printInputDomains(protectionDomainArr, protectionDomainArr2);
        }
        if (protectionDomainArr == null || protectionDomainArr.length == 0) {
            return protectionDomainArr2;
        }
        ProtectionDomain[] protectionDomainArrOptimize = optimize(protectionDomainArr);
        if (debug != null) {
            debug.println("after optimize");
            printInputDomains(protectionDomainArrOptimize, protectionDomainArr2);
        }
        if (protectionDomainArrOptimize == null && protectionDomainArr2 == null) {
            return null;
        }
        if (useJavaxPolicy) {
            return combineJavaxPolicy(protectionDomainArrOptimize, protectionDomainArr2);
        }
        int length = protectionDomainArrOptimize == null ? 0 : protectionDomainArrOptimize.length;
        int length2 = protectionDomainArr2 == null ? 0 : protectionDomainArr2.length;
        ProtectionDomain[] protectionDomainArrOptimize2 = new ProtectionDomain[length + length2];
        boolean z2 = true;
        synchronized (this.cachedPDs) {
            if (!this.subject.isReadOnly() && !this.subject.getPrincipals().equals(this.principalSet)) {
                Set<Principal> principals = this.subject.getPrincipals();
                synchronized (principals) {
                    this.principalSet = new HashSet(principals);
                }
                this.principals = (Principal[]) this.principalSet.toArray(new Principal[this.principalSet.size()]);
                this.cachedPDs.clear();
                if (debug != null) {
                    debug.println("Subject mutated - clearing cache");
                }
            }
            for (int i2 = 0; i2 < length; i2++) {
                ProtectionDomain protectionDomain = protectionDomainArrOptimize[i2];
                ProtectionDomain value = this.cachedPDs.getValue(protectionDomain);
                if (value == null) {
                    if (pdAccess.getStaticPermissionsField(protectionDomain)) {
                        value = new ProtectionDomain(protectionDomain.getCodeSource(), protectionDomain.getPermissions());
                    } else {
                        value = new ProtectionDomain(protectionDomain.getCodeSource(), protectionDomain.getPermissions(), protectionDomain.getClassLoader(), this.principals);
                    }
                    this.cachedPDs.putValue(protectionDomain, value);
                } else {
                    z2 = false;
                }
                protectionDomainArrOptimize2[i2] = value;
            }
        }
        if (debug != null) {
            debug.println("updated current: ");
            for (int i3 = 0; i3 < length; i3++) {
                debug.println("\tupdated[" + i3 + "] = " + printDomain(protectionDomainArrOptimize2[i3]));
            }
        }
        if (length2 > 0) {
            System.arraycopy(protectionDomainArr2, 0, protectionDomainArrOptimize2, length, length2);
            if (!z2) {
                protectionDomainArrOptimize2 = optimize(protectionDomainArrOptimize2);
            }
        }
        if (debug != null) {
            if (protectionDomainArrOptimize2 == null || protectionDomainArrOptimize2.length == 0) {
                debug.println("returning null");
            } else {
                debug.println("combinedDomains: ");
                for (int i4 = 0; i4 < protectionDomainArrOptimize2.length; i4++) {
                    debug.println("newDomain " + i4 + ": " + printDomain(protectionDomainArrOptimize2[i4]));
                }
            }
        }
        if (protectionDomainArrOptimize2 == null || protectionDomainArrOptimize2.length == 0) {
            return null;
        }
        return protectionDomainArrOptimize2;
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x016e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00bb A[Catch: all -> 0x020d, TryCatch #2 {, blocks: (B:15:0x003a, B:17:0x0044, B:19:0x0057, B:20:0x0065, B:21:0x0066, B:22:0x0075, B:29:0x0081, B:31:0x00aa, B:26:0x007d, B:28:0x0080, B:35:0x00bb, B:37:0x00d4, B:39:0x00e1, B:76:0x01e8, B:78:0x01ee, B:40:0x00f7, B:43:0x0111, B:44:0x0112, B:45:0x0119, B:47:0x0123, B:49:0x013b, B:53:0x0143, B:55:0x0146, B:56:0x0147, B:57:0x016d, B:58:0x016e, B:59:0x0175, B:61:0x017f, B:63:0x0195, B:65:0x01a2, B:68:0x01c6, B:75:0x01d2, B:72:0x01ce, B:74:0x01d1, B:79:0x01fa, B:81:0x0209), top: B:120:0x003a, inners: #0, #1, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.security.ProtectionDomain[] combineJavaxPolicy(java.security.ProtectionDomain[] r8, java.security.ProtectionDomain[] r9) {
        /*
            Method dump skipped, instructions count: 727
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.security.auth.SubjectDomainCombiner.combineJavaxPolicy(java.security.ProtectionDomain[], java.security.ProtectionDomain[]):java.security.ProtectionDomain[]");
    }

    private static ProtectionDomain[] optimize(ProtectionDomain[] protectionDomainArr) {
        if (protectionDomainArr == null || protectionDomainArr.length == 0) {
            return null;
        }
        ProtectionDomain[] protectionDomainArr2 = new ProtectionDomain[protectionDomainArr.length];
        int i2 = 0;
        for (int i3 = 0; i3 < protectionDomainArr.length; i3++) {
            ProtectionDomain protectionDomain = protectionDomainArr[i3];
            if (protectionDomain != null) {
                boolean z2 = false;
                for (int i4 = 0; i4 < i2 && !z2; i4++) {
                    z2 = protectionDomainArr2[i4] == protectionDomain;
                }
                if (!z2) {
                    int i5 = i2;
                    i2++;
                    protectionDomainArr2[i5] = protectionDomain;
                }
            }
        }
        if (i2 > 0 && i2 < protectionDomainArr.length) {
            ProtectionDomain[] protectionDomainArr3 = new ProtectionDomain[i2];
            System.arraycopy(protectionDomainArr2, 0, protectionDomainArr3, 0, protectionDomainArr3.length);
            protectionDomainArr2 = protectionDomainArr3;
        }
        if (i2 == 0 || protectionDomainArr2.length == 0) {
            return null;
        }
        return protectionDomainArr2;
    }

    private static boolean cachePolicy() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.auth.SubjectDomainCombiner.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("cache.auth.policy");
            }
        });
        if (str != null) {
            return Boolean.parseBoolean(str);
        }
        return true;
    }

    private static void printInputDomains(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        if (protectionDomainArr == null || protectionDomainArr.length == 0) {
            debug.println("currentDomains null or 0 length");
        } else {
            for (int i2 = 0; protectionDomainArr != null && i2 < protectionDomainArr.length; i2++) {
                if (protectionDomainArr[i2] == null) {
                    debug.println("currentDomain " + i2 + ": SystemDomain");
                } else {
                    debug.println("currentDomain " + i2 + ": " + printDomain(protectionDomainArr[i2]));
                }
            }
        }
        if (protectionDomainArr2 == null || protectionDomainArr2.length == 0) {
            debug.println("assignedDomains null or 0 length");
            return;
        }
        debug.println("assignedDomains = ");
        for (int i3 = 0; protectionDomainArr2 != null && i3 < protectionDomainArr2.length; i3++) {
            if (protectionDomainArr2[i3] == null) {
                debug.println("assignedDomain " + i3 + ": SystemDomain");
            } else {
                debug.println("assignedDomain " + i3 + ": " + printDomain(protectionDomainArr2[i3]));
            }
        }
    }

    private static String printDomain(final ProtectionDomain protectionDomain) {
        if (protectionDomain == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.security.auth.SubjectDomainCombiner.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return protectionDomain.toString();
            }
        });
    }

    /* loaded from: rt.jar:javax/security/auth/SubjectDomainCombiner$WeakKeyValueMap.class */
    private static class WeakKeyValueMap<K, V> extends WeakHashMap<K, WeakReference<V>> {
        private WeakKeyValueMap() {
        }

        public V getValue(K k2) {
            WeakReference weakReference = (WeakReference) super.get(k2);
            if (weakReference != null) {
                return (V) weakReference.get();
            }
            return null;
        }

        public V putValue(K k2, V v2) {
            WeakReference weakReference = (WeakReference) super.put(k2, new WeakReference(v2));
            if (weakReference != null) {
                return (V) weakReference.get();
            }
            return null;
        }
    }
}
