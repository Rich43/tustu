package com.sun.org.apache.xerces.internal.xni;

import java.util.Enumeration;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/NamespaceContext.class */
public interface NamespaceContext {
    public static final String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();

    void pushContext();

    void popContext();

    boolean declarePrefix(String str, String str2);

    String getURI(String str);

    String getPrefix(String str);

    int getDeclaredPrefixCount();

    String getDeclaredPrefixAt(int i2);

    Enumeration getAllPrefixes();

    void reset();
}
