package sun.security.util.math.intpoly;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial1305.class */
public class IntegerPolynomial1305 extends IntegerPolynomial {
    protected static final int SUBTRAHEND = 5;
    protected static final int NUM_LIMBS = 5;
    private static final int POWER = 130;
    private static final int BITS_PER_LIMB = 26;
    private static final BigInteger MODULUS = TWO.pow(130).subtract(BigInteger.valueOf(5));

    public IntegerPolynomial1305() {
        super(26, 5, 1, MODULUS);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void mult(long[] jArr, long[] jArr2, long[] jArr3) {
        carryReduce(jArr3, jArr[0] * jArr2[0], (jArr[0] * jArr2[1]) + (jArr[1] * jArr2[0]), (jArr[0] * jArr2[2]) + (jArr[1] * jArr2[1]) + (jArr[2] * jArr2[0]), (jArr[0] * jArr2[3]) + (jArr[1] * jArr2[2]) + (jArr[2] * jArr2[1]) + (jArr[3] * jArr2[0]), (jArr[0] * jArr2[4]) + (jArr[1] * jArr2[3]) + (jArr[2] * jArr2[2]) + (jArr[3] * jArr2[1]) + (jArr[4] * jArr2[0]), (jArr[1] * jArr2[4]) + (jArr[2] * jArr2[3]) + (jArr[3] * jArr2[2]) + (jArr[4] * jArr2[1]), (jArr[2] * jArr2[4]) + (jArr[3] * jArr2[3]) + (jArr[4] * jArr2[2]), (jArr[3] * jArr2[4]) + (jArr[4] * jArr2[3]), jArr[4] * jArr2[4]);
    }

    private void carryReduce(long[] jArr, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10) {
        jArr[2] = j4 + (j9 * 5);
        long j11 = j5 + (j10 * 5);
        long jCarryValue = carryValue(j11);
        jArr[3] = j11 - (jCarryValue << 26);
        long j12 = j6 + jCarryValue;
        long jCarryValue2 = carryValue(j12);
        jArr[4] = j12 - (jCarryValue2 << 26);
        jArr[0] = j2 + ((j7 + jCarryValue2) * 5);
        jArr[1] = j3 + (j8 * 5);
        carry(jArr);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void square(long[] jArr, long[] jArr2) {
        carryReduce(jArr2, jArr[0] * jArr[0], 2 * jArr[0] * jArr[1], (2 * jArr[0] * jArr[2]) + (jArr[1] * jArr[1]), 2 * ((jArr[0] * jArr[3]) + (jArr[1] * jArr[2])), (2 * ((jArr[0] * jArr[4]) + (jArr[1] * jArr[3]))) + (jArr[2] * jArr[2]), 2 * ((jArr[1] * jArr[4]) + (jArr[2] * jArr[3])), (2 * jArr[2] * jArr[4]) + (jArr[3] * jArr[3]), 2 * jArr[3] * jArr[4], jArr[4] * jArr[4]);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void encode(ByteBuffer byteBuffer, int i2, byte b2, long[] jArr) {
        if (i2 == 16) {
            encode(byteBuffer.getLong(), byteBuffer.getLong(), b2, jArr);
        } else {
            super.encode(byteBuffer, i2, b2, jArr);
        }
    }

    protected void encode(long j2, long j3, byte b2, long[] jArr) {
        jArr[0] = j3 & 67108863;
        jArr[1] = (j3 >>> 26) & 67108863;
        jArr[2] = (j3 >>> 52) + ((j2 & 16383) << 12);
        jArr[3] = (j2 >>> 14) & 67108863;
        jArr[4] = (j2 >>> 40) + (b2 << 24);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void encode(byte[] bArr, int i2, int i3, byte b2, long[] jArr) {
        if (i3 == 16) {
            ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr, i2, i3).order(ByteOrder.LITTLE_ENDIAN);
            encode(byteBufferOrder.getLong(), byteBufferOrder.getLong(), b2, jArr);
            return;
        }
        super.encode(bArr, i2, i3, b2, jArr);
    }

    private void modReduceIn(long[] jArr, int i2, long j2) {
        int i3 = i2 - 5;
        jArr[i3] = jArr[i3] + (j2 * 5);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void finalCarryReduceLast(long[] jArr) {
        long j2 = jArr[this.numLimbs - 1] >> this.bitsPerLimb;
        int i2 = this.numLimbs - 1;
        jArr[i2] = jArr[i2] - (j2 << this.bitsPerLimb);
        modReduceIn(jArr, this.numLimbs, j2);
    }

    protected final void modReduce(long[] jArr, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            modReduceIn(jArr, i4, jArr[i4]);
            jArr[i4] = 0;
        }
    }

    protected void modReduce(long[] jArr) {
        modReduce(jArr, 5, 4);
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected long carryValue(long j2) {
        return j2 >> 26;
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void postEncodeCarry(long[] jArr) {
    }

    @Override // sun.security.util.math.intpoly.IntegerPolynomial
    protected void reduce(long[] jArr) {
        long jCarryOut = carryOut(jArr, 3) + jArr[4];
        long jCarryValue = carryValue(jCarryOut);
        jArr[4] = jCarryOut - (jCarryValue << 26);
        modReduceIn(jArr, 5, jCarryValue);
        carry(jArr);
    }
}
