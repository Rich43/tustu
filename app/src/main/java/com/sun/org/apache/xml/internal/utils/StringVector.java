package com.sun.org.apache.xml.internal.utils;

import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StringVector.class */
public class StringVector implements Serializable {
    static final long serialVersionUID = 4995234972032919748L;
    protected int m_blocksize;
    protected String[] m_map;
    protected int m_firstFree;
    protected int m_mapSize;

    public StringVector() {
        this.m_firstFree = 0;
        this.m_blocksize = 8;
        this.m_mapSize = this.m_blocksize;
        this.m_map = new String[this.m_blocksize];
    }

    public StringVector(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = blocksize;
        this.m_map = new String[blocksize];
    }

    public int getLength() {
        return this.m_firstFree;
    }

    public final int size() {
        return this.m_firstFree;
    }

    public final void addElement(String value) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            String[] newMap = new String[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final String elementAt(int i2) {
        return this.m_map[i2];
    }

    public final boolean contains(String s2) {
        if (null == s2) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2].equals(s2)) {
                return true;
            }
        }
        return false;
    }

    public final boolean containsIgnoreCase(String s2) {
        if (null == s2) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (this.m_map[i2].equalsIgnoreCase(s2)) {
                return true;
            }
        }
        return false;
    }

    public final void push(String s2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            String[] newMap = new String[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = s2;
        this.m_firstFree++;
    }

    public final String pop() {
        if (this.m_firstFree <= 0) {
            return null;
        }
        this.m_firstFree--;
        String s2 = this.m_map[this.m_firstFree];
        this.m_map[this.m_firstFree] = null;
        return s2;
    }

    public final String peek() {
        if (this.m_firstFree <= 0) {
            return null;
        }
        return this.m_map[this.m_firstFree - 1];
    }
}
