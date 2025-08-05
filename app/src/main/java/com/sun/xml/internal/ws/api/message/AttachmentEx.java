package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/AttachmentEx.class */
public interface AttachmentEx extends Attachment {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/AttachmentEx$MimeHeader.class */
    public interface MimeHeader {
        String getName();

        String getValue();
    }

    @NotNull
    Iterator<MimeHeader> getMimeHeaders();
}
