package javax.crypto;

import java.net.URL;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: jce.jar:javax/crypto/JceSecurityManager.class */
final class JceSecurityManager extends SecurityManager {
    private static final Vector<Class<?>> TrustedCallersCache = new Vector<>(2);
    private static final ConcurrentMap<URL, CryptoPermissions> exemptCache = new ConcurrentHashMap();
    private static final CryptoPermissions CACHE_NULL_MARK = new CryptoPermissions();
    private static final CryptoPermissions defaultPolicy = JceSecurity.getDefaultPolicy();
    private static final CryptoPermissions exemptPolicy = JceSecurity.getExemptPolicy();
    private static final CryptoAllPermission allPerm = CryptoAllPermission.INSTANCE;
    static final JceSecurityManager INSTANCE = (JceSecurityManager) AccessController.doPrivileged(new PrivilegedAction<JceSecurityManager>() { // from class: javax.crypto.JceSecurityManager.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public JceSecurityManager run2() {
            return new JceSecurityManager();
        }
    });

    private JceSecurityManager() {
    }

    CryptoPermission getCryptoPermission(String str) {
        CryptoPermission cryptoPermission;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        CryptoPermission defaultPermission = getDefaultPermission(upperCase);
        if (defaultPermission == CryptoAllPermission.INSTANCE) {
            return defaultPermission;
        }
        Class[] classContext = getClassContext();
        URL codeBase = null;
        int i2 = 0;
        while (i2 < classContext.length) {
            Class cls = classContext[i2];
            codeBase = JceSecurity.getCodeBase(cls);
            if (codeBase != null) {
                break;
            }
            if (cls.getName().startsWith("javax.crypto.")) {
                i2++;
            } else {
                return defaultPermission;
            }
        }
        if (i2 == classContext.length) {
            return defaultPermission;
        }
        CryptoPermissions appPermissions = exemptCache.get(codeBase);
        if (appPermissions == null) {
            synchronized (getClass()) {
                appPermissions = exemptCache.get(codeBase);
                if (appPermissions == null) {
                    appPermissions = getAppPermissions(codeBase);
                    exemptCache.putIfAbsent(codeBase, appPermissions == null ? CACHE_NULL_MARK : appPermissions);
                }
            }
        }
        if (appPermissions == null || appPermissions == CACHE_NULL_MARK) {
            return defaultPermission;
        }
        if (appPermissions.implies(allPerm)) {
            return allPerm;
        }
        PermissionCollection permissionCollection = appPermissions.getPermissionCollection(upperCase);
        if (permissionCollection == null) {
            return defaultPermission;
        }
        Enumeration<Permission> enumerationElements = permissionCollection.elements();
        while (enumerationElements.hasMoreElements()) {
            CryptoPermission cryptoPermission2 = (CryptoPermission) enumerationElements.nextElement2();
            if (cryptoPermission2.getExemptionMechanism() == null) {
                return cryptoPermission2;
            }
        }
        PermissionCollection permissionCollection2 = exemptPolicy.getPermissionCollection(upperCase);
        if (permissionCollection2 == null) {
            return defaultPermission;
        }
        Enumeration<Permission> enumerationElements2 = permissionCollection2.elements();
        while (enumerationElements2.hasMoreElements()) {
            CryptoPermission cryptoPermission3 = (CryptoPermission) enumerationElements2.nextElement2();
            try {
                ExemptionMechanism.getInstance(cryptoPermission3.getExemptionMechanism());
                if (cryptoPermission3.getAlgorithm().equals("*")) {
                    if (cryptoPermission3.getCheckParam()) {
                        cryptoPermission = new CryptoPermission(upperCase, cryptoPermission3.getMaxKeySize(), cryptoPermission3.getAlgorithmParameterSpec(), cryptoPermission3.getExemptionMechanism());
                    } else {
                        cryptoPermission = new CryptoPermission(upperCase, cryptoPermission3.getMaxKeySize(), cryptoPermission3.getExemptionMechanism());
                    }
                    if (appPermissions.implies(cryptoPermission)) {
                        return cryptoPermission;
                    }
                }
            } catch (Exception e2) {
            }
            if (appPermissions.implies(cryptoPermission3)) {
                return cryptoPermission3;
            }
        }
        return defaultPermission;
    }

    private static CryptoPermissions getAppPermissions(URL url) {
        try {
            return JceSecurity.verifyExemptJar(url);
        } catch (Exception e2) {
            return null;
        }
    }

    private CryptoPermission getDefaultPermission(String str) {
        return (CryptoPermission) defaultPolicy.getPermissionCollection(str).elements().nextElement2();
    }

    boolean isCallerTrusted() {
        Class<?>[] classContext = getClassContext();
        URL codeBase = null;
        int i2 = 0;
        while (i2 < classContext.length) {
            codeBase = JceSecurity.getCodeBase(classContext[i2]);
            if (codeBase != null) {
                break;
            }
            i2++;
        }
        if (i2 == classContext.length || TrustedCallersCache.contains(classContext[i2])) {
            return true;
        }
        try {
            JceSecurity.verifyProviderJar(codeBase);
            TrustedCallersCache.addElement(classContext[i2]);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }
}
