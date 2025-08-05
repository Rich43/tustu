package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLStringFactory.class */
public abstract class XMLStringFactory {
    public abstract XMLString newstr(String str);

    public abstract XMLString newstr(FastStringBuffer fastStringBuffer, int i2, int i3);

    public abstract XMLString newstr(char[] cArr, int i2, int i3);

    public abstract XMLString emptystr();
}
