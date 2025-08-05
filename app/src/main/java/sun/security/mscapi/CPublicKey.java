package sun.security.mscapi;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ObjectStreamException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyException;
import java.security.KeyFactory;
import java.security.KeyRep;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Arrays;
import sun.security.mscapi.CKey;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.rsa.RSAUtil;
import sun.security.util.ECKeySizeParameterSpec;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CPublicKey.class */
public abstract class CPublicKey extends CKey implements PublicKey {
    private static final long serialVersionUID = -2289561342425825391L;
    protected byte[] encoding;

    native byte[] getPublicKeyBlob(long j2, long j3) throws KeyException;

    @Override // sun.security.mscapi.CKey, java.security.Key
    public /* bridge */ /* synthetic */ String getAlgorithm() {
        return super.getAlgorithm();
    }

    @Override // sun.security.mscapi.CKey
    public /* bridge */ /* synthetic */ long getHCryptProvider() {
        return super.getHCryptProvider();
    }

    @Override // sun.security.mscapi.CKey
    public /* bridge */ /* synthetic */ long getHCryptKey() {
        return super.getHCryptKey();
    }

    @Override // sun.security.mscapi.CKey, sun.security.util.Length
    public /* bridge */ /* synthetic */ int length() {
        return super.length();
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CPublicKey$CECPublicKey.class */
    public static class CECPublicKey extends CPublicKey implements ECPublicKey {

        /* renamed from: w, reason: collision with root package name */
        private ECPoint f13612w;
        private static final long serialVersionUID = 12;

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey, java.security.Key
        public /* bridge */ /* synthetic */ String getAlgorithm() {
            return super.getAlgorithm();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey
        public /* bridge */ /* synthetic */ long getHCryptProvider() {
            return super.getHCryptProvider();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey
        public /* bridge */ /* synthetic */ long getHCryptKey() {
            return super.getHCryptKey();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey, sun.security.util.Length
        public /* bridge */ /* synthetic */ int length() {
            return super.length();
        }

        CECPublicKey(CKey.NativeHandles nativeHandles, int i2) {
            super("EC", nativeHandles, i2);
            this.f13612w = null;
        }

        @Override // java.security.interfaces.ECPublicKey
        public ECPoint getW() {
            if (this.f13612w == null) {
                try {
                    byte[] publicKeyBlob = getPublicKeyBlob(this.handles.hCryptProv, this.handles.hCryptKey);
                    int i2 = publicKeyBlob[8] & 255;
                    this.f13612w = new ECPoint(new BigInteger(1, Arrays.copyOfRange(publicKeyBlob, 8, 8 + i2)), new BigInteger(1, Arrays.copyOfRange(publicKeyBlob, 8 + i2, 8 + i2 + i2)));
                } catch (KeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.f13612w;
        }

        @Override // java.security.Key
        public byte[] getEncoded() {
            if (this.encoding == null) {
                try {
                    this.encoding = KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(getW(), getParams())).getEncoded();
                } catch (Exception e2) {
                }
            }
            return this.encoding;
        }

        @Override // java.security.interfaces.ECKey
        public ECParameterSpec getParams() {
            try {
                AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
                algorithmParameters.init(new ECKeySizeParameterSpec(this.keyLength));
                return (ECParameterSpec) algorithmParameters.getParameterSpec(ECParameterSpec.class);
            } catch (Exception e2) {
                throw new ProviderException(e2);
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.algorithm + "PublicKey [size=").append(this.keyLength).append("]\n  ECPoint: ").append((Object) getW()).append("\n  params: ").append((Object) getParams());
            return stringBuffer.toString();
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CPublicKey$CRSAPublicKey.class */
    public static class CRSAPublicKey extends CPublicKey implements RSAPublicKey {
        private BigInteger modulus;
        private BigInteger exponent;
        private static final long serialVersionUID = 12;

        private native byte[] getExponent(byte[] bArr) throws KeyException;

        private native byte[] getModulus(byte[] bArr) throws KeyException;

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey, java.security.Key
        public /* bridge */ /* synthetic */ String getAlgorithm() {
            return super.getAlgorithm();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey
        public /* bridge */ /* synthetic */ long getHCryptProvider() {
            return super.getHCryptProvider();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey
        public /* bridge */ /* synthetic */ long getHCryptKey() {
            return super.getHCryptKey();
        }

        @Override // sun.security.mscapi.CPublicKey, sun.security.mscapi.CKey, sun.security.util.Length
        public /* bridge */ /* synthetic */ int length() {
            return super.length();
        }

        CRSAPublicKey(CKey.NativeHandles nativeHandles, int i2) {
            super("RSA", nativeHandles, i2);
            this.modulus = null;
            this.exponent = null;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.algorithm + "PublicKey [size=").append(this.keyLength).append(" bits, type=");
            if (this.handles.hCryptKey != 0) {
                stringBuffer.append(getKeyType(this.handles.hCryptKey)).append(", container=").append(getContainerName(this.handles.hCryptProv));
            } else {
                stringBuffer.append("CNG");
            }
            stringBuffer.append("]\n  modulus: ").append((Object) getModulus()).append("\n  public exponent: ").append((Object) getPublicExponent());
            return stringBuffer.toString();
        }

        @Override // java.security.interfaces.RSAPublicKey
        public BigInteger getPublicExponent() {
            if (this.exponent == null) {
                try {
                    this.exponent = new BigInteger(1, getExponent(getPublicKeyBlob(this.handles.hCryptProv, this.handles.hCryptKey)));
                } catch (KeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.exponent;
        }

        @Override // java.security.interfaces.RSAKey
        public BigInteger getModulus() {
            if (this.modulus == null) {
                try {
                    this.modulus = new BigInteger(1, getModulus(getPublicKeyBlob(this.handles.hCryptProv, this.handles.hCryptKey)));
                } catch (KeyException e2) {
                    throw new ProviderException(e2);
                }
            }
            return this.modulus;
        }

        @Override // java.security.Key
        public byte[] getEncoded() {
            if (this.encoding == null) {
                try {
                    this.encoding = RSAPublicKeyImpl.newKey(RSAUtil.KeyType.RSA, null, getModulus(), getPublicExponent()).getEncoded();
                } catch (KeyException e2) {
                }
            }
            return this.encoding;
        }
    }

    static CPublicKey of(String str, long j2, long j3, int i2) {
        return of(str, new CKey.NativeHandles(j2, j3), i2);
    }

    public static CPublicKey of(String str, CKey.NativeHandles nativeHandles, int i2) {
        switch (str) {
            case "RSA":
                return new CRSAPublicKey(nativeHandles, i2);
            case "EC":
                return new CECPublicKey(nativeHandles, i2);
            default:
                throw new AssertionError((Object) ("Unsupported algorithm: " + str));
        }
    }

    protected CPublicKey(String str, CKey.NativeHandles nativeHandles, int i2) {
        super(str, nativeHandles, i2);
        this.encoding = null;
    }

    @Override // java.security.Key
    public String getFormat() {
        return XMLX509Certificate.JCA_CERT_ID;
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PUBLIC, getAlgorithm(), getFormat(), getEncoded());
    }
}
