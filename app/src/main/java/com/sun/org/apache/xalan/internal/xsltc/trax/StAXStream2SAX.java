package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/StAXStream2SAX.class */
public class StAXStream2SAX implements XMLReader, Locator {
    private final XMLStreamReader staxStreamReader;
    private ContentHandler _sax = null;
    private LexicalHandler _lex = null;
    private SAXImpl _saxImpl = null;

    public StAXStream2SAX(XMLStreamReader staxSrc) {
        this.staxStreamReader = staxSrc;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this._sax;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) throws NullPointerException {
        this._sax = handler;
        if (handler instanceof LexicalHandler) {
            this._lex = (LexicalHandler) handler;
        }
        if (handler instanceof SAXImpl) {
            this._saxImpl = (SAXImpl) handler;
        }
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource unused) throws SAXException, IOException {
        try {
            bridge();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    public void parse() throws SAXException, XMLStreamException, IOException {
        bridge();
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String sysId) throws SAXException, IOException {
        throw new IOException("This method is not yet implemented.");
    }

    public void bridge() throws XMLStreamException {
        try {
            int depth = 0;
            int event = this.staxStreamReader.getEventType();
            if (event == 7) {
                event = this.staxStreamReader.next();
            }
            if (event != 1) {
                event = this.staxStreamReader.nextTag();
                if (event != 1) {
                    throw new IllegalStateException("The current event is not START_ELEMENT\n but" + event);
                }
            }
            handleStartDocument();
            do {
                switch (event) {
                    case 1:
                        depth++;
                        handleStartElement();
                        break;
                    case 2:
                        handleEndElement();
                        depth--;
                        break;
                    case 3:
                        handlePI();
                        break;
                    case 4:
                        handleCharacters();
                        break;
                    case 5:
                        handleComment();
                        break;
                    case 6:
                        handleSpace();
                        break;
                    case 7:
                    case 8:
                    default:
                        throw new InternalError("processing event: " + event);
                    case 9:
                        handleEntityReference();
                        break;
                    case 10:
                        handleAttribute();
                        break;
                    case 11:
                        handleDTD();
                        break;
                    case 12:
                        handleCDATA();
                        break;
                    case 13:
                        handleNamespace();
                        break;
                    case 14:
                        handleNotationDecl();
                        break;
                    case 15:
                        handleEntityDecl();
                        break;
                }
                event = this.staxStreamReader.next();
            } while (depth != 0);
            handleEndDocument();
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleEndDocument() throws SAXException {
        this._sax.endDocument();
    }

    private void handleStartDocument() throws SAXException {
        this._sax.setDocumentLocator(new Locator2() { // from class: com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX.1
            @Override // org.xml.sax.Locator
            public int getColumnNumber() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getColumnNumber();
            }

            @Override // org.xml.sax.Locator
            public int getLineNumber() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getLineNumber();
            }

            @Override // org.xml.sax.Locator
            public String getPublicId() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getPublicId();
            }

            @Override // org.xml.sax.Locator
            public String getSystemId() {
                return StAXStream2SAX.this.staxStreamReader.getLocation().getSystemId();
            }

            @Override // org.xml.sax.ext.Locator2
            public String getXMLVersion() {
                return StAXStream2SAX.this.staxStreamReader.getVersion();
            }

            @Override // org.xml.sax.ext.Locator2
            public String getEncoding() {
                return StAXStream2SAX.this.staxStreamReader.getEncoding();
            }
        });
        this._sax.startDocument();
    }

    private void handlePI() throws XMLStreamException {
        try {
            this._sax.processingInstruction(this.staxStreamReader.getPITarget(), this.staxStreamReader.getPIData());
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleCharacters() throws XMLStreamException {
        int textLength = this.staxStreamReader.getTextLength();
        char[] chars = new char[textLength];
        this.staxStreamReader.getTextCharacters(0, chars, 0, textLength);
        try {
            this._sax.characters(chars, 0, chars.length);
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleEndElement() throws XMLStreamException {
        QName qName = this.staxStreamReader.getName();
        try {
            String qname = "";
            if (qName.getPrefix() != null && qName.getPrefix().trim().length() != 0) {
                qname = qName.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER;
            }
            this._sax.endElement(qName.getNamespaceURI(), qName.getLocalPart(), qname + qName.getLocalPart());
            int nsCount = this.staxStreamReader.getNamespaceCount();
            for (int i2 = nsCount - 1; i2 >= 0; i2--) {
                String prefix = this.staxStreamReader.getNamespacePrefix(i2);
                if (prefix == null) {
                    prefix = "";
                }
                this._sax.endPrefixMapping(prefix);
            }
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleStartElement() throws XMLStreamException {
        String rawname;
        try {
            int nsCount = this.staxStreamReader.getNamespaceCount();
            for (int i2 = 0; i2 < nsCount; i2++) {
                String prefix = this.staxStreamReader.getNamespacePrefix(i2);
                if (prefix == null) {
                    prefix = "";
                }
                this._sax.startPrefixMapping(prefix, this.staxStreamReader.getNamespaceURI(i2));
            }
            QName qName = this.staxStreamReader.getName();
            String prefix2 = qName.getPrefix();
            if (prefix2 == null || prefix2.length() == 0) {
                rawname = qName.getLocalPart();
            } else {
                rawname = prefix2 + ':' + qName.getLocalPart();
            }
            Attributes attrs = getAttributes();
            this._sax.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, attrs);
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private Attributes getAttributes() {
        String str;
        AttributesImpl attrs = new AttributesImpl();
        int eventType = this.staxStreamReader.getEventType();
        if (eventType != 10 && eventType != 1) {
            throw new InternalError("getAttributes() attempting to process: " + eventType);
        }
        for (int i2 = 0; i2 < this.staxStreamReader.getAttributeCount(); i2++) {
            String uri = this.staxStreamReader.getAttributeNamespace(i2);
            if (uri == null) {
                uri = "";
            }
            String localName = this.staxStreamReader.getAttributeLocalName(i2);
            String prefix = this.staxStreamReader.getAttributePrefix(i2);
            if (prefix == null || prefix.length() == 0) {
                str = localName;
            } else {
                str = prefix + ':' + localName;
            }
            String qName = str;
            String type = this.staxStreamReader.getAttributeType(i2);
            String value = this.staxStreamReader.getAttributeValue(i2);
            attrs.addAttribute(uri, localName, qName, type, value);
        }
        return attrs;
    }

    private void handleNamespace() {
    }

    private void handleAttribute() {
    }

    private void handleDTD() {
    }

    private void handleComment() {
    }

    private void handleEntityReference() {
    }

    private void handleSpace() {
    }

    private void handleNotationDecl() {
    }

    private void handleEntityDecl() {
    }

    private void handleCDATA() {
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) throws NullPointerException {
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return null;
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return 0;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return 0;
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return null;
    }
}
