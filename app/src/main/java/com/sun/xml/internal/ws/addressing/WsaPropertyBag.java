package com.sun.xml.internal.ws.addressing;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaPropertyBag.class */
public class WsaPropertyBag extends BasePropertySet {
    public static final String WSA_REPLYTO_FROM_REQUEST = "com.sun.xml.internal.ws.addressing.WsaPropertyBag.ReplyToFromRequest";
    public static final String WSA_FAULTTO_FROM_REQUEST = "com.sun.xml.internal.ws.addressing.WsaPropertyBag.FaultToFromRequest";
    public static final String WSA_MSGID_FROM_REQUEST = "com.sun.xml.internal.ws.addressing.WsaPropertyBag.MessageIdFromRequest";
    public static final String WSA_TO = "com.sun.xml.internal.ws.addressing.WsaPropertyBag.To";

    @NotNull
    private final AddressingVersion addressingVersion;

    @NotNull
    private final SOAPVersion soapVersion;

    @NotNull
    private final Packet packet;
    private static final BasePropertySet.PropertyMap model = parse(WsaPropertyBag.class);
    private WSEndpointReference _replyToFromRequest = null;
    private WSEndpointReference _faultToFromRequest = null;
    private String _msgIdFromRequest = null;

    public WsaPropertyBag(AddressingVersion addressingVersion, SOAPVersion soapVersion, Packet packet) {
        this.addressingVersion = addressingVersion;
        this.soapVersion = soapVersion;
        this.packet = packet;
    }

    @PropertySet.Property({JAXWSProperties.ADDRESSING_TO})
    public String getTo() throws XMLStreamException {
        Header h2;
        if (this.packet.getMessage() == null || (h2 = this.packet.getMessage().getHeaders().get(this.addressingVersion.toTag, false)) == null) {
            return null;
        }
        return h2.getStringContent();
    }

    @PropertySet.Property({WSA_TO})
    public WSEndpointReference getToAsReference() throws XMLStreamException {
        Header h2;
        if (this.packet.getMessage() == null || (h2 = this.packet.getMessage().getHeaders().get(this.addressingVersion.toTag, false)) == null) {
            return null;
        }
        return new WSEndpointReference(h2.getStringContent(), this.addressingVersion);
    }

    @PropertySet.Property({JAXWSProperties.ADDRESSING_FROM})
    public WSEndpointReference getFrom() throws XMLStreamException {
        return getEPR(this.addressingVersion.fromTag);
    }

    @PropertySet.Property({JAXWSProperties.ADDRESSING_ACTION})
    public String getAction() {
        Header h2;
        if (this.packet.getMessage() == null || (h2 = this.packet.getMessage().getHeaders().get(this.addressingVersion.actionTag, false)) == null) {
            return null;
        }
        return h2.getStringContent();
    }

    @PropertySet.Property({JAXWSProperties.ADDRESSING_MESSAGEID, WsaServerTube.REQUEST_MESSAGE_ID})
    public String getMessageID() {
        if (this.packet.getMessage() == null) {
            return null;
        }
        return AddressingUtils.getMessageID(this.packet.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
    }

    private WSEndpointReference getEPR(QName tag) throws XMLStreamException {
        Header h2;
        if (this.packet.getMessage() == null || (h2 = this.packet.getMessage().getHeaders().get(tag, false)) == null) {
            return null;
        }
        return h2.readAsEPR(this.addressingVersion);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }

    @PropertySet.Property({WSA_REPLYTO_FROM_REQUEST})
    public WSEndpointReference getReplyToFromRequest() {
        return this._replyToFromRequest;
    }

    public void setReplyToFromRequest(WSEndpointReference ref) {
        this._replyToFromRequest = ref;
    }

    @PropertySet.Property({WSA_FAULTTO_FROM_REQUEST})
    public WSEndpointReference getFaultToFromRequest() {
        return this._faultToFromRequest;
    }

    public void setFaultToFromRequest(WSEndpointReference ref) {
        this._faultToFromRequest = ref;
    }

    @PropertySet.Property({WSA_MSGID_FROM_REQUEST})
    public String getMessageIdFromRequest() {
        return this._msgIdFromRequest;
    }

    public void setMessageIdFromRequest(String id) {
        this._msgIdFromRequest = id;
    }
}
