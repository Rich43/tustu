package com.sun.xml.internal.ws.developer;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;

@XmlRootElement(name = "EndpointReference", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
@XmlType(name = "EndpointReferenceType", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionEndpointReference.class */
public final class MemberSubmissionEndpointReference extends EndpointReference implements MemberSubmissionAddressingConstants {
    private static final ContextClassloaderLocal<JAXBContext> msjc = new ContextClassloaderLocal<JAXBContext>() { // from class: com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.developer.ContextClassloaderLocal
        public JAXBContext initialValue() throws Exception {
            return MemberSubmissionEndpointReference.getMSJaxbContext();
        }
    };

    @XmlElement(name = "Address", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    public Address addr;

    @XmlElement(name = "ReferenceProperties", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    public Elements referenceProperties;

    @XmlElement(name = "ReferenceParameters", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    public Elements referenceParameters;

    @XmlElement(name = MemberSubmissionAddressingConstants.WSA_PORTTYPE_NAME, namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    public AttributedQName portTypeName;

    @XmlElement(name = "ServiceName", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    public ServiceNameType serviceName;

    @XmlAnyAttribute
    public Map<QName, String> attributes;

    @XmlAnyElement
    public List<Element> elements;
    protected static final String MSNS = "http://schemas.xmlsoap.org/ws/2004/08/addressing";

    @XmlType(name = "address", namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    /* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionEndpointReference$Address.class */
    public static class Address {

        @XmlValue
        public String uri;

        @XmlAnyAttribute
        public Map<QName, String> attributes;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionEndpointReference$AttributedQName.class */
    public static class AttributedQName {

        @XmlValue
        public QName name;

        @XmlAnyAttribute
        public Map<QName, String> attributes;
    }

    @XmlType(name = Constants.ATTRNAME_ELEMENTS, namespace = "http://schemas.xmlsoap.org/ws/2004/08/addressing")
    /* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionEndpointReference$Elements.class */
    public static class Elements {

        @XmlAnyElement
        public List<Element> elements;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionEndpointReference$ServiceNameType.class */
    public static class ServiceNameType extends AttributedQName {

        @XmlAttribute(name = MemberSubmissionAddressingConstants.WSA_PORTNAME_NAME)
        public String portName;
    }

    public MemberSubmissionEndpointReference() {
    }

    public MemberSubmissionEndpointReference(@NotNull Source source) {
        if (source == null) {
            throw new WebServiceException("Source parameter can not be null on constructor");
        }
        try {
            Unmarshaller unmarshaller = msjc.get().createUnmarshaller();
            MemberSubmissionEndpointReference epr = (MemberSubmissionEndpointReference) unmarshaller.unmarshal(source, MemberSubmissionEndpointReference.class).getValue();
            this.addr = epr.addr;
            this.referenceProperties = epr.referenceProperties;
            this.referenceParameters = epr.referenceParameters;
            this.portTypeName = epr.portTypeName;
            this.serviceName = epr.serviceName;
            this.attributes = epr.attributes;
            this.elements = epr.elements;
        } catch (ClassCastException e2) {
            throw new WebServiceException("Source did not contain MemberSubmissionEndpointReference", e2);
        } catch (JAXBException e3) {
            throw new WebServiceException("Error unmarshalling MemberSubmissionEndpointReference ", e3);
        }
    }

    @Override // javax.xml.ws.EndpointReference
    public void writeTo(Result result) {
        try {
            Marshaller marshaller = msjc.get().createMarshaller();
            marshaller.marshal(this, result);
        } catch (JAXBException e2) {
            throw new WebServiceException("Error marshalling W3CEndpointReference. ", e2);
        }
    }

    public Source toWSDLSource() {
        Element wsdlElement = null;
        for (Element elem : this.elements) {
            if (elem.getNamespaceURI().equals("http://schemas.xmlsoap.org/wsdl/") && elem.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                wsdlElement = elem;
            }
        }
        return new DOMSource(wsdlElement);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JAXBContext getMSJaxbContext() {
        try {
            return JAXBContext.newInstance(MemberSubmissionEndpointReference.class);
        } catch (JAXBException e2) {
            throw new WebServiceException("Error creating JAXBContext for MemberSubmissionEndpointReference. ", e2);
        }
    }
}
