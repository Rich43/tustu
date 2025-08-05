package sun.security.mscapi;

import java.io.Serializable;
import java.security.ProviderException;
import java.security.SecureRandomSpi;

/* loaded from: sunmscapi.jar:sun/security/mscapi/PRNG.class */
public final class PRNG extends SecureRandomSpi implements Serializable {
    private static final long serialVersionUID = 4129268715132691532L;

    private static native byte[] generateSeed(int i2, byte[] bArr);

    @Override // java.security.SecureRandomSpi
    protected void engineSetSeed(byte[] bArr) {
        if (bArr != null) {
            generateSeed(-1, bArr);
        }
    }

    @Override // java.security.SecureRandomSpi
    protected void engineNextBytes(byte[] bArr) {
        if (bArr != null && generateSeed(0, bArr) == null) {
            throw new ProviderException("Error generating random bytes");
        }
    }

    @Override // java.security.SecureRandomSpi
    protected byte[] engineGenerateSeed(int i2) {
        byte[] bArrGenerateSeed = generateSeed(i2, null);
        if (bArrGenerateSeed == null) {
            throw new ProviderException("Error generating seed bytes");
        }
        return bArrGenerateSeed;
    }
}
