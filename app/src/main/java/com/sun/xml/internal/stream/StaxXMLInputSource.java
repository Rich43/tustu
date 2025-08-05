package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/StaxXMLInputSource.class */
public class StaxXMLInputSource {
    XMLStreamReader fStreamReader;
    XMLEventReader fEventReader;
    XMLInputSource fInputSource;
    boolean fIsCreatedByResolver;

    public StaxXMLInputSource(XMLStreamReader streamReader, boolean byResolver) {
        this.fIsCreatedByResolver = false;
        this.fStreamReader = streamReader;
    }

    public StaxXMLInputSource(XMLEventReader eventReader, boolean byResolver) {
        this.fIsCreatedByResolver = false;
        this.fEventReader = eventReader;
    }

    public StaxXMLInputSource(XMLInputSource inputSource, boolean byResolver) {
        this.fIsCreatedByResolver = false;
        this.fInputSource = inputSource;
        this.fIsCreatedByResolver = byResolver;
    }

    public XMLStreamReader getXMLStreamReader() {
        return this.fStreamReader;
    }

    public XMLEventReader getXMLEventReader() {
        return this.fEventReader;
    }

    public XMLInputSource getXMLInputSource() {
        return this.fInputSource;
    }

    public boolean hasXMLStreamOrXMLEventReader() {
        return (this.fStreamReader == null && this.fEventReader == null) ? false : true;
    }

    public boolean isCreatedByResolver() {
        return this.fIsCreatedByResolver;
    }
}
