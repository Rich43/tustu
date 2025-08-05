package javax.xml.bind.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:javax/xml/bind/util/JAXBSource.class */
public class JAXBSource extends SAXSource {
    private final Marshaller marshaller;
    private final Object contentObject;
    private final XMLReader pseudoParser;

    /* JADX WARN: Illegal instructions before constructor call */
    public JAXBSource(JAXBContext context, Object contentObject) throws JAXBException {
        Marshaller marshallerCreateMarshaller;
        if (context == null) {
            marshallerCreateMarshaller = assertionFailed(Messages.format("JAXBSource.NullContext"));
        } else {
            marshallerCreateMarshaller = context.createMarshaller();
        }
        this(marshallerCreateMarshaller, contentObject == null ? assertionFailed(Messages.format("JAXBSource.NullContent")) : contentObject);
    }

    public JAXBSource(Marshaller marshaller, Object contentObject) throws JAXBException {
        this.pseudoParser = new XMLReader() { // from class: javax.xml.bind.util.JAXBSource.1
            private LexicalHandler lexicalHandler;
            private EntityResolver entityResolver;
            private DTDHandler dtdHandler;
            private XMLFilter repeater = new XMLFilterImpl();
            private ErrorHandler errorHandler;

            @Override // org.xml.sax.XMLReader
            public boolean getFeature(String name) throws SAXNotRecognizedException {
                if (name.equals("http://xml.org/sax/features/namespaces")) {
                    return true;
                }
                if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
                    return false;
                }
                throw new SAXNotRecognizedException(name);
            }

            @Override // org.xml.sax.XMLReader
            public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
                if (name.equals("http://xml.org/sax/features/namespaces") && value) {
                    return;
                }
                if (name.equals("http://xml.org/sax/features/namespace-prefixes") && !value) {
                } else {
                    throw new SAXNotRecognizedException(name);
                }
            }

            @Override // org.xml.sax.XMLReader
            public Object getProperty(String name) throws SAXNotRecognizedException {
                if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                    return this.lexicalHandler;
                }
                throw new SAXNotRecognizedException(name);
            }

            @Override // org.xml.sax.XMLReader
            public void setProperty(String name, Object value) throws SAXNotRecognizedException {
                if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                    this.lexicalHandler = (LexicalHandler) value;
                    return;
                }
                throw new SAXNotRecognizedException(name);
            }

            @Override // org.xml.sax.XMLReader
            public void setEntityResolver(EntityResolver resolver) {
                this.entityResolver = resolver;
            }

            @Override // org.xml.sax.XMLReader
            public EntityResolver getEntityResolver() {
                return this.entityResolver;
            }

            @Override // org.xml.sax.XMLReader
            public void setDTDHandler(DTDHandler handler) {
                this.dtdHandler = handler;
            }

            @Override // org.xml.sax.XMLReader
            public DTDHandler getDTDHandler() {
                return this.dtdHandler;
            }

            @Override // org.xml.sax.XMLReader
            public void setContentHandler(ContentHandler handler) {
                this.repeater.setContentHandler(handler);
            }

            @Override // org.xml.sax.XMLReader
            public ContentHandler getContentHandler() {
                return this.repeater.getContentHandler();
            }

            @Override // org.xml.sax.XMLReader
            public void setErrorHandler(ErrorHandler handler) {
                this.errorHandler = handler;
            }

            @Override // org.xml.sax.XMLReader
            public ErrorHandler getErrorHandler() {
                return this.errorHandler;
            }

            @Override // org.xml.sax.XMLReader
            public void parse(InputSource input) throws SAXException {
                parse();
            }

            @Override // org.xml.sax.XMLReader
            public void parse(String systemId) throws SAXException {
                parse();
            }

            public void parse() throws SAXException {
                try {
                    JAXBSource.this.marshaller.marshal(JAXBSource.this.contentObject, (XMLFilterImpl) this.repeater);
                } catch (JAXBException e2) {
                    SAXParseException se = new SAXParseException(e2.getMessage(), null, null, -1, -1, e2);
                    if (this.errorHandler != null) {
                        this.errorHandler.fatalError(se);
                    }
                    throw se;
                }
            }
        };
        if (marshaller == null) {
            throw new JAXBException(Messages.format("JAXBSource.NullMarshaller"));
        }
        if (contentObject == null) {
            throw new JAXBException(Messages.format("JAXBSource.NullContent"));
        }
        this.marshaller = marshaller;
        this.contentObject = contentObject;
        super.setXMLReader(this.pseudoParser);
        super.setInputSource(new InputSource());
    }

    private static Marshaller assertionFailed(String message) throws JAXBException {
        throw new JAXBException(message);
    }
}
