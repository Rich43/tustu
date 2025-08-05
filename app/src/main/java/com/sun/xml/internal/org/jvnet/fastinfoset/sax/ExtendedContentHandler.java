package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/ExtendedContentHandler.class */
public interface ExtendedContentHandler extends ContentHandler {
    void characters(char[] cArr, int i2, int i3, boolean z2) throws SAXException;
}
