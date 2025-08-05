package java.security.cert;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.util.Collection;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:java/security/cert/CertStore.class */
public class CertStore {
    private static final String CERTSTORE_TYPE = "certstore.type";
    private CertStoreSpi storeSpi;
    private Provider provider;
    private String type;
    private CertStoreParameters params;

    protected CertStore(CertStoreSpi certStoreSpi, Provider provider, String str, CertStoreParameters certStoreParameters) {
        this.storeSpi = certStoreSpi;
        this.provider = provider;
        this.type = str;
        if (certStoreParameters != null) {
            this.params = (CertStoreParameters) certStoreParameters.clone();
        }
    }

    public final Collection<? extends Certificate> getCertificates(CertSelector certSelector) throws CertStoreException {
        return this.storeSpi.engineGetCertificates(certSelector);
    }

    public final Collection<? extends CRL> getCRLs(CRLSelector cRLSelector) throws CertStoreException {
        return this.storeSpi.engineGetCRLs(cRLSelector);
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, certStoreParameters);
            return new CertStore((CertStoreSpi) getInstance.impl, getInstance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    private static CertStore handleException(NoSuchAlgorithmException noSuchAlgorithmException) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Throwable cause = noSuchAlgorithmException.getCause();
        if (cause instanceof InvalidAlgorithmParameterException) {
            throw ((InvalidAlgorithmParameterException) cause);
        }
        throw noSuchAlgorithmException;
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters, String str2) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, certStoreParameters, str2);
            return new CertStore((CertStoreSpi) getInstance.impl, getInstance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters, Provider provider) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        try {
            GetInstance.Instance getInstance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, certStoreParameters, provider);
            return new CertStore((CertStoreSpi) getInstance.impl, getInstance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e2) {
            return handleException(e2);
        }
    }

    public final CertStoreParameters getCertStoreParameters() {
        if (this.params == null) {
            return null;
        }
        return (CertStoreParameters) this.params.clone();
    }

    public final String getType() {
        return this.type;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.cert.CertStore.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty(CertStore.CERTSTORE_TYPE);
            }
        });
        if (str == null) {
            str = "LDAP";
        }
        return str;
    }
}
