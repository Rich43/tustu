package com.sun.xml.internal.stream.buffer;

import com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor;
import java.io.ByteArrayInputStream;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/XMLStreamBufferSource.class */
public class XMLStreamBufferSource extends SAXSource {
    protected XMLStreamBuffer _buffer;
    protected SAXBufferProcessor _bufferProcessor;

    public XMLStreamBufferSource(XMLStreamBuffer buffer) {
        super(new InputSource(new ByteArrayInputStream(new byte[0])));
        setXMLStreamBuffer(buffer);
    }

    public XMLStreamBuffer getXMLStreamBuffer() {
        return this._buffer;
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer cannot be null");
        }
        this._buffer = buffer;
        if (this._bufferProcessor != null) {
            this._bufferProcessor.setBuffer(this._buffer, false);
        }
    }

    @Override // javax.xml.transform.sax.SAXSource
    public XMLReader getXMLReader() {
        if (this._bufferProcessor == null) {
            this._bufferProcessor = new SAXBufferProcessor(this._buffer, false);
            setXMLReader(this._bufferProcessor);
        } else if (super.getXMLReader() == null) {
            setXMLReader(this._bufferProcessor);
        }
        return this._bufferProcessor;
    }
}
