package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/StreamSOAPCodec.class */
public interface StreamSOAPCodec extends Codec {
    @NotNull
    Message decode(@NotNull XMLStreamReader xMLStreamReader);

    @NotNull
    Message decode(@NotNull XMLStreamReader xMLStreamReader, @NotNull AttachmentSet attachmentSet);
}
