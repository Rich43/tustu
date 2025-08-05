package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/BEncoderStream.class */
public class BEncoderStream extends BASE64EncoderStream {
    public BEncoderStream(OutputStream out) {
        super(out, Integer.MAX_VALUE);
    }

    public static int encodedLength(byte[] b2) {
        return ((b2.length + 2) / 3) * 4;
    }
}
