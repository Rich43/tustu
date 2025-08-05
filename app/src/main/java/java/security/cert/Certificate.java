package java.security.cert;

import java.io.ByteArrayInputStream;
import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Arrays;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:java/security/cert/Certificate.class */
public abstract class Certificate implements Serializable {
    private static final long serialVersionUID = -3585440601605666277L;
    private final String type;
    private int hash = -1;

    public abstract byte[] getEncoded() throws CertificateEncodingException;

    public abstract void verify(PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException, NoSuchProviderException;

    public abstract void verify(PublicKey publicKey, String str) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException, NoSuchProviderException;

    public abstract String toString();

    public abstract PublicKey getPublicKey();

    protected Certificate(String str) {
        this.type = str;
    }

    public final String getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Certificate)) {
            return false;
        }
        try {
            return Arrays.equals(X509CertImpl.getEncodedInternal(this), X509CertImpl.getEncodedInternal((Certificate) obj));
        } catch (CertificateException e2) {
            return false;
        }
    }

    public int hashCode() {
        int iHashCode = this.hash;
        if (iHashCode == -1) {
            try {
                iHashCode = Arrays.hashCode(X509CertImpl.getEncodedInternal(this));
            } catch (CertificateException e2) {
                iHashCode = 0;
            }
            this.hash = iHashCode;
        }
        return iHashCode;
    }

    public void verify(PublicKey publicKey, Provider provider) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException {
        throw new UnsupportedOperationException();
    }

    /* loaded from: rt.jar:java/security/cert/Certificate$CertificateRep.class */
    protected static class CertificateRep implements Serializable {
        private static final long serialVersionUID = -8563758940495660020L;
        private String type;
        private byte[] data;

        protected CertificateRep(String str, byte[] bArr) {
            this.type = str;
            this.data = bArr;
        }

        protected Object readResolve() throws ObjectStreamException {
            try {
                return CertificateFactory.getInstance(this.type).generateCertificate(new ByteArrayInputStream(this.data));
            } catch (CertificateException e2) {
                throw new NotSerializableException("java.security.cert.Certificate: " + this.type + ": " + e2.getMessage());
            }
        }
    }

    protected Object writeReplace() throws ObjectStreamException {
        try {
            return new CertificateRep(this.type, getEncoded());
        } catch (CertificateException e2) {
            throw new NotSerializableException("java.security.cert.Certificate: " + this.type + ": " + e2.getMessage());
        }
    }
}
