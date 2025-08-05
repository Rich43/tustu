package com.sun.org.apache.xml.internal.dtm;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMAxisTraverser.class */
public abstract class DTMAxisTraverser {
    public abstract int next(int i2, int i3);

    public abstract int next(int i2, int i3, int i4);

    public int first(int context) {
        return next(context, context);
    }

    public int first(int context, int extendedTypeID) {
        return next(context, context, extendedTypeID);
    }
}
