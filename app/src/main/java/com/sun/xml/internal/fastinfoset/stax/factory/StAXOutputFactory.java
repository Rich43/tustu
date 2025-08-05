package com.sun.xml.internal.fastinfoset.stax.factory;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer;
import com.sun.xml.internal.fastinfoset.stax.StAXManager;
import com.sun.xml.internal.fastinfoset.stax.events.StAXEventWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/factory/StAXOutputFactory.class */
public class StAXOutputFactory extends XMLOutputFactory {
    private StAXManager _manager;

    public StAXOutputFactory() {
        this._manager = null;
        this._manager = new StAXManager(2);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        return new StAXEventWriter(createXMLStreamWriter(result));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
        return new StAXEventWriter(createXMLStreamWriter(writer));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(OutputStream outputStream) throws XMLStreamException {
        return new StAXEventWriter(createXMLStreamWriter(outputStream));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLEventWriter createXMLEventWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
        return new StAXEventWriter(createXMLStreamWriter(outputStream, encoding));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        if (result instanceof StreamResult) {
            StreamResult streamResult = (StreamResult) result;
            if (streamResult.getWriter() != null) {
                return createXMLStreamWriter(streamResult.getWriter());
            }
            if (streamResult.getOutputStream() != null) {
                return createXMLStreamWriter(streamResult.getOutputStream());
            }
            if (streamResult.getSystemId() != null) {
                FileWriter writer = null;
                boolean isError = true;
                try {
                    try {
                        writer = new FileWriter(new File(streamResult.getSystemId()));
                        XMLStreamWriter streamWriter = createXMLStreamWriter(writer);
                        isError = false;
                        if (0 != 0 && writer != null) {
                            try {
                                writer.close();
                            } catch (IOException e2) {
                            }
                        }
                        return streamWriter;
                    } catch (IOException ie) {
                        throw new XMLStreamException(ie);
                    }
                } finally {
                }
            }
            throw new UnsupportedOperationException();
        }
        FileWriter writer2 = null;
        boolean isError2 = true;
        try {
            try {
                writer2 = new FileWriter(new File(result.getSystemId()));
                XMLStreamWriter streamWriter2 = createXMLStreamWriter(writer2);
                isError2 = false;
                if (0 != 0 && writer2 != null) {
                    try {
                        writer2.close();
                    } catch (IOException e3) {
                    }
                }
                return streamWriter2;
            } catch (IOException ie2) {
                throw new XMLStreamException(ie2);
            }
        } finally {
        }
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException {
        return new StAXDocumentSerializer(outputStream, new StAXManager(this._manager));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream, String encoding) throws XMLStreamException {
        StAXDocumentSerializer serializer = new StAXDocumentSerializer(outputStream, new StAXManager(this._manager));
        serializer.setEncoding(encoding);
        return serializer;
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public Object getProperty(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[]{null}));
        }
        if (this._manager.containsProperty(name)) {
            return this._manager.getProperty(name);
        }
        throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[]{name}));
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public boolean isPropertySupported(String name) {
        if (name == null) {
            return false;
        }
        return this._manager.containsProperty(name);
    }

    @Override // javax.xml.stream.XMLOutputFactory
    public void setProperty(String name, Object value) throws IllegalArgumentException {
        this._manager.setProperty(name, value);
    }
}
