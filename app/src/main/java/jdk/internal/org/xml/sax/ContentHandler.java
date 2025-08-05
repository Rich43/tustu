package jdk.internal.org.xml.sax;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/ContentHandler.class */
public interface ContentHandler {
    void setDocumentLocator(Locator locator);

    void startDocument() throws SAXException;

    void endDocument() throws SAXException;

    void startPrefixMapping(String str, String str2) throws SAXException;

    void endPrefixMapping(String str) throws SAXException;

    void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException;

    void endElement(String str, String str2, String str3) throws SAXException;

    void characters(char[] cArr, int i2, int i3) throws SAXException;

    void ignorableWhitespace(char[] cArr, int i2, int i3) throws SAXException;

    void processingInstruction(String str, String str2) throws SAXException;

    void skippedEntity(String str) throws SAXException;
}
