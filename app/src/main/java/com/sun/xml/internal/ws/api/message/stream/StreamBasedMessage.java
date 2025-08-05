package com.sun.xml.internal.ws.api.message.stream;

import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/stream/StreamBasedMessage.class */
abstract class StreamBasedMessage {
    public final Packet properties;
    public final AttachmentSet attachments;

    protected StreamBasedMessage(Packet properties) {
        this.properties = properties;
        this.attachments = new AttachmentSetImpl();
    }

    protected StreamBasedMessage(Packet properties, AttachmentSet attachments) {
        this.properties = properties;
        this.attachments = attachments;
    }
}
