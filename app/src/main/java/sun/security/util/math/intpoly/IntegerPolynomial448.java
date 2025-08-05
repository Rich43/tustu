package sun.security.util.math.intpoly;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial448.class */
public class IntegerPolynomial448 extends IntegerPolynomial {
    private static final int NUM_LIMBS = 16;
    private static final int BITS_PER_LIMB = 28;
    private static final int POWER = 448;
    public static final BigInteger MODULUS = TWO.pow(POWER).subtract(TWO.pow(224)).subtract(BigInteger.valueOf(1));

    public IntegerPolynomial448() {
        super(28, 16, 1, MODULUS);
    }

    private void modReduceIn(long[] jArr, int i2, long j2) {
        int i3 = i2 - 16;
        jArr[i3] = jArr[i3] + j2;
        int i4 = i2 - 8;
        jArr[i4] = jArr[i4] + j2;
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[this.numLimbs - 1] >> this.bitsPerLimb;
        int i2 = this.numLimbs - 1;
        jArr[i2] = jArr[i2] - (j2 << this.bitsPerLimb);
        modReduceIn(jArr, this.numLimbs, j2);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        long jCarryValue = carryValue(jArr[14]);
        jArr[14] = jArr[14] - (jCarryValue << 28);
        jArr[15] = jArr[15] + jCarryValue;
        long jCarryValue2 = carryValue(jArr[15]);
        jArr[15] = jArr[15] - (jCarryValue2 << 28);
        jArr[0] = jArr[0] + jCarryValue2;
        jArr[8] = jArr[8] + jCarryValue2;
        carry(jArr, 0, 15);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[0] * jArr2[5]) + (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]) + (jArr[5] * jArr2[0]), (jArr[0] * jArr2[6]) + (jArr[1] * jArr2[5]) + (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]) + (jArr[5] * jArr2[1]) + (jArr[6] * jArr2[0]), (jArr[0] * jArr2[7]) + (jArr[1] * jArr2[6]) + (jArr[2] * jArr2[5]) + (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]) + (jArr[5] * jArr2[2]) + (jArr[6] * jArr2[1]) + (jArr[7] * jArr2[0]), (jArr[0] * jArr2[8]) + (jArr[1] * jArr2[7]) + (jArr[2] * jArr2[6]) + (jArr[3] * jArr2[5]) + (jArr[4] * jArr2[4]) + (jArr[5] * jArr2[3]) + (jArr[6] * jArr2[2]) + (jArr[7] * jArr2[1]) + (jArr[8] * jArr2[0]), (jArr[0] * jArr2[9]) + (jArr[1] * jArr2[8]) + (jArr[2] * jArr2[7]) + (jArr[3] * jArr2[6]) + (jArr[4] * jArr2[5]) + (jArr[5] * jArr2[4]) + (jArr[6] * jArr2[3]) + (jArr[7] * jArr2[2]) + (jArr[8] * jArr2[1]) + (jArr[9] * jArr2[0]), (jArr[0] * jArr2[10]) + (jArr[1] * jArr2[9]) + (jArr[2] * jArr2[8]) + (jArr[3] * jArr2[7]) + (jArr[4] * jArr2[6]) + (jArr[5] * jArr2[5]) + (jArr[6] * jArr2[4]) + (jArr[7] * jArr2[3]) + (jArr[8] * jArr2[2]) + (jArr[9] * jArr2[1]) + (jArr[10] * jArr2[0]), (jArr[0] * jArr2[11]) + (jArr[1] * jArr2[10]) + (jArr[2] * jArr2[9]) + (jArr[3] * jArr2[8]) + (jArr[4] * jArr2[7]) + (jArr[5] * jArr2[6]) + (jArr[6] * jArr2[5]) + (jArr[7] * jArr2[4]) + (jArr[8] * jArr2[3]) + (jArr[9] * jArr2[2]) + (jArr[10] * jArr2[1]) + (jArr[11] * jArr2[0]), (jArr[0] * jArr2[12]) + (jArr[1] * jArr2[11]) + (jArr[2] * jArr2[10]) + (jArr[3] * jArr2[9]) + (jArr[4] * jArr2[8]) + (jArr[5] * jArr2[7]) + (jArr[6] * jArr2[6]) + (jArr[7] * jArr2[5]) + (jArr[8] * jArr2[4]) + (jArr[9] * jArr2[3]) + (jArr[10] * jArr2[2]) + (jArr[11] * jArr2[1]) + (jArr[12] * jArr2[0]), (jArr[0] * jArr2[13]) + (jArr[1] * jArr2[12]) + (jArr[2] * jArr2[11]) + (jArr[3] * jArr2[10]) + (jArr[4] * jArr2[9]) + (jArr[5] * jArr2[8]) + (jArr[6] * jArr2[7]) + (jArr[7] * jArr2[6]) + (jArr[8] * jArr2[5]) + (jArr[9] * jArr2[4]) + (jArr[10] * jArr2[3]) + (jArr[11] * jArr2[2]) + (jArr[12] * jArr2[1]) + (jArr[13] * jArr2[0]), (jArr[0] * jArr2[14]) + (jArr[1] * jArr2[13]) + (jArr[2] * jArr2[12]) + (jArr[3] * jArr2[11]) + (jArr[4] * jArr2[10]) + (jArr[5] * jArr2[9]) + (jArr[6] * jArr2[8]) + (jArr[7] * jArr2[7]) + (jArr[8] * jArr2[6]) + (jArr[9] * jArr2[5]) + (jArr[10] * jArr2[4]) + (jArr[11] * jArr2[3]) + (jArr[12] * jArr2[2]) + (jArr[13] * jArr2[1]) + (jArr[14] * jArr2[0]), (jArr[0] * jArr2[15]) + (jArr[1] * jArr2[14]) + (jArr[2] * jArr2[13]) + (jArr[3] * jArr2[12]) + (jArr[4] * jArr2[11]) + (jArr[5] * jArr2[10]) + (jArr[6] * jArr2[9]) + (jArr[7] * jArr2[8]) + (jArr[8] * jArr2[7]) + (jArr[9] * jArr2[6]) + (jArr[10] * jArr2[5]) + (jArr[11] * jArr2[4]) + (jArr[12] * jArr2[3]) + (jArr[13] * jArr2[2]) + (jArr[14] * jArr2[1]) + (jArr[15] * jArr2[0]), (jArr[1] * jArr2[15]) + (jArr[2] * jArr2[14]) + (jArr[3] * jArr2[13]) + (jArr[4] * jArr2[12]) + (jArr[5] * jArr2[11]) + (jArr[6] * jArr2[10]) + (jArr[7] * jArr2[9]) + (jArr[8] * jArr2[8]) + (jArr[9] * jArr2[7]) + (jArr[10] * jArr2[6]) + (jArr[11] * jArr2[5]) + (jArr[12] * jArr2[4]) + (jArr[13] * jArr2[3]) + (jArr[14] * jArr2[2]) + (jArr[15] * jArr2[1]), (jArr[2] * jArr2[15]) + (jArr[3] * jArr2[14]) + (jArr[4] * jArr2[13]) + (jArr[5] * jArr2[12]) + (jArr[6] * jArr2[11]) + (jArr[7] * jArr2[10]) + (jArr[8] * jArr2[9]) + (jArr[9] * jArr2[8]) + (jArr[10] * jArr2[7]) + (jArr[11] * jArr2[6]) + (jArr[12] * jArr2[5]) + (jArr[13] * jArr2[4]) + (jArr[14] * jArr2[3]) + (jArr[15] * jArr2[2]), (jArr[3] * jArr2[15]) + (jArr[4] * jArr2[14]) + (jArr[5] * jArr2[13]) + (jArr[6] * jArr2[12]) + (jArr[7] * jArr2[11]) + (jArr[8] * jArr2[10]) + (jArr[9] * jArr2[9]) + (jArr[10] * jArr2[8]) + (jArr[11] * jArr2[7]) + (jArr[12] * jArr2[6]) + (jArr[13] * jArr2[5]) + (jArr[14] * jArr2[4]) + (jArr[15] * jArr2[3]), (jArr[4] * jArr2[15]) + (jArr[5] * jArr2[14]) + (jArr[6] * jArr2[13]) + (jArr[7] * jArr2[12]) + (jArr[8] * jArr2[11]) + (jArr[9] * jArr2[10]) + (jArr[10] * jArr2[9]) + (jArr[11] * jArr2[8]) + (jArr[12] * jArr2[7]) + (jArr[13] * jArr2[6]) + (jArr[14] * jArr2[5]) + (jArr[15] * jArr2[4]), (jArr[5] * jArr2[15]) + (jArr[6] * jArr2[14]) + (jArr[7] * jArr2[13]) + (jArr[8] * jArr2[12]) + (jArr[9] * jArr2[11]) + (jArr[10] * jArr2[10]) + (jArr[11] * jArr2[9]) + (jArr[12] * jArr2[8]) + (jArr[13] * jArr2[7]) + (jArr[14] * jArr2[6]) + (jArr[15] * jArr2[5]), (jArr[6] * jArr2[15]) + (jArr[7] * jArr2[14]) + (jArr[8] * jArr2[13]) + (jArr[9] * jArr2[12]) + (jArr[10] * jArr2[11]) + (jArr[11] * jArr2[10]) + (jArr[12] * jArr2[9]) + (jArr[13] * jArr2[8]) + (jArr[14] * jArr2[7]) + (jArr[15] * jArr2[6]), (jArr[7] * jArr2[15]) + (jArr[8] * jArr2[14]) + (jArr[9] * jArr2[13]) + (jArr[10] * jArr2[12]) + (jArr[11] * jArr2[11]) + (jArr[12] * jArr2[10]) + (jArr[13] * jArr2[9]) + (jArr[14] * jArr2[8]) + (jArr[15] * jArr2[7]), (jArr[8] * jArr2[15]) + (jArr[9] * jArr2[14]) + (jArr[10] * jArr2[13]) + (jArr[11] * jArr2[12]) + (jArr[12] * jArr2[11]) + (jArr[13] * jArr2[10]) + (jArr[14] * jArr2[9]) + (jArr[15] * jArr2[8]), (jArr[9] * jArr2[15]) + (jArr[10] * jArr2[14]) + (jArr[11] * jArr2[13]) + (jArr[12] * jArr2[12]) + (jArr[13] * jArr2[11]) + (jArr[14] * jArr2[10]) + (jArr[15] * jArr2[9]), (jArr[10] * jArr2[15]) + (jArr[11] * jArr2[14]) + (jArr[12] * jArr2[13]) + (jArr[13] * jArr2[12]) + (jArr[14] * jArr2[11]) + (jArr[15] * jArr2[10]), (jArr[11] * jArr2[15]) + (jArr[12] * jArr2[14]) + (jArr[13] * jArr2[13]) + (jArr[14] * jArr2[12]) + (jArr[15] * jArr2[11]), (jArr[12] * jArr2[15]) + (jArr[13] * jArr2[14]) + (jArr[14] * jArr2[13]) + (jArr[15] * jArr2[12]), (jArr[13] * jArr2[15]) + (jArr[14] * jArr2[14]) + (jArr[15] * jArr2[13]), (jArr[14] * jArr2[15]) + (jArr[15] * jArr2[14]), jArr[15] * jArr2[15]);
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21, long j22, long j23, long j24, long j25, long j26, long j27, long j28, long j29, long j30, long j31, long j32) {
        long j33 = j19 + j27;
        long j34 = j20 + j28;
        long j35 = j21 + j29;
        long j36 = j22 + j30;
        long j37 = j23 + j31;
        long j38 = j24 + j32;
        jArr[4] = j6 + j36;
        jArr[12] = j14 + j30 + j36;
        jArr[5] = j7 + j37;
        jArr[13] = j15 + j31 + j37;
        jArr[6] = j8 + j38;
        long j39 = j16 + j32 + j38;
        jArr[7] = j9 + j25;
        long jCarryValue = carryValue(j39);
        jArr[14] = j39 - (jCarryValue << 28);
        long j40 = j17 + j25 + jCarryValue;
        long jCarryValue2 = carryValue(j40);
        jArr[15] = j40 - (jCarryValue2 << 28);
        long j41 = j18 + j26 + jCarryValue2;
        jArr[0] = j2 + j41;
        jArr[8] = j10 + j26 + j41;
        jArr[1] = j3 + j33;
        jArr[9] = j11 + j27 + j33;
        jArr[2] = j4 + j34;
        jArr[10] = j12 + j28 + j34;
        jArr[3] = j5 + j35;
        jArr[11] = j13 + j29 + j35;
        carry(jArr, 0, 15);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (jArr[1] * jArr[1]) + (2 * jArr[0] * jArr[2]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (jArr[2] * jArr[2]) + (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))), 2 * ((jArr[0] * jArr[5]) + (jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (jArr[3] * jArr[3]) + (2 * ((jArr[0] * jArr[6]) + (jArr[1] * jArr[5]) + (jArr[2] * jArr[4]))), 2 * ((jArr[0] * jArr[7]) + (jArr[1] * jArr[6]) + (jArr[2] * jArr[5]) + (jArr[3] * jArr[4])), (jArr[4] * jArr[4]) + (2 * ((jArr[0] * jArr[8]) + (jArr[1] * jArr[7]) + (jArr[2] * jArr[6]) + (jArr[3] * jArr[5]))), 2 * ((jArr[0] * jArr[9]) + (jArr[1] * jArr[8]) + (jArr[2] * jArr[7]) + (jArr[3] * jArr[6]) + (jArr[4] * jArr[5])), (jArr[5] * jArr[5]) + (2 * ((jArr[0] * jArr[10]) + (jArr[1] * jArr[9]) + (jArr[2] * jArr[8]) + (jArr[3] * jArr[7]) + (jArr[4] * jArr[6]))), 2 * ((jArr[0] * jArr[11]) + (jArr[1] * jArr[10]) + (jArr[2] * jArr[9]) + (jArr[3] * jArr[8]) + (jArr[4] * jArr[7]) + (jArr[5] * jArr[6])), (jArr[6] * jArr[6]) + (2 * ((jArr[0] * jArr[12]) + (jArr[1] * jArr[11]) + (jArr[2] * jArr[10]) + (jArr[3] * jArr[9]) + (jArr[4] * jArr[8]) + (jArr[5] * jArr[7]))), 2 * ((jArr[0] * jArr[13]) + (jArr[1] * jArr[12]) + (jArr[2] * jArr[11]) + (jArr[3] * jArr[10]) + (jArr[4] * jArr[9]) + (jArr[5] * jArr[8]) + (jArr[6] * jArr[7])), (jArr[7] * jArr[7]) + (2 * ((jArr[0] * jArr[14]) + (jArr[1] * jArr[13]) + (jArr[2] * jArr[12]) + (jArr[3] * jArr[11]) + (jArr[4] * jArr[10]) + (jArr[5] * jArr[9]) + (jArr[6] * jArr[8]))), 2 * ((jArr[0] * jArr[15]) + (jArr[1] * jArr[14]) + (jArr[2] * jArr[13]) + (jArr[3] * jArr[12]) + (jArr[4] * jArr[11]) + (jArr[5] * jArr[10]) + (jArr[6] * jArr[9]) + (jArr[7] * jArr[8])), (jArr[8] * jArr[8]) + (2 * ((jArr[1] * jArr[15]) + (jArr[2] * jArr[14]) + (jArr[3] * jArr[13]) + (jArr[4] * jArr[12]) + (jArr[5] * jArr[11]) + (jArr[6] * jArr[10]) + (jArr[7] * jArr[9]))), 2 * ((jArr[2] * jArr[15]) + (jArr[3] * jArr[14]) + (jArr[4] * jArr[13]) + (jArr[5] * jArr[12]) + (jArr[6] * jArr[11]) + (jArr[7] * jArr[10]) + (jArr[8] * jArr[9])), (jArr[9] * jArr[9]) + (2 * ((jArr[3] * jArr[15]) + (jArr[4] * jArr[14]) + (jArr[5] * jArr[13]) + (jArr[6] * jArr[12]) + (jArr[7] * jArr[11]) + (jArr[8] * jArr[10]))), 2 * ((jArr[4] * jArr[15]) + (jArr[5] * jArr[14]) + (jArr[6] * jArr[13]) + (jArr[7] * jArr[12]) + (jArr[8] * jArr[11]) + (jArr[9] * jArr[10])), (jArr[10] * jArr[10]) + (2 * ((jArr[5] * jArr[15]) + (jArr[6] * jArr[14]) + (jArr[7] * jArr[13]) + (jArr[8] * jArr[12]) + (jArr[9] * jArr[11]))), 2 * ((jArr[6] * jArr[15]) + (jArr[7] * jArr[14]) + (jArr[8] * jArr[13]) + (jArr[9] * jArr[12]) + (jArr[10] * jArr[11])), (jArr[11] * jArr[11]) + (2 * ((jArr[7] * jArr[15]) + (jArr[8] * jArr[14]) + (jArr[9] * jArr[13]) + (jArr[10] * jArr[12]))), 2 * ((jArr[8] * jArr[15]) + (jArr[9] * jArr[14]) + (jArr[10] * jArr[13]) + (jArr[11] * jArr[12])), (jArr[12] * jArr[12]) + (2 * ((jArr[9] * jArr[15]) + (jArr[10] * jArr[14]) + (jArr[11] * jArr[13]))), 2 * ((jArr[10] * jArr[15]) + (jArr[11] * jArr[14]) + (jArr[12] * jArr[13])), (jArr[13] * jArr[13]) + (2 * ((jArr[11] * jArr[15]) + (jArr[12] * jArr[14]))), 2 * ((jArr[12] * jArr[15]) + (jArr[13] * jArr[14])), (jArr[14] * jArr[14]) + (2 * jArr[13] * jArr[15]), 2 * jArr[14] * jArr[15], jArr[15] * jArr[15]);
    }
}
