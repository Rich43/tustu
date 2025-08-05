package jdk.internal.org.xml.sax.helpers;

import java.io.IOException;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.ContentHandler;
import jdk.internal.org.xml.sax.DTDHandler;
import jdk.internal.org.xml.sax.EntityResolver;
import jdk.internal.org.xml.sax.ErrorHandler;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.Locator;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.SAXParseException;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/helpers/DefaultHandler.class */
public class DefaultHandler implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler {
    @Override // jdk.internal.org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) throws IOException, SAXException {
        return null;
    }

    @Override // jdk.internal.org.xml.sax.DTDHandler
    public void notationDecl(String str, String str2, String str3) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String str, String str2, String str3, String str4) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void startPrefixMapping(String str, String str2) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void endPrefixMapping(String str) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int i2, int i3) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void processingInstruction(String str, String str2) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ContentHandler
    public void skippedEntity(String str) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
    }

    @Override // jdk.internal.org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }
}
