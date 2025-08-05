package com.sun.org.apache.xml.internal.utils;

import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/NodeVector.class */
public class NodeVector implements Serializable, Cloneable {
    static final long serialVersionUID = -713473092200731870L;
    private int m_blocksize;
    private int[] m_map;
    protected int m_firstFree;
    private int m_mapSize;

    public NodeVector() {
        this.m_firstFree = 0;
        this.m_blocksize = 32;
        this.m_mapSize = 0;
    }

    public NodeVector(int blocksize) {
        this.m_firstFree = 0;
        this.m_blocksize = blocksize;
        this.m_mapSize = 0;
    }

    public Object clone() throws CloneNotSupportedException {
        NodeVector clone = (NodeVector) super.clone();
        if (null != this.m_map && this.m_map == clone.m_map) {
            clone.m_map = new int[this.m_map.length];
            System.arraycopy(this.m_map, 0, clone.m_map, 0, this.m_map.length);
        }
        return clone;
    }

    public int size() {
        return this.m_firstFree;
    }

    public void addElement(int value) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new int[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                int[] newMap = new int[this.m_mapSize];
                System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
                this.m_map = newMap;
            }
        }
        this.m_map[this.m_firstFree] = value;
        this.m_firstFree++;
    }

    public final void push(int value) {
        int ff = this.m_firstFree;
        if (ff + 1 >= this.m_mapSize) {
            if (null == this.m_map) {
                this.m_map = new int[this.m_blocksize];
                this.m_mapSize = this.m_blocksize;
            } else {
                this.m_mapSize += this.m_blocksize;
                int[] newMap = new int[this.m_mapSize];
                System.arraycopy(this.m_map, 0, newMap, 0, ff + 1);
                this.m_map = newMap;
            }
        }
        this.m_map[ff] = value;
        this.m_firstFree = ff + 1;
    }

    public final int pop() {
        this.m_firstFree--;
        int n2 = this.m_map[this.m_firstFree];
        this.m_map[this.m_firstFree] = -1;
        return n2;
    }

    public final int popAndTop() {
        this.m_firstFree--;
        this.m_map[this.m_firstFree] = -1;
        if (this.m_firstFree == 0) {
            return -1;
        }
        return this.m_map[this.m_firstFree - 1];
    }

    public final void popQuick() {
        this.m_firstFree--;
        this.m_map[this.m_firstFree] = -1;
    }

    public final int peepOrNull() {
        if (null == this.m_map || this.m_firstFree <= 0) {
            return -1;
        }
        return this.m_map[this.m_firstFree - 1];
    }

    public final void pushPair(int v1, int v2) {
        if (null == this.m_map) {
            this.m_map = new int[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        } else if (this.m_firstFree + 2 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = v1;
        this.m_map[this.m_firstFree + 1] = v2;
        this.m_firstFree += 2;
    }

    public final void popPair() {
        this.m_firstFree -= 2;
        this.m_map[this.m_firstFree] = -1;
        this.m_map[this.m_firstFree + 1] = -1;
    }

    public final void setTail(int n2) {
        this.m_map[this.m_firstFree - 1] = n2;
    }

    public final void setTailSub1(int n2) {
        this.m_map[this.m_firstFree - 2] = n2;
    }

    public final int peepTail() {
        return this.m_map[this.m_firstFree - 1];
    }

    public final int peepTailSub1() {
        return this.m_map[this.m_firstFree - 2];
    }

    public void insertInOrder(int value) {
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            if (value < this.m_map[i2]) {
                insertElementAt(value, i2);
                return;
            }
        }
        addElement(value);
    }

    public void insertElementAt(int value, int at2) {
        if (null == this.m_map) {
            this.m_map = new int[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        } else if (this.m_firstFree + 1 >= this.m_mapSize) {
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

    public void appendNodes(NodeVector nodes) {
        int nNodes = nodes.size();
        if (null == this.m_map) {
            this.m_mapSize = nNodes + this.m_blocksize;
            this.m_map = new int[this.m_mapSize];
        } else if (this.m_firstFree + nNodes >= this.m_mapSize) {
            this.m_mapSize += nNodes + this.m_blocksize;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + nNodes);
            this.m_map = newMap;
        }
        System.arraycopy(nodes.m_map, 0, this.m_map, this.m_firstFree, nNodes);
        this.m_firstFree += nNodes;
    }

    public void removeAllElements() {
        if (null == this.m_map) {
            return;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            this.m_map[i2] = -1;
        }
        this.m_firstFree = 0;
    }

    public void RemoveAllNoClear() {
        if (null == this.m_map) {
            return;
        }
        this.m_firstFree = 0;
    }

    public boolean removeElement(int s2) {
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            int node = this.m_map[i2];
            if (node == s2) {
                if (i2 > this.m_firstFree) {
                    System.arraycopy(this.m_map, i2 + 1, this.m_map, i2 - 1, this.m_firstFree - i2);
                } else {
                    this.m_map[i2] = -1;
                }
                this.m_firstFree--;
                return true;
            }
        }
        return false;
    }

    public void removeElementAt(int i2) {
        if (null == this.m_map) {
            return;
        }
        if (i2 > this.m_firstFree) {
            System.arraycopy(this.m_map, i2 + 1, this.m_map, i2 - 1, this.m_firstFree - i2);
        } else {
            this.m_map[i2] = -1;
        }
    }

    public void setElementAt(int node, int index) {
        if (null == this.m_map) {
            this.m_map = new int[this.m_blocksize];
            this.m_mapSize = this.m_blocksize;
        }
        if (index == -1) {
            addElement(node);
        }
        this.m_map[index] = node;
    }

    public int elementAt(int i2) {
        if (null == this.m_map) {
            return -1;
        }
        return this.m_map[i2];
    }

    public boolean contains(int s2) {
        if (null == this.m_map) {
            return false;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            int node = this.m_map[i2];
            if (node == s2) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(int elem, int index) {
        if (null == this.m_map) {
            return -1;
        }
        for (int i2 = index; i2 < this.m_firstFree; i2++) {
            int node = this.m_map[i2];
            if (node == elem) {
                return i2;
            }
        }
        return -1;
    }

    public int indexOf(int elem) {
        if (null == this.m_map) {
            return -1;
        }
        for (int i2 = 0; i2 < this.m_firstFree; i2++) {
            int node = this.m_map[i2];
            if (node == elem) {
                return i2;
            }
        }
        return -1;
    }

    public void sort(int[] a2, int lo0, int hi0) throws Exception {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi) {
            return;
        }
        if (lo == hi - 1) {
            if (a2[lo] > a2[hi]) {
                int T2 = a2[lo];
                a2[lo] = a2[hi];
                a2[hi] = T2;
                return;
            }
            return;
        }
        int mid = (lo + hi) >>> 1;
        int pivot = a2[mid];
        a2[mid] = a2[hi];
        a2[hi] = pivot;
        while (lo < hi) {
            while (a2[lo] <= pivot && lo < hi) {
                lo++;
            }
            while (pivot <= a2[hi] && lo < hi) {
                hi--;
            }
            if (lo < hi) {
                int T3 = a2[lo];
                a2[lo] = a2[hi];
                a2[hi] = T3;
            }
        }
        a2[hi0] = a2[hi];
        a2[hi] = pivot;
        sort(a2, lo0, lo - 1);
        sort(a2, hi + 1, hi0);
    }

    public void sort() throws Exception {
        sort(this.m_map, 0, this.m_firstFree - 1);
    }
}
