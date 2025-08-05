package com.sun.org.apache.xml.internal.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/IncrementalSAXSource.class */
public interface IncrementalSAXSource {
    void setContentHandler(ContentHandler contentHandler);

    void setLexicalHandler(LexicalHandler lexicalHandler);

    void setDTDHandler(DTDHandler dTDHandler);

    Object deliverMoreNodes(boolean z2);

    void startParse(InputSource inputSource) throws SAXException;
}
