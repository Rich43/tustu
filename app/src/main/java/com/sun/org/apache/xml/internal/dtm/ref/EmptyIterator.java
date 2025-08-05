package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/EmptyIterator.class */
public final class EmptyIterator implements DTMAxisIterator {
    private static final EmptyIterator INSTANCE = new EmptyIterator();

    public static DTMAxisIterator getInstance() {
        return INSTANCE;
    }

    private EmptyIterator() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final int next() {
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final DTMAxisIterator reset() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final int getLast() {
        return 0;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final int getPosition() {
        return 1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final void setMark() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final void gotoMark() {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final DTMAxisIterator setStartNode(int node) {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final int getStartNode() {
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final boolean isReverse() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final DTMAxisIterator cloneIterator() {
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final void setRestartable(boolean isRestartable) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public final int getNodeByPosition(int position) {
        return -1;
    }
}
