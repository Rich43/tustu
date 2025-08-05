package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/SuballocatedIntVector.class */
public class SuballocatedIntVector {
    protected int m_blocksize;
    protected int m_SHIFT;
    protected int m_MASK;
    protected static final int NUMBLOCKS_DEFAULT = 32;
    protected int m_numblocks;
    protected int[][] m_map;
    protected int m_firstFree;
    protected int[] m_map0;
    protected int[] m_buildCache;
    protected int m_buildCacheStartIndex;

    public SuballocatedIntVector() {
        this(2048);
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [int[], int[][]] */
    public SuballocatedIntVector(int blocksize, int numblocks) {
        this.m_numblocks = 32;
        this.m_firstFree = 0;
        this.m_SHIFT = 0;
        while (true) {
            int i2 = blocksize >>> 1;
            blocksize = i2;
            if (0 == i2) {
                this.m_blocksize = 1 << this.m_SHIFT;
                this.m_MASK = this.m_blocksize - 1;
                this.m_numblocks = numblocks;
                this.m_map0 = new int[this.m_blocksize];
                this.m_map = new int[numblocks];
                this.m_map[0] = this.m_map0;
                this.m_buildCache = this.m_map0;
                this.m_buildCacheStartIndex = 0;
                return;
            }
            this.m_SHIFT++;
        }
    }

    public SuballocatedIntVector(int blocksize) {
        this(blocksize, 32);
    }

    public int size() {
        return this.m_firstFree;
    }

    public void setSize(int sz) {
        if (this.m_firstFree > sz) {
            this.m_firstFree = sz;
        }
    }

    /* JADX WARN: Type inference failed for: r0v25, types: [int[], int[][], java.lang.Object] */
    public void addElement(int value) {
        int indexRelativeToCache = this.m_firstFree - this.m_buildCacheStartIndex;
        if (indexRelativeToCache >= 0 && indexRelativeToCache < this.m_blocksize) {
            this.m_buildCache[indexRelativeToCache] = value;
            this.m_firstFree++;
            return;
        }
        int index = this.m_firstFree >>> this.m_SHIFT;
        int offset = this.m_firstFree & this.m_MASK;
        if (index >= this.m_map.length) {
            int newsize = index + this.m_numblocks;
            ?? r0 = new int[newsize];
            System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
            this.m_map = r0;
        }
        int[] block = this.m_map[index];
        if (null == block) {
            int[][] iArr = this.m_map;
            int[] iArr2 = new int[this.m_blocksize];
            iArr[index] = iArr2;
            block = iArr2;
        }
        block[offset] = value;
        this.m_buildCache = block;
        this.m_buildCacheStartIndex = this.m_firstFree - offset;
        this.m_firstFree++;
    }

    /* JADX WARN: Type inference failed for: r0v35, types: [int[], int[][], java.lang.Object] */
    private void addElements(int value, int numberOfElements) {
        if (this.m_firstFree + numberOfElements < this.m_blocksize) {
            for (int i2 = 0; i2 < numberOfElements; i2++) {
                int[] iArr = this.m_map0;
                int i3 = this.m_firstFree;
                this.m_firstFree = i3 + 1;
                iArr[i3] = value;
            }
            return;
        }
        int index = this.m_firstFree >>> this.m_SHIFT;
        int offset = this.m_firstFree & this.m_MASK;
        this.m_firstFree += numberOfElements;
        while (numberOfElements > 0) {
            if (index >= this.m_map.length) {
                int newsize = index + this.m_numblocks;
                ?? r0 = new int[newsize];
                System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
                this.m_map = r0;
            }
            int[] block = this.m_map[index];
            if (null == block) {
                int[] iArr2 = new int[this.m_blocksize];
                this.m_map[index] = iArr2;
                block = iArr2;
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
            int index = this.m_firstFree >>> this.m_SHIFT;
            int newindex = (this.m_firstFree + numberOfElements) >>> this.m_SHIFT;
            for (int i2 = index + 1; i2 <= newindex; i2++) {
                this.m_map[i2] = new int[this.m_blocksize];
            }
        }
        this.m_firstFree = newlen;
    }

    /* JADX WARN: Type inference failed for: r0v46, types: [int[], int[][], java.lang.Object] */
    private void insertElementAt(int value, int at2) {
        int push;
        if (at2 == this.m_firstFree) {
            addElement(value);
            return;
        }
        if (at2 > this.m_firstFree) {
            int index = at2 >>> this.m_SHIFT;
            if (index >= this.m_map.length) {
                int newsize = index + this.m_numblocks;
                ?? r0 = new int[newsize];
                System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
                this.m_map = r0;
            }
            int[] block = this.m_map[index];
            if (null == block) {
                int[][] iArr = this.m_map;
                int[] iArr2 = new int[this.m_blocksize];
                iArr[index] = iArr2;
                block = iArr2;
            }
            int offset = at2 & this.m_MASK;
            block[offset] = value;
            this.m_firstFree = offset + 1;
            return;
        }
        int maxindex = this.m_firstFree >>> this.m_SHIFT;
        this.m_firstFree++;
        int offset2 = at2 & this.m_MASK;
        for (int index2 = at2 >>> this.m_SHIFT; index2 <= maxindex; index2++) {
            int copylen = (this.m_blocksize - offset2) - 1;
            int[] block2 = this.m_map[index2];
            if (null == block2) {
                push = 0;
                int[] iArr3 = new int[this.m_blocksize];
                this.m_map[index2] = iArr3;
                block2 = iArr3;
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
        this.m_buildCache = this.m_map0;
        this.m_buildCacheStartIndex = 0;
    }

    private boolean removeElement(int s2) {
        int at2 = indexOf(s2, 0);
        if (at2 < 0) {
            return false;
        }
        removeElementAt(at2);
        return true;
    }

    private void removeElementAt(int at2) {
        if (at2 < this.m_firstFree) {
            int maxindex = this.m_firstFree >>> this.m_SHIFT;
            int offset = at2 & this.m_MASK;
            for (int index = at2 >>> this.m_SHIFT; index <= maxindex; index++) {
                int copylen = (this.m_blocksize - offset) - 1;
                int[] block = this.m_map[index];
                if (null == block) {
                    int[] iArr = new int[this.m_blocksize];
                    this.m_map[index] = iArr;
                    block = iArr;
                } else {
                    System.arraycopy(block, offset + 1, block, offset, copylen);
                }
                if (index < maxindex) {
                    int[] next = this.m_map[index + 1];
                    if (next != null) {
                        block[this.m_blocksize - 1] = next != null ? next[0] : 0;
                    }
                } else {
                    block[this.m_blocksize - 1] = 0;
                }
                offset = 0;
            }
        }
        this.m_firstFree--;
    }

    /* JADX WARN: Type inference failed for: r0v17, types: [int[], int[][], java.lang.Object] */
    public void setElementAt(int value, int at2) {
        if (at2 < this.m_blocksize) {
            this.m_map0[at2] = value;
        } else {
            int index = at2 >>> this.m_SHIFT;
            int offset = at2 & this.m_MASK;
            if (index >= this.m_map.length) {
                int newsize = index + this.m_numblocks;
                ?? r0 = new int[newsize];
                System.arraycopy(this.m_map, 0, r0, 0, this.m_map.length);
                this.m_map = r0;
            }
            int[] block = this.m_map[index];
            if (null == block) {
                int[][] iArr = this.m_map;
                int[] iArr2 = new int[this.m_blocksize];
                iArr[index] = iArr2;
                block = iArr2;
            }
            block[offset] = value;
        }
        if (at2 >= this.m_firstFree) {
            this.m_firstFree = at2 + 1;
        }
    }

    public int elementAt(int i2) {
        if (i2 < this.m_blocksize) {
            return this.m_map0[i2];
        }
        return this.m_map[i2 >>> this.m_SHIFT][i2 & this.m_MASK];
    }

    private boolean contains(int s2) {
        return indexOf(s2, 0) >= 0;
    }

    public int indexOf(int elem, int index) {
        if (index >= this.m_firstFree) {
            return -1;
        }
        int boffset = index & this.m_MASK;
        int maxindex = this.m_firstFree >>> this.m_SHIFT;
        for (int bindex = index >>> this.m_SHIFT; bindex < maxindex; bindex++) {
            int[] block = this.m_map[bindex];
            if (block != null) {
                for (int offset = boffset; offset < this.m_blocksize; offset++) {
                    if (block[offset] == elem) {
                        return offset + (bindex * this.m_blocksize);
                    }
                }
            }
            boffset = 0;
        }
        int maxoffset = this.m_firstFree & this.m_MASK;
        int[] block2 = this.m_map[maxindex];
        for (int offset2 = boffset; offset2 < maxoffset; offset2++) {
            if (block2[offset2] == elem) {
                return offset2 + (maxindex * this.m_blocksize);
            }
        }
        return -1;
    }

    public int indexOf(int elem) {
        return indexOf(elem, 0);
    }

    private int lastIndexOf(int elem) {
        int boffset = this.m_firstFree & this.m_MASK;
        for (int index = this.m_firstFree >>> this.m_SHIFT; index >= 0; index--) {
            int[] block = this.m_map[index];
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

    public final int[] getMap0() {
        return this.m_map0;
    }

    public final int[][] getMap() {
        return this.m_map;
    }
}
