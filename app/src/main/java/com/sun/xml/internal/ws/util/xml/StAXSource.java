package com.sun.xml.internal.ws.util.xml;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.SAXParseException2;
import com.sun.istack.internal.XMLStreamReaderToContentHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/StAXSource.class */
public class StAXSource extends SAXSource {
    private final XMLStreamReaderToContentHandler reader;
    private final XMLStreamReader staxReader;
    private final XMLFilterImpl repeater;
    private final XMLReader pseudoParser;

    public StAXSource(XMLStreamReader reader, boolean eagerQuit) {
        this(reader, eagerQuit, new String[0]);
    }

    public StAXSource(XMLStreamReader reader, boolean eagerQuit, @NotNull String[] inscope) {
        this.repeater = new XMLFilterImpl();
        this.pseudoParser = new XMLReader() { // from class: com.sun.xml.internal.ws.util.xml.StAXSource.1
            private LexicalHandler lexicalHandler;
            private EntityResolver entityResolver;
            private DTDHandler dtdHandler;
            private ErrorHandler errorHandler;

            @Override // org.xml.sax.XMLReader
            public boolean getFeature(String name) throws SAXNotRecognizedException {
                throw new SAXNotRecognizedException(name);
            }

            @Override // org.xml.sax.XMLReader
            public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
                if (!name.equals("http://xml.org/sax/features/namespaces") || !value) {
                    if (!name.equals("http://xml.org/sax/features/namespace-prefixes") || value) {
                        throw new SAXNotRecognizedException(name);
                    }
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
                StAXSource.this.repeater.setContentHandler(handler);
            }

            @Override // org.xml.sax.XMLReader
            public ContentHandler getContentHandler() {
                return StAXSource.this.repeater.getContentHandler();
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
                    try {
                        StAXSource.this.reader.bridge();
                    } catch (XMLStreamException e2) {
                        SAXParseException se = new SAXParseException2(e2.getMessage(), null, null, e2.getLocation() == null ? -1 : e2.getLocation().getLineNumber(), e2.getLocation() == null ? -1 : e2.getLocation().getColumnNumber(), e2);
                        if (this.errorHandler != null) {
                            this.errorHandler.fatalError(se);
                        }
                        throw se;
                    }
                } finally {
                    try {
                        StAXSource.this.staxReader.close();
                    } catch (XMLStreamException e3) {
                    }
                }
            }
        };
        if (reader == null) {
            throw new IllegalArgumentException();
        }
        this.staxReader = reader;
        int eventType = reader.getEventType();
        if (eventType != 7 && eventType != 1) {
            throw new IllegalStateException();
        }
        this.reader = new XMLStreamReaderToContentHandler(reader, this.repeater, eagerQuit, false, inscope);
        super.setXMLReader(this.pseudoParser);
        super.setInputSource(new InputSource());
    }
}
