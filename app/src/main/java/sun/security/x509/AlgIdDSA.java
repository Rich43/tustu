package sun.security.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.ProviderException;
import java.security.interfaces.DSAParams;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/x509/AlgIdDSA.class */
public final class AlgIdDSA extends AlgorithmId implements DSAParams {
    private static final long serialVersionUID = 3437177836797504046L;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f13669p;

    /* renamed from: q, reason: collision with root package name */
    private BigInteger f13670q;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f13671g;

    @Override // java.security.interfaces.DSAParams
    public BigInteger getP() {
        return this.f13669p;
    }

    @Override // java.security.interfaces.DSAParams
    public BigInteger getQ() {
        return this.f13670q;
    }

    @Override // java.security.interfaces.DSAParams
    public BigInteger getG() {
        return this.f13671g;
    }

    @Deprecated
    public AlgIdDSA() {
    }

    AlgIdDSA(DerValue derValue) throws IOException {
        super(derValue.getOID());
    }

    public AlgIdDSA(byte[] bArr) throws IOException {
        super(new DerValue(bArr).getOID());
    }

    public AlgIdDSA(byte[] bArr, byte[] bArr2, byte[] bArr3) throws IOException {
        this(new BigInteger(1, bArr), new BigInteger(1, bArr2), new BigInteger(1, bArr3));
    }

    public AlgIdDSA(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        super(DSA_oid);
        if (bigInteger != null || bigInteger2 != null || bigInteger3 != null) {
            if (bigInteger == null || bigInteger2 == null || bigInteger3 == null) {
                throw new ProviderException("Invalid parameters for DSS/DSA Algorithm ID");
            }
            try {
                this.f13669p = bigInteger;
                this.f13670q = bigInteger2;
                this.f13671g = bigInteger3;
                initializeParams();
            } catch (IOException e2) {
                throw new ProviderException("Construct DSS/DSA Algorithm ID");
            }
        }
    }

    @Override // sun.security.x509.AlgorithmId
    public String getName() {
        return "DSA";
    }

    private void initializeParams() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(this.f13669p);
        derOutputStream.putInteger(this.f13670q);
        derOutputStream.putInteger(this.f13671g);
        this.params = new DerValue((byte) 48, derOutputStream.toByteArray());
    }

    @Override // sun.security.x509.AlgorithmId
    protected void decodeParams() throws IOException {
        if (this.params == null) {
            throw new IOException("DSA alg params are null");
        }
        if (this.params.tag != 48) {
            throw new IOException("DSA alg parsing error");
        }
        this.params.data.reset();
        this.f13669p = this.params.data.getBigInteger();
        this.f13670q = this.params.data.getBigInteger();
        this.f13671g = this.params.data.getBigInteger();
        if (this.params.data.available() != 0) {
            throw new IOException("AlgIdDSA params, extra=" + this.params.data.available());
        }
    }

    @Override // sun.security.x509.AlgorithmId
    public String toString() {
        return paramsToString();
    }

    @Override // sun.security.x509.AlgorithmId
    protected String paramsToString() {
        if (this.params == null) {
            return " null\n";
        }
        return "\n    p:\n" + Debug.toHexString(this.f13669p) + "\n    q:\n" + Debug.toHexString(this.f13670q) + "\n    g:\n" + Debug.toHexString(this.f13671g) + "\n";
    }
}
