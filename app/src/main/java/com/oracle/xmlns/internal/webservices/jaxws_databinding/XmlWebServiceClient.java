package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebServiceClient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-service-client")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebServiceClient.class */
public class XmlWebServiceClient implements WebServiceClient {

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = WSDLConstants.ATTR_TNS)
    protected String targetNamespace;

    @XmlAttribute(name = W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME)
    protected String wsdlLocation;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    public String getWsdlLocation() {
        return this.wsdlLocation;
    }

    public void setWsdlLocation(String value) {
        this.wsdlLocation = value;
    }

    @Override // javax.xml.ws.WebServiceClient
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // javax.xml.ws.WebServiceClient
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.xml.ws.WebServiceClient
    public String wsdlLocation() {
        return Util.nullSafe(this.wsdlLocation);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebServiceClient.class;
    }
}
