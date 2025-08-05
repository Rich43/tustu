package com.sun.xml.internal.ws.api.message.stream;

import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/stream/InputStreamMessage.class */
public class InputStreamMessage extends StreamBasedMessage {
    public final String contentType;
    public final InputStream msg;

    public InputStreamMessage(Packet properties, String contentType, InputStream msg) {
        super(properties);
        this.contentType = contentType;
        this.msg = msg;
    }

    public InputStreamMessage(Packet properties, AttachmentSet attachments, String contentType, InputStream msg) {
        super(properties, attachments);
        this.contentType = contentType;
        this.msg = msg;
    }
}
