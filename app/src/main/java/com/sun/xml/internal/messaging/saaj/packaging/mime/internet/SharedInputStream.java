package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/SharedInputStream.class */
public interface SharedInputStream {
    long getPosition();

    InputStream newStream(long j2, long j3);

    void writeTo(long j2, long j3, OutputStream outputStream);
}
