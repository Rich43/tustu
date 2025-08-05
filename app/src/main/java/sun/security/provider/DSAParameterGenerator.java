package sun.security.provider;

import java.math.BigInteger;
import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAGenParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import sun.security.util.SecurityProviderConstants;

/* loaded from: rt.jar:sun/security/provider/DSAParameterGenerator.class */
public class DSAParameterGenerator extends AlgorithmParameterGeneratorSpi {
    private int valueL = -1;
    private int valueN = -1;
    private int seedLen = -1;
    private java.security.SecureRandom random;
    private static final BigInteger TWO = BigInteger.valueOf(2);

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected void engineInit(int i2, java.security.SecureRandom secureRandom) {
        if (i2 != 2048 && i2 != 3072 && (i2 < 512 || i2 > 1024 || i2 % 64 != 0)) {
            throw new InvalidParameterException("Unexpected strength (size of prime): " + i2 + ". Prime size should be 512-1024, 2048, or 3072");
        }
        this.valueL = i2;
        this.valueN = SecurityProviderConstants.getDefDSASubprimeSize(i2);
        this.seedLen = this.valueN;
        this.random = secureRandom;
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, java.security.SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof DSAGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Invalid parameter");
        }
        DSAGenParameterSpec dSAGenParameterSpec = (DSAGenParameterSpec) algorithmParameterSpec;
        this.valueL = dSAGenParameterSpec.getPrimePLength();
        this.valueN = dSAGenParameterSpec.getSubprimeQLength();
        this.seedLen = dSAGenParameterSpec.getSeedLength();
        this.random = secureRandom;
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected AlgorithmParameters engineGenerateParameters() {
        try {
            if (this.random == null) {
                this.random = new java.security.SecureRandom();
            }
            if (this.valueL == -1) {
                engineInit(SecurityProviderConstants.DEF_DSA_KEY_SIZE, this.random);
            }
            BigInteger[] bigIntegerArrGeneratePandQ = generatePandQ(this.random, this.valueL, this.valueN, this.seedLen);
            BigInteger bigInteger = bigIntegerArrGeneratePandQ[0];
            BigInteger bigInteger2 = bigIntegerArrGeneratePandQ[1];
            DSAParameterSpec dSAParameterSpec = new DSAParameterSpec(bigInteger, bigInteger2, generateG(bigInteger, bigInteger2));
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("DSA", "SUN");
            algorithmParameters.init(dSAParameterSpec);
            return algorithmParameters;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2.getMessage());
        } catch (NoSuchProviderException e3) {
            throw new RuntimeException(e3.getMessage());
        } catch (InvalidParameterSpecException e4) {
            throw new RuntimeException(e4.getMessage());
        }
    }

    private static BigInteger[] generatePandQ(java.security.SecureRandom secureRandom, int i2, int i3, int i4) {
        String str = null;
        if (i3 == 160) {
            str = "SHA";
        } else if (i3 == 224) {
            str = "SHA-224";
        } else if (i3 == 256) {
            str = "SHA-256";
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }
        int digestLength = messageDigest.getDigestLength() * 8;
        int i5 = (i2 - 1) / digestLength;
        int i6 = (i2 - 1) % digestLength;
        byte[] bArr = new byte[i4 / 8];
        BigInteger bigIntegerPow = TWO.pow(i4);
        int i7 = -1;
        if (i2 <= 1024) {
            i7 = 80;
        } else if (i2 == 2048) {
            i7 = 112;
        } else if (i2 == 3072) {
            i7 = 128;
        }
        if (i7 < 0) {
            throw new ProviderException("Invalid valueL: " + i2);
        }
        while (true) {
            secureRandom.nextBytes(bArr);
            BigInteger bigInteger = new BigInteger(1, bArr);
            BigInteger bigIntegerMod = new BigInteger(1, messageDigest.digest(bArr)).mod(TWO.pow(i3 - 1));
            BigInteger bigIntegerSubtract = TWO.pow(i3 - 1).add(bigIntegerMod).add(BigInteger.ONE).subtract(bigIntegerMod.mod(TWO));
            if (bigIntegerSubtract.isProbablePrime(i7)) {
                BigInteger bigIntegerAdd = BigInteger.ONE;
                for (int i8 = 0; i8 < 4 * i2; i8++) {
                    BigInteger[] bigIntegerArr = new BigInteger[i5 + 1];
                    for (int i9 = 0; i9 <= i5; i9++) {
                        bigIntegerArr[i9] = new BigInteger(1, messageDigest.digest(toByteArray(bigInteger.add(bigIntegerAdd).add(BigInteger.valueOf(i9)).mod(bigIntegerPow))));
                    }
                    BigInteger bigIntegerAdd2 = bigIntegerArr[0];
                    for (int i10 = 1; i10 < i5; i10++) {
                        bigIntegerAdd2 = bigIntegerAdd2.add(bigIntegerArr[i10].multiply(TWO.pow(i10 * digestLength)));
                    }
                    BigInteger bigIntegerAdd3 = bigIntegerAdd2.add(bigIntegerArr[i5].mod(TWO.pow(i6)).multiply(TWO.pow(i5 * digestLength)));
                    BigInteger bigIntegerPow2 = TWO.pow(i2 - 1);
                    BigInteger bigIntegerAdd4 = bigIntegerAdd3.add(bigIntegerPow2);
                    BigInteger bigIntegerSubtract2 = bigIntegerAdd4.subtract(bigIntegerAdd4.mod(bigIntegerSubtract.multiply(TWO)).subtract(BigInteger.ONE));
                    if (bigIntegerSubtract2.compareTo(bigIntegerPow2) > -1 && bigIntegerSubtract2.isProbablePrime(i7)) {
                        return new BigInteger[]{bigIntegerSubtract2, bigIntegerSubtract, bigInteger, BigInteger.valueOf(i8)};
                    }
                    bigIntegerAdd = bigIntegerAdd.add(BigInteger.valueOf(i5)).add(BigInteger.ONE);
                }
            }
        }
    }

    private static BigInteger generateG(BigInteger bigInteger, BigInteger bigInteger2) throws RuntimeException {
        BigInteger bigIntegerAdd = BigInteger.ONE;
        BigInteger bigIntegerDivide = bigInteger.subtract(BigInteger.ONE).divide(bigInteger2);
        BigInteger bigIntegerModPow = BigInteger.ONE;
        while (bigIntegerModPow.compareTo(TWO) < 0) {
            bigIntegerModPow = bigIntegerAdd.modPow(bigIntegerDivide, bigInteger);
            bigIntegerAdd = bigIntegerAdd.add(BigInteger.ONE);
        }
        return bigIntegerModPow;
    }

    private static byte[] toByteArray(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) {
            byte[] bArr = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, bArr, 0, bArr.length);
            byteArray = bArr;
        }
        return byteArray;
    }
}
