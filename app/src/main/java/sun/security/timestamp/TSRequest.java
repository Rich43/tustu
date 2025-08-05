package sun.security.timestamp;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Extension;
import sun.security.util.DerOutputStream;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/timestamp/TSRequest.class */
public class TSRequest {
    private AlgorithmId hashAlgorithmId;
    private byte[] hashValue;
    private String policyId;
    private int version = 1;
    private BigInteger nonce = null;
    private boolean returnCertificate = false;
    private X509Extension[] extensions = null;

    public TSRequest(String str, byte[] bArr, MessageDigest messageDigest) throws NoSuchAlgorithmException {
        this.hashAlgorithmId = null;
        this.policyId = null;
        this.policyId = str;
        this.hashAlgorithmId = AlgorithmId.get(messageDigest.getAlgorithm());
        this.hashValue = messageDigest.digest(bArr);
    }

    public byte[] getHashedMessage() {
        return (byte[]) this.hashValue.clone();
    }

    public void setVersion(int i2) {
        this.version = i2;
    }

    public void setPolicyId(String str) {
        this.policyId = str;
    }

    public void setNonce(BigInteger bigInteger) {
        this.nonce = bigInteger;
    }

    public void requestCertificate(boolean z2) {
        this.returnCertificate = z2;
    }

    public void setExtensions(X509Extension[] x509ExtensionArr) {
        this.extensions = x509ExtensionArr;
    }

    public byte[] encode() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(this.version);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.hashAlgorithmId.encode(derOutputStream2);
        derOutputStream2.putOctetString(this.hashValue);
        derOutputStream.write((byte) 48, derOutputStream2);
        if (this.policyId != null) {
            derOutputStream.putOID(new ObjectIdentifier(this.policyId));
        }
        if (this.nonce != null) {
            derOutputStream.putInteger(this.nonce);
        }
        if (this.returnCertificate) {
            derOutputStream.putBoolean(true);
        }
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        return derOutputStream3.toByteArray();
    }
}
