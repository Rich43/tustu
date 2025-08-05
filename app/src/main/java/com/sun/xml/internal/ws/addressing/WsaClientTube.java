package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaClientTube.class */
public class WsaClientTube extends WsaTube {
    protected boolean expectReply;

    @Override // com.sun.xml.internal.ws.addressing.WsaTube, com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public /* bridge */ /* synthetic */ NextAction processException(Throwable th) {
        return super.processException(th);
    }

    public WsaClientTube(WSDLPort wsdlPort, WSBinding binding, Tube next) {
        super(wsdlPort, binding, next);
        this.expectReply = true;
    }

    public WsaClientTube(WsaClientTube that, TubeCloner cloner) {
        super(that, cloner);
        this.expectReply = true;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public WsaClientTube copy(TubeCloner cloner) {
        return new WsaClientTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(Packet request) {
        this.expectReply = request.expectReply.booleanValue();
        return doInvoke(this.next, request);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(Packet response) {
        if (response.getMessage() != null) {
            response = validateInboundHeaders(response);
            response.addSatellite(new WsaPropertyBag(this.addressingVersion, this.soapVersion, response));
            String msgId = AddressingUtils.getMessageID(response.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
            response.put(WsaPropertyBag.WSA_MSGID_FROM_REQUEST, msgId);
        }
        return doReturnWith(response);
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTube
    protected void validateAction(Packet packet) {
        WSDLBoundOperation wbo = getWSDLBoundOperation(packet);
        if (wbo == null) {
            return;
        }
        String gotA = AddressingUtils.getAction(packet.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
        if (gotA == null) {
            throw new WebServiceException(AddressingMessages.VALIDATION_CLIENT_NULL_ACTION());
        }
        String expected = this.helper.getOutputAction(packet);
        if (expected != null && !gotA.equals(expected)) {
            throw new ActionNotSupportedException(gotA);
        }
    }
}
