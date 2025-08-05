package com.sun.org.apache.xml.internal.dtm;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMAxisIterator.class */
public interface DTMAxisIterator extends Cloneable {
    public static final int END = -1;

    int next();

    DTMAxisIterator reset();

    int getLast();

    int getPosition();

    void setMark();

    void gotoMark();

    DTMAxisIterator setStartNode(int i2);

    int getStartNode();

    boolean isReverse();

    DTMAxisIterator cloneIterator();

    void setRestartable(boolean z2);

    int getNodeByPosition(int i2);
}
