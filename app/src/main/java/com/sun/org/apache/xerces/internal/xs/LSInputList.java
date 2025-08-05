package com.sun.org.apache.xerces.internal.xs;

import java.util.List;
import org.w3c.dom.ls.LSInput;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/LSInputList.class */
public interface LSInputList extends List {
    int getLength();

    LSInput item(int i2);
}
