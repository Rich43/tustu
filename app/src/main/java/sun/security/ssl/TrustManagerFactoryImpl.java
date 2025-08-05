package sun.security.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.CertPathParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.Collection;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;
import sun.security.validator.TrustStoreUtil;
import sun.security.validator.Validator;

/* loaded from: jsse.jar:sun/security/ssl/TrustManagerFactoryImpl.class */
abstract class TrustManagerFactoryImpl extends TrustManagerFactorySpi {
    private X509TrustManager trustManager = null;
    private boolean isInitialized = false;

    abstract X509TrustManager getInstance(Collection<X509Certificate> collection);

    abstract X509TrustManager getInstance(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException;

    TrustManagerFactoryImpl() {
    }

    @Override // javax.net.ssl.TrustManagerFactorySpi
    protected void engineInit(KeyStore keyStore) throws KeyStoreException {
        if (keyStore == null) {
            try {
                this.trustManager = getInstance(TrustStoreManager.getTrustedCerts());
            } catch (Error e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine("SunX509: skip default keystore", e2);
                }
                throw e2;
            } catch (SecurityException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine("SunX509: skip default keystore", e3);
                }
            } catch (RuntimeException e4) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine("SunX509: skip default keystor", e4);
                }
                throw e4;
            } catch (Exception e5) {
                if (SSLLogger.isOn && SSLLogger.isOn("trustmanager")) {
                    SSLLogger.fine("SunX509: skip default keystore", e5);
                }
                throw new KeyStoreException("problem accessing trust store", e5);
            }
        } else {
            this.trustManager = getInstance(TrustStoreUtil.getTrustedCerts(keyStore));
        }
        this.isInitialized = true;
    }

    @Override // javax.net.ssl.TrustManagerFactorySpi
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        this.trustManager = getInstance(managerFactoryParameters);
        this.isInitialized = true;
    }

    @Override // javax.net.ssl.TrustManagerFactorySpi
    protected TrustManager[] engineGetTrustManagers() {
        if (!this.isInitialized) {
            throw new IllegalStateException("TrustManagerFactoryImpl is not initialized");
        }
        return new TrustManager[]{this.trustManager};
    }

    private static FileInputStream getFileInputStream(final File file) throws Exception {
        return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() { // from class: sun.security.ssl.TrustManagerFactoryImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedExceptionAction
            public FileInputStream run() throws Exception {
                try {
                    if (file.exists()) {
                        return new FileInputStream(file);
                    }
                    return null;
                } catch (FileNotFoundException e2) {
                    return null;
                }
            }
        });
    }

    /* loaded from: jsse.jar:sun/security/ssl/TrustManagerFactoryImpl$SimpleFactory.class */
    public static final class SimpleFactory extends TrustManagerFactoryImpl {
        @Override // sun.security.ssl.TrustManagerFactoryImpl
        X509TrustManager getInstance(Collection<X509Certificate> collection) {
            return new X509TrustManagerImpl(Validator.TYPE_SIMPLE, collection);
        }

        @Override // sun.security.ssl.TrustManagerFactoryImpl
        X509TrustManager getInstance(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("SunX509 TrustManagerFactory does not use ManagerFactoryParameters");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/TrustManagerFactoryImpl$PKIXFactory.class */
    public static final class PKIXFactory extends TrustManagerFactoryImpl {
        @Override // sun.security.ssl.TrustManagerFactoryImpl
        X509TrustManager getInstance(Collection<X509Certificate> collection) {
            return new X509TrustManagerImpl(Validator.TYPE_PKIX, collection);
        }

        @Override // sun.security.ssl.TrustManagerFactoryImpl
        X509TrustManager getInstance(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            if (!(managerFactoryParameters instanceof CertPathTrustManagerParameters)) {
                throw new InvalidAlgorithmParameterException("Parameters must be CertPathTrustManagerParameters");
            }
            CertPathParameters parameters = ((CertPathTrustManagerParameters) managerFactoryParameters).getParameters();
            if (!(parameters instanceof PKIXBuilderParameters)) {
                throw new InvalidAlgorithmParameterException("Encapsulated parameters must be PKIXBuilderParameters");
            }
            return new X509TrustManagerImpl(Validator.TYPE_PKIX, (PKIXBuilderParameters) parameters);
        }
    }
}
