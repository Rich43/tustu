package com.sun.xml.internal.txw2.output;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/XmlSerializer.class */
public interface XmlSerializer {
    void startDocument();

    void beginStartTag(String str, String str2, String str3);

    void writeAttribute(String str, String str2, String str3, StringBuilder sb);

    void writeXmlns(String str, String str2);

    void endStartTag(String str, String str2, String str3);

    void endTag();

    void text(StringBuilder sb);

    void cdata(StringBuilder sb);

    void comment(StringBuilder sb);

    void endDocument();

    void flush();
}
