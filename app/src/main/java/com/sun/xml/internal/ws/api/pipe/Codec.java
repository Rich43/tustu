package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.message.Packet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Codec.class */
public interface Codec {
    String getMimeType();

    ContentType getStaticContentType(Packet packet);

    ContentType encode(Packet packet, OutputStream outputStream) throws IOException;

    ContentType encode(Packet packet, WritableByteChannel writableByteChannel);

    Codec copy();

    void decode(InputStream inputStream, String str, Packet packet) throws IOException;

    void decode(ReadableByteChannel readableByteChannel, String str, Packet packet);
}
