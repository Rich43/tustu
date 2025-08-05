package com.sun.xml.internal.ws.message;

import com.sun.xml.internal.ws.api.message.Message;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/XMLReaderImpl.class */
final class XMLReaderImpl extends XMLFilterImpl {
    private final Message msg;
    private static final ContentHandler DUMMY = new DefaultHandler();
    protected static final InputSource THE_SOURCE = new InputSource();

    XMLReaderImpl(Message msg) {
        this.msg = msg;
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void parse(String systemId) {
        reportError();
    }

    private void reportError() {
        throw new IllegalStateException("This is a special XMLReader implementation that only works with the InputSource given in SAXSource.");
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException {
        if (input != THE_SOURCE) {
            reportError();
        }
        this.msg.writeTo(this, this);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        if (super.getContentHandler() == DUMMY) {
            return null;
        }
        return super.getContentHandler();
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) {
        if (contentHandler == null) {
            contentHandler = DUMMY;
        }
        super.setContentHandler(contentHandler);
    }
}
