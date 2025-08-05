package com.sun.org.apache.xalan.internal.xsltc.dom;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/BitArray.class */
public class BitArray implements Externalizable {
    static final long serialVersionUID = -4876019880708377663L;
    private int[] _bits;
    private int _bitSize;
    private int _intSize;
    private int _mask;
    private static final int[] _masks = {Integer.MIN_VALUE, 1073741824, 536870912, 268435456, 134217728, 67108864, 33554432, 16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    private static final boolean DEBUG_ASSERTIONS = false;
    private int _pos;
    private int _node;
    private int _int;
    private int _bit;
    int _first;
    int _last;

    public BitArray() {
        this(32);
    }

    public BitArray(int size) {
        this._pos = Integer.MAX_VALUE;
        this._node = 0;
        this._int = 0;
        this._bit = 0;
        this._first = Integer.MAX_VALUE;
        this._last = Integer.MIN_VALUE;
        this._bitSize = size < 32 ? 32 : size;
        this._intSize = (this._bitSize >>> 5) + 1;
        this._bits = new int[this._intSize + 1];
    }

    public BitArray(int size, int[] bits) {
        this._pos = Integer.MAX_VALUE;
        this._node = 0;
        this._int = 0;
        this._bit = 0;
        this._first = Integer.MAX_VALUE;
        this._last = Integer.MIN_VALUE;
        this._bitSize = size < 32 ? 32 : size;
        this._intSize = (this._bitSize >>> 5) + 1;
        this._bits = bits;
    }

    public void setMask(int mask) {
        this._mask = mask;
    }

    public int getMask() {
        return this._mask;
    }

    public final int size() {
        return this._bitSize;
    }

    public final boolean getBit(int bit) {
        return (this._bits[bit >>> 5] & _masks[bit % 32]) != 0;
    }

    public final int getNextBit(int startBit) {
        for (int i2 = startBit >>> 5; i2 <= this._intSize; i2++) {
            int bits = this._bits[i2];
            if (bits != 0) {
                for (int b2 = startBit % 32; b2 < 32; b2++) {
                    if ((bits & _masks[b2]) != 0) {
                        return (i2 << 5) + b2;
                    }
                }
            }
            startBit = 0;
        }
        return -1;
    }

    public final int getBitNumber(int pos) {
        if (pos == this._pos) {
            return this._node;
        }
        if (pos < this._pos) {
            this._pos = 0;
            this._bit = 0;
            this._int = 0;
        }
        while (this._int <= this._intSize) {
            int bits = this._bits[this._int];
            if (bits != 0) {
                while (this._bit < 32) {
                    if ((bits & _masks[this._bit]) != 0) {
                        int i2 = this._pos + 1;
                        this._pos = i2;
                        if (i2 == pos) {
                            this._node = ((this._int << 5) + this._bit) - 1;
                            return this._node;
                        }
                    }
                    this._bit++;
                }
                this._bit = 0;
            }
            this._int++;
        }
        return 0;
    }

    public final int[] data() {
        return this._bits;
    }

    public final void setBit(int bit) {
        if (bit >= this._bitSize) {
            return;
        }
        int i2 = bit >>> 5;
        if (i2 < this._first) {
            this._first = i2;
        }
        if (i2 > this._last) {
            this._last = i2;
        }
        int[] iArr = this._bits;
        iArr[i2] = iArr[i2] | _masks[bit % 32];
    }

    public final BitArray merge(BitArray other) {
        if (this._last == -1) {
            this._bits = other._bits;
        } else if (other._last != -1) {
            int start = this._first < other._first ? this._first : other._first;
            int stop = this._last > other._last ? this._last : other._last;
            if (other._intSize > this._intSize) {
                if (stop > this._intSize) {
                    stop = this._intSize;
                }
                for (int i2 = start; i2 <= stop; i2++) {
                    int[] iArr = other._bits;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] | this._bits[i2];
                }
                this._bits = other._bits;
            } else {
                if (stop > other._intSize) {
                    stop = other._intSize;
                }
                for (int i4 = start; i4 <= stop; i4++) {
                    int[] iArr2 = this._bits;
                    int i5 = i4;
                    iArr2[i5] = iArr2[i5] | other._bits[i4];
                }
            }
        }
        return this;
    }

    public final void resize(int newSize) {
        if (newSize > this._bitSize) {
            this._intSize = (newSize >>> 5) + 1;
            int[] newBits = new int[this._intSize + 1];
            System.arraycopy(this._bits, 0, newBits, 0, (this._bitSize >>> 5) + 1);
            this._bits = newBits;
            this._bitSize = newSize;
        }
    }

    public BitArray cloneArray() {
        return new BitArray(this._intSize, this._bits);
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this._bitSize);
        out.writeInt(this._mask);
        out.writeObject(this._bits);
        out.flush();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this._bitSize = in.readInt();
        this._intSize = (this._bitSize >>> 5) + 1;
        this._mask = in.readInt();
        this._bits = (int[]) in.readObject();
    }
}
