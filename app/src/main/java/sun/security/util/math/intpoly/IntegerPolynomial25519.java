package sun.security.util.math.intpoly;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial25519.class */
public class IntegerPolynomial25519 extends IntegerPolynomial {
    private static final int POWER = 255;
    private static final int SUBTRAHEND = 19;
    private static final int NUM_LIMBS = 10;
    private static final int BITS_PER_LIMB = 26;
    public static final BigInteger MODULUS = TWO.pow(255).subtract(BigInteger.valueOf(19));
    private static final int BIT_OFFSET = 5;
    private static final int LIMB_MASK = 67108863;
    private static final int RIGHT_BIT_OFFSET = 21;

    public IntegerPolynomial25519() {
        super(26, 10, 1, MODULUS);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[this.numLimbs - 1] >> 21;
        int i2 = this.numLimbs - 1;
        jArr[i2] = jArr[i2] - (j2 << 21);
        jArr[0] = jArr[0] + (j2 * 19);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        long jCarryValue = carryValue(jArr[8]);
        jArr[8] = jArr[8] - (jCarryValue << 26);
        jArr[9] = jArr[9] + jCarryValue;
        long jCarryValue2 = carryValue(jArr[9]);
        jArr[9] = jArr[9] - (jCarryValue2 << 26);
        long j2 = jCarryValue2 * 19;
        jArr[0] = jArr[0] + ((j2 << 5) & 67108863);
        jArr[1] = jArr[1] + (j2 >> 21);
        carry(jArr, 0, 9);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[0] * jArr2[5]) + (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]) + (jArr[5] * jArr2[0]), (jArr[0] * jArr2[6]) + (jArr[1] * jArr2[5]) + (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]) + (jArr[5] * jArr2[1]) + (jArr[6] * jArr2[0]), (jArr[0] * jArr2[7]) + (jArr[1] * jArr2[6]) + (jArr[2] * jArr2[5]) + (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]) + (jArr[5] * jArr2[2]) + (jArr[6] * jArr2[1]) + (jArr[7] * jArr2[0]), (jArr[0] * jArr2[8]) + (jArr[1] * jArr2[7]) + (jArr[2] * jArr2[6]) + (jArr[3] * jArr2[5]) + (jArr[4] * jArr2[4]) + (jArr[5] * jArr2[3]) + (jArr[6] * jArr2[2]) + (jArr[7] * jArr2[1]) + (jArr[8] * jArr2[0]), (jArr[0] * jArr2[9]) + (jArr[1] * jArr2[8]) + (jArr[2] * jArr2[7]) + (jArr[3] * jArr2[6]) + (jArr[4] * jArr2[5]) + (jArr[5] * jArr2[4]) + (jArr[6] * jArr2[3]) + (jArr[7] * jArr2[2]) + (jArr[8] * jArr2[1]) + (jArr[9] * jArr2[0]), (jArr[1] * jArr2[9]) + (jArr[2] * jArr2[8]) + (jArr[3] * jArr2[7]) + (jArr[4] * jArr2[6]) + (jArr[5] * jArr2[5]) + (jArr[6] * jArr2[4]) + (jArr[7] * jArr2[3]) + (jArr[8] * jArr2[2]) + (jArr[9] * jArr2[1]), (jArr[2] * jArr2[9]) + (jArr[3] * jArr2[8]) + (jArr[4] * jArr2[7]) + (jArr[5] * jArr2[6]) + (jArr[6] * jArr2[5]) + (jArr[7] * jArr2[4]) + (jArr[8] * jArr2[3]) + (jArr[9] * jArr2[2]), (jArr[3] * jArr2[9]) + (jArr[4] * jArr2[8]) + (jArr[5] * jArr2[7]) + (jArr[6] * jArr2[6]) + (jArr[7] * jArr2[5]) + (jArr[8] * jArr2[4]) + (jArr[9] * jArr2[3]), (jArr[4] * jArr2[9]) + (jArr[5] * jArr2[8]) + (jArr[6] * jArr2[7]) + (jArr[7] * jArr2[6]) + (jArr[8] * jArr2[5]) + (jArr[9] * jArr2[4]), (jArr[5] * jArr2[9]) + (jArr[6] * jArr2[8]) + (jArr[7] * jArr2[7]) + (jArr[8] * jArr2[6]) + (jArr[9] * jArr2[5]), (jArr[6] * jArr2[9]) + (jArr[7] * jArr2[8]) + (jArr[8] * jArr2[7]) + (jArr[9] * jArr2[6]), (jArr[7] * jArr2[9]) + (jArr[8] * jArr2[8]) + (jArr[9] * jArr2[7]), (jArr[8] * jArr2[9]) + (jArr[9] * jArr2[8]), jArr[9] * jArr2[9]);
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20) {
        long j21 = j19 * 19;
        long j22 = j9 + ((j21 << 5) & 67108863);
        long j23 = j10 + (j21 >> 21);
        long j24 = j20 * 19;
        long j25 = j23 + ((j24 << 5) & 67108863);
        long j26 = j11 + (j24 >> 21);
        long jCarryValue = carryValue(j25);
        jArr[8] = j25 - (jCarryValue << 26);
        long j27 = j26 + jCarryValue;
        long jCarryValue2 = carryValue(j27);
        jArr[9] = j27 - (jCarryValue2 << 26);
        long j28 = (j12 + jCarryValue2) * 19;
        jArr[0] = j2 + ((j28 << 5) & 67108863);
        long j29 = j3 + (j28 >> 21);
        long j30 = j13 * 19;
        jArr[1] = j29 + ((j30 << 5) & 67108863);
        long j31 = j4 + (j30 >> 21);
        long j32 = j14 * 19;
        jArr[2] = j31 + ((j32 << 5) & 67108863);
        long j33 = j5 + (j32 >> 21);
        long j34 = j15 * 19;
        jArr[3] = j33 + ((j34 << 5) & 67108863);
        long j35 = j6 + (j34 >> 21);
        long j36 = j16 * 19;
        jArr[4] = j35 + ((j36 << 5) & 67108863);
        long j37 = j7 + (j36 >> 21);
        long j38 = j17 * 19;
        jArr[5] = j37 + ((j38 << 5) & 67108863);
        long j39 = j8 + (j38 >> 21);
        long j40 = j18 * 19;
        jArr[6] = j39 + ((j40 << 5) & 67108863);
        jArr[7] = j22 + (j40 >> 21);
        carry(jArr, 0, 9);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (jArr[1] * jArr[1]) + (2 * jArr[0] * jArr[2]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (jArr[2] * jArr[2]) + (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))), 2 * ((jArr[0] * jArr[5]) + (jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (jArr[3] * jArr[3]) + (2 * ((jArr[0] * jArr[6]) + (jArr[1] * jArr[5]) + (jArr[2] * jArr[4]))), 2 * ((jArr[0] * jArr[7]) + (jArr[1] * jArr[6]) + (jArr[2] * jArr[5]) + (jArr[3] * jArr[4])), (jArr[4] * jArr[4]) + (2 * ((jArr[0] * jArr[8]) + (jArr[1] * jArr[7]) + (jArr[2] * jArr[6]) + (jArr[3] * jArr[5]))), 2 * ((jArr[0] * jArr[9]) + (jArr[1] * jArr[8]) + (jArr[2] * jArr[7]) + (jArr[3] * jArr[6]) + (jArr[4] * jArr[5])), (jArr[5] * jArr[5]) + (2 * ((jArr[1] * jArr[9]) + (jArr[2] * jArr[8]) + (jArr[3] * jArr[7]) + (jArr[4] * jArr[6]))), 2 * ((jArr[2] * jArr[9]) + (jArr[3] * jArr[8]) + (jArr[4] * jArr[7]) + (jArr[5] * jArr[6])), (jArr[6] * jArr[6]) + (2 * ((jArr[3] * jArr[9]) + (jArr[4] * jArr[8]) + (jArr[5] * jArr[7]))), 2 * ((jArr[4] * jArr[9]) + (jArr[5] * jArr[8]) + (jArr[6] * jArr[7])), (jArr[7] * jArr[7]) + (2 * ((jArr[5] * jArr[9]) + (jArr[6] * jArr[8]))), 2 * ((jArr[6] * jArr[9]) + (jArr[7] * jArr[8])), (jArr[8] * jArr[8]) + (2 * jArr[7] * jArr[9]), 2 * jArr[8] * jArr[9], jArr[9] * jArr[9]);
    }
}
