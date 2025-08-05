package com.sun.xml.internal.ws.util.xml;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLStreamReaderFilter.class */
public class XMLStreamReaderFilter implements XMLStreamReaderFactory.RecycleAware, XMLStreamReader {
    protected XMLStreamReader reader;

    public XMLStreamReaderFilter(XMLStreamReader core) {
        this.reader = core;
    }

    @Override // com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.RecycleAware
    public void onRecycled() {
        XMLStreamReaderFactory.recycle(this.reader);
        this.reader = null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        return this.reader.getAttributeCount();
    }

    public int getEventType() {
        return this.reader.getEventType();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        return this.reader.getNamespaceCount();
    }

    public int getTextLength() {
        return this.reader.getTextLength();
    }

    public int getTextStart() {
        return this.reader.getTextStart();
    }

    public int next() throws XMLStreamException {
        return this.reader.next();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        return this.reader.nextTag();
    }

    public void close() throws XMLStreamException {
        this.reader.close();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        return this.reader.hasName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        return this.reader.hasNext();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        return this.reader.hasText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this.reader.isCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this.reader.isEndElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return this.reader.isStandalone();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this.reader.isStartElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        return this.reader.isWhiteSpace();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return this.reader.standaloneSet();
    }

    public char[] getTextCharacters() {
        return this.reader.getTextCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int index) {
        return this.reader.isAttributeSpecified(index);
    }

    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return this.reader.getCharacterEncodingScheme();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        return this.reader.getElementText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return this.reader.getEncoding();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        return this.reader.getLocalName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        return this.reader.getNamespaceURI();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        return this.reader.getPIData();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        return this.reader.getPITarget();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        return this.reader.getPrefix();
    }

    public String getText() {
        return this.reader.getText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        return this.reader.getVersion();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        return this.reader.getAttributeLocalName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        return this.reader.getAttributeNamespace(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        return this.reader.getAttributePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        return this.reader.getAttributeType(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        return this.reader.getAttributeValue(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        return this.reader.getNamespacePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        return this.reader.getNamespaceURI(index);
    }

    public NamespaceContext getNamespaceContext() {
        return this.reader.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        return this.reader.getName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int index) {
        return this.reader.getAttributeName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return this.reader.getLocation();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) throws IllegalArgumentException {
        return this.reader.getProperty(name);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        this.reader.require(type, namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        return this.reader.getNamespaceURI(prefix);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String namespaceURI, String localName) {
        return this.reader.getAttributeValue(namespaceURI, localName);
    }
}
