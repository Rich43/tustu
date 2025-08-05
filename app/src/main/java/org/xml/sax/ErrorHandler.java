package org.xml.sax;

/* loaded from: rt.jar:org/xml/sax/ErrorHandler.class */
public interface ErrorHandler {
    void warning(SAXParseException sAXParseException) throws SAXException;

    void error(SAXParseException sAXParseException) throws SAXException;

    void fatalError(SAXParseException sAXParseException) throws SAXException;
}
