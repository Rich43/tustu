package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/IntVector.class */
public class IntVector implements Cloneable {
    protected int m_blocksize;
    protected int[] m_map;
    protected int m_firstFree;
    protected int m_mapSize;

    public IntVector() {
        this.m_firstFree = 0;
        this.m_blocksize = 32;
        this.m_mapSize = this.m_blocksize;
        this.m_map = new int[this.m_blocksize];
    }

    public IntVector(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = blocksize;
        this.m_map = new int[blocksize];
    }

    public IntVector(int blocksize, int increaseSize) {
        this.m_firstFree = 0;
        this.m_blocksize = increaseSize;
        this.m_mapSize = blocksize;
        this.m_map = new int[blocksize];
    }

    public IntVector(IntVector v2) {
        this.m_firstFree = 0;
        this.m_map = new int[v2.m_mapSize];
        this.m_mapSize = v2.m_mapSize;
        this.m_firstFree = v2.m_firstFree;
        this.m_blocksize = v2.m_blocksize;
        System.arraycopy(v2.m_map, 0, this.m_map, 0, this.m_firstFree);
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void setSize(int sz) {
        this.m_firstFree = sz;
    }

    public final void addElement(int value) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final void addElements(int value, int numberOfElements) {
        if (this.m_firstFree + numberOfElements >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize + numberOfElements;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        for (int i2 = 0; i2 < numberOfElements; i2++) {
            this.m_map[this.m_firstFree] = value;
            this.m_firstFree++;
        }
    }

    public final void addElements(int numberOfElements) {
        if (this.m_firstFree + numberOfElements >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize + numberOfElements;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_firstFree += numberOfElements;
    }

    public final void insertElementAt(int value, int at2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        if (at2 <= this.m_firstFree - 1) {
            System.arraycopy(this.m_map, at2, this.m_map, at2 + 1, this.m_firstFree - at2);
        }
        this.m_map[at2] = value;
        this.m_firstFree++;
    }

    public final void removeAllElements() {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            this.m_map[i2] = Integer.MIN_VALUE;
        }
        this.m_firstFree = 0;
    }

    public final boolean removeElement(int s2) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == s2) {
                if (i2 + 1 < this.m_firstFree) {
                    System.arraycopy(this.m_map, i2 + 1, this.m_map, i2 - 1, this.m_firstFree - i2);
                } else {
                    this.m_map[i2] = Integer.MIN_VALUE;
                }
                this.m_firstFree--;
                return true;
            }
        }
        return false;
    }

    public final void removeElementAt(int i2) {
        if (i2 > this.m_firstFree) {
            System.arraycopy(this.m_map, i2 + 1, this.m_map, i2, this.m_firstFree);
        } else {
            this.m_map[i2] = Integer.MIN_VALUE;
        }
        this.m_firstFree--;
    }

    public final void setElementAt(int value, int index) {
        this.m_map[index] = value;
    }

    public final int elementAt(int i2) {
        return this.m_map[i2];
    }

    public final boolean contains(int s2) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == s2) {
                return true;
            }
        }
        return false;
    }

    public final int indexOf(int elem, int index) {
        for (int i2 = index; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public final int indexOf(int elem) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public final int lastIndexOf(int elem) {
        for (int i2 = this.m_firstFree - 1; i2 >= 0; i2--) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public Object clone() throws CloneNotSupportedException {
        return new IntVector(this);
    }
}
