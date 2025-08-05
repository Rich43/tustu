package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLEntityDescription.class */
public interface XMLEntityDescription extends XMLResourceIdentifier {
    void setEntityName(String str);

    String getEntityName();
}
