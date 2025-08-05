package com.sun.rowset.internal;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/* loaded from: rt.jar:com/sun/rowset/internal/XmlResolver.class */
public class XmlResolver implements EntityResolver {
    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) {
        String strSubstring = str2.substring(str2.lastIndexOf("/"));
        if (str2.startsWith("http://java.sun.com/xml/ns/jdbc")) {
            return new InputSource(getClass().getResourceAsStream(strSubstring));
        }
        return null;
    }
}
