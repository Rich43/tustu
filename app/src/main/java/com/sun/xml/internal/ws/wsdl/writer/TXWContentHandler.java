package com.sun.xml.internal.ws.wsdl.writer;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/writer/TXWContentHandler.class */
public class TXWContentHandler implements ContentHandler {
    Stack<TypedXmlWriter> stack = new Stack<>();

    public TXWContentHandler(TypedXmlWriter txw) {
        this.stack.push(txw);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        TypedXmlWriter txw = this.stack.peek()._element(uri, localName, TypedXmlWriter.class);
        this.stack.push(txw);
        if (atts != null) {
            for (int i2 = 0; i2 < atts.getLength(); i2++) {
                String auri = atts.getURI(i2);
                if ("http://www.w3.org/2000/xmlns/".equals(auri)) {
                    if ("xmlns".equals(atts.getLocalName(i2))) {
                        txw._namespace(atts.getValue(i2), "");
                    } else {
                        txw._namespace(atts.getValue(i2), atts.getLocalName(i2));
                    }
                } else if (!"schemaLocation".equals(atts.getLocalName(i2)) || !"".equals(atts.getValue(i2))) {
                    txw._attribute(auri, atts.getLocalName(i2), atts.getValue(i2));
                }
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.stack.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }
}
