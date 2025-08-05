package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/UniqueValue.class */
class UniqueValue {
    private static int part = 0;

    UniqueValue() {
    }

    public static String getUniqueBoundaryValue() {
        StringBuffer s2 = new StringBuffer();
        StringBuffer stringBufferAppend = s2.append("----=_Part_");
        int i2 = part;
        part = i2 + 1;
        stringBufferAppend.append(i2).append("_").append(s2.hashCode()).append('.').append(System.currentTimeMillis());
        return s2.toString();
    }
}
