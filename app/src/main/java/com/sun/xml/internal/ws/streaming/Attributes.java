package com.sun.xml.internal.ws.streaming;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/Attributes.class */
public interface Attributes {
    int getLength();

    boolean isNamespaceDeclaration(int i2);

    QName getName(int i2);

    String getURI(int i2);

    String getLocalName(int i2);

    String getPrefix(int i2);

    String getValue(int i2);

    int getIndex(QName qName);

    int getIndex(String str, String str2);

    int getIndex(String str);

    String getValue(QName qName);

    String getValue(String str, String str2);

    String getValue(String str);
}
