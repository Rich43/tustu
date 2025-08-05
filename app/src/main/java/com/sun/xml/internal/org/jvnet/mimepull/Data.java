package com.sun.xml.internal.org.jvnet.mimepull;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/Data.class */
interface Data {
    int size();

    byte[] read();

    long writeTo(DataFile dataFile);

    Data createNext(DataHead dataHead, ByteBuffer byteBuffer);
}
