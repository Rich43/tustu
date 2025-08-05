package sun.reflect;

/* loaded from: rt.jar:sun/reflect/ByteVectorImpl.class */
class ByteVectorImpl implements ByteVector {
    private byte[] data;
    private int pos;

    public ByteVectorImpl() {
        this(100);
    }

    public ByteVectorImpl(int i2) {
        this.data = new byte[i2];
        this.pos = -1;
    }

    @Override // sun.reflect.ByteVector
    public int getLength() {
        return this.pos + 1;
    }

    @Override // sun.reflect.ByteVector
    public byte get(int i2) {
        if (i2 >= this.data.length) {
            resize(i2);
            this.pos = i2;
        }
        return this.data[i2];
    }

    @Override // sun.reflect.ByteVector
    public void put(int i2, byte b2) {
        if (i2 >= this.data.length) {
            resize(i2);
            this.pos = i2;
        }
        this.data[i2] = b2;
    }

    @Override // sun.reflect.ByteVector
    public void add(byte b2) {
        int i2 = this.pos + 1;
        this.pos = i2;
        if (i2 >= this.data.length) {
            resize(this.pos);
        }
        this.data[this.pos] = b2;
    }

    @Override // sun.reflect.ByteVector
    public void trim() {
        if (this.pos != this.data.length - 1) {
            byte[] bArr = new byte[this.pos + 1];
            System.arraycopy(this.data, 0, bArr, 0, this.pos + 1);
            this.data = bArr;
        }
    }

    @Override // sun.reflect.ByteVector
    public byte[] getData() {
        return this.data;
    }

    private void resize(int i2) {
        if (i2 <= 2 * this.data.length) {
            i2 = 2 * this.data.length;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.data, 0, bArr, 0, this.data.length);
        this.data = bArr;
    }
}
