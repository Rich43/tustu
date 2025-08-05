package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XMLStringFactoryImpl.class */
public class XMLStringFactoryImpl extends XMLStringFactory {
    private static XMLStringFactory m_xstringfactory = new XMLStringFactoryImpl();

    public static XMLStringFactory getFactory() {
        return m_xstringfactory;
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(String string) {
        return new XString(string);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(FastStringBuffer fsb, int start, int length) {
        return new XStringForFSB(fsb, start, length);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString newstr(char[] string, int start, int length) {
        return new XStringForChars(string, start, length);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLStringFactory
    public XMLString emptystr() {
        return XString.EMPTYSTRING;
    }
}
