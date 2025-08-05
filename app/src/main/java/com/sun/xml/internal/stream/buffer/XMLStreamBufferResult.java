package com.sun.xml.internal.stream.buffer;

import com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/XMLStreamBufferResult.class */
public class XMLStreamBufferResult extends SAXResult {
    protected MutableXMLStreamBuffer _buffer;
    protected SAXBufferCreator _bufferCreator;

    public XMLStreamBufferResult() {
        setXMLStreamBuffer(new MutableXMLStreamBuffer());
    }

    public XMLStreamBufferResult(MutableXMLStreamBuffer buffer) {
        setXMLStreamBuffer(buffer);
    }

    public MutableXMLStreamBuffer getXMLStreamBuffer() {
        return this._buffer;
    }

    public void setXMLStreamBuffer(MutableXMLStreamBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer cannot be null");
        }
        this._buffer = buffer;
        setSystemId(this._buffer.getSystemId());
        if (this._bufferCreator != null) {
            this._bufferCreator.setXMLStreamBuffer(this._buffer);
        }
    }

    @Override // javax.xml.transform.sax.SAXResult
    public ContentHandler getHandler() {
        if (this._bufferCreator == null) {
            this._bufferCreator = new SAXBufferCreator(this._buffer);
            setHandler(this._bufferCreator);
        } else if (super.getHandler() == null) {
            setHandler(this._bufferCreator);
        }
        return this._bufferCreator;
    }

    @Override // javax.xml.transform.sax.SAXResult
    public LexicalHandler getLexicalHandler() {
        return (LexicalHandler) getHandler();
    }
}
