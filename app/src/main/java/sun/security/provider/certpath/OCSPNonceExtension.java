package sun.security.provider.certpath;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Objects;
import sun.security.util.Debug;
import sun.security.util.DerValue;
import sun.security.x509.Extension;
import sun.security.x509.PKIXExtensions;

/* loaded from: rt.jar:sun/security/provider/certpath/OCSPNonceExtension.class */
public final class OCSPNonceExtension extends Extension {
    private static final String EXTENSION_NAME = "OCSPNonce";
    private byte[] nonceData;

    public OCSPNonceExtension(int i2) throws IOException {
        this(false, i2);
    }

    public OCSPNonceExtension(boolean z2, int i2) throws IOException {
        this.nonceData = null;
        this.extensionId = PKIXExtensions.OCSPNonce_Id;
        this.critical = z2;
        if (i2 > 0) {
            SecureRandom secureRandom = new SecureRandom();
            this.nonceData = new byte[i2];
            secureRandom.nextBytes(this.nonceData);
            this.extensionValue = new DerValue((byte) 4, this.nonceData).toByteArray();
            return;
        }
        throw new IllegalArgumentException("Length must be a positive integer");
    }

    public OCSPNonceExtension(byte[] bArr) throws IOException {
        this(false, bArr);
    }

    public OCSPNonceExtension(boolean z2, byte[] bArr) throws IOException {
        this.nonceData = null;
        this.extensionId = PKIXExtensions.OCSPNonce_Id;
        this.critical = z2;
        Objects.requireNonNull(bArr, "Nonce data must be non-null");
        if (bArr.length > 0) {
            this.nonceData = (byte[]) bArr.clone();
            this.extensionValue = new DerValue((byte) 4, this.nonceData).toByteArray();
            return;
        }
        throw new IllegalArgumentException("Nonce data must be at least 1 byte in length");
    }

    public byte[] getNonceValue() {
        return (byte[]) this.nonceData.clone();
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(EXTENSION_NAME).append(": ");
        sb.append(this.nonceData == null ? "" : Debug.toString(this.nonceData));
        sb.append("\n");
        return sb.toString();
    }

    public String getName() {
        return EXTENSION_NAME;
    }
}
