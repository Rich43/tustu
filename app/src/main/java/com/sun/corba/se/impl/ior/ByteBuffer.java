package com.sun.corba.se.impl.ior;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ByteBuffer.class */
public class ByteBuffer {
    protected byte[] elementData;
    protected int elementCount;
    protected int capacityIncrement;

    public ByteBuffer(int i2, int i3) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
        this.elementData = new byte[i2];
        this.capacityIncrement = i3;
    }

    public ByteBuffer(int i2) {
        this(i2, 0);
    }

    public ByteBuffer() {
        this(200);
    }

    public void trimToSize() {
        if (this.elementCount < this.elementData.length) {
            byte[] bArr = this.elementData;
            this.elementData = new byte[this.elementCount];
            System.arraycopy(bArr, 0, this.elementData, 0, this.elementCount);
        }
    }

    private void ensureCapacityHelper(int i2) {
        int length = this.elementData.length;
        if (i2 > length) {
            byte[] bArr = this.elementData;
            int i3 = this.capacityIncrement > 0 ? length + this.capacityIncrement : length * 2;
            if (i3 < i2) {
                i3 = i2;
            }
            this.elementData = new byte[i3];
            System.arraycopy(bArr, 0, this.elementData, 0, this.elementCount);
        }
    }

    public int capacity() {
        return this.elementData.length;
    }

    public int size() {
        return this.elementCount;
    }

    public boolean isEmpty() {
        return this.elementCount == 0;
    }

    public void append(byte b2) {
        ensureCapacityHelper(this.elementCount + 1);
        byte[] bArr = this.elementData;
        int i2 = this.elementCount;
        this.elementCount = i2 + 1;
        bArr[i2] = b2;
    }

    public void append(int i2) {
        ensureCapacityHelper(this.elementCount + 4);
        doAppend(i2);
    }

    private void doAppend(int i2) {
        int i3 = i2;
        for (int i4 = 0; i4 < 4; i4++) {
            this.elementData[this.elementCount + i4] = (byte) (i3 & 255);
            i3 >>= 8;
        }
        this.elementCount += 4;
    }

    public void append(String str) {
        byte[] bytes = str.getBytes();
        ensureCapacityHelper(this.elementCount + bytes.length + 4);
        doAppend(bytes.length);
        System.arraycopy(bytes, 0, this.elementData, this.elementCount, bytes.length);
        this.elementCount += bytes.length;
    }

    public byte[] toArray() {
        return this.elementData;
    }
}
