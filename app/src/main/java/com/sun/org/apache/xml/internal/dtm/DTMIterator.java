package com.sun.org.apache.xml.internal.dtm;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMIterator.class */
public interface DTMIterator {
    public static final short FILTER_ACCEPT = 1;
    public static final short FILTER_REJECT = 2;
    public static final short FILTER_SKIP = 3;

    DTM getDTM(int i2);

    DTMManager getDTMManager();

    int getRoot();

    void setRoot(int i2, Object obj);

    void reset();

    int getWhatToShow();

    boolean getExpandEntityReferences();

    int nextNode();

    int previousNode();

    void detach();

    void allowDetachToRelease(boolean z2);

    int getCurrentNode();

    boolean isFresh();

    void setShouldCacheNodes(boolean z2);

    boolean isMutable();

    int getCurrentPos();

    void runTo(int i2);

    void setCurrentPos(int i2);

    int item(int i2);

    void setItem(int i2, int i3);

    int getLength();

    DTMIterator cloneWithReset() throws CloneNotSupportedException;

    Object clone() throws CloneNotSupportedException;

    boolean isDocOrdered();

    int getAxis();
}
