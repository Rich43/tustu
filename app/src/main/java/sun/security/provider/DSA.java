package sun.security.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.DSAParams;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import sun.security.jca.JCAUtil;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/provider/DSA.class */
abstract class DSA extends SignatureSpi {
    private static final boolean debug = false;
    private static final int BLINDING_BITS = 7;
    private static final BigInteger BLINDING_CONSTANT = BigInteger.valueOf(128);
    private DSAParams params;
    private BigInteger presetP;
    private BigInteger presetQ;
    private BigInteger presetG;
    private BigInteger presetY;
    private BigInteger presetX;
    private java.security.SecureRandom signingRandom;
    private final MessageDigest md;

    DSA(MessageDigest messageDigest) {
        this.md = messageDigest;
    }

    private static void checkKey(DSAParams dSAParams, int i2, String str) throws InvalidKeyException {
        if (dSAParams.getQ().bitLength() > i2) {
            throw new InvalidKeyException("The security strength of " + str + " digest algorithm is not sufficient for this key size");
        }
    }

    @Override // java.security.SignatureSpi
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof java.security.interfaces.DSAPrivateKey)) {
            throw new InvalidKeyException("not a DSA private key: " + ((Object) privateKey));
        }
        java.security.interfaces.DSAPrivateKey dSAPrivateKey = (java.security.interfaces.DSAPrivateKey) privateKey;
        DSAParams params = dSAPrivateKey.getParams();
        if (params == null) {
            throw new InvalidKeyException("DSA private key lacks parameters");
        }
        if (this.md.getAlgorithm() != "NullDigest20") {
            checkKey(params, this.md.getDigestLength() * 8, this.md.getAlgorithm());
        }
        this.params = params;
        this.presetX = dSAPrivateKey.getX();
        this.presetY = null;
        this.presetP = params.getP();
        this.presetQ = params.getQ();
        this.presetG = params.getG();
        this.md.reset();
    }

    @Override // java.security.SignatureSpi
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof java.security.interfaces.DSAPublicKey)) {
            throw new InvalidKeyException("not a DSA public key: " + ((Object) publicKey));
        }
        java.security.interfaces.DSAPublicKey dSAPublicKey = (java.security.interfaces.DSAPublicKey) publicKey;
        DSAParams params = dSAPublicKey.getParams();
        if (params == null) {
            throw new InvalidKeyException("DSA public key lacks parameters");
        }
        this.params = params;
        this.presetY = dSAPublicKey.getY();
        this.presetX = null;
        this.presetP = params.getP();
        this.presetQ = params.getQ();
        this.presetG = params.getG();
        this.md.reset();
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte b2) {
        this.md.update(b2);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(byte[] bArr, int i2, int i3) {
        this.md.update(bArr, i2, i3);
    }

    @Override // java.security.SignatureSpi
    protected void engineUpdate(ByteBuffer byteBuffer) {
        this.md.update(byteBuffer);
    }

    @Override // java.security.SignatureSpi
    protected byte[] engineSign() throws SignatureException {
        BigInteger bigIntegerGenerateK = generateK(this.presetQ);
        BigInteger bigIntegerGenerateR = generateR(this.presetP, this.presetQ, this.presetG, bigIntegerGenerateK);
        BigInteger bigIntegerGenerateS = generateS(this.presetX, this.presetQ, bigIntegerGenerateR, bigIntegerGenerateK);
        try {
            DerOutputStream derOutputStream = new DerOutputStream(100);
            derOutputStream.putInteger(bigIntegerGenerateR);
            derOutputStream.putInteger(bigIntegerGenerateS);
            return new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (IOException e2) {
            throw new SignatureException("error encoding signature");
        }
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr) throws SignatureException {
        return engineVerify(bArr, 0, bArr.length);
    }

    @Override // java.security.SignatureSpi
    protected boolean engineVerify(byte[] bArr, int i2, int i3) throws SignatureException, IOException {
        try {
            DerInputStream derInputStream = new DerInputStream(bArr, i2, i3, false);
            DerValue[] sequence = derInputStream.getSequence(2);
            if (sequence.length != 2 || derInputStream.available() != 0) {
                throw new IOException("Invalid encoding for signature");
            }
            BigInteger bigInteger = sequence[0].getBigInteger();
            BigInteger bigInteger2 = sequence[1].getBigInteger();
            if (bigInteger.signum() < 0) {
                bigInteger = new BigInteger(1, bigInteger.toByteArray());
            }
            if (bigInteger2.signum() < 0) {
                bigInteger2 = new BigInteger(1, bigInteger2.toByteArray());
            }
            if (bigInteger.compareTo(this.presetQ) == -1 && bigInteger2.compareTo(this.presetQ) == -1 && bigInteger.signum() > 0 && bigInteger2.signum() > 0) {
                return generateV(this.presetY, this.presetP, this.presetQ, this.presetG, generateW(this.presetP, this.presetQ, this.presetG, bigInteger2), bigInteger).equals(bigInteger);
            }
            throw new SignatureException("invalid signature: out of range values");
        } catch (IOException e2) {
            throw new SignatureException("Invalid encoding for signature", e2);
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected void engineSetParameter(String str, Object obj) {
        throw new InvalidParameterException("No parameter accepted");
    }

    @Override // java.security.SignatureSpi
    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null) {
            throw new InvalidAlgorithmParameterException("No parameter accepted");
        }
    }

    @Override // java.security.SignatureSpi
    @Deprecated
    protected Object engineGetParameter(String str) {
        return null;
    }

    @Override // java.security.SignatureSpi
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    private BigInteger generateR(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return bigInteger3.modPow(bigInteger4.add(bigInteger2.multiply(new BigInteger(7, getSigningRandom()).add(BLINDING_CONSTANT))), bigInteger).mod(bigInteger2);
    }

    private BigInteger generateS(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) throws SignatureException {
        try {
            byte[] bArrDigest = this.md.digest();
            int iBitLength = bigInteger2.bitLength() / 8;
            if (iBitLength < bArrDigest.length) {
                bArrDigest = Arrays.copyOfRange(bArrDigest, 0, iBitLength);
            }
            BigInteger bigInteger5 = new BigInteger(1, bArrDigest);
            return bigInteger.multiply(bigInteger3).add(bigInteger5).multiply(bigInteger4.modInverse(bigInteger2)).mod(bigInteger2);
        } catch (RuntimeException e2) {
            throw new SignatureException(e2.getMessage());
        }
    }

    private BigInteger generateW(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return bigInteger4.modInverse(bigInteger2);
    }

    private BigInteger generateV(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6) throws SignatureException {
        try {
            byte[] bArrDigest = this.md.digest();
            int iBitLength = bigInteger3.bitLength() / 8;
            if (iBitLength < bArrDigest.length) {
                bArrDigest = Arrays.copyOfRange(bArrDigest, 0, iBitLength);
            }
            return bigInteger4.modPow(new BigInteger(1, bArrDigest).multiply(bigInteger5).mod(bigInteger3), bigInteger2).multiply(bigInteger.modPow(bigInteger6.multiply(bigInteger5).mod(bigInteger3), bigInteger2)).mod(bigInteger2).mod(bigInteger3);
        } catch (RuntimeException e2) {
            throw new SignatureException(e2.getMessage());
        }
    }

    protected BigInteger generateK(BigInteger bigInteger) {
        java.security.SecureRandom signingRandom = getSigningRandom();
        byte[] bArr = new byte[((bigInteger.bitLength() + 7) / 8) + 8];
        signingRandom.nextBytes(bArr);
        return new BigInteger(1, bArr).mod(bigInteger.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    protected java.security.SecureRandom getSigningRandom() {
        if (this.signingRandom == null) {
            if (this.appRandom != null) {
                this.signingRandom = this.appRandom;
            } else {
                this.signingRandom = JCAUtil.getSecureRandom();
            }
        }
        return this.signingRandom;
    }

    public String toString() {
        String str;
        if (this.presetP == null || this.presetQ == null || this.presetG == null) {
            str = "DSA Signature\n\t P, Q or G not initialized.";
        } else {
            str = (("DSA Signature\n\tp: " + Debug.toHexString(this.presetP)) + "\n\tq: " + Debug.toHexString(this.presetQ)) + "\n\tg: " + Debug.toHexString(this.presetG);
        }
        if (this.presetY != null) {
            str = str + "\n\ty: " + Debug.toHexString(this.presetY);
        }
        if (this.presetY == null && this.presetX == null) {
            str = str + "\n\tUNINIIALIZED";
        }
        return str;
    }

    /* loaded from: rt.jar:sun/security/provider/DSA$SHA224withDSA.class */
    public static final class SHA224withDSA extends DSA {
        @Override // sun.security.provider.DSA
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        public SHA224withDSA() throws NoSuchAlgorithmException {
            super(MessageDigest.getInstance("SHA-224"));
        }
    }

    /* loaded from: rt.jar:sun/security/provider/DSA$SHA256withDSA.class */
    public static final class SHA256withDSA extends DSA {
        @Override // sun.security.provider.DSA
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        public SHA256withDSA() throws NoSuchAlgorithmException {
            super(MessageDigest.getInstance("SHA-256"));
        }
    }

    /* loaded from: rt.jar:sun/security/provider/DSA$SHA1withDSA.class */
    public static final class SHA1withDSA extends DSA {
        @Override // sun.security.provider.DSA
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        public SHA1withDSA() throws NoSuchAlgorithmException {
            super(MessageDigest.getInstance("SHA-1"));
        }
    }

    /* loaded from: rt.jar:sun/security/provider/DSA$RawDSA.class */
    public static final class RawDSA extends DSA {
        @Override // sun.security.provider.DSA
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        /* loaded from: rt.jar:sun/security/provider/DSA$RawDSA$NullDigest20.class */
        public static final class NullDigest20 extends MessageDigest {
            private final byte[] digestBuffer;
            private int ofs;

            protected NullDigest20() {
                super("NullDigest20");
                this.digestBuffer = new byte[20];
                this.ofs = 0;
            }

            @Override // java.security.MessageDigestSpi
            protected void engineUpdate(byte b2) {
                if (this.ofs == this.digestBuffer.length) {
                    this.ofs = Integer.MAX_VALUE;
                    return;
                }
                byte[] bArr = this.digestBuffer;
                int i2 = this.ofs;
                this.ofs = i2 + 1;
                bArr[i2] = b2;
            }

            @Override // java.security.MessageDigestSpi
            protected void engineUpdate(byte[] bArr, int i2, int i3) {
                if (i3 > this.digestBuffer.length - this.ofs) {
                    this.ofs = Integer.MAX_VALUE;
                } else {
                    System.arraycopy(bArr, i2, this.digestBuffer, this.ofs, i3);
                    this.ofs += i3;
                }
            }

            @Override // java.security.MessageDigestSpi
            protected final void engineUpdate(ByteBuffer byteBuffer) {
                int iRemaining = byteBuffer.remaining();
                if (iRemaining > this.digestBuffer.length - this.ofs) {
                    this.ofs = Integer.MAX_VALUE;
                } else {
                    byteBuffer.get(this.digestBuffer, this.ofs, iRemaining);
                    this.ofs += iRemaining;
                }
            }

            @Override // java.security.MessageDigestSpi
            protected byte[] engineDigest() throws RuntimeException {
                if (this.ofs != this.digestBuffer.length) {
                    throw new RuntimeException("Data for RawDSA must be exactly 20 bytes long");
                }
                reset();
                return this.digestBuffer;
            }

            @Override // java.security.MessageDigestSpi
            protected int engineDigest(byte[] bArr, int i2, int i3) throws DigestException {
                if (this.ofs != this.digestBuffer.length) {
                    throw new DigestException("Data for RawDSA must be exactly 20 bytes long");
                }
                if (i3 < this.digestBuffer.length) {
                    throw new DigestException("Output buffer too small; must be at least 20 bytes");
                }
                System.arraycopy(this.digestBuffer, 0, bArr, i2, this.digestBuffer.length);
                reset();
                return this.digestBuffer.length;
            }

            @Override // java.security.MessageDigestSpi
            protected void engineReset() {
                this.ofs = 0;
            }

            @Override // java.security.MessageDigestSpi
            protected final int engineGetDigestLength() {
                return this.digestBuffer.length;
            }
        }

        public RawDSA() throws NoSuchAlgorithmException {
            super(new NullDigest20());
        }
    }
}
