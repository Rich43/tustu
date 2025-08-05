package java.util;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/* loaded from: rt.jar:java/util/BitSet.class */
public class BitSet implements Cloneable, Serializable {
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 64;
    private static final int BIT_INDEX_MASK = 63;
    private static final long WORD_MASK = -1;
    private static final ObjectStreamField[] serialPersistentFields;
    private long[] words;
    private transient int wordsInUse;
    private transient boolean sizeIsSticky;
    private static final long serialVersionUID = 7997698588986878753L;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BitSet.class.desiredAssertionStatus();
        serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField(ControllerParameter.PARAM_CLASS_BITS, long[].class)};
    }

    private static int wordIndex(int i2) {
        return i2 >> 6;
    }

    private void checkInvariants() {
        if (!$assertionsDisabled && this.wordsInUse != 0 && this.words[this.wordsInUse - 1] == 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (this.wordsInUse < 0 || this.wordsInUse > this.words.length)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.wordsInUse != this.words.length && this.words[this.wordsInUse] != 0) {
            throw new AssertionError();
        }
    }

    private void recalculateWordsInUse() {
        int i2 = this.wordsInUse - 1;
        while (i2 >= 0 && this.words[i2] == 0) {
            i2--;
        }
        this.wordsInUse = i2 + 1;
    }

    public BitSet() {
        this.wordsInUse = 0;
        this.sizeIsSticky = false;
        initWords(64);
        this.sizeIsSticky = false;
    }

    public BitSet(int i2) {
        this.wordsInUse = 0;
        this.sizeIsSticky = false;
        if (i2 < 0) {
            throw new NegativeArraySizeException("nbits < 0: " + i2);
        }
        initWords(i2);
        this.sizeIsSticky = true;
    }

    private void initWords(int i2) {
        this.words = new long[wordIndex(i2 - 1) + 1];
    }

    private BitSet(long[] jArr) {
        this.wordsInUse = 0;
        this.sizeIsSticky = false;
        this.words = jArr;
        this.wordsInUse = jArr.length;
        checkInvariants();
    }

    public static BitSet valueOf(long[] jArr) {
        int length = jArr.length;
        while (length > 0 && jArr[length - 1] == 0) {
            length--;
        }
        return new BitSet(Arrays.copyOf(jArr, length));
    }

    public static BitSet valueOf(LongBuffer longBuffer) {
        LongBuffer longBufferSlice = longBuffer.slice();
        int iRemaining = longBufferSlice.remaining();
        while (iRemaining > 0 && longBufferSlice.get(iRemaining - 1) == 0) {
            iRemaining--;
        }
        long[] jArr = new long[iRemaining];
        longBufferSlice.get(jArr);
        return new BitSet(jArr);
    }

    public static BitSet valueOf(byte[] bArr) {
        return valueOf(ByteBuffer.wrap(bArr));
    }

    public static BitSet valueOf(ByteBuffer byteBuffer) {
        ByteBuffer byteBufferOrder = byteBuffer.slice().order(ByteOrder.LITTLE_ENDIAN);
        int iRemaining = byteBufferOrder.remaining();
        while (iRemaining > 0 && byteBufferOrder.get(iRemaining - 1) == 0) {
            iRemaining--;
        }
        long[] jArr = new long[(iRemaining + 7) / 8];
        byteBufferOrder.limit(iRemaining);
        int i2 = 0;
        while (byteBufferOrder.remaining() >= 8) {
            int i3 = i2;
            i2++;
            jArr[i3] = byteBufferOrder.getLong();
        }
        int iRemaining2 = byteBufferOrder.remaining();
        for (int i4 = 0; i4 < iRemaining2; i4++) {
            int i5 = i2;
            jArr[i5] = jArr[i5] | ((byteBufferOrder.get() & 255) << (8 * i4));
        }
        return new BitSet(jArr);
    }

    public byte[] toByteArray() {
        int i2 = this.wordsInUse;
        if (i2 == 0) {
            return new byte[0];
        }
        int i3 = 8 * (i2 - 1);
        long j2 = this.words[i2 - 1];
        while (true) {
            long j3 = j2;
            if (j3 == 0) {
                break;
            }
            i3++;
            j2 = j3 >>> 8;
        }
        byte[] bArr = new byte[i3];
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
        for (int i4 = 0; i4 < i2 - 1; i4++) {
            byteBufferOrder.putLong(this.words[i4]);
        }
        long j4 = this.words[i2 - 1];
        while (true) {
            long j5 = j4;
            if (j5 != 0) {
                byteBufferOrder.put((byte) (j5 & 255));
                j4 = j5 >>> 8;
            } else {
                return bArr;
            }
        }
    }

    public long[] toLongArray() {
        return Arrays.copyOf(this.words, this.wordsInUse);
    }

    private void ensureCapacity(int i2) {
        if (this.words.length < i2) {
            this.words = Arrays.copyOf(this.words, Math.max(2 * this.words.length, i2));
            this.sizeIsSticky = false;
        }
    }

    private void expandTo(int i2) {
        int i3 = i2 + 1;
        if (this.wordsInUse < i3) {
            ensureCapacity(i3);
            this.wordsInUse = i3;
        }
    }

    private static void checkRange(int i2, int i3) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i2);
        }
        if (i3 < 0) {
            throw new IndexOutOfBoundsException("toIndex < 0: " + i3);
        }
        if (i2 > i3) {
            throw new IndexOutOfBoundsException("fromIndex: " + i2 + " > toIndex: " + i3);
        }
    }

    public void flip(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i2);
        }
        int iWordIndex = wordIndex(i2);
        expandTo(iWordIndex);
        long[] jArr = this.words;
        jArr[iWordIndex] = jArr[iWordIndex] ^ (1 << i2);
        recalculateWordsInUse();
        checkInvariants();
    }

    public void flip(int i2, int i3) {
        checkRange(i2, i3);
        if (i2 == i3) {
            return;
        }
        int iWordIndex = wordIndex(i2);
        int iWordIndex2 = wordIndex(i3 - 1);
        expandTo(iWordIndex2);
        long j2 = (-1) << i2;
        long j3 = (-1) >>> (-i3);
        if (iWordIndex == iWordIndex2) {
            long[] jArr = this.words;
            jArr[iWordIndex] = jArr[iWordIndex] ^ (j2 & j3);
        } else {
            long[] jArr2 = this.words;
            jArr2[iWordIndex] = jArr2[iWordIndex] ^ j2;
            for (int i4 = iWordIndex + 1; i4 < iWordIndex2; i4++) {
                long[] jArr3 = this.words;
                int i5 = i4;
                jArr3[i5] = jArr3[i5] ^ (-1);
            }
            long[] jArr4 = this.words;
            jArr4[iWordIndex2] = jArr4[iWordIndex2] ^ j3;
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void set(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i2);
        }
        int iWordIndex = wordIndex(i2);
        expandTo(iWordIndex);
        long[] jArr = this.words;
        jArr[iWordIndex] = jArr[iWordIndex] | (1 << i2);
        checkInvariants();
    }

    public void set(int i2, boolean z2) {
        if (z2) {
            set(i2);
        } else {
            clear(i2);
        }
    }

    public void set(int i2, int i3) {
        checkRange(i2, i3);
        if (i2 == i3) {
            return;
        }
        int iWordIndex = wordIndex(i2);
        int iWordIndex2 = wordIndex(i3 - 1);
        expandTo(iWordIndex2);
        long j2 = (-1) << i2;
        long j3 = (-1) >>> (-i3);
        if (iWordIndex == iWordIndex2) {
            long[] jArr = this.words;
            jArr[iWordIndex] = jArr[iWordIndex] | (j2 & j3);
        } else {
            long[] jArr2 = this.words;
            jArr2[iWordIndex] = jArr2[iWordIndex] | j2;
            for (int i4 = iWordIndex + 1; i4 < iWordIndex2; i4++) {
                this.words[i4] = -1;
            }
            long[] jArr3 = this.words;
            jArr3[iWordIndex2] = jArr3[iWordIndex2] | j3;
        }
        checkInvariants();
    }

    public void set(int i2, int i3, boolean z2) {
        if (z2) {
            set(i2, i3);
        } else {
            clear(i2, i3);
        }
    }

    public void clear(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i2);
        }
        int iWordIndex = wordIndex(i2);
        if (iWordIndex >= this.wordsInUse) {
            return;
        }
        long[] jArr = this.words;
        jArr[iWordIndex] = jArr[iWordIndex] & ((1 << i2) ^ (-1));
        recalculateWordsInUse();
        checkInvariants();
    }

    public void clear(int i2, int i3) {
        int iWordIndex;
        checkRange(i2, i3);
        if (i2 == i3 || (iWordIndex = wordIndex(i2)) >= this.wordsInUse) {
            return;
        }
        int iWordIndex2 = wordIndex(i3 - 1);
        if (iWordIndex2 >= this.wordsInUse) {
            i3 = length();
            iWordIndex2 = this.wordsInUse - 1;
        }
        long j2 = (-1) << i2;
        long j3 = (-1) >>> (-i3);
        if (iWordIndex == iWordIndex2) {
            long[] jArr = this.words;
            jArr[iWordIndex] = jArr[iWordIndex] & ((j2 & j3) ^ (-1));
        } else {
            long[] jArr2 = this.words;
            jArr2[iWordIndex] = jArr2[iWordIndex] & (j2 ^ (-1));
            for (int i4 = iWordIndex + 1; i4 < iWordIndex2; i4++) {
                this.words[i4] = 0;
            }
            long[] jArr3 = this.words;
            int i5 = iWordIndex2;
            jArr3[i5] = jArr3[i5] & (j3 ^ (-1));
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void clear() {
        while (this.wordsInUse > 0) {
            long[] jArr = this.words;
            int i2 = this.wordsInUse - 1;
            this.wordsInUse = i2;
            jArr[i2] = 0;
        }
    }

    public boolean get(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i2);
        }
        checkInvariants();
        int iWordIndex = wordIndex(i2);
        return iWordIndex < this.wordsInUse && (this.words[iWordIndex] & (1 << i2)) != 0;
    }

    public BitSet get(int i2, int i3) {
        checkRange(i2, i3);
        checkInvariants();
        int length = length();
        if (length <= i2 || i2 == i3) {
            return new BitSet(0);
        }
        if (i3 > length) {
            i3 = length;
        }
        BitSet bitSet = new BitSet(i3 - i2);
        int iWordIndex = wordIndex((i3 - i2) - 1) + 1;
        int iWordIndex2 = wordIndex(i2);
        boolean z2 = (i2 & 63) == 0;
        int i4 = 0;
        while (i4 < iWordIndex - 1) {
            bitSet.words[i4] = z2 ? this.words[iWordIndex2] : (this.words[iWordIndex2] >>> i2) | (this.words[iWordIndex2 + 1] << (-i2));
            i4++;
            iWordIndex2++;
        }
        long j2 = (-1) >>> (-i3);
        bitSet.words[iWordIndex - 1] = ((i3 - 1) & 63) < (i2 & 63) ? (this.words[iWordIndex2] >>> i2) | ((this.words[iWordIndex2 + 1] & j2) << (-i2)) : (this.words[iWordIndex2] & j2) >>> i2;
        bitSet.wordsInUse = iWordIndex;
        bitSet.recalculateWordsInUse();
        bitSet.checkInvariants();
        return bitSet;
    }

    public int nextSetBit(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i2);
        }
        checkInvariants();
        int iWordIndex = wordIndex(i2);
        if (iWordIndex >= this.wordsInUse) {
            return -1;
        }
        long j2 = this.words[iWordIndex] & ((-1) << i2);
        while (true) {
            long j3 = j2;
            if (j3 != 0) {
                return (iWordIndex * 64) + Long.numberOfTrailingZeros(j3);
            }
            iWordIndex++;
            if (iWordIndex == this.wordsInUse) {
                return -1;
            }
            j2 = this.words[iWordIndex];
        }
    }

    public int nextClearBit(int i2) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i2);
        }
        checkInvariants();
        int iWordIndex = wordIndex(i2);
        if (iWordIndex >= this.wordsInUse) {
            return i2;
        }
        long j2 = (this.words[iWordIndex] ^ (-1)) & ((-1) << i2);
        while (true) {
            long j3 = j2;
            if (j3 != 0) {
                return (iWordIndex * 64) + Long.numberOfTrailingZeros(j3);
            }
            iWordIndex++;
            if (iWordIndex == this.wordsInUse) {
                return this.wordsInUse * 64;
            }
            j2 = this.words[iWordIndex] ^ (-1);
        }
    }

    public int previousSetBit(int i2) {
        if (i2 < 0) {
            if (i2 == -1) {
                return -1;
            }
            throw new IndexOutOfBoundsException("fromIndex < -1: " + i2);
        }
        checkInvariants();
        int iWordIndex = wordIndex(i2);
        if (iWordIndex >= this.wordsInUse) {
            return length() - 1;
        }
        long j2 = this.words[iWordIndex] & ((-1) >>> (-(i2 + 1)));
        while (true) {
            long j3 = j2;
            if (j3 != 0) {
                return (((iWordIndex + 1) * 64) - 1) - Long.numberOfLeadingZeros(j3);
            }
            int i3 = iWordIndex;
            iWordIndex--;
            if (i3 == 0) {
                return -1;
            }
            j2 = this.words[iWordIndex];
        }
    }

    public int previousClearBit(int i2) {
        if (i2 < 0) {
            if (i2 == -1) {
                return -1;
            }
            throw new IndexOutOfBoundsException("fromIndex < -1: " + i2);
        }
        checkInvariants();
        int iWordIndex = wordIndex(i2);
        if (iWordIndex >= this.wordsInUse) {
            return i2;
        }
        long j2 = (this.words[iWordIndex] ^ (-1)) & ((-1) >>> (-(i2 + 1)));
        while (true) {
            long j3 = j2;
            if (j3 != 0) {
                return (((iWordIndex + 1) * 64) - 1) - Long.numberOfLeadingZeros(j3);
            }
            int i3 = iWordIndex;
            iWordIndex--;
            if (i3 == 0) {
                return -1;
            }
            j2 = this.words[iWordIndex] ^ (-1);
        }
    }

    public int length() {
        if (this.wordsInUse == 0) {
            return 0;
        }
        return (64 * (this.wordsInUse - 1)) + (64 - Long.numberOfLeadingZeros(this.words[this.wordsInUse - 1]));
    }

    public boolean isEmpty() {
        return this.wordsInUse == 0;
    }

    public boolean intersects(BitSet bitSet) {
        for (int iMin = Math.min(this.wordsInUse, bitSet.wordsInUse) - 1; iMin >= 0; iMin--) {
            if ((this.words[iMin] & bitSet.words[iMin]) != 0) {
                return true;
            }
        }
        return false;
    }

    public int cardinality() {
        int iBitCount = 0;
        for (int i2 = 0; i2 < this.wordsInUse; i2++) {
            iBitCount += Long.bitCount(this.words[i2]);
        }
        return iBitCount;
    }

    public void and(BitSet bitSet) {
        if (this == bitSet) {
            return;
        }
        while (this.wordsInUse > bitSet.wordsInUse) {
            long[] jArr = this.words;
            int i2 = this.wordsInUse - 1;
            this.wordsInUse = i2;
            jArr[i2] = 0;
        }
        for (int i3 = 0; i3 < this.wordsInUse; i3++) {
            long[] jArr2 = this.words;
            int i4 = i3;
            jArr2[i4] = jArr2[i4] & bitSet.words[i3];
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void or(BitSet bitSet) {
        if (this == bitSet) {
            return;
        }
        int iMin = Math.min(this.wordsInUse, bitSet.wordsInUse);
        if (this.wordsInUse < bitSet.wordsInUse) {
            ensureCapacity(bitSet.wordsInUse);
            this.wordsInUse = bitSet.wordsInUse;
        }
        for (int i2 = 0; i2 < iMin; i2++) {
            long[] jArr = this.words;
            int i3 = i2;
            jArr[i3] = jArr[i3] | bitSet.words[i2];
        }
        if (iMin < bitSet.wordsInUse) {
            System.arraycopy(bitSet.words, iMin, this.words, iMin, this.wordsInUse - iMin);
        }
        checkInvariants();
    }

    public void xor(BitSet bitSet) {
        int iMin = Math.min(this.wordsInUse, bitSet.wordsInUse);
        if (this.wordsInUse < bitSet.wordsInUse) {
            ensureCapacity(bitSet.wordsInUse);
            this.wordsInUse = bitSet.wordsInUse;
        }
        for (int i2 = 0; i2 < iMin; i2++) {
            long[] jArr = this.words;
            int i3 = i2;
            jArr[i3] = jArr[i3] ^ bitSet.words[i2];
        }
        if (iMin < bitSet.wordsInUse) {
            System.arraycopy(bitSet.words, iMin, this.words, iMin, bitSet.wordsInUse - iMin);
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void andNot(BitSet bitSet) {
        for (int iMin = Math.min(this.wordsInUse, bitSet.wordsInUse) - 1; iMin >= 0; iMin--) {
            long[] jArr = this.words;
            int i2 = iMin;
            jArr[i2] = jArr[i2] & (bitSet.words[iMin] ^ (-1));
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public int hashCode() {
        long j2 = 1234;
        int i2 = this.wordsInUse;
        while (true) {
            i2--;
            if (i2 >= 0) {
                j2 ^= this.words[i2] * (i2 + 1);
            } else {
                return (int) ((j2 >> 32) ^ j2);
            }
        }
    }

    public int size() {
        return this.words.length * 64;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BitSet)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        BitSet bitSet = (BitSet) obj;
        checkInvariants();
        bitSet.checkInvariants();
        if (this.wordsInUse != bitSet.wordsInUse) {
            return false;
        }
        for (int i2 = 0; i2 < this.wordsInUse; i2++) {
            if (this.words[i2] != bitSet.words[i2]) {
                return false;
            }
        }
        return true;
    }

    public Object clone() {
        if (!this.sizeIsSticky) {
            trimToSize();
        }
        try {
            BitSet bitSet = (BitSet) super.clone();
            bitSet.words = (long[]) this.words.clone();
            bitSet.checkInvariants();
            return bitSet;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private void trimToSize() {
        if (this.wordsInUse != this.words.length) {
            this.words = Arrays.copyOf(this.words, this.wordsInUse);
            checkInvariants();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        checkInvariants();
        if (!this.sizeIsSticky) {
            trimToSize();
        }
        objectOutputStream.putFields().put(ControllerParameter.PARAM_CLASS_BITS, this.words);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.words = (long[]) objectInputStream.readFields().get(ControllerParameter.PARAM_CLASS_BITS, (Object) null);
        this.wordsInUse = this.words.length;
        recalculateWordsInUse();
        this.sizeIsSticky = this.words.length > 0 && this.words[this.words.length - 1] == 0;
        checkInvariants();
    }

    public String toString() {
        checkInvariants();
        StringBuilder sb = new StringBuilder((6 * (this.wordsInUse > 128 ? cardinality() : this.wordsInUse * 64)) + 2);
        sb.append('{');
        int iNextSetBit = nextSetBit(0);
        if (iNextSetBit != -1) {
            sb.append(iNextSetBit);
            while (true) {
                int i2 = iNextSetBit + 1;
                if (i2 < 0) {
                    break;
                }
                int iNextSetBit2 = nextSetBit(i2);
                iNextSetBit = iNextSetBit2;
                if (iNextSetBit2 < 0) {
                    break;
                }
                int iNextClearBit = nextClearBit(iNextSetBit);
                do {
                    sb.append(", ").append(iNextSetBit);
                    iNextSetBit++;
                } while (iNextSetBit != iNextClearBit);
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public IntStream stream() {
        return StreamSupport.intStream(() -> {
            return Spliterators.spliterator(new PrimitiveIterator.OfInt() { // from class: java.util.BitSet.1BitSetIterator
                int next;

                {
                    this.next = BitSet.this.nextSetBit(0);
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.next != -1;
                }

                @Override // java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    if (this.next != -1) {
                        int i2 = this.next;
                        this.next = BitSet.this.nextSetBit(this.next + 1);
                        return i2;
                    }
                    throw new NoSuchElementException();
                }
            }, cardinality(), 21);
        }, 16469, false);
    }
}
