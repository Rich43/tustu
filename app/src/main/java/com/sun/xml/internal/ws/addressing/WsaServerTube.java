package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.addressing.model.ActionNotSupportedException;
import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.NonAnonymousResponseProcessor;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.client.Stub;
import com.sun.xml.internal.ws.message.FaultDetailHeader;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaServerTube.class */
public class WsaServerTube extends WsaTube {
    private WSEndpoint endpoint;
    private WSEndpointReference replyTo;
    private WSEndpointReference faultTo;
    private boolean isAnonymousRequired;
    protected boolean isEarlyBackchannelCloseAllowed;
    private WSDLBoundOperation wbo;
    public static final String REQUEST_MESSAGE_ID = "com.sun.xml.internal.ws.addressing.request.messageID";
    private static final Logger LOGGER = Logger.getLogger(WsaServerTube.class.getName());

    public WsaServerTube(WSEndpoint endpoint, @NotNull WSDLPort wsdlPort, WSBinding binding, Tube next) {
        super(wsdlPort, binding, next);
        this.isAnonymousRequired = false;
        this.isEarlyBackchannelCloseAllowed = true;
        this.endpoint = endpoint;
    }

    public WsaServerTube(WsaServerTube that, TubeCloner cloner) {
        super(that, cloner);
        this.isAnonymousRequired = false;
        this.isEarlyBackchannelCloseAllowed = true;
        this.endpoint = that.endpoint;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public WsaServerTube copy(TubeCloner cloner) {
        return new WsaServerTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(Packet request) {
        Message msg = request.getMessage();
        if (msg == null) {
            return doInvoke(this.next, request);
        }
        request.addSatellite(new WsaPropertyBag(this.addressingVersion, this.soapVersion, request));
        MessageHeaders hl = request.getMessage().getHeaders();
        try {
            this.replyTo = AddressingUtils.getReplyTo(hl, this.addressingVersion, this.soapVersion);
            this.faultTo = AddressingUtils.getFaultTo(hl, this.addressingVersion, this.soapVersion);
            String msgId = AddressingUtils.getMessageID(hl, this.addressingVersion, this.soapVersion);
            if (this.replyTo == null) {
                this.replyTo = this.addressingVersion.anonymousEpr;
            }
            if (this.faultTo == null) {
                this.faultTo = this.replyTo;
            }
            request.put(WsaPropertyBag.WSA_REPLYTO_FROM_REQUEST, this.replyTo);
            request.put(WsaPropertyBag.WSA_FAULTTO_FROM_REQUEST, this.faultTo);
            request.put(WsaPropertyBag.WSA_MSGID_FROM_REQUEST, msgId);
            this.wbo = getWSDLBoundOperation(request);
            this.isAnonymousRequired = isAnonymousRequired(this.wbo);
            Packet p2 = validateInboundHeaders(request);
            if (p2.getMessage() == null) {
                return doReturnWith(p2);
            }
            if (p2.getMessage().isFault()) {
                if (this.isEarlyBackchannelCloseAllowed && !this.isAnonymousRequired && !this.faultTo.isAnonymous() && request.transportBackChannel != null) {
                    request.transportBackChannel.close();
                }
                return processResponse(p2);
            }
            if (this.isEarlyBackchannelCloseAllowed && !this.isAnonymousRequired && !this.replyTo.isAnonymous() && !this.faultTo.isAnonymous() && request.transportBackChannel != null) {
                request.transportBackChannel.close();
            }
            return doInvoke(this.next, p2);
        } catch (InvalidAddressingHeaderException e2) {
            LOGGER.log(Level.WARNING, this.addressingVersion.getInvalidMapText() + ", Problem header:" + ((Object) e2.getProblemHeader()) + ", Reason: " + ((Object) e2.getSubsubcode()), (Throwable) e2);
            hl.remove(e2.getProblemHeader());
            SOAPFault soapFault = this.helper.createInvalidAddressingHeaderFault(e2, this.addressingVersion);
            if (this.wsdlPort != null && request.getMessage().isOneWay(this.wsdlPort)) {
                Packet response = request.createServerResponse((Message) null, this.wsdlPort, (SEIModel) null, this.binding);
                return doReturnWith(response);
            }
            Message m2 = Messages.create(soapFault);
            if (this.soapVersion == SOAPVersion.SOAP_11) {
                FaultDetailHeader s11FaultDetailHeader = new FaultDetailHeader(this.addressingVersion, this.addressingVersion.problemHeaderQNameTag.getLocalPart(), e2.getProblemHeader());
                m2.getHeaders().add(s11FaultDetailHeader);
            }
            Packet response2 = request.createServerResponse(m2, this.wsdlPort, (SEIModel) null, this.binding);
            return doReturnWith(response2);
        }
    }

    protected boolean isAnonymousRequired(@Nullable WSDLBoundOperation wbo) {
        return false;
    }

    protected void checkAnonymousSemantics(WSDLBoundOperation wbo, WSEndpointReference replyTo, WSEndpointReference faultTo) {
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTube, com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(Throwable t2) {
        Packet response = Fiber.current().getPacket();
        ThrowableContainerPropertySet tc = (ThrowableContainerPropertySet) response.getSatellite(ThrowableContainerPropertySet.class);
        if (tc == null) {
            tc = new ThrowableContainerPropertySet(t2);
            response.addSatellite(tc);
        } else if (t2 != tc.getThrowable()) {
            tc.setThrowable(t2);
        }
        return processResponse(response.endpoint.createServiceResponseForException(tc, response, this.soapVersion, this.wsdlPort, response.endpoint.getSEIModel(), this.binding));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(Packet response) {
        String outputAction;
        Message msg = response.getMessage();
        if (msg == null) {
            return doReturnWith(response);
        }
        String to = AddressingUtils.getTo(msg.getHeaders(), this.addressingVersion, this.soapVersion);
        if (to != null) {
            WSEndpointReference wSEndpointReference = new WSEndpointReference(to, this.addressingVersion);
            this.faultTo = wSEndpointReference;
            this.replyTo = wSEndpointReference;
        }
        if (this.replyTo == null) {
            this.replyTo = (WSEndpointReference) response.get(WsaPropertyBag.WSA_REPLYTO_FROM_REQUEST);
        }
        if (this.faultTo == null) {
            this.faultTo = (WSEndpointReference) response.get(WsaPropertyBag.WSA_FAULTTO_FROM_REQUEST);
        }
        WSEndpointReference target = msg.isFault() ? this.faultTo : this.replyTo;
        if (target == null && (response.proxy instanceof Stub)) {
            target = ((Stub) response.proxy).getWSEndpointReference();
        }
        if (target == null || target.isAnonymous() || this.isAnonymousRequired) {
            return doReturnWith(response);
        }
        if (target.isNone()) {
            response.setMessage(null);
            return doReturnWith(response);
        }
        if (this.wsdlPort != null && response.getMessage().isOneWay(this.wsdlPort)) {
            LOGGER.fine(AddressingMessages.NON_ANONYMOUS_RESPONSE_ONEWAY());
            return doReturnWith(response);
        }
        if (this.wbo != null || response.soapAction == null) {
            if (response.getMessage().isFault()) {
                outputAction = this.helper.getFaultAction(this.wbo, response);
            } else {
                outputAction = this.helper.getOutputAction(this.wbo);
            }
            String action = outputAction;
            if (response.soapAction == null || (action != null && !action.equals(AddressingVersion.UNSET_OUTPUT_ACTION))) {
                response.soapAction = action;
            }
        }
        response.expectReply = false;
        try {
            EndpointAddress adrs = new EndpointAddress(URI.create(target.getAddress()));
            response.endpointAddress = adrs;
            if (response.isAdapterDeliversNonAnonymousResponse) {
                return doReturnWith(response);
            }
            return doReturnWith(NonAnonymousResponseProcessor.getDefault().process(response));
        } catch (IllegalArgumentException e2) {
            throw new WebServiceException(e2);
        } catch (NullPointerException e3) {
            throw new WebServiceException(e3);
        }
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTube
    protected void validateAction(Packet packet) {
        WSDLBoundOperation wsdlBoundOperation = getWSDLBoundOperation(packet);
        if (wsdlBoundOperation == null) {
            return;
        }
        String gotA = AddressingUtils.getAction(packet.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
        if (gotA == null) {
            throw new WebServiceException(AddressingMessages.VALIDATION_SERVER_NULL_ACTION());
        }
        String expected = this.helper.getInputAction(packet);
        String soapAction = this.helper.getSOAPAction(packet);
        if (this.helper.isInputActionDefault(packet) && soapAction != null && !soapAction.equals("")) {
            expected = soapAction;
        }
        if (expected != null && !gotA.equals(expected)) {
            throw new ActionNotSupportedException(gotA);
        }
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTube
    protected void checkMessageAddressingProperties(Packet packet) {
        super.checkMessageAddressingProperties(packet);
        WSDLBoundOperation wsdlBoundOperation = getWSDLBoundOperation(packet);
        checkAnonymousSemantics(wsdlBoundOperation, this.replyTo, this.faultTo);
        checkNonAnonymousAddresses(this.replyTo, this.faultTo);
    }

    private void checkNonAnonymousAddresses(WSEndpointReference replyTo, WSEndpointReference faultTo) {
        if (!replyTo.isAnonymous()) {
            try {
                new EndpointAddress(URI.create(replyTo.getAddress()));
            } catch (Exception e2) {
                throw new InvalidAddressingHeaderException(this.addressingVersion.replyToTag, this.addressingVersion.invalidAddressTag);
            }
        }
    }
}
