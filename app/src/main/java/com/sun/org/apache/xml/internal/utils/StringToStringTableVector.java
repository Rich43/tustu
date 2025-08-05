package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StringToStringTableVector.class */
public class StringToStringTableVector {
    private int m_blocksize;
    private StringToStringTable[] m_map;
    private int m_firstFree;
    private int m_mapSize;

    public StringToStringTableVector() {
        this.m_firstFree = 0;
        this.m_blocksize = 8;
        this.m_mapSize = this.m_blocksize;
        this.m_map = new StringToStringTable[this.m_blocksize];
    }

    public StringToStringTableVector(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = blocksize;
        this.m_map = new StringToStringTable[blocksize];
    }

    public final int getLength() {
        return this.m_firstFree;
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void addElement(StringToStringTable value) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            StringToStringTable[] newMap = new StringToStringTable[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final String get(String key) {
        for (int i2 = this.m_firstFree - 1; i2 >= 0; i2--) {
            String nsuri = this.m_map[i2].get(key);
            if (nsuri != null) {
                return nsuri;
            }
        }
        return null;
    }

    public final boolean containsKey(String key) {
        for (int i2 = this.m_firstFree - 1; i2 >= 0; i2--) {
            if (this.m_map[i2].get(key) != null) {
                return true;
            }
        }
        return false;
    }

    public final void removeLastElem() {
        if (this.m_firstFree > 0) {
            this.m_map[this.m_firstFree] = null;
            this.m_firstFree--;
        }
    }

    public final StringToStringTable elementAt(int i2) {
        return this.m_map[i2];
    }

    public final boolean contains(StringToStringTable s2) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2].equals(s2)) {
                return true;
            }
        }
        return false;
    }
}
