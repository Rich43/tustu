package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/AttachmentSet.class */
public interface AttachmentSet extends Iterable<Attachment> {
    @Nullable
    Attachment get(String str);

    boolean isEmpty();

    void add(Attachment attachment);
}
