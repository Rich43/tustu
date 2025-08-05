package com.sun.xml.internal.ws.message;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/AttachmentSetImpl.class */
public final class AttachmentSetImpl implements AttachmentSet {
    private final ArrayList<Attachment> attList = new ArrayList<>();

    public AttachmentSetImpl() {
    }

    public AttachmentSetImpl(Iterable<Attachment> base) {
        for (Attachment a2 : base) {
            add(a2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    public Attachment get(String contentId) {
        for (int i2 = this.attList.size() - 1; i2 >= 0; i2--) {
            Attachment a2 = this.attList.get(i2);
            if (a2.getContentId().equals(contentId)) {
                return a2;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    public boolean isEmpty() {
        return this.attList.isEmpty();
    }

    @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
    public void add(Attachment att) {
        this.attList.add(att);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Attachment> iterator() {
        return this.attList.iterator();
    }
}
