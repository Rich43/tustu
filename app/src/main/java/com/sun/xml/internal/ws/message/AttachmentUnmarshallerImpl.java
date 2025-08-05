package com.sun.xml.internal.ws.message;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.resources.EncodingMessages;
import javax.activation.DataHandler;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/AttachmentUnmarshallerImpl.class */
public final class AttachmentUnmarshallerImpl extends AttachmentUnmarshaller {
    private final AttachmentSet attachments;

    public AttachmentUnmarshallerImpl(AttachmentSet attachments) {
        this.attachments = attachments;
    }

    @Override // javax.xml.bind.attachment.AttachmentUnmarshaller
    public DataHandler getAttachmentAsDataHandler(String cid) {
        Attachment a2 = this.attachments.get(stripScheme(cid));
        if (a2 == null) {
            throw new WebServiceException(EncodingMessages.NO_SUCH_CONTENT_ID(cid));
        }
        return a2.asDataHandler();
    }

    @Override // javax.xml.bind.attachment.AttachmentUnmarshaller
    public byte[] getAttachmentAsByteArray(String cid) {
        Attachment a2 = this.attachments.get(stripScheme(cid));
        if (a2 == null) {
            throw new WebServiceException(EncodingMessages.NO_SUCH_CONTENT_ID(cid));
        }
        return a2.asByteArray();
    }

    private String stripScheme(String cid) {
        if (cid.startsWith("cid:")) {
            cid = cid.substring(4);
        }
        return cid;
    }
}
