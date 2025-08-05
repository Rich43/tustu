package com.sun.org.apache.xerces.internal.impl;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLStreamFilterImpl.class */
public class XMLStreamFilterImpl implements XMLStreamReader {
    private StreamFilter fStreamFilter;
    private XMLStreamReader fStreamReader;
    private int fCurrentEvent;
    private boolean fEventAccepted;
    private boolean fStreamAdvancedByHasNext = false;

    public XMLStreamFilterImpl(XMLStreamReader reader, StreamFilter filter) {
        this.fStreamFilter = null;
        this.fStreamReader = null;
        this.fEventAccepted = false;
        this.fStreamReader = reader;
        this.fStreamFilter = filter;
        try {
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
            } else {
                findNextEvent();
            }
        } catch (XMLStreamException xs) {
            System.err.println("Error while creating a stream Filter" + ((Object) xs));
        }
    }

    protected void setStreamFilter(StreamFilter sf) {
        this.fStreamFilter = sf;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        if (this.fStreamAdvancedByHasNext && this.fEventAccepted) {
            this.fStreamAdvancedByHasNext = false;
            return this.fCurrentEvent;
        }
        int event = findNextEvent();
        if (event != -1) {
            return event;
        }
        throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        if (this.fStreamAdvancedByHasNext && this.fEventAccepted && (this.fCurrentEvent == 1 || this.fCurrentEvent == 1)) {
            this.fStreamAdvancedByHasNext = false;
            return this.fCurrentEvent;
        }
        int event = findNextTag();
        if (event != -1) {
            return event;
        }
        throw new IllegalStateException("The stream reader has reached the end of the document, or there are no more  items to return");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        if (this.fStreamReader.hasNext()) {
            if (!this.fEventAccepted) {
                int iFindNextEvent = findNextEvent();
                this.fCurrentEvent = iFindNextEvent;
                if (iFindNextEvent == -1) {
                    return false;
                }
                this.fStreamAdvancedByHasNext = true;
                return true;
            }
            return true;
        }
        return false;
    }

    private int findNextEvent() throws XMLStreamException {
        this.fStreamAdvancedByHasNext = false;
        while (this.fStreamReader.hasNext()) {
            this.fCurrentEvent = this.fStreamReader.next();
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
                return this.fCurrentEvent;
            }
        }
        if (this.fCurrentEvent == 8) {
            return this.fCurrentEvent;
        }
        return -1;
    }

    private int findNextTag() throws XMLStreamException {
        this.fStreamAdvancedByHasNext = false;
        while (this.fStreamReader.hasNext()) {
            this.fCurrentEvent = this.fStreamReader.nextTag();
            if (this.fStreamFilter.accept(this.fStreamReader)) {
                this.fEventAccepted = true;
                return this.fCurrentEvent;
            }
        }
        if (this.fCurrentEvent == 8) {
            return this.fCurrentEvent;
        }
        return -1;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        this.fStreamReader.close();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        return this.fStreamReader.getAttributeCount();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int index) {
        return this.fStreamReader.getAttributeName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        return this.fStreamReader.getAttributeNamespace(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        return this.fStreamReader.getAttributePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        return this.fStreamReader.getAttributeType(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        return this.fStreamReader.getAttributeValue(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String namespaceURI, String localName) {
        return this.fStreamReader.getAttributeValue(namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return this.fStreamReader.getCharacterEncodingScheme();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        return this.fStreamReader.getElementText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return this.fStreamReader.getEncoding();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getEventType() {
        return this.fStreamReader.getEventType();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        return this.fStreamReader.getLocalName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return this.fStreamReader.getLocation();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        return this.fStreamReader.getName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public NamespaceContext getNamespaceContext() {
        return this.fStreamReader.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        return this.fStreamReader.getNamespaceCount();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        return this.fStreamReader.getNamespacePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        return this.fStreamReader.getNamespaceURI();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        return this.fStreamReader.getNamespaceURI(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        return this.fStreamReader.getNamespaceURI(prefix);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        return this.fStreamReader.getPIData();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        return this.fStreamReader.getPITarget();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        return this.fStreamReader.getPrefix();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) throws IllegalArgumentException {
        return this.fStreamReader.getProperty(name);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getText() {
        return this.fStreamReader.getText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        return this.fStreamReader.getTextCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        return this.fStreamReader.getTextCharacters(sourceStart, target, targetStart, length);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        return this.fStreamReader.getTextLength();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        return this.fStreamReader.getTextStart();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        return this.fStreamReader.getVersion();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        return this.fStreamReader.hasName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        return this.fStreamReader.hasText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int index) {
        return this.fStreamReader.isAttributeSpecified(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this.fStreamReader.isCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this.fStreamReader.isEndElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return this.fStreamReader.isStandalone();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this.fStreamReader.isStartElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        return this.fStreamReader.isWhiteSpace();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        this.fStreamReader.require(type, namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return this.fStreamReader.standaloneSet();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        return this.fStreamReader.getAttributeLocalName(index);
    }
}
