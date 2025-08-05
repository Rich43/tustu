package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.SOAPBinding;
import org.icepdf.core.util.PdfOps;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaTube.class */
public abstract class WsaTube extends AbstractFilterTubeImpl {

    @NotNull
    protected final WSDLPort wsdlPort;
    protected final WSBinding binding;
    final WsaTubeHelper helper;

    @NotNull
    protected final AddressingVersion addressingVersion;
    protected final SOAPVersion soapVersion;
    private final boolean addressingRequired;
    private static final Logger LOGGER = Logger.getLogger(WsaTube.class.getName());

    protected abstract void validateAction(Packet packet);

    public WsaTube(WSDLPort wsdlPort, WSBinding binding, Tube next) {
        super(next);
        this.wsdlPort = wsdlPort;
        this.binding = binding;
        addKnownHeadersToBinding(binding);
        this.addressingVersion = binding.getAddressingVersion();
        this.soapVersion = binding.getSOAPVersion();
        this.helper = getTubeHelper();
        this.addressingRequired = AddressingVersion.isRequired(binding);
    }

    public WsaTube(WsaTube that, TubeCloner cloner) {
        super(that, cloner);
        this.wsdlPort = that.wsdlPort;
        this.binding = that.binding;
        this.helper = that.helper;
        this.addressingVersion = that.addressingVersion;
        this.soapVersion = that.soapVersion;
        this.addressingRequired = that.addressingRequired;
    }

    private void addKnownHeadersToBinding(WSBinding binding) {
        for (AddressingVersion addrVersion : AddressingVersion.values()) {
            binding.addKnownHeader(addrVersion.actionTag);
            binding.addKnownHeader(addrVersion.faultDetailTag);
            binding.addKnownHeader(addrVersion.faultToTag);
            binding.addKnownHeader(addrVersion.fromTag);
            binding.addKnownHeader(addrVersion.messageIDTag);
            binding.addKnownHeader(addrVersion.relatesToTag);
            binding.addKnownHeader(addrVersion.replyToTag);
            binding.addKnownHeader(addrVersion.toTag);
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(Throwable t2) {
        return super.processException(t2);
    }

    protected WsaTubeHelper getTubeHelper() {
        if (this.binding.isFeatureEnabled(AddressingFeature.class)) {
            return new WsaTubeHelperImpl(this.wsdlPort, null, this.binding);
        }
        if (this.binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class)) {
            return new com.sun.xml.internal.ws.addressing.v200408.WsaTubeHelperImpl(this.wsdlPort, null, this.binding);
        }
        throw new WebServiceException(AddressingMessages.ADDRESSING_NOT_ENABLED(getClass().getSimpleName()));
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0115  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.sun.xml.internal.ws.api.message.Packet validateInboundHeaders(com.sun.xml.internal.ws.api.message.Packet r7) {
        /*
            Method dump skipped, instructions count: 279
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.addressing.WsaTube.validateInboundHeaders(com.sun.xml.internal.ws.api.message.Packet):com.sun.xml.internal.ws.api.message.Packet");
    }

    protected void checkMessageAddressingProperties(Packet packet) {
        checkCardinality(packet);
    }

    final boolean isAddressingEngagedOrRequired(Packet packet, WSBinding binding) {
        if (AddressingVersion.isRequired(binding)) {
            return true;
        }
        if (packet == null || packet.getMessage() == null || packet.getMessage().getHeaders() != null) {
            return false;
        }
        String action = AddressingUtils.getAction(packet.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
        if (action == null) {
            return true;
        }
        return true;
    }

    protected void checkCardinality(Packet packet) {
        Message message = packet.getMessage();
        if (message == null) {
            if (this.addressingRequired) {
                throw new WebServiceException(AddressingMessages.NULL_MESSAGE());
            }
            return;
        }
        Iterator<Header> hIter = message.getHeaders().getHeaders(this.addressingVersion.nsUri, true);
        if (!hIter.hasNext()) {
            if (this.addressingRequired) {
                throw new MissingAddressingHeaderException(this.addressingVersion.actionTag, packet);
            }
            return;
        }
        boolean foundFrom = false;
        boolean foundTo = false;
        boolean foundReplyTo = false;
        boolean foundFaultTo = false;
        boolean foundAction = false;
        boolean foundMessageId = false;
        boolean foundRelatesTo = false;
        QName duplicateHeader = null;
        while (true) {
            if (!hIter.hasNext()) {
                break;
            }
            Header h2 = hIter.next();
            if (isInCurrentRole(h2, this.binding)) {
                String local = h2.getLocalPart();
                if (local.equals(this.addressingVersion.fromTag.getLocalPart())) {
                    if (foundFrom) {
                        duplicateHeader = this.addressingVersion.fromTag;
                        break;
                    }
                    foundFrom = true;
                } else if (local.equals(this.addressingVersion.toTag.getLocalPart())) {
                    if (foundTo) {
                        duplicateHeader = this.addressingVersion.toTag;
                        break;
                    }
                    foundTo = true;
                } else if (local.equals(this.addressingVersion.replyToTag.getLocalPart())) {
                    if (foundReplyTo) {
                        duplicateHeader = this.addressingVersion.replyToTag;
                        break;
                    }
                    foundReplyTo = true;
                    try {
                        h2.readAsEPR(this.addressingVersion);
                    } catch (XMLStreamException e2) {
                        throw new WebServiceException(AddressingMessages.REPLY_TO_CANNOT_PARSE(), e2);
                    }
                } else if (local.equals(this.addressingVersion.faultToTag.getLocalPart())) {
                    if (foundFaultTo) {
                        duplicateHeader = this.addressingVersion.faultToTag;
                        break;
                    }
                    foundFaultTo = true;
                    try {
                        h2.readAsEPR(this.addressingVersion);
                    } catch (XMLStreamException e3) {
                        throw new WebServiceException(AddressingMessages.FAULT_TO_CANNOT_PARSE(), e3);
                    }
                } else if (local.equals(this.addressingVersion.actionTag.getLocalPart())) {
                    if (foundAction) {
                        duplicateHeader = this.addressingVersion.actionTag;
                        break;
                    }
                    foundAction = true;
                } else if (local.equals(this.addressingVersion.messageIDTag.getLocalPart())) {
                    if (foundMessageId) {
                        duplicateHeader = this.addressingVersion.messageIDTag;
                        break;
                    }
                    foundMessageId = true;
                } else if (local.equals(this.addressingVersion.relatesToTag.getLocalPart())) {
                    foundRelatesTo = true;
                } else if (!local.equals(this.addressingVersion.faultDetailTag.getLocalPart())) {
                    System.err.println(AddressingMessages.UNKNOWN_WSA_HEADER());
                }
            }
        }
        if (duplicateHeader != null) {
            throw new InvalidAddressingHeaderException(duplicateHeader, this.addressingVersion.invalidCardinalityTag);
        }
        boolean engaged = foundAction;
        if (engaged || this.addressingRequired) {
            checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageId, foundRelatesTo);
        }
    }

    final boolean isInCurrentRole(Header header, WSBinding binding) {
        if (binding == null) {
            return true;
        }
        return ((SOAPBinding) binding).getRoles().contains(header.getRole(this.soapVersion));
    }

    protected final WSDLBoundOperation getWSDLBoundOperation(Packet packet) {
        QName opName;
        if (this.wsdlPort != null && (opName = packet.getWSDLOperation()) != null) {
            return this.wsdlPort.getBinding().get(opName);
        }
        return null;
    }

    protected void validateSOAPAction(Packet packet) {
        String gotA = AddressingUtils.getAction(packet.getMessage().getHeaders(), this.addressingVersion, this.soapVersion);
        if (gotA == null) {
            throw new WebServiceException(AddressingMessages.VALIDATION_SERVER_NULL_ACTION());
        }
        if (packet.soapAction != null && !packet.soapAction.equals("\"\"") && !packet.soapAction.equals(PdfOps.DOUBLE_QUOTE__TOKEN + gotA + PdfOps.DOUBLE_QUOTE__TOKEN)) {
            throw new InvalidAddressingHeaderException(this.addressingVersion.actionTag, this.addressingVersion.actionMismatchTag);
        }
    }

    protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageId, boolean foundRelatesTo) {
        if (!foundAction) {
            throw new MissingAddressingHeaderException(this.addressingVersion.actionTag, packet);
        }
        validateSOAPAction(packet);
    }
}
