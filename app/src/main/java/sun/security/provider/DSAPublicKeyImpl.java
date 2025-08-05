package sun.security.provider;

import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyRep;

/* loaded from: rt.jar:sun/security/provider/DSAPublicKeyImpl.class */
public final class DSAPublicKeyImpl extends DSAPublicKey {
    private static final long serialVersionUID = 7819830118247182730L;

    public DSAPublicKeyImpl(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws InvalidKeyException {
        super(bigInteger, bigInteger2, bigInteger3, bigInteger4);
    }

    public DSAPublicKeyImpl(byte[] bArr) throws InvalidKeyException {
        super(bArr);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
    }
}
