package java.security;

import java.io.Serializable;

/* loaded from: rt.jar:java/security/SecureRandomSpi.class */
public abstract class SecureRandomSpi implements Serializable {
    private static final long serialVersionUID = -2991854161009191830L;

    protected abstract void engineSetSeed(byte[] bArr);

    protected abstract void engineNextBytes(byte[] bArr);

    protected abstract byte[] engineGenerateSeed(int i2);
}
