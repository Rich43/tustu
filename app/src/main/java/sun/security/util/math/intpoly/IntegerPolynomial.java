package sun.security.util.math.intpoly;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.IntegerModuloP;
import sun.security.util.math.MutableIntegerModuloP;
import sun.security.util.math.SmallValue;

/* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial.class */
public abstract class IntegerPolynomial implements IntegerFieldModuloP {
    protected static final BigInteger TWO = BigInteger.valueOf(2);
    protected final int numLimbs;
    private final BigInteger modulus;
    protected final int bitsPerLimb;
    private final long[] posModLimbs = setPosModLimbs();
    private final int maxAdds;

    protected abstract void reduce(long[] jArr);

    protected abstract void mult(long[] jArr, long[] jArr2, long[] jArr3);

    protected abstract void square(long[] jArr, long[] jArr2);

    protected abstract void finalCarryReduceLast(long[] jArr);

    protected void multByInt(long[] jArr, long j2) {
        for (int i2 = 0; i2 < jArr.length; i2++) {
            int i3 = i2;
            jArr[i3] = jArr[i3] * j2;
        }
        reduce(jArr);
    }

    IntegerPolynomial(int i2, int i3, int i4, BigInteger bigInteger) {
        this.numLimbs = i3;
        this.modulus = bigInteger;
        this.bitsPerLimb = i2;
        this.maxAdds = i4;
    }

    private long[] setPosModLimbs() {
        long[] jArr = new long[this.numLimbs];
        setLimbsValuePositive(this.modulus, jArr);
        return jArr;
    }

    protected int getNumLimbs() {
        return this.numLimbs;
    }

    public int getMaxAdds() {
        return this.maxAdds;
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public BigInteger getSize() {
        return this.modulus;
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public ImmutableElement get0() {
        return new ImmutableElement(false);
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public ImmutableElement get1() {
        return new ImmutableElement(true);
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public ImmutableElement getElement(BigInteger bigInteger) {
        return new ImmutableElement(bigInteger);
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public SmallValue getSmallValue(int i2) {
        int i3 = 1 << (this.bitsPerLimb - 1);
        if (Math.abs(i2) >= i3) {
            throw new IllegalArgumentException("max magnitude is " + i3);
        }
        return new Limb(i2);
    }

    protected void encode(ByteBuffer byteBuffer, int i2, byte b2, long[] jArr) {
        int iNumberOfLeadingZeros = ((((8 * i2) + (32 - Integer.numberOfLeadingZeros(b2))) + this.bitsPerLimb) - 1) / this.bitsPerLimb;
        if (iNumberOfLeadingZeros > this.numLimbs) {
            long[] jArr2 = new long[iNumberOfLeadingZeros];
            encodeSmall(byteBuffer, i2, b2, jArr2);
            System.arraycopy(jArr2, 0, jArr, 0, jArr.length);
            return;
        }
        encodeSmall(byteBuffer, i2, b2, jArr);
    }

    protected void encodeSmall(ByteBuffer byteBuffer, int i2, byte b2, long[] jArr) {
        int i3 = 0;
        long j2 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            long j3 = byteBuffer.get() & 255;
            if (i4 + 8 >= this.bitsPerLimb) {
                int i6 = this.bitsPerLimb - i4;
                int i7 = i3;
                i3++;
                jArr[i7] = j2 + ((j3 & (255 >> (8 - i6))) << i4);
                j2 = j3 >> i6;
                i4 = 8 - i6;
            } else {
                j2 += j3 << i4;
                i4 += 8;
            }
        }
        if (b2 != 0) {
            long j4 = b2 & 255;
            if (i4 + 8 >= this.bitsPerLimb) {
                int i8 = this.bitsPerLimb - i4;
                int i9 = i3;
                i3++;
                jArr[i9] = j2 + ((j4 & (255 >> (8 - i8))) << i4);
                j2 = j4 >> i8;
            } else {
                j2 += j4 << i4;
            }
        }
        if (i3 < jArr.length) {
            int i10 = i3;
            i3++;
            jArr[i10] = j2;
        }
        Arrays.fill(jArr, i3, jArr.length, 0L);
        postEncodeCarry(jArr);
    }

    protected void encode(byte[] bArr, int i2, int i3, byte b2, long[] jArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr, i2, i3);
        byteBufferWrap.order(ByteOrder.LITTLE_ENDIAN);
        encode(byteBufferWrap, i3, b2, jArr);
    }

    protected void postEncodeCarry(long[] jArr) {
        reduce(jArr);
    }

    @Override // sun.security.util.math.IntegerFieldModuloP
    public ImmutableElement getElement(byte[] bArr, int i2, int i3, byte b2) {
        long[] jArr = new long[this.numLimbs];
        encode(bArr, i2, i3, b2, jArr);
        return new ImmutableElement(jArr, 0);
    }

    protected BigInteger evaluate(long[] jArr) {
        BigInteger bigIntegerAdd = BigInteger.ZERO;
        for (int length = jArr.length - 1; length >= 0; length--) {
            bigIntegerAdd = bigIntegerAdd.shiftLeft(this.bitsPerLimb).add(BigInteger.valueOf(jArr[length]));
        }
        return bigIntegerAdd.mod(this.modulus);
    }

    protected long carryValue(long j2) {
        return (j2 + (1 << (this.bitsPerLimb - 1))) >> this.bitsPerLimb;
    }

    protected void carry(long[] jArr, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            int i5 = i4 + 1;
            jArr[i5] = jArr[i5] + carryOut(jArr, i4);
        }
    }

    protected void carry(long[] jArr) {
        carry(jArr, 0, jArr.length - 1);
    }

    protected long carryOut(long[] jArr, int i2) {
        long jCarryValue = carryValue(jArr[i2]);
        jArr[i2] = jArr[i2] - (jCarryValue << this.bitsPerLimb);
        return jCarryValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLimbsValue(BigInteger bigInteger, long[] jArr) {
        setLimbsValuePositive(bigInteger, jArr);
        carry(jArr);
    }

    protected void setLimbsValuePositive(BigInteger bigInteger, long[] jArr) {
        BigInteger bigIntegerValueOf = BigInteger.valueOf(1 << this.bitsPerLimb);
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = bigInteger.mod(bigIntegerValueOf).longValue();
            bigInteger = bigInteger.shiftRight(this.bitsPerLimb);
        }
    }

    protected void finalReduce(long[] jArr) {
        for (int i2 = 0; i2 < 2; i2++) {
            finalCarryReduceLast(jArr);
            long j2 = 0;
            for (int i3 = 0; i3 < this.numLimbs - 1; i3++) {
                int i4 = i3;
                jArr[i4] = jArr[i4] + j2;
                j2 = jArr[i3] >> this.bitsPerLimb;
                int i5 = i3;
                jArr[i5] = jArr[i5] - (j2 << this.bitsPerLimb);
            }
            int i6 = this.numLimbs - 1;
            jArr[i6] = jArr[i6] + j2;
        }
        int i7 = 1;
        long[] jArr2 = new long[this.numLimbs];
        for (int i8 = this.numLimbs - 1; i8 >= 0; i8--) {
            jArr2[i8] = jArr[i8] - this.posModLimbs[i8];
            i7 *= ((int) (jArr2[i8] >> 63)) + 1;
        }
        conditionalSwap(i7, jArr, jArr2);
    }

    protected void decode(long[] jArr, byte[] bArr, int i2, int i3) {
        int i4 = 0 + 1;
        long j2 = jArr[0];
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i6 + i2;
            if (i5 + 8 >= this.bitsPerLimb) {
                bArr[i7] = (byte) j2;
                long j3 = 0;
                if (i4 < jArr.length) {
                    int i8 = i4;
                    i4++;
                    j3 = jArr[i8];
                }
                int i9 = 8 - (this.bitsPerLimb - i5);
                bArr[i7] = (byte) (bArr[i7] + ((j3 & (255 >> r0)) << r0));
                j2 = j3 >> i9;
                i5 = i9;
            } else {
                bArr[i7] = (byte) j2;
                j2 >>= 8;
                i5 += 8;
            }
        }
    }

    protected void addLimbs(long[] jArr, long[] jArr2, long[] jArr3) {
        for (int i2 = 0; i2 < jArr3.length; i2++) {
            jArr3[i2] = jArr[i2] + jArr2[i2];
        }
    }

    protected static void conditionalAssign(int i2, long[] jArr, long[] jArr2) {
        int i3 = 0 - i2;
        for (int i4 = 0; i4 < jArr.length; i4++) {
            jArr[i4] = (i3 & (jArr[i4] ^ jArr2[i4])) ^ jArr[i4];
        }
    }

    protected static void conditionalSwap(int i2, long[] jArr, long[] jArr2) {
        int i3 = 0 - i2;
        for (int i4 = 0; i4 < jArr.length; i4++) {
            long j2 = i3 & (jArr[i4] ^ jArr2[i4]);
            jArr[i4] = j2 ^ jArr[i4];
            jArr2[i4] = j2 ^ jArr2[i4];
        }
    }

    protected void limbsToByteArray(long[] jArr, byte[] bArr) {
        long[] jArr2 = (long[]) jArr.clone();
        finalReduce(jArr2);
        decode(jArr2, bArr, 0, bArr.length);
    }

    protected void addLimbsModPowerTwo(long[] jArr, long[] jArr2, byte[] bArr) {
        long[] jArr3 = (long[]) jArr2.clone();
        long[] jArr4 = (long[]) jArr.clone();
        finalReduce(jArr3);
        finalReduce(jArr4);
        addLimbs(jArr4, jArr3, jArr4);
        long j2 = 0;
        for (int i2 = 0; i2 < this.numLimbs; i2++) {
            int i3 = i2;
            jArr4[i3] = jArr4[i3] + j2;
            j2 = jArr4[i2] >> this.bitsPerLimb;
            int i4 = i2;
            jArr4[i4] = jArr4[i4] - (j2 << this.bitsPerLimb);
        }
        decode(jArr4, bArr, 0, bArr.length);
    }

    /* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial$Element.class */
    private abstract class Element implements IntegerModuloP {
        protected long[] limbs;
        protected int numAdds;

        public Element(BigInteger bigInteger) {
            this.limbs = new long[IntegerPolynomial.this.numLimbs];
            setValue(bigInteger);
        }

        public Element(boolean z2) {
            this.limbs = new long[IntegerPolynomial.this.numLimbs];
            this.limbs[0] = z2 ? 1L : 0L;
            this.numAdds = 0;
        }

        private Element(long[] jArr, int i2) {
            this.limbs = jArr;
            this.numAdds = i2;
        }

        private void setValue(BigInteger bigInteger) {
            IntegerPolynomial.this.setLimbsValue(bigInteger, this.limbs);
            this.numAdds = 0;
        }

        @Override // sun.security.util.math.IntegerModuloP
        public IntegerFieldModuloP getField() {
            return IntegerPolynomial.this;
        }

        @Override // sun.security.util.math.IntegerModuloP
        public BigInteger asBigInteger() {
            return IntegerPolynomial.this.evaluate(this.limbs);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public MutableElement mutable() {
            return IntegerPolynomial.this.new MutableElement((long[]) this.limbs.clone(), this.numAdds);
        }

        protected boolean isSummand() {
            return this.numAdds < IntegerPolynomial.this.maxAdds;
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement add(IntegerModuloP integerModuloP) {
            Element element = (Element) integerModuloP;
            if (!isSummand() || !element.isSummand()) {
                throw new ArithmeticException("Not a valid summand");
            }
            long[] jArr = new long[this.limbs.length];
            for (int i2 = 0; i2 < this.limbs.length; i2++) {
                jArr[i2] = this.limbs[i2] + element.limbs[i2];
            }
            return IntegerPolynomial.this.new ImmutableElement(jArr, Math.max(this.numAdds, element.numAdds) + 1);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement additiveInverse() {
            long[] jArr = new long[this.limbs.length];
            for (int i2 = 0; i2 < this.limbs.length; i2++) {
                jArr[i2] = -this.limbs[i2];
            }
            return IntegerPolynomial.this.new ImmutableElement(jArr, this.numAdds);
        }

        protected long[] cloneLow(long[] jArr) {
            long[] jArr2 = new long[IntegerPolynomial.this.numLimbs];
            copyLow(jArr, jArr2);
            return jArr2;
        }

        protected void copyLow(long[] jArr, long[] jArr2) {
            System.arraycopy(jArr, 0, jArr2, 0, jArr2.length);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement multiply(IntegerModuloP integerModuloP) {
            long[] jArr = new long[this.limbs.length];
            IntegerPolynomial.this.mult(this.limbs, ((Element) integerModuloP).limbs, jArr);
            return IntegerPolynomial.this.new ImmutableElement(jArr, 0);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement square() {
            long[] jArr = new long[this.limbs.length];
            IntegerPolynomial.this.square(this.limbs, jArr);
            return IntegerPolynomial.this.new ImmutableElement(jArr, 0);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public void addModPowerTwo(IntegerModuloP integerModuloP, byte[] bArr) {
            Element element = (Element) integerModuloP;
            if (!isSummand() || !element.isSummand()) {
                throw new ArithmeticException("Not a valid summand");
            }
            IntegerPolynomial.this.addLimbsModPowerTwo(this.limbs, element.limbs, bArr);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public void asByteArray(byte[] bArr) {
            if (!isSummand()) {
                throw new ArithmeticException("Not a valid summand");
            }
            IntegerPolynomial.this.limbsToByteArray(this.limbs, bArr);
        }
    }

    /* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial$MutableElement.class */
    protected class MutableElement extends Element implements MutableIntegerModuloP {
        protected MutableElement(long[] jArr, int i2) {
            super(jArr, i2);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement fixed() {
            return IntegerPolynomial.this.new ImmutableElement((long[]) this.limbs.clone(), this.numAdds);
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public void conditionalSet(IntegerModuloP integerModuloP, int i2) {
            Element element = (Element) integerModuloP;
            IntegerPolynomial.conditionalAssign(i2, this.limbs, element.limbs);
            this.numAdds = element.numAdds;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public void conditionalSwapWith(MutableIntegerModuloP mutableIntegerModuloP, int i2) {
            MutableElement mutableElement = (MutableElement) mutableIntegerModuloP;
            IntegerPolynomial.conditionalSwap(i2, this.limbs, mutableElement.limbs);
            int i3 = this.numAdds;
            this.numAdds = mutableElement.numAdds;
            mutableElement.numAdds = i3;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setValue(IntegerModuloP integerModuloP) {
            Element element = (Element) integerModuloP;
            System.arraycopy(element.limbs, 0, this.limbs, 0, element.limbs.length);
            this.numAdds = element.numAdds;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setValue(byte[] bArr, int i2, int i3, byte b2) {
            IntegerPolynomial.this.encode(bArr, i2, i3, b2, this.limbs);
            this.numAdds = 0;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setValue(ByteBuffer byteBuffer, int i2, byte b2) {
            IntegerPolynomial.this.encode(byteBuffer, i2, b2, this.limbs);
            this.numAdds = 0;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setProduct(IntegerModuloP integerModuloP) {
            IntegerPolynomial.this.mult(this.limbs, ((Element) integerModuloP).limbs, this.limbs);
            this.numAdds = 0;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setProduct(SmallValue smallValue) {
            IntegerPolynomial.this.multByInt(this.limbs, ((Limb) smallValue).value);
            this.numAdds = 0;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setSum(IntegerModuloP integerModuloP) {
            Element element = (Element) integerModuloP;
            if (!isSummand() || !element.isSummand()) {
                throw new ArithmeticException("Not a valid summand");
            }
            for (int i2 = 0; i2 < this.limbs.length; i2++) {
                this.limbs[i2] = this.limbs[i2] + element.limbs[i2];
            }
            this.numAdds = Math.max(this.numAdds, element.numAdds) + 1;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setDifference(IntegerModuloP integerModuloP) {
            Element element = (Element) integerModuloP;
            if (!isSummand() || !element.isSummand()) {
                throw new ArithmeticException("Not a valid summand");
            }
            for (int i2 = 0; i2 < this.limbs.length; i2++) {
                this.limbs[i2] = this.limbs[i2] - element.limbs[i2];
            }
            this.numAdds = Math.max(this.numAdds, element.numAdds) + 1;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setSquare() {
            IntegerPolynomial.this.square(this.limbs, this.limbs);
            this.numAdds = 0;
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setAdditiveInverse() {
            for (int i2 = 0; i2 < this.limbs.length; i2++) {
                this.limbs[i2] = -this.limbs[i2];
            }
            return this;
        }

        @Override // sun.security.util.math.MutableIntegerModuloP
        public MutableElement setReduced() {
            IntegerPolynomial.this.reduce(this.limbs);
            this.numAdds = 0;
            return this;
        }
    }

    /* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial$ImmutableElement.class */
    class ImmutableElement extends Element implements ImmutableIntegerModuloP {
        protected ImmutableElement(BigInteger bigInteger) {
            super(bigInteger);
        }

        protected ImmutableElement(boolean z2) {
            super(z2);
        }

        protected ImmutableElement(long[] jArr, int i2) {
            super(jArr, i2);
        }

        @Override // sun.security.util.math.IntegerModuloP
        public ImmutableElement fixed() {
            return this;
        }
    }

    /* loaded from: rt.jar:sun/security/util/math/intpoly/IntegerPolynomial$Limb.class */
    class Limb implements SmallValue {
        int value;

        Limb(int i2) {
            this.value = i2;
        }
    }
}
