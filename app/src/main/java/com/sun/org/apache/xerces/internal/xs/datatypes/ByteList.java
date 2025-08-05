package com.sun.org.apache.xerces.internal.xs.datatypes;

import com.sun.org.apache.xerces.internal.xs.XSException;
import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/datatypes/ByteList.class */
public interface ByteList extends List {
    int getLength();

    boolean contains(byte b2);

    byte item(int i2) throws XSException;
}
