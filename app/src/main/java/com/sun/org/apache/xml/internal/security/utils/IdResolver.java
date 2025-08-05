package com.sun.org.apache.xml.internal.security.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Deprecated
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/IdResolver.class */
public class IdResolver {
    private IdResolver() {
    }

    public static void registerElementById(Element element, Attr attr) throws DOMException {
        element.setIdAttributeNode(attr, true);
    }

    public static Element getElementById(Document document, String str) {
        return document.getElementById(str);
    }
}
