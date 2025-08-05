package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingPropertySet;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.OneWayFeature;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.message.RelatesToHeader;
import com.sun.xml.internal.ws.message.StringHeader;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import com.sun.xml.internal.ws.resources.ClientMessages;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/AddressingUtils.class */
public class AddressingUtils {
    public static void fillRequestAddressingHeaders(MessageHeaders headers, Packet packet, AddressingVersion av2, SOAPVersion sv, boolean oneway, String action) {
        fillRequestAddressingHeaders(headers, packet, av2, sv, oneway, action, false);
    }

    public static void fillRequestAddressingHeaders(MessageHeaders headers, Packet packet, AddressingVersion av2, SOAPVersion sv, boolean oneway, String action, boolean mustUnderstand) {
        fillCommonAddressingHeaders(headers, packet, av2, sv, action, mustUnderstand);
        if (!oneway) {
            WSEndpointReference epr = av2.anonymousEpr;
            if (headers.get(av2.replyToTag, false) == null) {
                headers.add(epr.createHeader(av2.replyToTag));
            }
            if (headers.get(av2.faultToTag, false) == null) {
                headers.add(epr.createHeader(av2.faultToTag));
            }
            if (packet.getMessage().getHeaders().get(av2.messageIDTag, false) == null && headers.get(av2.messageIDTag, false) == null) {
                Header h2 = new StringHeader(av2.messageIDTag, Message.generateMessageID());
                headers.add(h2);
            }
        }
    }

    public static void fillRequestAddressingHeaders(MessageHeaders headers, WSDLPort wsdlPort, WSBinding binding, Packet packet) {
        WSDLBoundOperation wbo;
        if (binding == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_BINDING());
        }
        if (binding.isFeatureEnabled(SuppressAutomaticWSARequestHeadersFeature.class)) {
            return;
        }
        MessageHeaders hl = packet.getMessage().getHeaders();
        String action = getAction(hl, binding.getAddressingVersion(), binding.getSOAPVersion());
        if (action != null) {
            return;
        }
        AddressingVersion addressingVersion = binding.getAddressingVersion();
        WsaTubeHelper wsaHelper = addressingVersion.getWsaHelper(wsdlPort, null, binding);
        String effectiveInputAction = wsaHelper.getEffectiveInputAction(packet);
        if (effectiveInputAction == null || (effectiveInputAction.equals("") && binding.getSOAPVersion() == SOAPVersion.SOAP_11)) {
            throw new WebServiceException(ClientMessages.INVALID_SOAP_ACTION());
        }
        boolean oneway = !packet.expectReply.booleanValue();
        if (wsdlPort != null && !oneway && packet.getMessage() != null && packet.getWSDLOperation() != null && (wbo = wsdlPort.getBinding().get(packet.getWSDLOperation())) != null && wbo.getAnonymous() == WSDLBoundOperation.ANONYMOUS.prohibited) {
            throw new WebServiceException(AddressingMessages.WSAW_ANONYMOUS_PROHIBITED());
        }
        OneWayFeature oneWayFeature = (OneWayFeature) binding.getFeature(OneWayFeature.class);
        AddressingPropertySet addressingPropertySet = (AddressingPropertySet) packet.getSatellite(AddressingPropertySet.class);
        OneWayFeature oneWayFeature2 = addressingPropertySet == null ? oneWayFeature : new OneWayFeature(addressingPropertySet, addressingVersion);
        if (oneWayFeature2 == null || !oneWayFeature2.isEnabled()) {
            fillRequestAddressingHeaders(headers, packet, addressingVersion, binding.getSOAPVersion(), oneway, effectiveInputAction, AddressingVersion.isRequired(binding));
        } else {
            fillRequestAddressingHeaders(headers, packet, addressingVersion, binding.getSOAPVersion(), oneWayFeature2, oneway, effectiveInputAction);
        }
    }

    public static String getAction(@NotNull MessageHeaders headers, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        String action = null;
        Header h2 = getFirstHeader(headers, av2.actionTag, true, sv);
        if (h2 != null) {
            action = h2.getStringContent();
        }
        return action;
    }

    public static WSEndpointReference getFaultTo(@NotNull MessageHeaders headers, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        Header h2 = getFirstHeader(headers, av2.faultToTag, true, sv);
        WSEndpointReference faultTo = null;
        if (h2 != null) {
            try {
                faultTo = h2.readAsEPR(av2);
            } catch (XMLStreamException e2) {
                throw new WebServiceException(AddressingMessages.FAULT_TO_CANNOT_PARSE(), e2);
            }
        }
        return faultTo;
    }

    public static String getMessageID(@NotNull MessageHeaders headers, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        Header h2 = getFirstHeader(headers, av2.messageIDTag, true, sv);
        String messageId = null;
        if (h2 != null) {
            messageId = h2.getStringContent();
        }
        return messageId;
    }

    public static String getRelatesTo(@NotNull MessageHeaders headers, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        Header h2 = getFirstHeader(headers, av2.relatesToTag, true, sv);
        String relatesTo = null;
        if (h2 != null) {
            relatesTo = h2.getStringContent();
        }
        return relatesTo;
    }

    public static WSEndpointReference getReplyTo(@NotNull MessageHeaders headers, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        WSEndpointReference replyTo;
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        Header h2 = getFirstHeader(headers, av2.replyToTag, true, sv);
        if (h2 != null) {
            try {
                replyTo = h2.readAsEPR(av2);
            } catch (XMLStreamException e2) {
                throw new WebServiceException(AddressingMessages.REPLY_TO_CANNOT_PARSE(), e2);
            }
        } else {
            replyTo = av2.anonymousEpr;
        }
        return replyTo;
    }

    public static String getTo(MessageHeaders headers, AddressingVersion av2, SOAPVersion sv) {
        String to;
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        Header h2 = getFirstHeader(headers, av2.toTag, true, sv);
        if (h2 != null) {
            to = h2.getStringContent();
        } else {
            to = av2.anonymousUri;
        }
        return to;
    }

    public static Header getFirstHeader(MessageHeaders headers, QName name, boolean markUnderstood, SOAPVersion sv) {
        if (sv == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_SOAP_VERSION());
        }
        Iterator<Header> iter = headers.getHeaders(name.getNamespaceURI(), name.getLocalPart(), markUnderstood);
        while (iter.hasNext()) {
            Header h2 = iter.next();
            if (h2.getRole(sv).equals(sv.implicitRole)) {
                return h2;
            }
        }
        return null;
    }

    private static void fillRequestAddressingHeaders(@NotNull MessageHeaders headers, @NotNull Packet packet, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv, @NotNull OneWayFeature oneWayFeature, boolean oneway, @NotNull String action) {
        WSEndpointReference faultToEpr;
        WSEndpointReference replyToEpr;
        if (!oneway && !oneWayFeature.isUseAsyncWithSyncInvoke() && Boolean.TRUE.equals(packet.isSynchronousMEP)) {
            fillRequestAddressingHeaders(headers, packet, av2, sv, oneway, action);
            return;
        }
        fillCommonAddressingHeaders(headers, packet, av2, sv, action, false);
        boolean isMessageIdAdded = false;
        if (headers.get(av2.replyToTag, false) == null && (replyToEpr = oneWayFeature.getReplyTo()) != null) {
            headers.add(replyToEpr.createHeader(av2.replyToTag));
            if (packet.getMessage().getHeaders().get(av2.messageIDTag, false) == null) {
                String newID = oneWayFeature.getMessageId() == null ? Message.generateMessageID() : oneWayFeature.getMessageId();
                headers.add(new StringHeader(av2.messageIDTag, newID));
                isMessageIdAdded = true;
            }
        }
        String messageId = oneWayFeature.getMessageId();
        if (!isMessageIdAdded && messageId != null) {
            headers.add(new StringHeader(av2.messageIDTag, messageId));
        }
        if (headers.get(av2.faultToTag, false) == null && (faultToEpr = oneWayFeature.getFaultTo()) != null) {
            headers.add(faultToEpr.createHeader(av2.faultToTag));
            if (headers.get(av2.messageIDTag, false) == null) {
                headers.add(new StringHeader(av2.messageIDTag, Message.generateMessageID()));
            }
        }
        if (oneWayFeature.getFrom() != null) {
            headers.addOrReplace(oneWayFeature.getFrom().createHeader(av2.fromTag));
        }
        if (oneWayFeature.getRelatesToID() != null) {
            headers.addOrReplace(new RelatesToHeader(av2.relatesToTag, oneWayFeature.getRelatesToID()));
        }
    }

    private static void fillCommonAddressingHeaders(MessageHeaders headers, Packet packet, @NotNull AddressingVersion av2, @NotNull SOAPVersion sv, @NotNull String action, boolean mustUnderstand) {
        if (packet == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_PACKET());
        }
        if (av2 == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ADDRESSING_VERSION());
        }
        if (sv == null) {
            throw new IllegalArgumentException(AddressingMessages.NULL_SOAP_VERSION());
        }
        if (action == null && !sv.httpBindingId.equals("http://www.w3.org/2003/05/soap/bindings/HTTP/")) {
            throw new IllegalArgumentException(AddressingMessages.NULL_ACTION());
        }
        if (headers.get(av2.toTag, false) == null) {
            StringHeader h2 = new StringHeader(av2.toTag, packet.endpointAddress.toString());
            headers.add(h2);
        }
        if (action != null) {
            packet.soapAction = action;
            if (headers.get(av2.actionTag, false) == null) {
                StringHeader h3 = new StringHeader(av2.actionTag, action, sv, mustUnderstand);
                headers.add(h3);
            }
        }
    }
}
