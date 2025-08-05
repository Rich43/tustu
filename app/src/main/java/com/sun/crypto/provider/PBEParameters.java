package com.sun.crypto.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBEParameters.class */
public final class PBEParameters extends AlgorithmParametersSpi {
    private byte[] salt = null;
    private int iCount = 0;
    private AlgorithmParameterSpec cipherParam = null;

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        this.salt = (byte[]) ((PBEParameterSpec) algorithmParameterSpec).getSalt().clone();
        this.iCount = ((PBEParameterSpec) algorithmParameterSpec).getIterationCount();
        this.cipherParam = ((PBEParameterSpec) algorithmParameterSpec).getParameterSpec();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        try {
            DerValue derValue = new DerValue(bArr);
            if (derValue.tag != 48) {
                throw new IOException("PBE parameter parsing error: not a sequence");
            }
            derValue.data.reset();
            this.salt = derValue.data.getOctetString();
            this.iCount = derValue.data.getInteger();
            if (derValue.data.available() != 0) {
                throw new IOException("PBE parameter parsing error: extra data");
            }
        } catch (NumberFormatException e2) {
            throw new IOException("iteration count too big");
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (PBEParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new PBEParameterSpec(this.salt, this.iCount, this.cipherParam));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putOctetString(this.salt);
        derOutputStream2.putInteger(this.iCount);
        derOutputStream.write((byte) 48, derOutputStream2);
        return derOutputStream.toByteArray();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        String property = System.getProperty("line.separator");
        return (((property + "    salt:" + property + "[") + new HexDumpEncoder().encodeBuffer(this.salt)) + "]") + property + "    iterationCount:" + property + Debug.toHexString(BigInteger.valueOf(this.iCount)) + property;
    }
}
