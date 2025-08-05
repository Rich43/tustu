package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import javax.activation.DataHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/SwaRefAdapter.class */
public final class SwaRefAdapter extends XmlAdapter<String, DataHandler> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public DataHandler unmarshal(String cid) {
        AttachmentUnmarshaller au2 = UnmarshallingContext.getInstance().parent.getAttachmentUnmarshaller();
        return au2.getAttachmentAsDataHandler(cid);
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(DataHandler data) {
        if (data == null) {
            return null;
        }
        AttachmentMarshaller am2 = XMLSerializer.getInstance().attachmentMarshaller;
        return am2.addSwaRefAttachment(data);
    }
}
