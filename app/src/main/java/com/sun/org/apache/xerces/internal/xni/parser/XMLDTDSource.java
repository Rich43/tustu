package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLDTDSource.class */
public interface XMLDTDSource {
    void setDTDHandler(XMLDTDHandler xMLDTDHandler);

    XMLDTDHandler getDTDHandler();
}
