package sun.security.util.math.intpoly;

import java.math.BigInteger;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomialP384.class */
public class IntegerPolynomialP384 extends IntegerPolynomial {
    private static final int BITS_PER_LIMB = 28;
    private static final int NUM_LIMBS = 14;
    private static final int MAX_ADDS = 2;
    public static final BigInteger MODULUS = evaluateModulus();
    private static final long CARRY_ADD = 134217728;
    private static final int LIMB_MASK = 268435455;

    public IntegerPolynomialP384() {
        super(28, 14, 2, MODULUS);
    }

    private static BigInteger evaluateModulus() {
        return BigInteger.valueOf(2L).pow(384).subtract(BigInteger.valueOf(2L).pow(128)).subtract(BigInteger.valueOf(2L).pow(96)).add(BigInteger.valueOf(2L).pow(32)).subtract(BigInteger.valueOf(1L));
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[13] >> 20;
        jArr[13] = jArr[13] - (j2 << 20);
        jArr[4] = jArr[4] + ((j2 << 16) & 268435455);
        jArr[5] = jArr[5] + (j2 >> 12);
        jArr[3] = jArr[3] + ((j2 << 12) & 268435455);
        jArr[4] = jArr[4] + (j2 >> 16);
        jArr[1] = jArr[1] - ((j2 << 4) & 268435455);
        jArr[2] = jArr[2] - (j2 >> 24);
        jArr[0] = jArr[0] + j2;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21, long j22, long j23, long j24, long j25, long j26, long j27, long j28) {
        long j29 = j19 + (j28 >> 4);
        long j30 = j18 + ((j28 << 24) & 268435455) + (j28 >> 8);
        long j31 = (j15 - ((j28 << 12) & 268435455)) + (j28 >> 20);
        long j32 = j17 + ((j28 << 20) & 268435455) + ((j27 << 24) & 268435455);
        long j33 = j30 + (j27 >> 4);
        long j34 = (j16 - (j28 >> 16)) + ((j27 << 20) & 268435455);
        long j35 = j32 + (j27 >> 8);
        long j36 = (j14 + ((j28 << 8) & 268435455)) - ((j27 << 12) & 268435455);
        long j37 = j31 - (j27 >> 16);
        long j38 = j36 + (j27 >> 20);
        long j39 = j34 + ((j26 << 24) & 268435455);
        long j40 = j35 + (j26 >> 4);
        long j41 = j37 + ((j26 << 20) & 268435455);
        long j42 = j39 + (j26 >> 8);
        long j43 = (j13 + ((j27 << 8) & 268435455)) - ((j26 << 12) & 268435455);
        long j44 = j38 - (j26 >> 16);
        long j45 = j43 + (j26 >> 20);
        long j46 = j41 + ((j25 << 24) & 268435455);
        long j47 = j42 + (j25 >> 4);
        long j48 = j44 + ((j25 << 20) & 268435455);
        long j49 = j46 + (j25 >> 8);
        long j50 = (j12 + ((j26 << 8) & 268435455)) - ((j25 << 12) & 268435455);
        long j51 = j45 - (j25 >> 16);
        long j52 = j50 + (j25 >> 20);
        long j53 = j48 + ((j24 << 24) & 268435455);
        long j54 = j49 + (j24 >> 4);
        long j55 = j51 + ((j24 << 20) & 268435455);
        long j56 = j53 + (j24 >> 8);
        long j57 = (j11 + ((j25 << 8) & 268435455)) - ((j24 << 12) & 268435455);
        long j58 = j52 - (j24 >> 16);
        long j59 = j57 + (j24 >> 20);
        long j60 = j55 + ((j23 << 24) & 268435455);
        long j61 = j56 + (j23 >> 4);
        long j62 = j58 + ((j23 << 20) & 268435455);
        long j63 = j60 + (j23 >> 8);
        long j64 = (j10 + ((j24 << 8) & 268435455)) - ((j23 << 12) & 268435455);
        long j65 = j59 - (j23 >> 16);
        long j66 = j64 + (j23 >> 20);
        long j67 = j62 + ((j22 << 24) & 268435455);
        long j68 = j63 + (j22 >> 4);
        long j69 = j65 + ((j22 << 20) & 268435455);
        long j70 = j67 + (j22 >> 8);
        long j71 = (j9 + ((j23 << 8) & 268435455)) - ((j22 << 12) & 268435455);
        long j72 = j66 - (j22 >> 16);
        long j73 = j71 + (j22 >> 20);
        long j74 = j69 + ((j21 << 24) & 268435455);
        long j75 = j70 + (j21 >> 4);
        long j76 = j72 + ((j21 << 20) & 268435455);
        long j77 = j74 + (j21 >> 8);
        long j78 = (j8 + ((j22 << 8) & 268435455)) - ((j21 << 12) & 268435455);
        long j79 = j73 - (j21 >> 16);
        long j80 = j78 + (j21 >> 20);
        long j81 = j76 + ((j20 << 24) & 268435455);
        long j82 = j77 + (j20 >> 4);
        long j83 = j79 + ((j20 << 20) & 268435455);
        long j84 = j81 + (j20 >> 8);
        long j85 = (j7 + ((j21 << 8) & 268435455)) - ((j20 << 12) & 268435455);
        long j86 = j80 - (j20 >> 16);
        long j87 = j85 + (j20 >> 20);
        long j88 = j83 + ((j29 << 24) & 268435455);
        long j89 = j84 + (j29 >> 4);
        long j90 = j86 + ((j29 << 20) & 268435455);
        long j91 = j88 + (j29 >> 8);
        long j92 = (j6 + ((j20 << 8) & 268435455)) - ((j29 << 12) & 268435455);
        long j93 = j87 - (j29 >> 16);
        long j94 = j5 + ((j29 << 8) & 268435455);
        long j95 = j92 + (j29 >> 20);
        long j96 = j90 + ((j33 << 24) & 268435455);
        long j97 = j91 + (j33 >> 4);
        long j98 = j93 + ((j33 << 20) & 268435455);
        long j99 = j96 + (j33 >> 8);
        long j100 = j94 - ((j33 << 12) & 268435455);
        long j101 = j95 - (j33 >> 16);
        long j102 = j4 + ((j33 << 8) & 268435455);
        long j103 = j100 + (j33 >> 20);
        long j104 = j98 + ((j40 << 24) & 268435455);
        long j105 = j99 + (j40 >> 4);
        long j106 = j101 + ((j40 << 20) & 268435455);
        long j107 = j104 + (j40 >> 8);
        long j108 = j102 - ((j40 << 12) & 268435455);
        long j109 = j103 - (j40 >> 16);
        long j110 = j3 + ((j40 << 8) & 268435455);
        long j111 = j108 + (j40 >> 20);
        long j112 = j106 + ((j47 << 24) & 268435455);
        long j113 = j107 + (j47 >> 4);
        long j114 = j109 + ((j47 << 20) & 268435455);
        long j115 = j112 + (j47 >> 8);
        long j116 = j110 - ((j47 << 12) & 268435455);
        carryReduce0(jArr, j2 + ((j47 << 8) & 268435455), j116 + (j47 >> 20), j111 - (j47 >> 16), j114, j115, j113, j105, j97, j89, j82, j75, j68, j61, j54, 0L, j40, j33, j29, j20, j21, j22, j23, j24, j25, j26, j27, j28, 0L);
    }

    void carryReduce0(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, long j18, long j19, long j20, long j21, long j22, long j23, long j24, long j25, long j26, long j27, long j28, long j29) {
        long j30 = (j14 + CARRY_ADD) >> 28;
        long j31 = j14 - (j30 << 28);
        long j32 = j15 + j30;
        long j33 = (j32 + CARRY_ADD) >> 28;
        long j34 = j32 - (j33 << 28);
        long j35 = j16 + j33;
        long j36 = j6 + ((j35 << 24) & 268435455);
        long j37 = j7 + (j35 >> 4);
        long j38 = j5 + ((j35 << 20) & 268435455);
        long j39 = j36 + (j35 >> 8);
        long j40 = j3 - ((j35 << 12) & 268435455);
        long j41 = j4 - (j35 >> 16);
        long j42 = j2 + ((j35 << 8) & 268435455);
        long j43 = j40 + (j35 >> 20);
        long j44 = (j42 + CARRY_ADD) >> 28;
        long j45 = j42 - (j44 << 28);
        long j46 = j43 + j44;
        long j47 = (j46 + CARRY_ADD) >> 28;
        long j48 = j46 - (j47 << 28);
        long j49 = j41 + j47;
        long j50 = (j49 + CARRY_ADD) >> 28;
        long j51 = j49 - (j50 << 28);
        long j52 = j38 + j50;
        long j53 = (j52 + CARRY_ADD) >> 28;
        long j54 = j52 - (j53 << 28);
        long j55 = j39 + j53;
        long j56 = (j55 + CARRY_ADD) >> 28;
        long j57 = j55 - (j56 << 28);
        long j58 = j37 + j56;
        long j59 = (j58 + CARRY_ADD) >> 28;
        long j60 = j58 - (j59 << 28);
        long j61 = j8 + j59;
        long j62 = (j61 + CARRY_ADD) >> 28;
        long j63 = j61 - (j62 << 28);
        long j64 = j9 + j62;
        long j65 = (j64 + CARRY_ADD) >> 28;
        long j66 = j64 - (j65 << 28);
        long j67 = j10 + j65;
        long j68 = (j67 + CARRY_ADD) >> 28;
        long j69 = j67 - (j68 << 28);
        long j70 = j11 + j68;
        long j71 = (j70 + CARRY_ADD) >> 28;
        long j72 = j70 - (j71 << 28);
        long j73 = j12 + j71;
        long j74 = (j73 + CARRY_ADD) >> 28;
        long j75 = j73 - (j74 << 28);
        long j76 = j13 + j74;
        long j77 = (j76 + CARRY_ADD) >> 28;
        long j78 = j76 - (j77 << 28);
        long j79 = j31 + j77;
        long j80 = (j79 + CARRY_ADD) >> 28;
        jArr[0] = j45;
        jArr[1] = j48;
        jArr[2] = j51;
        jArr[3] = j54;
        jArr[4] = j57;
        jArr[5] = j60;
        jArr[6] = j63;
        jArr[7] = j66;
        jArr[8] = j69;
        jArr[9] = j72;
        jArr[10] = j75;
        jArr[11] = j78;
        jArr[12] = j79 - (j80 << 28);
        jArr[13] = j34 + j80;
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15) {
        long j16 = (j14 + CARRY_ADD) >> 28;
        long j17 = j14 - (j16 << 28);
        long j18 = j15 + j16;
        long j19 = (j18 + CARRY_ADD) >> 28;
        long j20 = j18 - (j19 << 28);
        long j21 = 0 + j19;
        long j22 = j6 + ((j21 << 24) & 268435455);
        long j23 = j7 + (j21 >> 4);
        long j24 = j5 + ((j21 << 20) & 268435455);
        long j25 = j22 + (j21 >> 8);
        long j26 = j3 - ((j21 << 12) & 268435455);
        long j27 = j4 - (j21 >> 16);
        long j28 = j2 + ((j21 << 8) & 268435455);
        long j29 = j26 + (j21 >> 20);
        long j30 = (j28 + CARRY_ADD) >> 28;
        long j31 = j28 - (j30 << 28);
        long j32 = j29 + j30;
        long j33 = (j32 + CARRY_ADD) >> 28;
        long j34 = j32 - (j33 << 28);
        long j35 = j27 + j33;
        long j36 = (j35 + CARRY_ADD) >> 28;
        long j37 = j35 - (j36 << 28);
        long j38 = j24 + j36;
        long j39 = (j38 + CARRY_ADD) >> 28;
        long j40 = j38 - (j39 << 28);
        long j41 = j25 + j39;
        long j42 = (j41 + CARRY_ADD) >> 28;
        long j43 = j41 - (j42 << 28);
        long j44 = j23 + j42;
        long j45 = (j44 + CARRY_ADD) >> 28;
        long j46 = j44 - (j45 << 28);
        long j47 = j8 + j45;
        long j48 = (j47 + CARRY_ADD) >> 28;
        long j49 = j47 - (j48 << 28);
        long j50 = j9 + j48;
        long j51 = (j50 + CARRY_ADD) >> 28;
        long j52 = j50 - (j51 << 28);
        long j53 = j10 + j51;
        long j54 = (j53 + CARRY_ADD) >> 28;
        long j55 = j53 - (j54 << 28);
        long j56 = j11 + j54;
        long j57 = (j56 + CARRY_ADD) >> 28;
        long j58 = j56 - (j57 << 28);
        long j59 = j12 + j57;
        long j60 = (j59 + CARRY_ADD) >> 28;
        long j61 = j59 - (j60 << 28);
        long j62 = j13 + j60;
        long j63 = (j62 + CARRY_ADD) >> 28;
        long j64 = j62 - (j63 << 28);
        long j65 = j17 + j63;
        long j66 = (j65 + CARRY_ADD) >> 28;
        jArr[0] = j31;
        jArr[1] = j34;
        jArr[2] = j37;
        jArr[3] = j40;
        jArr[4] = j43;
        jArr[5] = j46;
        jArr[6] = j49;
        jArr[7] = j52;
        jArr[8] = j55;
        jArr[9] = j58;
        jArr[10] = j61;
        jArr[11] = j64;
        jArr[12] = j65 - (j66 << 28);
        jArr[13] = j20 + j66;
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[0] * jArr2[5]) + (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]) + (jArr[5] * jArr2[0]), (jArr[0] * jArr2[6]) + (jArr[1] * jArr2[5]) + (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]) + (jArr[5] * jArr2[1]) + (jArr[6] * jArr2[0]), (jArr[0] * jArr2[7]) + (jArr[1] * jArr2[6]) + (jArr[2] * jArr2[5]) + (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]) + (jArr[5] * jArr2[2]) + (jArr[6] * jArr2[1]) + (jArr[7] * jArr2[0]), (jArr[0] * jArr2[8]) + (jArr[1] * jArr2[7]) + (jArr[2] * jArr2[6]) + (jArr[3] * jArr2[5]) + (jArr[4] * jArr2[4]) + (jArr[5] * jArr2[3]) + (jArr[6] * jArr2[2]) + (jArr[7] * jArr2[1]) + (jArr[8] * jArr2[0]), (jArr[0] * jArr2[9]) + (jArr[1] * jArr2[8]) + (jArr[2] * jArr2[7]) + (jArr[3] * jArr2[6]) + (jArr[4] * jArr2[5]) + (jArr[5] * jArr2[4]) + (jArr[6] * jArr2[3]) + (jArr[7] * jArr2[2]) + (jArr[8] * jArr2[1]) + (jArr[9] * jArr2[0]), (jArr[0] * jArr2[10]) + (jArr[1] * jArr2[9]) + (jArr[2] * jArr2[8]) + (jArr[3] * jArr2[7]) + (jArr[4] * jArr2[6]) + (jArr[5] * jArr2[5]) + (jArr[6] * jArr2[4]) + (jArr[7] * jArr2[3]) + (jArr[8] * jArr2[2]) + (jArr[9] * jArr2[1]) + (jArr[10] * jArr2[0]), (jArr[0] * jArr2[11]) + (jArr[1] * jArr2[10]) + (jArr[2] * jArr2[9]) + (jArr[3] * jArr2[8]) + (jArr[4] * jArr2[7]) + (jArr[5] * jArr2[6]) + (jArr[6] * jArr2[5]) + (jArr[7] * jArr2[4]) + (jArr[8] * jArr2[3]) + (jArr[9] * jArr2[2]) + (jArr[10] * jArr2[1]) + (jArr[11] * jArr2[0]), (jArr[0] * jArr2[12]) + (jArr[1] * jArr2[11]) + (jArr[2] * jArr2[10]) + (jArr[3] * jArr2[9]) + (jArr[4] * jArr2[8]) + (jArr[5] * jArr2[7]) + (jArr[6] * jArr2[6]) + (jArr[7] * jArr2[5]) + (jArr[8] * jArr2[4]) + (jArr[9] * jArr2[3]) + (jArr[10] * jArr2[2]) + (jArr[11] * jArr2[1]) + (jArr[12] * jArr2[0]), (jArr[0] * jArr2[13]) + (jArr[1] * jArr2[12]) + (jArr[2] * jArr2[11]) + (jArr[3] * jArr2[10]) + (jArr[4] * jArr2[9]) + (jArr[5] * jArr2[8]) + (jArr[6] * jArr2[7]) + (jArr[7] * jArr2[6]) + (jArr[8] * jArr2[5]) + (jArr[9] * jArr2[4]) + (jArr[10] * jArr2[3]) + (jArr[11] * jArr2[2]) + (jArr[12] * jArr2[1]) + (jArr[13] * jArr2[0]), (jArr[1] * jArr2[13]) + (jArr[2] * jArr2[12]) + (jArr[3] * jArr2[11]) + (jArr[4] * jArr2[10]) + (jArr[5] * jArr2[9]) + (jArr[6] * jArr2[8]) + (jArr[7] * jArr2[7]) + (jArr[8] * jArr2[6]) + (jArr[9] * jArr2[5]) + (jArr[10] * jArr2[4]) + (jArr[11] * jArr2[3]) + (jArr[12] * jArr2[2]) + (jArr[13] * jArr2[1]), (jArr[2] * jArr2[13]) + (jArr[3] * jArr2[12]) + (jArr[4] * jArr2[11]) + (jArr[5] * jArr2[10]) + (jArr[6] * jArr2[9]) + (jArr[7] * jArr2[8]) + (jArr[8] * jArr2[7]) + (jArr[9] * jArr2[6]) + (jArr[10] * jArr2[5]) + (jArr[11] * jArr2[4]) + (jArr[12] * jArr2[3]) + (jArr[13] * jArr2[2]), (jArr[3] * jArr2[13]) + (jArr[4] * jArr2[12]) + (jArr[5] * jArr2[11]) + (jArr[6] * jArr2[10]) + (jArr[7] * jArr2[9]) + (jArr[8] * jArr2[8]) + (jArr[9] * jArr2[7]) + (jArr[10] * jArr2[6]) + (jArr[11] * jArr2[5]) + (jArr[12] * jArr2[4]) + (jArr[13] * jArr2[3]), (jArr[4] * jArr2[13]) + (jArr[5] * jArr2[12]) + (jArr[6] * jArr2[11]) + (jArr[7] * jArr2[10]) + (jArr[8] * jArr2[9]) + (jArr[9] * jArr2[8]) + (jArr[10] * jArr2[7]) + (jArr[11] * jArr2[6]) + (jArr[12] * jArr2[5]) + (jArr[13] * jArr2[4]), (jArr[5] * jArr2[13]) + (jArr[6] * jArr2[12]) + (jArr[7] * jArr2[11]) + (jArr[8] * jArr2[10]) + (jArr[9] * jArr2[9]) + (jArr[10] * jArr2[8]) + (jArr[11] * jArr2[7]) + (jArr[12] * jArr2[6]) + (jArr[13] * jArr2[5]), (jArr[6] * jArr2[13]) + (jArr[7] * jArr2[12]) + (jArr[8] * jArr2[11]) + (jArr[9] * jArr2[10]) + (jArr[10] * jArr2[9]) + (jArr[11] * jArr2[8]) + (jArr[12] * jArr2[7]) + (jArr[13] * jArr2[6]), (jArr[7] * jArr2[13]) + (jArr[8] * jArr2[12]) + (jArr[9] * jArr2[11]) + (jArr[10] * jArr2[10]) + (jArr[11] * jArr2[9]) + (jArr[12] * jArr2[8]) + (jArr[13] * jArr2[7]), (jArr[8] * jArr2[13]) + (jArr[9] * jArr2[12]) + (jArr[10] * jArr2[11]) + (jArr[11] * jArr2[10]) + (jArr[12] * jArr2[9]) + (jArr[13] * jArr2[8]), (jArr[9] * jArr2[13]) + (jArr[10] * jArr2[12]) + (jArr[11] * jArr2[11]) + (jArr[12] * jArr2[10]) + (jArr[13] * jArr2[9]), (jArr[10] * jArr2[13]) + (jArr[11] * jArr2[12]) + (jArr[12] * jArr2[11]) + (jArr[13] * jArr2[10]), (jArr[11] * jArr2[13]) + (jArr[12] * jArr2[12]) + (jArr[13] * jArr2[11]), (jArr[12] * jArr2[13]) + (jArr[13] * jArr2[12]), jArr[13] * jArr2[13]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        carryReduce(jArr, jArr[0], jArr[1], jArr[2], jArr[3], jArr[4], jArr[5], jArr[6], jArr[7], jArr[8], jArr[9], jArr[10], jArr[11], jArr[12], jArr[13]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (2 * jArr[0] * jArr[2]) + (jArr[1] * jArr[1]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))) + (jArr[2] * jArr[2]), 2 * ((jArr[0] * jArr[5]) + (jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (2 * ((jArr[0] * jArr[6]) + (jArr[1] * jArr[5]) + (jArr[2] * jArr[4]))) + (jArr[3] * jArr[3]), 2 * ((jArr[0] * jArr[7]) + (jArr[1] * jArr[6]) + (jArr[2] * jArr[5]) + (jArr[3] * jArr[4])), (2 * ((jArr[0] * jArr[8]) + (jArr[1] * jArr[7]) + (jArr[2] * jArr[6]) + (jArr[3] * jArr[5]))) + (jArr[4] * jArr[4]), 2 * ((jArr[0] * jArr[9]) + (jArr[1] * jArr[8]) + (jArr[2] * jArr[7]) + (jArr[3] * jArr[6]) + (jArr[4] * jArr[5])), (2 * ((jArr[0] * jArr[10]) + (jArr[1] * jArr[9]) + (jArr[2] * jArr[8]) + (jArr[3] * jArr[7]) + (jArr[4] * jArr[6]))) + (jArr[5] * jArr[5]), 2 * ((jArr[0] * jArr[11]) + (jArr[1] * jArr[10]) + (jArr[2] * jArr[9]) + (jArr[3] * jArr[8]) + (jArr[4] * jArr[7]) + (jArr[5] * jArr[6])), (2 * ((jArr[0] * jArr[12]) + (jArr[1] * jArr[11]) + (jArr[2] * jArr[10]) + (jArr[3] * jArr[9]) + (jArr[4] * jArr[8]) + (jArr[5] * jArr[7]))) + (jArr[6] * jArr[6]), 2 * ((jArr[0] * jArr[13]) + (jArr[1] * jArr[12]) + (jArr[2] * jArr[11]) + (jArr[3] * jArr[10]) + (jArr[4] * jArr[9]) + (jArr[5] * jArr[8]) + (jArr[6] * jArr[7])), (2 * ((jArr[1] * jArr[13]) + (jArr[2] * jArr[12]) + (jArr[3] * jArr[11]) + (jArr[4] * jArr[10]) + (jArr[5] * jArr[9]) + (jArr[6] * jArr[8]))) + (jArr[7] * jArr[7]), 2 * ((jArr[2] * jArr[13]) + (jArr[3] * jArr[12]) + (jArr[4] * jArr[11]) + (jArr[5] * jArr[10]) + (jArr[6] * jArr[9]) + (jArr[7] * jArr[8])), (2 * ((jArr[3] * jArr[13]) + (jArr[4] * jArr[12]) + (jArr[5] * jArr[11]) + (jArr[6] * jArr[10]) + (jArr[7] * jArr[9]))) + (jArr[8] * jArr[8]), 2 * ((jArr[4] * jArr[13]) + (jArr[5] * jArr[12]) + (jArr[6] * jArr[11]) + (jArr[7] * jArr[10]) + (jArr[8] * jArr[9])), (2 * ((jArr[5] * jArr[13]) + (jArr[6] * jArr[12]) + (jArr[7] * jArr[11]) + (jArr[8] * jArr[10]))) + (jArr[9] * jArr[9]), 2 * ((jArr[6] * jArr[13]) + (jArr[7] * jArr[12]) + (jArr[8] * jArr[11]) + (jArr[9] * jArr[10])), (2 * ((jArr[7] * jArr[13]) + (jArr[8] * jArr[12]) + (jArr[9] * jArr[11]))) + (jArr[10] * jArr[10]), 2 * ((jArr[8] * jArr[13]) + (jArr[9] * jArr[12]) + (jArr[10] * jArr[11])), (2 * ((jArr[9] * jArr[13]) + (jArr[10] * jArr[12]))) + (jArr[11] * jArr[11]), 2 * ((jArr[10] * jArr[13]) + (jArr[11] * jArr[12])), (2 * jArr[11] * jArr[13]) + (jArr[12] * jArr[12]), 2 * jArr[12] * jArr[13], jArr[13] * jArr[13]);
    }
}
