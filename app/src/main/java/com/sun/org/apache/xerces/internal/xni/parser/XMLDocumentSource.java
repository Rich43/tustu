package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLDocumentSource.class */
public interface XMLDocumentSource {
    void setDocumentHandler(XMLDocumentHandler xMLDocumentHandler);

    XMLDocumentHandler getDocumentHandler();
}
