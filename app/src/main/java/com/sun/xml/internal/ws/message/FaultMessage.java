package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.FilterMessageImpl;
import com.sun.xml.internal.ws.api.message.Message;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/FaultMessage.class */
public class FaultMessage extends FilterMessageImpl {

    @Nullable
    private final QName detailEntryName;

    public FaultMessage(Message delegate, @Nullable QName detailEntryName) {
        super(delegate);
        this.detailEntryName = detailEntryName;
    }

    @Override // com.sun.xml.internal.ws.api.message.FilterMessageImpl, com.sun.xml.internal.ws.api.message.Message
    @Nullable
    public QName getFirstDetailEntryName() {
        return this.detailEntryName;
    }
}
