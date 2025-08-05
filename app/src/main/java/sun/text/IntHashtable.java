package sun.text;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.net.SocketOptions;

/* loaded from: rt.jar:sun/text/IntHashtable.class */
public final class IntHashtable {
    private int defaultValue = 0;
    private int primeIndex;
    private static final float HIGH_WATER_FACTOR = 0.4f;
    private int highWaterMark;
    private static final float LOW_WATER_FACTOR = 0.0f;
    private int lowWaterMark;
    private int count;
    private int[] values;
    private int[] keyList;
    private static final int EMPTY = Integer.MIN_VALUE;
    private static final int DELETED = -2147483647;
    private static final int MAX_UNUSED = -2147483647;
    private static final int[] PRIMES = {17, 37, 67, 131, 257, 521, OpCodes.NODETYPE_TEXT, 2053, SocketOptions.SO_OOBINLINE, 8209, 16411, 32771, OSFCodeSetRegistry.ISO_8859_1_VALUE, 131101, 262147, 524309, 1048583, 2097169, 4194319, 8388617, 16777259, 33554467, 67108879, 134217757, 268435459, 536870923, 1073741827, Integer.MAX_VALUE};

    public IntHashtable() {
        initialize(3);
    }

    public IntHashtable(int i2) {
        initialize(leastGreaterPrimeIndex((int) (i2 / 0.4f)));
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public void put(int i2, int i3) {
        if (this.count > this.highWaterMark) {
            rehash();
        }
        int iFind = find(i2);
        if (this.keyList[iFind] <= -2147483647) {
            this.keyList[iFind] = i2;
            this.count++;
        }
        this.values[iFind] = i3;
    }

    public int get(int i2) {
        return this.values[find(i2)];
    }

    public void remove(int i2) {
        int iFind = find(i2);
        if (this.keyList[iFind] > -2147483647) {
            this.keyList[iFind] = -2147483647;
            this.values[iFind] = this.defaultValue;
            this.count--;
            if (this.count < this.lowWaterMark) {
                rehash();
            }
        }
    }

    public int getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(int i2) {
        this.defaultValue = i2;
        rehash();
    }

    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        IntHashtable intHashtable = (IntHashtable) obj;
        if (intHashtable.size() != this.count || intHashtable.defaultValue != this.defaultValue) {
            return false;
        }
        for (int i2 = 0; i2 < this.keyList.length; i2++) {
            int i3 = this.keyList[i2];
            if (i3 > -2147483647 && intHashtable.get(i3) != this.values[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i2 = 465;
        for (int i3 = 0; i3 < this.keyList.length; i3++) {
            i2 = (i2 * 1362796821) + 1 + this.keyList[i3];
        }
        for (int i4 = 0; i4 < this.values.length; i4++) {
            i2 = (i2 * 1362796821) + 1 + this.values[i4];
        }
        return i2;
    }

    public Object clone() throws CloneNotSupportedException {
        IntHashtable intHashtable = (IntHashtable) super.clone();
        this.values = (int[]) this.values.clone();
        this.keyList = (int[]) this.keyList.clone();
        return intHashtable;
    }

    private void initialize(int i2) {
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 >= PRIMES.length) {
            System.out.println("TOO BIG");
            i2 = PRIMES.length - 1;
        }
        this.primeIndex = i2;
        int i3 = PRIMES[i2];
        this.values = new int[i3];
        this.keyList = new int[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            this.keyList[i4] = Integer.MIN_VALUE;
            this.values[i4] = this.defaultValue;
        }
        this.count = 0;
        this.lowWaterMark = (int) (i3 * 0.0f);
        this.highWaterMark = (int) (i3 * 0.4f);
    }

    private void rehash() {
        int[] iArr = this.values;
        int[] iArr2 = this.keyList;
        int i2 = this.primeIndex;
        if (this.count > this.highWaterMark) {
            i2++;
        } else if (this.count < this.lowWaterMark) {
            i2 -= 2;
        }
        initialize(i2);
        for (int length = iArr.length - 1; length >= 0; length--) {
            int i3 = iArr2[length];
            if (i3 > -2147483647) {
                putInternal(i3, iArr[length]);
            }
        }
    }

    public void putInternal(int i2, int i3) {
        int iFind = find(i2);
        if (this.keyList[iFind] < -2147483647) {
            this.keyList[iFind] = i2;
            this.count++;
        }
        this.values[iFind] = i3;
    }

    private int find(int i2) {
        if (i2 <= -2147483647) {
            throw new IllegalArgumentException("key can't be less than 0xFFFFFFFE");
        }
        int i3 = -1;
        int length = (i2 ^ 67108864) % this.keyList.length;
        if (length < 0) {
            length = -length;
        }
        int i4 = 0;
        do {
            int i5 = this.keyList[length];
            if (i5 == i2) {
                return length;
            }
            if (i5 <= -2147483647) {
                if (i5 == Integer.MIN_VALUE) {
                    if (i3 >= 0) {
                        length = i3;
                    }
                    return length;
                }
                if (i3 < 0) {
                    i3 = length;
                }
            }
            if (i4 == 0) {
                int length2 = i2 % (this.keyList.length - 1);
                if (length2 < 0) {
                    length2 = -length2;
                }
                i4 = length2 + 1;
            }
            length = (length + i4) % this.keyList.length;
        } while (length != i3);
        return length;
    }

    private static int leastGreaterPrimeIndex(int i2) {
        int i3 = 0;
        while (i3 < PRIMES.length && i2 >= PRIMES[i3]) {
            i3++;
        }
        if (i3 == 0) {
            return 0;
        }
        return i3 - 1;
    }
}
