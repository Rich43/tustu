package sun.security.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import sun.security.action.GetPropertyAction;
import sun.security.action.OpenFileInputStreamAction;
import sun.security.validator.TrustStoreUtil;

/* loaded from: jsse.jar:sun/security/ssl/TrustStoreManager.class */
final class TrustStoreManager {
    private static final TrustAnchorManager tam = new TrustAnchorManager();

    private TrustStoreManager() {
    }

    public static Set<X509Certificate> getTrustedCerts() throws Exception {
        return tam.getTrustedCerts(TrustStoreDescriptor.createInstance());
    }

    public static KeyStore getTrustedKeyStore() throws Exception {
        return tam.getKeyStore(TrustStoreDescriptor.createInstance());
    }

    /* loaded from: jsse.jar:sun/security/ssl/TrustStoreManager$TrustStoreDescriptor.class */
    private static final class TrustStoreDescriptor {
        private static final String fileSep = File.separator;
        private static final String defaultStorePath = GetPropertyAction.privilegedGetProperty("java.home") + fileSep + "lib" + fileSep + "security";
        private static final String defaultStore = defaultStorePath + fileSep + "cacerts";
        private static final String jsseDefaultStore = defaultStorePath + fileSep + "jssecacerts";
        private final String storeName;
        private final String storeType;
        private final String storeProvider;
        private final String storePassword;
        private final File storeFile;
        private final long lastModified;

        private TrustStoreDescriptor(String str, String str2, String str3, String str4, File file, long j2) {
            this.storeName = str;
            this.storeType = str2;
            this.storeProvider = str3;
            this.storePassword = str4;
            this.storeFile = file;
            this.lastModified = j2;
            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                SSLLogger.fine("trustStore is: " + str + "\ntrustStore type is: " + str2 + "\ntrustStore provider is: " + str3 + "\nthe last modified time is: " + ((Object) new Date(j2)), new Object[0]);
            }
        }

        static TrustStoreDescriptor createInstance() {
            return (TrustStoreDescriptor) AccessController.doPrivileged(new PrivilegedAction<TrustStoreDescriptor>() { // from class: sun.security.ssl.TrustStoreManager.TrustStoreDescriptor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public TrustStoreDescriptor run2() {
                    String property = System.getProperty("javax.net.ssl.trustStore", TrustStoreDescriptor.jsseDefaultStore);
                    String property2 = System.getProperty("javax.net.ssl.trustStoreType", KeyStore.getDefaultType());
                    String property3 = System.getProperty("javax.net.ssl.trustStoreProvider", "");
                    String property4 = System.getProperty("javax.net.ssl.trustStorePassword", "");
                    String str = "";
                    File file = null;
                    long jLastModified = 0;
                    if (!"NONE".equals(property)) {
                        String[] strArr = {property, TrustStoreDescriptor.defaultStore};
                        int length = strArr.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                break;
                            }
                            String str2 = strArr[i2];
                            File file2 = new File(str2);
                            if (file2.isFile() && file2.canRead()) {
                                str = str2;
                                file = file2;
                                jLastModified = file2.lastModified();
                                break;
                            }
                            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                                SSLLogger.fine("Inaccessible trust store: " + property, new Object[0]);
                            }
                            i2++;
                        }
                    } else {
                        str = property;
                    }
                    return new TrustStoreDescriptor(str, property2, property3, property4, file, jLastModified);
                }
            });
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof TrustStoreDescriptor) {
                TrustStoreDescriptor trustStoreDescriptor = (TrustStoreDescriptor) obj;
                return this.lastModified == trustStoreDescriptor.lastModified && Objects.equals(this.storeName, trustStoreDescriptor.storeName) && Objects.equals(this.storeType, trustStoreDescriptor.storeType) && Objects.equals(this.storeProvider, trustStoreDescriptor.storeProvider);
            }
            return false;
        }

        public int hashCode() {
            int iHashCode = 17;
            if (this.storeName != null && !this.storeName.isEmpty()) {
                iHashCode = (31 * 17) + this.storeName.hashCode();
            }
            if (this.storeType != null && !this.storeType.isEmpty()) {
                iHashCode = (31 * iHashCode) + this.storeType.hashCode();
            }
            if (this.storeProvider != null && !this.storeProvider.isEmpty()) {
                iHashCode = (31 * iHashCode) + this.storeProvider.hashCode();
            }
            if (this.storeFile != null) {
                iHashCode = (31 * iHashCode) + this.storeFile.hashCode();
            }
            if (this.lastModified != 0) {
                iHashCode = (int) ((31 * iHashCode) + this.lastModified);
            }
            return iHashCode;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/TrustStoreManager$TrustAnchorManager.class */
    private static final class TrustAnchorManager {
        private TrustStoreDescriptor descriptor;
        private WeakReference<KeyStore> ksRef;
        private WeakReference<Set<X509Certificate>> csRef;

        private TrustAnchorManager() {
            this.descriptor = null;
            this.ksRef = new WeakReference<>(null);
            this.csRef = new WeakReference<>(null);
        }

        synchronized KeyStore getKeyStore(TrustStoreDescriptor trustStoreDescriptor) throws Exception {
            TrustStoreDescriptor trustStoreDescriptor2 = this.descriptor;
            KeyStore keyStore = this.ksRef.get();
            if (keyStore != null && trustStoreDescriptor.equals(trustStoreDescriptor2)) {
                return keyStore;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                SSLLogger.fine("Reload the trust store", new Object[0]);
            }
            KeyStore keyStoreLoadKeyStore = loadKeyStore(trustStoreDescriptor);
            this.descriptor = trustStoreDescriptor;
            this.ksRef = new WeakReference<>(keyStoreLoadKeyStore);
            return keyStoreLoadKeyStore;
        }

        synchronized Set<X509Certificate> getTrustedCerts(TrustStoreDescriptor trustStoreDescriptor) throws Exception {
            KeyStore keyStoreLoadKeyStore = null;
            TrustStoreDescriptor trustStoreDescriptor2 = this.descriptor;
            Set<X509Certificate> set = this.csRef.get();
            if (set != null) {
                if (trustStoreDescriptor.equals(trustStoreDescriptor2)) {
                    return set;
                }
                this.descriptor = trustStoreDescriptor;
            } else if (trustStoreDescriptor.equals(trustStoreDescriptor2)) {
                keyStoreLoadKeyStore = this.ksRef.get();
            } else {
                this.descriptor = trustStoreDescriptor;
            }
            if (keyStoreLoadKeyStore == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine("Reload the trust store", new Object[0]);
                }
                keyStoreLoadKeyStore = loadKeyStore(trustStoreDescriptor);
            }
            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                SSLLogger.fine("Reload trust certs", new Object[0]);
            }
            Set<X509Certificate> setLoadTrustedCerts = loadTrustedCerts(keyStoreLoadKeyStore);
            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                SSLLogger.fine("Reloaded " + setLoadTrustedCerts.size() + " trust certs", new Object[0]);
            }
            this.csRef = new WeakReference<>(setLoadTrustedCerts);
            return setLoadTrustedCerts;
        }

        private static KeyStore loadKeyStore(TrustStoreDescriptor trustStoreDescriptor) throws Exception {
            KeyStore keyStore;
            if ("NONE".equals(trustStoreDescriptor.storeName) || trustStoreDescriptor.storeFile != null) {
                if (trustStoreDescriptor.storeProvider.isEmpty()) {
                    keyStore = KeyStore.getInstance(trustStoreDescriptor.storeType);
                } else {
                    keyStore = KeyStore.getInstance(trustStoreDescriptor.storeType, trustStoreDescriptor.storeProvider);
                }
                char[] charArray = null;
                if (!trustStoreDescriptor.storePassword.isEmpty()) {
                    charArray = trustStoreDescriptor.storePassword.toCharArray();
                }
                if (!"NONE".equals(trustStoreDescriptor.storeName)) {
                    try {
                        FileInputStream fileInputStream = (FileInputStream) AccessController.doPrivileged(new OpenFileInputStreamAction(trustStoreDescriptor.storeFile));
                        Throwable th = null;
                        try {
                            try {
                                keyStore.load(fileInputStream, charArray);
                                if (fileInputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        fileInputStream.close();
                                    }
                                }
                            } finally {
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            throw th3;
                        }
                    } catch (FileNotFoundException e2) {
                        if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                            SSLLogger.fine("Not available key store: " + trustStoreDescriptor.storeName, new Object[0]);
                            return null;
                        }
                        return null;
                    }
                } else {
                    keyStore.load(null, charArray);
                }
                return keyStore;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                SSLLogger.fine("No available key store", new Object[0]);
                return null;
            }
            return null;
        }

        private static Set<X509Certificate> loadTrustedCerts(KeyStore keyStore) {
            if (keyStore == null) {
                return Collections.emptySet();
            }
            return TrustStoreUtil.getTrustedCerts(keyStore);
        }
    }
}
