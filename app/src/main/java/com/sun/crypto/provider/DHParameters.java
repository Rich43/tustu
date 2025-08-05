package com.sun.crypto.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHParameterSpec;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHParameters.class */
public final class DHParameters extends AlgorithmParametersSpi {

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f11812p = BigInteger.ZERO;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f11813g = BigInteger.ZERO;

    /* renamed from: l, reason: collision with root package name */
    private int f11814l = 0;

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        this.f11812p = ((DHParameterSpec) algorithmParameterSpec).getP();
        this.f11813g = ((DHParameterSpec) algorithmParameterSpec).getG();
        this.f11814l = ((DHParameterSpec) algorithmParameterSpec).getL();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        try {
            DerValue derValue = new DerValue(bArr);
            if (derValue.tag != 48) {
                throw new IOException("DH params parsing error");
            }
            derValue.data.reset();
            this.f11812p = derValue.data.getBigInteger();
            this.f11813g = derValue.data.getBigInteger();
            if (derValue.data.available() != 0) {
                this.f11814l = derValue.data.getInteger();
            }
            if (derValue.data.available() != 0) {
                throw new IOException("DH parameter parsing error: Extra data");
            }
        } catch (NumberFormatException e2) {
            throw new IOException("Private-value length too big");
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (DHParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new DHParameterSpec(this.f11812p, this.f11813g, this.f11814l));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter Specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.f11812p);
        derOutputStream2.putInteger(this.f11813g);
        if (this.f11814l > 0) {
            derOutputStream2.putInteger(this.f11814l);
        }
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
        StringBuffer stringBuffer = new StringBuffer("SunJCE Diffie-Hellman Parameters:" + property + "p:" + property + Debug.toHexString(this.f11812p) + property + "g:" + property + Debug.toHexString(this.f11813g));
        if (this.f11814l != 0) {
            stringBuffer.append(property + "l:" + property + "    " + this.f11814l);
        }
        return stringBuffer.toString();
    }
}
