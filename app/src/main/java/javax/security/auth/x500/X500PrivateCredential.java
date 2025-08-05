package javax.security.auth.x500;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.security.auth.Destroyable;

/* loaded from: rt.jar:javax/security/auth/x500/X500PrivateCredential.class */
public final class X500PrivateCredential implements Destroyable {
    private X509Certificate cert;
    private PrivateKey key;
    private String alias;

    public X500PrivateCredential(X509Certificate x509Certificate, PrivateKey privateKey) {
        if (x509Certificate == null || privateKey == null) {
            throw new IllegalArgumentException();
        }
        this.cert = x509Certificate;
        this.key = privateKey;
        this.alias = null;
    }

    public X500PrivateCredential(X509Certificate x509Certificate, PrivateKey privateKey, String str) {
        if (x509Certificate == null || privateKey == null || str == null) {
            throw new IllegalArgumentException();
        }
        this.cert = x509Certificate;
        this.key = privateKey;
        this.alias = str;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public PrivateKey getPrivateKey() {
        return this.key;
    }

    public String getAlias() {
        return this.alias;
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() {
        this.cert = null;
        this.key = null;
        this.alias = null;
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return this.cert == null && this.key == null && this.alias == null;
    }
}
