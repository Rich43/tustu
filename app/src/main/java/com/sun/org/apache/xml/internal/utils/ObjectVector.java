package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ObjectVector.class */
public class ObjectVector implements Cloneable {
    protected int m_blocksize;
    protected Object[] m_map;
    protected int m_firstFree;
    protected int m_mapSize;

    public ObjectVector() {
        this.m_firstFree = 0;
        this.m_blocksize = 32;
        this.m_mapSize = this.m_blocksize;
        this.m_map = new Object[this.m_blocksize];
    }

    public ObjectVector(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = blocksize;
        this.m_map = new Object[blocksize];
    }

    public ObjectVector(int blocksize, int increaseSize) {
        this.m_firstFree = 0;
        this.m_blocksize = increaseSize;
        this.m_mapSize = blocksize;
        this.m_map = new Object[blocksize];
    }

    public ObjectVector(ObjectVector v2) {
        this.m_firstFree = 0;
        this.m_map = new Object[v2.m_mapSize];
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

    public final void addElement(Object value) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Object[] newMap = new Object[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final void addElements(Object value, int numberOfElements) {
        if (this.m_firstFree + numberOfElements >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize + numberOfElements;
            Object[] newMap = new Object[this.m_mapSize];
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
            Object[] newMap = new Object[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_firstFree += numberOfElements;
    }

    public final void insertElementAt(Object value, int at2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Object[] newMap = new Object[this.m_mapSize];
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
            this.m_map[i2] = null;
        }
        this.m_firstFree = 0;
    }

    public final boolean removeElement(Object s2) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == s2) {
                if (i2 + 1 < this.m_firstFree) {
                    System.arraycopy(this.m_map, i2 + 1, this.m_map, i2 - 1, this.m_firstFree - i2);
                } else {
                    this.m_map[i2] = null;
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
            this.m_map[i2] = null;
        }
        this.m_firstFree--;
    }

    public final void setElementAt(Object value, int index) {
        this.m_map[index] = value;
    }

    public final Object elementAt(int i2) {
        return this.m_map[i2];
    }

    public final boolean contains(Object s2) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == s2) {
                return true;
            }
        }
        return false;
    }

    public final int indexOf(Object elem, int index) {
        for (int i2 = index; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public final int indexOf(Object elem) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public final int lastIndexOf(Object elem) {
        for (int i2 = this.m_firstFree - 1; i2 >= 0; i2--) {
            if (this.m_map[i2] == elem) {
                return i2;
            }
        }
        return Integer.MIN_VALUE;
    }

    public final void setToSize(int size) {
        Object[] newMap = new Object[size];
        System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
        this.m_mapSize = size;
        this.m_map = newMap;
    }

    public Object clone() throws CloneNotSupportedException {
        return new ObjectVector(this);
    }
}
