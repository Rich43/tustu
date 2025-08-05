package com.sun.org.apache.xerces.internal.xs.datatypes;

import com.sun.org.apache.xerces.internal.xni.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/datatypes/XSQName.class */
public interface XSQName {
    QName getXNIQName();

    javax.xml.namespace.QName getJAXPQName();
}
