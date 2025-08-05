package org.xml.sax;

/* loaded from: rt.jar:org/xml/sax/DocumentHandler.class */
public interface DocumentHandler {
    void setDocumentLocator(Locator locator);

    void startDocument() throws SAXException;

    void endDocument() throws SAXException;

    void startElement(String str, AttributeList attributeList) throws SAXException;

    void endElement(String str) throws SAXException;

    void characters(char[] cArr, int i2, int i3) throws SAXException;

    void ignorableWhitespace(char[] cArr, int i2, int i3) throws SAXException;

    void processingInstruction(String str, String str2) throws SAXException;
}
