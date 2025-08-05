package javax.crypto;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import sun.security.jca.GetInstance;
import sun.security.util.Debug;

/* loaded from: jce.jar:javax/crypto/JceSecurity.class */
final class JceSecurity {
    private static final boolean isRestricted;
    private static final Object PROVIDER_VERIFIED;
    private static final URL NULL_URL;
    private static final Map<Class<?>, URL> codeBaseCacheRef;
    static final SecureRandom RANDOM = new SecureRandom();
    private static CryptoPermissions defaultPolicy = null;
    private static CryptoPermissions exemptPolicy = null;
    private static final Map<Provider, Object> verificationResults = new IdentityHashMap();
    private static final Map<Provider, Object> verifyingProviders = new IdentityHashMap();
    private static final Debug debug = Debug.getInstance("jca", "Cipher");

    static {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: javax.crypto.JceSecurity.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    JceSecurity.setupJurisdictionPolicies();
                    return null;
                }
            });
            isRestricted = !defaultPolicy.implies(CryptoAllPermission.INSTANCE);
            PROVIDER_VERIFIED = Boolean.TRUE;
            try {
                NULL_URL = new URL("http://null.oracle.com/");
                codeBaseCacheRef = new WeakHashMap();
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } catch (Exception e3) {
            throw new SecurityException("Can not initialize cryptographic mechanism", e3);
        }
    }

    private JceSecurity() {
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        Provider.Service service = GetInstance.getService(str, str2, str3);
        Exception verificationResult = getVerificationResult(service.getProvider());
        if (verificationResult != null) {
            throw ((NoSuchProviderException) new NoSuchProviderException("JCE cannot authenticate the provider " + str3).initCause(verificationResult));
        }
        return GetInstance.getInstance(service, cls);
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2, Provider provider) throws NoSuchAlgorithmException {
        Provider.Service service = GetInstance.getService(str, str2, provider);
        Exception verificationResult = getVerificationResult(provider);
        if (verificationResult != null) {
            throw new SecurityException("JCE cannot authenticate the provider " + provider.getName(), verificationResult);
        }
        return GetInstance.getInstance(service, cls);
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2) throws NoSuchAlgorithmException {
        NoSuchAlgorithmException noSuchAlgorithmException = null;
        for (Provider.Service service : GetInstance.getServices(str, str2)) {
            if (canUseProvider(service.getProvider())) {
                try {
                    return GetInstance.getInstance(service, cls);
                } catch (NoSuchAlgorithmException e2) {
                    noSuchAlgorithmException = e2;
                }
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str2 + " not available", noSuchAlgorithmException);
    }

    static CryptoPermissions verifyExemptJar(URL url) throws Exception {
        JarVerifier jarVerifier = new JarVerifier(url, true);
        jarVerifier.verify();
        return jarVerifier.getPermissions();
    }

    static void verifyProviderJar(URL url) throws Exception {
        new JarVerifier(url, false).verify();
    }

    static synchronized Exception getVerificationResult(Provider provider) {
        Object obj = verificationResults.get(provider);
        if (obj == PROVIDER_VERIFIED) {
            return null;
        }
        if (obj != null) {
            return (Exception) obj;
        }
        try {
            if (verifyingProviders.get(provider) != null) {
                return new NoSuchProviderException("Recursion during verification");
            }
            try {
                verifyingProviders.put(provider, Boolean.FALSE);
                verifyProviderJar(getCodeBase(provider.getClass()));
                verificationResults.put(provider, PROVIDER_VERIFIED);
                verifyingProviders.remove(provider);
                return null;
            } catch (Exception e2) {
                verificationResults.put(provider, e2);
                verifyingProviders.remove(provider);
                return e2;
            }
        } catch (Throwable th) {
            verifyingProviders.remove(provider);
            throw th;
        }
    }

    static boolean canUseProvider(Provider provider) {
        return getVerificationResult(provider) == null;
    }

    static URL getCodeBase(final Class<?> cls) {
        URL url;
        synchronized (codeBaseCacheRef) {
            URL url2 = codeBaseCacheRef.get(cls);
            if (url2 == null) {
                url2 = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() { // from class: javax.crypto.JceSecurity.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public URL run2() {
                        CodeSource codeSource;
                        ProtectionDomain protectionDomain = cls.getProtectionDomain();
                        if (protectionDomain == null || (codeSource = protectionDomain.getCodeSource()) == null) {
                            return JceSecurity.NULL_URL;
                        }
                        return codeSource.getLocation();
                    }
                });
                codeBaseCacheRef.put(cls, url2);
            }
            url = url2 == NULL_URL ? null : url2;
        }
        return url;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setupJurisdictionPolicies() throws Exception {
        Path path;
        String property = System.getProperty("java.home");
        String property2 = Security.getProperty("crypto.policy");
        Path path2 = property2 == null ? null : Paths.get(property2, new String[0]);
        if (path2 != null && (path2.getNameCount() != 1 || path2.compareTo(path2.getFileName()) != 0)) {
            throw new SecurityException("Invalid policy directory name format: " + property2);
        }
        if (path2 == null) {
            path = Paths.get(property, "lib", "security");
        } else {
            path = Paths.get(property, "lib", "security", "policy", property2);
        }
        if (debug != null) {
            debug.println("crypto policy directory: " + ((Object) path));
        }
        File file = new File(path.toFile(), "US_export_policy.jar");
        File file2 = new File(path.toFile(), "local_policy.jar");
        if (property2 == null && (!file.exists() || !file2.exists())) {
            Path path3 = Paths.get(property, "lib", "security", "policy", "unlimited");
            file = new File(path3.toFile(), "US_export_policy.jar");
            file2 = new File(path3.toFile(), "local_policy.jar");
        }
        if (ClassLoader.getSystemResource("javax/crypto/Cipher.class") == null || !file.exists() || !file2.exists()) {
            throw new SecurityException("Cannot locate policy or framework files!");
        }
        CryptoPermissions cryptoPermissions = new CryptoPermissions();
        CryptoPermissions cryptoPermissions2 = new CryptoPermissions();
        loadPolicies(file, cryptoPermissions, cryptoPermissions2);
        CryptoPermissions cryptoPermissions3 = new CryptoPermissions();
        CryptoPermissions cryptoPermissions4 = new CryptoPermissions();
        loadPolicies(file2, cryptoPermissions3, cryptoPermissions4);
        if (cryptoPermissions.isEmpty() || cryptoPermissions3.isEmpty()) {
            throw new SecurityException("Missing mandatory jurisdiction policy files");
        }
        defaultPolicy = cryptoPermissions.getMinimum(cryptoPermissions3);
        if (cryptoPermissions2.isEmpty()) {
            exemptPolicy = cryptoPermissions4.isEmpty() ? null : cryptoPermissions4;
        } else {
            exemptPolicy = cryptoPermissions2.getMinimum(cryptoPermissions4);
        }
    }

    private static void loadPolicies(File file, CryptoPermissions cryptoPermissions, CryptoPermissions cryptoPermissions2) throws Exception {
        InputStream inputStream;
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> enumerationEntries = jarFile.entries();
        while (enumerationEntries.hasMoreElements()) {
            JarEntry jarEntryNextElement = enumerationEntries.nextElement2();
            AutoCloseable autoCloseable = null;
            try {
                if (jarEntryNextElement.getName().startsWith("default_")) {
                    inputStream = jarFile.getInputStream(jarEntryNextElement);
                    cryptoPermissions.load(inputStream);
                } else if (jarEntryNextElement.getName().startsWith("exempt_")) {
                    inputStream = jarFile.getInputStream(jarEntryNextElement);
                    cryptoPermissions2.load(inputStream);
                } else if (0 != 0) {
                    autoCloseable.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                JarVerifier.verifyPolicySigned(jarEntryNextElement.getCertificates());
            } catch (Throwable th) {
                if (0 != 0) {
                    autoCloseable.close();
                }
                throw th;
            }
        }
        jarFile.close();
    }

    static CryptoPermissions getDefaultPolicy() {
        return defaultPolicy;
    }

    static CryptoPermissions getExemptPolicy() {
        return exemptPolicy;
    }

    static boolean isRestricted() {
        return isRestricted;
    }
}
