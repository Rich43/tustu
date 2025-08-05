package com.sun.xml.internal.org.jvnet.staxex;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/XMLStreamReaderEx.class */
public interface XMLStreamReaderEx extends XMLStreamReader {
    CharSequence getPCDATA() throws XMLStreamException;

    @Override // javax.xml.stream.XMLStreamReader
    NamespaceContextEx getNamespaceContext();

    String getElementTextTrim() throws XMLStreamException;
}
