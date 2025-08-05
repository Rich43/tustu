package com.sun.org.apache.xml.internal.serializer;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ExtendedLexicalHandler.class */
public interface ExtendedLexicalHandler extends LexicalHandler {
    void comment(String str) throws SAXException;
}
