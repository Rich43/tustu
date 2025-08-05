package com.sun.org.apache.xml.internal.utils;

import java.util.EmptyStackException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/IntStack.class */
public class IntStack extends IntVector {
    public IntStack() {
    }

    public IntStack(int blocksize) {
        super(blocksize);
    }

    public IntStack(IntStack v2) {
        super(v2);
    }

    public int push(int i2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            int[] newMap = new int[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = i2;
        this.m_firstFree++;
        return i2;
    }

    public final int pop() {
        int[] iArr = this.m_map;
        int i2 = this.m_firstFree - 1;
        this.m_firstFree = i2;
        return iArr[i2];
    }

    public final void quickPop(int n2) {
        this.m_firstFree -= n2;
    }

    public final int peek() {
        try {
            return this.m_map[this.m_firstFree - 1];
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public int peek(int n2) {
        try {
            return this.m_map[this.m_firstFree - (1 + n2)];
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public void setTop(int val) {
        try {
            this.m_map[this.m_firstFree - 1] = val;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public boolean empty() {
        return this.m_firstFree == 0;
    }

    public int search(int o2) {
        int i2 = lastIndexOf(o2);
        if (i2 >= 0) {
            return size() - i2;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.utils.IntVector
    public Object clone() throws CloneNotSupportedException {
        return (IntStack) super.clone();
    }
}
