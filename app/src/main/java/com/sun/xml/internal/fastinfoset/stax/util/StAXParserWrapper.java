package com.sun.xml.internal.fastinfoset.stax.util;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/util/StAXParserWrapper.class */
public class StAXParserWrapper implements XMLStreamReader {
    private XMLStreamReader _reader;

    public StAXParserWrapper() {
    }

    public StAXParserWrapper(XMLStreamReader reader) {
        this._reader = reader;
    }

    public void setReader(XMLStreamReader reader) {
        this._reader = reader;
    }

    public XMLStreamReader getReader() {
        return this._reader;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        return this._reader.next();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        return this._reader.nextTag();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        return this._reader.getElementText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        this._reader.require(type, namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        return this._reader.hasNext();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        this._reader.close();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        return this._reader.getNamespaceURI(prefix);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public NamespaceContext getNamespaceContext() {
        return this._reader.getNamespaceContext();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this._reader.isStartElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this._reader.isEndElement();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this._reader.isCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        return this._reader.isWhiteSpace();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int index) {
        return this._reader.getAttributeName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        return this._reader.getTextCharacters(sourceStart, target, targetStart, length);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String namespaceUri, String localName) {
        return this._reader.getAttributeValue(namespaceUri, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        return this._reader.getAttributeCount();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        return this._reader.getAttributePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        return this._reader.getAttributeNamespace(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        return this._reader.getAttributeLocalName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        return this._reader.getAttributeType(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        return this._reader.getAttributeValue(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int index) {
        return this._reader.isAttributeSpecified(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        return this._reader.getNamespaceCount();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        return this._reader.getNamespacePrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        return this._reader.getNamespaceURI(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getEventType() {
        return this._reader.getEventType();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getText() {
        return this._reader.getText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        return this._reader.getTextCharacters();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        return this._reader.getTextStart();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        return this._reader.getTextLength();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return this._reader.getEncoding();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        return this._reader.hasText();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return this._reader.getLocation();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        return this._reader.getName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        return this._reader.getLocalName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        return this._reader.hasName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        return this._reader.getNamespaceURI();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        return this._reader.getPrefix();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        return this._reader.getVersion();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return this._reader.isStandalone();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return this._reader.standaloneSet();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return this._reader.getCharacterEncodingScheme();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        return this._reader.getPITarget();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        return this._reader.getPIData();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) {
        return this._reader.getProperty(name);
    }
}
