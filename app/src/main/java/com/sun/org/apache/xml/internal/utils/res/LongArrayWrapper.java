package com.sun.org.apache.xml.internal.utils.res;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/res/LongArrayWrapper.class */
public class LongArrayWrapper {
    private long[] m_long;

    public LongArrayWrapper(long[] arg) {
        this.m_long = arg;
    }

    public long getLong(int index) {
        return this.m_long[index];
    }

    public int getLength() {
        return this.m_long.length;
    }
}
