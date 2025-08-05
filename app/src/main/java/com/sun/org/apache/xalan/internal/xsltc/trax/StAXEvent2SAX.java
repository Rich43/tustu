package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
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

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/StAXEvent2SAX.class */
public class StAXEvent2SAX implements XMLReader, Locator {
    private final XMLEventReader staxEventReader;
    private ContentHandler _sax = null;
    private LexicalHandler _lex = null;
    private SAXImpl _saxImpl = null;
    private String version = null;
    private String encoding = null;

    public StAXEvent2SAX(XMLEventReader staxCore) {
        this.staxEventReader = staxCore;
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

    private void bridge() throws XMLStreamException {
        try {
            int depth = 0;
            boolean startedAtDocument = false;
            XMLEvent event = this.staxEventReader.peek();
            if (!event.isStartDocument() && !event.isStartElement()) {
                throw new IllegalStateException();
            }
            if (event.getEventType() == 7) {
                startedAtDocument = true;
                this.version = ((StartDocument) event).getVersion();
                if (((StartDocument) event).encodingSet()) {
                    this.encoding = ((StartDocument) event).getCharacterEncodingScheme();
                }
                this.staxEventReader.nextEvent();
                event = this.staxEventReader.nextEvent();
            }
            handleStartDocument(event);
            while (event.getEventType() != 1) {
                switch (event.getEventType()) {
                    case 3:
                        handlePI((ProcessingInstruction) event);
                        break;
                    case 4:
                        handleCharacters(event.asCharacters());
                        break;
                    case 5:
                        handleComment();
                        break;
                    case 6:
                        handleSpace();
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    default:
                        throw new InternalError("processing prolog event: " + ((Object) event));
                    case 11:
                        handleDTD();
                        break;
                }
                event = this.staxEventReader.nextEvent();
            }
            do {
                switch (event.getEventType()) {
                    case 1:
                        depth++;
                        handleStartElement(event.asStartElement());
                        break;
                    case 2:
                        handleEndElement(event.asEndElement());
                        depth--;
                        break;
                    case 3:
                        handlePI((ProcessingInstruction) event);
                        break;
                    case 4:
                        handleCharacters(event.asCharacters());
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
                        throw new InternalError("processing event: " + ((Object) event));
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
                event = this.staxEventReader.nextEvent();
            } while (depth != 0);
            if (startedAtDocument) {
                while (event.getEventType() != 8) {
                    switch (event.getEventType()) {
                        case 3:
                            handlePI((ProcessingInstruction) event);
                            break;
                        case 4:
                            handleCharacters(event.asCharacters());
                            break;
                        case 5:
                            handleComment();
                            break;
                        case 6:
                            handleSpace();
                            break;
                        default:
                            throw new InternalError("processing misc event after document element: " + ((Object) event));
                    }
                    event = this.staxEventReader.nextEvent();
                }
            }
            handleEndDocument();
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleEndDocument() throws SAXException {
        this._sax.endDocument();
    }

    private void handleStartDocument(final XMLEvent event) throws SAXException {
        this._sax.setDocumentLocator(new Locator2() { // from class: com.sun.org.apache.xalan.internal.xsltc.trax.StAXEvent2SAX.1
            @Override // org.xml.sax.Locator
            public int getColumnNumber() {
                return event.getLocation().getColumnNumber();
            }

            @Override // org.xml.sax.Locator
            public int getLineNumber() {
                return event.getLocation().getLineNumber();
            }

            @Override // org.xml.sax.Locator
            public String getPublicId() {
                return event.getLocation().getPublicId();
            }

            @Override // org.xml.sax.Locator
            public String getSystemId() {
                return event.getLocation().getSystemId();
            }

            @Override // org.xml.sax.ext.Locator2
            public String getXMLVersion() {
                return StAXEvent2SAX.this.version;
            }

            @Override // org.xml.sax.ext.Locator2
            public String getEncoding() {
                return StAXEvent2SAX.this.encoding;
            }
        });
        this._sax.startDocument();
    }

    private void handlePI(ProcessingInstruction event) throws XMLStreamException {
        try {
            this._sax.processingInstruction(event.getTarget(), event.getData());
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleCharacters(Characters event) throws XMLStreamException {
        try {
            this._sax.characters(event.getData().toCharArray(), 0, event.getData().length());
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleEndElement(EndElement event) throws XMLStreamException {
        QName qName = event.getName();
        String qname = "";
        if (qName.getPrefix() != null && qName.getPrefix().trim().length() != 0) {
            qname = qName.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER;
        }
        try {
            this._sax.endElement(qName.getNamespaceURI(), qName.getLocalPart(), qname + qName.getLocalPart());
            Iterator i2 = event.getNamespaces();
            while (i2.hasNext()) {
                String prefix = (String) i2.next();
                if (prefix == null) {
                    prefix = "";
                }
                this._sax.endPrefixMapping(prefix);
            }
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void handleStartElement(StartElement event) throws XMLStreamException {
        String rawname;
        try {
            Iterator i2 = event.getNamespaces();
            while (i2.hasNext()) {
                String prefix = ((Namespace) i2.next()).getPrefix();
                if (prefix == null) {
                    prefix = "";
                }
                this._sax.startPrefixMapping(prefix, event.getNamespaceURI(prefix));
            }
            QName qName = event.getName();
            String prefix2 = qName.getPrefix();
            if (prefix2 == null || prefix2.length() == 0) {
                rawname = qName.getLocalPart();
            } else {
                rawname = prefix2 + ':' + qName.getLocalPart();
            }
            Attributes saxAttrs = getAttributes(event);
            this._sax.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, saxAttrs);
        } catch (SAXException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private Attributes getAttributes(StartElement event) {
        String str;
        AttributesImpl attrs = new AttributesImpl();
        if (!event.isStartElement()) {
            throw new InternalError("getAttributes() attempting to process: " + ((Object) event));
        }
        Iterator i2 = event.getAttributes();
        while (i2.hasNext()) {
            Attribute staxAttr = (Attribute) i2.next();
            String uri = staxAttr.getName().getNamespaceURI();
            if (uri == null) {
                uri = "";
            }
            String localName = staxAttr.getName().getLocalPart();
            String prefix = staxAttr.getName().getPrefix();
            if (prefix == null || prefix.length() == 0) {
                str = localName;
            } else {
                str = prefix + ':' + localName;
            }
            String qName = str;
            String type = staxAttr.getDTDType();
            String value = staxAttr.getValue();
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
    public void parse(String sysId) throws SAXException, IOException {
        throw new IOException("This method is not yet implemented.");
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
