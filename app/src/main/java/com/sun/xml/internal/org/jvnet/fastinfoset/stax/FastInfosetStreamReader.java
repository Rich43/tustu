package com.sun.xml.internal.org.jvnet.fastinfoset.stax;

import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/stax/FastInfosetStreamReader.class */
public interface FastInfosetStreamReader {
    int peekNext() throws XMLStreamException;

    int accessNamespaceCount();

    String accessLocalName();

    String accessNamespaceURI();

    String accessPrefix();

    char[] accessTextCharacters();

    int accessTextStart();

    int accessTextLength();
}
