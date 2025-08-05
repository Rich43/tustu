package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/ContentVisitor.class */
interface ContentVisitor {
    void onStartDocument();

    void onEndDocument();

    void onEndTag();

    void onPcdata(StringBuilder sb);

    void onCdata(StringBuilder sb);

    void onStartTag(String str, String str2, Attribute attribute, NamespaceDecl namespaceDecl);

    void onComment(StringBuilder sb);
}
