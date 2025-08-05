package java.math;

import java.util.Random;
import jssc.SerialPort;

/* loaded from: rt.jar:java/math/BitSieve.class */
class BitSieve {
    private long[] bits;
    private int length;
    private static BitSieve smallSieve = new BitSieve();

    private BitSieve() {
        this.length = SerialPort.BAUDRATE_9600;
        this.bits = new long[unitIndex(this.length - 1) + 1];
        set(0);
        int iSieveSearch = 1;
        int i2 = 3;
        do {
            sieveSingle(this.length, iSieveSearch + i2, i2);
            iSieveSearch = sieveSearch(this.length, iSieveSearch + 1);
            i2 = (2 * iSieveSearch) + 1;
            if (iSieveSearch <= 0) {
                return;
            }
        } while (i2 < this.length);
    }

    BitSieve(BigInteger bigInteger, int i2) {
        this.bits = new long[unitIndex(i2 - 1) + 1];
        this.length = i2;
        int iSieveSearch = smallSieve.sieveSearch(smallSieve.length, 0);
        int i3 = (iSieveSearch * 2) + 1;
        MutableBigInteger mutableBigInteger = new MutableBigInteger(bigInteger);
        MutableBigInteger mutableBigInteger2 = new MutableBigInteger();
        do {
            int iDivideOneWord = i3 - mutableBigInteger.divideOneWord(i3, mutableBigInteger2);
            sieveSingle(i2, ((iDivideOneWord % 2 == 0 ? iDivideOneWord + i3 : iDivideOneWord) - 1) / 2, i3);
            iSieveSearch = smallSieve.sieveSearch(smallSieve.length, iSieveSearch + 1);
            i3 = (iSieveSearch * 2) + 1;
        } while (iSieveSearch > 0);
    }

    private static int unitIndex(int i2) {
        return i2 >>> 6;
    }

    private static long bit(int i2) {
        return 1 << (i2 & 63);
    }

    private boolean get(int i2) {
        return (this.bits[unitIndex(i2)] & bit(i2)) != 0;
    }

    private void set(int i2) {
        int iUnitIndex = unitIndex(i2);
        long[] jArr = this.bits;
        jArr[iUnitIndex] = jArr[iUnitIndex] | bit(i2);
    }

    private int sieveSearch(int i2, int i3) {
        if (i3 >= i2) {
            return -1;
        }
        int i4 = i3;
        while (get(i4)) {
            i4++;
            if (i4 >= i2 - 1) {
                return -1;
            }
        }
        return i4;
    }

    private void sieveSingle(int i2, int i3, int i4) {
        while (i3 < i2) {
            set(i3);
            i3 += i4;
        }
    }

    BigInteger retrieve(BigInteger bigInteger, int i2, Random random) {
        int i3 = 1;
        for (int i4 = 0; i4 < this.bits.length; i4++) {
            long j2 = this.bits[i4] ^ (-1);
            for (int i5 = 0; i5 < 64; i5++) {
                if ((j2 & 1) == 1) {
                    BigInteger bigIntegerAdd = bigInteger.add(BigInteger.valueOf(i3));
                    if (bigIntegerAdd.primeToCertainty(i2, random)) {
                        return bigIntegerAdd;
                    }
                }
                j2 >>>= 1;
                i3 += 2;
            }
        }
        return null;
    }
}
