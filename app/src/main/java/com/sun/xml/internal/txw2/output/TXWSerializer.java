package com.sun.xml.internal.txw2.output;

import com.sun.xml.internal.txw2.TypedXmlWriter;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/TXWSerializer.class */
public final class TXWSerializer implements XmlSerializer {
    public final TypedXmlWriter txw;

    public TXWSerializer(TypedXmlWriter txw) {
        this.txw = txw;
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void startDocument() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endDocument() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endTag() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void flush() {
        throw new UnsupportedOperationException();
    }
}
