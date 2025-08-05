package com.sun.istack.internal;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/istack/internal/FragmentContentHandler.class */
public class FragmentContentHandler extends XMLFilterImpl {
    public FragmentContentHandler() {
    }

    public FragmentContentHandler(XMLReader parent) {
        super(parent);
    }

    public FragmentContentHandler(ContentHandler handler) {
        setContentHandler(handler);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }
}
