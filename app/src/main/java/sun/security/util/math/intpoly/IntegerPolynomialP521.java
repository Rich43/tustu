package sun.security.util.math.intpoly;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomialP521.class */
public class IntegerPolynomialP521 extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 28;
    private static final int NUM_LIMBS = 19;
    private static final int MAX_ADDS = 2;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 134217728;
    private static final int LIMB_MASK = 268435455;

    public IntegerPolynomialP521() {
        super(28, 19, 2, MODULUS);
    }

    private static BigInteger evaluateModulus() {
        return BigInteger.valueOf(2L).pow(521).subtract(BigInteger.valueOf(1L));
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[18] >> 17;
        jArr[18] = jArr[18] - (j2 << 17);
        jArr[0] = jArr[0] + j2;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21, long j22, long j23, long j24, long j25, long j26, long j27, long j28, long j29, long j30, long j31, long j32, long j33, long j34, long j35, long j36, long j37, long j38) {
        long j39 = j19 + ((j38 << 11) & 268435455) + (j37 >> 17);
        long j40 = j18 + ((j37 << 11) & 268435455) + (j36 >> 17);
        long j41 = j17 + ((j36 << 11) & 268435455) + (j35 >> 17);
        long j42 = j16 + ((j35 << 11) & 268435455) + (j34 >> 17);
        long j43 = j15 + ((j34 << 11) & 268435455) + (j33 >> 17);
        long j44 = j14 + ((j33 << 11) & 268435455) + (j32 >> 17);
        long j45 = j13 + ((j32 << 11) & 268435455) + (j31 >> 17);
        long j46 = j12 + ((j31 << 11) & 268435455) + (j30 >> 17);
        long j47 = j11 + ((j30 << 11) & 268435455) + (j29 >> 17);
        long j48 = j10 + ((j29 << 11) & 268435455) + (j28 >> 17);
        long j49 = j9 + ((j28 << 11) & 268435455) + (j27 >> 17);
        long j50 = j8 + ((j27 << 11) & 268435455) + (j26 >> 17);
        long j51 = j7 + ((j26 << 11) & 268435455) + (j25 >> 17);
        long j52 = j6 + ((j25 << 11) & 268435455) + (j24 >> 17);
        long j53 = j5 + ((j24 << 11) & 268435455) + (j23 >> 17);
        long j54 = j4 + ((j23 << 11) & 268435455) + (j22 >> 17);
        carryReduce0(jArr, j2 + ((j21 << 11) & 268435455), j3 + ((j22 << 11) & 268435455) + (j21 >> 17), j54, j53, j52, j51, j50, j49, j48, j47, j46, j45, j44, j43, j42, j41, j40, j39, j20 + (j38 >> 17), 0L, j22, j23, j24, j25, j26, j27, j28, j29, j30, j31, j32, j33, j34, j35, j36, j37, j38, 0L);
    }

    void carryReduce0(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21, long j22, long j23, long j24, long j25, long j26, long j27, long j28, long j29, long j30, long j31, long j32, long j33, long j34, long j35, long j36, long j37, long j38, long j39) {
        long j40 = (j19 + CARRY_ADD) >> 28;
        long j41 = j19 - (j40 << 28);
        long j42 = j20 + j40;
        long j43 = (j42 + CARRY_ADD) >> 28;
        long j44 = j42 - (j43 << 28);
        long j45 = j21 + j43;
        long j46 = j2 + ((j45 << 11) & 268435455);
        long j47 = j3 + (j45 >> 17);
        long j48 = (j46 + CARRY_ADD) >> 28;
        long j49 = j46 - (j48 << 28);
        long j50 = j47 + j48;
        long j51 = (j50 + CARRY_ADD) >> 28;
        long j52 = j50 - (j51 << 28);
        long j53 = j4 + j51;
        long j54 = (j53 + CARRY_ADD) >> 28;
        long j55 = j53 - (j54 << 28);
        long j56 = j5 + j54;
        long j57 = (j56 + CARRY_ADD) >> 28;
        long j58 = j56 - (j57 << 28);
        long j59 = j6 + j57;
        long j60 = (j59 + CARRY_ADD) >> 28;
        long j61 = j59 - (j60 << 28);
        long j62 = j7 + j60;
        long j63 = (j62 + CARRY_ADD) >> 28;
        long j64 = j62 - (j63 << 28);
        long j65 = j8 + j63;
        long j66 = (j65 + CARRY_ADD) >> 28;
        long j67 = j65 - (j66 << 28);
        long j68 = j9 + j66;
        long j69 = (j68 + CARRY_ADD) >> 28;
        long j70 = j68 - (j69 << 28);
        long j71 = j10 + j69;
        long j72 = (j71 + CARRY_ADD) >> 28;
        long j73 = j71 - (j72 << 28);
        long j74 = j11 + j72;
        long j75 = (j74 + CARRY_ADD) >> 28;
        long j76 = j74 - (j75 << 28);
        long j77 = j12 + j75;
        long j78 = (j77 + CARRY_ADD) >> 28;
        long j79 = j77 - (j78 << 28);
        long j80 = j13 + j78;
        long j81 = (j80 + CARRY_ADD) >> 28;
        long j82 = j80 - (j81 << 28);
        long j83 = j14 + j81;
        long j84 = (j83 + CARRY_ADD) >> 28;
        long j85 = j83 - (j84 << 28);
        long j86 = j15 + j84;
        long j87 = (j86 + CARRY_ADD) >> 28;
        long j88 = j86 - (j87 << 28);
        long j89 = j16 + j87;
        long j90 = (j89 + CARRY_ADD) >> 28;
        long j91 = j89 - (j90 << 28);
        long j92 = j17 + j90;
        long j93 = (j92 + CARRY_ADD) >> 28;
        long j94 = j92 - (j93 << 28);
        long j95 = j18 + j93;
        long j96 = (j95 + CARRY_ADD) >> 28;
        long j97 = j95 - (j96 << 28);
        long j98 = j41 + j96;
        long j99 = (j98 + CARRY_ADD) >> 28;
        jArr[0] = j49;
        jArr[1] = j52;
        jArr[2] = j55;
        jArr[3] = j58;
        jArr[4] = j61;
        jArr[5] = j64;
        jArr[6] = j67;
        jArr[7] = j70;
        jArr[8] = j73;
        jArr[9] = j76;
        jArr[10] = j79;
        jArr[11] = j82;
        jArr[12] = j85;
        jArr[13] = j88;
        jArr[14] = j91;
        jArr[15] = j94;
        jArr[16] = j97;
        jArr[17] = j98 - (j99 << 28);
        jArr[18] = j44 + j99;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20) {
        long j21 = (j19 + CARRY_ADD) >> 28;
        long j22 = j19 - (j21 << 28);
        long j23 = j20 + j21;
        long j24 = (j23 + CARRY_ADD) >> 28;
        long j25 = j23 - (j24 << 28);
        long j26 = 0 + j24;
        long j27 = j2 + ((j26 << 11) & 268435455);
        long j28 = j3 + (j26 >> 17);
        long j29 = (j27 + CARRY_ADD) >> 28;
        long j30 = j27 - (j29 << 28);
        long j31 = j28 + j29;
        long j32 = (j31 + CARRY_ADD) >> 28;
        long j33 = j31 - (j32 << 28);
        long j34 = j4 + j32;
        long j35 = (j34 + CARRY_ADD) >> 28;
        long j36 = j34 - (j35 << 28);
        long j37 = j5 + j35;
        long j38 = (j37 + CARRY_ADD) >> 28;
        long j39 = j37 - (j38 << 28);
        long j40 = j6 + j38;
        long j41 = (j40 + CARRY_ADD) >> 28;
        long j42 = j40 - (j41 << 28);
        long j43 = j7 + j41;
        long j44 = (j43 + CARRY_ADD) >> 28;
        long j45 = j43 - (j44 << 28);
        long j46 = j8 + j44;
        long j47 = (j46 + CARRY_ADD) >> 28;
        long j48 = j46 - (j47 << 28);
        long j49 = j9 + j47;
        long j50 = (j49 + CARRY_ADD) >> 28;
        long j51 = j49 - (j50 << 28);
        long j52 = j10 + j50;
        long j53 = (j52 + CARRY_ADD) >> 28;
        long j54 = j52 - (j53 << 28);
        long j55 = j11 + j53;
        long j56 = (j55 + CARRY_ADD) >> 28;
        long j57 = j55 - (j56 << 28);
        long j58 = j12 + j56;
        long j59 = (j58 + CARRY_ADD) >> 28;
        long j60 = j58 - (j59 << 28);
        long j61 = j13 + j59;
        long j62 = (j61 + CARRY_ADD) >> 28;
        long j63 = j61 - (j62 << 28);
        long j64 = j14 + j62;
        long j65 = (j64 + CARRY_ADD) >> 28;
        long j66 = j64 - (j65 << 28);
        long j67 = j15 + j65;
        long j68 = (j67 + CARRY_ADD) >> 28;
        long j69 = j67 - (j68 << 28);
        long j70 = j16 + j68;
        long j71 = (j70 + CARRY_ADD) >> 28;
        long j72 = j70 - (j71 << 28);
        long j73 = j17 + j71;
        long j74 = (j73 + CARRY_ADD) >> 28;
        long j75 = j73 - (j74 << 28);
        long j76 = j18 + j74;
        long j77 = (j76 + CARRY_ADD) >> 28;
        long j78 = j76 - (j77 << 28);
        long j79 = j22 + j77;
        long j80 = (j79 + CARRY_ADD) >> 28;
        jArr[0] = j30;
        jArr[1] = j33;
        jArr[2] = j36;
        jArr[3] = j39;
        jArr[4] = j42;
        jArr[5] = j45;
        jArr[6] = j48;
        jArr[7] = j51;
        jArr[8] = j54;
        jArr[9] = j57;
        jArr[10] = j60;
        jArr[11] = j63;
        jArr[12] = j66;
        jArr[13] = j69;
        jArr[14] = j72;
        jArr[15] = j75;
        jArr[16] = j78;
        jArr[17] = j79 - (j80 << 28);
        jArr[18] = j25 + j80;
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[0] * jArr2[5]) + (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]) + (jArr[5] * jArr2[0]), (jArr[0] * jArr2[6]) + (jArr[1] * jArr2[5]) + (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]) + (jArr[5] * jArr2[1]) + (jArr[6] * jArr2[0]), (jArr[0] * jArr2[7]) + (jArr[1] * jArr2[6]) + (jArr[2] * jArr2[5]) + (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]) + (jArr[5] * jArr2[2]) + (jArr[6] * jArr2[1]) + (jArr[7] * jArr2[0]), (jArr[0] * jArr2[8]) + (jArr[1] * jArr2[7]) + (jArr[2] * jArr2[6]) + (jArr[3] * jArr2[5]) + (jArr[4] * jArr2[4]) + (jArr[5] * jArr2[3]) + (jArr[6] * jArr2[2]) + (jArr[7] * jArr2[1]) + (jArr[8] * jArr2[0]), (jArr[0] * jArr2[9]) + (jArr[1] * jArr2[8]) + (jArr[2] * jArr2[7]) + (jArr[3] * jArr2[6]) + (jArr[4] * jArr2[5]) + (jArr[5] * jArr2[4]) + (jArr[6] * jArr2[3]) + (jArr[7] * jArr2[2]) + (jArr[8] * jArr2[1]) + (jArr[9] * jArr2[0]), (jArr[0] * jArr2[10]) + (jArr[1] * jArr2[9]) + (jArr[2] * jArr2[8]) + (jArr[3] * jArr2[7]) + (jArr[4] * jArr2[6]) + (jArr[5] * jArr2[5]) + (jArr[6] * jArr2[4]) + (jArr[7] * jArr2[3]) + (jArr[8] * jArr2[2]) + (jArr[9] * jArr2[1]) + (jArr[10] * jArr2[0]), (jArr[0] * jArr2[11]) + (jArr[1] * jArr2[10]) + (jArr[2] * jArr2[9]) + (jArr[3] * jArr2[8]) + (jArr[4] * jArr2[7]) + (jArr[5] * jArr2[6]) + (jArr[6] * jArr2[5]) + (jArr[7] * jArr2[4]) + (jArr[8] * jArr2[3]) + (jArr[9] * jArr2[2]) + (jArr[10] * jArr2[1]) + (jArr[11] * jArr2[0]), (jArr[0] * jArr2[12]) + (jArr[1] * jArr2[11]) + (jArr[2] * jArr2[10]) + (jArr[3] * jArr2[9]) + (jArr[4] * jArr2[8]) + (jArr[5] * jArr2[7]) + (jArr[6] * jArr2[6]) + (jArr[7] * jArr2[5]) + (jArr[8] * jArr2[4]) + (jArr[9] * jArr2[3]) + (jArr[10] * jArr2[2]) + (jArr[11] * jArr2[1]) + (jArr[12] * jArr2[0]), (jArr[0] * jArr2[13]) + (jArr[1] * jArr2[12]) + (jArr[2] * jArr2[11]) + (jArr[3] * jArr2[10]) + (jArr[4] * jArr2[9]) + (jArr[5] * jArr2[8]) + (jArr[6] * jArr2[7]) + (jArr[7] * jArr2[6]) + (jArr[8] * jArr2[5]) + (jArr[9] * jArr2[4]) + (jArr[10] * jArr2[3]) + (jArr[11] * jArr2[2]) + (jArr[12] * jArr2[1]) + (jArr[13] * jArr2[0]), (jArr[0] * jArr2[14]) + (jArr[1] * jArr2[13]) + (jArr[2] * jArr2[12]) + (jArr[3] * jArr2[11]) + (jArr[4] * jArr2[10]) + (jArr[5] * jArr2[9]) + (jArr[6] * jArr2[8]) + (jArr[7] * jArr2[7]) + (jArr[8] * jArr2[6]) + (jArr[9] * jArr2[5]) + (jArr[10] * jArr2[4]) + (jArr[11] * jArr2[3]) + (jArr[12] * jArr2[2]) + (jArr[13] * jArr2[1]) + (jArr[14] * jArr2[0]), (jArr[0] * jArr2[15]) + (jArr[1] * jArr2[14]) + (jArr[2] * jArr2[13]) + (jArr[3] * jArr2[12]) + (jArr[4] * jArr2[11]) + (jArr[5] * jArr2[10]) + (jArr[6] * jArr2[9]) + (jArr[7] * jArr2[8]) + (jArr[8] * jArr2[7]) + (jArr[9] * jArr2[6]) + (jArr[10] * jArr2[5]) + (jArr[11] * jArr2[4]) + (jArr[12] * jArr2[3]) + (jArr[13] * jArr2[2]) + (jArr[14] * jArr2[1]) + (jArr[15] * jArr2[0]), (jArr[0] * jArr2[16]) + (jArr[1] * jArr2[15]) + (jArr[2] * jArr2[14]) + (jArr[3] * jArr2[13]) + (jArr[4] * jArr2[12]) + (jArr[5] * jArr2[11]) + (jArr[6] * jArr2[10]) + (jArr[7] * jArr2[9]) + (jArr[8] * jArr2[8]) + (jArr[9] * jArr2[7]) + (jArr[10] * jArr2[6]) + (jArr[11] * jArr2[5]) + (jArr[12] * jArr2[4]) + (jArr[13] * jArr2[3]) + (jArr[14] * jArr2[2]) + (jArr[15] * jArr2[1]) + (jArr[16] * jArr2[0]), (jArr[0] * jArr2[17]) + (jArr[1] * jArr2[16]) + (jArr[2] * jArr2[15]) + (jArr[3] * jArr2[14]) + (jArr[4] * jArr2[13]) + (jArr[5] * jArr2[12]) + (jArr[6] * jArr2[11]) + (jArr[7] * jArr2[10]) + (jArr[8] * jArr2[9]) + (jArr[9] * jArr2[8]) + (jArr[10] * jArr2[7]) + (jArr[11] * jArr2[6]) + (jArr[12] * jArr2[5]) + (jArr[13] * jArr2[4]) + (jArr[14] * jArr2[3]) + (jArr[15] * jArr2[2]) + (jArr[16] * jArr2[1]) + (jArr[17] * jArr2[0]), (jArr[0] * jArr2[18]) + (jArr[1] * jArr2[17]) + (jArr[2] * jArr2[16]) + (jArr[3] * jArr2[15]) + (jArr[4] * jArr2[14]) + (jArr[5] * jArr2[13]) + (jArr[6] * jArr2[12]) + (jArr[7] * jArr2[11]) + (jArr[8] * jArr2[10]) + (jArr[9] * jArr2[9]) + (jArr[10] * jArr2[8]) + (jArr[11] * jArr2[7]) + (jArr[12] * jArr2[6]) + (jArr[13] * jArr2[5]) + (jArr[14] * jArr2[4]) + (jArr[15] * jArr2[3]) + (jArr[16] * jArr2[2]) + (jArr[17] * jArr2[1]) + (jArr[18] * jArr2[0]), (jArr[1] * jArr2[18]) + (jArr[2] * jArr2[17]) + (jArr[3] * jArr2[16]) + (jArr[4] * jArr2[15]) + (jArr[5] * jArr2[14]) + (jArr[6] * jArr2[13]) + (jArr[7] * jArr2[12]) + (jArr[8] * jArr2[11]) + (jArr[9] * jArr2[10]) + (jArr[10] * jArr2[9]) + (jArr[11] * jArr2[8]) + (jArr[12] * jArr2[7]) + (jArr[13] * jArr2[6]) + (jArr[14] * jArr2[5]) + (jArr[15] * jArr2[4]) + (jArr[16] * jArr2[3]) + (jArr[17] * jArr2[2]) + (jArr[18] * jArr2[1]), (jArr[2] * jArr2[18]) + (jArr[3] * jArr2[17]) + (jArr[4] * jArr2[16]) + (jArr[5] * jArr2[15]) + (jArr[6] * jArr2[14]) + (jArr[7] * jArr2[13]) + (jArr[8] * jArr2[12]) + (jArr[9] * jArr2[11]) + (jArr[10] * jArr2[10]) + (jArr[11] * jArr2[9]) + (jArr[12] * jArr2[8]) + (jArr[13] * jArr2[7]) + (jArr[14] * jArr2[6]) + (jArr[15] * jArr2[5]) + (jArr[16] * jArr2[4]) + (jArr[17] * jArr2[3]) + (jArr[18] * jArr2[2]), (jArr[3] * jArr2[18]) + (jArr[4] * jArr2[17]) + (jArr[5] * jArr2[16]) + (jArr[6] * jArr2[15]) + (jArr[7] * jArr2[14]) + (jArr[8] * jArr2[13]) + (jArr[9] * jArr2[12]) + (jArr[10] * jArr2[11]) + (jArr[11] * jArr2[10]) + (jArr[12] * jArr2[9]) + (jArr[13] * jArr2[8]) + (jArr[14] * jArr2[7]) + (jArr[15] * jArr2[6]) + (jArr[16] * jArr2[5]) + (jArr[17] * jArr2[4]) + (jArr[18] * jArr2[3]), (jArr[4] * jArr2[18]) + (jArr[5] * jArr2[17]) + (jArr[6] * jArr2[16]) + (jArr[7] * jArr2[15]) + (jArr[8] * jArr2[14]) + (jArr[9] * jArr2[13]) + (jArr[10] * jArr2[12]) + (jArr[11] * jArr2[11]) + (jArr[12] * jArr2[10]) + (jArr[13] * jArr2[9]) + (jArr[14] * jArr2[8]) + (jArr[15] * jArr2[7]) + (jArr[16] * jArr2[6]) + (jArr[17] * jArr2[5]) + (jArr[18] * jArr2[4]), (jArr[5] * jArr2[18]) + (jArr[6] * jArr2[17]) + (jArr[7] * jArr2[16]) + (jArr[8] * jArr2[15]) + (jArr[9] * jArr2[14]) + (jArr[10] * jArr2[13]) + (jArr[11] * jArr2[12]) + (jArr[12] * jArr2[11]) + (jArr[13] * jArr2[10]) + (jArr[14] * jArr2[9]) + (jArr[15] * jArr2[8]) + (jArr[16] * jArr2[7]) + (jArr[17] * jArr2[6]) + (jArr[18] * jArr2[5]), (jArr[6] * jArr2[18]) + (jArr[7] * jArr2[17]) + (jArr[8] * jArr2[16]) + (jArr[9] * jArr2[15]) + (jArr[10] * jArr2[14]) + (jArr[11] * jArr2[13]) + (jArr[12] * jArr2[12]) + (jArr[13] * jArr2[11]) + (jArr[14] * jArr2[10]) + (jArr[15] * jArr2[9]) + (jArr[16] * jArr2[8]) + (jArr[17] * jArr2[7]) + (jArr[18] * jArr2[6]), (jArr[7] * jArr2[18]) + (jArr[8] * jArr2[17]) + (jArr[9] * jArr2[16]) + (jArr[10] * jArr2[15]) + (jArr[11] * jArr2[14]) + (jArr[12] * jArr2[13]) + (jArr[13] * jArr2[12]) + (jArr[14] * jArr2[11]) + (jArr[15] * jArr2[10]) + (jArr[16] * jArr2[9]) + (jArr[17] * jArr2[8]) + (jArr[18] * jArr2[7]), (jArr[8] * jArr2[18]) + (jArr[9] * jArr2[17]) + (jArr[10] * jArr2[16]) + (jArr[11] * jArr2[15]) + (jArr[12] * jArr2[14]) + (jArr[13] * jArr2[13]) + (jArr[14] * jArr2[12]) + (jArr[15] * jArr2[11]) + (jArr[16] * jArr2[10]) + (jArr[17] * jArr2[9]) + (jArr[18] * jArr2[8]), (jArr[9] * jArr2[18]) + (jArr[10] * jArr2[17]) + (jArr[11] * jArr2[16]) + (jArr[12] * jArr2[15]) + (jArr[13] * jArr2[14]) + (jArr[14] * jArr2[13]) + (jArr[15] * jArr2[12]) + (jArr[16] * jArr2[11]) + (jArr[17] * jArr2[10]) + (jArr[18] * jArr2[9]), (jArr[10] * jArr2[18]) + (jArr[11] * jArr2[17]) + (jArr[12] * jArr2[16]) + (jArr[13] * jArr2[15]) + (jArr[14] * jArr2[14]) + (jArr[15] * jArr2[13]) + (jArr[16] * jArr2[12]) + (jArr[17] * jArr2[11]) + (jArr[18] * jArr2[10]), (jArr[11] * jArr2[18]) + (jArr[12] * jArr2[17]) + (jArr[13] * jArr2[16]) + (jArr[14] * jArr2[15]) + (jArr[15] * jArr2[14]) + (jArr[16] * jArr2[13]) + (jArr[17] * jArr2[12]) + (jArr[18] * jArr2[11]), (jArr[12] * jArr2[18]) + (jArr[13] * jArr2[17]) + (jArr[14] * jArr2[16]) + (jArr[15] * jArr2[15]) + (jArr[16] * jArr2[14]) + (jArr[17] * jArr2[13]) + (jArr[18] * jArr2[12]), (jArr[13] * jArr2[18]) + (jArr[14] * jArr2[17]) + (jArr[15] * jArr2[16]) + (jArr[16] * jArr2[15]) + (jArr[17] * jArr2[14]) + (jArr[18] * jArr2[13]), (jArr[14] * jArr2[18]) + (jArr[15] * jArr2[17]) + (jArr[16] * jArr2[16]) + (jArr[17] * jArr2[15]) + (jArr[18] * jArr2[14]), (jArr[15] * jArr2[18]) + (jArr[16] * jArr2[17]) + (jArr[17] * jArr2[16]) + (jArr[18] * jArr2[15]), (jArr[16] * jArr2[18]) + (jArr[17] * jArr2[17]) + (jArr[18] * jArr2[16]), (jArr[17] * jArr2[18]) + (jArr[18] * jArr2[17]), jArr[18] * jArr2[18]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        carryReduce(jArr, jArr[0], jArr[1], jArr[2], jArr[3], jArr[4], jArr[5], jArr[6], jArr[7], jArr[8], jArr[9], jArr[10], jArr[11], jArr[12], jArr[13], jArr[14], jArr[15], jArr[16], jArr[17], jArr[18]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (2 * jArr[0] * jArr[2]) + (jArr[1] * jArr[1]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))) + (jArr[2] * jArr[2]), 2 * ((jArr[0] * jArr[5]) + (jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (2 * ((jArr[0] * jArr[6]) + (jArr[1] * jArr[5]) + (jArr[2] * jArr[4]))) + (jArr[3] * jArr[3]), 2 * ((jArr[0] * jArr[7]) + (jArr[1] * jArr[6]) + (jArr[2] * jArr[5]) + (jArr[3] * jArr[4])), (2 * ((jArr[0] * jArr[8]) + (jArr[1] * jArr[7]) + (jArr[2] * jArr[6]) + (jArr[3] * jArr[5]))) + (jArr[4] * jArr[4]), 2 * ((jArr[0] * jArr[9]) + (jArr[1] * jArr[8]) + (jArr[2] * jArr[7]) + (jArr[3] * jArr[6]) + (jArr[4] * jArr[5])), (2 * ((jArr[0] * jArr[10]) + (jArr[1] * jArr[9]) + (jArr[2] * jArr[8]) + (jArr[3] * jArr[7]) + (jArr[4] * jArr[6]))) + (jArr[5] * jArr[5]), 2 * ((jArr[0] * jArr[11]) + (jArr[1] * jArr[10]) + (jArr[2] * jArr[9]) + (jArr[3] * jArr[8]) + (jArr[4] * jArr[7]) + (jArr[5] * jArr[6])), (2 * ((jArr[0] * jArr[12]) + (jArr[1] * jArr[11]) + (jArr[2] * jArr[10]) + (jArr[3] * jArr[9]) + (jArr[4] * jArr[8]) + (jArr[5] * jArr[7]))) + (jArr[6] * jArr[6]), 2 * ((jArr[0] * jArr[13]) + (jArr[1] * jArr[12]) + (jArr[2] * jArr[11]) + (jArr[3] * jArr[10]) + (jArr[4] * jArr[9]) + (jArr[5] * jArr[8]) + (jArr[6] * jArr[7])), (2 * ((jArr[0] * jArr[14]) + (jArr[1] * jArr[13]) + (jArr[2] * jArr[12]) + (jArr[3] * jArr[11]) + (jArr[4] * jArr[10]) + (jArr[5] * jArr[9]) + (jArr[6] * jArr[8]))) + (jArr[7] * jArr[7]), 2 * ((jArr[0] * jArr[15]) + (jArr[1] * jArr[14]) + (jArr[2] * jArr[13]) + (jArr[3] * jArr[12]) + (jArr[4] * jArr[11]) + (jArr[5] * jArr[10]) + (jArr[6] * jArr[9]) + (jArr[7] * jArr[8])), (2 * ((jArr[0] * jArr[16]) + (jArr[1] * jArr[15]) + (jArr[2] * jArr[14]) + (jArr[3] * jArr[13]) + (jArr[4] * jArr[12]) + (jArr[5] * jArr[11]) + (jArr[6] * jArr[10]) + (jArr[7] * jArr[9]))) + (jArr[8] * jArr[8]), 2 * ((jArr[0] * jArr[17]) + (jArr[1] * jArr[16]) + (jArr[2] * jArr[15]) + (jArr[3] * jArr[14]) + (jArr[4] * jArr[13]) + (jArr[5] * jArr[12]) + (jArr[6] * jArr[11]) + (jArr[7] * jArr[10]) + (jArr[8] * jArr[9])), (2 * ((jArr[0] * jArr[18]) + (jArr[1] * jArr[17]) + (jArr[2] * jArr[16]) + (jArr[3] * jArr[15]) + (jArr[4] * jArr[14]) + (jArr[5] * jArr[13]) + (jArr[6] * jArr[12]) + (jArr[7] * jArr[11]) + (jArr[8] * jArr[10]))) + (jArr[9] * jArr[9]), 2 * ((jArr[1] * jArr[18]) + (jArr[2] * jArr[17]) + (jArr[3] * jArr[16]) + (jArr[4] * jArr[15]) + (jArr[5] * jArr[14]) + (jArr[6] * jArr[13]) + (jArr[7] * jArr[12]) + (jArr[8] * jArr[11]) + (jArr[9] * jArr[10])), (2 * ((jArr[2] * jArr[18]) + (jArr[3] * jArr[17]) + (jArr[4] * jArr[16]) + (jArr[5] * jArr[15]) + (jArr[6] * jArr[14]) + (jArr[7] * jArr[13]) + (jArr[8] * jArr[12]) + (jArr[9] * jArr[11]))) + (jArr[10] * jArr[10]), 2 * ((jArr[3] * jArr[18]) + (jArr[4] * jArr[17]) + (jArr[5] * jArr[16]) + (jArr[6] * jArr[15]) + (jArr[7] * jArr[14]) + (jArr[8] * jArr[13]) + (jArr[9] * jArr[12]) + (jArr[10] * jArr[11])), (2 * ((jArr[4] * jArr[18]) + (jArr[5] * jArr[17]) + (jArr[6] * jArr[16]) + (jArr[7] * jArr[15]) + (jArr[8] * jArr[14]) + (jArr[9] * jArr[13]) + (jArr[10] * jArr[12]))) + (jArr[11] * jArr[11]), 2 * ((jArr[5] * jArr[18]) + (jArr[6] * jArr[17]) + (jArr[7] * jArr[16]) + (jArr[8] * jArr[15]) + (jArr[9] * jArr[14]) + (jArr[10] * jArr[13]) + (jArr[11] * jArr[12])), (2 * ((jArr[6] * jArr[18]) + (jArr[7] * jArr[17]) + (jArr[8] * jArr[16]) + (jArr[9] * jArr[15]) + (jArr[10] * jArr[14]) + (jArr[11] * jArr[13]))) + (jArr[12] * jArr[12]), 2 * ((jArr[7] * jArr[18]) + (jArr[8] * jArr[17]) + (jArr[9] * jArr[16]) + (jArr[10] * jArr[15]) + (jArr[11] * jArr[14]) + (jArr[12] * jArr[13])), (2 * ((jArr[8] * jArr[18]) + (jArr[9] * jArr[17]) + (jArr[10] * jArr[16]) + (jArr[11] * jArr[15]) + (jArr[12] * jArr[14]))) + (jArr[13] * jArr[13]), 2 * ((jArr[9] * jArr[18]) + (jArr[10] * jArr[17]) + (jArr[11] * jArr[16]) + (jArr[12] * jArr[15]) + (jArr[13] * jArr[14])), (2 * ((jArr[10] * jArr[18]) + (jArr[11] * jArr[17]) + (jArr[12] * jArr[16]) + (jArr[13] * jArr[15]))) + (jArr[14] * jArr[14]), 2 * ((jArr[11] * jArr[18]) + (jArr[12] * jArr[17]) + (jArr[13] * jArr[16]) + (jArr[14] * jArr[15])), (2 * ((jArr[12] * jArr[18]) + (jArr[13] * jArr[17]) + (jArr[14] * jArr[16]))) + (jArr[15] * jArr[15]), 2 * ((jArr[13] * jArr[18]) + (jArr[14] * jArr[17]) + (jArr[15] * jArr[16])), (2 * ((jArr[14] * jArr[18]) + (jArr[15] * jArr[17]))) + (jArr[16] * jArr[16]), 2 * ((jArr[15] * jArr[18]) + (jArr[16] * jArr[17])), (2 * jArr[16] * jArr[18]) + (jArr[17] * jArr[17]), 2 * jArr[17] * jArr[18], jArr[18] * jArr[18]);
    }
}
