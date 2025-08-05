package com.sun.xml.internal.stream.buffer;

import com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator;
import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/MutableXMLStreamBuffer.class */
public class MutableXMLStreamBuffer extends XMLStreamBuffer {
    public static final int DEFAULT_ARRAY_SIZE = 512;

    public MutableXMLStreamBuffer() {
        this(512);
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public MutableXMLStreamBuffer(int size) {
        this._structure = new FragmentedArray<>(new byte[size]);
        this._structureStrings = new FragmentedArray<>(new String[size]);
        this._contentCharactersBuffer = new FragmentedArray<>(new char[4096]);
        this._contentObjects = new FragmentedArray<>(new Object[size]);
        this._structure.getArray()[0] = -112;
    }

    public void createFromXMLStreamReader(XMLStreamReader reader) throws XMLStreamException {
        reset();
        StreamReaderBufferCreator c2 = new StreamReaderBufferCreator(this);
        c2.create(reader);
    }

    public XMLStreamWriter createFromXMLStreamWriter() {
        reset();
        return new StreamWriterBufferCreator(this);
    }

    public SAXBufferCreator createFromSAXBufferCreator() {
        reset();
        SAXBufferCreator c2 = new SAXBufferCreator();
        c2.setBuffer(this);
        return c2;
    }

    public void createFromXMLReader(XMLReader reader, InputStream in) throws SAXException, IOException {
        createFromXMLReader(reader, in, null);
    }

    public void createFromXMLReader(XMLReader reader, InputStream in, String systemId) throws SAXException, IOException {
        reset();
        SAXBufferCreator c2 = new SAXBufferCreator(this);
        reader.setContentHandler(c2);
        reader.setDTDHandler(c2);
        reader.setProperty("http://xml.org/sax/properties/lexical-handler", c2);
        c2.create(reader, in, systemId);
    }

    public void reset() {
        this._contentObjectsPtr = 0;
        this._contentCharactersBufferPtr = 0;
        this._structureStringsPtr = 0;
        this._structurePtr = 0;
        this._structure.getArray()[0] = -112;
        this._contentObjects.setNext(null);
        Object[] o2 = this._contentObjects.getArray();
        for (int i2 = 0; i2 < o2.length && o2[i2] != null; i2++) {
            o2[i2] = null;
        }
        this.treeCount = 0;
    }

    protected void setHasInternedStrings(boolean hasInternedStrings) {
        this._hasInternedStrings = hasInternedStrings;
    }
}
