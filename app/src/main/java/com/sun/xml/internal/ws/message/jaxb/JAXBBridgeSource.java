package com.sun.xml.internal.ws.message.jaxb;

import com.sun.xml.internal.ws.spi.db.XMLBridge;
import javax.xml.bind.JAXBException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/jaxb/JAXBBridgeSource.class */
final class JAXBBridgeSource extends SAXSource {
    private final XMLBridge bridge;
    private final Object contentObject;
    private final XMLReader pseudoParser = new XMLFilterImpl() { // from class: com.sun.xml.internal.ws.message.jaxb.JAXBBridgeSource.1
        private LexicalHandler lexicalHandler;

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public boolean getFeature(String name) throws SAXNotRecognizedException {
            if (name.equals("http://xml.org/sax/features/namespaces")) {
                return true;
            }
            if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
                return false;
            }
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
            if (name.equals("http://xml.org/sax/features/namespaces") && value) {
                return;
            }
            if (name.equals("http://xml.org/sax/features/namespace-prefixes") && !value) {
            } else {
                throw new SAXNotRecognizedException(name);
            }
        }

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public Object getProperty(String name) throws SAXNotRecognizedException {
            if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                return this.lexicalHandler;
            }
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public void setProperty(String name, Object value) throws SAXNotRecognizedException {
            if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                this.lexicalHandler = (LexicalHandler) value;
                return;
            }
            throw new SAXNotRecognizedException(name);
        }

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public void parse(InputSource input) throws SAXException {
            parse();
        }

        @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
        public void parse(String systemId) throws SAXException {
            parse();
        }

        public void parse() throws SAXException {
            try {
                startDocument();
                JAXBBridgeSource.this.bridge.marshal((XMLBridge) JAXBBridgeSource.this.contentObject, (ContentHandler) this, (AttachmentMarshaller) null);
                endDocument();
            } catch (JAXBException e2) {
                SAXParseException se = new SAXParseException(e2.getMessage(), null, null, -1, -1, e2);
                fatalError(se);
                throw se;
            }
        }
    };

    public JAXBBridgeSource(XMLBridge bridge, Object contentObject) {
        this.bridge = bridge;
        this.contentObject = contentObject;
        super.setXMLReader(this.pseudoParser);
        super.setInputSource(new InputSource());
    }
}
