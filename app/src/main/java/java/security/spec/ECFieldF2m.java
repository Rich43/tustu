package java.security.spec;

import java.math.BigInteger;
import java.util.Arrays;

/* loaded from: rt.jar:java/security/spec/ECFieldF2m.class */
public class ECFieldF2m implements ECField {

    /* renamed from: m, reason: collision with root package name */
    private int f12481m;
    private int[] ks;
    private BigInteger rp;

    public ECFieldF2m(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("m is not positive");
        }
        this.f12481m = i2;
        this.ks = null;
        this.rp = null;
    }

    public ECFieldF2m(int i2, BigInteger bigInteger) {
        this.f12481m = i2;
        this.rp = bigInteger;
        if (i2 <= 0) {
            throw new IllegalArgumentException("m is not positive");
        }
        int iBitCount = this.rp.bitCount();
        if (!this.rp.testBit(0) || !this.rp.testBit(i2) || (iBitCount != 3 && iBitCount != 5)) {
            throw new IllegalArgumentException("rp does not represent a valid reduction polynomial");
        }
        BigInteger bigIntegerClearBit = this.rp.clearBit(0).clearBit(i2);
        this.ks = new int[iBitCount - 2];
        for (int length = this.ks.length - 1; length >= 0; length--) {
            int lowestSetBit = bigIntegerClearBit.getLowestSetBit();
            this.ks[length] = lowestSetBit;
            bigIntegerClearBit = bigIntegerClearBit.clearBit(lowestSetBit);
        }
    }

    public ECFieldF2m(int i2, int[] iArr) {
        this.f12481m = i2;
        this.ks = (int[]) iArr.clone();
        if (i2 <= 0) {
            throw new IllegalArgumentException("m is not positive");
        }
        if (this.ks.length != 1 && this.ks.length != 3) {
            throw new IllegalArgumentException("length of ks is neither 1 nor 3");
        }
        for (int i3 = 0; i3 < this.ks.length; i3++) {
            if (this.ks[i3] < 1 || this.ks[i3] > i2 - 1) {
                throw new IllegalArgumentException("ks[" + i3 + "] is out of range");
            }
            if (i3 != 0 && this.ks[i3] >= this.ks[i3 - 1]) {
                throw new IllegalArgumentException("values in ks are not in descending order");
            }
        }
        this.rp = BigInteger.ONE;
        this.rp = this.rp.setBit(i2);
        for (int i4 = 0; i4 < this.ks.length; i4++) {
            this.rp = this.rp.setBit(this.ks[i4]);
        }
    }

    @Override // java.security.spec.ECField
    public int getFieldSize() {
        return this.f12481m;
    }

    public int getM() {
        return this.f12481m;
    }

    public BigInteger getReductionPolynomial() {
        return this.rp;
    }

    public int[] getMidTermsOfReductionPolynomial() {
        if (this.ks == null) {
            return null;
        }
        return (int[]) this.ks.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ECFieldF2m) && this.f12481m == ((ECFieldF2m) obj).f12481m && Arrays.equals(this.ks, ((ECFieldF2m) obj).ks);
    }

    public int hashCode() {
        return (this.f12481m << 5) + (this.rp == null ? 0 : this.rp.hashCode());
    }
}
