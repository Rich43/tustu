package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StringToStringTable.class */
public class StringToStringTable {
    private int m_blocksize;
    private String[] m_map;
    private int m_firstFree;
    private int m_mapSize;

    public StringToStringTable() {
        this.m_firstFree = 0;
        this.m_blocksize = 16;
        this.m_mapSize = this.m_blocksize;
        this.m_map = new String[this.m_blocksize];
    }

    public StringToStringTable(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = blocksize;
        this.m_map = new String[blocksize];
    }

    public final int getLength() {
        return this.m_firstFree;
    }

    public final void put(String key, String value) {
        if (this.m_firstFree + 2 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            String[] newMap = new String[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = key;
        this.m_firstFree++;
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final String get(String key) {
        for (int i2 = 0; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equals(key)) {
                return this.m_map[i2 + 1];
            }
        }
        return null;
    }

    public final void remove(String key) {
        for (int i2 = 0; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equals(key)) {
                if (i2 + 2 < this.m_firstFree) {
                    System.arraycopy(this.m_map, i2 + 2, this.m_map, i2, this.m_firstFree - (i2 + 2));
                }
                this.m_firstFree -= 2;
                this.m_map[this.m_firstFree] = null;
                this.m_map[this.m_firstFree + 1] = null;
                return;
            }
        }
    }

    public final String getIgnoreCase(String key) {
        if (null == key) {
            return null;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equalsIgnoreCase(key)) {
                return this.m_map[i2 + 1];
            }
        }
        return null;
    }

    public final String getByValue(String val) {
        for (int i2 = 1; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equals(val)) {
                return this.m_map[i2 - 1];
            }
        }
        return null;
    }

    public final String elementAt(int i2) {
        return this.m_map[i2];
    }

    public final boolean contains(String key) {
        for (int i2 = 0; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equals(key)) {
                return true;
            }
        }
        return false;
    }

    public final boolean containsValue(String val) {
        for (int i2 = 1; i2 < this.m_firstFree; i2 += 2) {
            if (this.m_map[i2].equals(val)) {
                return true;
            }
        }
        return false;
    }
}
