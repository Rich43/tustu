package com.sun.org.apache.xalan.internal.xsltc.runtime.output;

import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2DOM;
import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXEventWriter;
import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/output/TransletOutputHandlerFactory.class */
public class TransletOutputHandlerFactory {
    public static final int STREAM = 0;
    public static final int SAX = 1;
    public static final int DOM = 2;
    public static final int STAX = 3;
    private String _encoding = "utf-8";
    private String _method = null;
    private int _outputType = 0;
    private OutputStream _ostream = System.out;
    private Writer _writer = null;
    private Node _node = null;
    private Node _nextSibling = null;
    private XMLEventWriter _xmlStAXEventWriter = null;
    private XMLStreamWriter _xmlStAXStreamWriter = null;
    private int _indentNumber = -1;
    private ContentHandler _handler = null;
    private LexicalHandler _lexHandler = null;
    private boolean _overrideDefaultParser;

    public static TransletOutputHandlerFactory newInstance() {
        return new TransletOutputHandlerFactory(true);
    }

    public static TransletOutputHandlerFactory newInstance(boolean overrideDefaultParser) {
        return new TransletOutputHandlerFactory(overrideDefaultParser);
    }

    public TransletOutputHandlerFactory(boolean overrideDefaultParser) {
        this._overrideDefaultParser = overrideDefaultParser;
    }

    public void setOutputType(int outputType) {
        this._outputType = outputType;
    }

    public void setEncoding(String encoding) {
        if (encoding != null) {
            this._encoding = encoding;
        }
    }

    public void setOutputMethod(String method) {
        this._method = method;
    }

    public void setOutputStream(OutputStream ostream) {
        this._ostream = ostream;
    }

    public void setWriter(Writer writer) {
        this._writer = writer;
    }

    public void setHandler(ContentHandler handler) {
        this._handler = handler;
    }

    public void setLexicalHandler(LexicalHandler lex) {
        this._lexHandler = lex;
    }

    public void setNode(Node node) {
        this._node = node;
    }

    public Node getNode() {
        if (this._handler instanceof SAX2DOM) {
            return ((SAX2DOM) this._handler).getDOM();
        }
        return null;
    }

    public void setNextSibling(Node nextSibling) {
        this._nextSibling = nextSibling;
    }

    public XMLEventWriter getXMLEventWriter() {
        if (this._handler instanceof SAX2StAXEventWriter) {
            return ((SAX2StAXEventWriter) this._handler).getEventWriter();
        }
        return null;
    }

    public void setXMLEventWriter(XMLEventWriter eventWriter) {
        this._xmlStAXEventWriter = eventWriter;
    }

    public XMLStreamWriter getXMLStreamWriter() {
        if (this._handler instanceof SAX2StAXStreamWriter) {
            return ((SAX2StAXStreamWriter) this._handler).getStreamWriter();
        }
        return null;
    }

    public void setXMLStreamWriter(XMLStreamWriter streamWriter) {
        this._xmlStAXStreamWriter = streamWriter;
    }

    public void setIndentNumber(int value) {
        this._indentNumber = value;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0141  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0172  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.sun.org.apache.xml.internal.serializer.SerializationHandler getSerializationHandler() throws javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 493
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory.getSerializationHandler():com.sun.org.apache.xml.internal.serializer.SerializationHandler");
    }
}
