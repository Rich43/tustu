package java.math;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.ObjectStreamField;
import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.Random;
import sun.misc.DoubleConsts;
import sun.misc.FloatConsts;
import sun.misc.Unsafe;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: rt.jar:java/math/BigInteger.class */
public class BigInteger extends Number implements Comparable<BigInteger> {
    final int signum;
    final int[] mag;

    @Deprecated
    private int bitCount;

    @Deprecated
    private int bitLength;

    @Deprecated
    private int lowestSetBit;

    @Deprecated
    private int firstNonzeroIntNum;
    static final long LONG_MASK = 4294967295L;
    private static final int MAX_MAG_LENGTH = 67108864;
    private static final int PRIME_SEARCH_BIT_LENGTH_LIMIT = 500000000;
    private static final int KARATSUBA_THRESHOLD = 80;
    private static final int TOOM_COOK_THRESHOLD = 240;
    private static final int KARATSUBA_SQUARE_THRESHOLD = 128;
    private static final int TOOM_COOK_SQUARE_THRESHOLD = 216;
    static final int BURNIKEL_ZIEGLER_THRESHOLD = 80;
    static final int BURNIKEL_ZIEGLER_OFFSET = 40;
    private static final int SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20;
    private static final int MULTIPLY_SQUARE_THRESHOLD = 20;
    private static final int MONTGOMERY_INTRINSIC_THRESHOLD = 512;
    private static long[] bitsPerDigit;
    private static final int SMALL_PRIME_THRESHOLD = 95;
    private static final int DEFAULT_PRIME_CERTAINTY = 100;
    private static final BigInteger SMALL_PRIME_PRODUCT;
    private static final int MAX_CONSTANT = 16;
    private static BigInteger[] posConst;
    private static BigInteger[] negConst;
    private static volatile BigInteger[][] powerCache;
    private static final double[] logCache;
    private static final double LOG_TWO;
    public static final BigInteger ZERO;
    public static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger NEGATIVE_ONE;
    public static final BigInteger TEN;
    static int[] bnExpModThreshTable;
    private static String[] zeros;
    private static int[] digitsPerLong;
    private static BigInteger[] longRadix;
    private static int[] digitsPerInt;
    private static int[] intRadix;
    private static final long serialVersionUID = -8287574255936472291L;
    private static final ObjectStreamField[] serialPersistentFields;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Type inference failed for: r0v17, types: [java.math.BigInteger[], java.math.BigInteger[][]] */
    static {
        $assertionsDisabled = !BigInteger.class.desiredAssertionStatus();
        bitsPerDigit = new long[]{0, 0, 1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672, 3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633, 4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210, 5253, 5295};
        SMALL_PRIME_PRODUCT = valueOf(152125131763605L);
        posConst = new BigInteger[17];
        negConst = new BigInteger[17];
        LOG_TWO = Math.log(2.0d);
        for (int i2 = 1; i2 <= 16; i2++) {
            int[] iArr = {i2};
            posConst[i2] = new BigInteger(iArr, 1);
            negConst[i2] = new BigInteger(iArr, -1);
        }
        powerCache = new BigInteger[37];
        logCache = new double[37];
        for (int i3 = 2; i3 <= 36; i3++) {
            powerCache[i3] = new BigInteger[]{valueOf(i3)};
            logCache[i3] = Math.log(i3);
        }
        ZERO = new BigInteger(new int[0], 0);
        ONE = valueOf(1L);
        TWO = valueOf(2L);
        NEGATIVE_ONE = valueOf(-1L);
        TEN = valueOf(10L);
        bnExpModThreshTable = new int[]{7, 25, 81, 241, 673, 1793, Integer.MAX_VALUE};
        zeros = new String[64];
        zeros[63] = "000000000000000000000000000000000000000000000000000000000000000";
        for (int i4 = 0; i4 < 63; i4++) {
            zeros[i4] = zeros[63].substring(0, i4);
        }
        digitsPerLong = new int[]{0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12};
        longRadix = new BigInteger[]{null, null, valueOf(4611686018427387904L), valueOf(4052555153018976267L), valueOf(4611686018427387904L), valueOf(7450580596923828125L), valueOf(4738381338321616896L), valueOf(3909821048582988049L), valueOf(1152921504606846976L), valueOf(1350851717672992089L), valueOf(1000000000000000000L), valueOf(5559917313492231481L), valueOf(2218611106740436992L), valueOf(8650415919381337933L), valueOf(2177953337809371136L), valueOf(6568408355712890625L), valueOf(1152921504606846976L), valueOf(2862423051509815793L), valueOf(6746640616477458432L), valueOf(799006685782884121L), valueOf(1638400000000000000L), valueOf(3243919932521508681L), valueOf(6221821273427820544L), valueOf(504036361936467383L), valueOf(876488338465357824L), valueOf(1490116119384765625L), valueOf(2481152873203736576L), valueOf(4052555153018976267L), valueOf(6502111422497947648L), valueOf(353814783205469041L), valueOf(531441000000000000L), valueOf(787662783788549761L), valueOf(1152921504606846976L), valueOf(1667889514952984961L), valueOf(2386420683693101056L), valueOf(3379220508056640625L), valueOf(4738381338321616896L)};
        digitsPerInt = new int[]{0, 0, 30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};
        intRadix = new int[]{0, 0, 1073741824, 1162261467, 1073741824, 1220703125, 362797056, 1977326743, 1073741824, 387420489, 1000000000, 214358881, 429981696, 815730721, 1475789056, 170859375, 268435456, 410338673, 612220032, 893871739, 1280000000, 1801088541, 113379904, 148035889, 191102976, 244140625, 308915776, 387420489, 481890304, 594823321, 729000000, 887503681, 1073741824, 1291467969, 1544804416, 1838265625, 60466176};
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("signum", Integer.TYPE), new ObjectStreamField("magnitude", byte[].class), new ObjectStreamField("bitCount", Integer.TYPE), new ObjectStreamField("bitLength", Integer.TYPE), new ObjectStreamField("firstNonzeroByteNum", Integer.TYPE), new ObjectStreamField("lowestSetBit", Integer.TYPE)};
    }

    public BigInteger(byte[] bArr) {
        if (bArr.length == 0) {
            throw new NumberFormatException("Zero length BigInteger");
        }
        if (bArr[0] < 0) {
            this.mag = makePositive(bArr);
            this.signum = -1;
        } else {
            this.mag = stripLeadingZeroBytes(bArr);
            this.signum = this.mag.length == 0 ? 0 : 1;
        }
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    private BigInteger(int[] iArr) {
        if (iArr.length == 0) {
            throw new NumberFormatException("Zero length BigInteger");
        }
        if (iArr[0] < 0) {
            this.mag = makePositive(iArr);
            this.signum = -1;
        } else {
            this.mag = trustedStripLeadingZeroInts(iArr);
            this.signum = this.mag.length == 0 ? 0 : 1;
        }
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    public BigInteger(int i2, byte[] bArr) {
        this.mag = stripLeadingZeroBytes(bArr);
        if (i2 < -1 || i2 > 1) {
            throw new NumberFormatException("Invalid signum value");
        }
        if (this.mag.length == 0) {
            this.signum = 0;
        } else {
            if (i2 == 0) {
                throw new NumberFormatException("signum-magnitude mismatch");
            }
            this.signum = i2;
        }
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    private BigInteger(int i2, int[] iArr) {
        this.mag = stripLeadingZeroInts(iArr);
        if (i2 < -1 || i2 > 1) {
            throw new NumberFormatException("Invalid signum value");
        }
        if (this.mag.length == 0) {
            this.signum = 0;
        } else {
            if (i2 == 0) {
                throw new NumberFormatException("signum-magnitude mismatch");
            }
            this.signum = i2;
        }
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    public BigInteger(String str, int i2) throws NumberFormatException {
        int i3 = 0;
        int length = str.length();
        if (i2 < 2 || i2 > 36) {
            throw new NumberFormatException("Radix out of range");
        }
        if (length == 0) {
            throw new NumberFormatException("Zero length BigInteger");
        }
        int i4 = 1;
        int iLastIndexOf = str.lastIndexOf(45);
        int iLastIndexOf2 = str.lastIndexOf(43);
        if (iLastIndexOf >= 0) {
            if (iLastIndexOf != 0 || iLastIndexOf2 >= 0) {
                throw new NumberFormatException("Illegal embedded sign character");
            }
            i4 = -1;
            i3 = 1;
        } else if (iLastIndexOf2 >= 0) {
            if (iLastIndexOf2 != 0) {
                throw new NumberFormatException("Illegal embedded sign character");
            }
            i3 = 1;
        }
        if (i3 == length) {
            throw new NumberFormatException("Zero length BigInteger");
        }
        while (i3 < length && Character.digit(str.charAt(i3), i2) == 0) {
            i3++;
        }
        if (i3 == length) {
            this.signum = 0;
            this.mag = ZERO.mag;
            return;
        }
        int i5 = length - i3;
        this.signum = i4;
        long j2 = ((i5 * bitsPerDigit[i2]) >>> 10) + 1;
        if (j2 + 31 >= EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH) {
            reportOverflow();
        }
        int i6 = ((int) (j2 + 31)) >>> 5;
        int[] iArr = new int[i6];
        int i7 = i5 % digitsPerInt[i2];
        int i8 = i3;
        int i9 = i3 + (i7 == 0 ? digitsPerInt[i2] : i7);
        int i10 = i9;
        iArr[i6 - 1] = Integer.parseInt(str.substring(i8, i9), i2);
        if (iArr[i6 - 1] < 0) {
            throw new NumberFormatException("Illegal digit");
        }
        int i11 = intRadix[i2];
        while (i10 < length) {
            int i12 = i10;
            int i13 = i10 + digitsPerInt[i2];
            i10 = i13;
            int i14 = Integer.parseInt(str.substring(i12, i13), i2);
            if (i14 < 0) {
                throw new NumberFormatException("Illegal digit");
            }
            destructiveMulAdd(iArr, i11, i14);
        }
        this.mag = trustedStripLeadingZeroInts(iArr);
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    BigInteger(char[] cArr, int i2, int i3) {
        int i4;
        int i5 = 0;
        while (i5 < i3 && Character.digit(cArr[i5], 10) == 0) {
            i5++;
        }
        if (i5 == i3) {
            this.signum = 0;
            this.mag = ZERO.mag;
            return;
        }
        int i6 = i3 - i5;
        this.signum = i2;
        if (i3 < 10) {
            i4 = 1;
        } else {
            long j2 = ((i6 * bitsPerDigit[10]) >>> 10) + 1;
            if (j2 + 31 >= EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH) {
                reportOverflow();
            }
            i4 = ((int) (j2 + 31)) >>> 5;
        }
        int[] iArr = new int[i4];
        int i7 = i6 % digitsPerInt[10];
        int i8 = i5;
        int i9 = i5 + (i7 == 0 ? digitsPerInt[10] : i7);
        int i10 = i9;
        iArr[i4 - 1] = parseInt(cArr, i8, i9);
        while (i10 < i3) {
            int i11 = i10;
            int i12 = i10 + digitsPerInt[10];
            i10 = i12;
            destructiveMulAdd(iArr, intRadix[10], parseInt(cArr, i11, i12));
        }
        this.mag = trustedStripLeadingZeroInts(iArr);
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    private int parseInt(char[] cArr, int i2, int i3) {
        int i4 = i2 + 1;
        int iDigit = Character.digit(cArr[i2], 10);
        if (iDigit == -1) {
            throw new NumberFormatException(new String(cArr));
        }
        for (int i5 = i4; i5 < i3; i5++) {
            int iDigit2 = Character.digit(cArr[i5], 10);
            if (iDigit2 == -1) {
                throw new NumberFormatException(new String(cArr));
            }
            iDigit = (10 * iDigit) + iDigit2;
        }
        return iDigit;
    }

    private static void destructiveMulAdd(int[] iArr, int i2, int i3) {
        long j2 = i2 & 4294967295L;
        long j3 = i3 & 4294967295L;
        int length = iArr.length;
        long j4 = 0;
        for (int i4 = length - 1; i4 >= 0; i4--) {
            long j5 = (j2 * (iArr[i4] & 4294967295L)) + j4;
            iArr[i4] = (int) j5;
            j4 = j5 >>> 32;
        }
        long j6 = (iArr[length - 1] & 4294967295L) + j3;
        iArr[length - 1] = (int) j6;
        long j7 = j6 >>> 32;
        for (int i5 = length - 2; i5 >= 0; i5--) {
            long j8 = (iArr[i5] & 4294967295L) + j7;
            iArr[i5] = (int) j8;
            j7 = j8 >>> 32;
        }
    }

    public BigInteger(String str) {
        this(str, 10);
    }

    public BigInteger(int i2, Random random) {
        this(1, randomBits(i2, random));
    }

    private static byte[] randomBits(int i2, Random random) {
        if (i2 < 0) {
            throw new IllegalArgumentException("numBits must be non-negative");
        }
        int i3 = (int) ((i2 + 7) / 8);
        byte[] bArr = new byte[i3];
        if (i3 > 0) {
            random.nextBytes(bArr);
            bArr[0] = (byte) (bArr[0] & ((1 << (8 - ((8 * i3) - i2))) - 1));
        }
        return bArr;
    }

    public BigInteger(int i2, int i3, Random random) {
        BigInteger bigIntegerLargePrime;
        if (i2 < 2) {
            throw new ArithmeticException("bitLength < 2");
        }
        if (i2 < 95) {
            bigIntegerLargePrime = smallPrime(i2, i3, random);
        } else {
            bigIntegerLargePrime = largePrime(i2, i3, random);
        }
        this.signum = 1;
        this.mag = bigIntegerLargePrime.mag;
    }

    public static BigInteger probablePrime(int i2, Random random) {
        if (i2 < 2) {
            throw new ArithmeticException("bitLength < 2");
        }
        if (i2 < 95) {
            return smallPrime(i2, 100, random);
        }
        return largePrime(i2, 100, random);
    }

    private static BigInteger smallPrime(int i2, int i3, Random random) {
        int i4 = (i2 + 31) >>> 5;
        int[] iArr = new int[i4];
        int i5 = 1 << ((i2 + 31) & 31);
        int i6 = (i5 << 1) - 1;
        while (true) {
            for (int i7 = 0; i7 < i4; i7++) {
                iArr[i7] = random.nextInt();
            }
            iArr[0] = (iArr[0] & i6) | i5;
            if (i2 > 2) {
                int i8 = i4 - 1;
                iArr[i8] = iArr[i8] | 1;
            }
            BigInteger bigInteger = new BigInteger(iArr, 1);
            if (i2 > 6) {
                long jLongValue = bigInteger.remainder(SMALL_PRIME_PRODUCT).longValue();
                if (jLongValue % 3 == 0 || jLongValue % 5 == 0 || jLongValue % 7 == 0 || jLongValue % 11 == 0 || jLongValue % 13 == 0 || jLongValue % 17 == 0 || jLongValue % 19 == 0 || jLongValue % 23 == 0 || jLongValue % 29 == 0 || jLongValue % 31 == 0 || jLongValue % 37 == 0 || jLongValue % 41 == 0) {
                }
            }
            if (i2 < 4) {
                return bigInteger;
            }
            if (bigInteger.primeToCertainty(i3, random)) {
                return bigInteger;
            }
        }
    }

    private static BigInteger largePrime(int i2, int i3, Random random) {
        BigInteger bit = new BigInteger(i2, random).setBit(i2 - 1);
        int[] iArr = bit.mag;
        int length = bit.mag.length - 1;
        iArr[length] = iArr[length] & (-2);
        int primeSearchLen = getPrimeSearchLen(i2);
        BigInteger bigIntegerRetrieve = new BitSieve(bit, primeSearchLen).retrieve(bit, i3, random);
        while (true) {
            BigInteger bigInteger = bigIntegerRetrieve;
            if (bigInteger == null || bigInteger.bitLength() != i2) {
                bit = bit.add(valueOf(2 * primeSearchLen));
                if (bit.bitLength() != i2) {
                    bit = new BigInteger(i2, random).setBit(i2 - 1);
                }
                int[] iArr2 = bit.mag;
                int length2 = bit.mag.length - 1;
                iArr2[length2] = iArr2[length2] & (-2);
                bigIntegerRetrieve = new BitSieve(bit, primeSearchLen).retrieve(bit, i3, random);
            } else {
                return bigInteger;
            }
        }
    }

    public BigInteger nextProbablePrime() {
        if (this.signum < 0) {
            throw new ArithmeticException("start < 0: " + ((Object) this));
        }
        if (this.signum == 0 || equals(ONE)) {
            return TWO;
        }
        BigInteger bigIntegerAdd = add(ONE);
        if (bigIntegerAdd.bitLength() < 95) {
            if (!bigIntegerAdd.testBit(0)) {
                bigIntegerAdd = bigIntegerAdd.add(ONE);
            }
            while (true) {
                if (bigIntegerAdd.bitLength() > 6) {
                    long jLongValue = bigIntegerAdd.remainder(SMALL_PRIME_PRODUCT).longValue();
                    if (jLongValue % 3 == 0 || jLongValue % 5 == 0 || jLongValue % 7 == 0 || jLongValue % 11 == 0 || jLongValue % 13 == 0 || jLongValue % 17 == 0 || jLongValue % 19 == 0 || jLongValue % 23 == 0 || jLongValue % 29 == 0 || jLongValue % 31 == 0 || jLongValue % 37 == 0 || jLongValue % 41 == 0) {
                        bigIntegerAdd = bigIntegerAdd.add(TWO);
                    }
                }
                if (bigIntegerAdd.bitLength() < 4) {
                    return bigIntegerAdd;
                }
                if (bigIntegerAdd.primeToCertainty(100, null)) {
                    return bigIntegerAdd;
                }
                bigIntegerAdd = bigIntegerAdd.add(TWO);
            }
        } else {
            if (bigIntegerAdd.testBit(0)) {
                bigIntegerAdd = bigIntegerAdd.subtract(ONE);
            }
            int primeSearchLen = getPrimeSearchLen(bigIntegerAdd.bitLength());
            while (true) {
                BigInteger bigIntegerRetrieve = new BitSieve(bigIntegerAdd, primeSearchLen).retrieve(bigIntegerAdd, 100, null);
                if (bigIntegerRetrieve != null) {
                    return bigIntegerRetrieve;
                }
                bigIntegerAdd = bigIntegerAdd.add(valueOf(2 * primeSearchLen));
            }
        }
    }

    private static int getPrimeSearchLen(int i2) {
        if (i2 > 500000001) {
            throw new ArithmeticException("Prime search implementation restriction on bitLength");
        }
        return (i2 / 20) * 64;
    }

    boolean primeToCertainty(int i2, Random random) {
        int i3;
        int iMin = (Math.min(i2, 2147483646) + 1) / 2;
        int iBitLength = bitLength();
        if (iBitLength < 100) {
            return passesMillerRabin(iMin < 50 ? iMin : 50, random);
        }
        if (iBitLength < 256) {
            i3 = 27;
        } else if (iBitLength < 512) {
            i3 = 15;
        } else if (iBitLength < 768) {
            i3 = 8;
        } else if (iBitLength < 1024) {
            i3 = 4;
        } else {
            i3 = 2;
        }
        return passesMillerRabin(iMin < i3 ? iMin : i3, random) && passesLucasLehmer();
    }

    private boolean passesLucasLehmer() {
        BigInteger bigIntegerAdd = add(ONE);
        int iAbs = 5;
        while (true) {
            int i2 = iAbs;
            if (jacobiSymbol(i2, this) != -1) {
                iAbs = i2 < 0 ? Math.abs(i2) + 2 : -(i2 + 2);
            } else {
                return lucasLehmerSequence(i2, bigIntegerAdd, this).mod(this).equals(ZERO);
            }
        }
    }

    private static int jacobiSymbol(int i2, BigInteger bigInteger) {
        if (i2 == 0) {
            return 0;
        }
        int i3 = 1;
        int i4 = bigInteger.mag[bigInteger.mag.length - 1];
        if (i2 < 0) {
            i2 = -i2;
            int i5 = i4 & 7;
            if (i5 == 3 || i5 == 7) {
                i3 = -1;
            }
        }
        while ((i2 & 3) == 0) {
            i2 >>= 2;
        }
        if ((i2 & 1) == 0) {
            i2 >>= 1;
            if (((i4 ^ (i4 >> 1)) & 2) != 0) {
                i3 = -i3;
            }
        }
        if (i2 == 1) {
            return i3;
        }
        if ((i2 & i4 & 2) != 0) {
            i3 = -i3;
        }
        int iIntValue = bigInteger.mod(valueOf(i2)).intValue();
        while (true) {
            int i6 = iIntValue;
            if (i6 != 0) {
                while ((i6 & 3) == 0) {
                    i6 >>= 2;
                }
                if ((i6 & 1) == 0) {
                    i6 >>= 1;
                    if (((i2 ^ (i2 >> 1)) & 2) != 0) {
                        i3 = -i3;
                    }
                }
                if (i6 == 1) {
                    return i3;
                }
                if (!$assertionsDisabled && i6 >= i2) {
                    throw new AssertionError();
                }
                int i7 = i2;
                i2 = i6;
                if ((i7 & i2 & 2) != 0) {
                    i3 = -i3;
                }
                iIntValue = i7 % i2;
            } else {
                return 0;
            }
        }
    }

    private static BigInteger lucasLehmerSequence(int i2, BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigIntegerValueOf = valueOf(i2);
        BigInteger bigInteger3 = ONE;
        BigInteger bigIntegerShiftRight = ONE;
        for (int iBitLength = bigInteger.bitLength() - 2; iBitLength >= 0; iBitLength--) {
            BigInteger bigIntegerMod = bigInteger3.multiply(bigIntegerShiftRight).mod(bigInteger2);
            BigInteger bigIntegerMod2 = bigIntegerShiftRight.square().add(bigIntegerValueOf.multiply(bigInteger3.square())).mod(bigInteger2);
            if (bigIntegerMod2.testBit(0)) {
                bigIntegerMod2 = bigIntegerMod2.subtract(bigInteger2);
            }
            bigInteger3 = bigIntegerMod;
            bigIntegerShiftRight = bigIntegerMod2.shiftRight(1);
            if (bigInteger.testBit(iBitLength)) {
                BigInteger bigIntegerMod3 = bigInteger3.add(bigIntegerShiftRight).mod(bigInteger2);
                if (bigIntegerMod3.testBit(0)) {
                    bigIntegerMod3 = bigIntegerMod3.subtract(bigInteger2);
                }
                BigInteger bigIntegerShiftRight2 = bigIntegerMod3.shiftRight(1);
                BigInteger bigIntegerMod4 = bigIntegerShiftRight.add(bigIntegerValueOf.multiply(bigInteger3)).mod(bigInteger2);
                if (bigIntegerMod4.testBit(0)) {
                    bigIntegerMod4 = bigIntegerMod4.subtract(bigInteger2);
                }
                bigInteger3 = bigIntegerShiftRight2;
                bigIntegerShiftRight = bigIntegerMod4.shiftRight(1);
            }
        }
        return bigInteger3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x009e, code lost:
    
        r11 = r11 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean passesMillerRabin(int r6, java.util.Random r7) throws java.lang.RuntimeException {
        /*
            r5 = this;
            r0 = r5
            java.math.BigInteger r1 = java.math.BigInteger.ONE
            java.math.BigInteger r0 = r0.subtract(r1)
            r8 = r0
            r0 = r8
            r9 = r0
            r0 = r9
            int r0 = r0.getLowestSetBit()
            r10 = r0
            r0 = r9
            r1 = r10
            java.math.BigInteger r0 = r0.shiftRight(r1)
            r9 = r0
            r0 = r7
            if (r0 != 0) goto L23
            java.util.concurrent.ThreadLocalRandom r0 = java.util.concurrent.ThreadLocalRandom.current()
            r7 = r0
        L23:
            r0 = 0
            r11 = r0
        L26:
            r0 = r11
            r1 = r6
            if (r0 >= r1) goto La4
        L2c:
            java.math.BigInteger r0 = new java.math.BigInteger
            r1 = r0
            r2 = r5
            int r2 = r2.bitLength()
            r3 = r7
            r1.<init>(r2, r3)
            r12 = r0
            r0 = r12
            java.math.BigInteger r1 = java.math.BigInteger.ONE
            int r0 = r0.compareTo(r1)
            if (r0 <= 0) goto L2c
            r0 = r12
            r1 = r5
            int r0 = r0.compareTo(r1)
            if (r0 >= 0) goto L2c
            r0 = 0
            r13 = r0
            r0 = r12
            r1 = r9
            r2 = r5
            java.math.BigInteger r0 = r0.modPow(r1, r2)
            r14 = r0
        L5b:
            r0 = r13
            if (r0 != 0) goto L6b
            r0 = r14
            java.math.BigInteger r1 = java.math.BigInteger.ONE
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L9e
        L6b:
            r0 = r14
            r1 = r8
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L9e
            r0 = r13
            if (r0 <= 0) goto L84
            r0 = r14
            java.math.BigInteger r1 = java.math.BigInteger.ONE
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L8e
        L84:
            int r13 = r13 + 1
            r0 = r13
            r1 = r10
            if (r0 != r1) goto L90
        L8e:
            r0 = 0
            return r0
        L90:
            r0 = r14
            java.math.BigInteger r1 = java.math.BigInteger.TWO
            r2 = r5
            java.math.BigInteger r0 = r0.modPow(r1, r2)
            r14 = r0
            goto L5b
        L9e:
            int r11 = r11 + 1
            goto L26
        La4:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.math.BigInteger.passesMillerRabin(int, java.util.Random):boolean");
    }

    BigInteger(int[] iArr, int i2) {
        this.signum = iArr.length == 0 ? 0 : i2;
        this.mag = iArr;
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    private BigInteger(byte[] bArr, int i2) {
        this.signum = bArr.length == 0 ? 0 : i2;
        this.mag = stripLeadingZeroBytes(bArr);
        if (this.mag.length >= 67108864) {
            checkRange();
        }
    }

    private void checkRange() {
        if (this.mag.length > 67108864 || (this.mag.length == 67108864 && this.mag[0] < 0)) {
            reportOverflow();
        }
    }

    private static void reportOverflow() {
        throw new ArithmeticException("BigInteger would overflow supported range");
    }

    public static BigInteger valueOf(long j2) {
        if (j2 == 0) {
            return ZERO;
        }
        if (j2 > 0 && j2 <= 16) {
            return posConst[(int) j2];
        }
        if (j2 < 0 && j2 >= -16) {
            return negConst[(int) (-j2)];
        }
        return new BigInteger(j2);
    }

    private BigInteger(long j2) {
        if (j2 < 0) {
            j2 = -j2;
            this.signum = -1;
        } else {
            this.signum = 1;
        }
        int i2 = (int) (j2 >>> 32);
        if (i2 == 0) {
            this.mag = new int[1];
            this.mag[0] = (int) j2;
        } else {
            this.mag = new int[2];
            this.mag[0] = i2;
            this.mag[1] = (int) j2;
        }
    }

    private static BigInteger valueOf(int[] iArr) {
        return iArr[0] > 0 ? new BigInteger(iArr, 1) : new BigInteger(iArr);
    }

    public BigInteger add(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return this;
        }
        if (this.signum == 0) {
            return bigInteger;
        }
        if (bigInteger.signum == this.signum) {
            return new BigInteger(add(this.mag, bigInteger.mag), this.signum);
        }
        int iCompareMagnitude = compareMagnitude(bigInteger);
        if (iCompareMagnitude == 0) {
            return ZERO;
        }
        return new BigInteger(trustedStripLeadingZeroInts(iCompareMagnitude > 0 ? subtract(this.mag, bigInteger.mag) : subtract(bigInteger.mag, this.mag)), iCompareMagnitude == this.signum ? 1 : -1);
    }

    BigInteger add(long j2) {
        if (j2 == 0) {
            return this;
        }
        if (this.signum == 0) {
            return valueOf(j2);
        }
        if (Long.signum(j2) == this.signum) {
            return new BigInteger(add(this.mag, Math.abs(j2)), this.signum);
        }
        int iCompareMagnitude = compareMagnitude(j2);
        if (iCompareMagnitude == 0) {
            return ZERO;
        }
        return new BigInteger(trustedStripLeadingZeroInts(iCompareMagnitude > 0 ? subtract(this.mag, Math.abs(j2)) : subtract(Math.abs(j2), this.mag)), iCompareMagnitude == this.signum ? 1 : -1);
    }

    private static int[] add(int[] iArr, long j2) {
        int[] iArr2;
        int i2;
        long j3;
        boolean z2;
        int length = iArr.length;
        int i3 = (int) (j2 >>> 32);
        if (i3 == 0) {
            iArr2 = new int[length];
            i2 = length - 1;
            j3 = (iArr[i2] & 4294967295L) + j2;
            iArr2[i2] = (int) j3;
        } else {
            if (length == 1) {
                long j4 = j2 + (iArr[0] & 4294967295L);
                return new int[]{(int) (j4 >>> 32), (int) j4};
            }
            iArr2 = new int[length];
            int i4 = length - 1;
            long j5 = (iArr[i4] & 4294967295L) + (j2 & 4294967295L);
            iArr2[i4] = (int) j5;
            i2 = i4 - 1;
            j3 = (iArr[i2] & 4294967295L) + (i3 & 4294967295L) + (j5 >>> 32);
            iArr2[i2] = (int) j3;
        }
        boolean z3 = (j3 >>> 32) != 0;
        while (true) {
            z2 = z3;
            if (i2 <= 0 || !z2) {
                break;
            }
            i2--;
            int i5 = iArr[i2] + 1;
            iArr2[i2] = i5;
            z3 = i5 == 0;
        }
        while (i2 > 0) {
            i2--;
            iArr2[i2] = iArr[i2];
        }
        if (z2) {
            int[] iArr3 = new int[iArr2.length + 1];
            System.arraycopy(iArr2, 0, iArr3, 1, iArr2.length);
            iArr3[0] = 1;
            return iArr3;
        }
        return iArr2;
    }

    private static int[] add(int[] iArr, int[] iArr2) {
        boolean z2;
        if (iArr.length < iArr2.length) {
            iArr = iArr2;
            iArr2 = iArr;
        }
        int length = iArr.length;
        int length2 = iArr2.length;
        int[] iArr3 = new int[length];
        long j2 = 0;
        if (length2 == 1) {
            length--;
            j2 = (iArr[length] & 4294967295L) + (iArr2[0] & 4294967295L);
            iArr3[length] = (int) j2;
        } else {
            while (length2 > 0) {
                length--;
                length2--;
                j2 = (iArr[length] & 4294967295L) + (iArr2[length2] & 4294967295L) + (j2 >>> 32);
                iArr3[length] = (int) j2;
            }
        }
        boolean z3 = (j2 >>> 32) != 0;
        while (true) {
            z2 = z3;
            if (length <= 0 || !z2) {
                break;
            }
            length--;
            int i2 = iArr[length] + 1;
            iArr3[length] = i2;
            z3 = i2 == 0;
        }
        while (length > 0) {
            length--;
            iArr3[length] = iArr[length];
        }
        if (z2) {
            int[] iArr4 = new int[iArr3.length + 1];
            System.arraycopy(iArr3, 0, iArr4, 1, iArr3.length);
            iArr4[0] = 1;
            return iArr4;
        }
        return iArr3;
    }

    private static int[] subtract(long j2, int[] iArr) {
        int i2 = (int) (j2 >>> 32);
        if (i2 == 0) {
            return new int[]{(int) (j2 - (iArr[0] & 4294967295L))};
        }
        int[] iArr2 = new int[2];
        if (iArr.length == 1) {
            long j3 = (((int) j2) & 4294967295L) - (iArr[0] & 4294967295L);
            iArr2[1] = (int) j3;
            if ((j3 >> 32) != 0) {
                iArr2[0] = i2 - 1;
            } else {
                iArr2[0] = i2;
            }
            return iArr2;
        }
        long j4 = (((int) j2) & 4294967295L) - (iArr[1] & 4294967295L);
        iArr2[1] = (int) j4;
        iArr2[0] = (int) (((i2 & 4294967295L) - (iArr[0] & 4294967295L)) + (j4 >> 32));
        return iArr2;
    }

    private static int[] subtract(int[] iArr, long j2) {
        int i2;
        long j3;
        int i3 = (int) (j2 >>> 32);
        int length = iArr.length;
        int[] iArr2 = new int[length];
        if (i3 == 0) {
            i2 = length - 1;
            j3 = (iArr[i2] & 4294967295L) - j2;
            iArr2[i2] = (int) j3;
        } else {
            int i4 = length - 1;
            long j4 = (iArr[i4] & 4294967295L) - (j2 & 4294967295L);
            iArr2[i4] = (int) j4;
            i2 = i4 - 1;
            j3 = ((iArr[i2] & 4294967295L) - (i3 & 4294967295L)) + (j4 >> 32);
            iArr2[i2] = (int) j3;
        }
        boolean z2 = (j3 >> 32) != 0;
        while (true) {
            boolean z3 = z2;
            if (i2 <= 0 || !z3) {
                break;
            }
            i2--;
            int i5 = iArr[i2] - 1;
            iArr2[i2] = i5;
            z2 = i5 == -1;
        }
        while (i2 > 0) {
            i2--;
            iArr2[i2] = iArr[i2];
        }
        return iArr2;
    }

    public BigInteger subtract(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return this;
        }
        if (this.signum == 0) {
            return bigInteger.negate();
        }
        if (bigInteger.signum != this.signum) {
            return new BigInteger(add(this.mag, bigInteger.mag), this.signum);
        }
        int iCompareMagnitude = compareMagnitude(bigInteger);
        if (iCompareMagnitude == 0) {
            return ZERO;
        }
        return new BigInteger(trustedStripLeadingZeroInts(iCompareMagnitude > 0 ? subtract(this.mag, bigInteger.mag) : subtract(bigInteger.mag, this.mag)), iCompareMagnitude == this.signum ? 1 : -1);
    }

    private static int[] subtract(int[] iArr, int[] iArr2) {
        int length = iArr.length;
        int[] iArr3 = new int[length];
        int length2 = iArr2.length;
        long j2 = 0;
        while (length2 > 0) {
            length--;
            length2--;
            j2 = ((iArr[length] & 4294967295L) - (iArr2[length2] & 4294967295L)) + (j2 >> 32);
            iArr3[length] = (int) j2;
        }
        boolean z2 = (j2 >> 32) != 0;
        while (true) {
            boolean z3 = z2;
            if (length <= 0 || !z3) {
                break;
            }
            length--;
            int i2 = iArr[length] - 1;
            iArr3[length] = i2;
            z2 = i2 == -1;
        }
        while (length > 0) {
            length--;
            iArr3[length] = iArr[length];
        }
        return iArr3;
    }

    public BigInteger multiply(BigInteger bigInteger) {
        return multiply(bigInteger, false);
    }

    private BigInteger multiply(BigInteger bigInteger, boolean z2) {
        if (bigInteger.signum == 0 || this.signum == 0) {
            return ZERO;
        }
        int length = this.mag.length;
        if (bigInteger == this && length > 20) {
            return square();
        }
        int length2 = bigInteger.mag.length;
        if (length < 80 || length2 < 80) {
            int i2 = this.signum == bigInteger.signum ? 1 : -1;
            if (bigInteger.mag.length == 1) {
                return multiplyByInt(this.mag, bigInteger.mag[0], i2);
            }
            if (this.mag.length == 1) {
                return multiplyByInt(bigInteger.mag, this.mag[0], i2);
            }
            return new BigInteger(trustedStripLeadingZeroInts(multiplyToLen(this.mag, length, bigInteger.mag, length2, null)), i2);
        }
        if (length < 240 && length2 < 240) {
            return multiplyKaratsuba(this, bigInteger);
        }
        if (!z2 && bitLength(this.mag, this.mag.length) + bitLength(bigInteger.mag, bigInteger.mag.length) > 2147483648L) {
            reportOverflow();
        }
        return multiplyToomCook3(this, bigInteger);
    }

    private static BigInteger multiplyByInt(int[] iArr, int i2, int i3) {
        if (Integer.bitCount(i2) == 1) {
            return new BigInteger(shiftLeft(iArr, Integer.numberOfTrailingZeros(i2)), i3);
        }
        int length = iArr.length;
        int[] iArrCopyOfRange = new int[length + 1];
        long j2 = 0;
        long j3 = i2 & 4294967295L;
        int length2 = iArrCopyOfRange.length - 1;
        for (int i4 = length - 1; i4 >= 0; i4--) {
            long j4 = ((iArr[i4] & 4294967295L) * j3) + j2;
            int i5 = length2;
            length2--;
            iArrCopyOfRange[i5] = (int) j4;
            j2 = j4 >>> 32;
        }
        if (j2 == 0) {
            iArrCopyOfRange = Arrays.copyOfRange(iArrCopyOfRange, 1, iArrCopyOfRange.length);
        } else {
            iArrCopyOfRange[length2] = (int) j2;
        }
        return new BigInteger(iArrCopyOfRange, i3);
    }

    BigInteger multiply(long j2) {
        if (j2 == 0 || this.signum == 0) {
            return ZERO;
        }
        if (j2 == Long.MIN_VALUE) {
            return multiply(valueOf(j2));
        }
        int i2 = j2 > 0 ? this.signum : -this.signum;
        if (j2 < 0) {
            j2 = -j2;
        }
        long j3 = j2 >>> 32;
        long j4 = j2 & 4294967295L;
        int length = this.mag.length;
        int[] iArr = this.mag;
        int[] iArrCopyOfRange = j3 == 0 ? new int[length + 1] : new int[length + 2];
        long j5 = 0;
        int length2 = iArrCopyOfRange.length - 1;
        for (int i3 = length - 1; i3 >= 0; i3--) {
            long j6 = ((iArr[i3] & 4294967295L) * j4) + j5;
            int i4 = length2;
            length2--;
            iArrCopyOfRange[i4] = (int) j6;
            j5 = j6 >>> 32;
        }
        iArrCopyOfRange[length2] = (int) j5;
        if (j3 != 0) {
            j5 = 0;
            int length3 = iArrCopyOfRange.length - 2;
            for (int i5 = length - 1; i5 >= 0; i5--) {
                long j7 = ((iArr[i5] & 4294967295L) * j3) + (iArrCopyOfRange[length3] & 4294967295L) + j5;
                int i6 = length3;
                length3--;
                iArrCopyOfRange[i6] = (int) j7;
                j5 = j7 >>> 32;
            }
            iArrCopyOfRange[0] = (int) j5;
        }
        if (j5 == 0) {
            iArrCopyOfRange = Arrays.copyOfRange(iArrCopyOfRange, 1, iArrCopyOfRange.length);
        }
        return new BigInteger(iArrCopyOfRange, i2);
    }

    private static int[] multiplyToLen(int[] iArr, int i2, int[] iArr2, int i3, int[] iArr3) {
        int i4 = i2 - 1;
        int i5 = i3 - 1;
        if (iArr3 == null || iArr3.length < i2 + i3) {
            iArr3 = new int[i2 + i3];
        }
        long j2 = 0;
        int i6 = i5;
        int i7 = i5 + 1 + i4;
        while (i6 >= 0) {
            long j3 = ((iArr2[i6] & 4294967295L) * (iArr[i4] & 4294967295L)) + j2;
            iArr3[i7] = (int) j3;
            j2 = j3 >>> 32;
            i6--;
            i7--;
        }
        iArr3[i4] = (int) j2;
        for (int i8 = i4 - 1; i8 >= 0; i8--) {
            long j4 = 0;
            int i9 = i5;
            int i10 = i5 + 1 + i8;
            while (i9 >= 0) {
                long j5 = ((iArr2[i9] & 4294967295L) * (iArr[i8] & 4294967295L)) + (iArr3[i10] & 4294967295L) + j4;
                iArr3[i10] = (int) j5;
                j4 = j5 >>> 32;
                i9--;
                i10--;
            }
            iArr3[i8] = (int) j4;
        }
        return iArr3;
    }

    private static BigInteger multiplyKaratsuba(BigInteger bigInteger, BigInteger bigInteger2) {
        int iMax = (Math.max(bigInteger.mag.length, bigInteger2.mag.length) + 1) / 2;
        BigInteger lower = bigInteger.getLower(iMax);
        BigInteger upper = bigInteger.getUpper(iMax);
        BigInteger lower2 = bigInteger2.getLower(iMax);
        BigInteger upper2 = bigInteger2.getUpper(iMax);
        BigInteger bigIntegerMultiply = upper.multiply(upper2);
        BigInteger bigIntegerMultiply2 = lower.multiply(lower2);
        BigInteger bigIntegerAdd = bigIntegerMultiply.shiftLeft(32 * iMax).add(upper.add(lower).multiply(upper2.add(lower2)).subtract(bigIntegerMultiply).subtract(bigIntegerMultiply2)).shiftLeft(32 * iMax).add(bigIntegerMultiply2);
        if (bigInteger.signum != bigInteger2.signum) {
            return bigIntegerAdd.negate();
        }
        return bigIntegerAdd;
    }

    private static BigInteger multiplyToomCook3(BigInteger bigInteger, BigInteger bigInteger2) {
        int iMax = Math.max(bigInteger.mag.length, bigInteger2.mag.length);
        int i2 = (iMax + 2) / 3;
        int i3 = iMax - (2 * i2);
        BigInteger toomSlice = bigInteger.getToomSlice(i2, i3, 0, iMax);
        BigInteger toomSlice2 = bigInteger.getToomSlice(i2, i3, 1, iMax);
        BigInteger toomSlice3 = bigInteger.getToomSlice(i2, i3, 2, iMax);
        BigInteger toomSlice4 = bigInteger2.getToomSlice(i2, i3, 0, iMax);
        BigInteger toomSlice5 = bigInteger2.getToomSlice(i2, i3, 1, iMax);
        BigInteger toomSlice6 = bigInteger2.getToomSlice(i2, i3, 2, iMax);
        BigInteger bigIntegerMultiply = toomSlice3.multiply(toomSlice6, true);
        BigInteger bigIntegerAdd = toomSlice.add(toomSlice3);
        BigInteger bigIntegerAdd2 = toomSlice4.add(toomSlice6);
        BigInteger bigIntegerMultiply2 = bigIntegerAdd.subtract(toomSlice2).multiply(bigIntegerAdd2.subtract(toomSlice5), true);
        BigInteger bigIntegerAdd3 = bigIntegerAdd.add(toomSlice2);
        BigInteger bigIntegerAdd4 = bigIntegerAdd2.add(toomSlice5);
        BigInteger bigIntegerMultiply3 = bigIntegerAdd3.multiply(bigIntegerAdd4, true);
        BigInteger bigIntegerMultiply4 = bigIntegerAdd3.add(toomSlice).shiftLeft(1).subtract(toomSlice3).multiply(bigIntegerAdd4.add(toomSlice4).shiftLeft(1).subtract(toomSlice6), true);
        BigInteger bigIntegerMultiply5 = toomSlice.multiply(toomSlice4, true);
        BigInteger bigIntegerExactDivideBy3 = bigIntegerMultiply4.subtract(bigIntegerMultiply2).exactDivideBy3();
        BigInteger bigIntegerShiftRight = bigIntegerMultiply3.subtract(bigIntegerMultiply2).shiftRight(1);
        BigInteger bigIntegerSubtract = bigIntegerMultiply3.subtract(bigIntegerMultiply);
        BigInteger bigIntegerShiftRight2 = bigIntegerExactDivideBy3.subtract(bigIntegerSubtract).shiftRight(1);
        BigInteger bigIntegerSubtract2 = bigIntegerSubtract.subtract(bigIntegerShiftRight).subtract(bigIntegerMultiply5);
        BigInteger bigIntegerSubtract3 = bigIntegerShiftRight2.subtract(bigIntegerMultiply5.shiftLeft(1));
        BigInteger bigIntegerSubtract4 = bigIntegerShiftRight.subtract(bigIntegerSubtract3);
        int i4 = i2 * 32;
        BigInteger bigIntegerAdd5 = bigIntegerMultiply5.shiftLeft(i4).add(bigIntegerSubtract3).shiftLeft(i4).add(bigIntegerSubtract2).shiftLeft(i4).add(bigIntegerSubtract4).shiftLeft(i4).add(bigIntegerMultiply);
        if (bigInteger.signum != bigInteger2.signum) {
            return bigIntegerAdd5.negate();
        }
        return bigIntegerAdd5;
    }

    private BigInteger getToomSlice(int i2, int i3, int i4, int i5) {
        int i6;
        int i7;
        int length = this.mag.length;
        int i8 = i5 - length;
        if (i4 == 0) {
            i6 = 0 - i8;
            i7 = (i3 - 1) - i8;
        } else {
            i6 = (i3 + ((i4 - 1) * i2)) - i8;
            i7 = (i6 + i2) - 1;
        }
        if (i6 < 0) {
            i6 = 0;
        }
        if (i7 < 0) {
            return ZERO;
        }
        int i9 = (i7 - i6) + 1;
        if (i9 <= 0) {
            return ZERO;
        }
        if (i6 == 0 && i9 >= length) {
            return abs();
        }
        int[] iArr = new int[i9];
        System.arraycopy(this.mag, i6, iArr, 0, i9);
        return new BigInteger(trustedStripLeadingZeroInts(iArr), 1);
    }

    private BigInteger exactDivideBy3() {
        int length = this.mag.length;
        int[] iArr = new int[length];
        long j2 = 0;
        for (int i2 = length - 1; i2 >= 0; i2--) {
            long j3 = this.mag[i2] & 4294967295L;
            long j4 = j3 - j2;
            if (j2 > j3) {
                j2 = 1;
            } else {
                j2 = 0;
            }
            long j5 = (j4 * 2863311531L) & 4294967295L;
            iArr[i2] = (int) j5;
            if (j5 >= 1431655766) {
                j2++;
                if (j5 >= 2863311531L) {
                    j2++;
                }
            }
        }
        return new BigInteger(trustedStripLeadingZeroInts(iArr), this.signum);
    }

    private BigInteger getLower(int i2) {
        int length = this.mag.length;
        if (length <= i2) {
            return abs();
        }
        int[] iArr = new int[i2];
        System.arraycopy(this.mag, length - i2, iArr, 0, i2);
        return new BigInteger(trustedStripLeadingZeroInts(iArr), 1);
    }

    private BigInteger getUpper(int i2) {
        int length = this.mag.length;
        if (length <= i2) {
            return ZERO;
        }
        int i3 = length - i2;
        int[] iArr = new int[i3];
        System.arraycopy(this.mag, 0, iArr, 0, i3);
        return new BigInteger(trustedStripLeadingZeroInts(iArr), 1);
    }

    private BigInteger square() {
        return square(false);
    }

    private BigInteger square(boolean z2) {
        if (this.signum == 0) {
            return ZERO;
        }
        int length = this.mag.length;
        if (length < 128) {
            return new BigInteger(trustedStripLeadingZeroInts(squareToLen(this.mag, length, null)), 1);
        }
        if (length < 216) {
            return squareKaratsuba();
        }
        if (!z2 && bitLength(this.mag, this.mag.length) > PKCS11Constants.CKF_ARRAY_ATTRIBUTE) {
            reportOverflow();
        }
        return squareToomCook3();
    }

    private static final int[] squareToLen(int[] iArr, int i2, int[] iArr2) throws RuntimeException {
        int i3 = i2 << 1;
        if (iArr2 == null || iArr2.length < i3) {
            iArr2 = new int[i3];
        }
        implSquareToLenChecks(iArr, i2, iArr2, i3);
        return implSquareToLen(iArr, i2, iArr2, i3);
    }

    private static void implSquareToLenChecks(int[] iArr, int i2, int[] iArr2, int i3) throws RuntimeException {
        if (i2 < 1) {
            throw new IllegalArgumentException("invalid input length: " + i2);
        }
        if (i2 > iArr.length) {
            throw new IllegalArgumentException("input length out of bound: " + i2 + " > " + iArr.length);
        }
        if (i2 * 2 > iArr2.length) {
            throw new IllegalArgumentException("input length out of bound: " + (i2 * 2) + " > " + iArr2.length);
        }
        if (i3 < 1) {
            throw new IllegalArgumentException("invalid input length: " + i3);
        }
        if (i3 > iArr2.length) {
            throw new IllegalArgumentException("input length out of bound: " + i2 + " > " + iArr2.length);
        }
    }

    private static final int[] implSquareToLen(int[] iArr, int i2, int[] iArr2, int i3) {
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i2; i6++) {
            long j2 = iArr[i6] & 4294967295L;
            long j3 = j2 * j2;
            int i7 = i5;
            int i8 = i5 + 1;
            iArr2[i7] = (i4 << 31) | ((int) (j3 >>> 33));
            i5 = i8 + 1;
            iArr2[i8] = (int) (j3 >>> 1);
            i4 = (int) j3;
        }
        int i9 = i2;
        int i10 = 1;
        while (i9 > 0) {
            addOne(iArr2, i10 - 1, i9, mulAdd(iArr2, iArr, i10, i9 - 1, iArr[i9 - 1]));
            i9--;
            i10 += 2;
        }
        primitiveLeftShift(iArr2, i3, 1);
        int i11 = i3 - 1;
        iArr2[i11] = iArr2[i11] | (iArr[i2 - 1] & 1);
        return iArr2;
    }

    private BigInteger squareKaratsuba() {
        int length = (this.mag.length + 1) / 2;
        BigInteger lower = getLower(length);
        BigInteger upper = getUpper(length);
        BigInteger bigIntegerSquare = upper.square();
        BigInteger bigIntegerSquare2 = lower.square();
        return bigIntegerSquare.shiftLeft(length * 32).add(lower.add(upper).square().subtract(bigIntegerSquare.add(bigIntegerSquare2))).shiftLeft(length * 32).add(bigIntegerSquare2);
    }

    private BigInteger squareToomCook3() {
        int length = this.mag.length;
        int i2 = (length + 2) / 3;
        int i3 = length - (2 * i2);
        BigInteger toomSlice = getToomSlice(i2, i3, 0, length);
        BigInteger toomSlice2 = getToomSlice(i2, i3, 1, length);
        BigInteger toomSlice3 = getToomSlice(i2, i3, 2, length);
        BigInteger bigIntegerSquare = toomSlice3.square(true);
        BigInteger bigIntegerAdd = toomSlice.add(toomSlice3);
        BigInteger bigIntegerSquare2 = bigIntegerAdd.subtract(toomSlice2).square(true);
        BigInteger bigIntegerAdd2 = bigIntegerAdd.add(toomSlice2);
        BigInteger bigIntegerSquare3 = bigIntegerAdd2.square(true);
        BigInteger bigIntegerSquare4 = toomSlice.square(true);
        BigInteger bigIntegerExactDivideBy3 = bigIntegerAdd2.add(toomSlice).shiftLeft(1).subtract(toomSlice3).square(true).subtract(bigIntegerSquare2).exactDivideBy3();
        BigInteger bigIntegerShiftRight = bigIntegerSquare3.subtract(bigIntegerSquare2).shiftRight(1);
        BigInteger bigIntegerSubtract = bigIntegerSquare3.subtract(bigIntegerSquare);
        BigInteger bigIntegerShiftRight2 = bigIntegerExactDivideBy3.subtract(bigIntegerSubtract).shiftRight(1);
        BigInteger bigIntegerSubtract2 = bigIntegerSubtract.subtract(bigIntegerShiftRight).subtract(bigIntegerSquare4);
        BigInteger bigIntegerSubtract3 = bigIntegerShiftRight2.subtract(bigIntegerSquare4.shiftLeft(1));
        BigInteger bigIntegerSubtract4 = bigIntegerShiftRight.subtract(bigIntegerSubtract3);
        int i4 = i2 * 32;
        return bigIntegerSquare4.shiftLeft(i4).add(bigIntegerSubtract3).shiftLeft(i4).add(bigIntegerSubtract2).shiftLeft(i4).add(bigIntegerSubtract4).shiftLeft(i4).add(bigIntegerSquare);
    }

    public BigInteger divide(BigInteger bigInteger) {
        if (bigInteger.mag.length < 80 || this.mag.length - bigInteger.mag.length < 40) {
            return divideKnuth(bigInteger);
        }
        return divideBurnikelZiegler(bigInteger);
    }

    private BigInteger divideKnuth(BigInteger bigInteger) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), mutableBigInteger, false);
        return mutableBigInteger.toBigInteger(this.signum * bigInteger.signum);
    }

    public BigInteger[] divideAndRemainder(BigInteger bigInteger) {
        if (bigInteger.mag.length < 80 || this.mag.length - bigInteger.mag.length < 40) {
            return divideAndRemainderKnuth(bigInteger);
        }
        return divideAndRemainderBurnikelZiegler(bigInteger);
    }

    private BigInteger[] divideAndRemainderKnuth(BigInteger bigInteger) {
        BigInteger[] bigIntegerArr = new BigInteger[2];
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        MutableBigInteger mutableBigIntegerDivideKnuth = new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), mutableBigInteger);
        bigIntegerArr[0] = mutableBigInteger.toBigInteger(this.signum == bigInteger.signum ? 1 : -1);
        bigIntegerArr[1] = mutableBigIntegerDivideKnuth.toBigInteger(this.signum);
        return bigIntegerArr;
    }

    public BigInteger remainder(BigInteger bigInteger) {
        if (bigInteger.mag.length < 80 || this.mag.length - bigInteger.mag.length < 40) {
            return remainderKnuth(bigInteger);
        }
        return remainderBurnikelZiegler(bigInteger);
    }

    private BigInteger remainderKnuth(BigInteger bigInteger) {
        return new MutableBigInteger(this.mag).divideKnuth(new MutableBigInteger(bigInteger.mag), new MutableBigInteger()).toBigInteger(this.signum);
    }

    private BigInteger divideBurnikelZiegler(BigInteger bigInteger) {
        return divideAndRemainderBurnikelZiegler(bigInteger)[0];
    }

    private BigInteger remainderBurnikelZiegler(BigInteger bigInteger) {
        return divideAndRemainderBurnikelZiegler(bigInteger)[1];
    }

    private BigInteger[] divideAndRemainderBurnikelZiegler(BigInteger bigInteger) {
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        MutableBigInteger mutableBigIntegerDivideAndRemainderBurnikelZiegler = new MutableBigInteger(this).divideAndRemainderBurnikelZiegler(new MutableBigInteger(bigInteger), mutableBigInteger);
        return new BigInteger[]{mutableBigInteger.isZero() ? ZERO : mutableBigInteger.toBigInteger(this.signum * bigInteger.signum), mutableBigIntegerDivideAndRemainderBurnikelZiegler.isZero() ? ZERO : mutableBigIntegerDivideAndRemainderBurnikelZiegler.toBigInteger(this.signum)};
    }

    public BigInteger pow(int i2) {
        int iBitLength;
        if (i2 < 0) {
            throw new ArithmeticException("Negative exponent");
        }
        if (this.signum == 0) {
            return i2 == 0 ? ONE : this;
        }
        BigInteger bigIntegerAbs = abs();
        int lowestSetBit = bigIntegerAbs.getLowestSetBit();
        long j2 = lowestSetBit * i2;
        if (j2 > 2147483647L) {
            reportOverflow();
        }
        int i3 = (int) j2;
        if (lowestSetBit > 0) {
            bigIntegerAbs = bigIntegerAbs.shiftRight(lowestSetBit);
            iBitLength = bigIntegerAbs.bitLength();
            if (iBitLength == 1) {
                if (this.signum < 0 && (i2 & 1) == 1) {
                    return NEGATIVE_ONE.shiftLeft(i3);
                }
                return ONE.shiftLeft(i3);
            }
        } else {
            iBitLength = bigIntegerAbs.bitLength();
            if (iBitLength == 1) {
                if (this.signum < 0 && (i2 & 1) == 1) {
                    return NEGATIVE_ONE;
                }
                return ONE;
            }
        }
        long j3 = iBitLength * i2;
        if (bigIntegerAbs.mag.length == 1 && j3 <= 62) {
            int i4 = (this.signum >= 0 || (i2 & 1) != 1) ? 1 : -1;
            long j4 = 1;
            long j5 = bigIntegerAbs.mag[0] & 4294967295L;
            int i5 = i2;
            while (i5 != 0) {
                if ((i5 & 1) == 1) {
                    j4 *= j5;
                }
                int i6 = i5 >>> 1;
                i5 = i6;
                if (i6 != 0) {
                    j5 *= j5;
                }
            }
            if (lowestSetBit <= 0) {
                return valueOf(j4 * i4);
            }
            if (i3 + j3 <= 62) {
                return valueOf((j4 << i3) * i4);
            }
            return valueOf(j4 * i4).shiftLeft(i3);
        }
        if ((bitLength() * i2) / 32 > PKCS11Constants.CKF_EC_CURVENAME) {
            reportOverflow();
        }
        BigInteger bigIntegerShiftLeft = ONE;
        int i7 = i2;
        while (i7 != 0) {
            if ((i7 & 1) == 1) {
                bigIntegerShiftLeft = bigIntegerShiftLeft.multiply(bigIntegerAbs);
            }
            int i8 = i7 >>> 1;
            i7 = i8;
            if (i8 != 0) {
                bigIntegerAbs = bigIntegerAbs.square();
            }
        }
        if (lowestSetBit > 0) {
            bigIntegerShiftLeft = bigIntegerShiftLeft.shiftLeft(i3);
        }
        if (this.signum < 0 && (i2 & 1) == 1) {
            return bigIntegerShiftLeft.negate();
        }
        return bigIntegerShiftLeft;
    }

    public BigInteger gcd(BigInteger bigInteger) {
        if (bigInteger.signum == 0) {
            return abs();
        }
        if (this.signum == 0) {
            return bigInteger.abs();
        }
        return new MutableBigInteger(this).hybridGCD(new MutableBigInteger(bigInteger)).toBigInteger(1);
    }

    static int bitLengthForInt(int i2) {
        return 32 - Integer.numberOfLeadingZeros(i2);
    }

    private static int[] leftShift(int[] iArr, int i2, int i3) {
        int i4 = i3 >>> 5;
        int i5 = i3 & 31;
        int iBitLengthForInt = bitLengthForInt(iArr[0]);
        if (i3 <= 32 - iBitLengthForInt) {
            primitiveLeftShift(iArr, i2, i5);
            return iArr;
        }
        if (i5 <= 32 - iBitLengthForInt) {
            int[] iArr2 = new int[i4 + i2];
            System.arraycopy(iArr, 0, iArr2, 0, i2);
            primitiveLeftShift(iArr2, iArr2.length, i5);
            return iArr2;
        }
        int[] iArr3 = new int[i4 + i2 + 1];
        System.arraycopy(iArr, 0, iArr3, 0, i2);
        primitiveRightShift(iArr3, iArr3.length, 32 - i5);
        return iArr3;
    }

    static void primitiveRightShift(int[] iArr, int i2, int i3) {
        int i4 = 32 - i3;
        int i5 = i2 - 1;
        int i6 = iArr[i5];
        while (i5 > 0) {
            int i7 = i6;
            i6 = iArr[i5 - 1];
            iArr[i5] = (i6 << i4) | (i7 >>> i3);
            i5--;
        }
        iArr[0] = iArr[0] >>> i3;
    }

    static void primitiveLeftShift(int[] iArr, int i2, int i3) {
        if (i2 == 0 || i3 == 0) {
            return;
        }
        int i4 = 32 - i3;
        int i5 = iArr[0];
        int i6 = (0 + i2) - 1;
        for (int i7 = 0; i7 < i6; i7++) {
            int i8 = i5;
            i5 = iArr[i7 + 1];
            iArr[i7] = (i8 << i3) | (i5 >>> i4);
        }
        int i9 = i2 - 1;
        iArr[i9] = iArr[i9] << i3;
    }

    private static int bitLength(int[] iArr, int i2) {
        if (i2 == 0) {
            return 0;
        }
        return ((i2 - 1) << 5) + bitLengthForInt(iArr[0]);
    }

    public BigInteger abs() {
        return this.signum >= 0 ? this : negate();
    }

    public BigInteger negate() {
        return new BigInteger(this.mag, -this.signum);
    }

    public int signum() {
        return this.signum;
    }

    public BigInteger mod(BigInteger bigInteger) {
        if (bigInteger.signum <= 0) {
            throw new ArithmeticException("BigInteger: modulus not positive");
        }
        BigInteger bigIntegerRemainder = remainder(bigInteger);
        return bigIntegerRemainder.signum >= 0 ? bigIntegerRemainder : bigIntegerRemainder.add(bigInteger);
    }

    public BigInteger modPow(BigInteger bigInteger, BigInteger bigInteger2) throws RuntimeException {
        BigInteger bigInteger3;
        if (bigInteger2.signum <= 0) {
            throw new ArithmeticException("BigInteger: modulus not positive");
        }
        if (bigInteger.signum == 0) {
            return bigInteger2.equals(ONE) ? ZERO : ONE;
        }
        if (equals(ONE)) {
            return bigInteger2.equals(ONE) ? ZERO : ONE;
        }
        if (equals(ZERO) && bigInteger.signum >= 0) {
            return ZERO;
        }
        if (equals(negConst[1]) && !bigInteger.testBit(0)) {
            return bigInteger2.equals(ONE) ? ZERO : ONE;
        }
        boolean z2 = bigInteger.signum < 0;
        boolean z3 = z2;
        if (z2) {
            bigInteger = bigInteger.negate();
        }
        BigInteger bigIntegerMod = (this.signum < 0 || compareTo(bigInteger2) >= 0) ? mod(bigInteger2) : this;
        if (bigInteger2.testBit(0)) {
            bigInteger3 = bigIntegerMod.oddModPow(bigInteger, bigInteger2);
        } else {
            int lowestSetBit = bigInteger2.getLowestSetBit();
            BigInteger bigIntegerShiftRight = bigInteger2.shiftRight(lowestSetBit);
            BigInteger bigIntegerShiftLeft = ONE.shiftLeft(lowestSetBit);
            BigInteger bigIntegerOddModPow = bigIntegerShiftRight.equals(ONE) ? ZERO : ((this.signum < 0 || compareTo(bigIntegerShiftRight) >= 0) ? mod(bigIntegerShiftRight) : this).oddModPow(bigInteger, bigIntegerShiftRight);
            BigInteger bigIntegerModPow2 = bigIntegerMod.modPow2(bigInteger, lowestSetBit);
            BigInteger bigIntegerModInverse = bigIntegerShiftLeft.modInverse(bigIntegerShiftRight);
            BigInteger bigIntegerModInverse2 = bigIntegerShiftRight.modInverse(bigIntegerShiftLeft);
            if (bigInteger2.mag.length < 33554432) {
                bigInteger3 = bigIntegerOddModPow.multiply(bigIntegerShiftLeft).multiply(bigIntegerModInverse).add(bigIntegerModPow2.multiply(bigIntegerShiftRight).multiply(bigIntegerModInverse2)).mod(bigInteger2);
            } else {
                MutableBigInteger mutableBigInteger = new MutableBigInteger();
                new MutableBigInteger(bigIntegerOddModPow.multiply(bigIntegerShiftLeft)).multiply(new MutableBigInteger(bigIntegerModInverse), mutableBigInteger);
                MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
                new MutableBigInteger(bigIntegerModPow2.multiply(bigIntegerShiftRight)).multiply(new MutableBigInteger(bigIntegerModInverse2), mutableBigInteger2);
                mutableBigInteger.add(mutableBigInteger2);
                bigInteger3 = mutableBigInteger.divide(new MutableBigInteger(bigInteger2), new MutableBigInteger()).toBigInteger();
            }
        }
        return z3 ? bigInteger3.modInverse(bigInteger2) : bigInteger3;
    }

    private static int[] montgomeryMultiply(int[] iArr, int[] iArr2, int[] iArr3, int i2, long j2, int[] iArr4) throws RuntimeException {
        implMontgomeryMultiplyChecks(iArr, iArr2, iArr3, i2, iArr4);
        if (i2 > 512) {
            return montReduce(multiplyToLen(iArr, i2, iArr2, i2, iArr4), iArr3, i2, (int) j2);
        }
        return implMontgomeryMultiply(iArr, iArr2, iArr3, i2, j2, materialize(iArr4, i2));
    }

    private static int[] montgomerySquare(int[] iArr, int[] iArr2, int i2, long j2, int[] iArr3) throws RuntimeException {
        implMontgomeryMultiplyChecks(iArr, iArr, iArr2, i2, iArr3);
        if (i2 > 512) {
            return montReduce(squareToLen(iArr, i2, iArr3), iArr2, i2, (int) j2);
        }
        return implMontgomerySquare(iArr, iArr2, i2, j2, materialize(iArr3, i2));
    }

    private static void implMontgomeryMultiplyChecks(int[] iArr, int[] iArr2, int[] iArr3, int i2, int[] iArr4) throws RuntimeException {
        if (i2 % 2 != 0) {
            throw new IllegalArgumentException("input array length must be even: " + i2);
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("invalid input length: " + i2);
        }
        if (i2 > iArr.length || i2 > iArr2.length || i2 > iArr3.length || (iArr4 != null && i2 > iArr4.length)) {
            throw new IllegalArgumentException("input array length out of bound: " + i2);
        }
    }

    private static int[] materialize(int[] iArr, int i2) {
        if (iArr == null || iArr.length < i2) {
            iArr = new int[i2];
        }
        return iArr;
    }

    private static int[] implMontgomeryMultiply(int[] iArr, int[] iArr2, int[] iArr3, int i2, long j2, int[] iArr4) {
        return montReduce(multiplyToLen(iArr, i2, iArr2, i2, iArr4), iArr3, i2, (int) j2);
    }

    private static int[] implMontgomerySquare(int[] iArr, int[] iArr2, int i2, long j2, int[] iArr3) {
        return montReduce(squareToLen(iArr, i2, iArr3), iArr2, i2, (int) j2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v111 */
    /* JADX WARN: Type inference failed for: r0v137, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v24, types: [int[]] */
    /* JADX WARN: Type inference failed for: r0v45 */
    /* JADX WARN: Type inference failed for: r0v48, types: [int[]] */
    /* JADX WARN: Type inference failed for: r0v69 */
    /* JADX WARN: Type inference failed for: r1v72 */
    /* JADX WARN: Type inference failed for: r3v10, types: [int[]] */
    /* JADX WARN: Type inference failed for: r4v8 */
    private BigInteger oddModPow(BigInteger bigInteger, BigInteger bigInteger2) throws RuntimeException {
        if (bigInteger.equals(ONE)) {
            return this;
        }
        if (this.signum == 0) {
            return ZERO;
        }
        int[] iArr = (int[]) this.mag.clone();
        int[] iArr2 = bigInteger.mag;
        int[] iArr3 = bigInteger2.mag;
        int length = iArr3.length;
        if ((length & 1) != 0) {
            int[] iArr4 = new int[length + 1];
            System.arraycopy(iArr3, 0, iArr4, 1, length);
            iArr3 = iArr4;
            length++;
        }
        int i2 = 0;
        int iBitLength = bitLength(iArr2, iArr2.length);
        if (iBitLength != 17 || iArr2[0] != 65537) {
            while (iBitLength > bnExpModThreshTable[i2]) {
                i2++;
            }
        }
        int i3 = 1 << i2;
        ?? r0 = new int[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            r0[i4] = new int[length];
        }
        long j2 = -MutableBigInteger.inverseMod64((iArr3[length - 1] & 4294967295L) + ((iArr3[length - 2] & 4294967295L) << 32));
        int[] iArrLeftShift = leftShift(iArr, iArr.length, length << 5);
        MutableBigInteger mutableBigInteger = new MutableBigInteger();
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger(iArrLeftShift);
        MutableBigInteger mutableBigInteger3 = new MutableBigInteger(iArr3);
        mutableBigInteger3.normalize();
        r0[0] = mutableBigInteger2.divide(mutableBigInteger3, mutableBigInteger).toIntArray();
        if (r0[0].length < length) {
            int length2 = length - r0[0].length;
            int[] iArr5 = new int[length];
            System.arraycopy(r0[0], 0, iArr5, length2, r0[0].length);
            r0[0] = iArr5;
        }
        int[] iArrMontgomerySquare = montgomerySquare(r0[0], iArr3, length, j2, null);
        int[] iArrCopyOf = Arrays.copyOf(iArrMontgomerySquare, length);
        for (int i5 = 1; i5 < i3; i5++) {
            r0[i5] = montgomeryMultiply(iArrCopyOf, r0[i5 - 1], iArr3, length, j2, null);
        }
        int i6 = 1 << ((iBitLength - 1) & 31);
        int i7 = 0;
        int length3 = iArr2.length;
        int i8 = 0;
        for (int i9 = 0; i9 <= i2; i9++) {
            i7 = (i7 << 1) | ((iArr2[i8] & i6) != 0 ? 1 : 0);
            i6 >>>= 1;
            if (i6 == 0) {
                i8++;
                i6 = Integer.MIN_VALUE;
                length3--;
            }
        }
        int i10 = iBitLength - 1;
        boolean z2 = true;
        int i11 = i10 - i2;
        while ((i7 & 1) == 0) {
            i7 >>>= 1;
            i11++;
        }
        int[] iArr6 = r0[i7 >>> 1];
        int i12 = 0;
        if (i11 == i10) {
            z2 = false;
        }
        while (true) {
            i10--;
            i12 <<= 1;
            if (length3 != 0) {
                i12 |= (iArr2[i8] & i6) != 0 ? 1 : 0;
                i6 >>>= 1;
                if (i6 == 0) {
                    i8++;
                    i6 = Integer.MIN_VALUE;
                    length3--;
                }
            }
            if ((i12 & i3) != 0) {
                i11 = i10 - i2;
                while ((i12 & 1) == 0) {
                    i12 >>>= 1;
                    i11++;
                }
                iArr6 = r0[i12 >>> 1];
                i12 = 0;
            }
            if (i10 == i11) {
                if (z2) {
                    iArrMontgomerySquare = (int[]) iArr6.clone();
                    z2 = false;
                } else {
                    int[] iArrMontgomeryMultiply = montgomeryMultiply(iArrMontgomerySquare, iArr6, iArr3, length, j2, iArrLeftShift);
                    iArrLeftShift = iArrMontgomerySquare;
                    iArrMontgomerySquare = iArrMontgomeryMultiply;
                }
            }
            if (i10 != 0) {
                if (!z2) {
                    int[] iArrMontgomerySquare2 = montgomerySquare(iArrMontgomerySquare, iArr3, length, j2, iArrLeftShift);
                    iArrLeftShift = iArrMontgomerySquare;
                    iArrMontgomerySquare = iArrMontgomerySquare2;
                }
            } else {
                int[] iArr7 = new int[2 * length];
                System.arraycopy(iArrMontgomerySquare, 0, iArr7, length, length);
                return new BigInteger(1, Arrays.copyOf(montReduce(iArr7, iArr3, length, (int) j2), length));
            }
        }
    }

    private static int[] montReduce(int[] iArr, int[] iArr2, int i2, int i3) {
        int iAddOne = 0;
        int i4 = i2;
        int i5 = 0;
        do {
            iAddOne += addOne(iArr, i5, i2, mulAdd(iArr, iArr2, i5, i2, i3 * iArr[(iArr.length - 1) - i5]));
            i5++;
            i4--;
        } while (i4 > 0);
        while (iAddOne > 0) {
            iAddOne += subN(iArr, iArr2, i2);
        }
        while (intArrayCmpToLen(iArr, iArr2, i2) >= 0) {
            subN(iArr, iArr2, i2);
        }
        return iArr;
    }

    private static int intArrayCmpToLen(int[] iArr, int[] iArr2, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            long j2 = iArr[i3] & 4294967295L;
            long j3 = iArr2[i3] & 4294967295L;
            if (j2 < j3) {
                return -1;
            }
            if (j2 > j3) {
                return 1;
            }
        }
        return 0;
    }

    private static int subN(int[] iArr, int[] iArr2, int i2) {
        long j2 = 0;
        while (true) {
            i2--;
            if (i2 >= 0) {
                j2 = ((iArr[i2] & 4294967295L) - (iArr2[i2] & 4294967295L)) + (j2 >> 32);
                iArr[i2] = (int) j2;
            } else {
                return (int) (j2 >> 32);
            }
        }
    }

    static int mulAdd(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        implMulAddCheck(iArr, iArr2, i2, i3, i4);
        return implMulAdd(iArr, iArr2, i2, i3, i4);
    }

    private static void implMulAddCheck(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        if (i3 > iArr2.length) {
            throw new IllegalArgumentException("input length is out of bound: " + i3 + " > " + iArr2.length);
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("input offset is invalid: " + i2);
        }
        if (i2 > iArr.length - 1) {
            throw new IllegalArgumentException("input offset is out of bound: " + i2 + " > " + (iArr.length - 1));
        }
        if (i3 > iArr.length - i2) {
            throw new IllegalArgumentException("input len is out of bound: " + i3 + " > " + (iArr.length - i2));
        }
    }

    private static int implMulAdd(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        long j2 = i4 & 4294967295L;
        long j3 = 0;
        int length = (iArr.length - i2) - 1;
        for (int i5 = i3 - 1; i5 >= 0; i5--) {
            long j4 = ((iArr2[i5] & 4294967295L) * j2) + (iArr[length] & 4294967295L) + j3;
            int i6 = length;
            length--;
            iArr[i6] = (int) j4;
            j3 = j4 >>> 32;
        }
        return (int) j3;
    }

    static int addOne(int[] iArr, int i2, int i3, int i4) {
        int length = ((iArr.length - 1) - i3) - i2;
        long j2 = (iArr[length] & 4294967295L) + (i4 & 4294967295L);
        iArr[length] = (int) j2;
        if ((j2 >>> 32) == 0) {
            return 0;
        }
        do {
            i3--;
            if (i3 >= 0) {
                length--;
                if (length < 0) {
                    return 1;
                }
                iArr[length] = iArr[length] + 1;
            } else {
                return 1;
            }
        } while (iArr[length] == 0);
        return 0;
    }

    private BigInteger modPow2(BigInteger bigInteger, int i2) {
        BigInteger bigIntegerMod2 = ONE;
        BigInteger bigIntegerMod22 = mod2(i2);
        int i3 = 0;
        int iBitLength = bigInteger.bitLength();
        if (testBit(0)) {
            iBitLength = i2 - 1 < iBitLength ? i2 - 1 : iBitLength;
        }
        while (i3 < iBitLength) {
            if (bigInteger.testBit(i3)) {
                bigIntegerMod2 = bigIntegerMod2.multiply(bigIntegerMod22).mod2(i2);
            }
            i3++;
            if (i3 < iBitLength) {
                bigIntegerMod22 = bigIntegerMod22.square().mod2(i2);
            }
        }
        return bigIntegerMod2;
    }

    private BigInteger mod2(int i2) {
        if (bitLength() <= i2) {
            return this;
        }
        int i3 = (i2 + 31) >>> 5;
        int[] iArr = new int[i3];
        System.arraycopy(this.mag, this.mag.length - i3, iArr, 0, i3);
        iArr[0] = (int) (iArr[0] & ((1 << (32 - ((i3 << 5) - i2))) - 1));
        return iArr[0] == 0 ? new BigInteger(1, iArr) : new BigInteger(iArr, 1);
    }

    public BigInteger modInverse(BigInteger bigInteger) {
        if (bigInteger.signum != 1) {
            throw new ArithmeticException("BigInteger: modulus not positive");
        }
        if (bigInteger.equals(ONE)) {
            return ZERO;
        }
        BigInteger bigIntegerMod = this;
        if (this.signum < 0 || compareMagnitude(bigInteger) >= 0) {
            bigIntegerMod = mod(bigInteger);
        }
        if (bigIntegerMod.equals(ONE)) {
            return ONE;
        }
        return new MutableBigInteger(bigIntegerMod).mutableModInverse(new MutableBigInteger(bigInteger)).toBigInteger(1);
    }

    public BigInteger shiftLeft(int i2) {
        if (this.signum == 0) {
            return ZERO;
        }
        if (i2 > 0) {
            return new BigInteger(shiftLeft(this.mag, i2), this.signum);
        }
        if (i2 == 0) {
            return this;
        }
        return shiftRightImpl(-i2);
    }

    private static int[] shiftLeft(int[] iArr, int i2) {
        int[] iArr2;
        int i3 = i2 >>> 5;
        int i4 = i2 & 31;
        int length = iArr.length;
        if (i4 == 0) {
            iArr2 = new int[length + i3];
            System.arraycopy(iArr, 0, iArr2, 0, length);
        } else {
            int i5 = 0;
            int i6 = 32 - i4;
            int i7 = iArr[0] >>> i6;
            if (i7 != 0) {
                iArr2 = new int[length + i3 + 1];
                i5 = 0 + 1;
                iArr2[0] = i7;
            } else {
                iArr2 = new int[length + i3];
            }
            int i8 = 0;
            while (i8 < length - 1) {
                int i9 = i5;
                i5++;
                int i10 = i8;
                i8++;
                iArr2[i9] = (iArr[i10] << i4) | (iArr[i8] >>> i6);
            }
            iArr2[i5] = iArr[i8] << i4;
        }
        return iArr2;
    }

    public BigInteger shiftRight(int i2) {
        if (this.signum == 0) {
            return ZERO;
        }
        if (i2 > 0) {
            return shiftRightImpl(i2);
        }
        if (i2 == 0) {
            return this;
        }
        return new BigInteger(shiftLeft(this.mag, -i2), this.signum);
    }

    private BigInteger shiftRightImpl(int i2) {
        int[] iArrJavaIncrement;
        int i3 = i2 >>> 5;
        int i4 = i2 & 31;
        int length = this.mag.length;
        if (i3 >= length) {
            return this.signum >= 0 ? ZERO : negConst[1];
        }
        if (i4 == 0) {
            iArrJavaIncrement = Arrays.copyOf(this.mag, length - i3);
        } else {
            int i5 = 0;
            int i6 = this.mag[0] >>> i4;
            if (i6 != 0) {
                iArrJavaIncrement = new int[length - i3];
                i5 = 0 + 1;
                iArrJavaIncrement[0] = i6;
            } else {
                iArrJavaIncrement = new int[(length - i3) - 1];
            }
            int i7 = 32 - i4;
            int i8 = 0;
            while (i8 < (length - i3) - 1) {
                int i9 = i5;
                i5++;
                int i10 = i8;
                i8++;
                iArrJavaIncrement[i9] = (this.mag[i10] << i7) | (this.mag[i8] >>> i4);
            }
        }
        if (this.signum < 0) {
            boolean z2 = false;
            int i11 = length - i3;
            for (int i12 = length - 1; i12 >= i11 && !z2; i12--) {
                z2 = this.mag[i12] != 0;
            }
            if (!z2 && i4 != 0) {
                z2 = (this.mag[(length - i3) - 1] << (32 - i4)) != 0;
            }
            if (z2) {
                iArrJavaIncrement = javaIncrement(iArrJavaIncrement);
            }
        }
        return new BigInteger(iArrJavaIncrement, this.signum);
    }

    int[] javaIncrement(int[] iArr) {
        int i2 = 0;
        for (int length = iArr.length - 1; length >= 0 && i2 == 0; length--) {
            int i3 = length;
            int i4 = iArr[i3] + 1;
            iArr[i3] = i4;
            i2 = i4;
        }
        if (i2 == 0) {
            iArr = new int[iArr.length + 1];
            iArr[0] = 1;
        }
        return iArr;
    }

    public BigInteger and(BigInteger bigInteger) {
        int[] iArr = new int[Math.max(intLength(), bigInteger.intLength())];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getInt((iArr.length - i2) - 1) & bigInteger.getInt((iArr.length - i2) - 1);
        }
        return valueOf(iArr);
    }

    public BigInteger or(BigInteger bigInteger) {
        int[] iArr = new int[Math.max(intLength(), bigInteger.intLength())];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getInt((iArr.length - i2) - 1) | bigInteger.getInt((iArr.length - i2) - 1);
        }
        return valueOf(iArr);
    }

    public BigInteger xor(BigInteger bigInteger) {
        int[] iArr = new int[Math.max(intLength(), bigInteger.intLength())];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getInt((iArr.length - i2) - 1) ^ bigInteger.getInt((iArr.length - i2) - 1);
        }
        return valueOf(iArr);
    }

    public BigInteger not() {
        int[] iArr = new int[intLength()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getInt((iArr.length - i2) - 1) ^ (-1);
        }
        return valueOf(iArr);
    }

    public BigInteger andNot(BigInteger bigInteger) {
        int[] iArr = new int[Math.max(intLength(), bigInteger.intLength())];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = getInt((iArr.length - i2) - 1) & (bigInteger.getInt((iArr.length - i2) - 1) ^ (-1));
        }
        return valueOf(iArr);
    }

    public boolean testBit(int i2) {
        if (i2 < 0) {
            throw new ArithmeticException("Negative bit address");
        }
        return (getInt(i2 >>> 5) & (1 << (i2 & 31))) != 0;
    }

    public BigInteger setBit(int i2) {
        if (i2 < 0) {
            throw new ArithmeticException("Negative bit address");
        }
        int i3 = i2 >>> 5;
        int[] iArr = new int[Math.max(intLength(), i3 + 2)];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[(iArr.length - i4) - 1] = getInt(i4);
        }
        int length = (iArr.length - i3) - 1;
        iArr[length] = iArr[length] | (1 << (i2 & 31));
        return valueOf(iArr);
    }

    public BigInteger clearBit(int i2) {
        if (i2 < 0) {
            throw new ArithmeticException("Negative bit address");
        }
        int i3 = i2 >>> 5;
        int[] iArr = new int[Math.max(intLength(), ((i2 + 1) >>> 5) + 1)];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[(iArr.length - i4) - 1] = getInt(i4);
        }
        int length = (iArr.length - i3) - 1;
        iArr[length] = iArr[length] & ((1 << (i2 & 31)) ^ (-1));
        return valueOf(iArr);
    }

    public BigInteger flipBit(int i2) {
        if (i2 < 0) {
            throw new ArithmeticException("Negative bit address");
        }
        int i3 = i2 >>> 5;
        int[] iArr = new int[Math.max(intLength(), i3 + 2)];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[(iArr.length - i4) - 1] = getInt(i4);
        }
        int length = (iArr.length - i3) - 1;
        iArr[length] = iArr[length] ^ (1 << (i2 & 31));
        return valueOf(iArr);
    }

    public int getLowestSetBit() {
        int i2;
        int iNumberOfTrailingZeros = this.lowestSetBit - 2;
        if (iNumberOfTrailingZeros == -2) {
            if (this.signum == 0) {
                iNumberOfTrailingZeros = 0 - 1;
            } else {
                int i3 = 0;
                while (true) {
                    i2 = getInt(i3);
                    if (i2 != 0) {
                        break;
                    }
                    i3++;
                }
                iNumberOfTrailingZeros = 0 + (i3 << 5) + Integer.numberOfTrailingZeros(i2);
            }
            this.lowestSetBit = iNumberOfTrailingZeros + 2;
        }
        return iNumberOfTrailingZeros;
    }

    public int bitLength() {
        int i2 = this.bitLength - 1;
        if (i2 == -1) {
            int length = this.mag.length;
            if (length == 0) {
                i2 = 0;
            } else {
                int iBitLengthForInt = ((length - 1) << 5) + bitLengthForInt(this.mag[0]);
                if (this.signum < 0) {
                    boolean z2 = Integer.bitCount(this.mag[0]) == 1;
                    for (int i3 = 1; i3 < length && z2; i3++) {
                        z2 = this.mag[i3] == 0;
                    }
                    i2 = z2 ? iBitLengthForInt - 1 : iBitLengthForInt;
                } else {
                    i2 = iBitLengthForInt;
                }
            }
            this.bitLength = i2 + 1;
        }
        return i2;
    }

    public int bitCount() {
        int iNumberOfTrailingZeros = this.bitCount - 1;
        if (iNumberOfTrailingZeros == -1) {
            iNumberOfTrailingZeros = 0;
            for (int i2 = 0; i2 < this.mag.length; i2++) {
                iNumberOfTrailingZeros += Integer.bitCount(this.mag[i2]);
            }
            if (this.signum < 0) {
                int i3 = 0;
                int length = this.mag.length - 1;
                while (this.mag[length] == 0) {
                    i3 += 32;
                    length--;
                }
                iNumberOfTrailingZeros += (i3 + Integer.numberOfTrailingZeros(this.mag[length])) - 1;
            }
            this.bitCount = iNumberOfTrailingZeros + 1;
        }
        return iNumberOfTrailingZeros;
    }

    public boolean isProbablePrime(int i2) {
        if (i2 <= 0) {
            return true;
        }
        BigInteger bigIntegerAbs = abs();
        if (bigIntegerAbs.equals(TWO)) {
            return true;
        }
        if (!bigIntegerAbs.testBit(0) || bigIntegerAbs.equals(ONE)) {
            return false;
        }
        return bigIntegerAbs.primeToCertainty(i2, null);
    }

    @Override // java.lang.Comparable
    public int compareTo(BigInteger bigInteger) {
        if (this.signum != bigInteger.signum) {
            return this.signum > bigInteger.signum ? 1 : -1;
        }
        switch (this.signum) {
            case -1:
                return bigInteger.compareMagnitude(this);
            case 1:
                return compareMagnitude(bigInteger);
            default:
                return 0;
        }
    }

    final int compareMagnitude(BigInteger bigInteger) {
        int[] iArr = this.mag;
        int length = iArr.length;
        int[] iArr2 = bigInteger.mag;
        int length2 = iArr2.length;
        if (length < length2) {
            return -1;
        }
        if (length > length2) {
            return 1;
        }
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = iArr[i2];
            int i4 = iArr2[i2];
            if (i3 != i4) {
                return (((long) i3) & 4294967295L) < (((long) i4) & 4294967295L) ? -1 : 1;
            }
        }
        return 0;
    }

    final int compareMagnitude(long j2) {
        if (!$assertionsDisabled && j2 == Long.MIN_VALUE) {
            throw new AssertionError();
        }
        int[] iArr = this.mag;
        int length = iArr.length;
        if (length > 2) {
            return 1;
        }
        if (j2 < 0) {
            j2 = -j2;
        }
        int i2 = (int) (j2 >>> 32);
        if (i2 == 0) {
            if (length < 1) {
                return -1;
            }
            if (length > 1) {
                return 1;
            }
            int i3 = iArr[0];
            int i4 = (int) j2;
            if (i3 != i4) {
                return (((long) i3) & 4294967295L) < (((long) i4) & 4294967295L) ? -1 : 1;
            }
            return 0;
        }
        if (length < 2) {
            return -1;
        }
        int i5 = iArr[0];
        if (i5 != i2) {
            return (((long) i5) & 4294967295L) < (((long) i2) & 4294967295L) ? -1 : 1;
        }
        int i6 = iArr[1];
        int i7 = (int) j2;
        if (i6 != i7) {
            return (((long) i6) & 4294967295L) < (((long) i7) & 4294967295L) ? -1 : 1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BigInteger)) {
            return false;
        }
        BigInteger bigInteger = (BigInteger) obj;
        if (bigInteger.signum != this.signum) {
            return false;
        }
        int[] iArr = this.mag;
        int length = iArr.length;
        int[] iArr2 = bigInteger.mag;
        if (length != iArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (iArr2[i2] != iArr[i2]) {
                return false;
            }
        }
        return true;
    }

    public BigInteger min(BigInteger bigInteger) {
        return compareTo(bigInteger) < 0 ? this : bigInteger;
    }

    public BigInteger max(BigInteger bigInteger) {
        return compareTo(bigInteger) > 0 ? this : bigInteger;
    }

    public int hashCode() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mag.length; i3++) {
            i2 = (int) ((31 * i2) + (this.mag[i3] & 4294967295L));
        }
        return i2 * this.signum;
    }

    public String toString(int i2) {
        if (this.signum == 0) {
            return "0";
        }
        if (i2 < 2 || i2 > 36) {
            i2 = 10;
        }
        if (this.mag.length <= 20) {
            return smallToString(i2);
        }
        StringBuilder sb = new StringBuilder();
        if (this.signum < 0) {
            toString(negate(), sb, i2, 0);
            sb.insert(0, '-');
        } else {
            toString(this, sb, i2, 0);
        }
        return sb.toString();
    }

    private String smallToString(int i2) {
        if (this.signum == 0) {
            return "0";
        }
        String[] strArr = new String[((4 * this.mag.length) + 6) / 7];
        BigInteger bigIntegerAbs = abs();
        int i3 = 0;
        while (bigIntegerAbs.signum != 0) {
            BigInteger bigInteger = longRadix[i2];
            MutableBigInteger mutableBigInteger = new MutableBigInteger();
            MutableBigInteger mutableBigIntegerDivide = new MutableBigInteger(bigIntegerAbs.mag).divide(new MutableBigInteger(bigInteger.mag), mutableBigInteger);
            BigInteger bigInteger2 = mutableBigInteger.toBigInteger(bigIntegerAbs.signum * bigInteger.signum);
            int i4 = i3;
            i3++;
            strArr[i4] = Long.toString(mutableBigIntegerDivide.toBigInteger(bigIntegerAbs.signum * bigInteger.signum).longValue(), i2);
            bigIntegerAbs = bigInteger2;
        }
        StringBuilder sb = new StringBuilder((i3 * digitsPerLong[i2]) + 1);
        if (this.signum < 0) {
            sb.append('-');
        }
        sb.append(strArr[i3 - 1]);
        for (int i5 = i3 - 2; i5 >= 0; i5--) {
            int length = digitsPerLong[i2] - strArr[i5].length();
            if (length != 0) {
                sb.append(zeros[length]);
            }
            sb.append(strArr[i5]);
        }
        return sb.toString();
    }

    private static void toString(BigInteger bigInteger, StringBuilder sb, int i2, int i3) {
        if (bigInteger.mag.length <= 20) {
            String strSmallToString = bigInteger.smallToString(i2);
            if (strSmallToString.length() < i3 && sb.length() > 0) {
                for (int length = strSmallToString.length(); length < i3; length++) {
                    sb.append('0');
                }
            }
            sb.append(strSmallToString);
            return;
        }
        int iRound = (int) Math.round((Math.log((bigInteger.bitLength() * LOG_TWO) / logCache[i2]) / LOG_TWO) - 1.0d);
        BigInteger[] bigIntegerArrDivideAndRemainder = bigInteger.divideAndRemainder(getRadixConversionCache(i2, iRound));
        int i4 = 1 << iRound;
        toString(bigIntegerArrDivideAndRemainder[0], sb, i2, i3 - i4);
        toString(bigIntegerArrDivideAndRemainder[1], sb, i2, i4);
    }

    private static BigInteger getRadixConversionCache(int i2, int i3) {
        BigInteger[] bigIntegerArr = powerCache[i2];
        if (i3 < bigIntegerArr.length) {
            return bigIntegerArr[i3];
        }
        int length = bigIntegerArr.length;
        BigInteger[] bigIntegerArr2 = (BigInteger[]) Arrays.copyOf(bigIntegerArr, i3 + 1);
        for (int i4 = length; i4 <= i3; i4++) {
            bigIntegerArr2[i4] = bigIntegerArr2[i4 - 1].pow(2);
        }
        BigInteger[][] bigIntegerArr3 = powerCache;
        if (i3 >= bigIntegerArr3[i2].length) {
            BigInteger[][] bigIntegerArr4 = (BigInteger[][]) bigIntegerArr3.clone();
            bigIntegerArr4[i2] = bigIntegerArr2;
            powerCache = bigIntegerArr4;
        }
        return bigIntegerArr2[i3];
    }

    public String toString() {
        return toString(10);
    }

    public byte[] toByteArray() {
        int iBitLength = (bitLength() / 8) + 1;
        byte[] bArr = new byte[iBitLength];
        int i2 = 4;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = iBitLength - 1; i5 >= 0; i5--) {
            if (i2 == 4) {
                int i6 = i4;
                i4++;
                i3 = getInt(i6);
                i2 = 1;
            } else {
                i3 >>>= 8;
                i2++;
            }
            bArr[i5] = (byte) i3;
        }
        return bArr;
    }

    @Override // java.lang.Number
    public int intValue() {
        return getInt(0);
    }

    @Override // java.lang.Number
    public long longValue() {
        long j2 = 0;
        for (int i2 = 1; i2 >= 0; i2--) {
            j2 = (j2 << 32) + (getInt(i2) & 4294967295L);
        }
        return j2;
    }

    @Override // java.lang.Number
    public float floatValue() {
        int i2;
        if (this.signum == 0) {
            return 0.0f;
        }
        int length = (((this.mag.length - 1) << 5) + bitLengthForInt(this.mag[0])) - 1;
        if (length < 63) {
            return longValue();
        }
        if (length > 127) {
            return this.signum > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int i3 = length - 24;
        int i4 = i3 & 31;
        int i5 = 32 - i4;
        if (i4 == 0) {
            i2 = this.mag[0];
        } else {
            i2 = this.mag[0] >>> i4;
            if (i2 == 0) {
                i2 = (this.mag[0] << i5) | (this.mag[1] >>> i4);
            }
        }
        int i6 = (i2 >> 1) & FloatConsts.SIGNIF_BIT_MASK;
        return Float.intBitsToFloat((((length + 127) << 23) + ((i2 & 1) != 0 && ((i6 & 1) != 0 || abs().getLowestSetBit() < i3) ? i6 + 1 : i6)) | (this.signum & Integer.MIN_VALUE));
    }

    @Override // java.lang.Number
    public double doubleValue() {
        int i2;
        int i3;
        if (this.signum == 0) {
            return 0.0d;
        }
        int length = (((this.mag.length - 1) << 5) + bitLengthForInt(this.mag[0])) - 1;
        if (length < 63) {
            return longValue();
        }
        if (length > 1023) {
            return this.signum > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        int i4 = length - 53;
        int i5 = i4 & 31;
        int i6 = 32 - i5;
        if (i5 == 0) {
            i2 = this.mag[0];
            i3 = this.mag[1];
        } else {
            i2 = this.mag[0] >>> i5;
            i3 = (this.mag[0] << i6) | (this.mag[1] >>> i5);
            if (i2 == 0) {
                i2 = i3;
                i3 = (this.mag[1] << i6) | (this.mag[2] >>> i5);
            }
        }
        long j2 = ((i2 & 4294967295L) << 32) | (i3 & 4294967295L);
        long j3 = (j2 >> 1) & DoubleConsts.SIGNIF_BIT_MASK;
        return Double.longBitsToDouble((((length + 1023) << 52) + (((j2 & 1) > 0L ? 1 : ((j2 & 1) == 0L ? 0 : -1)) != 0 && (((j3 & 1) > 0L ? 1 : ((j3 & 1) == 0L ? 0 : -1)) != 0 || abs().getLowestSetBit() < i4) ? j3 + 1 : j3)) | (this.signum & Long.MIN_VALUE));
    }

    private static int[] stripLeadingZeroInts(int[] iArr) {
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length && iArr[i2] == 0) {
            i2++;
        }
        return Arrays.copyOfRange(iArr, i2, length);
    }

    private static int[] trustedStripLeadingZeroInts(int[] iArr) {
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length && iArr[i2] == 0) {
            i2++;
        }
        return i2 == 0 ? iArr : Arrays.copyOfRange(iArr, i2, length);
    }

    private static int[] stripLeadingZeroBytes(byte[] bArr) {
        int length = bArr.length;
        int i2 = 0;
        while (i2 < length && bArr[i2] == 0) {
            i2++;
        }
        int i3 = ((length - i2) + 3) >>> 2;
        int[] iArr = new int[i3];
        int i4 = length - 1;
        for (int i5 = i3 - 1; i5 >= 0; i5--) {
            int i6 = i4;
            i4--;
            iArr[i5] = bArr[i6] & 255;
            int iMin = Math.min(3, (i4 - i2) + 1);
            for (int i7 = 8; i7 <= (iMin << 3); i7 += 8) {
                int i8 = i5;
                int i9 = i4;
                i4--;
                iArr[i8] = iArr[i8] | ((bArr[i9] & 255) << i7);
            }
        }
        return iArr;
    }

    private static int[] makePositive(byte[] bArr) {
        int length = bArr.length;
        int i2 = 0;
        while (i2 < length && bArr[i2] == -1) {
            i2++;
        }
        int i3 = i2;
        while (i3 < length && bArr[i3] == 0) {
            i3++;
        }
        int i4 = (((length - i2) + (i3 == length ? 1 : 0)) + 3) >>> 2;
        int[] iArr = new int[i4];
        int i5 = length - 1;
        for (int i6 = i4 - 1; i6 >= 0; i6--) {
            int i7 = i5;
            i5--;
            iArr[i6] = bArr[i7] & 255;
            int iMin = Math.min(3, (i5 - i2) + 1);
            if (iMin < 0) {
                iMin = 0;
            }
            for (int i8 = 8; i8 <= 8 * iMin; i8 += 8) {
                int i9 = i6;
                int i10 = i5;
                i5--;
                iArr[i9] = iArr[i9] | ((bArr[i10] & 255) << i8);
            }
            iArr[i6] = (iArr[i6] ^ (-1)) & ((-1) >>> (8 * (3 - iMin)));
        }
        for (int length2 = iArr.length - 1; length2 >= 0; length2--) {
            iArr[length2] = (int) ((iArr[length2] & 4294967295L) + 1);
            if (iArr[length2] != 0) {
                break;
            }
        }
        return iArr;
    }

    private static int[] makePositive(int[] iArr) {
        int i2 = 0;
        while (i2 < iArr.length && iArr[i2] == -1) {
            i2++;
        }
        int i3 = i2;
        while (i3 < iArr.length && iArr[i3] == 0) {
            i3++;
        }
        int i4 = i3 == iArr.length ? 1 : 0;
        int[] iArr2 = new int[(iArr.length - i2) + i4];
        for (int i5 = i2; i5 < iArr.length; i5++) {
            iArr2[(i5 - i2) + i4] = iArr[i5] ^ (-1);
        }
        int length = iArr2.length - 1;
        while (true) {
            int i6 = length;
            int i7 = iArr2[i6] + 1;
            iArr2[i6] = i7;
            if (i7 != 0) {
                return iArr2;
            }
            length--;
        }
    }

    private int intLength() {
        return (bitLength() >>> 5) + 1;
    }

    private int signBit() {
        return this.signum < 0 ? 1 : 0;
    }

    private int signInt() {
        return this.signum < 0 ? -1 : 0;
    }

    private int getInt(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 >= this.mag.length) {
            return signInt();
        }
        int i3 = this.mag[(this.mag.length - i2) - 1];
        return this.signum >= 0 ? i3 : i2 <= firstNonzeroIntNum() ? -i3 : i3 ^ (-1);
    }

    private int firstNonzeroIntNum() {
        int i2 = this.firstNonzeroIntNum - 2;
        if (i2 == -2) {
            int length = this.mag.length;
            int i3 = length - 1;
            while (i3 >= 0 && this.mag[i3] == 0) {
                i3--;
            }
            i2 = (length - i3) - 1;
            this.firstNonzeroIntNum = i2 + 2;
        }
        return i2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        int i2 = fields.get("signum", -2);
        if (i2 < -1 || i2 > 1) {
            String str = "BigInteger: Invalid signum value";
            if (fields.defaulted("signum")) {
                str = "BigInteger: Signum not present in stream";
            }
            throw new StreamCorruptedException(str);
        }
        int[] iArrStripLeadingZeroBytes = stripLeadingZeroBytes((byte[]) ((byte[]) fields.get("magnitude", (Object) null)).clone());
        if ((iArrStripLeadingZeroBytes.length == 0) != (i2 == 0)) {
            String str2 = "BigInteger: signum-magnitude mismatch";
            if (fields.defaulted("magnitude")) {
                str2 = "BigInteger: Magnitude not present in stream";
            }
            throw new StreamCorruptedException(str2);
        }
        if (iArrStripLeadingZeroBytes.length > 67108864 || (iArrStripLeadingZeroBytes.length == 67108864 && iArrStripLeadingZeroBytes[0] < 0)) {
            throw new StreamCorruptedException("BigInteger: Out of the supported range");
        }
        UnsafeHolder.putSignAndMag(this, i2, iArrStripLeadingZeroBytes);
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Deserialized BigInteger objects need data");
    }

    /* loaded from: rt.jar:java/math/BigInteger$UnsafeHolder.class */
    private static class UnsafeHolder {
        private static final Unsafe unsafe;
        private static final long signumOffset;
        private static final long magOffset;

        private UnsafeHolder() {
        }

        static {
            try {
                unsafe = Unsafe.getUnsafe();
                signumOffset = unsafe.objectFieldOffset(BigInteger.class.getDeclaredField("signum"));
                magOffset = unsafe.objectFieldOffset(BigInteger.class.getDeclaredField("mag"));
            } catch (Exception e2) {
                throw new ExceptionInInitializerError(e2);
            }
        }

        static void putSignAndMag(BigInteger bigInteger, int i2, int[] iArr) {
            unsafe.putIntVolatile(bigInteger, signumOffset, i2);
            unsafe.putObjectVolatile(bigInteger, magOffset, iArr);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("signum", this.signum);
        putFieldPutFields.put("magnitude", magSerializedForm());
        putFieldPutFields.put("bitCount", -1);
        putFieldPutFields.put("bitLength", -1);
        putFieldPutFields.put("lowestSetBit", -2);
        putFieldPutFields.put("firstNonzeroByteNum", -2);
        objectOutputStream.writeFields();
    }

    private byte[] magSerializedForm() {
        int length = this.mag.length;
        int iBitLengthForInt = ((length == 0 ? 0 : ((length - 1) << 5) + bitLengthForInt(this.mag[0])) + 7) >>> 3;
        byte[] bArr = new byte[iBitLengthForInt];
        int i2 = 4;
        int i3 = length - 1;
        int i4 = 0;
        for (int i5 = iBitLengthForInt - 1; i5 >= 0; i5--) {
            if (i2 == 4) {
                int i6 = i3;
                i3--;
                i4 = this.mag[i6];
                i2 = 1;
            } else {
                i4 >>>= 8;
                i2++;
            }
            bArr[i5] = (byte) i4;
        }
        return bArr;
    }

    public long longValueExact() {
        if (this.mag.length <= 2 && bitLength() <= 63) {
            return longValue();
        }
        throw new ArithmeticException("BigInteger out of long range");
    }

    public int intValueExact() {
        if (this.mag.length <= 1 && bitLength() <= 31) {
            return intValue();
        }
        throw new ArithmeticException("BigInteger out of int range");
    }

    public short shortValueExact() {
        int iIntValue;
        if (this.mag.length <= 1 && bitLength() <= 31 && (iIntValue = intValue()) >= -32768 && iIntValue <= 32767) {
            return shortValue();
        }
        throw new ArithmeticException("BigInteger out of short range");
    }

    public byte byteValueExact() {
        int iIntValue;
        if (this.mag.length <= 1 && bitLength() <= 31 && (iIntValue = intValue()) >= -128 && iIntValue <= 127) {
            return byteValue();
        }
        throw new ArithmeticException("BigInteger out of byte range");
    }
}
