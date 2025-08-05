package com.sun.xml.internal.stream.buffer.stax;

import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamWriterBufferCreator.class */
public class StreamWriterBufferCreator extends StreamBufferCreator implements XMLStreamWriterEx {
    private final NamespaceContexHelper namespaceContext = new NamespaceContexHelper();
    private int depth = 0;

    public StreamWriterBufferCreator() {
        setXMLStreamBuffer(new MutableXMLStreamBuffer());
    }

    public StreamWriterBufferCreator(MutableXMLStreamBuffer buffer) {
        setXMLStreamBuffer(buffer);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String str) throws IllegalArgumentException {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx, javax.xml.stream.XMLStreamWriter
    public NamespaceContextEx getNamespaceContext() {
        return this.namespaceContext;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String namespaceURI) throws XMLStreamException {
        setPrefix("", namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String namespaceURI) throws XMLStreamException {
        this.namespaceContext.declareNamespace(prefix, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String namespaceURI) throws XMLStreamException {
        return this.namespaceContext.getPrefix(namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        writeStartDocument("", "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        writeStartDocument("", "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        this.namespaceContext.resetContexts();
        storeStructure(16);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        storeStructure(144);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        this.namespaceContext.pushContext();
        this.depth++;
        String defaultNamespaceURI = this.namespaceContext.getNamespaceURI("");
        if (defaultNamespaceURI == null) {
            storeQualifiedName(32, null, null, localName);
        } else {
            storeQualifiedName(32, null, defaultNamespaceURI, localName);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        this.namespaceContext.pushContext();
        this.depth++;
        String prefix = this.namespaceContext.getPrefix(namespaceURI);
        if (prefix == null) {
            throw new XMLStreamException();
        }
        this.namespaceContext.pushContext();
        storeQualifiedName(32, prefix, namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        this.namespaceContext.pushContext();
        this.depth++;
        storeQualifiedName(32, prefix, namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String localName) throws XMLStreamException {
        writeStartElement(localName);
        writeEndElement();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        writeStartElement(namespaceURI, localName);
        writeEndElement();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        writeStartElement(prefix, localName, namespaceURI);
        writeEndElement();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        this.namespaceContext.popContext();
        storeStructure(144);
        int i2 = this.depth - 1;
        this.depth = i2;
        if (i2 == 0) {
            increaseTreeCount();
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        storeNamespaceAttribute(null, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        if ("xmlns".equals(prefix)) {
            prefix = null;
        }
        storeNamespaceAttribute(prefix, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        storeAttribute(null, null, localName, "CDATA", value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        String prefix = this.namespaceContext.getPrefix(namespaceURI);
        if (prefix == null) {
            throw new XMLStreamException();
        }
        writeAttribute(prefix, namespaceURI, localName, value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        storeAttribute(prefix, namespaceURI, localName, "CDATA", value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String data) throws XMLStreamException {
        storeStructure(88);
        storeContentString(data);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String charData) throws XMLStreamException {
        storeStructure(88);
        storeContentString(charData);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] buf, int start, int len) throws XMLStreamException {
        storeContentCharacters(80, buf, start, len);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String str) throws XMLStreamException {
        storeStructure(104);
        storeContentString(str);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String str) throws XMLStreamException {
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String str) throws XMLStreamException {
        storeStructure(128);
        storeContentString(str);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        writeProcessingInstruction(target, "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        storeProcessingInstruction(target, data);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
    public void writePCDATA(CharSequence charSequence) throws XMLStreamException {
        if (charSequence instanceof Base64Data) {
            storeStructure(92);
            storeContentObject(((Base64Data) charSequence).m2272clone());
        } else {
            writeCharacters(charSequence.toString());
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
    public void writeBinary(byte[] bytes, int offset, int length, String endpointURL) throws XMLStreamException {
        Base64Data d2 = new Base64Data();
        byte[] b2 = new byte[length];
        System.arraycopy(bytes, offset, b2, 0, length);
        d2.set(b2, length, null, true);
        storeStructure(92);
        storeContentObject(d2);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
    public void writeBinary(DataHandler dataHandler) throws XMLStreamException {
        Base64Data d2 = new Base64Data();
        d2.set(dataHandler);
        storeStructure(92);
        storeContentObject(d2);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx
    public OutputStream writeBinary(String endpointURL) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }
}
