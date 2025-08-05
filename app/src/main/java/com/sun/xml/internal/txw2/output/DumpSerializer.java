package com.sun.xml.internal.txw2.output;

import java.io.PrintStream;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/DumpSerializer.class */
public class DumpSerializer implements XmlSerializer {
    private final PrintStream out;

    public DumpSerializer(PrintStream out) {
        this.out = out;
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        this.out.println('<' + prefix + ':' + localName);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        this.out.println('@' + prefix + ':' + localName + '=' + ((Object) value));
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        this.out.println("xmlns:" + prefix + '=' + uri);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
        this.out.println('>');
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endTag() {
        this.out.println("</  >");
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        this.out.println(text);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        this.out.println("<![CDATA[");
        this.out.println(text);
        this.out.println("]]>");
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        this.out.println("<!--");
        this.out.println(comment);
        this.out.println("-->");
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void startDocument() {
        this.out.println("<?xml?>");
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endDocument() {
        this.out.println("done");
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void flush() {
        this.out.println("flush");
    }
}
