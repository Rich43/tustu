package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLStreamFilterImpl;
import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/stream/XMLInputFactoryImpl.class */
public class XMLInputFactoryImpl extends XMLInputFactory {
    private static final boolean DEBUG = false;
    private PropertyManager fPropertyManager = new PropertyManager(1);
    private XMLStreamReaderImpl fTempReader = null;
    boolean fPropertyChanged = false;
    boolean fReuseInstance = false;

    void initEventReader() {
        this.fPropertyChanged = true;
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(InputStream inputstream) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(inputstream));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(reader));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(source));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(String systemId, InputStream inputstream) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(systemId, inputstream));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(stream, encoding));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
        initEventReader();
        return new XMLEventReaderImpl(createXMLStreamReader(systemId, reader));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createXMLEventReader(XMLStreamReader reader) throws XMLStreamException {
        return new XMLEventReaderImpl(reader);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(InputStream inputstream) throws XMLStreamException {
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, inputstream, (String) null);
        return getXMLStreamReaderImpl(inputSource);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, reader, (String) null);
        return getXMLStreamReaderImpl(inputSource);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(String systemId, Reader reader) throws XMLStreamException {
        XMLInputSource inputSource = new XMLInputSource((String) null, systemId, (String) null, reader, (String) null);
        return getXMLStreamReaderImpl(inputSource);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
        return new XMLStreamReaderImpl(jaxpSourcetoXMLInputSource(source), new PropertyManager(this.fPropertyManager));
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(String systemId, InputStream inputstream) throws XMLStreamException {
        XMLInputSource inputSource = new XMLInputSource((String) null, systemId, (String) null, inputstream, (String) null);
        return getXMLStreamReaderImpl(inputSource);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createXMLStreamReader(InputStream inputstream, String encoding) throws XMLStreamException {
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, inputstream, encoding);
        return getXMLStreamReaderImpl(inputSource);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventAllocator getEventAllocator() {
        return (XMLEventAllocator) getProperty(XMLInputFactory.ALLOCATOR);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLReporter getXMLReporter() {
        return (XMLReporter) this.fPropertyManager.getProperty(XMLInputFactory.REPORTER);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLResolver getXMLResolver() {
        Object object = this.fPropertyManager.getProperty(XMLInputFactory.RESOLVER);
        return (XMLResolver) object;
    }

    @Override // javax.xml.stream.XMLInputFactory
    public void setXMLReporter(XMLReporter xmlreporter) {
        this.fPropertyManager.setProperty(XMLInputFactory.REPORTER, xmlreporter);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public void setXMLResolver(XMLResolver xmlresolver) {
        this.fPropertyManager.setProperty(XMLInputFactory.RESOLVER, xmlresolver);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter) throws XMLStreamException {
        return new EventFilterSupport(reader, filter);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter) throws XMLStreamException {
        if (reader != null && filter != null) {
            return new XMLStreamFilterImpl(reader, filter);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLInputFactory
    public Object getProperty(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Property not supported");
        }
        if (this.fPropertyManager.containsProperty(name)) {
            return this.fPropertyManager.getProperty(name);
        }
        throw new IllegalArgumentException("Property not supported");
    }

    @Override // javax.xml.stream.XMLInputFactory
    public boolean isPropertySupported(String name) {
        if (name == null) {
            return false;
        }
        return this.fPropertyManager.containsProperty(name);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public void setEventAllocator(XMLEventAllocator allocator) {
        this.fPropertyManager.setProperty(XMLInputFactory.ALLOCATOR, allocator);
    }

    @Override // javax.xml.stream.XMLInputFactory
    public void setProperty(String name, Object value) throws IllegalArgumentException {
        if (name == null || value == null || !this.fPropertyManager.containsProperty(name)) {
            throw new IllegalArgumentException("Property " + name + " is not supported");
        }
        if (name == Constants.REUSE_INSTANCE || name.equals(Constants.REUSE_INSTANCE)) {
            this.fReuseInstance = ((Boolean) value).booleanValue();
        } else {
            this.fPropertyChanged = true;
        }
        this.fPropertyManager.setProperty(name, value);
    }

    XMLStreamReader getXMLStreamReaderImpl(XMLInputSource inputSource) throws XMLStreamException {
        if (this.fTempReader == null) {
            this.fPropertyChanged = false;
            XMLStreamReaderImpl xMLStreamReaderImpl = new XMLStreamReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
            this.fTempReader = xMLStreamReaderImpl;
            return xMLStreamReaderImpl;
        }
        if (this.fReuseInstance && this.fTempReader.canReuse() && !this.fPropertyChanged) {
            this.fTempReader.reset();
            this.fTempReader.setInputSource(inputSource);
            this.fPropertyChanged = false;
            return this.fTempReader;
        }
        this.fPropertyChanged = false;
        XMLStreamReaderImpl xMLStreamReaderImpl2 = new XMLStreamReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
        this.fTempReader = xMLStreamReaderImpl2;
        return xMLStreamReaderImpl2;
    }

    XMLInputSource jaxpSourcetoXMLInputSource(Source source) {
        if (source instanceof StreamSource) {
            StreamSource stSource = (StreamSource) source;
            String systemId = stSource.getSystemId();
            String publicId = stSource.getPublicId();
            InputStream istream = stSource.getInputStream();
            Reader reader = stSource.getReader();
            if (istream != null) {
                return new XMLInputSource(publicId, systemId, (String) null, istream, (String) null);
            }
            if (reader != null) {
                return new XMLInputSource(publicId, systemId, (String) null, reader, (String) null);
            }
            return new XMLInputSource(publicId, systemId, null);
        }
        throw new UnsupportedOperationException("Cannot create XMLStreamReader or XMLEventReader from a " + source.getClass().getName());
    }
}
