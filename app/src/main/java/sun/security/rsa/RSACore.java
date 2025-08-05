package sun.security.rsa;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import javax.crypto.BadPaddingException;
import sun.security.jca.JCAUtil;

/* loaded from: rt.jar:sun/security/rsa/RSACore.class */
public final class RSACore {
    private static final boolean ENABLE_BLINDING = true;
    private static final Map<BigInteger, BlindingParameters> blindingCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RSACore.class.desiredAssertionStatus();
        blindingCache = new WeakHashMap();
    }

    private RSACore() {
    }

    public static int getByteLength(BigInteger bigInteger) {
        return (bigInteger.bitLength() + 7) >> 3;
    }

    public static int getByteLength(RSAKey rSAKey) {
        return getByteLength(rSAKey.getModulus());
    }

    public static byte[] convert(byte[] bArr, int i2, int i3) {
        if (i2 == 0 && i3 == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i2, bArr2, 0, i3);
        return bArr2;
    }

    public static byte[] rsa(byte[] bArr, RSAPublicKey rSAPublicKey) throws BadPaddingException {
        return crypt(bArr, rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
    }

    @Deprecated
    public static byte[] rsa(byte[] bArr, RSAPrivateKey rSAPrivateKey) throws BadPaddingException {
        return rsa(bArr, rSAPrivateKey, true);
    }

    public static byte[] rsa(byte[] bArr, RSAPrivateKey rSAPrivateKey, boolean z2) throws BadPaddingException {
        if (rSAPrivateKey instanceof RSAPrivateCrtKey) {
            return crtCrypt(bArr, (RSAPrivateCrtKey) rSAPrivateKey, z2);
        }
        return priCrypt(bArr, rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent());
    }

    private static byte[] crypt(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) throws BadPaddingException {
        return toByteArray(parseMsg(bArr, bigInteger).modPow(bigInteger2, bigInteger), getByteLength(bigInteger));
    }

    private static byte[] priCrypt(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2) throws BadPaddingException {
        BigInteger msg = parseMsg(bArr, bigInteger);
        BlindingRandomPair blindingRandomPair = getBlindingRandomPair(null, bigInteger2, bigInteger);
        return toByteArray(msg.multiply(blindingRandomPair.f13650u).mod(bigInteger).modPow(bigInteger2, bigInteger).multiply(blindingRandomPair.f13651v).mod(bigInteger), getByteLength(bigInteger));
    }

    private static byte[] crtCrypt(byte[] bArr, RSAPrivateCrtKey rSAPrivateCrtKey, boolean z2) throws BadPaddingException, RuntimeException {
        BigInteger modulus = rSAPrivateCrtKey.getModulus();
        BigInteger msg = parseMsg(bArr, modulus);
        BigInteger primeP = rSAPrivateCrtKey.getPrimeP();
        BigInteger primeQ = rSAPrivateCrtKey.getPrimeQ();
        BigInteger primeExponentP = rSAPrivateCrtKey.getPrimeExponentP();
        BigInteger primeExponentQ = rSAPrivateCrtKey.getPrimeExponentQ();
        BigInteger crtCoefficient = rSAPrivateCrtKey.getCrtCoefficient();
        BigInteger publicExponent = rSAPrivateCrtKey.getPublicExponent();
        BlindingRandomPair blindingRandomPair = getBlindingRandomPair(publicExponent, rSAPrivateCrtKey.getPrivateExponent(), modulus);
        BigInteger bigIntegerMod = msg.multiply(blindingRandomPair.f13650u).mod(modulus);
        BigInteger bigIntegerModPow = bigIntegerMod.modPow(primeExponentP, primeP);
        BigInteger bigIntegerModPow2 = bigIntegerMod.modPow(primeExponentQ, primeQ);
        BigInteger bigIntegerSubtract = bigIntegerModPow.subtract(bigIntegerModPow2);
        if (bigIntegerSubtract.signum() < 0) {
            bigIntegerSubtract = bigIntegerSubtract.add(primeP);
        }
        BigInteger bigIntegerMod2 = bigIntegerSubtract.multiply(crtCoefficient).mod(primeP).multiply(primeQ).add(bigIntegerModPow2).multiply(blindingRandomPair.f13651v).mod(modulus);
        if (z2 && !msg.equals(bigIntegerMod2.modPow(publicExponent, modulus))) {
            throw new BadPaddingException("RSA private key operation failed");
        }
        return toByteArray(bigIntegerMod2, getByteLength(modulus));
    }

    private static BigInteger parseMsg(byte[] bArr, BigInteger bigInteger) throws BadPaddingException {
        BigInteger bigInteger2 = new BigInteger(1, bArr);
        if (bigInteger2.compareTo(bigInteger) >= 0) {
            throw new BadPaddingException("Message is larger than modulus");
        }
        return bigInteger2;
    }

    private static byte[] toByteArray(BigInteger bigInteger, int i2) {
        byte[] byteArray = bigInteger.toByteArray();
        int length = byteArray.length;
        if (length == i2) {
            return byteArray;
        }
        if (length == i2 + 1 && byteArray[0] == 0) {
            byte[] bArr = new byte[i2];
            System.arraycopy(byteArray, 1, bArr, 0, i2);
            Arrays.fill(byteArray, (byte) 0);
            return bArr;
        }
        if (!$assertionsDisabled && length >= i2) {
            throw new AssertionError();
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(byteArray, 0, bArr2, i2 - length, length);
        Arrays.fill(byteArray, (byte) 0);
        return bArr2;
    }

    /* loaded from: rt.jar:sun/security/rsa/RSACore$BlindingRandomPair.class */
    private static final class BlindingRandomPair {

        /* renamed from: u, reason: collision with root package name */
        final BigInteger f13650u;

        /* renamed from: v, reason: collision with root package name */
        final BigInteger f13651v;

        BlindingRandomPair(BigInteger bigInteger, BigInteger bigInteger2) {
            this.f13650u = bigInteger;
            this.f13651v = bigInteger2;
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSACore$BlindingParameters.class */
    private static final class BlindingParameters {
        private static final BigInteger BIG_TWO = BigInteger.valueOf(2);

        /* renamed from: e, reason: collision with root package name */
        private final BigInteger f13646e;

        /* renamed from: d, reason: collision with root package name */
        private final BigInteger f13647d;

        /* renamed from: u, reason: collision with root package name */
        private BigInteger f13648u;

        /* renamed from: v, reason: collision with root package name */
        private BigInteger f13649v;

        BlindingParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            this.f13648u = null;
            this.f13649v = null;
            this.f13646e = bigInteger;
            this.f13647d = bigInteger2;
            this.f13648u = new BigInteger(bigInteger3.bitLength(), JCAUtil.getSecureRandom()).mod(bigInteger3);
            if (this.f13648u.equals(BigInteger.ZERO)) {
                this.f13648u = BigInteger.ONE;
            }
            try {
                this.f13649v = this.f13648u.modInverse(bigInteger3);
            } catch (ArithmeticException e2) {
                this.f13648u = BigInteger.ONE;
                this.f13649v = BigInteger.ONE;
            }
            if (bigInteger != null) {
                this.f13648u = this.f13648u.modPow(bigInteger, bigInteger3);
            } else {
                this.f13649v = this.f13649v.modPow(bigInteger2, bigInteger3);
            }
        }

        BlindingRandomPair getBlindingRandomPair(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            if ((this.f13646e != null && this.f13646e.equals(bigInteger)) || (this.f13647d != null && this.f13647d.equals(bigInteger2))) {
                BlindingRandomPair blindingRandomPair = null;
                synchronized (this) {
                    if (!this.f13648u.equals(BigInteger.ZERO) && !this.f13649v.equals(BigInteger.ZERO)) {
                        blindingRandomPair = new BlindingRandomPair(this.f13648u, this.f13649v);
                        if (this.f13648u.compareTo(BigInteger.ONE) <= 0 || this.f13649v.compareTo(BigInteger.ONE) <= 0) {
                            this.f13648u = BigInteger.ZERO;
                            this.f13649v = BigInteger.ZERO;
                        } else {
                            this.f13648u = this.f13648u.modPow(BIG_TWO, bigInteger3);
                            this.f13649v = this.f13649v.modPow(BIG_TWO, bigInteger3);
                        }
                    }
                }
                return blindingRandomPair;
            }
            return null;
        }
    }

    private static BlindingRandomPair getBlindingRandomPair(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        BlindingParameters blindingParameters;
        synchronized (blindingCache) {
            blindingParameters = blindingCache.get(bigInteger3);
        }
        if (blindingParameters == null) {
            blindingParameters = new BlindingParameters(bigInteger, bigInteger2, bigInteger3);
            synchronized (blindingCache) {
                blindingCache.putIfAbsent(bigInteger3, blindingParameters);
            }
        }
        BlindingRandomPair blindingRandomPair = blindingParameters.getBlindingRandomPair(bigInteger, bigInteger2, bigInteger3);
        if (blindingRandomPair == null) {
            BlindingParameters blindingParameters2 = new BlindingParameters(bigInteger, bigInteger2, bigInteger3);
            synchronized (blindingCache) {
                blindingCache.replace(bigInteger3, blindingParameters2);
            }
            blindingRandomPair = blindingParameters2.getBlindingRandomPair(bigInteger, bigInteger2, bigInteger3);
        }
        return blindingRandomPair;
    }
}
