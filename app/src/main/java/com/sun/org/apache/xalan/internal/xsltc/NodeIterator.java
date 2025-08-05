package com.sun.org.apache.xalan.internal.xsltc;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/NodeIterator.class */
public interface NodeIterator extends Cloneable {
    public static final int END = -1;

    int next();

    NodeIterator reset();

    int getLast();

    int getPosition();

    void setMark();

    void gotoMark();

    NodeIterator setStartNode(int i2);

    boolean isReverse();

    NodeIterator cloneIterator();

    void setRestartable(boolean z2);
}
