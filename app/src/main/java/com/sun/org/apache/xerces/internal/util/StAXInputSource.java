package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/StAXInputSource.class */
public final class StAXInputSource extends XMLInputSource {
    private final XMLStreamReader fStreamReader;
    private final XMLEventReader fEventReader;
    private final boolean fConsumeRemainingContent;

    public StAXInputSource(XMLStreamReader source) {
        this(source, false);
    }

    public StAXInputSource(XMLStreamReader source, boolean consumeRemainingContent) {
        super(null, source.getLocation().getSystemId(), null);
        if (source == null) {
            throw new IllegalArgumentException("XMLStreamReader parameter cannot be null.");
        }
        this.fStreamReader = source;
        this.fEventReader = null;
        this.fConsumeRemainingContent = consumeRemainingContent;
    }

    public StAXInputSource(XMLEventReader source) {
        this(source, false);
    }

    public StAXInputSource(XMLEventReader source, boolean consumeRemainingContent) {
        super(null, getEventReaderSystemId(source), null);
        if (source == null) {
            throw new IllegalArgumentException("XMLEventReader parameter cannot be null.");
        }
        this.fStreamReader = null;
        this.fEventReader = source;
        this.fConsumeRemainingContent = consumeRemainingContent;
    }

    public XMLStreamReader getXMLStreamReader() {
        return this.fStreamReader;
    }

    public XMLEventReader getXMLEventReader() {
        return this.fEventReader;
    }

    public boolean shouldConsumeRemainingContent() {
        return this.fConsumeRemainingContent;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setSystemId(String systemId) {
        throw new UnsupportedOperationException("Cannot set the system ID on a StAXInputSource");
    }

    private static String getEventReaderSystemId(XMLEventReader reader) {
        if (reader != null) {
            try {
                return reader.peek().getLocation().getSystemId();
            } catch (XMLStreamException e2) {
                return null;
            }
        }
        return null;
    }
}
