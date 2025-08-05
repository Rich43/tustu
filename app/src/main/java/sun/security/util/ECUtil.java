package sun.security.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/* loaded from: rt.jar:sun/security/util/ECUtil.class */
public final class ECUtil {
    public static ECPoint decodePoint(byte[] bArr, EllipticCurve ellipticCurve) throws IOException {
        if (bArr.length == 0 || bArr[0] != 4) {
            throw new IOException("Only uncompressed point format supported");
        }
        int length = (bArr.length - 1) / 2;
        if (length != ((ellipticCurve.getField().getFieldSize() + 7) >> 3)) {
            throw new IOException("Point does not match field size");
        }
        return new ECPoint(new BigInteger(1, Arrays.copyOfRange(bArr, 1, 1 + length)), new BigInteger(1, Arrays.copyOfRange(bArr, length + 1, length + 1 + length)));
    }

    public static byte[] encodePoint(ECPoint eCPoint, EllipticCurve ellipticCurve) {
        int fieldSize = (ellipticCurve.getField().getFieldSize() + 7) >> 3;
        byte[] bArrTrimZeroes = trimZeroes(eCPoint.getAffineX().toByteArray());
        byte[] bArrTrimZeroes2 = trimZeroes(eCPoint.getAffineY().toByteArray());
        if (bArrTrimZeroes.length > fieldSize || bArrTrimZeroes2.length > fieldSize) {
            throw new RuntimeException("Point coordinates do not match field size");
        }
        byte[] bArr = new byte[1 + (fieldSize << 1)];
        bArr[0] = 4;
        System.arraycopy(bArrTrimZeroes, 0, bArr, (fieldSize - bArrTrimZeroes.length) + 1, bArrTrimZeroes.length);
        System.arraycopy(bArrTrimZeroes2, 0, bArr, bArr.length - bArrTrimZeroes2.length, bArrTrimZeroes2.length);
        return bArr;
    }

    public static byte[] trimZeroes(byte[] bArr) {
        int i2 = 0;
        while (i2 < bArr.length - 1 && bArr[i2] == 0) {
            i2++;
        }
        if (i2 == 0) {
            return bArr;
        }
        return Arrays.copyOfRange(bArr, i2, bArr.length);
    }

    public static AlgorithmParameters getECParameters(Provider provider) {
        try {
            if (provider != null) {
                return AlgorithmParameters.getInstance("EC", provider);
            }
            return AlgorithmParameters.getInstance("EC");
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static byte[] encodeECParameterSpec(Provider provider, ECParameterSpec eCParameterSpec) {
        AlgorithmParameters eCParameters = getECParameters(provider);
        try {
            eCParameters.init(eCParameterSpec);
            try {
                return eCParameters.getEncoded();
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        } catch (InvalidParameterSpecException e3) {
            throw new RuntimeException("Not a known named curve: " + ((Object) eCParameterSpec));
        }
    }

    public static ECParameterSpec getECParameterSpec(Provider provider, ECParameterSpec eCParameterSpec) {
        AlgorithmParameters eCParameters = getECParameters(provider);
        try {
            eCParameters.init(eCParameterSpec);
            return (ECParameterSpec) eCParameters.getParameterSpec(ECParameterSpec.class);
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    public static ECParameterSpec getECParameterSpec(Provider provider, byte[] bArr) throws IOException {
        AlgorithmParameters eCParameters = getECParameters(provider);
        eCParameters.init(bArr);
        try {
            return (ECParameterSpec) eCParameters.getParameterSpec(ECParameterSpec.class);
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    public static ECParameterSpec getECParameterSpec(Provider provider, String str) {
        AlgorithmParameters eCParameters = getECParameters(provider);
        try {
            eCParameters.init(new ECGenParameterSpec(str));
            return (ECParameterSpec) eCParameters.getParameterSpec(ECParameterSpec.class);
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    public static ECParameterSpec getECParameterSpec(Provider provider, int i2) {
        AlgorithmParameters eCParameters = getECParameters(provider);
        try {
            eCParameters.init(new ECKeySizeParameterSpec(i2));
            return (ECParameterSpec) eCParameters.getParameterSpec(ECParameterSpec.class);
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    public static String getCurveName(Provider provider, ECParameterSpec eCParameterSpec) {
        AlgorithmParameters eCParameters = getECParameters(provider);
        try {
            eCParameters.init(eCParameterSpec);
            ECGenParameterSpec eCGenParameterSpec = (ECGenParameterSpec) eCParameters.getParameterSpec(ECGenParameterSpec.class);
            if (eCGenParameterSpec == null) {
                return null;
            }
            return eCGenParameterSpec.getName();
        } catch (InvalidParameterSpecException e2) {
            return null;
        }
    }

    public static boolean equals(ECParameterSpec eCParameterSpec, ECParameterSpec eCParameterSpec2) {
        if (eCParameterSpec == eCParameterSpec2) {
            return true;
        }
        return eCParameterSpec != null && eCParameterSpec2 != null && eCParameterSpec.getCofactor() == eCParameterSpec2.getCofactor() && eCParameterSpec.getOrder().equals(eCParameterSpec2.getOrder()) && eCParameterSpec.getCurve().equals(eCParameterSpec2.getCurve()) && eCParameterSpec.getGenerator().equals(eCParameterSpec2.getGenerator());
    }

    public static byte[] encodeSignature(byte[] bArr) throws SignatureException {
        try {
            int length = bArr.length >> 1;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 0, bArr2, 0, length);
            BigInteger bigInteger = new BigInteger(1, bArr2);
            System.arraycopy(bArr, length, bArr2, 0, length);
            BigInteger bigInteger2 = new BigInteger(1, bArr2);
            DerOutputStream derOutputStream = new DerOutputStream(bArr.length + 10);
            derOutputStream.putInteger(bigInteger);
            derOutputStream.putInteger(bigInteger2);
            return new DerValue((byte) 48, derOutputStream.toByteArray()).toByteArray();
        } catch (Exception e2) {
            throw new SignatureException("Could not encode signature", e2);
        }
    }

    public static byte[] decodeSignature(byte[] bArr) throws SignatureException, IOException {
        try {
            DerInputStream derInputStream = new DerInputStream(bArr, 0, bArr.length, false);
            DerValue[] sequence = derInputStream.getSequence(2);
            if (sequence.length != 2 || derInputStream.available() != 0) {
                throw new IOException("Invalid encoding for signature");
            }
            BigInteger positiveBigInteger = sequence[0].getPositiveBigInteger();
            BigInteger positiveBigInteger2 = sequence[1].getPositiveBigInteger();
            byte[] bArrTrimZeroes = trimZeroes(positiveBigInteger.toByteArray());
            byte[] bArrTrimZeroes2 = trimZeroes(positiveBigInteger2.toByteArray());
            int iMax = Math.max(bArrTrimZeroes.length, bArrTrimZeroes2.length);
            byte[] bArr2 = new byte[iMax << 1];
            System.arraycopy(bArrTrimZeroes, 0, bArr2, iMax - bArrTrimZeroes.length, bArrTrimZeroes.length);
            System.arraycopy(bArrTrimZeroes2, 0, bArr2, bArr2.length - bArrTrimZeroes2.length, bArrTrimZeroes2.length);
            return bArr2;
        } catch (Exception e2) {
            throw new SignatureException("Invalid encoding for signature", e2);
        }
    }

    private ECUtil() {
    }
}
