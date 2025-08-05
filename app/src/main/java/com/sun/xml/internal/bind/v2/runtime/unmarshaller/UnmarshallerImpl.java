package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.IDResolver;
import com.sun.xml.internal.bind.api.ClassResolver;
import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.util.XmlFactory;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallerImpl.class */
public final class UnmarshallerImpl extends AbstractUnmarshallerImpl implements ValidationEventHandler, Closeable {
    protected final JAXBContextImpl context;
    private Schema schema;
    public final UnmarshallingContext coordinator;
    private Unmarshaller.Listener externalListener;
    private AttachmentUnmarshaller attachmentUnmarshaller;
    private IDResolver idResolver = new DefaultIDResolver();
    private XMLReader reader = null;
    private static final DefaultHandler dummyHandler = new DefaultHandler();
    public static final String FACTORY = "com.sun.xml.internal.bind.ObjectFactory";

    public UnmarshallerImpl(JAXBContextImpl context, AssociationMap assoc) {
        this.context = context;
        this.coordinator = new UnmarshallingContext(this, assoc);
        try {
            setEventHandler(this);
        } catch (JAXBException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // javax.xml.bind.Unmarshaller
    public UnmarshallerHandler getUnmarshallerHandler() {
        return getUnmarshallerHandler(true, null);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl
    protected XMLReader getXMLReader() throws IllegalStateException, JAXBException {
        if (this.reader == null) {
            try {
                SAXParserFactory parserFactory = XmlFactory.createParserFactory(this.context.disableSecurityProcessing);
                parserFactory.setValidating(false);
                this.reader = parserFactory.newSAXParser().getXMLReader();
            } catch (ParserConfigurationException e2) {
                throw new JAXBException(e2);
            } catch (SAXException e3) {
                throw new JAXBException(e3);
            }
        }
        return this.reader;
    }

    private SAXConnector getUnmarshallerHandler(boolean intern, JaxBeanInfo expectedType) {
        XmlVisitor h2 = createUnmarshallerHandler(null, false, expectedType);
        if (intern) {
            h2 = new InterningXmlVisitor(h2);
        }
        return new SAXConnector(h2, null);
    }

    public final XmlVisitor createUnmarshallerHandler(InfosetScanner scanner, boolean inplace, JaxBeanInfo expectedType) {
        this.coordinator.reset(scanner, inplace, expectedType, this.idResolver);
        XmlVisitor unmarshaller = this.coordinator;
        if (this.schema != null) {
            unmarshaller = new ValidatingUnmarshaller(this.schema, unmarshaller);
        }
        if (this.attachmentUnmarshaller != null && this.attachmentUnmarshaller.isXOPPackage()) {
            unmarshaller = new MTOMDecorator(this, unmarshaller, this.attachmentUnmarshaller);
        }
        return unmarshaller;
    }

    public static boolean needsInterning(XMLReader reader) {
        try {
            reader.setFeature("http://xml.org/sax/features/string-interning", true);
        } catch (SAXException e2) {
        }
        try {
            if (reader.getFeature("http://xml.org/sax/features/string-interning")) {
                return false;
            }
            return true;
        } catch (SAXException e3) {
            return true;
        }
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl
    protected Object unmarshal(XMLReader reader, InputSource source) throws JAXBException {
        return unmarshal0(reader, source, null);
    }

    protected <T> JAXBElement<T> unmarshal(XMLReader reader, InputSource source, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) unmarshal0(reader, source, getBeanInfo(expectedType));
    }

    private Object unmarshal0(XMLReader reader, InputSource source, JaxBeanInfo expectedType) throws IllegalStateException, JAXBException {
        SAXConnector connector = getUnmarshallerHandler(needsInterning(reader), expectedType);
        reader.setContentHandler(connector);
        reader.setErrorHandler(this.coordinator);
        try {
            reader.parse(source);
            Object result = connector.getResult();
            reader.setContentHandler(dummyHandler);
            reader.setErrorHandler(dummyHandler);
            return result;
        } catch (IOException e2) {
            this.coordinator.clearStates();
            throw new UnmarshalException(e2);
        } catch (SAXException e3) {
            this.coordinator.clearStates();
            throw createUnmarshalException(e3);
        }
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <T> JAXBElement<T> unmarshal(Source source, Class<T> expectedType) throws IllegalStateException, JAXBException {
        if (source instanceof SAXSource) {
            SAXSource ss = (SAXSource) source;
            XMLReader locReader = ss.getXMLReader();
            if (locReader == null) {
                locReader = getXMLReader();
            }
            return unmarshal(locReader, ss.getInputSource(), expectedType);
        }
        if (source instanceof StreamSource) {
            return unmarshal(getXMLReader(), streamSourceToInputSource((StreamSource) source), expectedType);
        }
        if (source instanceof DOMSource) {
            return unmarshal(((DOMSource) source).getNode(), expectedType);
        }
        throw new IllegalArgumentException();
    }

    public Object unmarshal0(Source source, JaxBeanInfo expectedType) throws IllegalStateException, JAXBException {
        if (source instanceof SAXSource) {
            SAXSource ss = (SAXSource) source;
            XMLReader locReader = ss.getXMLReader();
            if (locReader == null) {
                locReader = getXMLReader();
            }
            return unmarshal0(locReader, ss.getInputSource(), expectedType);
        }
        if (source instanceof StreamSource) {
            return unmarshal0(getXMLReader(), streamSourceToInputSource((StreamSource) source), expectedType);
        }
        if (source instanceof DOMSource) {
            return unmarshal0(((DOMSource) source).getNode(), expectedType);
        }
        throw new IllegalArgumentException();
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public final ValidationEventHandler getEventHandler() {
        try {
            return super.getEventHandler();
        } catch (JAXBException e2) {
            throw new AssertionError();
        }
    }

    public final boolean hasEventHandler() {
        return getEventHandler() != this;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <T> JAXBElement<T> unmarshal(Node node, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) unmarshal0(node, getBeanInfo(expectedType));
    }

    @Override // javax.xml.bind.Unmarshaller
    public final Object unmarshal(Node node) throws JAXBException {
        return unmarshal0(node, (JaxBeanInfo) null);
    }

    @Deprecated
    public final Object unmarshal(SAXSource source) throws JAXBException {
        return super.unmarshal((Source) source);
    }

    public final Object unmarshal0(Node node, JaxBeanInfo expectedType) throws JAXBException {
        try {
            DOMScanner scanner = new DOMScanner();
            InterningXmlVisitor handler = new InterningXmlVisitor(createUnmarshallerHandler(null, false, expectedType));
            scanner.setContentHandler(new SAXConnector(handler, scanner));
            if (node.getNodeType() == 1) {
                scanner.scan((Element) node);
            } else if (node.getNodeType() == 9) {
                scanner.scan((Document) node);
            } else {
                throw new IllegalArgumentException("Unexpected node type: " + ((Object) node));
            }
            Object retVal = handler.getContext().getResult();
            handler.getContext().clearResult();
            return retVal;
        } catch (SAXException e2) {
            throw createUnmarshalException(e2);
        }
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public Object unmarshal(XMLStreamReader reader) throws JAXBException {
        return unmarshal0(reader, (JaxBeanInfo) null);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <T> JAXBElement<T> unmarshal(XMLStreamReader reader, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) unmarshal0(reader, getBeanInfo(expectedType));
    }

    public Object unmarshal0(XMLStreamReader reader, JaxBeanInfo expectedType) throws JAXBException {
        if (reader == null) {
            throw new IllegalArgumentException(com.sun.xml.internal.bind.unmarshaller.Messages.format(com.sun.xml.internal.bind.unmarshaller.Messages.NULL_READER));
        }
        int eventType = reader.getEventType();
        if (eventType != 1 && eventType != 7) {
            throw new IllegalStateException(com.sun.xml.internal.bind.unmarshaller.Messages.format(com.sun.xml.internal.bind.unmarshaller.Messages.ILLEGAL_READER_STATE, Integer.valueOf(eventType)));
        }
        XmlVisitor h2 = createUnmarshallerHandler(null, false, expectedType);
        StAXConnector connector = StAXStreamConnector.create(reader, h2);
        try {
            connector.bridge();
            Object retVal = h2.getContext().getResult();
            h2.getContext().clearResult();
            return retVal;
        } catch (XMLStreamException e2) {
            throw handleStreamException(e2);
        }
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <T> JAXBElement<T> unmarshal(XMLEventReader reader, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) unmarshal0(reader, getBeanInfo(expectedType));
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public Object unmarshal(XMLEventReader reader) throws JAXBException {
        return unmarshal0(reader, (JaxBeanInfo) null);
    }

    private Object unmarshal0(XMLEventReader reader, JaxBeanInfo expectedType) throws JAXBException {
        if (reader == null) {
            throw new IllegalArgumentException(com.sun.xml.internal.bind.unmarshaller.Messages.format(com.sun.xml.internal.bind.unmarshaller.Messages.NULL_READER));
        }
        try {
            XMLEvent event = reader.peek();
            if (!event.isStartElement() && !event.isStartDocument()) {
                throw new IllegalStateException(com.sun.xml.internal.bind.unmarshaller.Messages.format(com.sun.xml.internal.bind.unmarshaller.Messages.ILLEGAL_READER_STATE, Integer.valueOf(event.getEventType())));
            }
            boolean isZephyr = reader.getClass().getName().equals("com.sun.xml.internal.stream.XMLReaderImpl");
            XmlVisitor h2 = createUnmarshallerHandler(null, false, expectedType);
            if (!isZephyr) {
                h2 = new InterningXmlVisitor(h2);
            }
            new StAXEventConnector(reader, h2).bridge();
            return h2.getContext().getResult();
        } catch (XMLStreamException e2) {
            throw handleStreamException(e2);
        }
    }

    public Object unmarshal0(InputStream input, JaxBeanInfo expectedType) throws JAXBException {
        return unmarshal0(getXMLReader(), new InputSource(input), expectedType);
    }

    private static JAXBException handleStreamException(XMLStreamException e2) {
        Throwable ne = e2.getNestedException();
        if (ne instanceof JAXBException) {
            return (JAXBException) ne;
        }
        if (ne instanceof SAXException) {
            return new UnmarshalException(ne);
        }
        return new UnmarshalException(e2);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public Object getProperty(String name) throws PropertyException {
        if (name.equals(IDResolver.class.getName())) {
            return this.idResolver;
        }
        return super.getProperty(name);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public void setProperty(String name, Object value) throws PropertyException {
        if (name.equals(FACTORY)) {
            this.coordinator.setFactories(value);
            return;
        }
        if (name.equals(IDResolver.class.getName())) {
            this.idResolver = (IDResolver) value;
            return;
        }
        if (name.equals(ClassResolver.class.getName())) {
            this.coordinator.classResolver = (ClassResolver) value;
        } else if (name.equals(ClassLoader.class.getName())) {
            this.coordinator.classLoader = (ClassLoader) value;
        } else {
            super.setProperty(name, value);
        }
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public Schema getSchema() {
        return this.schema;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public AttachmentUnmarshaller getAttachmentUnmarshaller() {
        return this.attachmentUnmarshaller;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public void setAttachmentUnmarshaller(AttachmentUnmarshaller au2) {
        this.attachmentUnmarshaller = au2;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public boolean isValidating() {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public void setValidating(boolean validating) {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        this.coordinator.putAdapter(type, adapter);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public <A extends XmlAdapter> A getAdapter(Class<A> cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (this.coordinator.containsAdapter(cls)) {
            return (A) this.coordinator.getAdapter(cls);
        }
        return null;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl
    public UnmarshalException createUnmarshalException(SAXException e2) {
        return super.createUnmarshalException(e2);
    }

    @Override // javax.xml.bind.ValidationEventHandler
    public boolean handleEvent(ValidationEvent event) {
        return event.getSeverity() != 2;
    }

    private static InputSource streamSourceToInputSource(StreamSource ss) {
        InputSource is = new InputSource();
        is.setSystemId(ss.getSystemId());
        is.setByteStream(ss.getInputStream());
        is.setCharacterStream(ss.getReader());
        return is;
    }

    public <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz) throws JAXBException {
        return this.context.getBeanInfo((Class) clazz, true);
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public Unmarshaller.Listener getListener() {
        return this.externalListener;
    }

    @Override // javax.xml.bind.helpers.AbstractUnmarshallerImpl, javax.xml.bind.Unmarshaller
    public void setListener(Unmarshaller.Listener listener) {
        this.externalListener = listener;
    }

    public UnmarshallingContext getContext() {
        return this.coordinator;
    }

    protected void finalize() throws Throwable {
        try {
            ClassFactory.cleanCache();
        } finally {
            super.finalize();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        ClassFactory.cleanCache();
    }
}
