package com.sun.xml.internal.org.jvnet.fastinfoset.sax.helpers;

import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/helpers/FastInfosetDefaultHandler.class */
public class FastInfosetDefaultHandler extends DefaultHandler implements LexicalHandler, EncodingAlgorithmContentHandler, PrimitiveTypeContentHandler {
    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public void octets(String URI, int algorithm, byte[] b2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public void object(String URI, int algorithm, Object o2) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void booleans(boolean[] b2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void bytes(byte[] b2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void shorts(short[] s2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void ints(int[] i2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void longs(long[] l2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void floats(float[] f2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void doubles(double[] d2, int start, int length) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void uuids(long[] msblsb, int start, int length) throws SAXException {
    }
}
