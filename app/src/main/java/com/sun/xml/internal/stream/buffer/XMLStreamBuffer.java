package com.sun.xml.internal.stream.buffer;

import com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor;
import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/XMLStreamBuffer.class */
public abstract class XMLStreamBuffer {
    protected Map<String, String> _inscopeNamespaces = Collections.emptyMap();
    protected boolean _hasInternedStrings;
    protected FragmentedArray<byte[]> _structure;
    protected int _structurePtr;
    protected FragmentedArray<String[]> _structureStrings;
    protected int _structureStringsPtr;
    protected FragmentedArray<char[]> _contentCharactersBuffer;
    protected int _contentCharactersBufferPtr;
    protected FragmentedArray<Object[]> _contentObjects;
    protected int _contentObjectsPtr;
    protected int treeCount;
    protected String systemId;
    private static final ContextClassloaderLocal<TransformerFactory> trnsformerFactory = new ContextClassloaderLocal<TransformerFactory>() { // from class: com.sun.xml.internal.stream.buffer.XMLStreamBuffer.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.stream.buffer.ContextClassloaderLocal
        public TransformerFactory initialValue() throws Exception {
            return TransformerFactory.newInstance();
        }
    };

    public final boolean isCreated() {
        return this._structure.getArray()[0] != 144;
    }

    public final boolean isFragment() {
        return isCreated() && (this._structure.getArray()[this._structurePtr] & 240) != 16;
    }

    public final boolean isElementFragment() {
        return isCreated() && (this._structure.getArray()[this._structurePtr] & 240) == 32;
    }

    public final boolean isForest() {
        return isCreated() && this.treeCount > 1;
    }

    public final String getSystemId() {
        return this.systemId;
    }

    public final Map<String, String> getInscopeNamespaces() {
        return this._inscopeNamespaces;
    }

    public final boolean hasInternedStrings() {
        return this._hasInternedStrings;
    }

    public final StreamReaderBufferProcessor readAsXMLStreamReader() throws XMLStreamException {
        return new StreamReaderBufferProcessor(this);
    }

    public final void writeToXMLStreamWriter(XMLStreamWriter writer, boolean writeAsFragment) throws XMLStreamException {
        StreamWriterBufferProcessor p2 = new StreamWriterBufferProcessor(this, writeAsFragment);
        p2.process(writer);
    }

    public final void writeToXMLStreamWriter(XMLStreamWriter writer) throws XMLStreamException {
        writeToXMLStreamWriter(writer, isFragment());
    }

    public final SAXBufferProcessor readAsXMLReader() {
        return new SAXBufferProcessor(this, isFragment());
    }

    public final SAXBufferProcessor readAsXMLReader(boolean produceFragmentEvent) {
        return new SAXBufferProcessor(this, produceFragmentEvent);
    }

    public final void writeTo(ContentHandler handler, boolean produceFragmentEvent) throws SAXException {
        SAXBufferProcessor p2 = readAsXMLReader(produceFragmentEvent);
        p2.setContentHandler(handler);
        if (p2 instanceof LexicalHandler) {
            p2.setLexicalHandler((LexicalHandler) handler);
        }
        if (p2 instanceof DTDHandler) {
            p2.setDTDHandler((DTDHandler) handler);
        }
        if (p2 instanceof ErrorHandler) {
            p2.setErrorHandler((ErrorHandler) handler);
        }
        p2.process();
    }

    public final void writeTo(ContentHandler handler) throws SAXException {
        writeTo(handler, isFragment());
    }

    public final void writeTo(ContentHandler handler, ErrorHandler errorHandler, boolean produceFragmentEvent) throws SAXException {
        SAXBufferProcessor p2 = readAsXMLReader(produceFragmentEvent);
        p2.setContentHandler(handler);
        if (p2 instanceof LexicalHandler) {
            p2.setLexicalHandler((LexicalHandler) handler);
        }
        if (p2 instanceof DTDHandler) {
            p2.setDTDHandler((DTDHandler) handler);
        }
        p2.setErrorHandler(errorHandler);
        p2.process();
    }

    public final void writeTo(ContentHandler handler, ErrorHandler errorHandler) throws SAXException {
        writeTo(handler, errorHandler, isFragment());
    }

    public final Node writeTo(Node n2) throws XMLStreamBufferException {
        try {
            Transformer t2 = trnsformerFactory.get().newTransformer();
            t2.transform(new XMLStreamBufferSource(this), new DOMResult(n2));
            return n2.getLastChild();
        } catch (TransformerException e2) {
            throw new XMLStreamBufferException(e2);
        }
    }

    public static XMLStreamBuffer createNewBufferFromXMLStreamReader(XMLStreamReader reader) throws XMLStreamException {
        MutableXMLStreamBuffer b2 = new MutableXMLStreamBuffer();
        b2.createFromXMLStreamReader(reader);
        return b2;
    }

    public static XMLStreamBuffer createNewBufferFromXMLReader(XMLReader reader, InputStream in) throws SAXException, IOException {
        MutableXMLStreamBuffer b2 = new MutableXMLStreamBuffer();
        b2.createFromXMLReader(reader, in);
        return b2;
    }

    public static XMLStreamBuffer createNewBufferFromXMLReader(XMLReader reader, InputStream in, String systemId) throws SAXException, IOException {
        MutableXMLStreamBuffer b2 = new MutableXMLStreamBuffer();
        b2.createFromXMLReader(reader, in, systemId);
        return b2;
    }

    protected final FragmentedArray<byte[]> getStructure() {
        return this._structure;
    }

    protected final int getStructurePtr() {
        return this._structurePtr;
    }

    protected final FragmentedArray<String[]> getStructureStrings() {
        return this._structureStrings;
    }

    protected final int getStructureStringsPtr() {
        return this._structureStringsPtr;
    }

    protected final FragmentedArray<char[]> getContentCharactersBuffer() {
        return this._contentCharactersBuffer;
    }

    protected final int getContentCharactersBufferPtr() {
        return this._contentCharactersBufferPtr;
    }

    protected final FragmentedArray<Object[]> getContentObjects() {
        return this._contentObjects;
    }

    protected final int getContentObjectsPtr() {
        return this._contentObjectsPtr;
    }
}
