package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.encoding.MimeMultipartParser;
import com.sun.xml.internal.ws.resources.EncodingMessages;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/MimeAttachmentSet.class */
public final class MimeAttachmentSet implements AttachmentSet {
    private final MimeMultipartParser mpp;
    private Map<String, Attachment> atts = new HashMap();

    public MimeAttachmentSet(MimeMultipartParser mpp) {
        this.mpp = mpp;
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    @Nullable
    public Attachment get(String contentId) {
        Attachment att = this.atts.get(contentId);
        if (att != null) {
            return att;
        }
        try {
            Attachment att2 = this.mpp.getAttachmentPart(contentId);
            if (att2 != null) {
                this.atts.put(contentId, att2);
            }
            return att2;
        } catch (IOException e2) {
            throw new WebServiceException(EncodingMessages.NO_SUCH_CONTENT_ID(contentId), e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    public boolean isEmpty() {
        return this.atts.size() <= 0 && this.mpp.getAttachmentParts().isEmpty();
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    public void add(Attachment att) {
        this.atts.put(att.getContentId(), att);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Attachment> iterator() {
        Map<String, Attachment> attachments = this.mpp.getAttachmentParts();
        for (Map.Entry<String, Attachment> att : attachments.entrySet()) {
            if (this.atts.get(att.getKey()) == null) {
                this.atts.put(att.getKey(), att.getValue());
            }
        }
        return this.atts.values().iterator();
    }
}
