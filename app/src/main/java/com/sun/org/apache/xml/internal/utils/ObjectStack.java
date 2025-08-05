package com.sun.org.apache.xml.internal.utils;

import java.util.EmptyStackException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ObjectStack.class */
public class ObjectStack extends ObjectVector {
    public ObjectStack() {
    }

    public ObjectStack(int blocksize) {
        super(blocksize);
    }

    public ObjectStack(ObjectStack v2) {
        super(v2);
    }

    public Object push(Object i2) {
        if (this.m_firstFree + 1 >= this.m_mapSize) {
            this.m_mapSize += this.m_blocksize;
            Object[] newMap = new Object[this.m_mapSize];
            System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
            this.m_map = newMap;
        }
        this.m_map[this.m_firstFree] = i2;
        this.m_firstFree++;
        return i2;
    }

    public Object pop() {
        Object[] objArr = this.m_map;
        int i2 = this.m_firstFree - 1;
        this.m_firstFree = i2;
        Object val = objArr[i2];
        this.m_map[this.m_firstFree] = null;
        return val;
    }

    public void quickPop(int n2) {
        this.m_firstFree -= n2;
    }

    public Object peek() {
        try {
            return this.m_map[this.m_firstFree - 1];
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public Object peek(int n2) {
        try {
            return this.m_map[this.m_firstFree - (1 + n2)];
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public void setTop(Object val) {
        try {
            this.m_map[this.m_firstFree - 1] = val;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new EmptyStackException();
        }
    }

    public boolean empty() {
        return this.m_firstFree == 0;
    }

    public int search(Object o2) {
        int i2 = lastIndexOf(o2);
        if (i2 >= 0) {
            return size() - i2;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.utils.ObjectVector
    public Object clone() throws CloneNotSupportedException {
        return (ObjectStack) super.clone();
    }
}
