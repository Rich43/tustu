package com.sun.xml.internal.bind.api;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/BridgeContext.class */
public abstract class BridgeContext {
    public abstract void setErrorHandler(ValidationEventHandler validationEventHandler);

    public abstract void setAttachmentMarshaller(AttachmentMarshaller attachmentMarshaller);

    public abstract void setAttachmentUnmarshaller(AttachmentUnmarshaller attachmentUnmarshaller);

    public abstract AttachmentMarshaller getAttachmentMarshaller();

    public abstract AttachmentUnmarshaller getAttachmentUnmarshaller();

    protected BridgeContext() {
    }
}
