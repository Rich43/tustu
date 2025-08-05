package com.sun.xml.internal.ws.api.addressing;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/AddressingPropertySet.class */
public class AddressingPropertySet extends BasePropertySet {
    public static final String ADDRESSING_FAULT_TO = "com.sun.xml.internal.ws.api.addressing.fault.to";
    private String faultTo;
    public static final String ADDRESSING_MESSAGE_ID = "com.sun.xml.internal.ws.api.addressing.message.id";
    private String messageId;
    public static final String ADDRESSING_RELATES_TO = "com.sun.xml.internal.ws.api.addressing.relates.to";

    @PropertySet.Property({ADDRESSING_RELATES_TO})
    private String relatesTo;
    public static final String ADDRESSING_REPLY_TO = "com.sun.xml.internal.ws.api.addressing.reply.to";

    @PropertySet.Property({ADDRESSING_REPLY_TO})
    private String replyTo;
    private static final BasePropertySet.PropertyMap model = parse(AddressingPropertySet.class);

    @PropertySet.Property({ADDRESSING_FAULT_TO})
    public String getFaultTo() {
        return this.faultTo;
    }

    public void setFaultTo(String x2) {
        this.faultTo = x2;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String x2) {
        this.messageId = x2;
    }

    public String getRelatesTo() {
        return this.relatesTo;
    }

    public void setRelatesTo(String x2) {
        this.relatesTo = x2;
    }

    public String getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(String x2) {
        this.replyTo = x2;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }
}
