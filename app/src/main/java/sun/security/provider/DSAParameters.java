package sun.security.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/provider/DSAParameters.class */
public class DSAParameters extends AlgorithmParametersSpi {

    /* renamed from: p, reason: collision with root package name */
    protected BigInteger f13630p;

    /* renamed from: q, reason: collision with root package name */
    protected BigInteger f13631q;

    /* renamed from: g, reason: collision with root package name */
    protected BigInteger f13632g;

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        this.f13630p = ((DSAParameterSpec) algorithmParameterSpec).getP();
        this.f13631q = ((DSAParameterSpec) algorithmParameterSpec).getQ();
        this.f13632g = ((DSAParameterSpec) algorithmParameterSpec).getG();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag != 48) {
            throw new IOException("DSA params parsing error");
        }
        derValue.data.reset();
        this.f13630p = derValue.data.getBigInteger();
        this.f13631q = derValue.data.getBigInteger();
        this.f13632g = derValue.data.getBigInteger();
        if (derValue.data.available() != 0) {
            throw new IOException("encoded params have " + derValue.data.available() + " extra bytes");
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        try {
            if (Class.forName("java.security.spec.DSAParameterSpec").isAssignableFrom(cls)) {
                return cls.cast(new DSAParameterSpec(this.f13630p, this.f13631q, this.f13632g));
            }
            throw new InvalidParameterSpecException("Inappropriate parameter Specification");
        } catch (ClassNotFoundException e2) {
            throw new InvalidParameterSpecException("Unsupported parameter specification: " + e2.getMessage());
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.f13630p);
        derOutputStream2.putInteger(this.f13631q);
        derOutputStream2.putInteger(this.f13632g);
        derOutputStream.write((byte) 48, derOutputStream2);
        return derOutputStream.toByteArray();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        return "\n\tp: " + Debug.toHexString(this.f13630p) + "\n\tq: " + Debug.toHexString(this.f13631q) + "\n\tg: " + Debug.toHexString(this.f13632g) + "\n";
    }
}
