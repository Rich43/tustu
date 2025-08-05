package com.sun.org.apache.xpath.internal.axes;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/ContextNodeList.class */
public interface ContextNodeList {
    Node getCurrentNode();

    int getCurrentPos();

    void reset();

    void setShouldCacheNodes(boolean z2);

    void runTo(int i2);

    void setCurrentPos(int i2);

    int size();

    boolean isFresh();

    NodeIterator cloneWithReset() throws CloneNotSupportedException;

    Object clone() throws CloneNotSupportedException;

    int getLast();

    void setLast(int i2);
}
