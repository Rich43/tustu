package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/XmlOutput.class */
public interface XmlOutput {
    void startDocument(XMLSerializer xMLSerializer, boolean z2, int[] iArr, NamespaceContextImpl namespaceContextImpl) throws SAXException, XMLStreamException, IOException;

    void endDocument(boolean z2) throws SAXException, XMLStreamException, IOException;

    void beginStartTag(Name name) throws XMLStreamException, IOException;

    void beginStartTag(int i2, String str) throws XMLStreamException, IOException;

    void attribute(Name name, String str) throws XMLStreamException, IOException;

    void attribute(int i2, String str, String str2) throws XMLStreamException, IOException;

    void endStartTag() throws SAXException, IOException;

    void endTag(Name name) throws SAXException, XMLStreamException, IOException;

    void endTag(int i2, String str) throws SAXException, XMLStreamException, IOException;

    void text(String str, boolean z2) throws SAXException, XMLStreamException, IOException;

    void text(Pcdata pcdata, boolean z2) throws SAXException, XMLStreamException, IOException;
}
