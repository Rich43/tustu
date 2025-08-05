package com.sun.org.apache.xml.internal.serializer.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/utils/BoolStack.class */
public final class BoolStack {
    private boolean[] m_values;
    private int m_allocatedSize;
    private int m_index;

    public BoolStack() {
        this(32);
    }

    public BoolStack(int size) {
        this.m_allocatedSize = size;
        this.m_values = new boolean[size];
        this.m_index = -1;
    }

    public final int size() {
        return this.m_index + 1;
    }

    public final void clear() {
        this.m_index = -1;
    }

    public final boolean push(boolean val) {
        if (this.m_index == this.m_allocatedSize - 1) {
            grow();
        }
        boolean[] zArr = this.m_values;
        int i2 = this.m_index + 1;
        this.m_index = i2;
        zArr[i2] = val;
        return val;
    }

    public final boolean pop() {
        boolean[] zArr = this.m_values;
        int i2 = this.m_index;
        this.m_index = i2 - 1;
        return zArr[i2];
    }

    public final boolean popAndTop() {
        this.m_index--;
        if (this.m_index >= 0) {
            return this.m_values[this.m_index];
        }
        return false;
    }

    public final void setTop(boolean b2) {
        this.m_values[this.m_index] = b2;
    }

    public final boolean peek() {
        return this.m_values[this.m_index];
    }

    public final boolean peekOrFalse() {
        if (this.m_index > -1) {
            return this.m_values[this.m_index];
        }
        return false;
    }

    public final boolean peekOrTrue() {
        if (this.m_index > -1) {
            return this.m_values[this.m_index];
        }
        return true;
    }

    public boolean isEmpty() {
        return this.m_index == -1;
    }

    private void grow() {
        this.m_allocatedSize *= 2;
        boolean[] newVector = new boolean[this.m_allocatedSize];
        System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
        this.m_values = newVector;
    }
}
