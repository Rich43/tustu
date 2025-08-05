package javax.xml.ws.wsaddressing;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;

@XmlRootElement(name = "EndpointReference", namespace = "http://www.w3.org/2005/08/addressing")
@XmlType(name = "EndpointReferenceType", namespace = "http://www.w3.org/2005/08/addressing")
/* loaded from: rt.jar:javax/xml/ws/wsaddressing/W3CEndpointReference.class */
public final class W3CEndpointReference extends EndpointReference {
    private final JAXBContext w3cjc = getW3CJaxbContext();
    protected static final String NS = "http://www.w3.org/2005/08/addressing";

    @XmlElement(name = "Address", namespace = "http://www.w3.org/2005/08/addressing")
    private Address address;

    @XmlElement(name = "ReferenceParameters", namespace = "http://www.w3.org/2005/08/addressing")
    private Elements referenceParameters;

    @XmlElement(name = W3CAddressingConstants.WSA_METADATA_NAME, namespace = "http://www.w3.org/2005/08/addressing")
    private Elements metadata;

    @XmlAnyAttribute
    Map<QName, String> attributes;

    @XmlAnyElement
    List<Element> elements;

    protected W3CEndpointReference() {
    }

    public W3CEndpointReference(Source source) {
        try {
            W3CEndpointReference epr = (W3CEndpointReference) this.w3cjc.createUnmarshaller().unmarshal(source, W3CEndpointReference.class).getValue();
            this.address = epr.address;
            this.metadata = epr.metadata;
            this.referenceParameters = epr.referenceParameters;
            this.elements = epr.elements;
            this.attributes = epr.attributes;
        } catch (ClassCastException e2) {
            throw new WebServiceException("Source did not contain W3CEndpointReference", e2);
        } catch (JAXBException e3) {
            throw new WebServiceException("Error unmarshalling W3CEndpointReference ", e3);
        }
    }

    @Override // javax.xml.ws.EndpointReference
    public void writeTo(Result result) {
        try {
            Marshaller marshaller = this.w3cjc.createMarshaller();
            marshaller.marshal(this, result);
        } catch (JAXBException e2) {
            throw new WebServiceException("Error marshalling W3CEndpointReference. ", e2);
        }
    }

    private static JAXBContext getW3CJaxbContext() {
        try {
            return JAXBContext.newInstance(W3CEndpointReference.class);
        } catch (JAXBException e2) {
            throw new WebServiceException("Error creating JAXBContext for W3CEndpointReference. ", e2);
        }
    }

    @XmlType(name = "address", namespace = "http://www.w3.org/2005/08/addressing")
    /* loaded from: rt.jar:javax/xml/ws/wsaddressing/W3CEndpointReference$Address.class */
    private static class Address {

        @XmlValue
        String uri;

        @XmlAnyAttribute
        Map<QName, String> attributes;

        protected Address() {
        }
    }

    @XmlType(name = Constants.ATTRNAME_ELEMENTS, namespace = "http://www.w3.org/2005/08/addressing")
    /* loaded from: rt.jar:javax/xml/ws/wsaddressing/W3CEndpointReference$Elements.class */
    private static class Elements {

        @XmlAnyElement
        List<Element> elements;

        @XmlAnyAttribute
        Map<QName, String> attributes;

        protected Elements() {
        }
    }
}
