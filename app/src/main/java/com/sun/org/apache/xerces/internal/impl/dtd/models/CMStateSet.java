package com.sun.org.apache.xerces.internal.impl.dtd.models;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/CMStateSet.class */
public class CMStateSet {
    int fBitCount;
    int fByteCount;
    int fBits1;
    int fBits2;
    byte[] fByteArray;

    public CMStateSet(int bitCount) {
        this.fBitCount = bitCount;
        if (this.fBitCount < 0) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount > 64) {
            this.fByteCount = this.fBitCount / 8;
            if (this.fBitCount % 8 != 0) {
                this.fByteCount++;
            }
            this.fByteArray = new byte[this.fByteCount];
        }
        zeroBits();
    }

    public String toString() {
        StringBuffer strRet = new StringBuffer();
        try {
            strRet.append(VectorFormat.DEFAULT_PREFIX);
            for (int index = 0; index < this.fBitCount; index++) {
                if (getBit(index)) {
                    strRet.append(" " + index);
                }
            }
            strRet.append(" }");
        } catch (RuntimeException e2) {
        }
        return strRet.toString();
    }

    public final void intersection(CMStateSet setToAnd) {
        if (this.fBitCount < 65) {
            this.fBits1 &= setToAnd.fBits1;
            this.fBits2 &= setToAnd.fBits2;
            return;
        }
        for (int index = this.fByteCount - 1; index >= 0; index--) {
            byte[] bArr = this.fByteArray;
            int i2 = index;
            bArr[i2] = (byte) (bArr[i2] & setToAnd.fByteArray[index]);
        }
    }

    public final boolean getBit(int bitToGet) {
        if (bitToGet >= this.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            int mask = 1 << (bitToGet % 32);
            return bitToGet < 32 ? (this.fBits1 & mask) != 0 : (this.fBits2 & mask) != 0;
        }
        int ofs = bitToGet >> 3;
        return (this.fByteArray[ofs] & ((byte) (1 << (bitToGet % 8)))) != 0;
    }

    public final boolean isEmpty() {
        if (this.fBitCount < 65) {
            return this.fBits1 == 0 && this.fBits2 == 0;
        }
        for (int index = this.fByteCount - 1; index >= 0; index--) {
            if (this.fByteArray[index] != 0) {
                return false;
            }
        }
        return true;
    }

    final boolean isSameSet(CMStateSet setToCompare) {
        if (this.fBitCount != setToCompare.fBitCount) {
            return false;
        }
        if (this.fBitCount < 65) {
            return this.fBits1 == setToCompare.fBits1 && this.fBits2 == setToCompare.fBits2;
        }
        for (int index = this.fByteCount - 1; index >= 0; index--) {
            if (this.fByteArray[index] != setToCompare.fByteArray[index]) {
                return false;
            }
        }
        return true;
    }

    public final void union(CMStateSet setToOr) {
        if (this.fBitCount < 65) {
            this.fBits1 |= setToOr.fBits1;
            this.fBits2 |= setToOr.fBits2;
            return;
        }
        for (int index = this.fByteCount - 1; index >= 0; index--) {
            byte[] bArr = this.fByteArray;
            int i2 = index;
            bArr[i2] = (byte) (bArr[i2] | setToOr.fByteArray[index]);
        }
    }

    public final void setBit(int bitToSet) {
        if (bitToSet >= this.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            int mask = 1 << (bitToSet % 32);
            if (bitToSet < 32) {
                this.fBits1 &= mask ^ (-1);
                this.fBits1 |= mask;
                return;
            } else {
                this.fBits2 &= mask ^ (-1);
                this.fBits2 |= mask;
                return;
            }
        }
        byte mask2 = (byte) (1 << (bitToSet % 8));
        int ofs = bitToSet >> 3;
        byte[] bArr = this.fByteArray;
        bArr[ofs] = (byte) (bArr[ofs] & (mask2 ^ (-1)));
        byte[] bArr2 = this.fByteArray;
        bArr2[ofs] = (byte) (bArr2[ofs] | mask2);
    }

    public final void setTo(CMStateSet srcSet) {
        if (this.fBitCount != srcSet.fBitCount) {
            throw new RuntimeException("ImplementationMessages.VAL_CMSI");
        }
        if (this.fBitCount < 65) {
            this.fBits1 = srcSet.fBits1;
            this.fBits2 = srcSet.fBits2;
        } else {
            for (int index = this.fByteCount - 1; index >= 0; index--) {
                this.fByteArray[index] = srcSet.fByteArray[index];
            }
        }
    }

    public final void zeroBits() {
        if (this.fBitCount < 65) {
            this.fBits1 = 0;
            this.fBits2 = 0;
        } else {
            for (int index = this.fByteCount - 1; index >= 0; index--) {
                this.fByteArray[index] = 0;
            }
        }
    }

    public boolean equals(Object o2) {
        if (o2 instanceof CMStateSet) {
            return isSameSet((CMStateSet) o2);
        }
        return false;
    }

    public int hashCode() {
        if (this.fBitCount < 65) {
            return this.fBits1 + (this.fBits2 * 31);
        }
        int hash = 0;
        for (int index = this.fByteCount - 1; index >= 0; index--) {
            hash = this.fByteArray[index] + (hash * 31);
        }
        return hash;
    }
}
