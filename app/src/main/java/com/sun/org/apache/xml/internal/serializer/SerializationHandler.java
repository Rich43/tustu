package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/SerializationHandler.class */
public interface SerializationHandler extends ExtendedContentHandler, ExtendedLexicalHandler, XSLOutputAttributes, DeclHandler, DTDHandler, ErrorHandler, DOMSerializer, Serializer {
    void setContentHandler(ContentHandler contentHandler);

    void close();

    void serialize(Node node) throws IOException;

    boolean setEscaping(boolean z2) throws SAXException;

    void setIndentAmount(int i2);

    void setTransformer(Transformer transformer);

    Transformer getTransformer();

    void setNamespaceMappings(NamespaceMappings namespaceMappings);

    void flushPending() throws SAXException;

    void setDTDEntityExpansion(boolean z2);

    void setIsStandalone(boolean z2);
}
