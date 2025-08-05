package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebServiceProvider;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-service-provider")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebServiceProvider.class */
public class XmlWebServiceProvider implements WebServiceProvider {

    @XmlAttribute(name = WSDLConstants.ATTR_TNS)
    protected String targetNamespace;

    @XmlAttribute(name = "serviceName")
    protected String serviceName;

    @XmlAttribute(name = "portName")
    protected String portName;

    @XmlAttribute(name = W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME)
    protected String wsdlLocation;

    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String value) {
        this.serviceName = value;
    }

    public String getPortName() {
        return this.portName;
    }

    public void setPortName(String value) {
        this.portName = value;
    }

    public String getWsdlLocation() {
        return this.wsdlLocation;
    }

    public void setWsdlLocation(String value) {
        this.wsdlLocation = value;
    }

    @Override // javax.xml.ws.WebServiceProvider
    public String wsdlLocation() {
        return Util.nullSafe(this.wsdlLocation);
    }

    @Override // javax.xml.ws.WebServiceProvider
    public String serviceName() {
        return Util.nullSafe(this.serviceName);
    }

    @Override // javax.xml.ws.WebServiceProvider
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.xml.ws.WebServiceProvider
    public String portName() {
        return Util.nullSafe(this.portName);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebServiceProvider.class;
    }
}
