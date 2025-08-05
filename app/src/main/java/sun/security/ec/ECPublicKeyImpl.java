package sun.security.ec;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.InvalidParameterSpecException;
import sun.security.util.ECParameters;
import sun.security.util.ECUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X509Key;

/* loaded from: sunec.jar:sun/security/ec/ECPublicKeyImpl.class */
public final class ECPublicKeyImpl extends X509Key implements ECPublicKey {
    private static final long serialVersionUID = -2462037275160462289L;

    /* renamed from: w, reason: collision with root package name */
    private ECPoint f13604w;
    private ECParameterSpec params;

    public ECPublicKeyImpl(ECPoint eCPoint, ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        this.f13604w = eCPoint;
        this.params = eCParameterSpec;
        this.algid = new AlgorithmId(AlgorithmId.EC_oid, ECParameters.getAlgorithmParameters(eCParameterSpec));
        this.key = ECUtil.encodePoint(eCPoint, eCParameterSpec.getCurve());
    }

    public ECPublicKeyImpl(byte[] bArr) throws InvalidKeyException {
        decode(bArr);
    }

    @Override // sun.security.x509.X509Key, java.security.Key
    public String getAlgorithm() {
        return "EC";
    }

    @Override // java.security.interfaces.ECPublicKey
    public ECPoint getW() {
        return this.f13604w;
    }

    @Override // java.security.interfaces.ECKey
    public ECParameterSpec getParams() {
        return this.params;
    }

    public byte[] getEncodedPublicValue() {
        return (byte[]) this.key.clone();
    }

    @Override // sun.security.x509.X509Key
    protected void parseKeyBits() throws InvalidKeyException {
        AlgorithmParameters parameters = this.algid.getParameters();
        if (parameters == null) {
            throw new InvalidKeyException("EC domain parameters must be encoded in the algorithm identifier");
        }
        try {
            this.params = (ECParameterSpec) parameters.getParameterSpec(ECParameterSpec.class);
            this.f13604w = ECUtil.decodePoint(this.key, this.params.getCurve());
        } catch (IOException e2) {
            throw new InvalidKeyException("Invalid EC key", e2);
        } catch (InvalidParameterSpecException e3) {
            throw new InvalidKeyException("Invalid EC key", e3);
        }
    }

    @Override // sun.security.x509.X509Key
    public String toString() {
        return "Sun EC public key, " + this.params.getCurve().getField().getFieldSize() + " bits\n  public x coord: " + ((Object) this.f13604w.getAffineX()) + "\n  public y coord: " + ((Object) this.f13604w.getAffineY()) + "\n  parameters: " + ((Object) this.params);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
    }
}
