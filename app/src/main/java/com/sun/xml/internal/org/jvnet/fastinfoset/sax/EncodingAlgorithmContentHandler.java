package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/EncodingAlgorithmContentHandler.class */
public interface EncodingAlgorithmContentHandler {
    void octets(String str, int i2, byte[] bArr, int i3, int i4) throws SAXException;

    void object(String str, int i2, Object obj) throws SAXException;
}
