package sun.security.util.math.intpoly;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomialP256.class */
public class IntegerPolynomialP256 extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 26;
    private static final int NUM_LIMBS = 10;
    private static final int MAX_ADDS = 2;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 33554432;
    private static final int LIMB_MASK = 67108863;

    public IntegerPolynomialP256() {
        super(26, 10, 2, MODULUS);
    }

    private static BigInteger evaluateModulus() {
        return BigInteger.valueOf(2L).pow(256).subtract(BigInteger.valueOf(2L).pow(224)).add(BigInteger.valueOf(2L).pow(192)).add(BigInteger.valueOf(2L).pow(96)).subtract(BigInteger.valueOf(1L));
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[9] >> 22;
        jArr[9] = jArr[9] - (j2 << 22);
        jArr[8] = jArr[8] + ((j2 << 16) & 67108863);
        jArr[9] = jArr[9] + (j2 >> 10);
        jArr[7] = jArr[7] - ((j2 << 10) & 67108863);
        jArr[8] = jArr[8] - (j2 >> 16);
        jArr[3] = jArr[3] - ((j2 << 18) & 67108863);
        jArr[4] = jArr[4] - (j2 >> 8);
        jArr[0] = jArr[0] + j2;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20) {
        long j21 = j19 + (j20 >> 6);
        long j22 = (j18 + ((j20 << 20) & 67108863)) - (j20 >> 12);
        long j23 = (j17 - ((j20 << 14) & 67108863)) + ((j21 << 20) & 67108863);
        long j24 = j22 + (j21 >> 6);
        long j25 = j16 - ((j21 << 14) & 67108863);
        long j26 = j23 - (j21 >> 12);
        long j27 = j12 - ((j21 << 22) & 67108863);
        long j28 = (j13 - ((j20 << 22) & 67108863)) - (j21 >> 4);
        long j29 = j9 + ((j21 << 4) & 67108863);
        long j30 = j10 + ((j20 << 4) & 67108863) + (j21 >> 22);
        long j31 = j25 + ((j24 << 20) & 67108863);
        long j32 = j26 + (j24 >> 6);
        long j33 = j15 - ((j24 << 14) & 67108863);
        long j34 = j31 - (j24 >> 12);
        long j35 = (j11 + (j20 >> 22)) - ((j24 << 22) & 67108863);
        long j36 = j27 - (j24 >> 4);
        long j37 = j8 + ((j24 << 4) & 67108863);
        long j38 = j29 + (j24 >> 22);
        long j39 = j33 + ((j32 << 20) & 67108863);
        long j40 = j34 + (j32 >> 6);
        long j41 = (j14 - (j20 >> 4)) - ((j32 << 14) & 67108863);
        long j42 = j39 - (j32 >> 12);
        long j43 = j30 - ((j32 << 22) & 67108863);
        long j44 = j35 - (j32 >> 4);
        long j45 = j7 + ((j32 << 4) & 67108863);
        long j46 = j37 + (j32 >> 22);
        long j47 = j41 + ((j40 << 20) & 67108863);
        long j48 = j42 + (j40 >> 6);
        long j49 = j28 - ((j40 << 14) & 67108863);
        long j50 = j47 - (j40 >> 12);
        long j51 = j38 - ((j40 << 22) & 67108863);
        long j52 = j43 - (j40 >> 4);
        long j53 = j6 + ((j40 << 4) & 67108863);
        long j54 = j45 + (j40 >> 22);
        long j55 = j49 + ((j48 << 20) & 67108863);
        long j56 = j50 + (j48 >> 6);
        long j57 = j36 - ((j48 << 14) & 67108863);
        long j58 = j55 - (j48 >> 12);
        long j59 = j46 - ((j48 << 22) & 67108863);
        long j60 = j51 - (j48 >> 4);
        long j61 = j5 + ((j48 << 4) & 67108863);
        long j62 = j53 + (j48 >> 22);
        long j63 = j57 + ((j56 << 20) & 67108863);
        long j64 = j58 + (j56 >> 6);
        long j65 = j44 - ((j56 << 14) & 67108863);
        long j66 = j63 - (j56 >> 12);
        long j67 = j54 - ((j56 << 22) & 67108863);
        long j68 = j59 - (j56 >> 4);
        long j69 = j4 + ((j56 << 4) & 67108863);
        long j70 = j61 + (j56 >> 22);
        long j71 = j65 + ((j64 << 20) & 67108863);
        long j72 = j66 + (j64 >> 6);
        long j73 = j52 - ((j64 << 14) & 67108863);
        long j74 = j71 - (j64 >> 12);
        long j75 = j62 - ((j64 << 22) & 67108863);
        long j76 = j67 - (j64 >> 4);
        long j77 = j3 + ((j64 << 4) & 67108863);
        long j78 = j69 + (j64 >> 22);
        long j79 = j73 + ((j72 << 20) & 67108863);
        long j80 = j74 + (j72 >> 6);
        long j81 = j60 - ((j72 << 14) & 67108863);
        long j82 = j79 - (j72 >> 12);
        carryReduce0(jArr, j2 + ((j72 << 4) & 67108863), j77 + (j72 >> 22), j78, j70 - ((j72 << 22) & 67108863), j75 - (j72 >> 4), j76, j68, j81, j82, j80, 0L, j64, j56, j48, j40, j32, j24, j21, j20, 0L);
    }

    void carryReduce0(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21) {
        long j22 = (j10 + 33554432) >> 26;
        long j23 = j10 - (j22 << 26);
        long j24 = j11 + j22;
        long j25 = (j24 + 33554432) >> 26;
        long j26 = j24 - (j25 << 26);
        long j27 = j12 + j25;
        long j28 = j23 + ((j27 << 20) & 67108863);
        long j29 = j26 + (j27 >> 6);
        long j30 = j9 - ((j27 << 14) & 67108863);
        long j31 = j28 - (j27 >> 12);
        long j32 = j5 - ((j27 << 22) & 67108863);
        long j33 = j6 - (j27 >> 4);
        long j34 = j2 + ((j27 << 4) & 67108863);
        long j35 = j3 + (j27 >> 22);
        long j36 = (j34 + 33554432) >> 26;
        long j37 = j34 - (j36 << 26);
        long j38 = j35 + j36;
        long j39 = (j38 + 33554432) >> 26;
        long j40 = j38 - (j39 << 26);
        long j41 = j4 + j39;
        long j42 = (j41 + 33554432) >> 26;
        long j43 = j41 - (j42 << 26);
        long j44 = j32 + j42;
        long j45 = (j44 + 33554432) >> 26;
        long j46 = j44 - (j45 << 26);
        long j47 = j33 + j45;
        long j48 = (j47 + 33554432) >> 26;
        long j49 = j47 - (j48 << 26);
        long j50 = j7 + j48;
        long j51 = (j50 + 33554432) >> 26;
        long j52 = j50 - (j51 << 26);
        long j53 = j8 + j51;
        long j54 = (j53 + 33554432) >> 26;
        long j55 = j53 - (j54 << 26);
        long j56 = j30 + j54;
        long j57 = (j56 + 33554432) >> 26;
        long j58 = j56 - (j57 << 26);
        long j59 = j31 + j57;
        long j60 = (j59 + 33554432) >> 26;
        jArr[0] = j37;
        jArr[1] = j40;
        jArr[2] = j43;
        jArr[3] = j46;
        jArr[4] = j49;
        jArr[5] = j52;
        jArr[6] = j55;
        jArr[7] = j58;
        jArr[8] = j59 - (j60 << 26);
        jArr[9] = j29 + j60;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11) {
        long j12 = (j10 + 33554432) >> 26;
        long j13 = j10 - (j12 << 26);
        long j14 = j11 + j12;
        long j15 = (j14 + 33554432) >> 26;
        long j16 = j14 - (j15 << 26);
        long j17 = 0 + j15;
        long j18 = j13 + ((j17 << 20) & 67108863);
        long j19 = j16 + (j17 >> 6);
        long j20 = j9 - ((j17 << 14) & 67108863);
        long j21 = j18 - (j17 >> 12);
        long j22 = j5 - ((j17 << 22) & 67108863);
        long j23 = j6 - (j17 >> 4);
        long j24 = j2 + ((j17 << 4) & 67108863);
        long j25 = j3 + (j17 >> 22);
        long j26 = (j24 + 33554432) >> 26;
        long j27 = j24 - (j26 << 26);
        long j28 = j25 + j26;
        long j29 = (j28 + 33554432) >> 26;
        long j30 = j28 - (j29 << 26);
        long j31 = j4 + j29;
        long j32 = (j31 + 33554432) >> 26;
        long j33 = j31 - (j32 << 26);
        long j34 = j22 + j32;
        long j35 = (j34 + 33554432) >> 26;
        long j36 = j34 - (j35 << 26);
        long j37 = j23 + j35;
        long j38 = (j37 + 33554432) >> 26;
        long j39 = j37 - (j38 << 26);
        long j40 = j7 + j38;
        long j41 = (j40 + 33554432) >> 26;
        long j42 = j40 - (j41 << 26);
        long j43 = j8 + j41;
        long j44 = (j43 + 33554432) >> 26;
        long j45 = j43 - (j44 << 26);
        long j46 = j20 + j44;
        long j47 = (j46 + 33554432) >> 26;
        long j48 = j46 - (j47 << 26);
        long j49 = j21 + j47;
        long j50 = (j49 + 33554432) >> 26;
        jArr[0] = j27;
        jArr[1] = j30;
        jArr[2] = j33;
        jArr[3] = j36;
        jArr[4] = j39;
        jArr[5] = j42;
        jArr[6] = j45;
        jArr[7] = j48;
        jArr[8] = j49 - (j50 << 26);
        jArr[9] = j19 + j50;
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[0] * jArr2[5]) + (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]) + (jArr[5] * jArr2[0]), (jArr[0] * jArr2[6]) + (jArr[1] * jArr2[5]) + (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]) + (jArr[5] * jArr2[1]) + (jArr[6] * jArr2[0]), (jArr[0] * jArr2[7]) + (jArr[1] * jArr2[6]) + (jArr[2] * jArr2[5]) + (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]) + (jArr[5] * jArr2[2]) + (jArr[6] * jArr2[1]) + (jArr[7] * jArr2[0]), (jArr[0] * jArr2[8]) + (jArr[1] * jArr2[7]) + (jArr[2] * jArr2[6]) + (jArr[3] * jArr2[5]) + (jArr[4] * jArr2[4]) + (jArr[5] * jArr2[3]) + (jArr[6] * jArr2[2]) + (jArr[7] * jArr2[1]) + (jArr[8] * jArr2[0]), (jArr[0] * jArr2[9]) + (jArr[1] * jArr2[8]) + (jArr[2] * jArr2[7]) + (jArr[3] * jArr2[6]) + (jArr[4] * jArr2[5]) + (jArr[5] * jArr2[4]) + (jArr[6] * jArr2[3]) + (jArr[7] * jArr2[2]) + (jArr[8] * jArr2[1]) + (jArr[9] * jArr2[0]), (jArr[1] * jArr2[9]) + (jArr[2] * jArr2[8]) + (jArr[3] * jArr2[7]) + (jArr[4] * jArr2[6]) + (jArr[5] * jArr2[5]) + (jArr[6] * jArr2[4]) + (jArr[7] * jArr2[3]) + (jArr[8] * jArr2[2]) + (jArr[9] * jArr2[1]), (jArr[2] * jArr2[9]) + (jArr[3] * jArr2[8]) + (jArr[4] * jArr2[7]) + (jArr[5] * jArr2[6]) + (jArr[6] * jArr2[5]) + (jArr[7] * jArr2[4]) + (jArr[8] * jArr2[3]) + (jArr[9] * jArr2[2]), (jArr[3] * jArr2[9]) + (jArr[4] * jArr2[8]) + (jArr[5] * jArr2[7]) + (jArr[6] * jArr2[6]) + (jArr[7] * jArr2[5]) + (jArr[8] * jArr2[4]) + (jArr[9] * jArr2[3]), (jArr[4] * jArr2[9]) + (jArr[5] * jArr2[8]) + (jArr[6] * jArr2[7]) + (jArr[7] * jArr2[6]) + (jArr[8] * jArr2[5]) + (jArr[9] * jArr2[4]), (jArr[5] * jArr2[9]) + (jArr[6] * jArr2[8]) + (jArr[7] * jArr2[7]) + (jArr[8] * jArr2[6]) + (jArr[9] * jArr2[5]), (jArr[6] * jArr2[9]) + (jArr[7] * jArr2[8]) + (jArr[8] * jArr2[7]) + (jArr[9] * jArr2[6]), (jArr[7] * jArr2[9]) + (jArr[8] * jArr2[8]) + (jArr[9] * jArr2[7]), (jArr[8] * jArr2[9]) + (jArr[9] * jArr2[8]), jArr[9] * jArr2[9]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        carryReduce(jArr, jArr[0], jArr[1], jArr[2], jArr[3], jArr[4], jArr[5], jArr[6], jArr[7], jArr[8], jArr[9]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (2 * jArr[0] * jArr[2]) + (jArr[1] * jArr[1]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))) + (jArr[2] * jArr[2]), 2 * ((jArr[0] * jArr[5]) + (jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (2 * ((jArr[0] * jArr[6]) + (jArr[1] * jArr[5]) + (jArr[2] * jArr[4]))) + (jArr[3] * jArr[3]), 2 * ((jArr[0] * jArr[7]) + (jArr[1] * jArr[6]) + (jArr[2] * jArr[5]) + (jArr[3] * jArr[4])), (2 * ((jArr[0] * jArr[8]) + (jArr[1] * jArr[7]) + (jArr[2] * jArr[6]) + (jArr[3] * jArr[5]))) + (jArr[4] * jArr[4]), 2 * ((jArr[0] * jArr[9]) + (jArr[1] * jArr[8]) + (jArr[2] * jArr[7]) + (jArr[3] * jArr[6]) + (jArr[4] * jArr[5])), (2 * ((jArr[1] * jArr[9]) + (jArr[2] * jArr[8]) + (jArr[3] * jArr[7]) + (jArr[4] * jArr[6]))) + (jArr[5] * jArr[5]), 2 * ((jArr[2] * jArr[9]) + (jArr[3] * jArr[8]) + (jArr[4] * jArr[7]) + (jArr[5] * jArr[6])), (2 * ((jArr[3] * jArr[9]) + (jArr[4] * jArr[8]) + (jArr[5] * jArr[7]))) + (jArr[6] * jArr[6]), 2 * ((jArr[4] * jArr[9]) + (jArr[5] * jArr[8]) + (jArr[6] * jArr[7])), (2 * ((jArr[5] * jArr[9]) + (jArr[6] * jArr[8]))) + (jArr[7] * jArr[7]), 2 * ((jArr[6] * jArr[9]) + (jArr[7] * jArr[8])), (2 * jArr[7] * jArr[9]) + (jArr[8] * jArr[8]), 2 * jArr[8] * jArr[9], jArr[9] * jArr[9]);
    }
}
