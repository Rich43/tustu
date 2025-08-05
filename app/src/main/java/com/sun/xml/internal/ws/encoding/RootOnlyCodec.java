package com.sun.xml.internal.ws.encoding;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/RootOnlyCodec.class */
public interface RootOnlyCodec extends Codec {
    void decode(@NotNull InputStream inputStream, @NotNull String str, @NotNull Packet packet, @NotNull AttachmentSet attachmentSet) throws IOException;

    void decode(@NotNull ReadableByteChannel readableByteChannel, @NotNull String str, @NotNull Packet packet, @NotNull AttachmentSet attachmentSet);
}
