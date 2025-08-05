package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/SuballocatedByteVector.class */
public class SuballocatedByteVector {
    protected int m_blocksize;
    protected int m_numblocks;
    protected byte[][] m_map;
    protected int m_firstFree;
    protected byte[] m_map0;

    public SuballocatedByteVector() {
        this(2048);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [byte[], byte[][]] */
    public SuballocatedByteVector(int blocksize) {
        this.m_numblocks = 32;
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_map0 = new byte[blocksize];
        this.m_map = new byte[this.m_numblocks];
        this.m_map[0] = this.m_map0;
    }

    public SuballocatedByteVector(int blocksize, int increaseSize) {
        this(blocksize);
    }

    public int size() {
        return this.m_firstFree;
    }

    private void setSize(int sz) {
        if (this.m_firstFree < sz) {
            this.m_firstFree = sz;
        }
    }

    /* JADX WARN: Type inference failed for: r0v21, types: [byte[], byte[][], java.lang.Object] */
    public void addElement(byte value) {
        if (this.m_firstFree < this.m_blocksize) {
            byte[] bArr = this.m_map0;
            int i2 = this.m_firstFree;
            this.m_firstFree = i2 + 1;
            bArr[i2] = value;
            return;
        }
        int index = this.m_firstFree / this.m_blocksize;
        int offset = this.m_firstFree % this.m_blocksize;
        this.m_firstFree++;
        if (index >= this.m_map.length) {
            int newsize = index + this.m_numblocks;
            ?? r0 = new byte[newsize];
            System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
            this.m_map = r0;
        }
        byte[] block = this.m_map[index];
        if (null == block) {
            byte[][] bArr2 = this.m_map;
            byte[] bArr3 = new byte[this.m_blocksize];
            bArr2[index] = bArr3;
            block = bArr3;
        }
        block[offset] = value;
    }

    /* JADX WARN: Type inference failed for: r0v35, types: [byte[], byte[][], java.lang.Object] */
    private void addElements(byte value, int numberOfElements) {
        if (this.m_firstFree + numberOfElements < this.m_blocksize) {
            for (int i2 = 0; i2 < numberOfElements; i2++) {
                byte[] bArr = this.m_map0;
                int i3 = this.m_firstFree;
                this.m_firstFree = i3 + 1;
                bArr[i3] = value;
            }
            return;
        }
        int index = this.m_firstFree / this.m_blocksize;
        int offset = this.m_firstFree % this.m_blocksize;
        this.m_firstFree += numberOfElements;
        while (numberOfElements > 0) {
            if (index >= this.m_map.length) {
                int newsize = index + this.m_numblocks;
                ?? r0 = new byte[newsize];
                System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
                this.m_map = r0;
            }
            byte[] block = this.m_map[index];
            if (null == block) {
                byte[] bArr2 = new byte[this.m_blocksize];
                this.m_map[index] = bArr2;
                block = bArr2;
            }
            int copied = this.m_blocksize - offset < numberOfElements ? this.m_blocksize - offset : numberOfElements;
            numberOfElements -= copied;
            while (true) {
                int i4 = copied;
                copied--;
                if (i4 > 0) {
                    int i5 = offset;
                    offset++;
                    block[i5] = value;
                }
            }
            index++;
            offset = 0;
        }
    }

    private void addElements(int numberOfElements) {
        int newlen = this.m_firstFree + numberOfElements;
        if (newlen > this.m_blocksize) {
            int index = this.m_firstFree % this.m_blocksize;
            int newindex = (this.m_firstFree + numberOfElements) % this.m_blocksize;
            for (int i2 = index + 1; i2 <= newindex; i2++) {
                this.m_map[i2] = new byte[this.m_blocksize];
            }
        }
        this.m_firstFree = newlen;
    }

    /* JADX WARN: Type inference failed for: r0v46, types: [byte[], byte[][], java.lang.Object] */
    private void insertElementAt(byte value, int at2) {
        byte push;
        if (at2 == this.m_firstFree) {
            addElement(value);
            return;
        }
        if (at2 > this.m_firstFree) {
            int index = at2 / this.m_blocksize;
            if (index >= this.m_map.length) {
                int newsize = index + this.m_numblocks;
                ?? r0 = new byte[newsize];
                System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
                this.m_map = r0;
            }
            byte[] block = this.m_map[index];
            if (null == block) {
                byte[][] bArr = this.m_map;
                byte[] bArr2 = new byte[this.m_blocksize];
                bArr[index] = bArr2;
                block = bArr2;
            }
            int offset = at2 % this.m_blocksize;
            block[offset] = value;
            this.m_firstFree = offset + 1;
            return;
        }
        int maxindex = this.m_firstFree + (1 / this.m_blocksize);
        this.m_firstFree++;
        int offset2 = at2 % this.m_blocksize;
        for (int index2 = at2 / this.m_blocksize; index2 <= maxindex; index2++) {
            int copylen = (this.m_blocksize - offset2) - 1;
            byte[] block2 = this.m_map[index2];
            if (null == block2) {
                push = 0;
                byte[] bArr3 = new byte[this.m_blocksize];
                this.m_map[index2] = bArr3;
                block2 = bArr3;
            } else {
                push = block2[this.m_blocksize - 1];
                System.arraycopy(block2, offset2, block2, offset2 + 1, copylen);
            }
            block2[offset2] = value;
            value = push;
            offset2 = 0;
        }
    }

    public void removeAllElements() {
        this.m_firstFree = 0;
    }

    private boolean removeElement(byte s2) {
        int at2 = indexOf(s2, 0);
        if (at2 < 0) {
            return false;
        }
        removeElementAt(at2);
        return true;
    }

    private void removeElementAt(int at2) {
        if (at2 < this.m_firstFree) {
            int maxindex = this.m_firstFree / this.m_blocksize;
            int offset = at2 % this.m_blocksize;
            for (int index = at2 / this.m_blocksize; index <= maxindex; index++) {
                int copylen = (this.m_blocksize - offset) - 1;
                byte[] block = this.m_map[index];
                if (null == block) {
                    byte[] bArr = new byte[this.m_blocksize];
                    this.m_map[index] = bArr;
                    block = bArr;
                } else {
                    System.arraycopy(block, offset + 1, block, offset, copylen);
                }
                if (index < maxindex) {
                    byte[] next = this.m_map[index + 1];
                    if (next != null) {
                        block[this.m_blocksize - 1] = next != null ? next[0] : (byte) 0;
                    }
                } else {
                    block[this.m_blocksize - 1] = 0;
                }
                offset = 0;
            }
        }
        this.m_firstFree--;
    }

    /* JADX WARN: Type inference failed for: r0v19, types: [byte[], byte[][], java.lang.Object] */
    public void setElementAt(byte value, int at2) {
        if (at2 < this.m_blocksize) {
            this.m_map0[at2] = value;
            return;
        }
        int index = at2 / this.m_blocksize;
        int offset = at2 % this.m_blocksize;
        if (index >= this.m_map.length) {
            int newsize = index + this.m_numblocks;
            ?? r0 = new byte[newsize];
            System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
            this.m_map = r0;
        }
        byte[] block = this.m_map[index];
        if (null == block) {
            byte[][] bArr = this.m_map;
            byte[] bArr2 = new byte[this.m_blocksize];
            bArr[index] = bArr2;
            block = bArr2;
        }
        block[offset] = value;
        if (at2 >= this.m_firstFree) {
            this.m_firstFree = at2 + 1;
        }
    }

    public byte elementAt(int i2) {
        if (i2 < this.m_blocksize) {
            return this.m_map0[i2];
        }
        return this.m_map[i2 / this.m_blocksize][i2 % this.m_blocksize];
    }

    private boolean contains(byte s2) {
        return indexOf(s2, 0) >= 0;
    }

    public int indexOf(byte elem, int index) {
        if (index >= this.m_firstFree) {
            return -1;
        }
        int boffset = index % this.m_blocksize;
        int maxindex = this.m_firstFree / this.m_blocksize;
        for (int bindex = index / this.m_blocksize; bindex < maxindex; bindex++) {
            byte[] block = this.m_map[bindex];
            if (block != null) {
                for (int offset = boffset; offset < this.m_blocksize; offset++) {
                    if (block[offset] == elem) {
                        return offset + (bindex * this.m_blocksize);
                    }
                }
            }
            boffset = 0;
        }
        int maxoffset = this.m_firstFree % this.m_blocksize;
        byte[] block2 = this.m_map[maxindex];
        for (int offset2 = boffset; offset2 < maxoffset; offset2++) {
            if (block2[offset2] == elem) {
                return offset2 + (maxindex * this.m_blocksize);
            }
        }
        return -1;
    }

    public int indexOf(byte elem) {
        return indexOf(elem, 0);
    }

    private int lastIndexOf(byte elem) {
        int boffset = this.m_firstFree % this.m_blocksize;
        for (int index = this.m_firstFree / this.m_blocksize; index >= 0; index--) {
            byte[] block = this.m_map[index];
            if (block != null) {
                for (int offset = boffset; offset >= 0; offset--) {
                    if (block[offset] == elem) {
                        return offset + (index * this.m_blocksize);
                    }
                }
            }
            boffset = 0;
        }
        return -1;
    }
}
