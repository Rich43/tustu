package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.BridgeContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/BridgeContextImpl.class */
public final class BridgeContextImpl extends BridgeContext {
    public final UnmarshallerImpl unmarshaller;
    public final MarshallerImpl marshaller;

    BridgeContextImpl(JAXBContextImpl context) {
        this.unmarshaller = context.createUnmarshaller();
        this.marshaller = context.createMarshaller();
    }

    @Override // com.sun.xml.internal.bind.api.BridgeContext
    public void setErrorHandler(ValidationEventHandler handler) {
        try {
            this.unmarshaller.setEventHandler(handler);
            this.marshaller.setEventHandler(handler);
        } catch (JAXBException e2) {
            throw new Error(e2);
        }
    }

    @Override // com.sun.xml.internal.bind.api.BridgeContext
    public void setAttachmentMarshaller(AttachmentMarshaller m2) {
        this.marshaller.setAttachmentMarshaller(m2);
    }

    @Override // com.sun.xml.internal.bind.api.BridgeContext
    public void setAttachmentUnmarshaller(AttachmentUnmarshaller u2) {
        this.unmarshaller.setAttachmentUnmarshaller(u2);
    }

    @Override // com.sun.xml.internal.bind.api.BridgeContext
    public AttachmentMarshaller getAttachmentMarshaller() {
        return this.marshaller.getAttachmentMarshaller();
    }

    @Override // com.sun.xml.internal.bind.api.BridgeContext
    public AttachmentUnmarshaller getAttachmentUnmarshaller() {
        return this.unmarshaller.getAttachmentUnmarshaller();
    }
}
