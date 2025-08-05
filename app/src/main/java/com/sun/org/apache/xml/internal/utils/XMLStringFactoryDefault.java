package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLStringFactoryDefault.class */
public class XMLStringFactoryDefault extends XMLStringFactory {
    private static final XMLStringDefault EMPTY_STR = new XMLStringDefault("");

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(String string) {
        return new XMLStringDefault(string);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(FastStringBuffer fsb, int start, int length) {
        return new XMLStringDefault(fsb.getString(start, length));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(char[] string, int start, int length) {
        return new XMLStringDefault(new String(string, start, length));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString emptystr() {
        return EMPTY_STR;
    }
}
