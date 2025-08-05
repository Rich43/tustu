package com.sun.org.apache.xml.internal.serializer;

import org.xml.sax.Attributes;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/SerializerTrace.class */
public interface SerializerTrace {
    public static final int EVENTTYPE_STARTDOCUMENT = 1;
    public static final int EVENTTYPE_ENDDOCUMENT = 2;
    public static final int EVENTTYPE_STARTELEMENT = 3;
    public static final int EVENTTYPE_ENDELEMENT = 4;
    public static final int EVENTTYPE_CHARACTERS = 5;
    public static final int EVENTTYPE_IGNORABLEWHITESPACE = 6;
    public static final int EVENTTYPE_PI = 7;
    public static final int EVENTTYPE_COMMENT = 8;
    public static final int EVENTTYPE_ENTITYREF = 9;
    public static final int EVENTTYPE_CDATA = 10;
    public static final int EVENTTYPE_OUTPUT_PSEUDO_CHARACTERS = 11;
    public static final int EVENTTYPE_OUTPUT_CHARACTERS = 12;

    boolean hasTraceListeners();

    void fireGenerateEvent(int i2);

    void fireGenerateEvent(int i2, String str, Attributes attributes);

    void fireGenerateEvent(int i2, char[] cArr, int i3, int i4);

    void fireGenerateEvent(int i2, String str, String str2);

    void fireGenerateEvent(int i2, String str);
}
