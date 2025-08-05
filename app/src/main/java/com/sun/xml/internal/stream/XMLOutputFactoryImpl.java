package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.xml.internal.stream.writers.XMLDOMWriterImpl;
import com.sun.xml.internal.stream.writers.XMLEventWriterImpl;
import com.sun.xml.internal.stream.writers.XMLStreamWriterImpl;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;

/* loaded from: rt.jar:com/sun/xml/internal/stream/XMLOutputFactoryImpl.class */
public class XMLOutputFactoryImpl extends XMLOutputFactory {
    private PropertyManager fPropertyManager = new PropertyManager(2);
    private XMLStreamWriterImpl fStreamWriter = null;
    boolean fReuseInstance = false;
    private static final boolean DEBUG = false;
    private boolean fPropertyChanged;

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(OutputStream outputStream) throws XMLStreamException {
        return createXMLEventWriter(outputStream, null);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
        return new XMLEventWriterImpl(createXMLStreamWriter(outputStream, encoding));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        if ((result instanceof StAXResult) && ((StAXResult) result).getXMLEventWriter() != null) {
            return ((StAXResult) result).getXMLEventWriter();
        }
        return new XMLEventWriterImpl(createXMLStreamWriter(result));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
        return new XMLEventWriterImpl(createXMLStreamWriter(writer));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        if (result instanceof StreamResult) {
            return createXMLStreamWriter((StreamResult) result, (String) null);
        }
        if (result instanceof DOMResult) {
            return new XMLDOMWriterImpl((DOMResult) result);
        }
        if (result instanceof StAXResult) {
            if (((StAXResult) result).getXMLStreamWriter() != null) {
                return ((StAXResult) result).getXMLStreamWriter();
            }
            throw new UnsupportedOperationException("Result of type " + ((Object) result) + " is not supported");
        }
        if (result.getSystemId() != null) {
            return createXMLStreamWriter(new StreamResult(result.getSystemId()));
        }
        throw new UnsupportedOperationException("Result of type " + ((Object) result) + " is not supported. Supported result types are: DOMResult, StAXResult and StreamResult.");
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException {
        return createXMLStreamWriter(toStreamResult(null, writer, null), (String) null);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException {
        return createXMLStreamWriter(outputStream, (String) null);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
        return createXMLStreamWriter(toStreamResult(outputStream, null, null), encoding);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public Object getProperty(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Property not supported");
        }
        if (this.fPropertyManager.containsProperty(name)) {
            return this.fPropertyManager.getProperty(name);
        }
        throw new IllegalArgumentException("Property not supported");
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public boolean isPropertySupported(String name) {
        if (name == null) {
            return false;
        }
        return this.fPropertyManager.containsProperty(name);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public void setProperty(String name, Object value) throws IllegalArgumentException {
        if (name == null || value == null || !this.fPropertyManager.containsProperty(name)) {
            throw new IllegalArgumentException("Property " + name + "is not supported");
        }
        if (name == Constants.REUSE_INSTANCE || name.equals(Constants.REUSE_INSTANCE)) {
            this.fReuseInstance = ((Boolean) value).booleanValue();
            if (this.fReuseInstance) {
                throw new IllegalArgumentException("Property " + name + " is not supported: XMLStreamWriters are not Thread safe");
            }
        } else {
            this.fPropertyChanged = true;
        }
        this.fPropertyManager.setProperty(name, value);
    }

    StreamResult toStreamResult(OutputStream os, Writer writer, String systemId) {
        StreamResult sr = new StreamResult();
        sr.setOutputStream(os);
        sr.setWriter(writer);
        sr.setSystemId(systemId);
        return sr;
    }

    XMLStreamWriter createXMLStreamWriter(StreamResult sr, String encoding) throws XMLStreamException {
        try {
            if (this.fReuseInstance && this.fStreamWriter != null && this.fStreamWriter.canReuse() && !this.fPropertyChanged) {
                this.fStreamWriter.reset();
                this.fStreamWriter.setOutput(sr, encoding);
                return this.fStreamWriter;
            }
            XMLStreamWriterImpl xMLStreamWriterImpl = new XMLStreamWriterImpl(sr, encoding, new PropertyManager(this.fPropertyManager));
            this.fStreamWriter = xMLStreamWriterImpl;
            return xMLStreamWriterImpl;
        } catch (IOException io) {
            throw new XMLStreamException(io);
        }
    }
}
