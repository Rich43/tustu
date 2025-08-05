package java.math;

/* loaded from: rt.jar:java/math/SignedMutableBigInteger.class */
class SignedMutableBigInteger extends MutableBigInteger {
    int sign;

    SignedMutableBigInteger() {
        this.sign = 1;
    }

    SignedMutableBigInteger(int i2) {
        super(i2);
        this.sign = 1;
    }

    SignedMutableBigInteger(MutableBigInteger mutableBigInteger) {
        super(mutableBigInteger);
        this.sign = 1;
    }

    void signedAdd(SignedMutableBigInteger signedMutableBigInteger) {
        if (this.sign == signedMutableBigInteger.sign) {
            add(signedMutableBigInteger);
        } else {
            this.sign *= subtract(signedMutableBigInteger);
        }
    }

    void signedAdd(MutableBigInteger mutableBigInteger) {
        if (this.sign == 1) {
            add(mutableBigInteger);
        } else {
            this.sign *= subtract(mutableBigInteger);
        }
    }

    void signedSubtract(SignedMutableBigInteger signedMutableBigInteger) {
        if (this.sign == signedMutableBigInteger.sign) {
            this.sign *= subtract(signedMutableBigInteger);
        } else {
            add(signedMutableBigInteger);
        }
    }

    void signedSubtract(MutableBigInteger mutableBigInteger) {
        if (this.sign == 1) {
            this.sign *= subtract(mutableBigInteger);
        } else {
            add(mutableBigInteger);
        }
        if (this.intLen == 0) {
            this.sign = 1;
        }
    }

    @Override // java.math.MutableBigInteger
    public String toString() {
        return toBigInteger(this.sign).toString();
    }
}
