package com.sun.xml.internal.org.jvnet.fastinfoset.sax;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/fastinfoset/sax/RestrictedAlphabetContentHandler.class */
public interface RestrictedAlphabetContentHandler {
    void numericCharacters(char[] cArr, int i2, int i3) throws SAXException;

    void dateTimeCharacters(char[] cArr, int i2, int i3) throws SAXException;

    void alphabetCharacters(String str, char[] cArr, int i2, int i3) throws SAXException;
}
