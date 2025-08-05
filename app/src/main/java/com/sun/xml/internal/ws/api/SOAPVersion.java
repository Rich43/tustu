package com.sun.xml.internal.ws.api;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import com.oracle.webservices.internal.api.EnvelopeStyleFeature;
import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/SOAPVersion.class */
public enum SOAPVersion {
    SOAP_11(SOAPBinding.SOAP11HTTP_BINDING, "http://schemas.xmlsoap.org/soap/envelope/", "text/xml", "http://schemas.xmlsoap.org/soap/actor/next", SOAPNamespaceConstants.ATTR_ACTOR, "SOAP 1.1 Protocol", new QName("http://schemas.xmlsoap.org/soap/envelope/", "MustUnderstand"), "Client", "Server", Collections.singleton("http://schemas.xmlsoap.org/soap/actor/next")),
    SOAP_12("http://www.w3.org/2003/05/soap/bindings/HTTP/", "http://www.w3.org/2003/05/soap-envelope", "application/soap+xml", "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver", SOAP12NamespaceConstants.ATTR_ACTOR, SOAPConstants.SOAP_1_2_PROTOCOL, new QName("http://www.w3.org/2003/05/soap-envelope", "MustUnderstand"), "Sender", "Receiver", new HashSet(Arrays.asList("http://www.w3.org/2003/05/soap-envelope/role/next", "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver")));

    public final String httpBindingId;
    public final String nsUri;
    public final String contentType;
    public final QName faultCodeMustUnderstand;
    public final MessageFactory saajMessageFactory;
    public final SOAPFactory saajSoapFactory;
    private final String saajFactoryString;
    public final String implicitRole;
    public final Set<String> implicitRoleSet;
    public final Set<String> requiredRoles;
    public final String roleAttributeName;
    public final QName faultCodeClient;
    public final QName faultCodeServer;

    SOAPVersion(String httpBindingId, String nsUri, String contentType, String implicitRole, String roleAttributeName, String saajFactoryString, QName faultCodeMustUnderstand, String faultCodeClientLocalName, String faultCodeServerLocalName, Set set) {
        this.httpBindingId = httpBindingId;
        this.nsUri = nsUri;
        this.contentType = contentType;
        this.implicitRole = implicitRole;
        this.implicitRoleSet = Collections.singleton(implicitRole);
        this.roleAttributeName = roleAttributeName;
        this.saajFactoryString = saajFactoryString;
        try {
            this.saajMessageFactory = MessageFactory.newInstance(saajFactoryString);
            this.saajSoapFactory = SOAPFactory.newInstance(saajFactoryString);
            this.faultCodeMustUnderstand = faultCodeMustUnderstand;
            this.requiredRoles = set;
            this.faultCodeClient = new QName(nsUri, faultCodeClientLocalName);
            this.faultCodeServer = new QName(nsUri, faultCodeServerLocalName);
        } catch (NoSuchMethodError e2) {
            LinkageError x2 = new LinkageError("You are loading old SAAJ from " + Which.which(MessageFactory.class));
            x2.initCause(e2);
            throw x2;
        } catch (SOAPException e3) {
            throw new Error(e3);
        }
    }

    public SOAPFactory getSOAPFactory() {
        try {
            return SAAJFactory.getSOAPFactory(this.saajFactoryString);
        } catch (NoSuchMethodError e2) {
            LinkageError x2 = new LinkageError("You are loading old SAAJ from " + Which.which(MessageFactory.class));
            x2.initCause(e2);
            throw x2;
        } catch (SOAPException e3) {
            throw new Error(e3);
        }
    }

    public MessageFactory getMessageFactory() {
        try {
            return SAAJFactory.getMessageFactory(this.saajFactoryString);
        } catch (NoSuchMethodError e2) {
            LinkageError x2 = new LinkageError("You are loading old SAAJ from " + Which.which(MessageFactory.class));
            x2.initCause(e2);
            throw x2;
        } catch (SOAPException e3) {
            throw new Error(e3);
        }
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.httpBindingId;
    }

    public static SOAPVersion fromHttpBinding(String binding) {
        if (binding == null) {
            return SOAP_11;
        }
        if (binding.equals(SOAP_12.httpBindingId)) {
            return SOAP_12;
        }
        return SOAP_11;
    }

    public static SOAPVersion fromNsUri(String nsUri) {
        if (nsUri.equals(SOAP_12.nsUri)) {
            return SOAP_12;
        }
        return SOAP_11;
    }

    public static SOAPVersion from(EnvelopeStyleFeature f2) {
        EnvelopeStyle.Style[] style = f2.getStyles();
        if (style.length != 1) {
            throw new IllegalArgumentException("The EnvelopingFeature must has exactly one Enveloping.Style");
        }
        return from(style[0]);
    }

    public static SOAPVersion from(EnvelopeStyle.Style style) {
        switch (style) {
            case SOAP11:
                return SOAP_11;
            case SOAP12:
                return SOAP_12;
            case XML:
            default:
                return SOAP_11;
        }
    }

    public EnvelopeStyleFeature toFeature() {
        return SOAP_11.equals(this) ? new EnvelopeStyleFeature(EnvelopeStyle.Style.SOAP11) : new EnvelopeStyleFeature(EnvelopeStyle.Style.SOAP12);
    }
}
