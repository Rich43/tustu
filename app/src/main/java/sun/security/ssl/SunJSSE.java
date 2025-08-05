package sun.security.ssl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import org.apache.commons.net.imap.IMAPSClient;
import sun.security.util.SecurityConstants;
import sun.security.validator.Validator;

/* loaded from: jsse.jar:sun/security/ssl/SunJSSE.class */
public abstract class SunJSSE extends Provider {
    private static final long serialVersionUID = 3231825739635378733L;
    private static String info = "Sun JSSE provider(PKCS12, SunX509/PKIX key/trust factories, SSLv3/TLSv1/TLSv1.1/TLSv1.2/TLSv1.3)";
    private static String fipsInfo = "Sun JSSE provider (FIPS mode, crypto provider ";
    private static Boolean fips;
    static Provider cryptoProvider;

    protected static synchronized boolean isFIPS() {
        if (fips == null) {
            fips = false;
        }
        return fips.booleanValue();
    }

    private static synchronized void ensureFIPS(Provider provider) {
        if (fips == null) {
            fips = true;
            cryptoProvider = provider;
        } else {
            if (!fips.booleanValue()) {
                throw new ProviderException("SunJSSE already initialized in non-FIPS mode");
            }
            if (cryptoProvider != provider) {
                throw new ProviderException("SunJSSE already initialized with FIPS crypto provider " + ((Object) cryptoProvider));
            }
        }
    }

    protected SunJSSE() {
        super("SunJSSE", 1.8d, info);
        subclassCheck();
        if (Boolean.TRUE.equals(fips)) {
            throw new ProviderException("SunJSSE is already initialized in FIPS mode");
        }
        registerAlgorithms(false);
    }

    protected SunJSSE(Provider provider) {
        this((Provider) checkNull(provider), provider.getName());
    }

    protected SunJSSE(String str) {
        this(null, (String) checkNull(str));
    }

    private static <T> T checkNull(T t2) {
        if (t2 == null) {
            throw new ProviderException("cryptoProvider must not be null");
        }
        return t2;
    }

    private SunJSSE(Provider provider, String str) {
        super("SunJSSE", SecurityConstants.PROVIDER_VER.doubleValue(), fipsInfo + str + ")");
        subclassCheck();
        if (provider == null) {
            provider = Security.getProvider(str);
            if (provider == null) {
                throw new ProviderException("Crypto provider not installed: " + str);
            }
        }
        ensureFIPS(provider);
        registerAlgorithms(true);
    }

    private void registerAlgorithms(final boolean z2) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.security.ssl.SunJSSE.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                SunJSSE.this.doRegister(z2);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRegister(boolean z2) {
        if (!z2) {
            put("KeyFactory.RSA", "sun.security.rsa.RSAKeyFactory$Legacy");
            put("Alg.Alias.KeyFactory.1.2.840.113549.1.1", "RSA");
            put("Alg.Alias.KeyFactory.OID.1.2.840.113549.1.1", "RSA");
            put("KeyPairGenerator.RSA", "sun.security.rsa.RSAKeyPairGenerator$Legacy");
            put("Alg.Alias.KeyPairGenerator.1.2.840.113549.1.1", "RSA");
            put("Alg.Alias.KeyPairGenerator.OID.1.2.840.113549.1.1", "RSA");
            put("Signature.MD2withRSA", "sun.security.rsa.RSASignature$MD2withRSA");
            put("Alg.Alias.Signature.1.2.840.113549.1.1.2", "MD2withRSA");
            put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.2", "MD2withRSA");
            put("Signature.MD5withRSA", "sun.security.rsa.RSASignature$MD5withRSA");
            put("Alg.Alias.Signature.1.2.840.113549.1.1.4", "MD5withRSA");
            put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.4", "MD5withRSA");
            put("Signature.SHA1withRSA", "sun.security.rsa.RSASignature$SHA1withRSA");
            put("Alg.Alias.Signature.1.2.840.113549.1.1.5", "SHA1withRSA");
            put("Alg.Alias.Signature.OID.1.2.840.113549.1.1.5", "SHA1withRSA");
            put("Alg.Alias.Signature.1.3.14.3.2.29", "SHA1withRSA");
            put("Alg.Alias.Signature.OID.1.3.14.3.2.29", "SHA1withRSA");
        }
        put("Signature.MD5andSHA1withRSA", "sun.security.ssl.RSASignature");
        put("KeyManagerFactory.SunX509", "sun.security.ssl.KeyManagerFactoryImpl$SunX509");
        put("KeyManagerFactory.NewSunX509", "sun.security.ssl.KeyManagerFactoryImpl$X509");
        put("Alg.Alias.KeyManagerFactory.PKIX", "NewSunX509");
        put("TrustManagerFactory.SunX509", "sun.security.ssl.TrustManagerFactoryImpl$SimpleFactory");
        put("TrustManagerFactory.PKIX", "sun.security.ssl.TrustManagerFactoryImpl$PKIXFactory");
        put("Alg.Alias.TrustManagerFactory.SunPKIX", Validator.TYPE_PKIX);
        put("Alg.Alias.TrustManagerFactory.X509", Validator.TYPE_PKIX);
        put("Alg.Alias.TrustManagerFactory.X.509", Validator.TYPE_PKIX);
        put("SSLContext.TLSv1", "sun.security.ssl.SSLContextImpl$TLS10Context");
        put("SSLContext.TLSv1.1", "sun.security.ssl.SSLContextImpl$TLS11Context");
        put("SSLContext.TLSv1.2", "sun.security.ssl.SSLContextImpl$TLS12Context");
        put("SSLContext.TLSv1.3", "sun.security.ssl.SSLContextImpl$TLS13Context");
        put("SSLContext.TLS", "sun.security.ssl.SSLContextImpl$TLSContext");
        if (!z2) {
            put("Alg.Alias.SSLContext.SSL", IMAPSClient.DEFAULT_PROTOCOL);
            put("Alg.Alias.SSLContext.SSLv3", "TLSv1");
        }
        put("SSLContext.Default", "sun.security.ssl.SSLContextImpl$DefaultSSLContext");
        put("KeyStore.PKCS12", "sun.security.pkcs12.PKCS12KeyStore");
    }

    private void subclassCheck() {
        if (getClass() != com.sun.net.ssl.internal.ssl.Provider.class) {
            throw new AssertionError((Object) ("Illegal subclass: " + ((Object) getClass())));
        }
    }

    protected final void finalize() throws Throwable {
        super.finalize();
    }
}
