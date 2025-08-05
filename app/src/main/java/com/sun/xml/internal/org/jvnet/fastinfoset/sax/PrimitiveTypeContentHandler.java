package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/PrimitiveTypeContentHandler.class */
public interface PrimitiveTypeContentHandler {
    void booleans(boolean[] zArr, int i2, int i3) throws SAXException;

    void bytes(byte[] bArr, int i2, int i3) throws SAXException;

    void shorts(short[] sArr, int i2, int i3) throws SAXException;

    void ints(int[] iArr, int i2, int i3) throws SAXException;

    void longs(long[] jArr, int i2, int i3) throws SAXException;

    void floats(float[] fArr, int i2, int i3) throws SAXException;

    void doubles(double[] dArr, int i2, int i3) throws SAXException;

    void uuids(long[] jArr, int i2, int i3) throws SAXException;
}
