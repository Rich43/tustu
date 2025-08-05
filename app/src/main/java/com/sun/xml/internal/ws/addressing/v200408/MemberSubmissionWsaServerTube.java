package com.sun.xml.internal.ws.addressing.v200408;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.addressing.WsaServerTube;
import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/v200408/MemberSubmissionWsaServerTube.class */
public class MemberSubmissionWsaServerTube extends WsaServerTube {
    private final MemberSubmissionAddressing.Validation validation;

    public MemberSubmissionWsaServerTube(WSEndpoint endpoint, @NotNull WSDLPort wsdlPort, WSBinding binding, Tube next) {
        super(endpoint, wsdlPort, binding, next);
        this.validation = ((MemberSubmissionAddressingFeature) binding.getFeature(MemberSubmissionAddressingFeature.class)).getValidation();
    }

    public MemberSubmissionWsaServerTube(MemberSubmissionWsaServerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.validation = that.validation;
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaServerTube, com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public MemberSubmissionWsaServerTube copy(TubeCloner cloner) {
        return new MemberSubmissionWsaServerTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTube
    protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageId, boolean foundRelatesTo) {
        WSDLBoundOperation wbo;
        super.checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageId, foundRelatesTo);
        if (!foundTo) {
            throw new MissingAddressingHeaderException(this.addressingVersion.toTag, packet);
        }
        if (this.wsdlPort != null && (wbo = getWSDLBoundOperation(packet)) != null && !wbo.getOperation().isOneWay() && !foundReplyTo) {
            throw new MissingAddressingHeaderException(this.addressingVersion.replyToTag, packet);
        }
        if (!this.validation.equals(MemberSubmissionAddressing.Validation.LAX)) {
            if ((foundReplyTo || foundFaultTo) && !foundMessageId) {
                throw new MissingAddressingHeaderException(this.addressingVersion.messageIDTag, packet);
            }
        }
    }
}
