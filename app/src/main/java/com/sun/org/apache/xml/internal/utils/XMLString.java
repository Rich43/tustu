package com.sun.org.apache.xml.internal.utils;

import java.util.Locale;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLString.class */
public interface XMLString {
    void dispatchCharactersEvents(ContentHandler contentHandler) throws SAXException;

    void dispatchAsComment(LexicalHandler lexicalHandler) throws SAXException;

    XMLString fixWhiteSpace(boolean z2, boolean z3, boolean z4);

    int length();

    char charAt(int i2);

    void getChars(int i2, int i3, char[] cArr, int i4);

    boolean equals(XMLString xMLString);

    boolean equals(String str);

    boolean equals(Object obj);

    boolean equalsIgnoreCase(String str);

    int compareTo(XMLString xMLString);

    int compareToIgnoreCase(XMLString xMLString);

    boolean startsWith(String str, int i2);

    boolean startsWith(XMLString xMLString, int i2);

    boolean startsWith(String str);

    boolean startsWith(XMLString xMLString);

    boolean endsWith(String str);

    int hashCode();

    int indexOf(int i2);

    int indexOf(int i2, int i3);

    int lastIndexOf(int i2);

    int lastIndexOf(int i2, int i3);

    int indexOf(String str);

    int indexOf(XMLString xMLString);

    int indexOf(String str, int i2);

    int lastIndexOf(String str);

    int lastIndexOf(String str, int i2);

    XMLString substring(int i2);

    XMLString substring(int i2, int i3);

    XMLString concat(String str);

    XMLString toLowerCase(Locale locale);

    XMLString toLowerCase();

    XMLString toUpperCase(Locale locale);

    XMLString toUpperCase();

    XMLString trim();

    String toString();

    boolean hasString();

    double toDouble();
}
