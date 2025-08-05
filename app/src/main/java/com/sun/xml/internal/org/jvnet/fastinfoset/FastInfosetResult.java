package com.sun.xml.internal.org.jvnet.fastinfoset;

import com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer;
import java.io.OutputStream;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/FastInfosetResult.class */
public class FastInfosetResult extends SAXResult {
    OutputStream _outputStream;

    public FastInfosetResult(OutputStream outputStream) {
        this._outputStream = outputStream;
    }

    @Override // javax.xml.transform.sax.SAXResult
    public ContentHandler getHandler() {
        ContentHandler handler = super.getHandler();
        if (handler == null) {
            handler = new SAXDocumentSerializer();
            setHandler(handler);
        }
        ((SAXDocumentSerializer) handler).setOutputStream(this._outputStream);
        return handler;
    }

    @Override // javax.xml.transform.sax.SAXResult
    public LexicalHandler getLexicalHandler() {
        return (LexicalHandler) getHandler();
    }

    public OutputStream getOutputStream() {
        return this._outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this._outputStream = outputStream;
    }
}
